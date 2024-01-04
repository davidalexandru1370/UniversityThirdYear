class Assignment {
  late int? id;
  late String name;
  late String subject;
  late DateTime dueDate;
  late DateTime receivedDate;
  late bool isCompleted = false;

  Assignment(
      {required this.id,
      required this.name,
      required this.subject,
      required this.dueDate,
      required this.receivedDate,
      required this.isCompleted});

  factory Assignment.fromMap(Map<String, dynamic> map) {
    return Assignment(
        id: map['id'],
        name: map['name'],
        subject: map['subject'],
        dueDate: DateTime.parse(map['dueDate']),
        receivedDate: DateTime.parse(map['receivedDate']),
        isCompleted: map['isCompleted'] == 1 ? true : false);
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'name': name,
      'subject': subject,
      'dueDate': dueDate.toString().split(" ")[0],
      'receivedDate': receivedDate.toString().split(" ")[0],
      'isCompleted': isCompleted
    };
  }

  factory Assignment.fromJson(Map<String, dynamic> json) {
    return Assignment(
        id: json['id'],
        name: json['name'],
        subject: json['subject'],
        dueDate: DateTime.parse(json['dueDate']),
        receivedDate: DateTime.parse(json['receivedDate']),
        isCompleted: json['isCompleted'] == true ? true : false);
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id.toString(),
      'name': name.toString(),
      'subject': subject.toString(),
      'dueDate': dueDate.toString(),
      'receivedDate': receivedDate.toString(),
      'isCompleted': isCompleted.toString()
    };
  }
}
