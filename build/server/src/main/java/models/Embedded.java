package models;

import d3e.core.CloneContext;
import d3e.core.SchemaConstants;
import java.util.Objects;
import java.util.function.Consumer;
import javax.persistence.Embeddable;
import org.apache.solr.client.solrj.beans.Field;
import store.DBObject;
import store.DatabaseObject;
import store.ICloneable;

@Embeddable
public class Embedded extends DBObject implements ICloneable {
  public static final int _EMBNAME = 0;
  @Field private String embName;
  private transient Creatable masterCreatable;
  private transient NonCreatable masterNonCreatable;

  @Override
  public int _typeIdx() {
    return SchemaConstants.Embedded;
  }

  @Override
  public String _type() {
    return "Embedded";
  }

  @Override
  public int _fieldsCount() {
    return 1;
  }

  public DatabaseObject _masterObject() {
    if (masterCreatable != null) {
      return masterCreatable;
    }
    if (masterNonCreatable != null) {
      return masterNonCreatable;
    }
    return null;
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {}

  public String getEmbName() {
    _checkProxy();
    return this.embName;
  }

  public void setEmbName(String embName) {
    if (Objects.equals(this.embName, embName)) {
      return;
    }
    fieldChanged(_EMBNAME, this.embName);
    this.embName = embName;
  }

  public Creatable getMasterCreatable() {
    return this.masterCreatable;
  }

  public void setMasterCreatable(Creatable masterCreatable) {
    this.masterCreatable = masterCreatable;
  }

  public NonCreatable getMasterNonCreatable() {
    return this.masterNonCreatable;
  }

  public void setMasterNonCreatable(NonCreatable masterNonCreatable) {
    this.masterNonCreatable = masterNonCreatable;
  }

  public String displayName() {
    return "Embedded";
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof Embedded && super.equals(a);
  }

  public Embedded deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    Embedded _obj = ((Embedded) dbObj);
    _obj.setEmbName(embName);
  }

  public Embedded cloneInstance(Embedded cloneObj) {
    if (cloneObj == null) {
      cloneObj = new Embedded();
    }
    cloneObj.setEmbName(this.getEmbName());
    return cloneObj;
  }

  public void clear() {
    this.embName = "";
  }

  public boolean emptyEmbeddedModel() {
    if (this.embName != null) {
      return false;
    }
    return true;
  }

  public Embedded createNewInstance() {
    return new Embedded();
  }
}
