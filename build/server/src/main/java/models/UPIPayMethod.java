package models;

import d3e.core.CloneContext;
import d3e.core.SchemaConstants;
import java.util.Objects;
import java.util.function.Consumer;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import org.apache.solr.client.solrj.beans.Field;
import store.DatabaseObject;
import store.ICloneable;

@Entity
public class UPIPayMethod extends PaymentMethod {
  public static final int _UPIID = 0;
  @Field @NotNull private String upiId;
  private transient UPIPayMethod old;

  @Override
  public int _typeIdx() {
    return SchemaConstants.UPIPayMethod;
  }

  @Override
  public String _type() {
    return "UPIPayMethod";
  }

  @Override
  public int _fieldsCount() {
    return 1;
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
  }

  public String getUpiId() {
    return this.upiId;
  }

  public void setUpiId(String upiId) {
    if (Objects.equals(this.upiId, upiId)) {
      return;
    }
    fieldChanged(_UPIID, this.upiId);
    this.upiId = upiId;
  }

  public UPIPayMethod getOld() {
    return this.old;
  }

  public void setOld(DatabaseObject old) {
    this.old = ((UPIPayMethod) old);
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof UPIPayMethod && super.equals(a);
  }

  public UPIPayMethod deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    super.deepCloneIntoObj(dbObj, ctx);
    UPIPayMethod _obj = ((UPIPayMethod) dbObj);
    _obj.setUpiId(upiId);
  }

  public UPIPayMethod cloneInstance(UPIPayMethod cloneObj) {
    if (cloneObj == null) {
      cloneObj = new UPIPayMethod();
    }
    super.cloneInstance(cloneObj);
    cloneObj.setUpiId(this.getUpiId());
    return cloneObj;
  }

  public UPIPayMethod createNewInstance() {
    return new UPIPayMethod();
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
    super._handleChildChange(_childIdx);
  }
}
