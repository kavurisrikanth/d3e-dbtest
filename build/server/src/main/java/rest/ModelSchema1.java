package rest;

import d3e.core.SchemaConstants;
import gqltosql.schema.DModel;
import gqltosql.schema.FieldPrimitiveType;
import java.util.HashMap;
import java.util.Map;
import models.AnonymousUser;
import models.Avatar;
import models.CardPayMethod;
import models.Customer;
import models.D3EImage;
import models.D3EMessage;
import models.EmailMessage;
import models.InventoryItem;
import models.OneTimePassword;
import models.Order;
import models.OrderItem;
import models.PaymentMethod;
import models.PushNotification;
import models.ReportConfig;
import models.ReportConfigOption;
import models.SMSMessage;
import models.UPIPayMethod;
import models.User;
import models.UserSession;

public class ModelSchema1 {
  private Map<String, DModel<?>> allTypes = new HashMap<>();

  public ModelSchema1(Map<String, DModel<?>> allTypes) {
    this.allTypes = allTypes;
  }

  public void createAllTables() {
    addAnonymousUserFields();
    addAvatarFields();
    addCardPayMethodFields();
    addCustomerFields();
    addD3EImageFields();
    addD3EMessageFields();
    addEmailMessageFields();
    addInventoryItemFields();
    addOneTimePasswordFields();
    addOrderFields();
    addOrderItemFields();
    addPaymentMethodFields();
    addPushNotificationFields();
    addReportConfigFields();
    addReportConfigOptionFields();
    addSMSMessageFields();
    addUPIPayMethodFields();
    addUserFields();
    addUserSessionFields();
  }

  public DModel<?> getType(String type) {
    return allTypes.get(type);
  }

  public <T> DModel<T> getType2(String type) {
    return ((DModel<T>) allTypes.get(type));
  }

  private void addAnonymousUserFields() {
    DModel<AnonymousUser> m = getType2("AnonymousUser");
    m.setParent(getType("User"));
  }

  private void addAvatarFields() {
    DModel<Avatar> m = getType2("Avatar");
    m.addReference(
        "image",
        Avatar._IMAGE,
        "_image_id",
        true,
        getType("D3EImage"),
        (s) -> s.getImage(),
        (s, v) -> s.setImage(v));
    m.addPrimitive(
        "createFrom",
        Avatar._CREATEFROM,
        "_create_from",
        FieldPrimitiveType.String,
        (s) -> s.getCreateFrom(),
        (s, v) -> s.setCreateFrom(v));
  }

  private void addCardPayMethodFields() {
    DModel<CardPayMethod> m = getType2("CardPayMethod");
    m.setParent(getType("PaymentMethod"));
    m.addPrimitive(
        "cardNumber",
        CardPayMethod._CARDNUMBER,
        "_card_number",
        FieldPrimitiveType.String,
        (s) -> s.getCardNumber(),
        (s, v) -> s.setCardNumber(v));
    m.addPrimitive(
        "validTill",
        CardPayMethod._VALIDTILL,
        "_valid_till",
        FieldPrimitiveType.DateTime,
        (s) -> s.getValidTill(),
        (s, v) -> s.setValidTill(v));
    m.addPrimitive(
        "cvv",
        CardPayMethod._CVV,
        "_cvv",
        FieldPrimitiveType.String,
        (s) -> s.getCvv(),
        (s, v) -> s.setCvv(v));
    m.addPrimitive(
        "nameOnCard",
        CardPayMethod._NAMEONCARD,
        "_name_on_card",
        FieldPrimitiveType.String,
        (s) -> s.getNameOnCard(),
        (s, v) -> s.setNameOnCard(v));
  }

