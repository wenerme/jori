package me.wener.jori.logic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;
import me.wener.jori.logic.QuineMcCluskeyOptimizer.Term;
import org.junit.jupiter.api.Test;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/9/28
 */
class QuineMcCluskeyTest {
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
    QuineMcCluskeyOptimizer optimizer = new QuineMcCluskeyOptimizer().build(matches);
    System.out.println(optimizer.getCompares());
    for (Term essential : optimizer.getEssentials()) {
      System.out.println(essential);
    }

    // A&&C || !B
    assertArrayEquals(new int[] {-1, 0, -1}, optimizer.getEssentials().get(0).getInts());
    assertArrayEquals(new int[] {1, -1, 0}, optimizer.getEssentials().get(1).getInts());
  }
}
