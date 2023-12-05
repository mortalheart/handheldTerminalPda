package com.example.handheld_terminal_pda.utils

import android.util.Log
import java.io.*


/*
* SerialPort类是JNI类，负责程序与硬件的通信
*/
class SerialPort(port: Int, baudrate: Int, flags: Int) {
    /*
	 * Do not remove or rename the field mFd: it is used by native method close();
	 */
    private val mFd: FileDescriptor?
    private val mFileInputStream: FileInputStream
    private val mFileOutputStream: FileOutputStream
    private var trig_on = false
    lateinit var test: ByteArray

    // Getters and setters
    val inputStream: InputStream
        get() = mFileInputStream

    val outputStream: OutputStream
        get() = mFileOutputStream

    fun power_5Von() {
        zigbeepoweron()
    }

    fun power_5Voff() {
        zigbeepoweroff()
    }

    fun power_3v3on() {
        power3v3on()
    }

    fun power_3v3off() {
        power3v3off()
    }

    fun rfid_poweron() {
        rfidPoweron()
    }

    fun rfid_poweroff() {
        rfidPoweroff()
    }

    fun psam_poweron() {
        psampoweron()
    }

    fun psam_poweroff() {
        psampoweroff()
        //scaner_trigoff();
    }

    fun scaner_poweron() {
        scanerpoweron()
        scaner_trigoff()
    }

    fun scaner_poweroff() {
        scanerpoweroff()
        //scaner_trigoff();
    }

    fun scaner_trigon() {
        scanertrigeron()
        trig_on = true
    }

    fun scaner_trigoff() {
        scanertrigeroff()
        trig_on = false
    }

    fun scaner_trig_stat(): Boolean {
        return trig_on
    }

    external fun close(port: Int)
    external fun zigbeepoweron()
    external fun zigbeepoweroff()
    external fun scanerpoweron()
    external fun scanerpoweroff()
    external fun psampoweron()
    external fun psampoweroff()
    external fun scanertrigeron()
    external fun scanertrigeroff()
    external fun power3v3on()
    external fun power3v3off()
    external fun rfidPoweron()
    external fun rfidPoweroff()
    external fun usbOTGpowerOn()
    external fun usbOTGpowerOff()
    external fun irdapoweron()
    external fun irdapoweroff()

    // Getters and setters
    @JvmName("getInputStream1")
    fun getInputStream(): InputStream {
        return mFileInputStream
    }
    //	public native void setPortParity(int mode); //设置校验位
    external fun test(bytes: ByteArray?)
    @JvmName("getOutputStream1")
    fun getOutputStream(): OutputStream {
        return mFileOutputStream
    }
    companion object {
        private const val TAG = "SerialPort"
        var TNCOM_EVENPARITY = 0 //偶校验
        var TNCOM_ODDPARITY = 1 //奇校验

        //	/**
        //	 * 设置奇偶校验
        //	 * @param mode
        //	 */
        //	public void setPortparity(int mode){
        //		setPortParity(mode);
        //	}
        // JNI
        private external fun open(port: Int, baudrate: Int): FileDescriptor?
        private external fun open(port: Int, baudrate: Int, portparity: Int): FileDescriptor?

        init {
            System.loadLibrary("devapi")
            System.loadLibrary("irdaSerialPort")
        }
    }

    init {
        mFd = open(port, baudrate)
        if (mFd == null) {
            Log.e(TAG, "native open returns null")
            throw IOException()
        }
        mFileInputStream = FileInputStream(mFd)
        mFileOutputStream = FileOutputStream(mFd)
    }
}
