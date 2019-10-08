package me.wener.jori.peg;

import java.util.Iterator;
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

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/10/7
 */
public interface PegJsStringer {

  interface PegVisitor<T> extends PegParse.Visitor<T> {

    @Override
    default T visit(Node node) {
      if (node instanceof Unit) {
        return visitUnit((Unit) node);
      }
      if (node instanceof Rule) {
        return visitRule((Rule) node);
      }
      if (node instanceof Parentheses) {
        return visitParentheses((Parentheses) node);
      }
      if (node instanceof RuleMatch) {
        return visitRuleMatch((RuleMatch) node);
      }
      if (node instanceof EmptyMatch) {
        return visitEmptyMatch((EmptyMatch) node);
      }
      if (node instanceof AnyMatch) {
        return visitAnyMatch((AnyMatch) node);
      }
      if (node instanceof NoneMatch) {
        return visitNoneMatch((NoneMatch) node);
      }
      if (node instanceof NoneOfCharMatch) {
        return visitNoneOfCharMatch((NoneOfCharMatch) node);
      }
      if (node instanceof AnyOfCharMatch) {
        return visitAnyOfCharMatch((AnyOfCharMatch) node);
      }
      if (node instanceof CharRangeMatch) {
        return visitCharRangeMatch((CharRangeMatch) node);
      }
      if (node instanceof CharMatch) {
        return visitCharMatch((CharMatch) node);
      }
      if (node instanceof StringMatch) {
        return visitStringMatch((StringMatch) node);
      }
      if (node instanceof NotPredicate) {
        return visitNotPredicate((NotPredicate) node);
      }
      if (node instanceof AndPredicate) {
        return visitAndPredicate((AndPredicate) node);
      }
      if (node instanceof Optional) {
        return visitOptional((Optional) node);
      }
      if (node instanceof OneOrMore) {
        return visitOneOrMore((OneOrMore) node);
      }
      if (node instanceof ZeroOrMore) {
        return visitZeroOrMore((ZeroOrMore) node);
      }
      if (node instanceof FirstOf) {
        return visitFirstOf((FirstOf) node);
      }
      if (node instanceof Sequence) {
        return visitSequence((Sequence) node);
      }
      return defaultResult();
    }

    default T visitUnit(Unit ctx) {
      return visitChildren(ctx);
    }

    default T visitRule(Rule ctx) {
      return visitChildren(ctx);
    }

    default T visitParentheses(Parentheses ctx) {
      return visitChildren(ctx);
    }

    default T visitRuleMatch(RuleMatch ctx) {
      return visitChildren(ctx);
    }

    default T visitEmptyMatch(EmptyMatch ctx) {
      return visitChildren(ctx);
    }

    default T visitAnyMatch(AnyMatch ctx) {
      return visitChildren(ctx);
    }

    default T visitNoneMatch(NoneMatch ctx) {
      return visitChildren(ctx);
    }

    default T visitNoneOfCharMatch(NoneOfCharMatch ctx) {
      return visitChildren(ctx);
    }

    default T visitAnyOfCharMatch(AnyOfCharMatch ctx) {
      return visitChildren(ctx);
    }

    default T visitCharRangeMatch(CharRangeMatch ctx) {
      return visitChildren(ctx);
    }

    default T visitCharMatch(CharMatch ctx) {
      return visitChildren(ctx);
    }

    default T visitStringMatch(StringMatch ctx) {
      return visitChildren(ctx);
    }

    default T visitNotPredicate(NotPredicate ctx) {
      return visitChildren(ctx);
    }

    default T visitAndPredicate(AndPredicate ctx) {
      return visitChildren(ctx);
    }

    default T visitOptional(Optional ctx) {
      return visitChildren(ctx);
    }

    default T visitOneOrMore(OneOrMore ctx) {
      return visitChildren(ctx);
    }

    default T visitZeroOrMore(ZeroOrMore ctx) {
      return visitChildren(ctx);
    }

    default T visitFirstOf(FirstOf ctx) {
      return visitChildren(ctx);
    }

    default T visitSequence(Sequence ctx) {
      return visitChildren(ctx);
    }
  }

  class Stringer implements PegVisitor<String> {
    private static final Stringer INSTANCE = new Stringer();

    public static String toString(Node node) {
      return INSTANCE.visit(node);
    }

    @Override
    public String visitUnit(Unit ctx) {
      StringBuilder sb = new StringBuilder();
      for (Rule rule : ctx.getRules()) {
        sb.append(visit(rule)).append("\n");
      }
      return sb.toString();
    }

    @Override
    public String visitRule(Rule node) {
      return node.getName() + " = " + visit(node.getExpression()) + ";";
    }

    @Override
    public String visitCharRangeMatch(CharRangeMatch ctx) {
      return "[" + ctx.getStartInclusive() + '-' + ctx.getEndInclusive() + ']';
    }

    @Override
    public String visitCharMatch(CharMatch ctx) {
      return "'" + ctx.getMatch() + "'";
    }

    @Override
    public String visitAnyOfCharMatch(AnyOfCharMatch ctx) {
      return "[" + new String(ctx.getChars()) + "]";
    }

    @Override
    public String visitSequence(Sequence ctx) {
      StringBuilder sb = new StringBuilder();
      for (Iterator<Expression> iterator = ctx.getExpressions().iterator(); iterator.hasNext(); ) {
        Expression expression = iterator.next();
        sb.append(visit(expression));
        if (iterator.hasNext()) {
          sb.append(" ");
        }
      }
      return sb.toString();
    }

    @Override
    public String visitFirstOf(FirstOf ctx) {
      StringBuilder sb = new StringBuilder();
      for (Iterator<Expression> iterator = ctx.getExpressions().iterator(); iterator.hasNext(); ) {
        Expression expression = iterator.next();
        sb.append(visit(expression));
        if (iterator.hasNext()) {
          sb.append(" / ");
        }
      }
      return sb.toString();
    }

    @Override
    public String visitRuleMatch(RuleMatch ctx) {
      return ctx.getName();
    }

    @Override
    public String visitZeroOrMore(ZeroOrMore ctx) {
      return visit(ctx.getExpression()) + "*";
    }

    @Override
    public String visitOneOrMore(OneOrMore ctx) {
      return visit(ctx.getExpression()) + "+";
    }

    @Override
    public String visitParentheses(Parentheses ctx) {
      return '(' + visit(ctx.getExpression()) + ')';
    }
  }
}
