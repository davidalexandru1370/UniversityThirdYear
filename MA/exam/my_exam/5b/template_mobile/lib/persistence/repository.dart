import 'package:template_mobile/common/utilities.dart';
import 'package:template_mobile/domain/Task.dart';
import 'package:template_mobile/persistence/abstract_repository.dart';

import 'db_context.dart';

class Repository extends AbstractRepository<Task> {
  Future<List<Task>> getAllTasks() async {
    return await super.getAll(Utilities.principalTable, Task.fromMap);
  }

  Future<List<String>> getAllDates() async {
    final db = await DatabaseContext.instance.database;
    final result = await db.query(Utilities.secondTable);
    return result.map((e) => e[Utilities.secondTable].toString()).toList();
  }

  Future<int> insertFitness(Task entity) {
    return super.insert(entity.toMap(), Utilities.principalTable);
  }
}
