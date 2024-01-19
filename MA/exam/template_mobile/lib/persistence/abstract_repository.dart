import 'package:template_mobile/domain/abstract_entity.dart';
import 'package:template_mobile/service/abstract_service.dart';

import 'db_context.dart';

abstract class AbstractRepository<T1 extends AbstractEntity,
    T2 extends AbstractService> {
  late AbstractService _service;
  DatabaseContext instance = DatabaseContext.instance;

  AbstractRepository(this._service);

  T2 getService();

  T1 getEntity();

  Future<T1> insert(T1 entity);

  Future<List<T1>> getAll();

  Future<T1> update(T1 entity);

  Future<T1> delete(T1 entity);
}
