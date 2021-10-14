package models;

import d3e.core.CloneContext;
import d3e.core.SchemaConstants;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.ColumnDefault;
import store.DatabaseObject;
import store.ICloneable;

@Entity
public class OrderItem extends DatabaseObject {
  public static final int _ITEM = 0;
  public static final int _QUANTITY = 1;
  public static final int _AMOUNT = 2;

  @Field
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private InventoryItem item;

  @Field
  @ColumnDefault("0")
  private long quantity = 0l;

  @Field
  @ColumnDefault("0.0")
  private double amount = 0.0d;

  @Field @ManyToOne private Order masterOrder;
  private transient OrderItem old;

  @Override
  public int _typeIdx() {
    return SchemaConstants.OrderItem;
  }

  @Override
  public String _type() {
    return "OrderItem";
  }

  @Override
  public int _fieldsCount() {
    return 3;
  }

  public DatabaseObject _masterObject() {
    if (masterOrder != null) {
      return masterOrder;
    }
    return null;
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
  }

  public void updateFlat(DatabaseObject obj) {
    super.updateFlat(obj);
    if (masterOrder != null) {
      masterOrder.updateFlat(obj);
    }
  }

  public InventoryItem getItem() {
    return this.item;
  }

  public void setItem(InventoryItem item) {
    if (Objects.equals(this.item, item)) {
      return;
    }
    fieldChanged(_ITEM, this.item);
    this.item = item;
  }

  public long getQuantity() {
    return this.quantity;
  }

  public void setQuantity(long quantity) {
    if (Objects.equals(this.quantity, quantity)) {
      return;
    }
    fieldChanged(_QUANTITY, this.quantity);
    this.quantity = quantity;
  }

  public double getAmount() {
    return this.amount;
  }

  public void setAmount(double amount) {
    if (Objects.equals(this.amount, amount)) {
      return;
    }
    fieldChanged(_AMOUNT, this.amount);
    this.amount = amount;
  }

  public Order getMasterOrder() {
    return this.masterOrder;
  }

  public void setMasterOrder(Order masterOrder) {
    this.masterOrder = masterOrder;
  }

  public OrderItem getOld() {
    return this.old;
  }

  public void setOld(DatabaseObject old) {
    this.old = ((OrderItem) old);
  }

  public String displayName() {
    return "OrderItem";
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof OrderItem && super.equals(a);
  }

  public OrderItem deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    super.deepCloneIntoObj(dbObj, ctx);
    OrderItem _obj = ((OrderItem) dbObj);
    _obj.setItem(item);
    _obj.setQuantity(quantity);
    _obj.setAmount(amount);
  }

  public OrderItem cloneInstance(OrderItem cloneObj) {
    if (cloneObj == null) {
      cloneObj = new OrderItem();
    }
    super.cloneInstance(cloneObj);
    cloneObj.setItem(this.getItem());
    cloneObj.setQuantity(this.getQuantity());
    cloneObj.setAmount(this.getAmount());
    return cloneObj;
  }

  public OrderItem createNewInstance() {
    return new OrderItem();
  }

  public boolean needOldObject() {
    return true;
  }

  public void collectCreatableReferences(List<Object> _refs) {
    super.collectCreatableReferences(_refs);
    _refs.add(this.item);
  }

  @Override
  public boolean _isEntity() {
    return true;
  }
}
