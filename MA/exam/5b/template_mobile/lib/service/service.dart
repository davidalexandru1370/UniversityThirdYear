import 'dart:convert';

import 'package:template_mobile/common/utilities.dart';
import 'package:template_mobile/persistence/Repository.dart';
import 'package:template_mobile/service/abstract_service.dart';
import 'package:http/http.dart' as http;

import '../domain/Fitness.dart';

class FitnessService extends AbstractService<Fitness> {
  FitnessService();

  Future<List<String>> getAllDates() async {
    var url = Uri.parse("${baseUrl}dates");

    final response = await http.get(url).timeout(const Duration(seconds: 45));

    if (response.statusCode == 200) {
      var items = <String>[];
      var body = response.body;
      var jsonList = jsonDecode(body) as List;
      for (var item in jsonList) {
        var entity = item as String;
        items.add(entity);
      }

      return items;
    } else {
      var body = response.body;
      var errorMessage = jsonDecode(body) as String;
      throw Exception(errorMessage);
    }
  }

  Future<List<Fitness>> getAllFitness(String date) async {
    var url = Uri.parse("${baseUrl}entries/$date");
    try {
      final response = await http.get(url).timeout(const Duration(seconds: 15));
      if (response.statusCode == 200) {
        var items = <Fitness>[];
        var body = response.body;
        var jsonList = jsonDecode(body) as List;
        for (var item in jsonList) {
          var entity = Fitness.fromMap(item);
          items.add(entity);
        }
        return items;
      } else {
        var body = response.body;
        var errorMessage = jsonDecode(body) as String;
        throw Exception(errorMessage);
      }
    } catch (e) {
      print(e);
      throw e;
    }
  }

  Future deleteItem(int id) async {
    var url = Uri.parse("${baseUrl}entry/$id");

    final response =
        await http.delete(url).timeout(const Duration(seconds: 15));
    if (response.statusCode == 200) {
      return;
    } else {
      var body = response.body;
      var errorMessage = jsonDecode(body) as Map<String, dynamic>;
      throw Exception(errorMessage['text']);
    }
  }

  Future<Fitness> addFitness(Fitness item) async {
    var url = Uri.parse("${baseUrl}${Utilities.addEndpoint}");
    try {
      final response = await http.post(url,
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
          },
          body: jsonEncode(item.toMap()));
      if (response.statusCode == 200) {
        var body = response.body;
        var jsonItem = jsonDecode(body) as Map<String, dynamic>;
        var entity = Fitness.fromMap(jsonItem);
        return entity;
      } else {
        var body = response.body;
        var errorMessage = jsonDecode(body) as Map<String, dynamic>;
        throw Exception(errorMessage['text']);
      }
    } catch (e) {
      print(e);
      throw e;
    }
  }

  Future<List<Fitness>> getAll(String endpoint) async {
    var url = Uri.parse(baseUrl + endpoint);

    final response = await http.get(url);

    if (response.statusCode == 200) {
      var items = <Fitness>[];
      var body = response.body;
      var jsonList = jsonDecode(body) as List;
      for (var item in jsonList) {
        var entity = Fitness.fromMap(item);
        items.add(entity);
      }

      return items;
    } else {
      var body = response.body;
      var errorMessage = jsonDecode(body) as String;
      throw Exception(errorMessage);
    }
  }
}
