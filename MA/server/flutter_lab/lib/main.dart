import 'package:flutter/material.dart';
import 'package:flutter_lab/persistence/Repository.dart';
import 'package:flutter_lab/screens/add_assignment_screen.dart';
import 'package:flutter_lab/widgets/assignment_widget.dart';
import 'domain/assignment.dart';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/widgets.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
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
  bool _isLoading = true;
  List<Assignment> assignments = [];

  Future refreshAssignments() async {
    await Repository.instance.syncAssignments();
    return await Repository.instance.getAssignments();
  }

  @override
  void initState() {
    super.initState();
    Connectivity().onConnectivityChanged.listen((event) {
      print(event.toString());
      if (event == ConnectivityResult.wifi ||
          event == ConnectivityResult.mobile) {
        Repository.instance.syncAssignments();
      }
    });
    Future.delayed(Duration.zero, () async {
      var data = await refreshAssignments();
      setState(() {
        _isLoading = false;
        assignments = data;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
        body: _isLoading
            ? Center(child: CircularProgressIndicator())
            : ListView(
                children: assignments
                    .map((e) => AssignmentWidget(
                          assignment: e,
                          onDeleteFunction: () => onDelete(e.id!),
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

  void addItem(Assignment assignment) async {
    Assignment addedAssignment =
        await Repository.instance.insertAssignment(assignment);
    setState(() {
      assignments = [...assignments, addedAssignment];
    });
  }

  void getAll() async {
    List<Assignment> assignments = await Repository.instance.getAssignments();
    setState(() {
      this.assignments = assignments;
    });
  }

  void onDelete(int assignmentId) async {
    await Repository.instance.deleteAssignment(assignmentId);
    setState(() {
      assignments =
          assignments.where((element) => element.id != assignmentId).toList();
    });
  }

  void onUpdate(Assignment assignment) async {
    var result = await Repository.instance.updateAssignment(assignment);
    setState(() {
      assignments =
          assignments.map((e) => e.id == assignment.id ? result : e).toList();
    });
  }
}
