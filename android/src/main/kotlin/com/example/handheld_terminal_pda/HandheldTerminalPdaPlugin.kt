package com.example.handheld_terminal_pda
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.util.*

open class HandheldTerminalPdaPlugin : FlutterPlugin, MethodCallHandler {
  private var context: Context? = null
  private lateinit var mScannerManager: SupporterAssembly<IScannerManager>
  private lateinit var channel: MethodChannel
  private val establishAChannel = "com.handheld_terminal_pda.MethodChannel"
  private val scanResults: HashSet<String> = HashSet()
  private var vendorsAreNotSupported: Boolean = true

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, establishAChannel)
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  @RequiresApi(Build.VERSION_CODES.KITKAT)
  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "getPlatformVersion" -> result.success("Android " + Build.VERSION.RELEASE)
      "PDA" -> {
        if (Build.MANUFACTURER == "Google"){
           vendorsAreNotSupported = false
          result.success("厂商通道不支持 " + Build.MANUFACTURER)
        } else {
          initScanner()
          result.success("型号" + Build.MODEL + "，厂商" + Build.MANUFACTURER)
          Log.e("TAG", "型号" + Build.MODEL + "，厂商" + Build.MANUFACTURER);
        }
      }
      "singleScan" -> handleSingleScan(result)
      "multipleScans" -> handleMultipleScans(result)
      "Scanning" -> handleScanning(result)
      "Write" -> handleWrite(call.arguments<Any>() as Objects, result)
      "sendData" -> handleSendData(call.arguments<Any>() as List<*>,result)
      else -> result.notImplemented()
    }
  }

  private fun handleSendData(list: List<*>, result: MethodChannel.Result) {
    Log.d("接收发送的数据", "handleSendData: $list")
  }

  private fun initScanner() {
    context?.let {
      mScannerManager = SupporterAssembly(it, object : SupporterAssembly.IScanListener {
        override fun onScannerResultChange(result: String) {
          scanResults.add(result) // 将扫描结果添加到集合中
          // 当有新的结果时，去重并返回给Flutter端
          sendUniqueScanResultsToFlutter()
        }

        override fun onScannerServiceConnected() {
          Log.e("\n", "初始化成功")
        }

        override fun onScannerInitFail() {
          Log.e("\n", "无法获取扫描头")
        }
      })
    }
  }

  private fun sendUniqueScanResultsToFlutter() {
    val uniqueResults: List<String> = ArrayList(scanResults) // 转换为List去重
    Log.d("返回结果:", uniqueResults.toString())
    channel.invokeMethod("onUniqueScanResults", uniqueResults)
  }

  // 用于处理各种方法调用的其他方法

  private fun handleSingleScan(result: Result) {
    // 当有新的结果时，去重并返回给Flutter端
    if (::mScannerManager.isInitialized && vendorsAreNotSupported) {
      mScannerManager.singleScan(true)
    val uniqueResults: List<String> = ArrayList(scanResults) // 转换为List去重
      result.success(uniqueResults.toString())
    } else {
      result.error("SCANNER_NOT_INITIALIZED", "Scanner not initialized", null)
    }
  }

  private fun handleMultipleScans(result: Result) {
    if (::mScannerManager.isInitialized && vendorsAreNotSupported) {
      mScannerManager.continuousScan(true)
//      result.success("Scan is off")
      val uniqueResults: List<String> = ArrayList(scanResults) // 转换为List去重
      result.success(uniqueResults.toString())
    } else {
      result.error("SCANNER_NOT_INITIALIZED", "Scanner not initialized", null)
    }
  }

  private fun handleScanning(result: Result) {
    if (::mScannerManager.isInitialized && vendorsAreNotSupported) {
      mScannerManager.recycle()
      result.success("Prepare to write data")
    } else {
      result.error("SCANNER_NOT_INITIALIZED", "Scanner not initialized", null)
    }
  }

  private fun handleWrite(arguments: Objects,result: Result) {
    if (::mScannerManager.isInitialized && vendorsAreNotSupported) {
      mScannerManager.writeTagData(arguments)
      val uniqueResults: List<String> = ArrayList(scanResults) // 转换为List去重
      result.success(uniqueResults.toString())
//      result.success("Scan is off")
    } else {
      result.error("SCANNER_NOT_INITIALIZED", "Scanner not initialized", null)
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

}