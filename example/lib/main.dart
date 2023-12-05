import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:handheld_terminal_pda/handheld_terminal_pda.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String _pdaVersion = 'Unknown';
  List list = [];
  final _handheldTerminalPdaPlugin = HandheldTerminalPda();
  @override
  void initState() {
    super.initState();
    initPlatformState();

  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    late String pdaType;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _handheldTerminalPdaPlugin.getPlatformVersion() ?? 'Unknown platform version';
       pdaType = await _handheldTerminalPdaPlugin.getPdaType() ?? 'Unknown platform Type';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
      _pdaVersion = pdaType;
    });
  }
  void singleScan() async {
    final single = await _handheldTerminalPdaPlugin.getSingleScan();
    print("单次扫描结果:$single");
  }

  void continuousScan() async {
    final continuous = await _handheldTerminalPdaPlugin.getMultipleScans();
    print("连续扫描:$continuous");
  }

  void onDestroyScan() async {
    final onDestroy = await _handheldTerminalPdaPlugin.getStopScanning();
    print("停止连续扫描:$onDestroy");
  }

  void clickWrite(list) async {
    final onDestroy = await _handheldTerminalPdaPlugin.getWrite(list,'12345678910');
    print("开始写入标签:$onDestroy");
  }

  void onSendData() async {
    final sendDataList =[{
      "title": "桌子",
      "status": "0",
      "create": "2023-11-10 11:31:00",
      "img": "https://cn.bing.com/images/search?view=detailV2&ccid=HIzE3QZ1&id=6FAE60BF454ABE61824FD854C9823DF9C8C7A470&thid=OIP.HIzE3QZ1MQ8Bh9AixT5TngHaHa&mediaurl=https%3A%2F%2Fm.360buyimg.com%2Fmobilecms%2Fs750x750_jfs%2Ft1%2F133307%2F26%2F1041%2F425624%2F5ed4ae4aE692d6ff1%2Ffd848401de3942a6.jpg!q80.dpg&exph=750&expw=750&q=%e6%a1%8c%e5%ad%90&simid=608054244575770720&form=IRPRST&ck=D7E13F7393DE4A4FE3D99AD793193FC0&selectedindex=2&ajaxhist=0&ajaxserp=0&vt=0&sim=11",
      "usage": "0",
      "numbering": "000001",
      "purchaser": "李四",
      "purchaseApprove": "赵主管",
      "vendor": "鑫星科技有限责任公司",
      "assetType": "办公室",
      "brand": "北美胡桃木",
      "RFID":"0000000000000000000000000000"
    }];
    print("发送数据:$sendDataList");
    final sendData = await _handheldTerminalPdaPlugin.getSendData(sendDataList);
    print("$sendData");
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              Text(_platformVersion),
              Text(_pdaVersion),

              ElevatedButton(
                onPressed: () {
                  singleScan();
                },
                child: const Text('单次扫描'),
              ),
              ElevatedButton(
                onPressed: () {
                  continuousScan();
                },
                child: const Text('多次扫描'),
              ),
              ElevatedButton(
                onPressed: () {
                  onDestroyScan();
                },
                child: const Text('停止扫描'),
              ),
              ElevatedButton(
                onPressed: () {
                  onSendData();
                },
                child: const Text('发送数据'),
              ),
              const SizedBox(
                height: 50,
              ),
              Expanded(
                child: ListView.builder(
                  shrinkWrap: true,
                  scrollDirection: Axis.vertical,
                  itemCount: list.length,
                  itemBuilder: (BuildContext context, int index) {
                    return InkWell(
                      onTap: () => clickWrite(list[index]),
                      child: Container(
                        width: 150,
                        height: 40,
                        color: Colors.blue,
                        margin: const EdgeInsets.all(10),
                        child: Center(
                          child: Text('${list[index]}'),
                        ),
                      ),
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
