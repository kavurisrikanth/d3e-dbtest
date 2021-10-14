package models;

import classes.Gender;
import d3e.core.CloneContext;
import d3e.core.DFile;
import d3e.core.SchemaConstants;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.solr.core.mapping.ChildDocument;
import org.springframework.data.solr.core.mapping.SolrDocument;
import store.Database;
import store.DatabaseObject;
import store.ICloneable;

@SolrDocument(collection = "Customer")
@Entity
public class Customer extends CreatableObject {
  public static final int _NAME = 0;
  public static final int _DOB = 1;
  public static final int _UNDERAGE = 2;
  public static final int _GENDER = 3;
  public static final int _GUARDIAN = 4;
  public static final int _GUARDIANAGREEMENT = 5;
  public static final int _ORDERS = 6;
  public static final int _PAYMENTMETHODS = 7;
  @Field @NotNull private String name;
  @Field @NotNull private LocalDateTime dob;

  @Field
  @ColumnDefault("false")
  private boolean underage = false;

  @Field
  @Enumerated(javax.persistence.EnumType.STRING)
  private Gender gender = Gender.Unspecified;

  @Field
  @ManyToOne(fetch = FetchType.LAZY)
  private Customer guardian;

  @Field
  @ManyToOne(fetch = FetchType.LAZY)
  private DFile guardianAgreement;

  @Field
  @ManyToMany(mappedBy = "customer")
  private List<Order> orders = new ArrayList<>();

  @Field
  @ChildDocument
  @OrderColumn
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PaymentMethod> paymentMethods = new ArrayList<>();

  @Override
  public int _typeIdx() {
    return SchemaConstants.Customer;
  }

  @Override
  public String _type() {
    return "Customer";
  }

  @Override
  public int _fieldsCount() {
    return 8;
  }

  public void addToOrders(Order val, long index) {
    collFieldChanged(_ORDERS, this.orders);
    if (index == -1) {
      this.orders.add(val);
    } else {
      this.orders.add(((int) index), val);
    }
  }

  public void removeFromOrders(Order val) {
    collFieldChanged(_ORDERS, this.orders);
    this.orders.remove(val);
  }

  public void addToPaymentMethods(PaymentMethod val, long index) {
    collFieldChanged(_PAYMENTMETHODS, this.paymentMethods);
    val.setMasterCustomer(this);
    val._setChildIdx(_PAYMENTMETHODS);
    if (index == -1) {
      this.paymentMethods.add(val);
    } else {
      this.paymentMethods.add(((int) index), val);
    }
  }

  public void removeFromPaymentMethods(PaymentMethod val) {
    collFieldChanged(_PAYMENTMETHODS, this.paymentMethods);
    val._clearChildIdx();
    this.paymentMethods.remove(val);
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
    for (PaymentMethod obj : this.getPaymentMethods()) {
      visitor.accept(obj);
      obj.setMasterCustomer(this);
      obj.updateMasters(visitor);
    }
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

  public LocalDateTime getDob() {
    return this.dob;
  }

  public void setDob(LocalDateTime dob) {
    if (Objects.equals(this.dob, dob)) {
      return;
    }
    fieldChanged(_DOB, this.dob);
    this.dob = dob;
  }

  public boolean isUnderage() {
    return this.underage;
  }

  public void setUnderage(boolean underage) {
    if (Objects.equals(this.underage, underage)) {
      return;
    }
    fieldChanged(_UNDERAGE, this.underage);
    this.underage = underage;
  }

  public Gender getGender() {
    return this.gender;
  }

  public void setGender(Gender gender) {
    if (Objects.equals(this.gender, gender)) {
      return;
    }
    fieldChanged(_GENDER, this.gender);
    this.gender = gender;
  }

  public Customer getGuardian() {
    return this.guardian;
  }

  public void setGuardian(Customer guardian) {
    if (Objects.equals(this.guardian, guardian)) {
      return;
    }
    fieldChanged(_GUARDIAN, this.guardian);
    this.guardian = guardian;
  }

  public DFile getGuardianAgreement() {
    return this.guardianAgreement;
  }

  public void setGuardianAgreement(DFile guardianAgreement) {
    if (Objects.equals(this.guardianAgreement, guardianAgreement)) {
      return;
    }
    fieldChanged(_GUARDIANAGREEMENT, this.guardianAgreement);
    this.guardianAgreement = guardianAgreement;
  }

  public List<Order> getOrders() {
    return this.orders;
  }

  public void setOrders(List<Order> orders) {
    if (Objects.equals(this.orders, orders)) {
      return;
    }
    collFieldChanged(_ORDERS, this.orders);
    this.orders.clear();
    this.orders.addAll(orders);
  }

  public List<PaymentMethod> getPaymentMethods() {
    return this.paymentMethods;
  }

  public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
    if (Objects.equals(this.paymentMethods, paymentMethods)) {
      return;
    }
    collFieldChanged(_PAYMENTMETHODS, this.paymentMethods);
    this.paymentMethods.clear();
    this.paymentMethods.addAll(paymentMethods);
    this.paymentMethods.forEach(
        (one) -> {
          one.setMasterCustomer(this);
          one._setChildIdx(_PAYMENTMETHODS);
        });
  }

  public String displayName() {
    return "Customer";
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof Customer && super.equals(a);
  }

  public Customer deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void collectChildValues(CloneContext ctx) {
    super.collectChildValues(ctx);
    ctx.collectChilds(paymentMethods);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    super.deepCloneIntoObj(dbObj, ctx);
    Customer _obj = ((Customer) dbObj);
    _obj.setName(name);
    _obj.setDob(dob);
    _obj.setUnderage(underage);
    _obj.setGender(gender);
    _obj.setGuardian(guardian);
    _obj.setGuardianAgreement(guardianAgreement);
    _obj.setOrders(orders);
    ctx.cloneChildList(paymentMethods, (v) -> _obj.setPaymentMethods(v));
  }

  public Customer cloneInstance(Customer cloneObj) {
    if (cloneObj == null) {
      cloneObj = new Customer();
    }
    super.cloneInstance(cloneObj);
    cloneObj.setName(this.getName());
    cloneObj.setDob(this.getDob());
    cloneObj.setUnderage(this.isUnderage());
    cloneObj.setGender(this.getGender());
    cloneObj.setGuardian(this.getGuardian());
    cloneObj.setGuardianAgreement(this.getGuardianAgreement());
    cloneObj.setOrders(new ArrayList<>(this.getOrders()));
    cloneObj.setPaymentMethods(
        this.getPaymentMethods().stream()
            .map((PaymentMethod colObj) -> colObj.cloneInstance(null))
            .collect(Collectors.toList()));
    return cloneObj;
  }

  public Customer createNewInstance() {
    return new Customer();
  }

  public void collectCreatableReferences(List<Object> _refs) {
    super.collectCreatableReferences(_refs);
    _refs.add(this.guardian);
    _refs.add(this.guardianAgreement);
    Database.collectCollctionCreatableReferences(_refs, this.paymentMethods);
  }

  @Override
  public boolean _isEntity() {
    return true;
  }

  @Override
  protected void _handleChildChange(int _childIdx) {
    switch (_childIdx) {
      case _PAYMENTMETHODS:
        {
          this.collFieldChanged(_childIdx, this.paymentMethods);
          break;
        }
    }
  }
}
