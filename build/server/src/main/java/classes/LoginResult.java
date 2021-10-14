package classes;

import d3e.core.SchemaConstants;
import lists.TypeAndId;
import models.User;
import store.DBObject;

public class LoginResult extends DBObject {
  public static final int _FAILUREMESSAGE = 0;
  public static final int _SUCCESS = 1;
  public static final int _TOKEN = 2;
  public static final int _USEROBJECT = 3;
  private long id;
  private String failureMessage;
  private boolean success;
  private String token;
  private User userObject;
  private TypeAndId userObjectRef;

  public LoginResult() {}

  public LoginResult(String failureMessage, boolean success, String token, User userObject) {
    this.failureMessage = failureMessage;
    this.success = success;
    this.token = token;
    this.userObject = userObject;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFailureMessage() {
    return failureMessage;
  }

  public void setFailureMessage(String failureMessage) {
    fieldChanged(_FAILUREMESSAGE, this.failureMessage);
    this.failureMessage = failureMessage;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    fieldChanged(_SUCCESS, this.success);
    this.success = success;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    fieldChanged(_TOKEN, this.token);
    this.token = token;
  }

  public User getUserObject() {
    return userObject;
  }

  public TypeAndId getUserObjectRef() {
    return userObjectRef;
  }

  public void setUserObject(User userObject) {
    fieldChanged(_USEROBJECT, this.userObject);
    this.userObject = userObject;
  }

  public void setUserObjectRef(TypeAndId userObjectRef) {
    fieldChanged(_USEROBJECT, this.userObjectRef);
    this.userObjectRef = userObjectRef;
  }

  @Override
  public int _typeIdx() {
    return SchemaConstants.LoginResult;
  }

  @Override
  public String _type() {
    return "LoginResult";
  }

  @Override
  public int _fieldsCount() {
    return 4;
  }

  public void _convertToObjectRef() {
    this.userObjectRef = TypeAndId.from(this.userObject);
    this.userObject = null;
  }
}
