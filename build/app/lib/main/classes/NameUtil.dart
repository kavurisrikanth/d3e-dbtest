class NameUtil {
  static int _idx = 0;
  NameUtil();
  static String getName() {
    String name = '_name_' + NameUtil._idx.toString();

    NameUtil._idx++;

    return name;
  }

  static String getFileName() {
    String name = '_file_name_' + NameUtil._idx.toString();

    NameUtil._idx++;

    return name;
  }
}
