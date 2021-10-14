package models;

import d3e.core.CloneContext;
import d3e.core.SchemaConstants;
import java.util.function.Consumer;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.apache.solr.client.solrj.beans.Field;
import store.DatabaseObject;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentMethod extends DatabaseObject {
  @Field @ManyToOne private Customer masterCustomer;

  @Override
  public int _typeIdx() {
    return SchemaConstants.PaymentMethod;
  }

  @Override
  public String _type() {
    return "PaymentMethod";
  }

  @Override
  public int _fieldsCount() {
    return 0;
  }

  public DatabaseObject _masterObject() {
    if (masterCustomer != null) {
      return masterCustomer;
    }
    return null;
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
  }

  public void updateFlat(DatabaseObject obj) {
    super.updateFlat(obj);
    if (masterCustomer != null) {
      masterCustomer.updateFlat(obj);
    }
  }

  public Customer getMasterCustomer() {
    return this.masterCustomer;
  }

  public void setMasterCustomer(Customer masterCustomer) {
    this.masterCustomer = masterCustomer;
  }

  public String displayName() {
    return "PaymentMethod";
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof PaymentMethod && super.equals(a);
  }

  public PaymentMethod deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public PaymentMethod cloneInstance(PaymentMethod cloneObj) {
    super.cloneInstance(cloneObj);
    return cloneObj;
  }

  @Override
  public boolean _isEntity() {
    return true;
  }
}
