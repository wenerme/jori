package me.wener.jori.logic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/9/28
 */
class LogicsTest {

  @Test
  void fromBinaryIntArrayToLong() {
    assertEquals(8L, Logics.fromBinaryIntArrayToLong(new int[]{1, 0, 0, 0}));
  }

  @Test
  void toBinaryIntArray() {
    assertArrayEquals(new int[]{1, 0, 0, 0}, Logics.toBinaryIntArray(4, 8));
    assertArrayEquals(new int[]{0, 1, 0, 0, 0}, Logics.toBinaryIntArray(5, 8));
  }
}
