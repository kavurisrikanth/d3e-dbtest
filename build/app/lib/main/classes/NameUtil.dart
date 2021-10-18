class NameUtil {
  static int _idx = 0;
  NameUtil();
  static String getName() {
    String name = '_name_' + NameUtil._idx.toString();

    NameUtil._idx++;

    return name;
  }

  static void setIndex(int idx) {
    NameUtil._idx = idx;
  }
}
