package com.example.handheld_terminal_pda

import android.content.Context
import android.os.Build
import com.example.handheld_terminal_pda.devices.*
import com.example.handheld_terminal_pda.devices.thcan.THCANSG6900ScannerManager


class SupporterAssembly<T : IScannerManager>( val context: Context, private val iScanListener: IScanListener) {
    private  var scannerManager: T

    enum class ScannerSupporter( val title: String) {
        PDA("PDA"), AUTOID("AUTOID"), KU8905("KU-8905"),
        JieNaoHT380K("HT380K"),SUNMI("SUNMI"),SEUIC("SEUIC"),
        UBX("UBX"),SG6900("SG6900"),ALPS("alps"), IDATA("idata"),
        NFC("NFC");
    }

    private fun getSupporter(): ScannerSupporter {
        for (supporter in ScannerSupporter.values()) {
            if (supporter.title == Build.MODEL || supporter.title == Build.MANUFACTURER) {
                return supporter
            }
        }

        return try {
            ScannerSupporter.valueOf(getSupporter().title)
        } catch (e: Exception) {
            ScannerSupporter.NFC
        }
    }

     init {
        val scannerSupporter = getSupporter()
        scannerManager = when (scannerSupporter) {
            ScannerSupporter.PDA -> PDAScannerManager.getInstance(context) as? T
            ScannerSupporter.AUTOID, ScannerSupporter.KU8905 -> RFIDScannerManager.getInstance(context) as? T
            ScannerSupporter.JieNaoHT380K -> JIEBAOScannerManager.getInstance(context) as? T
            ScannerSupporter.SUNMI -> SunmiScannerManager.getInstance() as? T
            ScannerSupporter.SEUIC -> SEUICScannerManager.getInstance(context) as? T
            ScannerSupporter.UBX -> UBXScannerManager.getInstance(context) as? T
            ScannerSupporter.SG6900 -> THCANSG6900ScannerManager.getInstance(context) as? T
            ScannerSupporter.ALPS -> AlpsScannerManager.getInstance(context) as? T
            ScannerSupporter.IDATA -> IDataScannerManager.getInstance(context) as? T
            ScannerSupporter.NFC -> NFCScannerManager.getInstance(context) as? T
        } ?: throw IllegalStateException("无法初始化扫描程序管理器")
         scannerManager.apply {
             setScannerListener(iScanListener)
             scannerManager.init()
         }
    }

     fun singleScan(b: Boolean) {
         scannerManager.singleScan(b)
     }

     fun recycle() {
         scannerManager.recycle()
     }

     fun continuousScan(b: Boolean) {
         scannerManager.continuousScan(b)
     }

     fun writeTagData(arguments: Any) {
         scannerManager.writeTagData(arguments)
     }

    interface IScanListener {
        fun onScannerResultChange(result: String)
        fun onScannerServiceConnected()
        fun onScannerInitFail()
    }
}
