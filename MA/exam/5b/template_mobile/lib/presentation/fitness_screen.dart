import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:template_mobile/common/utilities.dart';
import 'package:template_mobile/service/service.dart';
import 'package:fluttertoast/fluttertoast.dart';

import '../domain/Fitness.dart';
import '../persistence/Repository.dart';

class FitnessScreen extends StatefulWidget {
  final String category;

  const FitnessScreen({super.key, required this.category});

  @override
  State<FitnessScreen> createState() => _FitnessScreenState(category);
}

class _FitnessScreenState extends State<FitnessScreen> {
  List<Fitness> items = [];
  final String date;
  FitnessService itemService = FitnessService();
  Repository repository = Repository();
  bool _isLoading = true;
  bool _isDeleteLoading = false;

  _FitnessScreenState(this.date);

  @override
  void initState() {
    try {
      itemService.getAllFitness(date).then((value) {
        setState(() {
          _isLoading = false;
          items = value;
        });
        for (var item in items) {
          repository.insert(item.toMap(), Utilities.principalTable);
        }
      }).onError((error, _) {
        print(error);
        repository.getAllFitness().then((value) {
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
              title: const Text("Items"),
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
                      Text(items[index].calories.toString()),
                      Text(items[index].duration.toString()),
                      Text(items[index].distance.toString()),
                      Text(items[index].rate.toString()),
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
                          ElevatedButton(
                              onPressed: () {}, child: const Text("Update")),
                        ],
                      )
                    ]),
                  );
                }),
          );
  }
}
