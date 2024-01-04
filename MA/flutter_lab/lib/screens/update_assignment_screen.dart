import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../domain/assignment.dart';

class UpdateAssignment extends StatefulWidget {
  final Assignment assignment;

  const UpdateAssignment({Key? key, required this.assignment})
      : super(key: key);

  @override
  _UpdateAssignmentState createState() =>
      _UpdateAssignmentState(this.assignment);
}

class _UpdateAssignmentState extends State<UpdateAssignment> {
  final _formKey = GlobalKey<FormState>();

  var _assignmentName = '';
  var _assignmentSubject = '';
  var _assignmentDueDate = DateTime(0);
  var _assignmentAssignedDate = DateTime(0);
  var _assignmentIsCompleted = false;
  var _assignmentId;

  TextEditingController dateInputAssignedDate = TextEditingController();
  TextEditingController dateInputDueDate = TextEditingController();

  _UpdateAssignmentState(Assignment assignment) {
    _assignmentName = assignment.name;
    _assignmentSubject = assignment.subject;
    _assignmentDueDate = assignment.dueDate;
    _assignmentAssignedDate = assignment.assignedDate;
    _assignmentIsCompleted = assignment.isCompleted;
    _assignmentId = assignment.id;

    dateInputAssignedDate = TextEditingController(
        text: DateFormat('yyyy-MM-dd').format(assignment.assignedDate));

    dateInputDueDate = TextEditingController(
        text: DateFormat('yyyy-MM-dd').format(assignment.dueDate));
  }

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Scaffold(
        appBar: AppBar(
          title: Text("Update Assignment"),
        ),
        body: Column(
          children: [
            TextFormField(
              decoration: InputDecoration(labelText: 'Assignment Name'),
              initialValue: _assignmentName,
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Please enter some text';
                }
                return null;
              },
              onSaved: (value) {
                _assignmentName = value!;
              },
            ),
            TextFormField(
              initialValue: _assignmentSubject,
              decoration: InputDecoration(labelText: 'Assignment Subject'),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return 'Please enter some text';
                }
                return null;
              },
              onSaved: (value) {
                _assignmentSubject = value!;
              },
            ),
            Center(
                child: TextField(
              controller: dateInputDueDate,
              //editing controller of this TextField
              decoration: InputDecoration(
                  icon: Icon(Icons.calendar_today), //icon of text field
                  labelText: "Enter Date" //label text of field
                  ),
              readOnly: true,
              //set it true, so that user will not able to edit text
              onTap: () async {
                DateTime? pickedDate = await showDatePicker(
                    context: context,
                    initialDate: DateTime.now(),
                    firstDate: DateTime(1950),
                    //DateTime.now() - not to allow to choose before today.
                    lastDate: DateTime(2100));

                if (pickedDate != null) {
                  String formattedDate =
                      DateFormat('yyyy-MM-dd').format(pickedDate);
                  setState(() {
                    _assignmentDueDate = pickedDate;
                  });
                  dateInputDueDate.text =
                      formattedDate; //set output date to TextField value.
                } else {}
              },
            )),
            Center(
                child: TextField(
              controller: dateInputAssignedDate,
              //editing controller of this TextField
              decoration: InputDecoration(
                  icon: Icon(Icons.calendar_today), //icon of text field
                  labelText: "Enter Date" //label text of field
                  ),
              readOnly: true,
              //set it true, so that user will not able to edit text
              onTap: () async {
                DateTime? pickedDate = await showDatePicker(
                    context: context,
                    initialDate: DateTime.now(),
                    firstDate: DateTime(1950),
                    //DateTime.now() - not to allow to choose before today.
                    lastDate: DateTime(2100));

                if (pickedDate != null) {
                  String formattedDate =
                      DateFormat('yyyy-MM-dd').format(pickedDate);
                  setState(() {
                    _assignmentAssignedDate = pickedDate;
                  });
                  dateInputAssignedDate.text =
                      formattedDate; //set output date to TextField value.
                } else {}
              },
            )),
            Padding(
              padding: const EdgeInsets.only(left: 10),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text("Is Completed?"),
                  Checkbox(
                      value: _assignmentIsCompleted,
                      onChanged: (value) {
                        setState(() {
                          _assignmentIsCompleted = value!;
                        });
                      })
                ],
              ),
            ),
            ElevatedButton(
              onPressed: () {
                if (_formKey.currentState!.validate()) {
                  _formKey.currentState!.save();
                  Navigator.pop(
                      context,
                      Assignment(
                          id: _assignmentId,
                          name: _assignmentName,
                          subject: _assignmentSubject,
                          dueDate: _assignmentDueDate,
                          assignedDate: _assignmentAssignedDate,
                          isCompleted: _assignmentIsCompleted));
                }
              },
              child: Text('Submit'),
            )
          ],
        ),
      ),
    );
  }
}
