package com.example.handheld_terminal_pda
import android.content.Context
import android.os.Build
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
          result.success("不支持")
        } else {
          initScanner()
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
  }

  private fun initScanner() {
    context?.let {
      mScannerManager = SupporterAssembly(it, object : SupporterAssembly.IScanListener {
        override fun onScannerResultChange(result: String) {
          scanResults.add(result) // 将扫描结果添加到集合中
          // 当有新的结果时，去重并返回给Flutter端
          sendUniqueScanResultsToFlutter()
        }
// 初始化成功
        override fun onScannerServiceConnected() {

        }
//        初始化失败
        override fun onScannerInitFail() {

        }
      })
    }
  }

  private fun sendUniqueScanResultsToFlutter() {
    val uniqueResults: List<String> = ArrayList(scanResults) // 转换为List去重
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
      result.success("单次扫描失败")
    }
  }

  private fun handleMultipleScans(result: Result) {
    if (::mScannerManager.isInitialized && vendorsAreNotSupported) {
      mScannerManager.continuousScan(true)
//      result.success("Scan is off")
      val uniqueResults: List<String> = ArrayList(scanResults) // 转换为List去重
      result.success(uniqueResults.toString())
    } else {
      result.success("连续扫描失败")
    }
  }

  private fun handleScanning(result: Result) {
    if (::mScannerManager.isInitialized && vendorsAreNotSupported) {
      mScannerManager.recycle()
      result.success("Prepare to write data")
    } else {
      result.success("重置失败")
    }
  }

  private fun handleWrite(arguments: Objects,result: Result) {
    if (::mScannerManager.isInitialized && vendorsAreNotSupported) {
      mScannerManager.writeTagData(arguments)
      val uniqueResults: List<String> = ArrayList(scanResults) // 转换为List去重
      result.success(uniqueResults.toString())
//      result.success("Scan is off")
    } else {
      result.success("修改失败")
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

}