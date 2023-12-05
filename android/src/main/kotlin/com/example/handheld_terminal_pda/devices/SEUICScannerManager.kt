package com.example.handheld_terminal_pda.devices

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly
import com.seuic.scanner.DecodeInfoCallBack
import com.seuic.scanner.Scanner
import com.seuic.scanner.ScannerFactory
import com.seuic.scanner.ScannerKey


 class SEUICScannerManager private constructor(activity: Context) : IScannerManager {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val activity: Context
    private var mScanner: Scanner? = null
    private var listener: SupporterAssembly.IScanListener? = null
    private val mDecodeInfoCallBack =
        DecodeInfoCallBack { decodeInfo ->
            handler.post {
                val s = decodeInfo.barcode
                if (s != null) {
                    listener?.onScannerResultChange(s)
                }
            }
        }

    override fun init() {
        mScanner = ScannerFactory.getScanner(activity)
        val isOpen: Boolean = mScanner?.open() == true
        if (isOpen) {
            listener?.onScannerServiceConnected()
        } else {
            listener?.onScannerInitFail()
        }
        mScanner?.setDecodeInfoCallBack(mDecodeInfoCallBack)
        Thread {
            val ret1 = ScannerKey.open()
            if (ret1 > -1) {
                while (true) {
                    val ret = ScannerKey.getKeyEvent()
                    if (ret > -1) {
                        when (ret) {
                            ScannerKey.KEY_DOWN -> mScanner?.startScan()
                            ScannerKey.KEY_UP -> mScanner?.stopScan()
                        }
                    }
                }
            }
        }.start()
    }

    override fun recycle() {
        mScanner?.close()
        ScannerKey.close()
    }

    override fun setScannerListener(listener: SupporterAssembly.IScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {}
    override fun scannerEnable(enable: Boolean) {
        if (enable) {
            mScanner?.enable()
        } else {
            mScanner?.disable()
        }
    }

    override fun setScanMode(mode: String?) {}
    override fun setDataTransferType(type: String?) {}
    override fun singleScan(bool: Boolean) {
        if (bool) {
            mScanner?.startScan()
        } else {
            mScanner?.stopScan()
        }
    }

    override fun continuousScan(bool: Boolean) {}
     override fun findingCards(num: Int) {
         TODO("Not yet implemented")
     }

     override fun getRegion() {
         TODO("Not yet implemented")
     }

     override fun setRegion(region: String?) {
         TODO("Not yet implemented")
     }

     override fun getPower() {
         TODO("Not yet implemented")
     }

     override fun setPower(power: Int) {
         TODO("Not yet implemented")
     }

     override fun writeTagData(epcObj: Any?) {
         TODO("Not yet implemented")
     }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SEUICScannerManager? = null
        fun getInstance(activity: Context): SEUICScannerManager? {
            if (instance == null) {
                synchronized(SEUICScannerManager::class.java) {
                    if (instance == null) {
                        instance = SEUICScannerManager(activity)
                    }
                }
            }
            return instance
        }
    }

    init {
        this.activity = activity
    }
}