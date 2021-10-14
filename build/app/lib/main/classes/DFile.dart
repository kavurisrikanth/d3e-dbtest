import 'Env.dart';

class DFile {
  static const int _ID = 0;
  static const int _NAME = 1;
  static const int _SIZE = 2;

  String _name;
  String _id;
  int _size;

  DFile();

  DFile.fromJson(Map<String, dynamic> json) {
    this._name = json['name'];
    this._id = json['id'];
    this._size = json['size'];
  }

  Map toJson() {
    Map jsonMap = Map();

    jsonMap['id'] = this._id;
    jsonMap['name'] = this._name;
    jsonMap['size'] = this._size;

    return jsonMap;
  }

  String get id {
    return _id;
  }

  set id(String id) {
    this._id = id;
  }

  int get size {
    return _size;
  }

  set size(int size) {
    this._size = size;
  }

  String get name {
    return _name;
  }

  set name(String name) {
    this._name = name;
  }

  String get downloadUrl {
    return Env.get().baseHttpUrl + '/api/download/' + this.id;
  }
}
