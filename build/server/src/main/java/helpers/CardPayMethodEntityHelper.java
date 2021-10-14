package helpers;

import classes.Util;
import java.time.LocalDateTime;
import models.CardPayMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.jpa.CardPayMethodRepository;
import rest.GraphQLInputContext;
import store.EntityValidationContext;

@Service("CardPayMethod")
public class CardPayMethodEntityHelper<T extends CardPayMethod>
    extends PaymentMethodEntityHelper<T> {
  @Autowired private CardPayMethodRepository cardPayMethodRepository;

  public CardPayMethod newInstance() {
    return new CardPayMethod();
  }

  @Override
  public void fromInput(T entity, GraphQLInputContext ctx) {
    if (ctx.has("cardNumber")) {
      entity.setCardNumber(ctx.readString("cardNumber"));
    }
    if (ctx.has("validTill")) {
      entity.setValidTill(ctx.readDateTime("validTill"));
    }
    if (ctx.has("cvv")) {
      entity.setCvv(ctx.readString("cvv"));
    }
    if (ctx.has("nameOnCard")) {
      entity.setNameOnCard(ctx.readString("nameOnCard"));
    }
  }

  public void referenceFromValidations(T entity, EntityValidationContext validationContext) {}

  public void validateFieldCardNumber(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    String it = entity.getCardNumber();
    if (it == null) {
      validationContext.addFieldError("cardNumber", "cardNumber is required.");
      return;
    }
  }

  public void validateFieldValidTill(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    LocalDateTime it = entity.getValidTill();
    if (it == null) {
      validationContext.addFieldError("validTill", "validTill is required.");
      return;
    }
    if (onCreate || onUpdate) {
      if (!(Util.isValidCard(entity))) {
        validationContext.addFieldError("validTill", "Card must be valid");
      }
    }
  }

  public void validateFieldCvv(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    String it = entity.getCvv();
    if (it == null) {
      validationContext.addFieldError("cvv", "cvv is required.");
      return;
    }
  }

  public void validateFieldNameOnCard(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    String it = entity.getNameOnCard();
    if (it == null) {
      validationContext.addFieldError("nameOnCard", "nameOnCard is required.");
      return;
    }
  }

  public void validateInternal(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    super.validateInternal(entity, validationContext, onCreate, onUpdate);
    validateFieldCardNumber(entity, validationContext, onCreate, onUpdate);
    validateFieldValidTill(entity, validationContext, onCreate, onUpdate);
    validateFieldCvv(entity, validationContext, onCreate, onUpdate);
    validateFieldNameOnCard(entity, validationContext, onCreate, onUpdate);
  }

  public void validateOnCreate(T entity, EntityValidationContext validationContext) {
    super.validateOnCreate(entity, validationContext);
  }

  public void validateOnUpdate(T entity, EntityValidationContext validationContext) {
    super.validateOnUpdate(entity, validationContext);
  }

  @Override
  public T clone(T entity) {
    return null;
  }

  @Override
  public T getById(long id) {
    return id == 0l ? null : ((T) cardPayMethodRepository.findById(id).orElse(null));
  }

  @Override
  public void setDefaults(T entity) {}

  @Override
  public void compute(T entity) {}

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
