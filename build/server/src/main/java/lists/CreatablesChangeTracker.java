package lists;

import classes.Creatables;
import classes.IdGenerator;
import d3e.core.CurrentUser;
import d3e.core.ListExt;
import d3e.core.SchemaConstants;
import d3e.core.TransactionManager;
import gqltosql2.Field;
import gqltosql2.OutObject;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Cancellable;
import java.util.List;
import models.AnonymousUser;
import models.Creatable;
import models.User;
import rest.ws.ClientSession;
import rest.ws.DataChangeTracker;
import store.StoreEventType;

public class CreatablesChangeTracker implements Cancellable {
  private Creatables root;
  private DataChangeTracker tracker;
  private ClientSession clientSession;
  private List<Disposable> disposables = ListExt.List();
  private Field field;

  public CreatablesChangeTracker(
      DataChangeTracker tracker, ClientSession clientSession, Field field) {
    this.tracker = tracker;
    this.clientSession = clientSession;
    this.field = field;
  }

  public void init(OutObject out, Creatables initialData) {
    {
      User currentUser = CurrentUser.get();
      if (!(currentUser instanceof AnonymousUser)) {
        throw new RuntimeException(
            "Current user type does not have read permissions for this ObjectList.");
      }
    }
    initialData._clearChanges();
    storeInitialData(initialData);
    addSubscriptions();
    out.setId(root.getId());
    disposables.add(tracker.listen(out, field, clientSession));
  }

  @Override
  public void cancel() {
    disposables.forEach((d) -> d.dispose());
  }

  private void storeInitialData(Creatables initialData) {
    this.root = initialData;
    long id = IdGenerator.getNext();
    this.root.setId(id);
    initialData._convertToObjectRef();
  }

  private void addSubscriptions() {
    /*
    This method will register listeners on each reference that is referred to in the DataQuery expression.
    A listener is added by default on the Table from which we pull the data, since any change in that must trigger a subscription change.
    */
    Disposable baseSubscribe =
        tracker.listen(
            SchemaConstants.Creatable,
            null,
            (obj, type) -> applyCreatable(((Creatable) obj), type));
    disposables.add(baseSubscribe);
  }

  private TypeAndId find(long id) {
    /*
    TODO: Maybe remove
    */
    return this.root.getItemsRef().stream().filter((x) -> x.id == id).findFirst().orElse(null);
  }

  private boolean has(long id) {
    return this.root.getItemsRef().stream().anyMatch((x) -> x.id == id);
  }

  private void fire() {
    TransactionManager.get().update(root);
  }

  public void applyCreatable(Creatable model, StoreEventType type) {
    if (type == StoreEventType.Insert) {
      /*
      New data is inserted
      So we just insert the new data depending on the clauses.
      */
      createInsertChange(model);
    } else if (type == StoreEventType.Delete) {
      /*
      Existing data is deleted
      */
      createDeleteChange(model);
    } else {
      /*
      Existing data is updated
      */
      Creatable old = model.getOld();
      if (old == null) {
        return;
      }
    }
  }

  private void createInsertChange(Creatable model) {
    root.addToItemsRef(new TypeAndId(model._typeIdx(), model.getId()), -1);
    fire();
  }

  private void createDeleteChange(Creatable model) {
    long id = model.getId();
    TypeAndId existing = find(id);
    if (existing == null) {
      return;
    }
    root.removeFromItemsRef(existing);
    fire();
  }
}
