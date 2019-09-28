package me.wener.jori.util;

import java.util.function.Supplier;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/9/28
 */
public interface Lambdas {

  static <T> T ifNull(T v, Supplier<T> supplier) {
    if (v != null) {
      return v;
    }
    return supplier.get();
  }
}
