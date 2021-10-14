import '../classes/DBResult.dart';
import '../rocket/MessageDispatch.dart';
import '../utils/CloneContext.dart';
import '../utils/DBObject.dart';

class InventoryItem extends DBObject {
  int id = 0;
  DBObject otherMaster;
  InventoryItem() {}
  String get d3eType {
    return 'InventoryItem';
  }

  void clear() {
    this.d3eChanges.clear();
  }

  Object get(int field) {
    switch (field) {
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

  InventoryItem deepClone({clearId = true}) {
    CloneContext ctx = CloneContext(clearId: clearId);

    return ctx.startClone(this);
  }

  void collectChildValues(CloneContext ctx) {}
  void deepCloneIntoObj(DBObject dbObj, CloneContext ctx) {
    InventoryItem obj = (dbObj as InventoryItem);

    obj.id = id;
  }

  Future<DBResult> save() async {
    return (await MessageDispatch.get().save(this));
  }

  Future<DBResult> delete() async {
    return (await MessageDispatch.get().delete(this));
  }

  void set(int field, Object value) {
    switch (field) {
    }
  }
}
