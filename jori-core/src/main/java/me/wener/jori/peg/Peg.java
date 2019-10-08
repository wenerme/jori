package me.wener.jori.peg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.wener.jori.util.PropertyObject;

/**
 * PEG AST
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/10/1
 */
public interface Peg {
  interface Node {
    default List<? extends Node> getChildren() {
      return Collections.emptyList();
    }
  }

  interface Expression extends Node {}

  @Data
  class ActionBlock extends AbstractExpression {
    private String language;
    private Map<String, String> options;
    private String code;
  }

  abstract class AbstractNode extends PropertyObject implements Node {
    AbstractNode copy() {
      try {
        return (AbstractNode) clone();
      } catch (CloneNotSupportedException e) {
        throw new IllegalStateException();
      }
    }
  }

  @Getter
  @Setter
  abstract class AbstractExpression extends AbstractNode implements Expression {
    private String label;
    private List<ActionBlock> actions;

    public Expression addAction(ActionBlock action) {
      if (actions == null) {
        actions = new ArrayList<>();
      }
      actions.add(action);
      return this;
    }
  }

  @Getter
  @Setter
  abstract class AbstractChildExpression extends AbstractExpression {
    private Expression expression;

    @Override
    public List<? extends Node> getChildren() {
      return Collections.singletonList(expression);
    }
  }

  @Data
  class Sequence extends AbstractExpression {
    private List<Expression> expressions;

    public Sequence add(Expression e) {
      if (expressions == null) {
        expressions = new ArrayList<>();
      }
      expressions.add(e);
      return this;
    }

    @Override
    public List<? extends Node> getChildren() {
      return expressions;
    }
  }

  @Data
  class FirstOf extends AbstractExpression {
    private List<Expression> expressions;

    public FirstOf add(Expression e) {
      if (expressions == null) {
        expressions = new ArrayList<>();
      }
      expressions.add(e);
      return this;
    }

    @Override
    public List<? extends Node> getChildren() {
      return expressions;
    }
  }

  @Data
  class ZeroOrMore extends AbstractExpression {
    private Expression expression;

    public List<Node> getChildren() {
      return Collections.singletonList(expression);
    }
  }

  @Data
  class OneOrMore extends AbstractExpression {
    private Expression expression;

    public List<Node> getChildren() {
      return Collections.singletonList(expression);
    }
  }

  @Data
  class Optional extends AbstractExpression {
    private Expression expression;
  }

  @Data
  class AndPredicate extends AbstractExpression {
    private Expression expression;
  }

  @Data
  class NotPredicate extends AbstractExpression {
    private Expression expression;
  }

  @Data
  class StringMatch extends AbstractExpression {
    private char[] match;
  }

  @Data
  class CharMatch extends AbstractExpression {
    private char match;
  }

  @Data
  class CharRangeMatch extends AbstractExpression {
    private char startInclusive;
    private char endInclusive;
  }

  @Data
  class AnyOfCharMatch extends AbstractExpression {
    private char[] chars;
  }

  @Data
  class NoneOfCharMatch extends AbstractExpression {
    private char[] chars;
  }

  @Data
  class NoneMatch extends AbstractExpression {}

  @Data
  class AnyMatch extends AbstractExpression {}

  @Data
  class EmptyMatch extends AbstractExpression {}

  @Data
  class RuleMatch extends AbstractExpression {
    private String name;
  }

  @Data
  class Parentheses extends AbstractExpression {
    private Expression expression;
  }

  @Data
  class Rule extends AbstractNode {
    private String name;
    private String displayName;
    private Expression expression;
  }

  @Data
  class Unit extends AbstractNode {
    private List<ActionBlock> actions;
    private List<Rule> rules = new ArrayList<>();
  }
}
