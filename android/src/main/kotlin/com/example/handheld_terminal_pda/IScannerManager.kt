package com.example.handheld_terminal_pda

import android.view.KeyEvent


interface IScannerManager {

    fun init()
    fun recycle()
    fun setScannerListener(listener: SupporterAssembly.IScanListener)
    fun sendKeyEvent(key: KeyEvent?)
    fun scannerEnable(enable: Boolean)
    fun setScanMode(mode: String?)
    fun setDataTransferType(type: String?)
    fun singleScan(bool: Boolean)
    fun continuousScan(bool: Boolean)
    fun findingCards(num: Int)
    fun getRegion()
    fun setRegion(region: String?)
    fun getPower()
    fun setPower(power: Int)
    fun writeTagData(epcObj: Any?)
}