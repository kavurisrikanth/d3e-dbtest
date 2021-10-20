package models;

import d3e.core.CloneContext;
import d3e.core.DFile;
import d3e.core.SchemaConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.solr.core.mapping.ChildDocument;
import org.springframework.data.solr.core.mapping.SolrDocument;
import store.Database;
import store.DatabaseObject;
import store.ICloneable;

@SolrDocument(collection = "Creatable")
@Entity
public class Creatable extends CreatableObject {
  public static final int _NAME = 0;
  public static final int _REF = 1;
  public static final int _REFCOLL = 2;
  public static final int _CHILD = 3;
  public static final int _CHILDCOLL = 4;
  public static final int _FILE = 5;
  public static final int _EMB = 6;
  public static final int _ISBASIC = 7;
  @Field private String name;

  @Field
  @ManyToOne(fetch = FetchType.LAZY)
  private Creatable ref;

  @Field @OrderColumn @ManyToMany private List<Creatable> refColl = new ArrayList<>();

  @Field
  @ChildDocument
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  private NonCreatable child;

  @Field
  @ChildDocument
  @OrderColumn
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NonCreatable> childColl = new ArrayList<>();

  @Field
  @ManyToOne(fetch = FetchType.LAZY)
  private DFile file;

  @Field @ChildDocument @javax.persistence.Embedded private Embedded emb = new Embedded();

  @Field
  @ColumnDefault("false")
  private boolean isBasic = false;

  private transient Creatable old;

  @Override
  public int _typeIdx() {
    return SchemaConstants.Creatable;
  }

  @Override
  public String _type() {
    return "Creatable";
  }

  @Override
  public int _fieldsCount() {
    return 8;
  }

  public void addToRefColl(Creatable val, long index) {
    collFieldChanged(_REFCOLL, this.refColl);
    if (index == -1) {
      this.refColl.add(val);
    } else {
      this.refColl.add(((int) index), val);
    }
  }

  public void removeFromRefColl(Creatable val) {
    collFieldChanged(_REFCOLL, this.refColl);
    this.refColl.remove(val);
  }

  public void addToChildColl(NonCreatable val, long index) {
    collFieldChanged(_CHILDCOLL, this.childColl);
    val.setMasterCreatable(this);
    val._setChildIdx(_CHILDCOLL);
    if (index == -1) {
      this.childColl.add(val);
    } else {
      this.childColl.add(((int) index), val);
    }
  }

