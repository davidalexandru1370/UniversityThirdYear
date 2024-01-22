import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:template_mobile/service/service.dart';
import 'package:collection/collection.dart';

import '../domain/Fitness.dart';

class ProgressScreen extends StatefulWidget {
  const ProgressScreen({super.key});

  @override
  State<ProgressScreen> createState() => _ProgressScreenState();
}

class _ProgressScreenState extends State<ProgressScreen> {
  FitnessService itemService = FitnessService();
  final Map<int, int> _caloriesBurned = {};
  List<String> top3 = [];
  bool _isLoading = true;

  @override
  void initState() {
    // TODO: implement initState
    itemService.getAll("all").then((value) {
      Map<String, int> distances = {};
      for (var item in value) {
        distances[item.type] = (distances[item.type] ?? 0) + item.distance;
        var elements = item.date.split("-");
        var week = weeksBetween(
            DateTime(int.parse(elements[0]), int.parse(elements[1]),
                int.parse(elements[2])),
            DateTime.now());
        _caloriesBurned[week] = (_caloriesBurned[week] ?? 0) + item.calories;
      }

      top3 = distances.entries
          .toList()
          .sorted((a, b) => b.value.compareTo(a.value))
          .map((e) => e.key)
          .toList();

      setState(() {
        _isLoading = false;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Progress"),
      ),
      body: _isLoading == true
          ? const Center(child: CircularProgressIndicator())
          : Column(
              children: [
                const SizedBox(
                  height: 20,
                ),
                const Text(
                  "Top 3 activities",
                  style: TextStyle(fontSize: 20),
                ),
                const SizedBox(
                  height: 20,
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    Text(
                      top3[0],
                      style: const TextStyle(fontSize: 20),
                    ),
                    Text(
                      top3[1],
                      style: const TextStyle(fontSize: 20),
                    ),
                    Text(
                      top3[2],
                      style: const TextStyle(fontSize: 20),
                    ),
                  ],
                ),
                const SizedBox(
                  height: 20,
                ),
                const Text(
                  "Calories burned per week",
                  style: TextStyle(fontSize: 20),
                ),
                const SizedBox(
                  height: 20,
                ),
                Expanded(
                    child: ListView(
                  children: _caloriesBurned.entries
                      .toList()
                      .map((e) => Row(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: [
                              Text(
                                "Week ${e.key}",
                                style: const TextStyle(fontSize: 20),
                              ),
                              Text(
                                "${e.value} calories",
                                style: const TextStyle(fontSize: 20),
                              ),
                            ],
                          ))
                      .toList(),
                )),
              ],
            ),
    );
  }

  int weeksBetween(DateTime from, DateTime to) {
    from = DateTime.utc(from.year, from.month, from.day);
    to = DateTime.utc(to.year, to.month, to.day);
    return abs((to.difference(from).inDays / 7).ceil());
  }

  int abs(int x) {
    if (x < 0) {
      return -x;
    }
    return x;
  }
}
