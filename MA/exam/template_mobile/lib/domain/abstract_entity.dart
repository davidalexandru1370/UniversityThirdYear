import 'package:template_mobile/domain/status.dart';

abstract class AbstractEntity {
  int? id;
  Status status;

  AbstractEntity({this.id, required this.status});

  Map<String, dynamic> toMap();

  AbstractEntity fromMap(Map<String, dynamic> map);
}