  public void removeFromChildColl(NonCreatable val) {
    collFieldChanged(_CHILDCOLL, this.childColl);
    val._clearChildIdx();
    this.childColl.remove(val);
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
    if (child != null) {
      visitor.accept(child);
      child.setMasterCreatable(this);
      child.updateMasters(visitor);
    }
    for (NonCreatable obj : this.getChildColl()) {
      visitor.accept(obj);
      obj.setMasterCreatable(this);
      obj.updateMasters(visitor);
    }
    if (emb != null) {
      emb.setMasterCreatable(this);
      emb.updateMasters(visitor);
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

  public Creatable getRef() {
    return this.ref;
  }

  public void setRef(Creatable ref) {
    if (Objects.equals(this.ref, ref)) {
      return;
    }
    fieldChanged(_REF, this.ref);
    this.ref = ref;
  }

  public List<Creatable> getRefColl() {
    return this.refColl;
  }

  public void setRefColl(List<Creatable> refColl) {
    if (Objects.equals(this.refColl, refColl)) {
      return;
    }
    collFieldChanged(_REFCOLL, this.refColl);
    this.refColl.clear();
    this.refColl.addAll(refColl);
  }

  public NonCreatable getChild() {
    return this.child;
  }

  public void setChild(NonCreatable child) {
    if (Objects.equals(this.child, child)) {
      return;
    }
    fieldChanged(_CHILD, this.child);
    this.child = child;
    if (this.child != null) {
      this.child.setMasterCreatable(this);
      this.child._setChildIdx(_CHILD);
    }
  }

  public List<NonCreatable> getChildColl() {
    return this.childColl;
  }

  public void setChildColl(List<NonCreatable> childColl) {
    if (Objects.equals(this.childColl, childColl)) {
      return;
    }
    collFieldChanged(_CHILDCOLL, this.childColl);
    this.childColl.clear();
    this.childColl.addAll(childColl);
    this.childColl.forEach(
        (one) -> {
          one.setMasterCreatable(this);
          one._setChildIdx(_CHILDCOLL);
        });
  }

  public DFile getFile() {
    return this.file;
  }

  public void setFile(DFile file) {
    if (Objects.equals(this.file, file)) {
      return;
    }
    fieldChanged(_FILE, this.file);
    this.file = file;
  }

  public Embedded getEmb() {
    return this.emb;
  }

  public void setEmb(Embedded emb) {
    if (Objects.equals(this.emb, emb)) {
      return;
    }
    fieldChanged(_EMB, this.emb);
    if (emb == null) {
      emb = new Embedded();
    }
    this.emb = emb;
    if (this.emb != null) {
      this.emb.setMasterCreatable(this);
      this.emb._setChildIdx(_EMB);
    }
  }

  public boolean isIsBasic() {
    return this.isBasic;
  }

  public void setIsBasic(boolean isBasic) {
    if (Objects.equals(this.isBasic, isBasic)) {
      return;
    }
    fieldChanged(_ISBASIC, this.isBasic);
    this.isBasic = isBasic;
  }

  public Creatable getOld() {
    return this.old;
  }

  public void setOld(DatabaseObject old) {
    this.old = ((Creatable) old);
  }

  public void recordOld(CloneContext ctx) {
    super.recordOld(ctx);
    if (this.getChild() != null) {
      this.getChild().recordOld(ctx);
    }
    this.getChildColl().forEach((one) -> one.recordOld(ctx));
  }

  public String displayName() {
    return "Creatable";
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof Creatable && super.equals(a);
  }

  public Creatable deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void collectChildValues(CloneContext ctx) {
    super.collectChildValues(ctx);
    ctx.collectChild(child);
    ctx.collectChilds(childColl);
    ctx.collectChild(emb);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    super.deepCloneIntoObj(dbObj, ctx);
    Creatable _obj = ((Creatable) dbObj);
    _obj.setName(name);
    _obj.setRef(ref);
    _obj.setRefColl(refColl);
    ctx.cloneChild(child, (v) -> _obj.setChild(v));
    ctx.cloneChildList(childColl, (v) -> _obj.setChildColl(v));
    _obj.setFile(file);
    ctx.cloneChild(emb, (v) -> _obj.setEmb(v));
    _obj.setIsBasic(isBasic);
  }

  public Creatable cloneInstance(Creatable cloneObj) {
    if (cloneObj == null) {
      cloneObj = new Creatable();
    }
    super.cloneInstance(cloneObj);
    cloneObj.setName(this.getName());
    cloneObj.setRef(this.getRef());
    cloneObj.setRefColl(new ArrayList<>(this.getRefColl()));
    cloneObj.setChild(this.getChild() == null ? null : this.getChild().cloneInstance(null));
    cloneObj.setChildColl(
        this.getChildColl().stream()
            .map((NonCreatable colObj) -> colObj.cloneInstance(null))
            .collect(Collectors.toList()));
    cloneObj.setFile(this.getFile());
    cloneObj.setEmb(this.getEmb().cloneInstance(null));
    cloneObj.setIsBasic(this.isIsBasic());
    return cloneObj;
  }

  public Creatable createNewInstance() {
    return new Creatable();
  }

  public boolean needOldObject() {
    return true;
  }

  public void collectCreatableReferences(List<Object> _refs) {
    super.collectCreatableReferences(_refs);
    _refs.add(this.ref);
    _refs.addAll(this.refColl);
    _refs.add(this.file);
    Database.collectCreatableReferences(_refs, this.child);
    Database.collectCollctionCreatableReferences(_refs, this.childColl);
  }

  @Override
  public boolean _isEntity() {
    return true;
  }

  @Override
  protected void _handleChildChange(int _childIdx) {
    switch (_childIdx) {
      case _CHILD:
        {
          this.fieldChanged(_childIdx, this.child);
          break;
        }
      case _CHILDCOLL:
        {
          this.collFieldChanged(_childIdx, this.childColl);
          break;
        }
      case _EMB:
        {
          this.fieldChanged(_childIdx, this.emb);
          break;
        }
    }
  }
}
