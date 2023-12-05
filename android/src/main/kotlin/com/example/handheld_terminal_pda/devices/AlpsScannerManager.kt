package com.example.handheld_terminal_pda.devices

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly
import com.zltd.industry.ScannerManager
import com.zltd.industry.ScannerManager.IScannerStatusListener
import java.io.UnsupportedEncodingException


class AlpsScannerManager private constructor(activity: Context) : IScannerManager {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val activity: Context
    private var mScannerManager: ScannerManager? = null
    private var listener: SupporterAssembly.IScanListener? = null
    private val mIScannerStatusListener: IScannerStatusListener = object : IScannerStatusListener {
        override fun onScannerStatusChanage(i: Int) {}
        override fun onScannerResultChanage(bytes: ByteArray) {
            handler.post {
                var s: String? = null
                try {
                    s = String(bytes)
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                if (s != null && listener != null) {
                    listener!!.onScannerResultChange(s)
                }
            }
        }
    }

    override fun init() {
        mScannerManager = ScannerManager.getInstance()
        mScannerManager?.scannerEnable(true)
        mScannerManager?.scannerEnable(true)
        mScannerManager?.scanMode = ScannerManager.SCAN_SINGLE_MODE
        mScannerManager?.dataTransferType = ScannerManager.TRANSFER_BY_API
        mScannerManager?.addScannerStatusListener(mIScannerStatusListener)
    }

    override fun recycle() {
        if (mScannerManager != null) {
            mScannerManager!!.removeScannerStatusListener(mIScannerStatusListener)
        }
    }

    override fun setScannerListener(listener: SupporterAssembly.IScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {}
    override fun scannerEnable(enable: Boolean) {
        mScannerManager!!.scannerEnable(enable)
    }

    override fun setScanMode(mode: String?) {
        when (mode) {
            "single" -> mScannerManager!!.scanMode = ScannerManager.SCAN_SINGLE_MODE
            "continuous" -> mScannerManager!!.scanMode = ScannerManager.SCAN_CONTINUOUS_MODE
            "keyHold" -> mScannerManager!!.scanMode = ScannerManager.SCAN_KEY_HOLD_MODE
            else -> {}
        }
    }

    override fun setDataTransferType(type: String?) {
        when (type) {
            "api" -> mScannerManager!!.dataTransferType = ScannerManager.TRANSFER_BY_API
            "editText" -> mScannerManager!!.dataTransferType = ScannerManager.TRANSFER_BY_EDITTEXT
            "key" -> mScannerManager!!.dataTransferType = ScannerManager.TRANSFER_BY_KEY
            else -> {}
        }
    }

    override fun singleScan(bool: Boolean) {
        if (bool) {
            mScannerManager!!.startKeyHoldScan()
        } else {
            mScannerManager!!.stopKeyHoldScan()
        }
    }

    override fun continuousScan(bool: Boolean) {
        if (bool) {
            mScannerManager!!.startContinuousScan()
        } else {
            mScannerManager!!.stopContinuousScan()
        }
    }

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
        private var instance: AlpsScannerManager? = null
        fun getInstance(activity: Context): AlpsScannerManager? {
            if (instance == null) {
                synchronized(AlpsScannerManager::class.java) {
                    if (instance == null) {
                        instance = AlpsScannerManager(activity)
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
