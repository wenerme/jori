package me.wener.jori.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/9/28
 */
class TruthTableBuilderTest {

  @Test
  public void test() {
    List<int[]> matches =
        new TruthTableBuilder()
            .setSize(3)
            .setEvaluation(
                v -> {
                  // a && b && !c || !b
                  return v[0] == 1 && v[1] == 1 && v[2] != 1 || v[1] != 1;
                })
            .build();

    for (int[] match : matches) {
      System.out.printf(
          "%s  - %s\n",
          Logics.toBinaryRepresentationString(match), Logics.fromBinaryIntArrayToLong(match));
    }

    assertEquals(
        Sets.newHashSet(0L, 1L, 4L, 5L, 6L),
        matches.stream().map(Logics::fromBinaryIntArrayToLong).collect(Collectors.toSet()));
  }
}
