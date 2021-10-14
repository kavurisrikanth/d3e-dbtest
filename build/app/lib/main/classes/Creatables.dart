import '../models/Creatable.dart';
import '../utils/CollectionUtils.dart';
import '../utils/DBObject.dart';
import 'DBResultStatus.dart';

class Creatables extends DBObject {
  int _id = 0;
  static const int _ERRORS = 0;
  static const int _ITEMS = 1;
  static const int _STATUS = 2;
  DBResultStatus _status = DBResultStatus.Success;
  List<String> _errors = [];
  List<Creatable> _items = [];
  Creatables(
      {List<String> errors, List<Creatable> items, DBResultStatus status}) {
    if (errors != null) {
      this.setErrors(errors);
    }

    if (items != null) {
      this.setItems(items);
    }

    if (status != null) {
      this.setStatus(status);
    }
  }
  int get id {
    return _id;
  }

  set id(int id) {
    this._id = id;
  }

  String get d3eType {
    return 'Creatables';
  }

  void clear() {}
  @override
  void initListeners() {
    super.initListeners();
  }

  DBResultStatus get status {
    return _status;
  }

  void setStatus(DBResultStatus val) {
    bool isValChanged = _status != val;

    if (!isValChanged) {
      return;
    }

    this.updateD3EChanges(_STATUS, _status.index);

    _status = val;

    fire('status', this);
  }

  List<String> get errors {
    return _errors;
  }

  void setErrors(List<String> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_errors, val);

    if (!isValChanged) {
      return;
    }

    if (!this.d3eChanges.contains(_ERRORS)) {
      List<String> _old = List.from(_errors);

      this.updateD3EChanges(_ERRORS, _old);
    }

    _errors.clear();

    _errors.addAll(val);

    fire('errors', this);
  }

  void addToErrors(String val, [int index = -1]) {
    List<String> _old = [];

    bool _isNewChange = !this.d3eChanges.contains(_ERRORS);

    if (_isNewChange) {
      _old = List.from(_errors);
    }

    if (index == -1) {
      if (!_errors.contains(val)) _errors.add(val);
    } else {
      _errors.insert(index, val);
    }

    fire('errors', this, val, true);

    if (_isNewChange) {
      this.updateD3EChanges(_ERRORS, _old);
    }
  }

  void removeFromErrors(String val) {
    List<String> _old = [];

    bool _isNewChange = !this.d3eChanges.contains(_ERRORS);

    if (_isNewChange) {
      _old = List.from(_errors);
    }

    _errors.remove(val);

    fire('errors', this, val, false);

    if (_isNewChange) {
      this.updateD3EChanges(_ERRORS, _old);
    }
  }

  List<Creatable> get items {
    return _items;
  }

  void setItems(List<Creatable> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_items, val);

    if (!isValChanged) {
      return;
    }

    if (!this.d3eChanges.contains(_ITEMS)) {
      List<Creatable> _old = List.from(_items);

      this.updateD3EChanges(_ITEMS, _old);
    }

    updateObservableColl('items', _items, val);

    _items.clear();

    _items.addAll(val);

    fire('items', this);
  }

  void addToItems(Creatable val, [int index = -1]) {
    List<Creatable> _old = [];

    bool _isNewChange = !this.d3eChanges.contains(_ITEMS);

    if (_isNewChange) {
      _old = List.from(_items);
    }

    if (index == -1) {
      if (!_items.contains(val)) _items.add(val);
    } else {
      _items.insert(index, val);
    }

    fire('items', this, val, true);

    updateObservable('items', null, val);

    if (_isNewChange) {
      this.updateD3EChanges(_ITEMS, _old);
    }
  }

  void removeFromItems(Creatable val) {
    List<Creatable> _old = [];

    bool _isNewChange = !this.d3eChanges.contains(_ITEMS);

    if (_isNewChange) {
      _old = List.from(_items);
    }

    _items.remove(val);

    fire('items', this, val, false);

    removeObservable('items', val);

    if (_isNewChange) {
      this.updateD3EChanges(_ITEMS, _old);
    }
  }

  void set(int field, Object value) {
    switch (field) {
      case _STATUS:
        {
          this.setStatus(DBResultStatus.values[(value as int)]);
          break;
        }

      case _ERRORS:
        {
          this.setErrors((value as List).cast<String>());
          break;
        }

      case _ITEMS:
        {
          this.setItems((value as List).cast<Creatable>());
          break;
        }
    }
  }

  Object get(int field) {
    switch (field) {
      case _STATUS:
        {
          return this._status.index;
        }

      case _ERRORS:
        {
          return this._errors;
        }

      case _ITEMS:
        {
          return this._items;
        }
      default:
        {
          return null;
        }
    }
  }

  bool operator ==(Object other) {
    return identical(this, other) ||
        other is Creatables &&
            _status == other._status &&
            _errors == other._errors &&
            _items == other._items;
  }

  @override
  int get hashCode {
    return _status?.hashCode ??
        0 + _errors?.hashCode ??
        0 + _items?.hashCode ??
        0;
  }
}
