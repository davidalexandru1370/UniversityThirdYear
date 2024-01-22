import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:template_mobile/service/service.dart';
import 'package:fluttertoast/fluttertoast.dart';

import '../domain/Fitness.dart';
import '../persistence/Repository.dart';

class AddFitness extends StatefulWidget {
  const AddFitness({super.key});

  @override
  State<AddFitness> createState() => _AddFitnessState();
}

class _AddFitnessState extends State<AddFitness> {
  final _formKey = GlobalKey<FormState>();

  final FitnessService _itemService = FitnessService();
  final Repository repository = Repository();
  String _date = "";
  String _type = "";
  int _duration = 1;
  int _distance = 0;
  int _calories = 0;
  int _rate = 0;
  bool _isLoading = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Add Item"),
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
                      return "Please enter an duration";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    _duration = int.parse(value!);
                  },
                ),
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Distance",
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter the distance";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    _distance = int.parse(value!);
                  },
                ),
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Calories",
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter the calories";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    _calories = int.parse(value!);
                  },
                ),
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Rate",
                  ),
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return "Please enter the rate";
                    }
                    return null;
                  },
                  onSaved: (value) {
                    _rate = int.parse(value!);
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
                        var result = await _itemService.addFitness(Fitness(
                          date: _date,
                          type: _type,
                          duration: _duration,
                          distance: _distance,
                          calories: _calories,
                          rate: _rate,
                        ));
                        repository.insert(result.toMap(), "fitness");
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
                        ? const Text("Add Fitness")
                        : const CircularProgressIndicator()),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
