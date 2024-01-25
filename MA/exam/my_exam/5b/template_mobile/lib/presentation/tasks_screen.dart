import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:template_mobile/common/utilities.dart';
import 'package:template_mobile/service/service.dart';
import 'package:fluttertoast/fluttertoast.dart';

import '../domain/Task.dart';
import '../persistence/Repository.dart';

class TaskScreen extends StatefulWidget {
  final String date;

  const TaskScreen({super.key, required this.date});

  @override
  State<TaskScreen> createState() => _TaskScreenState(date);
}

class _TaskScreenState extends State<TaskScreen> {
  List<Task> items = [];
  final String date;
  FitnessService itemService = FitnessService();
  Repository repository = Repository();
  bool _isLoading = true;
  bool _isDeleteLoading = false;

  _TaskScreenState(this.date);

  @override
  void initState() {
    try {
      itemService.getAllTasks(date).then((value) {
        if (mounted == false) {
          return;
        }
        setState(() {
          _isLoading = false;
          items = value;
        });
        for (var item in items) {
          repository.insert(item.toMap(), Utilities.principalTable);
        }
      }).onError((error, _) {
        print(error);
        repository.getAllTasks().then((value) {
          setState(() {
            _isLoading = false;
            items = value.where((element) => element.date == date).toList();
          });
        });
      });
    } catch (e) {}
  }

  @override
  Widget build(BuildContext context) {
    return _isLoading == true
        ? const Center(child: CircularProgressIndicator())
        : Scaffold(
            appBar: AppBar(
              title: const Text("Tasks"),
            ),
            body: ListView.builder(
                itemCount: items.length,
                itemBuilder: (context, index) {
                  return Card(
                    margin: const EdgeInsets.all(8.0),
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(15)),
                    child: Column(children: [
                      Text(items[index].date),
                      Text(items[index].type),
                      Text(items[index].category.toString()),
                      Text(items[index].duration.toString()),
                      Text(items[index].priority.toString()),
                      Text(items[index].description.toString()),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          ElevatedButton(
                              onPressed: () async {
                                setState(() {
                                  _isDeleteLoading = true;
                                });
                                try {
                                  await itemService
                                      .deleteItem(items[index].id!);
                                  await repository.delete(items[index].id!,
                                      Utilities.principalTable);
                                  setState(() {
                                    items = items
                                        .where((element) =>
                                            element.id != items[index].id)
                                        .toList();
                                  });
                                } catch (e) {
                                  Fluttertoast.showToast(
                                      msg: e.toString(),
                                      gravity: ToastGravity.TOP,
                                      backgroundColor: Colors.red);
                                }

                                setState(() {
                                  _isDeleteLoading = false;
                                });
                              },
                              child: _isDeleteLoading == false
                                  ? const Text("Delete")
                                  : const CircularProgressIndicator()),
                        ],
                      )
                    ]),
                  );
                }),
          );
  }
}
