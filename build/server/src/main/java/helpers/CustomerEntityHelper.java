package helpers;

import classes.Gender;
import classes.Util;
import d3e.core.D3EResourceHandler;
import d3e.core.DFile;
import java.time.LocalDateTime;
import models.Customer;
import models.Order;
import models.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.jpa.CustomerRepository;
import repository.jpa.DFileRepository;
import repository.jpa.OrderRepository;
import rest.GraphQLInputContext;
import store.EntityHelper;
import store.EntityMutator;
import store.EntityValidationContext;

@Service("Customer")
public class CustomerEntityHelper<T extends Customer> implements EntityHelper<T> {
  @Autowired protected EntityMutator mutator;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private OrderRepository orderRepository;
  @Autowired private DFileRepository dFileRepository;
  @Autowired private D3EResourceHandler resourceHandler;

  public void setMutator(EntityMutator obj) {
    mutator = obj;
  }

  public Customer newInstance() {
    return new Customer();
  }

  @Override
  public void fromInput(T entity, GraphQLInputContext ctx) {
    if (ctx.has("name")) {
      entity.setName(ctx.readString("name"));
    }
    if (ctx.has("dob")) {
      entity.setDob(ctx.readDateTime("dob"));
    }
    if (ctx.has("gender")) {
      entity.setGender(ctx.readEnum("gender", Gender.class));
    }
    if (ctx.has("guardian")) {
      entity.setGuardian(ctx.readRef("guardian", "Customer"));
    }
    if (ctx.has("guardianAgreement")) {
      entity.setGuardianAgreement(ctx.readDFile("guardianAgreement"));
    }
    if (ctx.has("paymentMethods")) {
      entity.setPaymentMethods(ctx.readUnionColl("paymentMethods", "PaymentMethod"));
    }
    entity.updateMasters((o) -> {});
  }

  public void referenceFromValidations(T entity, EntityValidationContext validationContext) {}

  public void validateFieldName(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    String it = entity.getName();
    if (it == null) {
      validationContext.addFieldError("name", "name is required.");
      return;
    }
  }

  public void validateFieldDob(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    LocalDateTime it = entity.getDob();
    if (it == null) {
      validationContext.addFieldError("dob", "dob is required.");
      return;
    }
  }

  public void validateFieldGuardian(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    Customer it = entity.getGuardian();
    if (it == null) {
      if (isGuardianExists(entity)) {
        validationContext.addFieldError("guardian", "guardian is required.");
      } else {
        entity.setGuardian(null);
      }
      return;
    }
  }

  public void validateFieldGuardianAgreement(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    DFile it = entity.getGuardianAgreement();
    if (it == null) {
      if (isGuardianAgreementExists(entity)) {
        validationContext.addFieldError("guardianAgreement", "guardianAgreement is required.");
      } else {
        entity.setGuardianAgreement(null);
      }
      return;
    }
  }

  public void validateInternal(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    validateFieldName(entity, validationContext, onCreate, onUpdate);
    validateFieldDob(entity, validationContext, onCreate, onUpdate);
    if (isGuardianExists(entity)) {
      validateFieldGuardian(entity, validationContext, onCreate, onUpdate);
    }
    if (isGuardianAgreementExists(entity)) {
      validateFieldGuardianAgreement(entity, validationContext, onCreate, onUpdate);
    }
    long paymentMethodsIndex = 0l;
    for (PaymentMethod obj : entity.getPaymentMethods()) {
      PaymentMethodEntityHelper helper = mutator.getHelperByInstance(obj);
      if (onCreate) {
        helper.validateOnCreate(
            obj, validationContext.child("paymentMethods", null, paymentMethodsIndex++));
      } else {
        helper.validateOnUpdate(
            obj, validationContext.child("paymentMethods", null, paymentMethodsIndex++));
      }
    }
    isGuardianExists(entity);
    isGuardianAgreementExists(entity);
  }

  public void validateOnCreate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, true, false);
  }

  public void validateOnUpdate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, false, true);
  }

  public void computeUnderage(T entity) {
    try {
      entity.setUnderage(Util.isUnderAge(entity));
    } catch (RuntimeException e) {
    }
  }

  public boolean isGuardianExists(T entity) {
    try {
      if (entity.isUnderage()) {
        return true;
      } else {
        entity.setGuardian(null);
        return false;
      }
    } catch (RuntimeException e) {
      return false;
    }
  }

  public boolean isGuardianAgreementExists(T entity) {
    try {
      if (entity.isUnderage()) {
        return true;
      } else {
        entity.setGuardianAgreement(null);
        return false;
      }
    } catch (RuntimeException e) {
      return false;
    }
  }

  @Override
  public T clone(T entity) {
    return null;
  }

  @Override
  public T getById(long id) {
    return id == 0l ? null : ((T) customerRepository.findById(id).orElse(null));
  }

  @Override
  public void setDefaults(T entity) {
    for (PaymentMethod obj : entity.getPaymentMethods()) {
      PaymentMethodEntityHelper helper = mutator.getHelperByInstance(obj);
      helper.setDefaults(obj);
    }
  }

  @Override
  public void compute(T entity) {
    this.computeUnderage(entity);
    for (PaymentMethod obj : entity.getPaymentMethods()) {
      PaymentMethodEntityHelper helper = mutator.getHelperByInstance(obj);
      helper.compute(obj);
    }
  }

  private void deleteGuardianInCustomer(T entity, EntityValidationContext deletionContext) {
    if (EntityHelper.haveUnDeleted(this.customerRepository.getByGuardian(entity))) {
      deletionContext.addEntityError(
          "This Customer cannot be deleted as it is being referred to by Customer.");
    }
  }

  private void deleteCustomerInOrder(T entity, EntityValidationContext deletionContext) {
    if (EntityHelper.haveUnDeleted(this.orderRepository.getByCustomer(entity))) {
      deletionContext.addEntityError(
          "This Customer cannot be deleted as it is being referred to by Order.");
    }
  }

  public Boolean onDelete(T entity, boolean internal, EntityValidationContext deletionContext) {
    return true;
  }

  public void validateOnDelete(T entity, EntityValidationContext deletionContext) {
    this.deleteGuardianInCustomer(entity, deletionContext);
    this.deleteCustomerInOrder(entity, deletionContext);
  }

  public void performFileAction(T entity) {
    if (entity.getGuardianAgreement() != null) {
      entity.setGuardianAgreement(resourceHandler.save(entity.getGuardianAgreement()));
    }
  }

  @Override
  public Boolean onCreate(T entity, boolean internal) {
    performFileAction(entity);
    return true;
  }

  @Override
  public Boolean onUpdate(T entity, boolean internal) {
    performFileAction(entity);
    return true;
  }

  public T getOld(long id) {
    return ((T) getById(id).clone());
  }
}
