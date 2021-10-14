package helpers;

import models.Order;
import models.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.jpa.CustomerRepository;
import repository.jpa.OrderRepository;
import repository.jpa.PaymentMethodRepository;
import rest.GraphQLInputContext;
import store.EntityHelper;
import store.EntityMutator;
import store.EntityValidationContext;

@Service("PaymentMethod")
public class PaymentMethodEntityHelper<T extends PaymentMethod> implements EntityHelper<T> {
  @Autowired protected EntityMutator mutator;
  @Autowired private PaymentMethodRepository paymentMethodRepository;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private OrderRepository orderRepository;

  public void setMutator(EntityMutator obj) {
    mutator = obj;
  }

  @Override
  public void fromInput(T entity, GraphQLInputContext ctx) {}

  public void referenceFromValidations(T entity, EntityValidationContext validationContext) {}

  public void validateInternal(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {}

  public void validateOnCreate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, true, false);
  }

  public void validateOnUpdate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, false, true);
  }

  @Override
  public T clone(T entity) {
    return null;
  }

  @Override
  public T getById(long id) {
    return id == 0l ? null : ((T) paymentMethodRepository.findById(id).orElse(null));
  }

  @Override
  public void setDefaults(T entity) {}

  @Override
  public void compute(T entity) {}

  private void deletePaymentMethodInOrder(T entity, EntityValidationContext deletionContext) {
    if (EntityHelper.haveUnDeleted(this.orderRepository.getByPaymentMethod(entity))) {
      deletionContext.addEntityError(
          "This PaymentMethod cannot be deleted as it is being referred to by Order.");
    }
  }

  public Boolean onDelete(T entity, boolean internal, EntityValidationContext deletionContext) {
    return true;
  }

  public void validateOnDelete(T entity, EntityValidationContext deletionContext) {
    this.deletePaymentMethodInOrder(entity, deletionContext);
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
