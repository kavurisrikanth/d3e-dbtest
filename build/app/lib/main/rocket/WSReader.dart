import '../classes/BlockString.dart';
import '../classes/DFile.dart';
import '../classes/Date.dart';
import '../classes/ExpressionString.dart';
import '../classes/Time.dart';
import 'BufferReader.dart';
import 'Template.dart';
import 'TemplateTypes.dart';
import '../utils/DBObject.dart';
import '../utils/DBSaveStatus.dart';
import '../utils/ReferenceCatch.dart';

class WSReader {
  BufferReader _data;
  ReferenceCatch _catch;
  List<int> out = [];
  WSReader(this._catch, this._data);

  void done() {
    //print('r done');
  }

  int readByte() {
    int b = _data.readByte();
    //print('r byte: ' + b.toString());
    return b;
  }

  String readString() {
    String str = _data.readString();
    //print('r str: ' + (str == null ? 'null' : str));
    return str;
  }

  List<String> readStringList() {
    List<String> res = [];
    int size = _data.readInt();
    //print('r str list: ' + size.toString());
    for (int i = 0; i < size; i++) {
      res.add(_data.readString());
    }
    return res;
  }

  double readDouble() {
    double d = _data.readDouble();
    //print('r double: ' + d.toString());
    return d;
  }

  int readInteger() {
    int i = _data.readInt();
    //print('r int: ' + i.toString());
    return i;
  }

  List<int> readIntegerList() {
    List<int> res = [];
    int size = _data.readInt();
    //print('r int list: ' + size.toString());
    for (int i = 0; i < size; i++) {
      res.add(_data.readInt());
    }
    return res;
  }

  bool readBoolean() {
    bool b = _data.readBool();
    //print('r bool: ' + b.toString());
    return b;
  }

  List<bool> readBooleanList() {
    List<bool> res = [];
    int size = _data.readInt();
    //print('r bool list: ' + size.toString());
    for (int i = 0; i < size; i++) {
      res.add(_data.readBool());
    }
    return res;
  }

  T readEnum<T>(T convert(String enumStr)) {
    int type = readInteger();
    if (type == -1) {
      //print('r enum null');
      return null;
    }
    int field = readInteger();
    T e = Template.getEnumField(type, field);
    //print('r enum: ' + e.toString());
    return e;
  }

  List<T> readEnumList<T>(T convert(String enumStr)) {
    List<T> list = [];
    //print('r enum list: ');
    int size = readInteger();
    for (int i = 0; i < size; i++) {
      list.add(readEnum(convert));
    }
    return list;
  }

  ExpressionString readExpressionString() {
    //print('r exp str: ');
    String str = readString();
    if (str == null) {
      return null;
    }
    return ExpressionString(str);
  }

  BlockString readBlockString() {
    //print('r block str: ');
    String str = readString();
    if (str == null) {
      return null;
    }
    return BlockString(str);
  }

  DateTime readDateTime() {
    //print('r date time');
    String dateTime = readString();
    if (dateTime == null) {
      return null;
    }
    return DateTime.parse(dateTime + 'Z');
  }

  Date readDate() {
    //print('r date');
    String str = readString();
    if (str == null) {
      return null;
    }
    return Date.parse(readString());
  }

  List<Date> readDateList() {
    List<Date> list = [];
    int size = readInteger();
    //print('r date list: ' + size.toString());
    for (int i = 0; i < size; i++) {
      list.add(readDate());
    }
    return list;
  }

  Duration readDuration() {
    //TODO
  }

  Time readTime() {
    //print('r time');
    String time = readString();
    if (time == null) {
      return null;
    }
    return Time.parse(time);
  }

  List<Time> readTimeList() {
    List<Time> list = [];
    int size = readInteger();
    //print('r time list: ' + size.toString());
    for (int i = 0; i < size; i++) {
      list.add(readTime());
    }
    return list;
  }

  DFile readDFile() {
    String id = readString();
    if (id == null) {
      //print('r dfile: null');
      return null;
    }
    //print('r dfile: ' + id);
    DFile file = new DFile();
    file.id = id;
    file.name = readString();
    file.size = readInteger();
    file.mimeType = readString();
    return file;
  }

  List<DFile> readDFileList() {
    List<DFile> list = [];
    int size = readInteger();
    //print('r dfile list: ' + size.toString());
    for (int i = 0; i < size; i++) {
      list.add(readDFile());
    }
    return list;
  }

