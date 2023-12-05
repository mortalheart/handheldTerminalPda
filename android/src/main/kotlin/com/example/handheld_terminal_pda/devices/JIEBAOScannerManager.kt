package com.example.handheld_terminal_pda.devices

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly


 class JIEBAOScannerManager private constructor(private val activity: Context) : IScannerManager {
     private val broadcast: String = "com.android.server.scannerservice.broadcast"
     private val data: String = "scannerdata"
     private var listener: SupporterAssembly.IScanListener? = null

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val code: String? = intent.getStringExtra(data)
            if (code != null) {
                if (code.isNotEmpty()) {
                    listener?.onScannerResultChange(code)
                }
            }
        }
    }

    override fun init() {
        registerReceiver()
        listener?.onScannerServiceConnected()
    }

    override fun recycle() {}

    override fun setScannerListener(listener: SupporterAssembly.IScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {}

    override fun scannerEnable(enable: Boolean) {}

    override fun setScanMode(mode: String?) {}

    override fun setDataTransferType(type: String?) {}

    override fun singleScan(bool: Boolean) {}

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
        intentFilter.addAction(broadcast)
        activity.registerReceiver(receiver, intentFilter)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: JIEBAOScannerManager? = null
        fun getInstance(activity: Context): JIEBAOScannerManager? {
            if (instance == null) {
                synchronized(JIEBAOScannerManager::class.java) {
                    if (instance == null) {
                        instance = JIEBAOScannerManager(activity)
                    }
                }
            }
            return instance
        }
    }
}