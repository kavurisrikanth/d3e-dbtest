import '../classes/DBResult.dart';
import '../classes/DFile.dart';
import '../rocket/MessageDispatch.dart';
import '../utils/CloneContext.dart';
import '../utils/CollectionUtils.dart';
import '../utils/DBObject.dart';
import 'Embedded.dart';
import 'NonCreatable.dart';

class Creatable extends DBObject {
  static const int _CHILD = 0;
  static const int _CHILDCOLL = 1;
  static const int _EMB = 2;
  static const int _FILE = 3;
  static const int _NAME = 4;
  static const int _REF = 5;
  static const int _REFCOLL = 6;
  int id = 0;
  DBObject otherMaster;
  String _name = '';
  Creatable _ref;
  List<Creatable> _refColl = [];
  NonCreatable _child;
  List<NonCreatable> _childColl = [];
  DFile _file;
  Embedded _emb = Embedded();
  Creatable(
      {NonCreatable child,
      List<NonCreatable> childColl,
      Embedded emb,
      DFile file,
      String name,
      Creatable ref,
      List<Creatable> refColl}) {
    this.setChild(child ?? null);

    this.setChildColl(childColl ?? []);

    this.emb.otherMaster = this;

    this.emb.childPropertyInMaster = _EMB;

    updateObservable('emb', null, _emb);

    if (emb != null) {
      this.emb.setTo(emb);
    }

    this.setFile(file ?? null);

    this.setName(name ?? '');

    this.setRef(ref ?? null);

    this.setRefColl(refColl ?? []);
  }
  String get d3eType {
    return 'Creatable';
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

  Creatable get ref {
    return _ref;
  }

  void setRef(Creatable val) {
    bool isValChanged = _ref != val;

    if (!isValChanged) {
      return;
    }

    this.updateD3EChanges(_REF, _ref);

    updateObservable('ref', _ref, val);

    _ref = val;

    fire('ref', this);
  }

  List<Creatable> get refColl {
    return _refColl;
  }

  void setRefColl(List<Creatable> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_refColl, val);

    if (!isValChanged) {
      return;
    }

    if (!this.d3eChanges.contains(_REFCOLL)) {
      List<Creatable> _old = List.from(_refColl);

      this.updateD3EChanges(_REFCOLL, _old);
    }

    updateObservableColl('refColl', _refColl, val);

    _refColl.clear();

    _refColl.addAll(val);

    fire('refColl', this);
  }

  void addToRefColl(Creatable val, [int index = -1]) {
    List<Creatable> _old = [];

    bool _isNewChange = !this.d3eChanges.contains(_REFCOLL);

    if (_isNewChange) {
      _old = List.from(_refColl);
    }

    if (index == -1) {
      if (!_refColl.contains(val)) _refColl.add(val);
    } else {
      _refColl.insert(index, val);
    }

    fire('refColl', this, val, true);

    updateObservable('refColl', null, val);

    if (_isNewChange) {
      this.updateD3EChanges(_REFCOLL, _old);
    }
  }

  void removeFromRefColl(Creatable val) {
    List<Creatable> _old = [];

    bool _isNewChange = !this.d3eChanges.contains(_REFCOLL);

    if (_isNewChange) {
      _old = List.from(_refColl);
    }

    _refColl.remove(val);

    fire('refColl', this, val, false);

    if (_isNewChange) {
      this.updateD3EChanges(_REFCOLL, _old);
    }
  }

  NonCreatable get child {
    return _child;
  }

  void setChild(NonCreatable val) {
    bool isValChanged = _child != val;

    if (!isValChanged) {
      return;
    }

    if (_child != null) {
      _child.otherMaster = null;
    }

    if (val != null) {
      val.otherMaster = this;

      val.childPropertyInMaster = _CHILD;
    }

    this.updateD3EChanges(_CHILD, _child);

    updateObservable('child', _child, val);

    _child = val;

    fire('child', this);
  }

