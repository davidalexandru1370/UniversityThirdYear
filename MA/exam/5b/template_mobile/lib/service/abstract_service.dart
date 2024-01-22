import 'dart:convert';

import 'package:template_mobile/common/utilities.dart';
import 'package:template_mobile/domain/abstract_entity.dart';
import 'package:http/http.dart' as http;

class AbstractService<T extends AbstractEntity> {
  //adb reverse tcp:2325 tcp:2325
  //disable windows firewall
  final String baseUrl = "http://${Utilities.serverIpAndPort}/";


}
