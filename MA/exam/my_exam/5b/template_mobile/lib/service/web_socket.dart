import 'package:template_mobile/common/utilities.dart';
import 'package:web_socket_channel/web_socket_channel.dart';

import 'network.dart';

class WebSocketConnection {
  static final WebSocketConnection instance = WebSocketConnection._init();
  static WebSocketChannel? _channel = init();
  static bool isConnected = false;
  static late Function? _onConnectionChange;
  static var _connectivity = NetworkConnectivity.instance;

  WebSocketConnection._init();

  static WebSocketChannel init() {
    // if (isConnected == true) {
    //   return _channel!;
    // }

    try {
      var connection =
          WebSocketChannel.connect(Uri.parse("ws://${Utilities.serverIpAndPort}"));
      connection.ready.then((value) {
        _connectionChange(true);
        isConnected = true;
      });
      return connection;
    } catch (e) {
      print("Error can not connect to web socket: $e");
      isConnected = false;
      _connectionChange(false);
      return init();
    }
  }

  static void _connectionChange(bool status) {
    if (_onConnectionChange == null) {
      return;
    }

    _onConnectionChange!(status);
    if (status == false) {
      init();
    }
  }

  void listen(Function onReceive, Function onDone, Function onError,
      Function onConnectionChange) async {
    _onConnectionChange = onConnectionChange;
    _connectionChange(isConnected);
    // if (_channel == null || (await _channel!.stream.isEmpty) == false) {
    //   return;
    // }
    _channel?.stream.listen((event) {
      print(event);
      isConnected = true;
      _connectionChange(true);
      onReceive(event);
    }, onDone: () async {
      print("connection aborted");
      isConnected = false;
      _connectionChange(false);
      _channel = init();
      await Future.delayed(Duration(seconds: 10));
      listen(onReceive, onDone, onError, onConnectionChange);
      onDone();
    }, onError: (e) {
      print("Server error: $e");
      _channel = init();
      _connectionChange(false);
      onError();
    });
  }
}
