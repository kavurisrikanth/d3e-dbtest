package models;

import d3e.core.CloneContext;
import d3e.core.SchemaConstants;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import org.apache.solr.client.solrj.beans.Field;
import store.DatabaseObject;
import store.ICloneable;

@Entity
public class CardPayMethod extends PaymentMethod {
  public static final int _CARDNUMBER = 0;
  public static final int _VALIDTILL = 1;
  public static final int _CVV = 2;
  public static final int _NAMEONCARD = 3;
  @Field @NotNull private String cardNumber;
  @Field @NotNull private LocalDateTime validTill;
  @Field @NotNull private String cvv;
  @Field @NotNull private String nameOnCard;
  private transient CardPayMethod old;

  @Override
  public int _typeIdx() {
    return SchemaConstants.CardPayMethod;
  }

  @Override
  public String _type() {
    return "CardPayMethod";
  }

  @Override
  public int _fieldsCount() {
    return 4;
  }

  public void updateMasters(Consumer<DatabaseObject> visitor) {
    super.updateMasters(visitor);
  }

  public String getCardNumber() {
    return this.cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    if (Objects.equals(this.cardNumber, cardNumber)) {
      return;
    }
    fieldChanged(_CARDNUMBER, this.cardNumber);
    this.cardNumber = cardNumber;
  }

  public LocalDateTime getValidTill() {
    return this.validTill;
  }

  public void setValidTill(LocalDateTime validTill) {
    if (Objects.equals(this.validTill, validTill)) {
      return;
    }
    fieldChanged(_VALIDTILL, this.validTill);
    this.validTill = validTill;
  }

  public String getCvv() {
    return this.cvv;
  }

  public void setCvv(String cvv) {
    if (Objects.equals(this.cvv, cvv)) {
      return;
    }
    fieldChanged(_CVV, this.cvv);
    this.cvv = cvv;
  }

  public String getNameOnCard() {
    return this.nameOnCard;
  }

  public void setNameOnCard(String nameOnCard) {
    if (Objects.equals(this.nameOnCard, nameOnCard)) {
      return;
    }
    fieldChanged(_NAMEONCARD, this.nameOnCard);
    this.nameOnCard = nameOnCard;
  }

  public CardPayMethod getOld() {
    return this.old;
  }

  public void setOld(DatabaseObject old) {
    this.old = ((CardPayMethod) old);
  }

  @Override
  public boolean equals(Object a) {
    return a instanceof CardPayMethod && super.equals(a);
  }

  public CardPayMethod deepClone(boolean clearId) {
    CloneContext ctx = new CloneContext(clearId);
    return ctx.startClone(this);
  }

  public void deepCloneIntoObj(ICloneable dbObj, CloneContext ctx) {
    super.deepCloneIntoObj(dbObj, ctx);
    CardPayMethod _obj = ((CardPayMethod) dbObj);
    _obj.setCardNumber(cardNumber);
    _obj.setValidTill(validTill);
    _obj.setCvv(cvv);
    _obj.setNameOnCard(nameOnCard);
  }

  public CardPayMethod cloneInstance(CardPayMethod cloneObj) {
    if (cloneObj == null) {
      cloneObj = new CardPayMethod();
    }
    super.cloneInstance(cloneObj);
    cloneObj.setCardNumber(this.getCardNumber());
    cloneObj.setValidTill(this.getValidTill());
    cloneObj.setCvv(this.getCvv());
    cloneObj.setNameOnCard(this.getNameOnCard());
    return cloneObj;
  }

  public CardPayMethod createNewInstance() {
    return new CardPayMethod();
  }

  public boolean needOldObject() {
    return true;
  }

  @Override
  public boolean _isEntity() {
    return true;
  }

  @Override
  protected void _handleChildChange(int _childIdx) {
    super._handleChildChange(_childIdx);
  }
}
