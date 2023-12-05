import 'package:flutter_test/flutter_test.dart';
import 'package:handheld_terminal_pda/handheld_terminal_pda.dart';
import 'package:handheld_terminal_pda/handheld_terminal_pda_platform_interface.dart';
import 'package:handheld_terminal_pda/handheld_terminal_pda_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockHandheldTerminalPdaPlatform
    with MockPlatformInterfaceMixin
    implements HandheldTerminalPdaPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<String?> getMultipleScans() {
    // TODO: implement getMultipleScans
    throw UnimplementedError();
  }

  @override
  Future<String?> getPdaType() {
    // TODO: implement getPdaType
    throw UnimplementedError();
  }

  @override
  Future<String?> getSingleScan() {
    // TODO: implement getSingleScan
    throw UnimplementedError();
  }

  @override
  Future<String?> getStopScanning() {
    // TODO: implement getStopScanning
    throw UnimplementedError();
  }

  @override
  Future<String?> getWrite(String oldEpcID, String newEpcID) {
    // TODO: implement getWrite
    throw UnimplementedError();
  }

  @override
  Future<String?> getSendData(list) {
    // TODO: implement getSendData
    throw UnimplementedError();
  }

}

void main() {
  final HandheldTerminalPdaPlatform initialPlatform = HandheldTerminalPdaPlatform.instance;

  test('$MethodChannelHandheldTerminalPda is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelHandheldTerminalPda>());
  });

  test('getPlatformVersion', () async {
    HandheldTerminalPda handheldTerminalPdaPlugin = HandheldTerminalPda();
    MockHandheldTerminalPdaPlatform fakePlatform = MockHandheldTerminalPdaPlatform();
    HandheldTerminalPdaPlatform.instance = fakePlatform;

    expect(await handheldTerminalPdaPlugin.getPlatformVersion(), '42');
  });
}
