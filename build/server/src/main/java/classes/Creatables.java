package classes;

import d3e.core.ListExt;
import d3e.core.SchemaConstants;
import java.util.List;
import lists.TypeAndId;
import models.Creatable;
import store.DBObject;

public class Creatables extends DBObject {
  public static final int _STATUS = 0;
  public static final int _ERRORS = 1;
  public static final int _ITEMS = 2;
  private long id;
  private DBResultStatus status;
  private List<String> errors = ListExt.List();
  private List<Creatable> items = ListExt.List();
  private List<TypeAndId> itemsRef;

  public Creatables() {}

  public Creatables(List<String> errors, List<Creatable> items, DBResultStatus status) {
    this.errors = errors;
    this.items = items;
    this.status = status;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public DBResultStatus getStatus() {
    return status;
  }

  public void setStatus(DBResultStatus status) {
    fieldChanged(_STATUS, this.status);
    this.status = status;
  }

  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    collFieldChanged(_ERRORS, this.errors);
    this.errors = errors;
  }

  public void addToErrors(String val, long index) {
    collFieldChanged(_ERRORS, this.errors);
    if (index == -1) {
      this.errors.add(val);
    } else {
      this.errors.add(((int) index), val);
    }
  }

  public void removeFromErrors(String val) {
    collFieldChanged(_ERRORS, this.errors);
    this.errors.remove(val);
  }

  public List<Creatable> getItems() {
    return items;
  }

  public List<TypeAndId> getItemsRef() {
    return itemsRef;
  }

  public void setItems(List<Creatable> items) {
    collFieldChanged(_ITEMS, this.items);
    this.items = items;
  }

  public void setItemsRef(List<TypeAndId> itemsRef) {
    collFieldChanged(_ITEMS, this.itemsRef);
    this.itemsRef = itemsRef;
  }

  public void addToItems(Creatable val, long index) {
    collFieldChanged(_ITEMS, this.items);
    if (index == -1) {
      this.items.add(val);
    } else {
      this.items.add(((int) index), val);
    }
  }

  public void addToItemsRef(TypeAndId val, long index) {
    collFieldChanged(_ITEMS, this.itemsRef);
    if (index == -1) {
      this.itemsRef.add(val);
    } else {
      this.itemsRef.add(((int) index), val);
    }
  }

  public void removeFromItems(Creatable val) {
    collFieldChanged(_ITEMS, this.items);
    this.items.remove(val);
  }

  public void removeFromItemsRef(TypeAndId val) {
    collFieldChanged(_ITEMS, this.itemsRef);
    this.itemsRef.remove(val);
  }

  @Override
  public int _typeIdx() {
    return SchemaConstants.Creatables;
  }

  @Override
  public String _type() {
    return "Creatables";
  }

  @Override
  public int _fieldsCount() {
    return 3;
  }

  public void _convertToObjectRef() {
    this.itemsRef = TypeAndId.fromList(this.items);
    this.items.clear();
  }
}
