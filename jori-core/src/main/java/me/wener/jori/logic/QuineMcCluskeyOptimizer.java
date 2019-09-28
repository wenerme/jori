package me.wener.jori.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.wener.jori.util.Lambdas;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/9/28
 * @see <a href=https://en.wikipedia.org/wiki/Quine%E2%80%93McCluskey_algorithm>Quine–McCluskey
 *     algorithm</a>
 */
public class QuineMcCluskeyOptimizer {
  private Set<Long> ignored = new HashSet<>();
  private Set<Long> matches = new HashSet<>();

  @Getter private List<Term> terms = new ArrayList<>();
  @Getter private List<Term> essentials = new ArrayList<>();
  @Getter private List<Term> primes = new ArrayList<>();

  @Getter private long compares;
  @Getter @Setter private long compareThreshold = 1 << 20;
  @Getter @Setter private boolean trackIteration = false;
  @Getter private List<List<Term>> iterations = new ArrayList<>();

  public static int[] combine(int[] a, int[] b) {
    int n = 0;
    int[] ints = new int[a.length];
    int i = 0;
    while (i < a.length && n < 2) {
      ints[i] = a[i];
      if (a[i] != b[i]) {
        n++;
        ints[i] = Logics.REDUCED;
      }
      i++;
    }

    return n == 1 ? ints : null;
  }

  public QuineMcCluskeyOptimizer build(List<int[]> matches) {
    return build(matches, null);
  }

  public QuineMcCluskeyOptimizer build(List<int[]> matches, List<int[]> ignored) {
    reset();

    for (int[] ints : matches) {
      long v = Logics.fromBinaryIntArrayToLong(ints);
      this.matches.add(v);
      terms.add(Term.of(ints).addMatch(v));
    }

    for (int[] ints : Lambdas.<List<int[]>>ifNull(ignored, Collections::emptyList)) {
      long v = Logics.fromBinaryIntArrayToLong(ints);
      this.ignored.add(v);
      terms.add(Term.of(ints).addMatch(v));
    }

    return apply();
  }

  private QuineMcCluskeyOptimizer apply() {
    // finding prime implicants
    List<Term> candicates = new ArrayList<>(terms);
    Map<Integer, List<Term>> groups = new HashMap<>();

    // last group
    List<Term> ga;
    // current group
    List<Term> gb;

    // dedup tracking
    Map<String, Term> dedup = new HashMap<>();
    do {
      // the groups order depends on the candidates
      candicates.sort(Comparator.comparing(Term::getOnes));
      // debug the processing
      if (trackIteration) {
        iterations.add(new ArrayList<>(candicates));
      }

      // clear the value list ?
      groups.clear();
      for (Term term : candicates) {
        groups.computeIfAbsent(term.getOnes(), k -> new ArrayList<>()).add(term);
        // track the prime
        primes.add(term);
      }
      candicates.clear();

      Iterator<List<Term>> itor = groups.values().iterator();
      gb = itor.next();

      while (itor.hasNext()) {
        ga = gb;
        gb = itor.next();

        // group is not continue
        if (gb.get(0).getOnes() - ga.get(0).getOnes() != 1) {
          continue;
        }

        for (Term a : ga) {
          for (Term b : gb) {
            if (compares++ > compareThreshold) {
              throw new IllegalStateException("too many compares " + compares);
            }

            int[] ints = combine(a.ints, b.ints);
            if (ints != null) {
              String s = Logics.toBinaryRepresentationString(ints);
              Term last = dedup.get(s);
              if (last != null) {
                last.getMatches().addAll(a.getMatches());
                last.getMatches().addAll(b.getMatches());
              } else {
                Term term = Term.of(ints).setA(a).setB(b);
                term.getMatches().addAll(a.getMatches());
                term.getMatches().addAll(b.getMatches());

                a.combined = true;
                b.combined = true;

                candicates.add(term);
                dedup.put(s, term);
              }
            }
          }
        }
      }
    } while (!candicates.isEmpty());
    // cleanup
    dedup.clear();

    // remove combined
    primes.removeIf(v -> v.combined || dedup.put(v.getView(), v) != null);

    List<Long> targets = new ArrayList<>();
    targets.addAll(matches);

    // find essentials in primes
    // skip alternatives

    HashMap<Long, Term> matchTerms = new HashMap<>();
    primes.forEach(v -> v.matches.forEach(m -> matchTerms.put(m, v)));

    while (!targets.isEmpty()) {
      Term term = matchTerms.get(targets.get(0));

      // do remove columns, count as essential
      if (targets.removeAll(term.getMatches())) {
        essentials.add(term);
      }
    }

    return this;
  }

  private void reset() {
    // reset state
    iterations.clear();
    terms.clear();
    essentials.clear();
    primes.clear();
  }

  @Data
  public static class Term {
    private int[] ints;
    private int ones;
    private boolean combined;
    private Term a;
    private Term b;
    private Set<Long> matches = new HashSet<>();
    private String view;

    public static Term of(int[] ints) {
      return new Term()
          .setInts(ints)
          .setOnes(Logics.countOnes(ints))
          .setView(Logics.toBinaryRepresentationString(ints));
    }

    @Override
    public String toString() {
      return "Term(" + view + ")";
    }

    public Term addMatch(long match) {
      matches.add(match);
      return this;
    }
  }
}
