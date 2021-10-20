package helpers;

import models.NonCreatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.jpa.CreatableRepository;
import repository.jpa.NonCreatableRepository;
import rest.GraphQLInputContext;
import store.EntityHelper;
import store.EntityMutator;
import store.EntityValidationContext;

@Service("NonCreatable")
public class NonCreatableEntityHelper<T extends NonCreatable> implements EntityHelper<T> {
  @Autowired protected EntityMutator mutator;
  @Autowired private NonCreatableRepository nonCreatableRepository;
  @Autowired private CreatableRepository creatableRepository;

  public void setMutator(EntityMutator obj) {
    mutator = obj;
  }

  public NonCreatable newInstance() {
    return new NonCreatable();
  }

  @Override
  public void fromInput(T entity, GraphQLInputContext ctx) {
    if (ctx.has("name")) {
      entity.setName(ctx.readString("name"));
    }
    if (ctx.has("emb")) {
      entity.setEmb(ctx.readEmbedded("emb", "Embedded", entity.getEmb()));
    }
  }

  public void referenceFromValidations(T entity, EntityValidationContext validationContext) {}

  public void validateInternal(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    long embIndex = 0l;
    if (entity.getEmb() != null) {
      EmbeddedEntityHelper helper = mutator.getHelperByInstance(entity.getEmb());
      if (onCreate) {
        helper.validateOnCreate(entity.getEmb(), validationContext.child("emb", null, 0l));
      } else {
        helper.validateOnUpdate(entity.getEmb(), validationContext.child("emb", null, 0l));
      }
    }
  }

  public void validateOnCreate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, true, false);
  }

  public void validateOnUpdate(T entity, EntityValidationContext validationContext) {
    validateInternal(entity, validationContext, false, true);
  }

  public void setDefaultName(T entity) {
    if (entity.getName() != null && !(entity.getName().isEmpty())) {
      return;
    }
    entity.setName("NonCreatable name");
  }

  @Override
  public T clone(T entity) {
    return null;
  }

  @Override
  public T getById(long id) {
    return id == 0l ? null : ((T) nonCreatableRepository.findById(id));
  }

  @Override
  public void setDefaults(T entity) {
    this.setDefaultName(entity);
    if (entity.getEmb() != null) {
      EmbeddedEntityHelper helper = mutator.getHelperByInstance(entity.getEmb());
      helper.setDefaults(entity.getEmb());
    }
  }

  @Override
  public void compute(T entity) {
    if (entity.getEmb() != null) {
      EmbeddedEntityHelper helper = mutator.getHelperByInstance(entity.getEmb());
      helper.compute(entity.getEmb());
    }
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
