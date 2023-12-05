package com.example.handheld_terminal_pda.devices

import android.annotation.SuppressLint
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly


class SunmiScannerManager private constructor() : IScannerManager {

    override fun init() {
        TODO("Not yet implemented")
    }

    override fun recycle() {
        TODO("Not yet implemented")
    }

    override fun setScannerListener(listener: SupporterAssembly.IScanListener) {
        TODO("Not yet implemented")
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
        private var instance: SunmiScannerManager? = null
        fun getInstance(): SunmiScannerManager? {
            if (instance == null) {
                synchronized(SunmiScannerManager::class.java) {
                    if (instance == null) {
                        instance = SunmiScannerManager()
                    }
                }
            }
            return instance
        }
    }
}