  private void addCustomerFields() {
    DModel<Customer> m = getType2("Customer");
    m.addPrimitive(
        "name",
        Customer._NAME,
        "_name",
        FieldPrimitiveType.String,
        (s) -> s.getName(),
        (s, v) -> s.setName(v));
    m.addPrimitive(
        "dob",
        Customer._DOB,
        "_dob",
        FieldPrimitiveType.DateTime,
        (s) -> s.getDob(),
        (s, v) -> s.setDob(v));
    m.addPrimitive(
        "underage",
        Customer._UNDERAGE,
        "_underage",
        FieldPrimitiveType.Boolean,
        (s) -> s.isUnderage(),
        (s, v) -> s.setUnderage(v));
    m.addEnum(
        "gender",
        Customer._GENDER,
        "_gender",
        SchemaConstants.Gender,
        (s) -> s.getGender(),
        (s, v) -> s.setGender(v));
    m.addReference(
        "guardian",
        Customer._GUARDIAN,
        "_guardian_id",
        false,
        getType("Customer"),
        (s) -> s.getGuardian(),
        (s, v) -> s.setGuardian(v));
    m.addReference(
        "guardianAgreement",
        Customer._GUARDIANAGREEMENT,
        "_guardian_agreement_id",
        false,
        getType("DFile"),
        (s) -> s.getGuardianAgreement(),
        (s, v) -> s.setGuardianAgreement(v));
    m.addInverseCollection(
        "orders", Customer._ORDERS, "_customer_id", getType("Order"), (s) -> s.getOrders());
    m.addReferenceCollection(
        "paymentMethods",
        Customer._PAYMENTMETHODS,
        "_payment_methods_id",
        "_customer_payment_methods",
        true,
        getType("PaymentMethod"),
        (s) -> s.getPaymentMethods(),
        (s, v) -> s.setPaymentMethods(v));
  }

  private void addD3EImageFields() {
    DModel<D3EImage> m = getType2("D3EImage");
    m.addPrimitive(
        "size",
        D3EImage._SIZE,
        "_size",
        FieldPrimitiveType.Integer,
        (s) -> s.getSize(),
        (s, v) -> s.setSize(v));
    m.addPrimitive(
        "width",
        D3EImage._WIDTH,
        "_width",
        FieldPrimitiveType.Integer,
        (s) -> s.getWidth(),
        (s, v) -> s.setWidth(v));
    m.addPrimitive(
        "height",
        D3EImage._HEIGHT,
        "_height",
        FieldPrimitiveType.Integer,
        (s) -> s.getHeight(),
        (s, v) -> s.setHeight(v));
    m.addReference(
        "file",
        D3EImage._FILE,
        "_file_id",
        false,
        getType("DFile"),
        (s) -> s.getFile(),
        (s, v) -> s.setFile(v));
  }

  private void addD3EMessageFields() {
    DModel<D3EMessage> m = getType2("D3EMessage");
    m.addPrimitive(
        "from",
        D3EMessage._FROM,
        "_from",
        FieldPrimitiveType.String,
        (s) -> s.getFrom(),
        (s, v) -> s.setFrom(v));
    m.addPrimitiveCollection(
        "to",
        D3EMessage._TO,
        "_to",
        "_d3emessage_to",
        FieldPrimitiveType.String,
        (s) -> s.getTo(),
        (s, v) -> s.setTo(v));
    m.addPrimitive(
        "body",
        D3EMessage._BODY,
        "_body",
        FieldPrimitiveType.String,
        (s) -> s.getBody(),
        (s, v) -> s.setBody(v));
    m.addPrimitive(
        "createdOn",
        D3EMessage._CREATEDON,
        "_created_on",
        FieldPrimitiveType.DateTime,
        (s) -> s.getCreatedOn(),
        (s, v) -> s.setCreatedOn(v));
  }

  private void addEmailMessageFields() {
    DModel<EmailMessage> m = getType2("EmailMessage");
    m.setParent(getType("D3EMessage"));
    m.addPrimitiveCollection(
        "bcc",
        EmailMessage._BCC,
        "_bcc",
        "_email_message_bcc",
        FieldPrimitiveType.String,
        (s) -> s.getBcc(),
        (s, v) -> s.setBcc(v));
    m.addPrimitiveCollection(
        "cc",
        EmailMessage._CC,
        "_cc",
        "_email_message_cc",
        FieldPrimitiveType.String,
        (s) -> s.getCc(),
        (s, v) -> s.setCc(v));
    m.addPrimitive(
        "subject",
        EmailMessage._SUBJECT,
        "_subject",
        FieldPrimitiveType.String,
        (s) -> s.getSubject(),
        (s, v) -> s.setSubject(v));
    m.addPrimitive(
        "html",
        EmailMessage._HTML,
        "_html",
        FieldPrimitiveType.Boolean,
        (s) -> s.isHtml(),
        (s, v) -> s.setHtml(v));
    m.addReferenceCollection(
        "inlineAttachments",
        EmailMessage._INLINEATTACHMENTS,
        "_inline_attachments_id",
        "_email_message_inline_attachments",
        false,
        getType("DFile"),
        (s) -> s.getInlineAttachments(),
        (s, v) -> s.setInlineAttachments(v));
    m.addReferenceCollection(
        "attachments",
        EmailMessage._ATTACHMENTS,
        "_attachments_id",
        "_email_message_attachments",
        false,
        getType("DFile"),
        (s) -> s.getAttachments(),
        (s, v) -> s.setAttachments(v));
  }

