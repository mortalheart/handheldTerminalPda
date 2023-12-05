import 'handheld_terminal_pda_platform_interface.dart';

class HandheldTerminalPda {

  Future<String?> getPlatformVersion() {
    return HandheldTerminalPdaPlatform.instance.getPlatformVersion();
  }

  Future<String?> getPdaType() {
    return HandheldTerminalPdaPlatform.instance.getPdaType();
  }

  Future<String?> getSingleScan() {
    return HandheldTerminalPdaPlatform.instance.getSingleScan();
  }

  Future<String?> getMultipleScans() {
    return HandheldTerminalPdaPlatform.instance.getMultipleScans();
  }

  Future<String?> getStopScanning() {
    return HandheldTerminalPdaPlatform.instance.getStopScanning();
  }

  Future<String?> getWrite(String oldEpcID, String newEpcID) {
    return HandheldTerminalPdaPlatform.instance.getWrite(oldEpcID,newEpcID);
  }

  Future<String?> getSendData(list){
    return HandheldTerminalPdaPlatform.instance.getSendData(list);
  }
}
