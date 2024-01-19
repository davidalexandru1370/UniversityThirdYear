abstract class AbstractService {
  Future<List> getAll();

  Future insert(Object entity);

  Future update(Object entity);

  Future delete(Object entity);
}
