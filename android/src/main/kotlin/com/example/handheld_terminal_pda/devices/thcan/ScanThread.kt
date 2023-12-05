package com.example.handheld_terminal_pda.devices.thcan

import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.handheld_terminal_pda.utils.SerialPort
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ScanThread(handler: Handler) : Thread() {
    private val mSerialPort: SerialPort?
    private val `is`: InputStream?
    private val os: OutputStream?

    /* seaport parameter */
    private val port = 0
    private val baudrate = 9600
    private val flags = 0
    private val handler: Handler
    override fun run() {
        try {
            var size = 0
            val buffer = ByteArray(2048)
            var available = 0
            while (!isInterrupted) {
                available = (`is`?.available() ?: if (available > 0) {
                    size = (`is`?.read(buffer) ?: if (size <= 0) {
                    } else {
                        sendMessege(buffer, size, SCAN)
                    }) as Int
                } else {
                    sendMessege(buffer, size, SCAN)
                }) as Int
            }
        } catch (e: IOException) {
            // 返回错误信息
            e.printStackTrace()
        }
        super.run()
    }

    private fun sendMessege(data: ByteArray, dataLen: Int, mode: Int) {
        try {
            val dataStr = String(data, 0, dataLen)
            val bundle = Bundle()
            bundle.putString("data", dataStr)
            val msg = Message()
            msg.what = mode
            msg.data = bundle
            handler.sendMessage(msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun scan() {
        if (mSerialPort?.scaner_trig_stat() == true) {
            mSerialPort.scaner_trigoff()
            try {
                sleep(50)
            } catch (e: InterruptedException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        mSerialPort?.scaner_trigon()
    }

    fun close() {
        if (mSerialPort != null) {
            mSerialPort.scaner_poweroff()
            try {
                `is`?.close()
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mSerialPort.close(port)
        }
    }

    companion object {
        var SCAN = 1001
    }

    init {
        this.handler = handler
        mSerialPort = SerialPort(port, baudrate, flags)
        mSerialPort.scaner_poweron()
        `is` = mSerialPort.getInputStream()
        os = mSerialPort.getOutputStream()
        try {
            sleep(500)
        } catch (e: InterruptedException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        /** clear useless data  */
        val temp = ByteArray(128)
        `is`.read(temp)
    }
}

