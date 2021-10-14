import '../classes/DBResult.dart';
import '../utils/CloneContext.dart';
import '../utils/DBObject.dart';

class Embedded extends DBObject {
  static const int _EMBNAME = 0;
  int id = 0;
  DBObject otherMaster;
  String _embName = '';
  int childPropertyInMaster = 0;
  Embedded({String embName}) {
    this.setEmbName(embName ?? '');
  }
  String get d3eType {
    return 'Embedded';
  }

  void clear() {
    this.d3eChanges.clear();
  }

  String get embName {
    return _embName;
  }

  void setEmbName(String val) {
    bool isValChanged = _embName != val;

    if (!isValChanged) {
      return;
    }

    this.updateD3EChanges(_EMBNAME, _embName);

    _embName = val;

    fire('embName', this);
  }

  Object get(int field) {
    switch (field) {
      case _EMBNAME:
        {
          return this._embName;
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

  Embedded deepClone({clearId = true}) {
    CloneContext ctx = CloneContext(clearId: clearId);

    return ctx.startClone(this);
  }

  void collectChildValues(CloneContext ctx) {}
  void deepCloneIntoObj(DBObject dbObj, CloneContext ctx) {
    Embedded obj = (dbObj as Embedded);

    obj.id = id;

    obj.setEmbName(_embName);
  }

  void setTo(Embedded obj) {
    this.setEmbName(obj.embName);
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
      case _EMBNAME:
        {
          this.setEmbName((value as String));
          break;
        }
    }
  }
}
