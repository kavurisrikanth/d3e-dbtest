package rest;

import classes.ChangeEventType;
import classes.ConnectionStatus;
import classes.Creatables;
import classes.DBResultStatus;
import classes.IconType;
import classes.ImageFrom;
import classes.LoginResult;
import classes.MutateResultStatus;
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
import models.Creatable;
import models.CreatablesRequest;
import models.D3EImage;
import models.D3EMessage;
import models.EmailMessage;
import models.Embedded;
import models.NonCreatable;
import models.OneTimePassword;
import models.PushNotification;
import models.ReportConfig;
import models.ReportConfigOption;
import models.SMSMessage;
import models.User;
import models.UserSession;

@org.springframework.stereotype.Service
public class ModelSchema extends AbstractModelSchema {
  protected void createAllEnums() {
    addEnum(DBResultStatus.class, SchemaConstants.DBResultStatus);
    addEnum(ConnectionStatus.class, SchemaConstants.ConnectionStatus);
    addEnum(MutateResultStatus.class, SchemaConstants.MutateResultStatus);
    addEnum(ChangeEventType.class, SchemaConstants.ChangeEventType);
    addEnum(TrackSizeType.class, SchemaConstants.TrackSizeType);
    addEnum(IconType.class, SchemaConstants.IconType);
    addEnum(ImageFrom.class, SchemaConstants.ImageFrom);
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
        new DModel<Creatable>(
            "Creatable",
            SchemaConstants.Creatable,
            8,
            0,
            "_creatable",
            DModelType.MODEL,
            () -> new Creatable()));
    addTable(
        new DModel<CreatablesRequest>(
                "CreatablesRequest",
                SchemaConstants.CreatablesRequest,
                0,
                0,
                "_creatables_request",
                DModelType.MODEL,
                () -> new CreatablesRequest())
            .trans());
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
        new DModel<Embedded>(
                "Embedded",
                SchemaConstants.Embedded,
                1,
                0,
                "_embedded",
                DModelType.MODEL,
                () -> new Embedded())
            .emb());
    addTable(
        new DModel<NonCreatable>(
            "NonCreatable",
            SchemaConstants.NonCreatable,
            2,
            0,
            "_non_creatable",
            DModelType.MODEL,
            () -> new NonCreatable()));
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
        new DModel<Creatables>(
            "Creatables",
            SchemaConstants.Creatables,
            3,
            0,
            null,
            DModelType.STRUCT,
            () -> new Creatables()));
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
