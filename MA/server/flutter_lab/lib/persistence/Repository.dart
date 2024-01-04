import 'dart:math';

import 'package:flutter_lab/domain/assignment.dart';
import 'package:flutter_lab/domain/status.dart';
import 'package:flutter_lab/service/assignment_service.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

import '../domain/local_assignment.dart';

class Repository {
  static final Repository instance = Repository._init();
  static final AssignmentService _assignmentService = AssignmentService();
  static Database? _database;

  Repository._init();

  Future<Database> get database async {
    if (_database != null) {
      return _database!;
    }

    _database = await init();
    _database!.execute("DELETE FROM assignments", null);
    return _database!;
  }

  Repository() {}

  Future<Database> init() async {
    return openDatabase(
        join(await getDatabasesPath(), 'assignment_database2.db'),
        onConfigure: (db) {
      db.execute("DROP TABLE IF EXISTS assignments");
      return db.execute(
        'CREATE TABLE assignments(id INTEGER, name TEXT, subject TEXT, dueDate TEXT, receivedDate TEXT, isCompleted INTEGER, status INTEGER)',
      );
    }, onCreate: (db, version) {
      return db.execute(
        'CREATE TABLE assignments(id INTEGER PRIMARY KEY, name TEXT, subject TEXT, dueDate TEXT, receivedDate TEXT, isCompleted INTEGER, status INTEGER)',
      );
    }, version: 2);
  }

  Future<Assignment> insertAssignment(Assignment assignment) async {
    final db = await instance.database;
    var localAssignment = LocalAssignment(
        id: null,
        name: assignment.name,
        subject: assignment.subject,
        dueDate: assignment.dueDate,
        receivedDate: assignment.receivedDate,
        isCompleted: assignment.isCompleted,
        status: Status.addedInLocalDatabase);
    try {
      var response = await _assignmentService.insertAssignment(assignment);
      localAssignment.id = response.id;
      localAssignment.status = Status.handledByServer;
    } catch (e) {
      localAssignment.id = Random().nextInt(1000000);
      print(e.toString());
    }

    await db?.insert(
      'assignments',
      localAssignment.toMap(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );

    return assignment;
  }

  Future<Assignment> updateAssignment(Assignment assignment) async {
    final db = await instance.database;
    try {
      var response = await _assignmentService.updateAssignment(assignment);
      await db?.update(
        'assignments',
        LocalAssignment(
          id: response.id,
          name: assignment.name,
          subject: assignment.subject,
          dueDate: assignment.dueDate,
          receivedDate: assignment.receivedDate,
          isCompleted: assignment.isCompleted,
          status: Status.handledByServer,
        ).toMap(),
        where: 'id = ?',
        whereArgs: [assignment.id],
      );
      return response;
    } catch (e) {
      await db?.update(
        'assignments',
        LocalAssignment(
          id: assignment.id,
          name: assignment.name,
          subject: assignment.subject,
          dueDate: assignment.dueDate,
          receivedDate: assignment.receivedDate,
          isCompleted: assignment.isCompleted,
          status: Status.updatedInLocalDatabase,
        ).toMap(),
        where: 'id = ?',
        whereArgs: [assignment.id],
      );
    }

    return assignment;
  }

  Future<void> deleteAssignment(int id) async {
    final db = await instance.database;
    try {
      await _assignmentService
          .deleteAssignment(id)
          .timeout(new Duration(seconds: 15));
      await db?.delete(
        'assignments',
        where: 'id = ?',
        whereArgs: [id],
      );
    } catch (e) {
      await db?.update(
          'assignments', {'status': Status.deletedInLocalDatabase.index},
          where: 'id = ?', whereArgs: [id]);
    }
  }

  Future<List<Assignment>> getAssignments() async {
    final db = await instance.database;
    try {
      var assignments = await _assignmentService
          .getAssignments()
          .timeout(new Duration(seconds: 15));
      await db?.delete('assignments');
      assignments.forEach((element) async {
        await db?.insert(
          'assignments',
          LocalAssignment(
                  id: element.id,
                  name: element.name,
                  subject: element.subject,
                  dueDate: element.dueDate,
                  receivedDate: element.receivedDate,
                  isCompleted: element.isCompleted,
                  status: Status.handledByServer)
              .toMap(),
          conflictAlgorithm: ConflictAlgorithm.replace,
        );
      });
      return assignments;
    } catch (e) {
      List<Map<String, dynamic>> maps = await db!.query('assignments');
      maps = maps
          .where((element) =>
              element['status'] != Status.deletedInLocalDatabase.index)
          .toList();
      return List.generate(maps.length, (index) {
        return Assignment.fromMap(maps[index]);
      });
    }
  }

  syncAssignments() async {
    final db = await instance.database;
    var query = await db!.query('assignments');
    var assignmentsFromDatabase = List.generate(query.length, (index) {
      return LocalAssignment.fromMap(query[index]);
    });

    for (var assignment in assignmentsFromDatabase) {
      if (assignment.id == null) {
        continue;
      }
      if (assignment.status == Status.addedInLocalDatabase) {
        try {
          var response = await _assignmentService.insertAssignment(Assignment(
              id: null,
              name: assignment.name,
              subject: assignment.subject,
              dueDate: assignment.dueDate,
              receivedDate: assignment.receivedDate,
              isCompleted: assignment.isCompleted));
          assignment.id = response.id;
          assignment.status = Status.handledByServer;
          await db.update('assignments', assignment.toMap(),
              where: 'id = ?', whereArgs: [assignment.id]);
        } catch (e) {
          print(e.toString());
        }
      } else if (assignment.status == Status.updatedInLocalDatabase) {
        try {
          await _assignmentService.updateAssignment(Assignment(
              id: assignment.id,
              name: assignment.name,
              subject: assignment.subject,
              dueDate: assignment.dueDate,
              receivedDate: assignment.receivedDate,
              isCompleted: assignment.isCompleted));
          assignment.status = Status.handledByServer;
          await db.update('assignments', assignment.toMap(),
              where: 'id = ?', whereArgs: [assignment.id]);
        } catch (e) {
          print(e.toString());
        }
      } else if (assignment.status == Status.deletedInLocalDatabase) {
        try {
          await _assignmentService.deleteAssignment(assignment.id!);
          await db.delete('assignments',
              where: 'id = ?', whereArgs: [assignment.id]);
        } catch (e) {
          print(e.toString());
        }
      }
    }

    await getAssignments();
  }

  Future<Assignment> getAssignment(int id) async {
    final db = await instance.database;
    final List<Map<String, dynamic>> maps =
        await db!.query('assignments', where: 'id = ?', whereArgs: [id]);

    return Assignment.fromMap(maps[0]);
  }
}
