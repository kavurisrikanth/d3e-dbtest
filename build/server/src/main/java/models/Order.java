package models;

import classes.PaymentStatus;
import d3e.core.CloneContext;
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

@SolrDocument(collection = "Order")
@Entity
public class Order extends CreatableObject {
  public static final int _CUSTOMER = 0;
  public static final int _ITEMS = 1;
  public static final int _TOTALAMOUNT = 2;
  public static final int _PAYMENTMETHOD = 3;
  public static final int _PAYMENTSTATUS = 4;
  public static final int _CREATEDDATE = 5;

  @Field
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private Customer customer;

  @Field
  @ChildDocument
  @OrderColumn
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items = new ArrayList<>();

  @Field
  @ColumnDefault("0.0")
  private double totalAmount = 0.0d;

  @Field
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private PaymentMethod paymentMethod;

  @Field
  @Enumerated(javax.persistence.EnumType.STRING)
  private PaymentStatus paymentStatus = PaymentStatus.Pending;

  @Field private LocalDateTime createdDate;

  @Override
  public int _typeIdx() {
    return SchemaConstants.Order;
  }

  @Override
  public String _type() {
    return "Order";
  }

  @Override
  public int _fieldsCount() {
    return 6;
  }

  public void addToItems(OrderItem val, long index) {
    collFieldChanged(_ITEMS, this.items);
    val.setMasterOrder(this);
    val._setChildIdx(_ITEMS);
    if (index == -1) {
      this.items.add(val);
    } else {
      this.items.add(((int) index), val);
    }
  }

  public void removeFromItems(OrderItem val) {
    collFieldChanged(_ITEMS, this.items);
    val._clearChildIdx();
    this.items.remove(val);
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
    for (OrderItem obj : this.getItems()) {
      visitor.accept(obj);
      obj.setMasterOrder(this);
      obj.updateMasters(visitor);
    }
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public void setCustomer(Customer customer) {
    if (Objects.equals(this.customer, customer)) {
      return;
    }
    fieldChanged(_CUSTOMER, this.customer);
    if (!(isOld) && this.customer != null) {
      this.customer.removeFromOrders(this);
    }
    this.customer = customer;
    if (!(isOld) && customer != null && !(customer.getOrders().contains(this))) {
      customer.addToOrders(this, -1);
    }
  }

  public List<OrderItem> getItems() {
    return this.items;
  }

  public void setItems(List<OrderItem> items) {
    if (Objects.equals(this.items, items)) {
      return;
    }
    collFieldChanged(_ITEMS, this.items);
    this.items.clear();
    this.items.addAll(items);
    this.items.forEach(
        (one) -> {
          one.setMasterOrder(this);
          one._setChildIdx(_ITEMS);
        });
  }

  public double getTotalAmount() {
    return this.totalAmount;
  }

  public void setTotalAmount(double totalAmount) {
    if (Objects.equals(this.totalAmount, totalAmount)) {
      return;
    }
    fieldChanged(_TOTALAMOUNT, this.totalAmount);
    this.totalAmount = totalAmount;
  }

  public PaymentMethod getPaymentMethod() {
    return this.paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    if (Objects.equals(this.paymentMethod, paymentMethod)) {
      return;
    }
    fieldChanged(_PAYMENTMETHOD, this.paymentMethod);
    this.paymentMethod = paymentMethod;
  }

  public PaymentStatus getPaymentStatus() {
    return this.paymentStatus;
  }

  public void setPaymentStatus(PaymentStatus paymentStatus) {
    if (Objects.equals(this.paymentStatus, paymentStatus)) {
      return;
    }
    fieldChanged(_PAYMENTSTATUS, this.paymentStatus);
    this.paymentStatus = paymentStatus;
  }

  public LocalDateTime getCreatedDate() {
    return this.createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    if (Objects.equals(this.createdDate, createdDate)) {
      return;
    }
    fieldChanged(_CREATEDDATE, this.createdDate);
    this.createdDate = createdDate;
  }

  public String displayName() {
    return "Order";
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof Order && super.equals(a);
  }

  public Order deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void collectChildValues(CloneContext ctx) {
    super.collectChildValues(ctx);
    ctx.collectChilds(items);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    super.deepCloneIntoObj(dbObj, ctx);
    Order _obj = ((Order) dbObj);
    _obj.setCustomer(customer);
    ctx.cloneChildList(items, (v) -> _obj.setItems(v));
    _obj.setTotalAmount(totalAmount);
    _obj.setPaymentMethod(ctx.cloneRef(paymentMethod));
    _obj.setPaymentStatus(paymentStatus);
    _obj.setCreatedDate(createdDate);
  }

  public Order cloneInstance(Order cloneObj) {
    if (cloneObj == null) {
      cloneObj = new Order();
    }
    super.cloneInstance(cloneObj);
    cloneObj.setCustomer(this.getCustomer());
    cloneObj.setItems(
        this.getItems().stream()
            .map((OrderItem colObj) -> colObj.cloneInstance(null))
            .collect(Collectors.toList()));
    cloneObj.setTotalAmount(this.getTotalAmount());
    cloneObj.setPaymentMethod(this.getPaymentMethod());
    cloneObj.setPaymentStatus(this.getPaymentStatus());
    cloneObj.setCreatedDate(this.getCreatedDate());
    return cloneObj;
  }

  public Order createNewInstance() {
    return new Order();
  }

  public void collectCreatableReferences(List<Object> _refs) {
    super.collectCreatableReferences(_refs);
    _refs.add(this.customer);
    Database.collectCollctionCreatableReferences(_refs, this.items);
  }

  @Override
  public boolean _isEntity() {
    return true;
  }

  @Override
  protected void _handleChildChange(int _childIdx) {
    switch (_childIdx) {
      case _ITEMS:
        {
          this.collFieldChanged(_childIdx, this.items);
          break;
        }
    }
  }
}
