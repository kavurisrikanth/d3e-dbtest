import '../models/Creatable.dart';
import '../models/Embedded.dart';
import '../models/NonCreatable.dart';
import 'CollectionUtils.dart';
import 'DFile.dart';

class EqualsUtil {
  EqualsUtil();
  static bool checkCreatable(Creatable one, Creatable two) {
    if (one == null && two == null) {
      return true;
    }

    if (one == null) {
      return false;
    }

    if (two == null) {
      return false;
    }

    if (one.name != two.name) {
      return false;
    }

    if (one.isBasic != two.isBasic) {
      return false;
    }

    if (one.ref != two.ref) {
      return false;
    }

    if (!CollectionUtils.isEquals(one.refColl, two.refColl)) {
      return false;
    }

    if (!EqualsUtil.checkNonCreatable(one.child, two.child)) {
      return false;
    }

    if (!CollectionUtils.isEquals(one.childColl, two.childColl)) {
      return false;
    }

    if (!EqualsUtil.checkFile(one.file, two.file)) {
      return false;
    }

    if (!EqualsUtil.checkEmbedded(one.emb, two.emb)) {
      return false;
    }

    return true;
  }

  static bool checkFile(DFile one, DFile two) {
    if (one == null && two == null) {
      return true;
    }

    if (one == null) {
      return false;
    }

    if (two == null) {
      return false;
    }

    return one.id == two.id;
  }

  static bool checkNonCreatable(NonCreatable one, NonCreatable two) {
    if (one == null && two == null) {
      return true;
    }

    if (one == null) {
      return false;
    }

    if (two == null) {
      return false;
    }

    if (one.name != two.name) {
      return false;
    }

    if (!EqualsUtil.checkEmbedded(one.emb, two.emb)) {
      return false;
    }

    return true;
  }

  static bool checkEmbedded(Embedded one, Embedded two) {
    if (one == null && two == null) {
      return true;
    }

    if (one == null) {
      return false;
    }

    if (two == null) {
      return false;
    }

    return one.embName == two.embName;
  }
}
