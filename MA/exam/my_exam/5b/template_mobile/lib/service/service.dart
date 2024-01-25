import 'dart:convert';

import 'package:template_mobile/common/utilities.dart';
import 'package:template_mobile/persistence/Repository.dart';
import 'package:template_mobile/service/abstract_service.dart';
import 'package:http/http.dart' as http;
import 'package:logger/logger.dart';

import '../domain/Task.dart';

class FitnessService extends AbstractService<Task> {
  FitnessService();

  var logger = Logger();

  Future<List<String>> getAllDates() async {
    var url = Uri.parse("${baseUrl}taskDays");
    logger.log(Level.info, 'Getting all dates from $url');
    final response = await http.get(url).timeout(const Duration(seconds: 45));

    if (response.statusCode == 200) {
      var items = <String>[];
      var body = response.body;
      logger.log(Level.info, 'Response for getting all data: $body');
      var jsonList = jsonDecode(body) as List;
      for (var item in jsonList) {
        var entity = item as String;
        items.add(entity);
      }

      return items;
    } else {
      var body = response.body;
      var errorMessage = jsonDecode(body) as String;
      logger.log(Level.info, 'Error for getting all dates: $errorMessage');
      throw Exception(errorMessage);
    }
  }

  Future<List<Task>> getAllTasks(String date) async {
    var url = Uri.parse("${baseUrl}details/$date");
    try {
      final response = await http.get(url).timeout(const Duration(seconds: 15));
      if (response.statusCode == 200) {
        var items = <Task>[];
        var body = response.body;
        var jsonList = jsonDecode(body) as List;
        for (var item in jsonList) {
          var entity = Task.fromMap(item);
          items.add(entity);
        }
        return items;
      } else {
        var body = response.body;
        var errorMessage = jsonDecode(body) as String;
        logger.log(Level.info,
            'Error for deleting all data by date: $date' + errorMessage);
        throw Exception(errorMessage);
      }
    } catch (e) {
      print(e);
      throw e;
    }
  }

  Future deleteItem(int id) async {
    var url = Uri.parse("${baseUrl}task/$id");
    logger.log(Level.info, 'Response for deleting all data by id: $id');

    final response =
        await http.delete(url).timeout(const Duration(seconds: 15));
    if (response.statusCode == 200) {
      return;
    } else {
      var body = response.body;
      var errorMessage = jsonDecode(body) as Map<String, dynamic>;
      logger.log(Level.info,
          'Error for deleting all data by id: $id' + errorMessage['text']);
      throw Exception(errorMessage['text']);
    }
  }

  Future<Task> addTask(Task item) async {
    var url = Uri.parse("${baseUrl}${Utilities.addEndpoint}");
    logger.log(Level.info, 'Trying to  add task: $item');
    try {
      final response = await http.post(url,
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
          },
          body: jsonEncode(item.toMap()));
      if (response.statusCode == 200) {
        var body = response.body;
        var jsonItem = jsonDecode(body) as Map<String, dynamic>;
        var entity = Task.fromMap(jsonItem);
        return entity;
      } else {
        var body = response.body;
        var errorMessage = jsonDecode(body) as Map<String, dynamic>;
        logger.log(
            Level.info, 'Error for add task: $item' + errorMessage['text']);
        throw Exception(errorMessage['text']);
      }
    } catch (e) {
      print(e);
      throw e;
    }
  }

  Future<List<Task>> getAll(String endpoint) async {
    var url = Uri.parse(baseUrl + endpoint);
    logger.log(Level.info, 'Request for get all from endpoint $endpoint');
    final response = await http.get(url);

    if (response.statusCode == 200) {
      var items = <Task>[];
      var body = response.body;
      var jsonList = jsonDecode(body) as List;
      for (var item in jsonList) {
        var entity = Task.fromMap(item);
        items.add(entity);
      }
      logger.log(Level.info,
          'Response for get all from endpoint $endpoint : $items');
      return items;
    } else {
      var body = response.body;
      var errorMessage = jsonDecode(body) as String;
      throw Exception(errorMessage);
    }
  }
}
