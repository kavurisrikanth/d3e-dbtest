package helpers;

import classes.PaymentStatus;
import d3e.core.IterableExt;
import d3e.core.ListExt;
import java.time.LocalDateTime;
import java.util.List;
import models.Customer;
import models.Order;
import models.OrderItem;
import models.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.jpa.CustomerRepository;
import repository.jpa.OrderRepository;
import rest.GraphQLInputContext;
import store.EntityHelper;
import store.EntityMutator;
import store.EntityValidationContext;

@Service("Order")
public class OrderEntityHelper<T extends Order> implements EntityHelper<T> {
  @Autowired protected EntityMutator mutator;
  @Autowired private OrderRepository orderRepository;
  @Autowired private CustomerRepository customerRepository;

  public void setMutator(EntityMutator obj) {
    mutator = obj;
  }

  public Order newInstance() {
    return new Order();
  }

  @Override
  public void fromInput(T entity, GraphQLInputContext ctx) {
    if (ctx.has("customer")) {
      entity.setCustomer(ctx.readRef("customer", "Customer"));
    }
    if (ctx.has("items")) {
      entity.setItems(ctx.readChildColl("items", "OrderItem"));
    }
    if (ctx.has("paymentMethod")) {
      entity.setPaymentMethod(ctx.readRef("paymentMethod", "PaymentMethod"));
    }
    if (ctx.has("paymentStatus")) {
      entity.setPaymentStatus(ctx.readEnum("paymentStatus", PaymentStatus.class));
    }
    if (ctx.has("createdDate")) {
      entity.setCreatedDate(ctx.readDateTime("createdDate"));
    }
    entity.updateMasters((o) -> {});
  }

  public List<PaymentMethod> computePaymentMethodReferenceFrom(T entity) {
    return entity.getCustomer().getPaymentMethods();
  }

  public void referenceFromValidations(T entity, EntityValidationContext validationContext) {
    if (entity.getPaymentMethod() != null
        && !(IterableExt.contains(
            entity.getCustomer().getPaymentMethods(), entity.getPaymentMethod()))) {
      validationContext.addFieldError(
          "paymentMethod", "paymentMethod referenceFrom validation error");
    }
  }

  public void validateFieldCustomer(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    Customer it = entity.getCustomer();
    if (it == null) {
      validationContext.addFieldError("customer", "customer is required.");
      return;
    }
  }

  public void validateFieldPaymentMethod(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    PaymentMethod it = entity.getPaymentMethod();
    if (it == null) {
      validationContext.addFieldError("paymentMethod", "paymentMethod is required.");
      return;
    }
  }

  public void validateInternal(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    referenceFromValidations(entity, validationContext);
    validateFieldCustomer(entity, validationContext, onCreate, onUpdate);
    validateFieldPaymentMethod(entity, validationContext, onCreate, onUpdate);
    long itemsIndex = 0l;
    for (OrderItem obj : entity.getItems()) {
      OrderItemEntityHelper helper = mutator.getHelperByInstance(obj);
      if (onCreate) {
        helper.validateOnCreate(obj, validationContext.child("items", null, itemsIndex++));
      } else {
        helper.validateOnUpdate(obj, validationContext.child("items", null, itemsIndex++));
      }
    }
  }

  public void validateOnCreate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, true, false);
  }

  public void validateOnUpdate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, false, true);
  }

  public void computeTotalAmount(T entity) {
    try {
      entity.setTotalAmount(
          ListExt.fold(
              entity.getItems(),
              0.0d,
              (i, a) -> {
                return i + a.getAmount();
              }));
    } catch (RuntimeException e) {
    }
  }

  @Override
  public T clone(T entity) {
    return null;
  }

  @Override
  public T getById(long id) {
    return id == 0l ? null : ((T) orderRepository.findById(id).orElse(null));
  }

  @Override
  public void setDefaults(T entity) {
    for (OrderItem obj : entity.getItems()) {
      OrderItemEntityHelper helper = mutator.getHelperByInstance(obj);
      helper.setDefaults(obj);
    }
  }

  @Override
  public void compute(T entity) {
    for (OrderItem obj : entity.getItems()) {
      OrderItemEntityHelper helper = mutator.getHelperByInstance(obj);
      helper.compute(obj);
    }
    this.computeTotalAmount(entity);
  }

  public Boolean onDelete(T entity, boolean internal, EntityValidationContext deletionContext) {
    if (entity.getCustomer() != null) {
      entity.getCustomer().removeFromOrders(entity);
    }
    return true;
  }

  @Override
  public Boolean onCreate(T entity, boolean internal) {
    entity.setCreatedDate(LocalDateTime.now());
    return true;
  }

  @Override
  public Boolean onUpdate(T entity, boolean internal) {
    return true;
  }

  public T getOld(long id) {
    return ((T) getById(id).clone());
  }
}
