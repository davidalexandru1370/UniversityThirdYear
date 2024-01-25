import 'package:template_mobile/domain/abstract_entity.dart';

class Task extends AbstractEntity {
  String date;
  String type;
  double duration;
  String priority;
  String category;
  String description;

  Task(
      {required this.date,
      required this.type,
      required this.duration,
      required this.priority,
      required this.category,
      required this.description,
      int? id})
      : super(id: id);

  @override
  Map<String, dynamic> toMap() {
    return {
      'date': date,
      'type': type,
      'duration': duration,
      'priority': priority,
      'description': description,
      'category': category,
      'id': id
    };
  }

  factory Task.fromMap(Map<String, dynamic> map) {
    return Task(
        date: map['date'],
        type: map['type'],
        duration: double.parse(map['duration'].toString()),
        priority: map['priority'],
        category: map['category'],
        id: map['id'],
        description: map['description']);
  }

  @override
  String toString() {
    return 'date: $date, type: $type, duration: $duration, priority: $priority, category: $category, description: $description';
  }
}
