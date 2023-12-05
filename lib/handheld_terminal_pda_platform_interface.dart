import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'handheld_terminal_pda_method_channel.dart';

abstract class HandheldTerminalPdaPlatform extends PlatformInterface {
  /// Constructs a HandheldTerminalPdaPlatform.
  HandheldTerminalPdaPlatform() : super(token: _token);

  static final Object _token = Object();

  static HandheldTerminalPdaPlatform _instance = MethodChannelHandheldTerminalPda();

  /// The default instance of [HandheldTerminalPdaPlatform] to use.
  ///
  /// Defaults to [MethodChannelHandheldTerminalPda].
  static HandheldTerminalPdaPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [HandheldTerminalPdaPlatform] when
  /// they register themselves.
  static set instance(HandheldTerminalPdaPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
  Future<String?> getPdaType() {
    throw UnimplementedError('getPdaType() has not been implemented.');
  }

  Future<String?> getSingleScan() {
    throw UnimplementedError('getSingleScan() has not been implemented.');
  }

  Future<String?> getStopScanning() {
    throw UnimplementedError('getStopScanning() has not been implemented.');
  }

  Future<String?> getMultipleScans() {
    throw UnimplementedError('getMultipleScans() has not been implemented.');
  }

  Future<String?> getWrite(String oldEpcID, String newEpcID) {
    throw UnimplementedError('getWrite() has not been implemented.');
  }

  Future<String?> getSendData(list) {
    throw UnimplementedError('getSendData() has not been implemented.');
  }

}
