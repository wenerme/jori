package me.wener.jori.peg;

import java.lang.reflect.Modifier;
import me.wener.jori.peg.Peg.Node;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/10/7
 */
public interface PegParse {

  public static void main(String[] args) {
    for (Class<?> type : Peg.class.getDeclaredClasses()) {
      if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
        continue;
      }
      if (!Node.class.isAssignableFrom(type)) {
        continue;
      }
      System.out.printf(
          "if(node instanceof %1$s){return visit%1$s((%1$s)node);}\n", type.getSimpleName());
      //      System.out.printf(
      //          "default T visit%s(%s ctx){ return visitChildren(ctx); }\n",
      //          type.getSimpleName(), type.getSimpleName());
    }
  }

  interface Visitor<T> {
    static <T> T accept(Node node, Visitor<T> visitor) {
      return null;
    }

    default T visit(Node node) {
      return accept(node, this);
    }

    default T visitChildren(Node node) {
      T result = defaultResult();
      //      int n = node.getChildCount();
      //      for (int i = 0; i < n; i++) {
      //        if (!shouldVisitNextChild(node, result)) {
      //          break;
      //        }
      //
      //        ParseTree c = node.getChild(i);
      //        T childResult = c.accept(this);
      //        result = aggregateResult(result, childResult);
      //      }

      return result;
    }

    default T defaultResult() {
      return null;
    }

    default T aggregateResult(T aggregate, T nextResult) {
      return nextResult;
    }

    default boolean shouldVisitNextChild(Node node, T currentResult) {
      return true;
    }
  }
}
