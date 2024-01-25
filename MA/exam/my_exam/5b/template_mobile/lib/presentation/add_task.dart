import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:template_mobile/common/utilities.dart';
import 'package:template_mobile/service/service.dart';
import 'package:fluttertoast/fluttertoast.dart';

import '../domain/Task.dart';
import '../persistence/Repository.dart';

class AddTask extends StatefulWidget {
  const AddTask({super.key});

  @override
  State<AddTask> createState() => _AddTaskState();
}

class _AddTaskState extends State<AddTask> {
  final _formKey = GlobalKey<FormState>();

  final FitnessService _itemService = FitnessService();
  final Repository repository = Repository();
  String _date = "";
  String _type = "";
  double _duration = 1;
  String priority = "";
  String _category = "";
  String _description = "";
  bool _isLoading = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Add Task"),
      ),
      body: SingleChildScrollView(
        child: Form(
          key: _formKey,
          child: Container(
            child: Column(
              children: [
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Date",
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter a date";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    _date = value!;
                  },
                ),
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Type",
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter a type";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    _type = value!;
                  },
                ),
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Duration",
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter a duration";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    _duration = double.parse(value!);
                  },
                ),
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Priority",
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter the priority";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    priority = value!;
                  },
                ),
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Category",
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter the category";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    _category = value!;
                  },
                ),
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Description",
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter the description";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    _description = value!;
                  },
                ),
                ElevatedButton(
                    onPressed: () async {
                      if (!_formKey.currentState!.validate()) {
                        return;
                      }
                      _formKey.currentState!.save();
                      setState(() {
                        _isLoading = true;
                      });
                      try {
                        var result = await _itemService.addTask(Task(
                          date: _date,
                          type: _type,
                          duration: _duration,
                          priority: priority,
                          category: _category,
                          description: _description,
                        ));
                        repository.insert(result.toMap(), Utilities.principalTable);
                        setState(() {
                          _isLoading = false;
                        });
                        Navigator.pop(context);
                      } catch (e) {
                        setState(() {
                          _isLoading = false;
                        });
                        print(e);
                        Fluttertoast.showToast(
                            msg: e.toString(), backgroundColor: Colors.red);
                      }
                    },
                    child: _isLoading == false
                        ? const Text("Add Task")
                        : const CircularProgressIndicator()),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
