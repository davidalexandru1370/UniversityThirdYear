class Assignment {
  late int? id;
  late String name;
  late String subject;
  late DateTime dueDate;
  late DateTime assignedDate;
  late bool isCompleted = false;

  Assignment(
      {this.id,
      required this.name,
      required this.subject,
      required this.dueDate,
      required this.assignedDate,
      required this.isCompleted});

  factory Assignment.fromMap(Map<String, dynamic> map) {
    return Assignment(
        id: map['id'],
        name: map['name'],
        subject: map['subject'],
        dueDate: DateTime.parse(map['dueDate']),
        assignedDate: DateTime.parse(map['assignedDate']),
        isCompleted: map['isCompleted'] == 1 ? true : false);
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'subject': subject,
      'dueDate': dueDate.toString(),
      'assignedDate': assignedDate.toString(),
      'isCompleted': isCompleted == true ? 1 : 0
    };
  }
}
