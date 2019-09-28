package me.wener.jori.logic;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/9/27
 */
public interface Logics {
  int REDUCED = -1;
  int IGNORED = -2;

  static int countOnes(int[] ints) {
    int n = 0;
    for (int i : ints) {
      if (i == 1) {
        n++;
      }
    }
    return n;
  }

  static String toBinaryRepresentationString(int[] ints) {
    char[] chars = new char[ints.length];
    for (int i = 0; i < ints.length; i++) {
      char c;
      switch (ints[i]) {
        case 0:
          c = '0';
          break;
        case 1:
          c = '1';
          break;
        case REDUCED:
          c = '-';
          break;
        default:
        case IGNORED:
          c = '?';
          break;
      }
      chars[i] = c;
    }
    return new String(chars);
  }

  static long fromBinaryIntArrayToLong(int[] ints) {
    long s = 0;
    long c = 1;
    for (int i = ints.length - 1; i >= 0; i--) {
      if (ints[i] == 1) {
        s += c;
      }
      c <<= 1;
    }
    return s;
  }

  static int[] toBinaryIntArray(int n, long v) {
    int[] ints = new int[n];
    long c = 1;
    for (int i = ints.length - 1; i >= 0; i--) {
      if ((v & c) > 0) {
        ints[i] = 1;
      }
      c <<= 1;
    }
    return ints;
  }
}
