package helpers;

import d3e.core.D3EResourceHandler;
import java.util.List;
import models.Creatable;
import models.NonCreatable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.jpa.CreatableRepository;
import repository.jpa.DFileRepository;
import rest.GraphQLInputContext;
import store.EntityHelper;
import store.EntityMutator;
import store.EntityValidationContext;

@Service("Creatable")
public class CreatableEntityHelper<T extends Creatable> implements EntityHelper<T> {
  @Autowired protected EntityMutator mutator;
  @Autowired private CreatableRepository creatableRepository;
  @Autowired private DFileRepository dFileRepository;
  @Autowired private D3EResourceHandler resourceHandler;

  public void setMutator(EntityMutator obj) {
    mutator = obj;
  }

  public Creatable newInstance() {
    return new Creatable();
  }

  @Override
  public void fromInput(T entity, GraphQLInputContext ctx) {
    if (ctx.has("name")) {
      entity.setName(ctx.readString("name"));
    }
    if (ctx.has("ref")) {
      entity.setRef(ctx.readRef("ref", "Creatable"));
    }
    if (ctx.has("refColl")) {
      entity.setRefColl(ctx.readRefColl("refColl", "Creatable"));
    }
    if (ctx.has("child")) {
      entity.setChild(ctx.readChild("child", "NonCreatable"));
    }
    if (ctx.has("childColl")) {
      entity.setChildColl(ctx.readChildColl("childColl", "NonCreatable"));
    }
    if (ctx.has("file")) {
      entity.setFile(ctx.readDFile("file"));
    }
    if (ctx.has("emb")) {
      entity.setEmb(ctx.readEmbedded("emb", "Embedded", entity.getEmb()));
    }
    entity.updateMasters((o) -> {});
  }

  public void referenceFromValidations(T entity, EntityValidationContext validationContext) {}

  public void validateInternal(
      T entity, EntityValidationContext validationContext, boolean onCreate, boolean onUpdate) {
    long childIndex = 0l;
    if (entity.getChild() != null) {
      NonCreatableEntityHelper helper = mutator.getHelperByInstance(entity.getChild());
      if (onCreate) {
        helper.validateOnCreate(entity.getChild(), validationContext.child("child", null, 0l));
      } else {
        helper.validateOnUpdate(entity.getChild(), validationContext.child("child", null, 0l));
      }
    }
    long childCollIndex = 0l;
    for (NonCreatable obj : entity.getChildColl()) {
      NonCreatableEntityHelper helper = mutator.getHelperByInstance(obj);
      if (onCreate) {
        helper.validateOnCreate(obj, validationContext.child("childColl", null, childCollIndex++));
      } else {
        helper.validateOnUpdate(obj, validationContext.child("childColl", null, childCollIndex++));
      }
    }
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

  public void computeIsBasic(T entity) {
    try {
      entity.setIsBasic(
          entity.getRef() == null
              && entity.getRefColl().isEmpty()
              && entity.getChild() == null
              && entity.getChildColl().isEmpty()
              && entity.getFile() == null
              && entity.getEmb() == null);
    } catch (RuntimeException e) {
    }
  }

  @Override
  public T clone(T entity) {
    return null;
  }

  @Override
  public T getById(long id) {
    return id == 0l ? null : ((T) creatableRepository.findById(id));
  }

  @Override
  public void setDefaults(T entity) {
    if (entity.getChild() != null) {
      NonCreatableEntityHelper helper = mutator.getHelperByInstance(entity.getChild());
      helper.setDefaults(entity.getChild());
    }
    for (NonCreatable obj : entity.getChildColl()) {
      NonCreatableEntityHelper helper = mutator.getHelperByInstance(obj);
      helper.setDefaults(obj);
    }
    if (entity.getEmb() != null) {
      EmbeddedEntityHelper helper = mutator.getHelperByInstance(entity.getEmb());
      helper.setDefaults(entity.getEmb());
    }
  }

  @Override
  public void compute(T entity) {
    if (entity.getChild() != null) {
      NonCreatableEntityHelper helper = mutator.getHelperByInstance(entity.getChild());
      helper.compute(entity.getChild());
    }
    for (NonCreatable obj : entity.getChildColl()) {
      NonCreatableEntityHelper helper = mutator.getHelperByInstance(obj);
      helper.compute(obj);
    }
    if (entity.getEmb() != null) {
      EmbeddedEntityHelper helper = mutator.getHelperByInstance(entity.getEmb());
      helper.compute(entity.getEmb());
    }
    this.computeIsBasic(entity);
  }

  private void deleteRefInCreatable(T entity, EntityValidationContext deletionContext) {
    if (EntityHelper.haveUnDeleted(this.creatableRepository.getByRef(entity))) {
      deletionContext.addEntityError(
          "This Creatable cannot be deleted as it is being referred to by Creatable.");
    }
  }

  private void deleteRefCollInCreatable(T entity, EntityValidationContext deletionContext) {
    if (EntityHelper.haveUnDeleted(this.creatableRepository.findByRefColl(entity))) {
      deletionContext.addEntityError(
          "This Creatable cannot be deleted as it is being referred to by Creatable.");
    }
  }

  public Boolean onDelete(T entity, boolean internal, EntityValidationContext deletionContext) {
    return true;
  }

  public void validateOnDelete(T entity, EntityValidationContext deletionContext) {
    this.deleteRefInCreatable(entity, deletionContext);
    this.deleteRefCollInCreatable(entity, deletionContext);
  }

  public void performFileAction(T entity) {
    if (entity.getFile() != null) {
      entity.setFile(resourceHandler.save(entity.getFile()));
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