  private void addInventoryItemFields() {
    DModel<InventoryItem> m = getType2("InventoryItem");
    m.addPrimitive(
        "name",
        InventoryItem._NAME,
        "_name",
        FieldPrimitiveType.String,
        (s) -> s.getName(),
        (s, v) -> s.setName(v));
    m.addPrimitive(
        "price",
        InventoryItem._PRICE,
        "_price",
        FieldPrimitiveType.Double,
        (s) -> s.getPrice(),
        (s, v) -> s.setPrice(v));
  }

  private void addOneTimePasswordFields() {
    DModel<OneTimePassword> m = getType2("OneTimePassword");
    m.addPrimitive(
        "input",
        OneTimePassword._INPUT,
        "_input",
        FieldPrimitiveType.String,
        (s) -> s.getInput(),
        (s, v) -> s.setInput(v));
    m.addPrimitive(
        "inputType",
        OneTimePassword._INPUTTYPE,
        "_input_type",
        FieldPrimitiveType.String,
        (s) -> s.getInputType(),
        (s, v) -> s.setInputType(v));
    m.addPrimitive(
        "userType",
        OneTimePassword._USERTYPE,
        "_user_type",
        FieldPrimitiveType.String,
        (s) -> s.getUserType(),
        (s, v) -> s.setUserType(v));
    m.addPrimitive(
        "success",
        OneTimePassword._SUCCESS,
        "_success",
        FieldPrimitiveType.Boolean,
        (s) -> s.isSuccess(),
        (s, v) -> s.setSuccess(v));
    m.addPrimitive(
        "errorMsg",
        OneTimePassword._ERRORMSG,
        "_error_msg",
        FieldPrimitiveType.String,
        (s) -> s.getErrorMsg(),
        (s, v) -> s.setErrorMsg(v));
    m.addPrimitive(
        "token",
        OneTimePassword._TOKEN,
        "_token",
        FieldPrimitiveType.String,
        (s) -> s.getToken(),
        (s, v) -> s.setToken(v));
    m.addPrimitive(
        "code",
        OneTimePassword._CODE,
        "_code",
        FieldPrimitiveType.String,
        (s) -> s.getCode(),
        (s, v) -> s.setCode(v));
    m.addReference(
        "user",
        OneTimePassword._USER,
        "_user_id",
        false,
        getType("User"),
        (s) -> s.getUser(),
        (s, v) -> s.setUser(v));
    m.addPrimitive(
        "expiry",
        OneTimePassword._EXPIRY,
        "_expiry",
        FieldPrimitiveType.DateTime,
        (s) -> s.getExpiry(),
        (s, v) -> s.setExpiry(v));
  }

  private void addOrderFields() {
    DModel<Order> m = getType2("Order");
    m.addReference(
        "customer",
        Order._CUSTOMER,
        "_customer_id",
        false,
        getType("Customer"),
        (s) -> s.getCustomer(),
        (s, v) -> s.setCustomer(v));
    m.addReferenceCollection(
        "items",
        Order._ITEMS,
        "_items_id",
        "_order_items",
        true,
        getType("OrderItem"),
        (s) -> s.getItems(),
        (s, v) -> s.setItems(v));
    m.addPrimitive(
        "totalAmount",
        Order._TOTALAMOUNT,
        "_total_amount",
        FieldPrimitiveType.Double,
        (s) -> s.getTotalAmount(),
        (s, v) -> s.setTotalAmount(v));
    m.addReference(
        "paymentMethod",
        Order._PAYMENTMETHOD,
        "_payment_method_id",
        false,
        getType("PaymentMethod"),
        (s) -> s.getPaymentMethod(),
        (s, v) -> s.setPaymentMethod(v));
    m.addEnum(
        "paymentStatus",
        Order._PAYMENTSTATUS,
        "_payment_status",
        SchemaConstants.PaymentStatus,
        (s) -> s.getPaymentStatus(),
        (s, v) -> s.setPaymentStatus(v));
    m.addPrimitive(
        "createdDate",
        Order._CREATEDDATE,
        "_created_date",
        FieldPrimitiveType.DateTime,
        (s) -> s.getCreatedDate(),
        (s, v) -> s.setCreatedDate(v));
  }

