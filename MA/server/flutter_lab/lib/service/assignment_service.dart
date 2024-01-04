import 'dart:convert';

import 'package:flutter_lab/domain/assignment.dart';
import 'package:http/http.dart' as http;

class AssignmentService {
  final String _baseUrl = 'http://192.168.1.6:5000/api/assignment';

  AssignmentService() {}

  Future<List<Assignment>> getAssignments() async {
    var url = Uri.parse(_baseUrl);
    var response = await http.get(url);
    if (response.statusCode == 200) {
      var assignments = <Assignment>[];
      var json = response.body;
      var jsonList = jsonDecode(json) as List;
      for (var jsonAssignment in jsonList) {
        var assignment = Assignment.fromJson(jsonAssignment);
        assignments.add(assignment);
      }
      return assignments;
    } else {
      throw Exception('Failed to load assignments');
    }
  }

  Future<Assignment> insertAssignment(Assignment assignment) async {
    var url = Uri.parse(_baseUrl);
    var response = await http.post(url,
        body: jsonEncode(assignment.toMap()),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        });
    print(response);
    if (response.statusCode == 201) {
      var json = response.body;
      var addedAssignment = Assignment.fromJson(jsonDecode(json));
      return addedAssignment;
    } else {
      throw Exception(response.body);
    }
  }

  Future<Assignment> updateAssignment(Assignment assignment) async {
    var url = Uri.parse(_baseUrl);
    var response = await http.put(url,
        body: jsonEncode(assignment.toMap()),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8',
        });
    print(response.body);
    if (response.statusCode == 200) {
      var json = response.body;
      var assignment = Assignment.fromJson(jsonDecode(json));
      return assignment;
    } else {
      throw Exception(response.body);
    }
  }

  Future<void> deleteAssignment(int id) async {
    var url = Uri.parse('$_baseUrl/$id');
    var response = await http.delete(url);
    if (response.statusCode == 200) {
      return;
    } else {
      throw Exception('Failed to delete assignment');
    }
  }
}
