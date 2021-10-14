package rest;

import classes.ChangeEventType;
import classes.ConnectionStatus;
import classes.Customers;
import classes.Gender;
import classes.IconType;
import classes.ImageFrom;
import classes.Inventory;
import classes.LoginResult;
import classes.MutateResultStatus;
import classes.PaymentStatus;
import classes.ReportOutAttribute;
import classes.ReportOutCell;
import classes.ReportOutColumn;
import classes.ReportOutOption;
import classes.ReportOutRow;
import classes.ReportOutput;
import classes.TrackSizeType;
import d3e.core.DFile;
import d3e.core.SchemaConstants;
import gqltosql.schema.DModel;
import gqltosql.schema.DModelType;
import gqltosql.schema.FieldPrimitiveType;
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

@org.springframework.stereotype.Service
public class ModelSchema extends AbstractModelSchema {
  protected void createAllEnums() {
    addEnum(ConnectionStatus.class, SchemaConstants.ConnectionStatus);
    addEnum(MutateResultStatus.class, SchemaConstants.MutateResultStatus);
    addEnum(ChangeEventType.class, SchemaConstants.ChangeEventType);
    addEnum(TrackSizeType.class, SchemaConstants.TrackSizeType);
    addEnum(IconType.class, SchemaConstants.IconType);
    addEnum(ImageFrom.class, SchemaConstants.ImageFrom);
    addEnum(Gender.class, SchemaConstants.Gender);
    addEnum(PaymentStatus.class, SchemaConstants.PaymentStatus);
  }