  private void addOrderItemFields() {
    DModel<OrderItem> m = getType2("OrderItem");
    m.addReference(
        "item",
        OrderItem._ITEM,
        "_item_id",
        false,
        getType("InventoryItem"),
        (s) -> s.getItem(),
        (s, v) -> s.setItem(v));
    m.addPrimitive(
        "quantity",
        OrderItem._QUANTITY,
        "_quantity",
        FieldPrimitiveType.Integer,
        (s) -> s.getQuantity(),
        (s, v) -> s.setQuantity(v));
    m.addPrimitive(
        "amount",
        OrderItem._AMOUNT,
        "_amount",
        FieldPrimitiveType.Double,
        (s) -> s.getAmount(),
        (s, v) -> s.setAmount(v));
  }

  private void addPaymentMethodFields() {
    DModel<PaymentMethod> m = getType2("PaymentMethod");
  }

  private void addPushNotificationFields() {
    DModel<PushNotification> m = getType2("PushNotification");
    m.addPrimitiveCollection(
        "deviceTokens",
        PushNotification._DEVICETOKENS,
        "_device_tokens",
        "_push_notification_device_tokens",
        FieldPrimitiveType.String,
        (s) -> s.getDeviceTokens(),
        (s, v) -> s.setDeviceTokens(v));
    m.addPrimitive(
        "title",
        PushNotification._TITLE,
        "_title",
        FieldPrimitiveType.String,
        (s) -> s.getTitle(),
        (s, v) -> s.setTitle(v));
    m.addPrimitive(
        "body",
        PushNotification._BODY,
        "_body",
        FieldPrimitiveType.String,
        (s) -> s.getBody(),
        (s, v) -> s.setBody(v));
    m.addPrimitive(
        "path",
        PushNotification._PATH,
        "_path",
        FieldPrimitiveType.String,
        (s) -> s.getPath(),
        (s, v) -> s.setPath(v));
  }

  private void addReportConfigFields() {
    DModel<ReportConfig> m = getType2("ReportConfig");
    m.addPrimitive(
        "identity",
        ReportConfig._IDENTITY,
        "_identity",
        FieldPrimitiveType.String,
        (s) -> s.getIdentity(),
        (s, v) -> s.setIdentity(v));
    m.addReferenceCollection(
        "values",
        ReportConfig._VALUES,
        "_values_id",
        "_report_config_values",
        true,
        getType("ReportConfigOption"),
        (s) -> s.getValues(),
        (s, v) -> s.setValues(v));
  }

  private void addReportConfigOptionFields() {
    DModel<ReportConfigOption> m = getType2("ReportConfigOption");
    m.addPrimitive(
        "identity",
        ReportConfigOption._IDENTITY,
        "_identity",
        FieldPrimitiveType.String,
        (s) -> s.getIdentity(),
        (s, v) -> s.setIdentity(v));
    m.addPrimitive(
        "value",
        ReportConfigOption._VALUE,
        "_value",
        FieldPrimitiveType.String,
        (s) -> s.getValue(),
        (s, v) -> s.setValue(v));
  }

  private void addSMSMessageFields() {
    DModel<SMSMessage> m = getType2("SMSMessage");
    m.setParent(getType("D3EMessage"));
    m.addPrimitive(
        "dltTemplateId",
        SMSMessage._DLTTEMPLATEID,
        "_dlt_template_id",
        FieldPrimitiveType.String,
        (s) -> s.getDltTemplateId(),
        (s, v) -> s.setDltTemplateId(v));
  }

  private void addUPIPayMethodFields() {
    DModel<UPIPayMethod> m = getType2("UPIPayMethod");
    m.setParent(getType("PaymentMethod"));
    m.addPrimitive(
        "upiId",
        UPIPayMethod._UPIID,
        "_upi_id",
        FieldPrimitiveType.String,
        (s) -> s.getUpiId(),
        (s, v) -> s.setUpiId(v));
  }

  private void addUserFields() {
    DModel<User> m = getType2("User");
    m.addPrimitive(
        "isActive",
        User._ISACTIVE,
        "_is_active",
        FieldPrimitiveType.Boolean,
        (s) -> s.isIsActive(),
        (s, v) -> s.setIsActive(v));
    m.addPrimitive(
        "deviceToken",
        User._DEVICETOKEN,
        "_device_token",
        FieldPrimitiveType.String,
        (s) -> s.getDeviceToken(),
        (s, v) -> s.setDeviceToken(v));
  }

  private void addUserSessionFields() {
    DModel<UserSession> m = getType2("UserSession");
    m.addPrimitive(
        "userSessionId",
        UserSession._USERSESSIONID,
        "_user_session_id",
        FieldPrimitiveType.String,
        (s) -> s.getUserSessionId(),
        (s, v) -> s.setUserSessionId(v));
  }
}
