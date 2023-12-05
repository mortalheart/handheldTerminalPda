package com.example.handheld_terminal_pda.devices

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import com.example.handheld_terminal_pda.IScannerManager
import com.example.handheld_terminal_pda.SupporterAssembly
import com.example.handheld_terminal_pda.utils.BaseUtil
import com.seuic.uhf.EPC
import com.seuic.uhf.IReadTagsListener
import com.seuic.uhf.UHFService
import java.util.*
import kotlin.math.log


class RFIDScannerManager private constructor(activity: Context) : IScannerManager {
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var listener: SupporterAssembly.IScanListener? = null
    private val mContext: Context
    private var mDevice: UHFService? = null
    private var continuousQuantity = 1
    private val mIReadTagsListener = IReadTagsListener { epcList ->
        handler.post {
            if (epcList != null) {
                for (epc in epcList) {
                    val epcTage = epc.getId()
                    listener?.onScannerResultChange(epcTage)
                }
            } else {
                mDevice!!.inventoryStop()
            }
        }
    }

    override fun init() {
        mDevice = UHFService.getInstance(mContext)
        val initializationSuccess = mDevice?.open()
        if (initializationSuccess == true) {
            mDevice?.registerReadTags(mIReadTagsListener) // 初始化函数
            initSetting()
            listener?.onScannerServiceConnected()
        }
    }

    private fun initSetting() {
        // 获取固件版本号
        val firmwareVersion = mDevice!!.firmwareVersion
        // 获取温度
        val temperatur = mDevice!!.temperature
    }

    override fun recycle() {
        mDevice!!.inventoryStop()
    }

    override fun setScannerListener(listener: SupporterAssembly.IScanListener) {
        this.listener = listener
    }

    override fun sendKeyEvent(key: KeyEvent?) {}
    override fun scannerEnable(enable: Boolean) {}
    override fun setScanMode(mode: String?) {}
    override fun setDataTransferType(type: String?) {}
    override fun singleScan(bool: Boolean) {
        if (bool && mDevice != null) {
            val epc = EPC()
            val result = mDevice!!.inventoryOnce(epc, 100)
            if (result) {
                val tagData = epc.getId()
                listener?.onScannerResultChange(tagData)
            }
        }
    }

    override fun continuousScan(bool: Boolean) {
        if (bool && mDevice != null) {
            mDevice!!.inventoryStart()
        }
    }

    override fun findingCards(num: Int) {
        continuousQuantity = num
    }

    override fun getRegion() {
//        获取区域 ：”FCC” , ”eTSI”, ”China1” , ”China2
        mDevice!!.region
    }

    override fun setRegion(region: String?) {
        mDevice!!.region = region
    }

    override fun getPower() {
        mDevice!!.power
    }

    override fun setPower(power: Int) {
        mDevice!!.power = power
    }

    override fun writeTagData(epcObj: Any?) {
        if (epcObj is Map<*, *>) {
            val oldEpcID = epcObj["oldEpcID"]
            val newEpcID = epcObj["newEpcID"]

            // 检查是否成功转换
            if (oldEpcID != null && newEpcID != null) {
                println("oldEpcID: $oldEpcID")
                println("newEpcID: $newEpcID")
                val psw = "00000000"
                //                00000000000000000000000000000000
//                int len = newEpcID.length();
                writeTagData( //标签ID，16进制字符串，使用获取标签的接口取得
                    BaseUtil.stringToHexByteArray(oldEpcID as String),  //密码，16进制字符串
                    BaseUtil.stringToHexByteArray(psw),  //标签的存储区（0：密码区 1：EPC区 2：TI区 3：用户区）
                   3,//写入的起始地址，单位为字节
                    0,  //要写入的数据长度，单位为字节
                    4,  //需要写入的数据，16进制字符串
                    BaseUtil.stringToHexByteArray(newEpcID as String)
                )
            } else {
                println("无法将对象转换为 Map<String, String>")
            }
        } else {
            println("Object 不是 Map<String、String 的实例>")
        }
    }

    private fun writeTagData(epc: ByteArray?, password: ByteArray?, bank: Int, offset: Int, len: Int, data: ByteArray? ) {
      val writeSuccess = mDevice!!.writeTagData(epc, password, bank, offset, len, data)
      if (writeSuccess){
          Log.e("成功", "修改成功")
      } else {
          Log.e("失败", "修改失败")
      }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: RFIDScannerManager? = null
        fun getInstance(activity: Context): RFIDScannerManager? {
            if (instance == null) {
                synchronized(RFIDScannerManager::class.java) {
                    if (instance == null) {
                        instance = RFIDScannerManager(activity)
                    }
                }
            }
            return instance
        }
    }

    init {
        mContext = activity
    }
}