  protected void createAllTables() {
    addTable(
        new DModel<DFile>(
            "DFile", SchemaConstants.DFile, 3, 0, "_dfile", DModelType.MODEL, () -> new DFile()));
    addTable(
        new DModel<AnonymousUser>(
            "AnonymousUser",
            SchemaConstants.AnonymousUser,
            0,
            2,
            "_anonymous_user",
            DModelType.MODEL,
            () -> new AnonymousUser()));
    addTable(
        new DModel<Avatar>(
            "Avatar",
            SchemaConstants.Avatar,
            2,
            0,
            "_avatar",
            DModelType.MODEL,
            () -> new Avatar()));
    addTable(
        new DModel<CardPayMethod>(
            "CardPayMethod",
            SchemaConstants.CardPayMethod,
            4,
            0,
            "_card_pay_method",
            DModelType.MODEL,
            () -> new CardPayMethod()));
    addTable(
        new DModel<Customer>(
            "Customer",
            SchemaConstants.Customer,
            8,
            0,
            "_customer",
            DModelType.MODEL,
            () -> new Customer()));
    addTable(
        new DModel<D3EImage>(
                "D3EImage",
                SchemaConstants.D3EImage,
                4,
                0,
                "_d3eimage",
                DModelType.MODEL,
                () -> new D3EImage())
            .emb());
    addTable(
        new DModel<D3EMessage>(
                "D3EMessage", SchemaConstants.D3EMessage, 4, 0, "_d3emessage", DModelType.MODEL)
            .trans());
    addTable(
        new DModel<EmailMessage>(
                "EmailMessage",
                SchemaConstants.EmailMessage,
                6,
                4,
                "_email_message",
                DModelType.MODEL,
                () -> new EmailMessage())
            .trans());
    addTable(
        new DModel<InventoryItem>(
            "InventoryItem",
            SchemaConstants.InventoryItem,
            2,
            0,
            "_inventory_item",
            DModelType.MODEL,
            () -> new InventoryItem()));
    addTable(
        new DModel<OneTimePassword>(
            "OneTimePassword",
            SchemaConstants.OneTimePassword,
            9,
            0,
            "_one_time_password",
            DModelType.MODEL,
            () -> new OneTimePassword()));
    addTable(
        new DModel<Order>(
            "Order", SchemaConstants.Order, 6, 0, "_order", DModelType.MODEL, () -> new Order()));
    addTable(
        new DModel<OrderItem>(
            "OrderItem",
            SchemaConstants.OrderItem,
            3,
            0,
            "_order_item",
            DModelType.MODEL,
            () -> new OrderItem()));
    addTable(
        new DModel<PaymentMethod>(
            "PaymentMethod",
            SchemaConstants.PaymentMethod,
            0,
            0,
            "_payment_method",
            DModelType.MODEL));
    addTable(
        new DModel<PushNotification>(
                "PushNotification",
                SchemaConstants.PushNotification,
                4,
                0,
                "_push_notification",
                DModelType.MODEL,
                () -> new PushNotification())
            .trans());
    addTable(
        new DModel<ReportConfig>(
            "ReportConfig",
            SchemaConstants.ReportConfig,
            2,
            0,
            "_report_config",
            DModelType.MODEL,
            () -> new ReportConfig()));
    addTable(
        new DModel<ReportConfigOption>(
            "ReportConfigOption",
            SchemaConstants.ReportConfigOption,
            2,
            0,
            "_report_config_option",
            DModelType.MODEL,
            () -> new ReportConfigOption()));
    addTable(
        new DModel<SMSMessage>(
                "SMSMessage",
                SchemaConstants.SMSMessage,
                1,
                4,
                "_smsmessage",
                DModelType.MODEL,
                () -> new SMSMessage())
            .trans());
    addTable(
        new DModel<UPIPayMethod>(
            "UPIPayMethod",
            SchemaConstants.UPIPayMethod,
            1,
            0,
            "_upipay_method",
            DModelType.MODEL,
            () -> new UPIPayMethod()));
    addTable(new DModel<User>("User", SchemaConstants.User, 2, 0, "_user", DModelType.MODEL));
    addTable(
        new DModel<UserSession>(
            "UserSession", SchemaConstants.UserSession, 1, 0, "_user_session", DModelType.MODEL));
    addTable(
        new DModel<ReportOutput>(
            "ReportOutput",
            SchemaConstants.ReportOutput,
            5,
            0,
            null,
            DModelType.STRUCT,
            () -> new ReportOutput()));
    addTable(
        new DModel<ReportOutOption>(
            "ReportOutOption",
            SchemaConstants.ReportOutOption,
            2,
            0,
            null,
            DModelType.STRUCT,
            () -> new ReportOutOption()));
    addTable(
        new DModel<ReportOutColumn>(
            "ReportOutColumn",
            SchemaConstants.ReportOutColumn,
            3,
            0,
            null,
            DModelType.STRUCT,
            () -> new ReportOutColumn()));
    addTable(
        new DModel<ReportOutAttribute>(
            "ReportOutAttribute",
            SchemaConstants.ReportOutAttribute,
            2,
            0,
            null,
            DModelType.STRUCT,
            () -> new ReportOutAttribute()));
    addTable(
        new DModel<ReportOutRow>(
            "ReportOutRow",
            SchemaConstants.ReportOutRow,
            4,
            0,
            null,
            DModelType.STRUCT,
            () -> new ReportOutRow()));
    addTable(
        new DModel<ReportOutCell>(
            "ReportOutCell",
            SchemaConstants.ReportOutCell,
            4,
            0,
            null,
            DModelType.STRUCT,
            () -> new ReportOutCell()));
    addTable(
        new DModel<LoginResult>(
            "LoginResult",
            SchemaConstants.LoginResult,
            4,
            0,
            null,
            DModelType.STRUCT,
            () -> new LoginResult()));
    addTable(
        new DModel<Customers>(
            "Customers",
            SchemaConstants.Customers,
            1,
            0,
            null,
            DModelType.STRUCT,
            () -> new Customers()));
    addTable(
        new DModel<Inventory>(
            "Inventory",
            SchemaConstants.Inventory,
            1,
            0,
            null,
            DModelType.STRUCT,
            () -> new Inventory()));
    addDFileFields();
  }

  protected void addFields() {
    new ModelSchema1(allTypes).createAllTables();
    new StructSchema1(allTypes).createAllTables();
  }

  protected void recordAllChannels() {
    recordNumChannels(0);
  }
}
