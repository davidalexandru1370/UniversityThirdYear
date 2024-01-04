import 'package:flutter_lab/domain/assignment.dart';
import 'package:flutter_lab/domain/status.dart';

class LocalAssignment extends Assignment {
  Status status;

  LocalAssignment(
      {required  super.id,
      required super.name,
      required super.subject,
      required super.dueDate,
      required super.receivedDate,
      required super.isCompleted,
      required this.status}) {
    super.id = id;
  }

  Map<String, dynamic> toMap() {
    var map = super.toMap();
    map['status'] = status.index;

    return map;
  }

  factory LocalAssignment.fromMap(Map<String, dynamic> map) {
    return LocalAssignment(
      id: map['id'],
      name: map['name'],
      subject: map['subject'],
      dueDate: DateTime.parse(map['dueDate']),
      receivedDate: DateTime.parse(map['receivedDate']),
      isCompleted: map['isCompleted'] == 1,
      status: Status.values[map['status']],
    );
  }
}
