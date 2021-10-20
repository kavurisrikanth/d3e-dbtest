import '../models/Creatable.dart';
import '../models/Embedded.dart';
import '../models/NonCreatable.dart';
import '../rocket/Template.dart';
import 'DBAction.dart';
import 'DBResult.dart';
import 'DBResultStatus.dart';
import 'DFile.dart';
import 'DisplayUtil.dart';
import 'EqualsUtil.dart';
import 'NameUtil.dart';
import 'Query.dart';
import 'dart:async';

class ActionUtil {
  static Creatable ref1;
  static Creatable ref2;
  static Creatable ref3;
  static Creatable ref4;
  static Creatable ref5;
  static Creatable ref6;
  static Creatable ref7;
  ActionUtil();
  static Future<List<DBAction>> createActions() async {
    return [
      (await ActionUtil.createBasic()),
      (await ActionUtil.createRef()),
      (await ActionUtil.createRefColl()),
      (await ActionUtil.createChild()),
      (await ActionUtil.createChildColl()),
      (await ActionUtil.createEmb()),
      (await ActionUtil.createFile())
    ];
  }

  static Future<List<DBAction>> updateActions() async {
    return [
      (await ActionUtil.updateBasic()),
      (await ActionUtil.updateRef()),
      (await ActionUtil.updateRefColl()),
      (await ActionUtil.updateChild()),
      (await ActionUtil.updateChildColl()),
      (await ActionUtil.updateEmb()),
      (await ActionUtil.updateFile())
    ];
  }

  static Future<List<DBAction>> deleteActions() async {
    return [
      (await ActionUtil.deleteBasic()),
      (await ActionUtil.deleteRefColl()),
      (await ActionUtil.deleteChild()),
      (await ActionUtil.deleteChildColl()),
      (await ActionUtil.deleteEmb()),
      (await ActionUtil.deleteFile()),
      (await ActionUtil.deleteAll())
    ];
  }

