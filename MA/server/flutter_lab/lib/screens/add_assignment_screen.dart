import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter_lab/domain/assignment.dart';
import 'package:intl/intl.dart';

class AddAssignment extends StatefulWidget {
  const AddAssignment({super.key});

  @override
  _AddAssignmentState createState() => _AddAssignmentState();
}

class _AddAssignmentState extends State<AddAssignment> {
  final _formKey = GlobalKey<FormState>();
  var _assignmentName = '';
  var _assignmentSubject = '';
  var _assignmentDueDate = DateTime(0);
  var _assignmentAssignedDate = DateTime(0);
  var _assignmentIsCompleted = false;

  TextEditingController dateInputAssignedDate = TextEditingController();
  TextEditingController dateInputDueDate = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Scaffold(
        appBar: AppBar(
          title: Text("Add Assignment"),
        ),
        body: Column(
          children: [
            TextFormField(
              decoration: InputDecoration(labelText: 'Assignment Name'),
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
              decoration: const InputDecoration(
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
                    lastDate: DateTime(2100));

                if (pickedDate != null) {
                  String formattedDate =
                      DateFormat('yyyy-MM-dd').format(pickedDate);
                  setState(() {
                    _assignmentDueDate = pickedDate;
                  });
                  dateInputDueDate.text = formattedDate;
                } else {}
              },
            )),
            Center(
                child: TextField(
              controller: dateInputAssignedDate,
              //editing controller of this TextField
              decoration: const InputDecoration(
                  icon: Icon(Icons.calendar_today), //icon of text field
                  labelText: "Enter Date" //label text of field
                  ),
              readOnly: true,
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
                  dateInputAssignedDate.text = formattedDate;
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
                          id: null,
                          name: _assignmentName,
                          subject: _assignmentSubject,
                          dueDate: _assignmentDueDate,
                          receivedDate: _assignmentAssignedDate,
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
