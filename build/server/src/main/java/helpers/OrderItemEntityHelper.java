package helpers;

import models.InventoryItem;
import models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.jpa.OrderItemRepository;
import repository.jpa.OrderRepository;
import rest.GraphQLInputContext;
import store.EntityHelper;
import store.EntityMutator;
import store.EntityValidationContext;

@Service("OrderItem")
public class OrderItemEntityHelper<T extends OrderItem> implements EntityHelper<T> {
  @Autowired protected EntityMutator mutator;
  @Autowired private OrderItemRepository orderItemRepository;
  @Autowired private OrderRepository orderRepository;

  public void setMutator(EntityMutator obj) {
    mutator = obj;
  }

  public OrderItem newInstance() {
    return new OrderItem();
  }

  @Override
  public void fromInput(T entity, GraphQLInputContext ctx) {
    if (ctx.has("item")) {
      entity.setItem(ctx.readRef("item", "InventoryItem"));
    }
    if (ctx.has("quantity")) {
      entity.setQuantity(ctx.readInteger("quantity"));
    }
  }

  public void referenceFromValidations(T entity, EntityValidationContext validationContext) {}

  public void validateFieldItem(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    InventoryItem it = entity.getItem();
    if (it == null) {
      validationContext.addFieldError("item", "item is required.");
      return;
    }
  }

  public void validateFieldQuantity(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    long it = entity.getQuantity();
    if (onCreate || onUpdate) {
      if (!(it > 0l)) {
        validationContext.addFieldError("quantity", "Quantity must be positive");
      }
    }
  }

  public void validateInternal(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    validateFieldItem(entity, validationContext, onCreate, onUpdate);
    validateFieldQuantity(entity, validationContext, onCreate, onUpdate);
  }

  public void validateOnCreate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, true, false);
  }

  public void validateOnUpdate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, false, true);
  }

  public void computeAmount(T entity) {
    try {
      entity.setAmount(entity.getItem().getPrice() * entity.getQuantity());
    } catch (RuntimeException e) {
    }
  }

  @Override
  public T clone(T entity) {
    return null;
  }

  @Override
  public T getById(long id) {
    return id == 0l ? null : ((T) orderItemRepository.findById(id).orElse(null));
  }

  @Override
  public void setDefaults(T entity) {}

  @Override
  public void compute(T entity) {
    this.computeAmount(entity);
  }

  public Boolean onDelete(T entity, boolean internal, EntityValidationContext deletionContext) {
    return true;
  }

  @Override
  public Boolean onCreate(T entity, boolean internal) {
    return true;
  }

  @Override
  public Boolean onUpdate(T entity, boolean internal) {
    return true;
  }
}
