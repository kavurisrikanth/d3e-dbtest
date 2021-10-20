package models;

import d3e.core.CloneContext;
import d3e.core.SchemaConstants;
import java.util.Objects;
import java.util.function.Consumer;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.ChildDocument;
import store.DatabaseObject;
import store.ICloneable;

@Entity
public class NonCreatable extends DatabaseObject {
  public static final int _NAME = 0;
  public static final int _EMB = 1;
  @Field private String name = "NonCreatable name";
  @Field @ChildDocument @javax.persistence.Embedded private Embedded emb = new Embedded();
  @Field @ManyToOne private Creatable masterCreatable;
  private transient NonCreatable old;

  @Override
  public int _typeIdx() {
    return SchemaConstants.NonCreatable;
  }

  @Override
  public String _type() {
    return "NonCreatable";
  }

  @Override
  public int _fieldsCount() {
    return 2;
  }

  public DatabaseObject _masterObject() {
    if (masterCreatable != null) {
      return masterCreatable;
    }
    return null;
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
    if (emb != null) {
      emb.setMasterNonCreatable(this);
      emb.updateMasters(visitor);
    }
  }

  public void updateFlat(DatabaseObject obj) {
    super.updateFlat(obj);
    if (masterCreatable != null) {
      masterCreatable.updateFlat(obj);
    }
  }

  public String getName() {
    _checkProxy();
    return this.name;
  }

  public void setName(String name) {
    if (Objects.equals(this.name, name)) {
      return;
    }
    fieldChanged(_NAME, this.name);
    this.name = name;
  }

  public Embedded getEmb() {
    _checkProxy();
    return this.emb;
  }

  public void setEmb(Embedded emb) {
    if (Objects.equals(this.emb, emb)) {
      return;
    }
    fieldChanged(_EMB, this.emb);
    if (emb == null) {
      emb = new Embedded();
    }
    this.emb = emb;
    if (this.emb != null) {
      this.emb.setMasterNonCreatable(this);
      this.emb._setChildIdx(_EMB);
    }
  }

  public Creatable getMasterCreatable() {
    return this.masterCreatable;
  }

  public void setMasterCreatable(Creatable masterCreatable) {
    this.masterCreatable = masterCreatable;
  }

  public NonCreatable getOld() {
    return this.old;
  }

  public void setOld(DatabaseObject old) {
    this.old = ((NonCreatable) old);
  }

  public String displayName() {
    return "NonCreatable";
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof NonCreatable && super.equals(a);
  }

  public NonCreatable deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void collectChildValues(CloneContext ctx) {
    super.collectChildValues(ctx);
    ctx.collectChild(emb);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    super.deepCloneIntoObj(dbObj, ctx);
    NonCreatable _obj = ((NonCreatable) dbObj);
    _obj.setName(name);
    ctx.cloneChild(emb, (v) -> _obj.setEmb(v));
  }

  public NonCreatable cloneInstance(NonCreatable cloneObj) {
    if (cloneObj == null) {
      cloneObj = new NonCreatable();
    }
    super.cloneInstance(cloneObj);
    cloneObj.setName(this.getName());
    cloneObj.setEmb(this.getEmb().cloneInstance(null));
    return cloneObj;
  }

  public NonCreatable createNewInstance() {
    return new NonCreatable();
  }

  public boolean needOldObject() {
    return true;
  }

  @Override
  public boolean _isEntity() {
    return true;
  }

  @Override
  protected void _handleChildChange(int _childIdx) {
    switch (_childIdx) {
      case _EMB:
        {
          this.fieldChanged(_childIdx, this.emb);
          break;
        }
    }
  }
}
