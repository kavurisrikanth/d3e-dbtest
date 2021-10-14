import '../classes/DBResult.dart';
import '../rocket/MessageDispatch.dart';
import '../utils/CloneContext.dart';
import '../utils/DBObject.dart';

class Customer extends DBObject {
  static const int _DOB = 0;
  static const int _NAME = 1;
  int id = 0;
  DBObject otherMaster;
  String _name = '';
  DateTime _dob;
  Customer({DateTime dob, String name}) {
    this.setDob(dob ?? null);

    this.setName(name ?? '');
  }
  String get d3eType {
    return 'Customer';
  }

  void clear() {
    this.d3eChanges.clear();
  }

  String get name {
    return _name;
  }

  void setName(String val) {
    bool isValChanged = _name != val;

    if (!isValChanged) {
      return;
    }

    this.updateD3EChanges(_NAME, _name);

    _name = val;

    fire('name', this);
  }

  DateTime get dob {
    return _dob;
  }

  void setDob(DateTime val) {
    bool isValChanged = _dob != val;

    if (!isValChanged) {
      return;
    }

    this.updateD3EChanges(_DOB, _dob);

    _dob = val;

    fire('dob', this);
  }

  Object get(int field) {
    switch (field) {
      case _NAME:
        {
          return this._name;
        }

      case _DOB:
        {
          return this._dob;
        }
      default:
        {
          return null;
        }
    }
  }

  void updateD3EChanges(int index, Object value) {
    if (lockedChanges()) {
      return;
    }

    super.updateD3EChanges(index, value);
  }

  void restore() {
    /*
TODO: Might be removed
*/

    this.d3eChanges.restore(this);
  }

  Customer deepClone({clearId = true}) {
    CloneContext ctx = CloneContext(clearId: clearId);

    return ctx.startClone(this);
  }

  void collectChildValues(CloneContext ctx) {}
  void deepCloneIntoObj(DBObject dbObj, CloneContext ctx) {
    Customer obj = (dbObj as Customer);

    obj.id = id;

    obj.setName(_name);

    obj.setDob(_dob);
  }

  Future<DBResult> save() async {
    return (await MessageDispatch.get().save(this));
  }

  Future<DBResult> delete() async {
    return (await MessageDispatch.get().delete(this));
  }

  void set(int field, Object value) {
    switch (field) {
      case _NAME:
        {
          this.setName((value as String));
          break;
        }

      case _DOB:
        {
          this.setDob((value as DateTime));
          break;
        }
    }
  }
}
