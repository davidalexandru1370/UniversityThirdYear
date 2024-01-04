class Assignment {
  late int id;
  late String name;
  late String subject;
  late DateTime dueDate;
  late DateTime assignedDate;
  late bool isCompleted = false;

  Assignment(
      {required this.id,
      required this.name,
      required this.subject,
      required this.dueDate,
      required this.assignedDate,
      required this.isCompleted});
}
