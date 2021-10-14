package models;

import d3e.core.CloneContext;
import d3e.core.SchemaConstants;
import java.util.function.Consumer;
import store.DatabaseObject;

public class CreatablesRequest extends CreatableObject {
  @Override
  public int _typeIdx() {
    return SchemaConstants.CreatablesRequest;
  }

  @Override
  public String _type() {
    return "CreatablesRequest";
  }

  @Override
  public int _fieldsCount() {
    return 0;
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
  }

  public String displayName() {
    return "CreatablesRequest";
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof CreatablesRequest && super.equals(a);
  }

  public CreatablesRequest deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public CreatablesRequest cloneInstance(CreatablesRequest cloneObj) {
    if (cloneObj == null) {
      cloneObj = new CreatablesRequest();
    }
    super.cloneInstance(cloneObj);
    return cloneObj;
  }

  public boolean transientModel() {
    return true;
  }

  public CreatablesRequest createNewInstance() {
    return new CreatablesRequest();
  }
}
