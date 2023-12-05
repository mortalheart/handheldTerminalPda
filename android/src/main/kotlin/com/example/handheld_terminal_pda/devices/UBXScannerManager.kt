package com.example.handheld_terminal_pda.devices

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly


 class UBXScannerManager private constructor(activity: Context) : IScannerManager {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val activity: Context
    private var listener: SupporterAssembly.IScanListener? = null
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            handler.post {
                val code: String? = intent.getStringExtra(ScanManager.BARCODE_STRING_TAG)
                if (code != null) {
                    if (code.isNotEmpty()) {
                        listener?.onScannerResultChange(code)
                    }
                }
            }
        }
    }
    private var mScanManager: ScanManager? = null
    override fun init() {
        mScanManager = ScanManager()
        val b = mScanManager!!.openScanner()
        if (b) {
            listener?.onScannerServiceConnected()
        } else {
            listener?.onScannerInitFail()
        }
        registerReceiver()
    }

    override fun recycle() {
        mScanManager!!.closeScanner()
        activity.unregisterReceiver(receiver)
        listener = null
    }

    override fun setScannerListener(listener: SupporterAssembly.IScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {}
    override fun scannerEnable(enable: Boolean) {}
    override fun setScanMode(mode: String?) {}
    override fun setDataTransferType(type: String?) {}
    override fun singleScan(bool: Boolean) {
        if (bool) {
            mScanManager!!.startDecode()
        } else {
            mScanManager!!.stopDecode()
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
    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED)
        activity.registerReceiver(receiver, intentFilter)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: UBXScannerManager? = null
        const val ACTION_DATA_CODE_RECEIVED = "android.intent.ACTION_DECODE_DATA"
        fun getInstance(activity: Context): UBXScannerManager? {
            if (instance == null) {
                synchronized(UBXScannerManager::class.java) {
                    if (instance == null) {
                        instance = UBXScannerManager(activity)
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
