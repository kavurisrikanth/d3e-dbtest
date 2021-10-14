import '../models/Creatable.dart';
import '../models/NonCreatable.dart';

class DisplayUtil {
  DisplayUtil();
  static String displayCreatable(Creatable c) {
    if (c == null) {
      return 'Unnamed';
    }

    StringBuffer sb = StringBuffer();

    sb.write(c.name);

    sb.write(' ');

    if (c.ref != null) {
      sb.write(DisplayUtil.displayCreatable(c.ref));

      sb.write(' ');
    }

    c.refColl.forEach((r) {
      sb.write(DisplayUtil.displayCreatable(r));

      sb.write(' ');
    });

    if (c.child != null) {
      sb.write(DisplayUtil.displayNonCreatable(c.child));

      sb.write(' ');
    }

    c.childColl.forEach((nc) {
      sb.write(DisplayUtil.displayNonCreatable(nc));

      sb.write(' ');
    });

    if (c.file != null) {
      sb.write(c.file.id);
    }

    if (c.emb != null) {
      sb.write(c.emb);
    }

    return sb.toString();
  }

  static String displayNonCreatable(NonCreatable nc) {
    if (nc == null) {
      return 'Unnamed NonCreatable';
    }

    StringBuffer sb = StringBuffer();

    if (nc.emb != null) {
      sb.write(nc.emb);
    }

    sb.write(nc.name);

    return sb.toString();
  }
}