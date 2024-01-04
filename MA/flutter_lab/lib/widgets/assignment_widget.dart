import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../domain/assignment.dart';
import '../screens/update_assignment_screen.dart';

class AssignmentWidget extends StatelessWidget {
  const AssignmentWidget(
      {Key? key,
      required this.assignment,
      required this.onDeleteFunction,
      required this.onUpdateFunction})
      : super(key: key);

  final Assignment assignment;
  final VoidCallback onDeleteFunction;
  final Function(int, Assignment) onUpdateFunction;

  @override
  Widget build(BuildContext context) {
    return Card(
        child:
            Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
      Column(
        children: [
          Text("Name: ${assignment.name}"),
          Text("Subject: ${assignment.subject}"),
          Text("Due Date: ${assignment.dueDate}"),
          Text("Assigned Date: ${assignment.assignedDate}"),
          Text("Completed ${assignment.isCompleted == true ? "Yes" : "No"}"),
        ],
      ),
      Column(
        children: [
          IconButton(
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) {
                  return UpdateAssignment(assignment: assignment);
                })).then((value) {
                  value = value as Assignment;
                  if (value != null) {
                    onUpdateFunction(value.id, value);
                  }
                });
              },
              icon: const Icon(Icons.edit)),
          IconButton(
              onPressed: () {
                showAlertDialog(context, onDeleteFunction);
              },
              icon: const Icon(Icons.delete)),
        ],
      )
    ]));
  }

  showAlertDialog(BuildContext context, VoidCallback onDeleteFunction) {
    // set up the buttons
    Widget cancelButton = ElevatedButton(
      child: Text("Cancel"),
      onPressed: () {
        Navigator.pop(context);
      },
    );
    Widget continueButton = ElevatedButton(
      child: Text("Delete"),
      onPressed: () {
        onDeleteFunction();
        Navigator.pop(context);
      },
    );
    // set up the AlertDialog
    AlertDialog alert = AlertDialog(
      title: Text("Are you sure?"),
      content: Text("Are you sure you want to delete this?"),
      actions: [
        cancelButton,
        continueButton,
      ],
    );
    // show the dialog
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }
}
