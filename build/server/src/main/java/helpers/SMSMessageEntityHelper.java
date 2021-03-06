package helpers;

import models.SMSMessage;
import org.springframework.stereotype.Service;
import rest.GraphQLInputContext;
import store.EntityValidationContext;

@Service("SMSMessage")
public class SMSMessageEntityHelper<T extends SMSMessage> extends D3EMessageEntityHelper<T> {
  public SMSMessage newInstance() {
    return new SMSMessage();
  }

  @Override
  public void fromInput(T entity, GraphQLInputContext ctx) {
    if (ctx.has("from")) {
      entity.setFrom(ctx.readString("from"));
    }
    if (ctx.has("to")) {
      entity.setTo(ctx.readStringColl("to"));
    }
    if (ctx.has("body")) {
      entity.setBody(ctx.readString("body"));
    }
    if (ctx.has("createdOn")) {
      entity.setCreatedOn(ctx.readDateTime("createdOn"));
    }
    if (ctx.has("dltTemplateId")) {
      entity.setDltTemplateId(ctx.readString("dltTemplateId"));
    }
    entity.updateMasters((o) -> {});
  }

  public void referenceFromValidations(T entity, EntityValidationContext validationContext) {}

  public void validateInternal(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    super.validateInternal(entity, validationContext, onCreate, onUpdate);
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
    return null;
  }

  @Override
  public void setDefaults(T entity) {
    this.setDefaultCreatedOn(entity);
  }

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

  public T getOld(long id) {
    return ((T) getById(id).clone());
  }
}
