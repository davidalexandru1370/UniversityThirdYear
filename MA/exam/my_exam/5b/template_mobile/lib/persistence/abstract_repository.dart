import 'package:template_mobile/domain/abstract_entity.dart';
import 'package:sqflite/sqflite.dart';
import 'db_context.dart';
import 'package:logger/logger.dart';

class AbstractRepository<T extends AbstractEntity> {
  var logger = Logger();

  Future<int> insert(Map<String, dynamic> entity, String tableName) async {
    final db = await DatabaseContext.instance.database;
    logger.log(Level.info, "Inserting entity $entity in table $tableName");
    final int result = await db.insert(tableName, entity,
        conflictAlgorithm: ConflictAlgorithm.replace);
    logger.log(Level.info, "Result of insert is $result");
    return result;
  }

  Future<List<T>> getAll(String table, Function constructor) async {
    final db = await DatabaseContext.instance.database;
    logger.log(Level.info, "Getting all entities from table $table");
    final result = await db.query(table);
    logger.log(Level.info, "Result of query is $result");
    return result.map((e) => constructor(e) as T).toList();
  }

  Future<int> update(Map<String, dynamic> entity, String table) async {
    final db = await DatabaseContext.instance.database;
    logger.log(Level.info, "Updating entity $entity in table $table");
    final result = await db
        .update(table, entity, where: 'id = ?', whereArgs: [entity['id']]);
    logger.log(Level.info, "Result of update is $result");
    return result;
  }

  Future<int> delete(int id, String table) async {
    final db = await DatabaseContext.instance.database;
    logger.log(Level.info, "Deleting entity with id $id from table $table");
    final result = await db.delete(table, where: 'id = ?', whereArgs: [id]);
    logger.log(Level.info, "Result of delete is $result");
    return result;
  }

  Future<int> deleteAll(String table) async {
    final db = await DatabaseContext.instance.database;
    logger.log(Level.info, "Deleting all entities from table $table");
    final result = await db.delete(table);
    logger.log(Level.info, "Result of delete is $result");
    return result;
  }
}
