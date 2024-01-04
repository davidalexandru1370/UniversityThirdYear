import 'package:flutter_lab/domain/assignment.dart';
import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class Repository {
  static final Repository instance = Repository._init();

  static Database? _database;

  Repository._init();

  Future<Database> get database async {
    if (_database != null) {
      return _database!;
    }

    _database = await init();
    return _database!;
  }

  Repository() {}

  Future<Database> init() async {
    return openDatabase(
        join(await getDatabasesPath(), 'assignment_database.db'),
        onCreate: (db, version) {
      return db.execute(
        'CREATE TABLE assignments(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, subject TEXT, dueDate TEXT, assignedDate TEXT, isCompleted INTEGER)',
      );
    }, version: 1);
  }

  Future<Assignment> insertAssignment(Assignment assignment) async {
    final db = await instance.database;
    var id = await db?.insert(
      'assignments',
      assignment.toMap(),
      conflictAlgorithm: ConflictAlgorithm.replace,
    );
    assignment.id = id;
    return assignment;
  }

  Future<Assignment> updateAssignment(Assignment assignment) async {
    print(assignment.id);
    final db = await instance.database;
    var updatedAssignment = await db?.update(
      'assignments',
      assignment.toMap(),
      where: 'id = ?',
      whereArgs: [assignment.id],
    );

    return assignment;
  }

  Future<void> deleteAssignment(int id) async {
    final db = await instance.database;
    await db?.delete(
      'assignments',
      where: 'id = ?',
      whereArgs: [id],
    );
  }

  Future<List<Assignment>> getAssignments() async {
    final db = await instance.database;
    final List<Map<String, dynamic>> maps = await db!.query('assignments');
    return List.generate(maps.length, (index) {
      return Assignment.fromMap(maps[index]);
    });
  }

  Future<Assignment> getAssignment(int id) async {
    final db = await instance.database;
    final List<Map<String, dynamic>> maps =
        await db!.query('assignments', where: 'id = ?', whereArgs: [id]);

    return Assignment.fromMap(maps[0]);
  }
}
