import 'package:flutter/cupertino.dart';
import 'package:collection/collection.dart';
import 'package:flutter/material.dart';

import '../service/service.dart';

class TopScreen extends StatefulWidget {
  const TopScreen({super.key});

  @override
  State<TopScreen> createState() => _TopScreenState();
}

class _TopScreenState extends State<TopScreen> {

  FitnessService itemService = FitnessService();
  List<List<dynamic>> items = [];
  bool _isLoading = true;

  @override
  void initState() {
    // TODO: implement initState
    itemService.getAll("entries").then((value) {
      Map<String, int> categories = {};
      for (var item in value) {
        var category = item.category.toLowerCase();
        categories[category] = (categories[category] ?? 0) + 1;
      }

      items = categories.entries
          .toList()
          .sorted((a, b) {
        var ans = b.key.compareTo(a.key);
        if (ans == 0) {
          return b.value.compareTo(a.value);
        }
        return ans;
      })
          .map((e) => [e.key, e.value])
          .toList()
      .sublist(0, 3);

      setState(() {
        _isLoading = false;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Top"),
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
                    "Category: ${items[index][0]}",
                    style: const TextStyle(fontSize: 20),
                  ),
                  subtitle: Text(
                    "Tasks: ${items[index][1]}",
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
}
