class CollectionUtils {
  CollectionUtils();
  static bool isEquals(Iterable<Object> left, Iterable<Object> right) {
    if (left == right) {
      return true;
    }

    if (left == null || right == null) {
      return false;
    }

    if (left.length != right.length) {
      return false;
    }

    Iterator<Object> leftIt = left.iterator;

    Iterator<Object> rightIt = right.iterator;

    while (leftIt.moveNext() && rightIt.moveNext()) {
      if (leftIt.current != rightIt.current) {
        return false;
      }
    }

    return true;
  }
}
