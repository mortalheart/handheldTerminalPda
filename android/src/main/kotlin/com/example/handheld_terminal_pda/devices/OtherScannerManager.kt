package com.example.handheld_terminal_pda.devices

import android.content.Context
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly


class OtherScannerManager internal constructor(context: Context) : IScannerManager {
    private val mContext: Context
    private var listener: SupporterAssembly.IScanListener? = null
    override fun init() {
        listener?.onScannerInitFail()
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
    override fun findingCards(num: Int) {}
    override fun getRegion() {}
    override fun setRegion(region: String?) {}
    override fun getPower() {}
    override fun setPower(power: Int) {}
    override fun writeTagData(epcObj: Any?) {}

    init {
        mContext = context
    }
}