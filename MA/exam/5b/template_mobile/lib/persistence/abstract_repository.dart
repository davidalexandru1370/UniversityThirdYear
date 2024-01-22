import 'package:template_mobile/domain/abstract_entity.dart';
import 'package:sqflite/sqflite.dart';
import 'db_context.dart';

class AbstractRepository<T extends AbstractEntity> {
  Future<int> insert(Map<String, dynamic> entity, String tableName) async {
    final db = await DatabaseContext.instance.database;
    final int result = await db.insert(tableName, entity,
        conflictAlgorithm: ConflictAlgorithm.replace);
    return result;
  }

  Future<List<T>> getAll(String table, Function constructor) async {
    final db = await DatabaseContext.instance.database;
    final result = await db.query(table);
    return result.map((e) => constructor(e) as T).toList();
  }

  Future<int> update(Map<String, dynamic> entity, String table) async {
    final db = await DatabaseContext.instance.database;
    final result = await db
        .update(table, entity, where: 'id = ?', whereArgs: [entity['id']]);
    return result;
  }

  Future<int> delete(int id, String table) async {
    final db = await DatabaseContext.instance.database;
    final result = await db.delete(table, where: 'id = ?', whereArgs: [id]);
    return result;
  }
}
