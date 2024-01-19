import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

class DatabaseContext {
  static final DatabaseContext instance = DatabaseContext._init();
  static Database? _database;

  DatabaseContext._init();

  Future<Database> get database async {
    if (_database != null) {
      return _database!;
    }

    _database = await init("db");
    return _database!;
  }

  Future<Database> init(String tableName) async {
    String createDatabaseQuery =
        'CREATE TABLE $tableName(id INTEGER, name TEXT, subject TEXT, dueDate TEXT, receivedDate TEXT, isCompleted INTEGER, status INTEGER)';
    return openDatabase(
        join(await getDatabasesPath(), '${tableName}_database.db'),
        onConfigure: (db) {
      db.execute("DROP TABLE IF EXISTS $tableName}");
      return db.execute(
        createDatabaseQuery,
      );
    }, onCreate: (db, version) {
      return db.execute(
        createDatabaseQuery,
      );
    }, version: 2);
  }
}
