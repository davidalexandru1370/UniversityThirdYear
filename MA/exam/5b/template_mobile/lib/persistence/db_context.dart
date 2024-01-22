import 'package:path/path.dart';
import 'package:sqflite/sqflite.dart';

import '../common/utilities.dart';

class DatabaseContext {
  static final DatabaseContext instance = DatabaseContext._init();
  static Database? _database;

  DatabaseContext._init();

  Future<Database> get database async {
    if (_database != null) {
      return _database!;
    }

    _database = await init();
    return _database!;
  }

  Future<Database> init() async {
    String createDatabaseQuery =
        'CREATE TABLE ${Utilities.principalTable}(id INTEGER Primary Key, date TEXT, type TEXT, duration integer, distance integer, calories integer, rate integer)';
    return openDatabase(join(await getDatabasesPath(), 'exam6_database.db'),
        onCreate: (db, version) async {
      await db.execute(
        createDatabaseQuery,
      );

      return await db.execute(
        'CREATE TABLE ${Utilities.secondTable}(${Utilities.secondTable} TEXT Primary Key)',
      );
    }, version: 2);
  }
}
