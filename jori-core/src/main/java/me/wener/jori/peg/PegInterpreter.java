package me.wener.jori.peg;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import me.wener.jori.peg.Peg.AndPredicate;
import me.wener.jori.peg.Peg.CharMatch;
import me.wener.jori.peg.Peg.CharRangeMatch;
import me.wener.jori.peg.Peg.Expression;
import me.wener.jori.peg.Peg.FirstOf;
import me.wener.jori.peg.Peg.OneOrMore;
import me.wener.jori.peg.Peg.Optional;
import me.wener.jori.peg.Peg.Parentheses;
import me.wener.jori.peg.Peg.Rule;
import me.wener.jori.peg.Peg.RuleMatch;
import me.wener.jori.peg.Peg.Sequence;
import me.wener.jori.peg.Peg.StringMatch;
import me.wener.jori.peg.Peg.Unit;
import me.wener.jori.peg.Peg.ZeroOrMore;
import me.wener.jori.peg.PegJsStringer.Stringer;

/**
 * A peg interpreter to eval peg ast
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/10/1
 */
public class PegInterpreter implements PegParser {
  private static final Map<Class<? extends Expression>, BiPredicate<Expression, Context>> MATCHERS =
      Maps.newHashMap();

  static {
    register(Optional.class, PegInterpreter::match);
    register(CharMatch.class, PegInterpreter::match);
    register(CharRangeMatch.class, PegInterpreter::match);
    register(StringMatch.class, PegInterpreter::match);
    register(ZeroOrMore.class, PegInterpreter::match);
    register(Sequence.class, PegInterpreter::match);
    register(RuleMatch.class, PegInterpreter::match);
    register(OneOrMore.class, PegInterpreter::match);
    register(FirstOf.class, PegInterpreter::match);
    register(Parentheses.class, PegInterpreter::match);
  }

  @Setter private Peg.Unit unit;
  @Setter private Context context;

  public static <T extends Expression> void register(
      Class<T> type, BiPredicate<T, Context> matcher) {
    MATCHERS.put(type, (BiPredicate<Expression, Context>) matcher);
  }

