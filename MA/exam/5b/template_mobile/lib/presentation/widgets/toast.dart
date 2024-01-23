import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class MyToast {
  static Widget getToast(String message) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 24.0, vertical: 12.0),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(25.0),
        color: Colors.greenAccent,
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(Icons.check),
          SizedBox(
            width: 100.0,
            height: 200,
            child: Text(
              message,
              softWrap: true,
              textAlign: TextAlign.left,
              textScaleFactor: 1.25,
            ),
          ),
        ],
      ),
    );
  }
}
