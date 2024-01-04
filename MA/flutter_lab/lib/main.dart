import 'package:flutter/material.dart';
import 'package:flutter_lab/screens/add_assignment_screen.dart';
import 'package:flutter_lab/widgets/assignment_widget.dart';

import 'domain/assignment.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  List<Assignment> assignments = [
    Assignment(
        id: 1,
        name: "lab2",
        subject: "OOP",
        dueDate: DateTime(2023, 2, 3),
        assignedDate: DateTime(2023, 2, 1),
        isCompleted: false),
    Assignment(
        id: 2,
        name: "lab2",
        subject: "OOP",
        dueDate: DateTime(2023, 2, 3),
        assignedDate: DateTime(2023, 2, 1),
        isCompleted: false)
  ];

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
        body: ListView(
          children: assignments
              .map((e) => AssignmentWidget(
                    assignment: e,
                    onDeleteFunction: () => onDelete(e.id),
                    onUpdateFunction: onUpdate,
                  ))
              .toList(),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () {
            Navigator.push(context, MaterialPageRoute(builder: (context) {
              return const AddAssignment();
            })).then((value) {
              if (value != null) {
                addItem(value);
              }
            });
          },
          tooltip: 'Add Assignment',
          child: const Icon(Icons.add),
        ));
  }

  void addItem(Assignment assignment) {
    setState(() {
      assignments = [...assignments, assignment];
    });
  }

  void onDelete(int assignmentId) {
    setState(() {
      assignments =
          assignments.where((element) => element.id != assignmentId).toList();
    });
  }

  void onUpdate(int assigmentId, Assignment assignment) {
    setState(() {
      assignments =
          assignments.map((e) => e.id == assigmentId ? assignment : e).toList();
    });
  }
}
