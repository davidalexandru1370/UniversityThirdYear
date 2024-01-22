import 'package:template_mobile/domain/abstract_entity.dart';

class Fitness extends AbstractEntity {
  String date;
  String type;
  int duration;
  int distance;
  int calories;
  int rate;

  Fitness(
      {required this.date,
      required this.type,
      required this.duration,
      required this.distance,
      required this.calories,
      required this.rate,
      int? id})
      : super(id: id);

  @override
  Map<String, dynamic> toMap() {
    return {
      'date': date,
      'type': type,
      'duration': duration,
      'distance': distance,
      'rate': rate,
      'calories': calories,
      'id': id
    };
  }

  factory Fitness.fromMap(Map<String, dynamic> map) {
    return Fitness(
        date: map['date'],
        type: map['type'],
        duration: map['duration'],
        distance: map['distance'],
        calories: map['calories'],
        id: map['id'],
        rate: map['rate']);
  }

  @override
  String toString() {
    return 'Fitness{date: $date, type: $type, duration: $duration, distance: $distance, calories: $calories, rate: $rate}';
  }
}