  static void checkCreatable(DBAction action, Creatable c) async {
    DBResult r = (await c.save());

    action.setActionRun(true);

    if (r.status == DBResultStatus.Success) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants.ACTIONUTIL_CHECKCREATABLE_QUERY_LOADCREATABLE, c.id));

      if (EqualsUtil.checkCreatable(c, fromDb)) {
        action.setSuccess(true);
      } else {
        action.setError(DisplayUtil.displayCreatable(c) +
            ' <=> ' +
            DisplayUtil.displayCreatable(fromDb));

        action.setSuccess(false);
      }
    } else {
      action.setSuccess(false);

      action.setError(r.errors.toString());
    }
  }

  static void checkDelete(DBAction action, Creatable c) async {
    DBResult r = (await c.delete());

    action.setActionRun(true);

    if (r.status == DBResultStatus.Success) {
      Creatable fromDb = (await Query.get().getCreatableById(
          UsageConstants.ACTIONUTIL_CHECKDELETE_QUERY_LOADCREATABLE, c.id));

      if (fromDb == null) {
        action.setSuccess(true);
      } else {
        action.setError(DisplayUtil.displayCreatable(fromDb));

        action.setSuccess(true);
      }
    } else {
      action.setSuccess(false);

      action.setError(r.errors.toString());
    }
  }

  static Future<DBAction> createBasic() async {
    return DBAction(
        title: 'Create Basic',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = Creatable(name: NameUtil.getName());

          ActionUtil.checkCreatable(action, c);

          ActionUtil.ref1 = c;
        });
  }

  static Future<DBAction> createRef() async {
    return DBAction(
        title: 'Create Ref',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c =
              Creatable(name: NameUtil.getName(), ref: ActionUtil.ref1);

          ActionUtil.checkCreatable(action, c);

          ActionUtil.ref2 = c;
        });
  }

  static Future<DBAction> createRefColl() async {
    return DBAction(
        title: 'Create Ref Coll',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = Creatable(
              name: NameUtil.getName(),
              refColl: [ActionUtil.ref1, ActionUtil.ref2]);

          ActionUtil.checkCreatable(action, c);

          ActionUtil.ref3 = c;
        });
  }

  static Future<DBAction> createChild() async {
    return DBAction(
        title: 'Create Child',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = Creatable(
              name: NameUtil.getName(),
              child: NonCreatable(name: NameUtil.getName()));

          ActionUtil.checkCreatable(action, c);

          ActionUtil.ref4 = c;
        });
  }

  static Future<DBAction> createChildColl() async {
    return DBAction(
        title: 'Create Child Coll',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = Creatable(name: NameUtil.getName(), childColl: [
            NonCreatable(name: NameUtil.getName()),
            NonCreatable(name: NameUtil.getName()),
            NonCreatable(name: NameUtil.getName())
          ]);

          ActionUtil.checkCreatable(action, c);

          ActionUtil.ref5 = c;
        });
  }

  static Future<DBAction> createEmb() async {
    return DBAction(
        title: 'Create Emb',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = Creatable(
              name: NameUtil.getName(),
              emb: Embedded(embName: NameUtil.getName()));

          ActionUtil.checkCreatable(action, c);

          ActionUtil.ref6 = c;
        });
  }

  static Future<DBAction> createFile() async {
    return DBAction(
        title: 'Create File',
        exe: (a) {
          DBAction action = (a as DBAction);

          DFile file = DFile();

          file.name = NameUtil.getFileName();

          file.size = 123;

          Creatable c = Creatable(name: NameUtil.getName(), file: file);

          ActionUtil.checkCreatable(action, c);

          ActionUtil.ref7 = c;
        });
  }

  static Future<DBAction> updateBasic() async {
    return DBAction(
        title: 'Update Basic',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = ActionUtil.ref1;

          c.setName(NameUtil.getName());

          ActionUtil.checkCreatable(action, c);
        });
  }

  static Future<DBAction> updateRef() async {
    return DBAction(
        title: 'Update Ref',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = ActionUtil.ref2;

          c.setRef(ActionUtil.ref4);

          ActionUtil.checkCreatable(action, c);
        });
  }

  static Future<DBAction> updateRefColl() async {
    return DBAction(
        title: 'Update Ref Coll',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = ActionUtil.ref3;

          c.setRefColl([ActionUtil.ref2, ActionUtil.ref4]);

          ActionUtil.checkCreatable(action, c);
        });
  }

  static Future<DBAction> updateChild() async {
    return DBAction(
        title: 'Update Child',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = ActionUtil.ref4;

          c.setChild(NonCreatable(name: NameUtil.getName()));

          ActionUtil.checkCreatable(action, c);
        });
  }

  static Future<DBAction> updateChildColl() async {
    return DBAction(
        title: 'Update Child Coll',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = ActionUtil.ref5;

          c.childColl.removeLast();

          c.addToChildColl(NonCreatable(name: NameUtil.getName()));

          ActionUtil.checkCreatable(action, c);
        });
  }

  static Future<DBAction> updateEmb() async {
    return DBAction(
        title: 'Update Emb',
        exe: (a) {
          DBAction action = (a as DBAction);

          Creatable c = ActionUtil.ref6;

          c.setEmb(Embedded(embName: NameUtil.getName()));

          ActionUtil.checkCreatable(action, c);
        });
  }

  static Future<DBAction> updateFile() async {
    return DBAction(
        title: 'Update File',
        exe: (a) {
          DBAction action = (a as DBAction);

          DFile newFile = DFile();

          newFile.name = NameUtil.getFileName();

          newFile.size = 234;

          Creatable c = ActionUtil.ref7;

          c.setFile(newFile);

          ActionUtil.checkCreatable(action, c);
        });
  }

  static Future<DBAction> deleteBasic() async {
    return DBAction(
        title: 'Delete Basic',
        exe: (a) {
          DBAction action = (a as DBAction);

          ActionUtil.checkDelete(action, ActionUtil.ref1);
        });
  }

  static Future<DBAction> deleteRefColl() async {
    return DBAction(
        title: 'Delete Ref Coll',
        exe: (a) {
          DBAction action = (a as DBAction);

          ActionUtil.checkDelete(action, ActionUtil.ref3);

          ActionUtil.checkDelete(action, ActionUtil.ref2);
        });
  }

  static Future<DBAction> deleteChild() async {
    return DBAction(
        title: 'Delete Child',
        exe: (a) {
          DBAction action = (a as DBAction);

          ActionUtil.checkDelete(action, ActionUtil.ref4);
        });
  }

  static Future<DBAction> deleteChildColl() async {
    return DBAction(
        title: 'Delete Child Coll',
        exe: (a) {
          DBAction action = (a as DBAction);

          ActionUtil.checkDelete(action, ActionUtil.ref5);
        });
  }

  static Future<DBAction> deleteEmb() async {
    return DBAction(
        title: 'Delete Emb',
        exe: (a) {
          DBAction action = (a as DBAction);

          ActionUtil.checkDelete(action, ActionUtil.ref6);
        });
  }

  static Future<DBAction> deleteFile() async {
    return DBAction(
        title: 'Delete File',
        exe: (a) {
          DBAction action = (a as DBAction);

          ActionUtil.checkDelete(action, ActionUtil.ref7);
        });
  }

  static Future<DBAction> deleteAll() async {
    return DBAction(
        title: 'Delete All',
        exe: (a) {
          DBAction action = (a as DBAction);

          action.setSuccess(true);
        });
  }
}
