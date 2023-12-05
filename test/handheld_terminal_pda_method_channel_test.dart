import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:handheld_terminal_pda/handheld_terminal_pda_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelHandheldTerminalPda platform = MethodChannelHandheldTerminalPda();
  const MethodChannel channel = MethodChannel('handheld_terminal_pda');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
