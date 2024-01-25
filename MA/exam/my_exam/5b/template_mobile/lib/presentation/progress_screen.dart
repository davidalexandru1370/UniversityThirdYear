import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:template_mobile/service/service.dart';
import 'package:collection/collection.dart';

import '../domain/Task.dart';

class ProgressScreen extends StatefulWidget {
  const ProgressScreen({super.key});

  @override
  State<ProgressScreen> createState() => _ProgressScreenState();
}

class _ProgressScreenState extends State<ProgressScreen> {
  FitnessService itemService = FitnessService();
  final Map<int, int> _caloriesBurned = {};
  List<List<dynamic>> items = [];
  bool _isLoading = true;

  @override
  void initState() {
    // TODO: implement initState
    itemService.getAll("entries").then((value) {
      Map<int, double> duration = {};
      for (var item in value) {
        var month = int.parse(item.date.split("-")[1]);
        duration[month] = (duration[month] ?? 0) + item.duration;
      }

      items = duration.entries
          .toList()
          .sorted((a, b) {
            var ans = b.key.compareTo(a.key);
            if (ans == 0) {
              return b.value.compareTo(a.value);
            }
            return ans;
          })
          .map((e) => [e.key, e.value])
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
                Expanded(
                  child: ListView.builder(
                    itemBuilder: (context, index) {
                      return ListTile(
                        title: Text(
                          "Month: ${items[index][0]}",
                          style: const TextStyle(fontSize: 20),
                        ),
                        subtitle: Text(
                          "Duration: ${items[index][1]}",
                          style: const TextStyle(fontSize: 20),
                        ),
                      );
                    },
                    itemCount: items.length,
                  ),
                ),
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
