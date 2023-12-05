package com.example.handheld_terminal_pda.devices

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanDevice
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly


class PDAScannerManager private constructor(private val mContext: Context) : IScannerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var listener: SupporterAssembly.IScanListener? = null
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            handler.post {
                val broadCode = intent.getByteArrayExtra("barocode")
                val code = String(broadCode!!)
                if (code.isNotEmpty()) {
                    listener?.onScannerResultChange(code)
                }
            }
        }
    }
    private var mScanDevice: ScanDevice? = null
    override fun init() {
        mScanDevice = ScanDevice()
        mScanDevice!!.openScan()
        mScanDevice!!.outScanMode = 0
        registerReceiver()
        listener?.onScannerServiceConnected()
    }

    override fun recycle() {
        mScanDevice!!.scanLaserMode = 8
        mScanDevice!!.resetScan()
        mScanDevice!!.stopScan()
    }

    override fun setScannerListener(listener: SupporterAssembly.IScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {}
    override fun scannerEnable(enable: Boolean) {}
    override fun setScanMode(mode: String?) {}
    override fun setDataTransferType(type: String?) {}
    override fun singleScan(bool: Boolean) {
        mScanDevice!!.scanLaserMode = 8
        mScanDevice!!.startScan()
    }

    override fun continuousScan(bool: Boolean) {
        mScanDevice!!.scanLaserMode = 4
        mScanDevice!!.startScan()
        Log.e("获取当前连接状态", "continuousScan: " + mScanDevice!!.scanLaserMode)
    }

    override fun findingCards(num: Int) {}
    override fun getRegion() {}
    override fun setRegion(region: String?) {}
    override fun getPower() {}
    override fun setPower(power: Int) {}
    override fun writeTagData(epcObj: Any?) {}
    private fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_DATA_CODE_RECEIVED)
        mContext.registerReceiver(receiver, intentFilter)
    }

    companion object {
        private const val ACTION_DATA_CODE_RECEIVED = "scan.rcv.message"
        @SuppressLint("StaticFieldLeak")
        private var instance: PDAScannerManager? = null
        fun getInstance(activity: Context): PDAScannerManager? {
            if (instance == null) {
                synchronized(PDAScannerManager::class.java) {
                    if (instance == null) {
                        instance = PDAScannerManager(activity)
                    }
                }
            }
            return instance
        }
    }
}