  T readRef<T extends DBObject>(int fieldIdx, DBObject parent) {
    int typeIndex = readInteger();
    if (typeIndex == -1) {
      return null;
    }
    TemplateType type = Template.types[typeIndex];
    //print('r ref: ' + type.name);
    DBObject obj = type.embedded
        ? parent.get(fieldIdx)
        : _fromId(readInteger(), typeIndex);
    bool locked = obj.lockedChanges();
    obj.lock();
    int fieldIndex = readInteger();
    while (fieldIndex != -1) {
      //print('r fieldIndex: ' + fieldIndex.toString());
      TemplateField field = type[fieldIndex];
      //print('r field: ' + field.name);
      if (field.collection) {
        if (obj.d3eChanges.contains(fieldIndex)) {
          List val = obj.d3eChanges.getValue(fieldIndex);
          List newList = _readList(field, fieldIndex, val, obj);
          obj.d3eChanges.replaceValue(fieldIndex, newList);
        } else {
          List val = obj.get(fieldIndex);
          List newList = _readList(field, fieldIndex, val, obj);
          obj.set(fieldIndex, newList);
        }
      } else {
        Object val = _readField(field, fieldIndex, obj);
        if (obj.d3eChanges.contains(fieldIndex)) {
          obj.d3eChanges.replaceValue(fieldIndex, val);
        } else {
          obj.set(fieldIndex, val);
        }
      }
      fieldIndex = readInteger();
    }
    if (!locked) {
      obj.unlock();
    }
    return obj;
  }

  List _readList(
      TemplateField field, int fieldIdx, List list, DBObject parent) {
    List newList = List.from(list);
    int count = readInteger();
    //print('r list ${field.name} ' + count.toString());
    if (count == 0) {
      newList.clear();
    } else if (count > 0) {
      newList.clear();
      while (count > 0) {
        newList.add(_readField(field, fieldIdx, parent));
        count--;
      }
    } else if (count < 0) {
      //print('List Changes: size: ' + newList.length.toString());
      count = -count;
      //print('Total changes: ' + count.toString());
      while (count > 0) {
        int index = readInteger();
        if (index > 0) {
          index--;
          // added
          Object val = _readField(field, fieldIdx, parent);
          if (index == newList.length) {
            //print('Added at : ' + index.toString());
            newList.add(val);
          } else {
            //print('Insert at : ' + index.toString());
            newList.insert(index, val);
          }
        } else {
          // removed
          index = -index;
          index--;
          //print('Removed at : ' + index.toString());
          newList.removeAt(index);
        }
        count--;
      }
      //print('List changes done: ' + newList.length.toString());
    }
    return newList;
  }

  Object _readField(TemplateField field, int fieldIdx, DBObject parent) {
    switch (field.fieldType) {
      case FieldType.String:
        return readString();
      case FieldType.Integer:
        return readInteger();
      case FieldType.Double:
        return readDouble();
      case FieldType.Boolean:
        return readBoolean();
      case FieldType.Date:
        int year = readInteger();
        if (year == null) {
          return null;
        }
        int month = readInteger();
        int dayOfMonth = readInteger();
        return Date.of(year, month, dayOfMonth);

      case FieldType.DateTime:
        int millisecondsSinceEpoch = readInteger();
        if (millisecondsSinceEpoch == -1) {
          return null;
        }
        return DateTime.fromMillisecondsSinceEpoch(millisecondsSinceEpoch);
      case FieldType.Time:
        int millisecondsSinceEpoch = readInteger();
        if (millisecondsSinceEpoch == -1) {
          return null;
        }
        return Time.fromDateTime(
            DateTime.fromMillisecondsSinceEpoch(millisecondsSinceEpoch));
      case FieldType.Duration:
        int inMilliseconds = readInteger();
        if (inMilliseconds == -1) {
          return null;
        }
        return Duration(milliseconds: inMilliseconds);
      case FieldType.DFile:
        return readDFile();
      case FieldType.Enum:
        return readInteger();
      case FieldType.Ref:
        return readRef(fieldIdx, parent);
    }
    return null;
  }

  DBObject _fromId(int id, int type) {
    DBObject obj = _catch.findObject(type, id);
    if (obj == null) {
      obj = Template.types[type].creator();
      obj.id = id;
      if (id > 0) {
        obj.saveStatus = DBSaveStatus.Saved;
      }
      _catch.addObject(obj);
    }
    return obj;
  }
}
