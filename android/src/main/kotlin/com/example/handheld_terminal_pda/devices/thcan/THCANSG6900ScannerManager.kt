package com.example.handheld_terminal_pda.devices.thcan

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly
import java.io.IOException

class THCANSG6900ScannerManager private constructor(activity: Context) : IScannerManager {
    private val activity: Context
    private var listener: SupporterAssembly.IScanListener? = null
    private var scanThread: ScanThread? = null

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            if (msg.what == ScanThread.SCAN) {
                val data = msg.data.getString("data")
                if (data != null && data.isNotEmpty()) {
                    listener?.onScannerResultChange(data)
                }
            }
        }
    }

    override fun init() {
        try {
            scanThread = ScanThread(mHandler)
            listener?.onScannerServiceConnected()
        } catch (e: IOException) {
            listener?.onScannerInitFail()
            return
        }
        scanThread!!.start()
    }

    override fun recycle() {
        scanThread?.close()
    }

    override fun setScannerListener(listener: SupporterAssembly.IScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {}
    override fun scannerEnable(enable: Boolean) {}
    override fun setScanMode(mode: String?) {}
    override fun setDataTransferType(type: String?) {}
    override fun singleScan(bool: Boolean) {
        scanThread?.scan()
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
        private var instance: THCANSG6900ScannerManager? = null
        fun getInstance(activity: Context): THCANSG6900ScannerManager? {
            if (instance == null) {
                synchronized(THCANSG6900ScannerManager::class.java) {
                    if (instance == null) {
                        instance = THCANSG6900ScannerManager(activity)
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
