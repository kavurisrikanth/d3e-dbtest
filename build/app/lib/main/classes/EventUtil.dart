import 'package:d3edbtest/main/classes/DBResult.dart';
import 'package:d3edbtest/main/classes/DBResultStatus.dart';
import 'package:d3edbtest/main/classes/DFile.dart';
import 'package:d3edbtest/main/classes/NameUtil.dart';
import 'package:d3edbtest/main/models/Creatable.dart';
import 'package:d3edbtest/main/models/Embedded.dart';
import 'package:d3edbtest/main/models/NonCreatable.dart';

class EventUtil {
  static Future<Creatable> createBasic() async {
    Creatable c = Creatable(name: NameUtil.getName());

    DBResult r = (await c.save());

    if (r.status == DBResultStatus.Success) {
      return c;
    } else {
      return null;
    }
  }

  static Future<Creatable> createRef() async {
    Creatable ref = Creatable(name: NameUtil.getName());

    DBResult r = (await ref.save());

    if (r.status == DBResultStatus.Success) {
      Creatable c = Creatable(name: NameUtil.getName(), ref: ref);

      r = (await c.save());

      if (r.status == DBResultStatus.Success) {
        return c;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  static Future<Creatable> createRefColl() async {
    List<Creatable> refs = [];

    for (int x = 0; x < 3; x++) {
      Creatable ref = Creatable(name: NameUtil.getName());

      DBResult r = (await ref.save());

      if (r.status == DBResultStatus.Success) {
        refs.add(ref);
      } else {
        return null;
      }
    }

    Creatable c = Creatable(name: NameUtil.getName(), refColl: refs);

    DBResult r = (await c.save());

    if (r.status == DBResultStatus.Success) {
      return c;
    } else {
      return null;
    }
  }

  static Future<Creatable> createChild() async {
    Creatable ref = Creatable(
        name: NameUtil.getName(),
        child: NonCreatable(name: NameUtil.getName()));

    DBResult r = (await ref.save());

    if (r.status == DBResultStatus.Success) {
      return ref;
    } else {
      return null;
    }
  }

  static Future<Creatable> createChildColl() async {
    List<NonCreatable> refs = [];

    for (int x = 0; x < 3; x++) {
      NonCreatable ref = NonCreatable(name: NameUtil.getName());

      refs.add(ref);
    }

    Creatable c = Creatable(name: NameUtil.getName(), childColl: refs);

    DBResult r = (await c.save());

    if (r.status == DBResultStatus.Success) {
      return c;
    } else {
      return null;
    }
  }

  static Future<Creatable> createEmb() async {
    Creatable ref = Creatable(
        name: NameUtil.getName(), emb: Embedded(embName: NameUtil.getName()));

    DBResult r = (await ref.save());

    if (r.status == DBResultStatus.Success) {
      return ref;
    } else {
      return null;
    }
  }

  static Future<Creatable> createFile() async {
    DFile file = DFile();

    file.name = NameUtil.getFileName();

    file.size = 123;

    Creatable ref = Creatable(name: NameUtil.getName(), file: file);

    DBResult r = (await ref.save());

    if (r.status == DBResultStatus.Success) {
      return ref;
    } else {
      return null;
    }
  }

  static Future<Creatable> updateBasic(List<Creatable> all) async {
    Creatable obj = all.firstWhere((c) => c.isBasic, orElse: () => null);

    if (obj == null) {
      obj = await createBasic();
    }

    if (obj == null) {
      return null;
    }

    obj.setName(NameUtil.getName());

    DBResult r = (await obj.save());

    if (r.status == DBResultStatus.Success) {
      return obj;
    } else {
      return null;
    }
  }

  static Future<Creatable> updateRef(List<Creatable> all) async {
    Creatable obj = all.firstWhere((c) => c.ref != null, orElse: () => null);

    if (obj == null) {
      obj = await createRef();
    }

    if (obj == null) {
      return null;
    }

    Creatable newRef = await createBasic();

    if (newRef == null) {
      return null;
    }

    obj.setRef(newRef);

    DBResult r = (await obj.save());

    if (r.status == DBResultStatus.Success) {
      return obj;
    } else {
      return null;
    }
  }

  static Future<Creatable> updateRefColl(List<Creatable> all) async {
    Creatable obj =
        all.firstWhere((c) => c.refColl.isNotEmpty, orElse: () => null);

    if (obj == null) {
      obj = await createRefColl();
    }

    if (obj == null) {
      return null;
    }

    obj.refColl.removeLast();

    DBResult r = (await obj.save());

    if (r.status == DBResultStatus.Success) {
      return obj;
    } else {
      return null;
    }
  }

  static Future<Creatable> updateChild(List<Creatable> all) async {
    Creatable obj = all.firstWhere((c) => c.child != null, orElse: () => null);

    if (obj == null) {
      obj = await createChild();
    }

    if (obj == null) {
      return null;
    }

    NonCreatable newChild = NonCreatable(name: NameUtil.getName());

    obj.setChild(newChild);

    DBResult r = (await obj.save());

    if (r.status == DBResultStatus.Success) {
      return obj;
    } else {
      return null;
    }
  }

  static Future<Creatable> updateChildColl(List<Creatable> all) async {
    Creatable obj =
        all.firstWhere((c) => c.childColl.isNotEmpty, orElse: () => null);

    if (obj == null) {
      obj = await createChildColl();
    }

    if (obj == null) {
      return null;
    }

    obj.childColl.removeLast();

    DBResult r = (await obj.save());

    if (r.status == DBResultStatus.Success) {
      return obj;
    } else {
      return null;
    }
  }

  static Future<Creatable> updateEmb(List<Creatable> all) async {
    Creatable obj = all.firstWhere((c) => c.emb != null, orElse: () => null);

    if (obj == null) {
      obj = await createEmb();
    }

    if (obj == null) {
      return null;
    }

    obj.setEmb(Embedded(embName: NameUtil.getName()));

    DBResult r = (await obj.save());

    if (r.status == DBResultStatus.Success) {
      return obj;
    } else {
      return null;
    }
  }

  static Future<Creatable> updateFile(List<Creatable> all) async {
    Creatable obj = all.firstWhere((c) => c.file != null, orElse: () => null);

    if (obj == null) {
      obj = await createFile();
    }

    if (obj == null) {
      return null;
    }

    DFile newFile = DFile();
    newFile.name = NameUtil.getFileName();
    newFile.size = 234;

    obj.setFile(newFile);

    DBResult r = (await obj.save());

    if (r.status == DBResultStatus.Success) {
      return obj;
    } else {
      return null;
    }
  }
}
