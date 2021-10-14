package models;

import d3e.core.CloneContext;
import d3e.core.SchemaConstants;
import java.util.Objects;
import java.util.function.Consumer;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.solr.core.mapping.SolrDocument;
import store.DatabaseObject;
import store.ICloneable;

@SolrDocument(collection = "InventoryItem")
@Entity
public class InventoryItem extends CreatableObject {
  public static final int _NAME = 0;
  public static final int _PRICE = 1;
  @Field @NotNull private String name;

  @Field
  @ColumnDefault("0.0")
  private double price = 0.0d;

  private transient InventoryItem old;

  @Override
  public int _typeIdx() {
    return SchemaConstants.InventoryItem;
  }

  @Override
  public String _type() {
    return "InventoryItem";
  }

  @Override
  public int _fieldsCount() {
    return 2;
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    if (Objects.equals(this.name, name)) {
      return;
    }
    fieldChanged(_NAME, this.name);
    this.name = name;
  }

  public double getPrice() {
    return this.price;
  }

  public void setPrice(double price) {
    if (Objects.equals(this.price, price)) {
      return;
    }
    fieldChanged(_PRICE, this.price);
    this.price = price;
  }

  public InventoryItem getOld() {
    return this.old;
  }

  public void setOld(DatabaseObject old) {
    this.old = ((InventoryItem) old);
  }

  public String displayName() {
    return "InventoryItem";
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof InventoryItem && super.equals(a);
  }

  public InventoryItem deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    super.deepCloneIntoObj(dbObj, ctx);
    InventoryItem _obj = ((InventoryItem) dbObj);
    _obj.setName(name);
    _obj.setPrice(price);
  }

  public InventoryItem cloneInstance(InventoryItem cloneObj) {
    if (cloneObj == null) {
      cloneObj = new InventoryItem();
    }
    super.cloneInstance(cloneObj);
    cloneObj.setName(this.getName());
    cloneObj.setPrice(this.getPrice());
    return cloneObj;
  }

  public InventoryItem createNewInstance() {
    return new InventoryItem();
  }

  public boolean needOldObject() {
    return true;
  }

  @Override
  public boolean _isEntity() {
    return true;
  }
}