  public static String toStringTree(MatchContext match) {
    StringBuilder sb = new StringBuilder();
    sb.append(match.toString());
    if (match.getChildCount() > 0) {
      for (MatchContext child : match.getChildren()) {
        sb.append(" ").append(toStringTree(child));
      }
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    //    PegInterpreter interpreter = new PegInterpreter();
    //    Unit unit = new Unit();
    //    interpreter.setUnit(unit);
    //    unit.getRules()
    //        .add(
    //            new Rule()
    //                .setName("main")
    //                .setExpression(
    //                    // new Peg.Sequence().add(new StringMatch().setMatch("aaa".toCharArray()))
    //                    new Peg.ZeroOrMore().setExpression(new CharMatch().setMatch('a')) //
    //                    ));
    //
    //    Input input = Input.of("aaa");
    //    Context context = new Context(input);
    //    interpreter.setContext(context);
    //    Match match = interpreter.eval("main");
    //    System.out.println(match);

    Input input = Input.of("1+1");
    Unit unit = PegGrammars.calculatorBuilder();
    System.out.println(new Stringer().visit(unit));

    Context context = new Context(input, unit);
    context.setMatcher(PegInterpreter::dispatch);
    if (context.match(context.getRule("Expression"))) {
      Match match = context.getCurrentMatch();
      System.out.println(match);
    } else {
      System.out.println("No match");
    }
  }

  public static boolean match(OneOrMore e, Context context) {
    if (!context.match(e.getExpression())) {
      context.addError("at least match one time %s", e.getExpression());
      return false;
    }
    int lastIndex = context.getCurrentIndex();
    while (context.match(e.getExpression())) {
      lastIndex = context.getCurrentIndex();
    }
    context.setCurrentIndex(lastIndex);
    return true;
  }

  public static boolean match(ZeroOrMore e, Context context) {
    int lastIndex = context.getCurrentIndex();
    while (context.match(e.getExpression())) {
      lastIndex = context.getCurrentIndex();
    }
    context.setCurrentIndex(lastIndex);
    return true;
  }

  public static boolean match(AndPredicate e, Context context) {

    return true;
  }

  public static boolean match(Sequence e, Context context) {
    for (Expression sub : e.getExpressions()) {
      if (!context.match(sub)) {
        context.addError("expected %s", sub);
        return false;
      }
    }
    return true;
  }

  public static boolean match(Optional e, Context context) {
    int lastIndex = context.getCurrentIndex();
    if (!context.match(e.getExpression())) {
      context.setCurrentIndex(lastIndex);
    }
    return true;
  }

  public static boolean match(StringMatch e, Context context) {
    if (!context.getInput().test(context.getCurrentIndex(), e.getMatch())) {
      return false;
    }
    context.advanceIndex(e.getMatch().length);
    return true;
  }

  public static boolean match(CharMatch e, Context context) {
    if (context.getCurrentChar() != e.getMatch()) {
      return false;
    }
    context.advanceIndex(1);
    return true;
  }

  public static boolean match(CharRangeMatch e, Context context) {
    char c = context.getCurrentChar();
    if (c < e.getStartInclusive() || c > e.getEndInclusive()) return false;
    context.advanceIndex(1);
    return true;
  }

  public static boolean match(Parentheses e, Context context) {
    return context.match(e.getExpression());
  }

  public static boolean match(FirstOf e, Context context) {
    int lastIndex = context.getCurrentIndex();
    for (Expression expression : e.getExpressions()) {
      context.setCurrentIndex(lastIndex);
      if (context.match(expression)) {
        return true;
      }
    }
    context.setCurrentIndex(lastIndex);
    return false;
  }

  public static boolean match(Peg.RuleMatch e, Context context) {
    return context.match(context.getRule(e.getName()));
  }

  public static boolean dispatch(Peg.Expression e, Context context) {
    BiPredicate<Expression, Context> matcher = MATCHERS.get(e.getClass());
    if (matcher == null) {
      throw new IllegalStateException("no matcher for type " + e.getClass().getSimpleName());
    }
    return matcher.test(e, context);
  }

  /*context 提供这样的能力，但是在 match 的时候调用
  1. 是否创建 match / tree
  2. 是否前进 / optional
  4. 是否嵌套关系 - sequence and ordered is not nested - same as ast
  3. action 什么时候执行
  me.wener.jori.peg.PegParser.MatchContext
     */

  @Getter
  @Setter
  public static class Match implements me.wener.jori.peg.PegParser.MatchContext {
    protected Context context;
    protected Match parent;
    protected List<MatchContext> children;
    protected int startIndex;
    protected int endIndex;
    protected Expression expression;

    public MatchContext getRoot() {
      MatchContext current = this;
      while (current.getParent() != null) {
        current = current.getParent();
      }
      return current;
    }

    @Override
    public String getText() {
      return context.getInput().getText(startIndex, endIndex);
    }

    @Override
    public String toString() {
      String label = "";
      if (expression instanceof RuleMatch) {
        label = "(" + ((RuleMatch) expression).getName() + ")";
      }

      return String.format(
          "Match(%s%s <- %s <- %s,%s '%s') <- %s",
          expression.getClass().getSimpleName(),
          label,
          PegJsStringer.Stringer.toString(expression),
          startIndex,
          endIndex,
          getText(),
          getPayload());
    }

    public Object getPayload() {
      if (getChildCount() == 0) {
        return getText();
      }
      return getChildren().stream().map(MatchContext::getPayload).collect(Collectors.toList());
    }
  }

  public static class Context {
    @Getter private final Input input;
    @Getter @Setter private int currentIndex;
    @Getter private char currentChar;
    @Setter private BiPredicate<Expression, Context> matcher;
    @Getter private Match currentMatch;
    private Peg.Unit unit;

    public Context(Input input, Unit unit) {
      this.input = input;
      this.unit = unit;
      advanceIndex(0);
    }

    public void advanceIndex(int delta) {
      currentIndex += delta;
      currentChar = input.charAt(currentIndex);
    }

    public boolean match(Expression e) {
      Match match;
      match = createMatch();

      match.setExpression(e);
      currentMatch = match;
      boolean test = matcher.test(e, this);
      if (test) {
        match.setEndIndex(currentIndex);
        if (match.getParent() != null) {
          match.getParent().addChild(match);
        }
      }
      System.out.println(String.format("Match %s %s", test, match));
      if (match.getParent() != null) {
        currentMatch = match.getParent();
      }
      return test;
    }

    public boolean match() {
      return matcher.test(currentMatch.getExpression(), this);

      //      if (matcher.test(match.getExpression(), this)) {
      //        if (parent != null) {}
      //      }

      /*
      if (matchHandler.match(this)) {
                if (parent != null) {
                    parent.currentIndex = currentIndex;
                    parent.currentChar = currentChar;
                }
                matcher = null; // "retire" this context
                return true;
            }
            matcher = null; // "retire" this context until is "activated" again by a getSubContext(...) on the parent
            return false;
       */
    }

    private Match createMatch() {
      return new Match()
          .setContext(this)
          .setStartIndex(currentIndex)
          .setEndIndex(currentIndex)
          .setParent(currentMatch);
    }

    public void addError(String format, Object... args) {
      System.out.println(String.format(format, args));
      //      throw new PegParser.GrammarException(format, args);
    }

    public Expression getRule(String name) {
      return unit.getRules().stream()
          .filter(v -> v.getName().equals(name))
          .findFirst()
          .map(Rule::getExpression)
          .orElse(null);
    }
  }
}
