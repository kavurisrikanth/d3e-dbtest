import '../models/InventoryItem.dart';
import '../utils/CollectionUtils.dart';
import '../utils/DBObject.dart';

class Inventory extends DBObject {
  int _id = 0;
  static const int _ITEMS = 0;
  List<InventoryItem> _items = [];
  Inventory({List<InventoryItem> items}) {
    if (items != null) {
      this.setItems(items);
    }
  }
  int get id {
    return _id;
  }

  set id(int id) {
    this._id = id;
  }

  String get d3eType {
    return 'Inventory';
  }

  void clear() {}
  @override
  void initListeners() {
    super.initListeners();
  }

  List<InventoryItem> get items {
    return _items;
  }

  void setItems(List<InventoryItem> val) {
    bool isValChanged = CollectionUtils.isNotEquals(_items, val);

    if (!isValChanged) {
      return;
    }

    if (!this.d3eChanges.contains(_ITEMS)) {
      List<InventoryItem> _old = List.from(_items);

      this.updateD3EChanges(_ITEMS, _old);
    }

    updateObservableColl('items', _items, val);

    _items.clear();

    _items.addAll(val);

    fire('items', this);
  }

  void addToItems(InventoryItem val, [int index = -1]) {
    List<InventoryItem> _old = [];

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

  void removeFromItems(InventoryItem val) {
    List<InventoryItem> _old = [];

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
      case _ITEMS:
        {
          this.setItems((value as List).cast<InventoryItem>());
          break;
        }
    }
  }

  Object get(int field) {
    switch (field) {
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
        other is Inventory && _items == other._items;
  }

  @override
  int get hashCode {
    return _items?.hashCode ?? 0;
  }
}