  List<NonCreatable> get childColl {
    return _childColl;
  }

  void setChildColl(List<NonCreatable> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_childColl, val);

    if (!isValChanged) {
      return;
    }

    if (_childColl != null) {
      _childColl.forEach((one) => one.otherMaster = null);
    }

    if (val != null) {
      for (NonCreatable o in val) {
        o.otherMaster = this;

        o.childPropertyInMaster = _CHILDCOLL;
      }
    }

    if (!this.d3eChanges.contains(_CHILDCOLL)) {
      List<NonCreatable> _old = List.from(_childColl);

      this.updateD3EChanges(_CHILDCOLL, _old);
    }

    updateObservableColl('childColl', _childColl, val);

    _childColl.clear();

    _childColl.addAll(val);

    fire('childColl', this);
  }

  void addToChildColl(NonCreatable val, [int index = -1]) {
    List<NonCreatable> _old = [];

    bool _isNewChange = !this.d3eChanges.contains(_CHILDCOLL);

    if (_isNewChange) {
      _old = List.from(_childColl);
    }

    val.otherMaster = this;

    val.childPropertyInMaster = _CHILDCOLL;

    if (index == -1) {
      if (!_childColl.contains(val)) _childColl.add(val);
    } else {
      _childColl.insert(index, val);
    }

    fire('childColl', this, val, true);

    updateObservable('childColl', null, val);

    if (_isNewChange) {
      this.updateD3EChanges(_CHILDCOLL, _old);
    }
  }

  void removeFromChildColl(NonCreatable val) {
    List<NonCreatable> _old = [];

    bool _isNewChange = !this.d3eChanges.contains(_CHILDCOLL);

    if (_isNewChange) {
      _old = List.from(_childColl);
    }

    _childColl.remove(val);

    val.otherMaster = null;

    fire('childColl', this, val, false);

    removeObservable('childColl', val);

    if (_isNewChange) {
      this.updateD3EChanges(_CHILDCOLL, _old);
    }
  }

  DFile get file {
    return _file;
  }

  void setFile(DFile val) {
    bool isValChanged = _file != val;

    if (!isValChanged) {
      return;
    }

    this.updateD3EChanges(_FILE, _file);

    _file = val;

    fire('file', this);
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

      case _REF:
        {
          return this._ref;
        }

      case _REFCOLL:
        {
          return this._refColl;
        }

      case _CHILD:
        {
          return this._child;
        }

      case _CHILDCOLL:
        {
          return this._childColl;
        }

      case _FILE:
        {
          return this._file;
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
  }

  void restore() {
    /*
TODO: Might be removed
*/

    this.d3eChanges.restore(this);
  }

  Creatable deepClone({clearId = true}) {
    CloneContext ctx = CloneContext(clearId: clearId);

    return ctx.startClone(this);
  }

  void collectChildValues(CloneContext ctx) {
    ctx.collectChild(_child);

    ctx.collectChilds(_childColl);

    ctx.collectChild(_emb);
  }

  void deepCloneIntoObj(DBObject dbObj, CloneContext ctx) {
    Creatable obj = (dbObj as Creatable);

    obj.id = id;

    obj.setName(_name);

    obj.setRef(_ref);

    obj.setRefColl(_refColl);

    ctx.cloneChild(_child, (v) => obj.setChild(v));

    ctx.cloneChildList(_childColl, (v) => obj.setChildColl(v));

    obj.setFile(_file);

    _emb.deepCloneIntoObj(obj._emb, ctx);
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

      case _REF:
        {
          this.setRef((value as Creatable));
          break;
        }

      case _REFCOLL:
        {
          this.setRefColl((value as List).cast<Creatable>());
          break;
        }

      case _CHILD:
        {
          this.setChild((value as NonCreatable));
          break;
        }

      case _CHILDCOLL:
        {
          this.setChildColl((value as List).cast<NonCreatable>());
          break;
        }

      case _FILE:
        {
          this.setFile((value as DFile));
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
