import '../classes/DBResult.dart';
import '../utils/CloneContext.dart';
import '../utils/DBObject.dart';
import 'Embedded.dart';

class NonCreatable extends DBObject {
  static const int _EMB = 0;
  static const int _NAME = 1;
  int id = 0;
  DBObject otherMaster;
  String _name = 'NonCreatable name';
  Embedded _emb = Embedded();
  int childPropertyInMaster = 0;
  NonCreatable({Embedded emb, String name}) {
    this.emb.otherMaster = this;

    this.emb.childPropertyInMaster = _EMB;

    updateObservable('emb', null, _emb);

    if (emb != null) {
      this.emb.setTo(emb);
    }

    this.setName(name ?? 'NonCreatable name');
  }
  String get d3eType {
    return 'NonCreatable';
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

  Embedded get emb {
    return _emb;
  }

  void setEmb(Embedded val) {
    _emb.setTo(val);
  }

  Object get(int field) {
    switch (field) {
      case _NAME:
        {
          return this._name;
        }

      case _EMB:
        {
          return this._emb;
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

    this.otherMaster?.updateChildChanges(this.childPropertyInMaster);
  }

  void restore() {
    /*
TODO: Might be removed
*/

    this.d3eChanges.restore(this);
  }

  NonCreatable deepClone({clearId = true}) {
    CloneContext ctx = CloneContext(clearId: clearId);

    return ctx.startClone(this);
  }

  void collectChildValues(CloneContext ctx) {
    ctx.collectChild(_emb);
  }

  void deepCloneIntoObj(DBObject dbObj, CloneContext ctx) {
    NonCreatable obj = (dbObj as NonCreatable);

    obj.id = id;

    obj.setName(_name);

    _emb.deepCloneIntoObj(obj._emb, ctx);
  }

  Future<DBResult> save() async {
    if (this.otherMaster != null) {
      return this.otherMaster.save();
    }

    return DBResult();
  }

  Future<DBResult> delete() async {
    if (this.otherMaster != null) {
      return this.otherMaster.delete();
    }

    return DBResult();
  }

  void set(int field, Object value) {
    switch (field) {
      case _NAME:
        {
          this.setName((value as String));
          break;
        }

      case _EMB:
        {
          this.setEmb((value as Embedded));
          break;
        }
    }
  }
}
