import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'handheld_terminal_pda_platform_interface.dart';

/// An implementation of [HandheldTerminalPdaPlatform] that uses method channels.
class MethodChannelHandheldTerminalPda extends HandheldTerminalPdaPlatform {
  /// The method channel used to interact with the native platform.
  List list = [];
  @visibleForTesting
  final methodChannel = const MethodChannel('com.handheld_terminal_pda.MethodChannel');
  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
  @override
  Future<String?> getPdaType() async {
    final pdaType = await methodChannel.invokeMethod<String>('PDA');
    return pdaType;
  }

  /// 单次扫描
  @override
  Future<String?> getSingleScan() async {
   final singleScan = await methodChannel.invokeMethod<String>('singleScan');
    return singleScan;
  }

  /// 多次扫描
  @override
  Future<String?> getMultipleScans() async {
    final multipleScans = await methodChannel.invokeMethod<String>('multipleScans');
    return multipleScans;
  }

  @override
  Future<String?> getStopScanning() async {
    final scanningScans = await methodChannel.invokeMethod<String>('Scanning');
    return scanningScans;
  }

  @override
  Future<String?> getWrite(String oldEpcID, String newEpcID) async{
    final write = await methodChannel.invokeMethod<String>('Write',{
      "oldEpcID":oldEpcID,
      "newEpcID":newEpcID
    });
    return write;
  }

  @override
  Future<String?> getSendData(list) async{
    final sendData = await methodChannel.invokeMethod<String>('sendData',list);
    return sendData;
  }
}
