package com.example.handheld_terminal_pda.devices

import android.annotation.SuppressLint
import android.content.Context
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly

class NFCScannerManager private constructor(private val activity: Context) : IScannerManager {
    private var listener: SupporterAssembly.IScanListener? = null
    override fun init() {
        TODO("Not yet implemented")
    }

    override fun recycle() {
        TODO("Not yet implemented")
    }

    override fun setScannerListener(listener: SupporterAssembly.IScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {
        TODO("Not yet implemented")
    }

    override fun scannerEnable(enable: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setScanMode(mode: String?) {
        TODO("Not yet implemented")
    }

    override fun setDataTransferType(type: String?) {
        TODO("Not yet implemented")
    }

    override fun singleScan(bool: Boolean) {
        TODO("Not yet implemented")
    }

    override fun continuousScan(bool: Boolean) {
        TODO("Not yet implemented")
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
        private var instance: NFCScannerManager? = null
        fun getInstance(activity: Context): NFCScannerManager? {
            if (instance == null) {
                synchronized(NFCScannerManager::class.java) {
                    if (instance == null) {
                        instance = NFCScannerManager(activity)
                    }
                }
            }
            return instance
        }
    }
}