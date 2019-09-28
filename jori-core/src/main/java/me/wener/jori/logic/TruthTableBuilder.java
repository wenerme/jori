package me.wener.jori.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/9/28
 */
public class TruthTableBuilder {

  private int size;
  private Predicate<int[]> evaluation;

  public static void permutation(int n, Consumer<int[]> action) {
    int[] result = new int[n];
    int[] indexes = new int[n];

    for (int i = 0; i <= n; i++) {
      permutation(result, indexes, 0, n - 1, 0, i, action);
    }
  }

  public static void permutation(
      int[] result, int[] indexes, int start, int end, int index, int n, Consumer<int[]> action) {
    if (index == n) {
      Arrays.fill(result, 0);
      for (int i = 0; i < n; i++) {
        result[indexes[i]] = 1;
      }
      action.accept(result);
    } else {
      for (int i = start; i <= end && end - i + 1 >= n - index; i++) {
        indexes[index] = i;
        permutation(result, indexes, i + 1, end, index + 1, n, action);
      }
    }
  }

  public List<int[]> build() {
    List<int[]> matches = new ArrayList<>();
    permutation(
        size,
        v -> {
          if (evaluation.test(v)) {
            matches.add(Arrays.copyOf(v, v.length));
          }
        });

    return matches;
  }

  public int getSize() {
    return this.size;
  }

  public TruthTableBuilder setSize(int size) {
    this.size = size;
    return this;
  }

  public Predicate<int[]> getEvaluation() {
    return this.evaluation;
  }

  public TruthTableBuilder setEvaluation(Predicate<int[]> evaluation) {
    this.evaluation = evaluation;
    return this;
  }
}
