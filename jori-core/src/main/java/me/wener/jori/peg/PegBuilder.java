package me.wener.jori.peg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import me.wener.jori.peg.Peg.AndPredicate;
import me.wener.jori.peg.Peg.AnyMatch;
import me.wener.jori.peg.Peg.AnyOfCharMatch;
import me.wener.jori.peg.Peg.CharMatch;
import me.wener.jori.peg.Peg.CharRangeMatch;
import me.wener.jori.peg.Peg.EmptyMatch;
import me.wener.jori.peg.Peg.Expression;
import me.wener.jori.peg.Peg.FirstOf;
import me.wener.jori.peg.Peg.Node;
import me.wener.jori.peg.Peg.NoneMatch;
import me.wener.jori.peg.Peg.NoneOfCharMatch;
import me.wener.jori.peg.Peg.NotPredicate;
import me.wener.jori.peg.Peg.OneOrMore;
import me.wener.jori.peg.Peg.Optional;
import me.wener.jori.peg.Peg.Parentheses;
import me.wener.jori.peg.Peg.Rule;
import me.wener.jori.peg.Peg.RuleMatch;
import me.wener.jori.peg.Peg.Sequence;
import me.wener.jori.peg.Peg.StringMatch;
import me.wener.jori.peg.Peg.Unit;
import me.wener.jori.peg.Peg.ZeroOrMore;
import me.wener.jori.peg.PegParser.Input;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/10/7
 */
public interface PegBuilder {
  static <T extends Node> T build(Supplier<T> builder) {
    return (T) builder.get();
  }

  static Expression RuleOf(String name) {
    return new RuleMatch().setName(name);
  }

  static Expression EOI() {
    return new CharMatch().setMatch(Input.EOI());
  }

  static Expression Any() {
    return new AnyMatch();
  }

  static Expression Empty() {
    return new EmptyMatch();
  }

  static Expression None() {
    return new NoneMatch();
  }

  static Expression Char(char match) {
    return new CharMatch().setMatch(match);
  }

  static Expression CharRange(char startInclusive, char endInclusive) {
    return new CharRangeMatch().setStartInclusive(startInclusive).setEndInclusive(endInclusive);
  }

  static Expression AnyOf(CharSequence sequence) {
    return AnyOf(sequence.toString().toCharArray());
  }

  static Expression NoneOf(CharSequence sequence) {
    return NoneOf(sequence.toString().toCharArray());
  }

  static Expression AnyOf(char[] chars) {
    return new AnyOfCharMatch().setChars(chars);
  }

  static Expression NoneOf(char[] chars) {
    return new NoneOfCharMatch().setChars(chars);
  }

  static Expression String(CharSequence sequence) {
    return new StringMatch().setMatch(sequence.toString().toCharArray());
  }

  static Expression Optional(Expression expression) {
    return new Optional().setExpression(expression);
  }

  static Expression ZeroOrMore(Expression expression) {
    return new ZeroOrMore().setExpression(expression);
  }

  static Expression OneOrMore(Expression expression) {
    return new OneOrMore().setExpression(expression);
  }

  static Expression Parentheses(Expression expression) {
    return new Parentheses().setExpression(expression);
  }

  static Expression FirstOfRules(String... matches) {
    return new FirstOf()
        .setExpressions(
            Arrays.stream(matches).map(PegBuilder::RuleOf).collect(Collectors.toList()));
  }

  static Expression FirstOfChars(CharSequence matches) {
    return FirstOfChars(matches.toString().toCharArray());
  }

  static Expression FirstOfChars(char... matches) {
    ArrayList<Expression> list = new ArrayList<>(matches.length);
    for (char match : matches) {
      list.add(Char(match));
    }
    return FirstOf(list.toArray(new Expression[0]));
  }

  static Expression FirstOf(Expression... expressions) {
    return new FirstOf().setExpressions(Arrays.asList(expressions));
  }

  static Expression Sequence(Expression... expressions) {
    return new Sequence().setExpressions(Arrays.asList(expressions));
  }

  static Expression AndPredicate(Expression expression) {
    return new AndPredicate().setExpression(expression);
  }

  static Expression NotPredicate(Expression expression) {
    return new NotPredicate().setExpression(expression);
  }

  static Expression Repeat(int atLeast, int atMost, Expression expression) {
    throw new UnsupportedOperationException();
  }

  static Expression Repeat(int n, Expression expression) {
    return Repeat(n, n, expression);
  }

  static Expression RepeatAtLeast(int n, Expression expression) {
    return Repeat(n, -1, expression);
  }

  static Expression RepeatAtMost(int n, Expression expression) {
    return Repeat(-1, n, expression);
  }

  static Rule Rule(String name, Expression expression) {
    return new Rule().setName(name).setExpression(expression);
  }

  static Unit Unit(Rule... rules) {
    return new Unit().setRules(Arrays.asList(rules));
  }
}
