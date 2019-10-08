package me.wener.jori.peg;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.wener.jori.peg.Peg.Expression;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/10/7
 */
public interface PegParser {

  interface Input {

    static char EOI() {
      return '\uffff';
    }

    static String getInlineFilename() {
      return "<inline>";
    }

    static Input of(String content) {
      return of(getInlineFilename(), content);
    }

    static Input of(String filename, String content) {
      return new CharArrayInput(filename, content.toCharArray());
    }

    char charAt(int index);

    boolean test(int index, char[] characters);

    String getText(int start, int end);

    Location getLocation(int index);

    String getLineText(int lineNumber);

    int getLineCount();

    String getFilename();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Location {

      private static final Location NOWHERE =
          new Location("<internally generated location>", -1, -1);
      private String filename;
      private int line;
      private int column;

      public static Location nowhere() {
        return NOWHERE;
      }
    }
  }

  interface Action {
    boolean run(MatchContext c);
  }

  interface MatchContext {
    MatchContext getParent();

    List<MatchContext> getChildren();

    MatchContext setChildren(List<MatchContext> children);

    Expression getExpression();

    int getStartIndex();

    int getEndIndex();

    String getText();

    default Object getPayload() {
      return null;
    }

    default MatchContext getChild(int i) {
      List<MatchContext> children = getChildren();
      if (children == null) {
        throw new IllegalArgumentException("no child " + i);
      }
      return children.get(i);
    }

    default MatchContext addChild(MatchContext child) {
      List<MatchContext> children = getChildren();
      if (children == null) {
        setChildren(children = new ArrayList<>());
      }
      children.add(child);
      return this;
    }

    default int getChildCount() {
      List<MatchContext> children = getChildren();
      if (children != null) {
        return children.size();
      }
      return 0;
    }
  }

  class CharArrayInput implements Input {

    @Getter private final char[] buffer;
    private final int length;
    @Getter private final String filename;
    private int[] newlines;

    CharArrayInput(String filename, char[] buffer) {
      this.filename = filename;
      this.buffer = buffer;
      this.length = buffer.length;
    }

    // returns the zero based input line number the character with the given index is found in
    private static int getLine0(int[] newlines, int index) {
      int j = Arrays.binarySearch(newlines, index);
      return j >= 0 ? j : -(j + 1);
    }

    public char charAt(int index) {
      return 0 <= index && index < length
          ? buffer[index]
          : index - length > 100000 ? throwParsingException() : Input.EOI();
    }

    private char throwParsingException() {
      throw new PegRuntimeException(
          "Parser read more than 100K chars beyond EOI, "
              + "verify that your grammar does not consume EOI indefinitely!");
    }

    public boolean test(int index, char[] characters) {
      int len = characters.length;
      if (index < 0 || index > length - len) {
        return false;
      }
      for (int i = 0; i < len; i++) {
        if (buffer[index + i] != characters[i]) return false;
      }
      return true;
    }

    public String getText(int start, int end) {
      if (start < 0) start = 0;
      if (end >= length) end = length;
      if (end <= start) return "";
      return new String(buffer, start, end - start);
    }

    public Location getLocation(int index) {
      buildNewlines();
      int line = getLine0(newlines, index);
      int column = index - (line > 0 ? newlines[line - 1] : -1);
      return new Location(filename, line + 1, column);
    }

    public String getLineText(int lineNumber) {
      buildNewlines();
      Preconditions.checkArgument(0 < lineNumber && lineNumber <= newlines.length + 1);
      int start = lineNumber > 1 ? newlines[lineNumber - 2] + 1 : 0;
      int end = lineNumber <= newlines.length ? newlines[lineNumber - 1] : length;
      if (charAt(end - 1) == '\r') end--;
      return getText(start, end);
    }

    public int getLineCount() {
      buildNewlines();
      return newlines.length + 1;
    }

    private void buildNewlines() {
      if (newlines == null) {
        LinkedList<Integer> newlines = new LinkedList<>();
        for (int i = 0; i < length; i++) {
          if (buffer[i] == '\n') {
            newlines.push(i);
          }
        }
        this.newlines = Ints.toArray(newlines);
      }
    }
  }
  //
  //  @Getter
  //  @Setter
  //  class Match {
  //    private int startIndex;
  //    private int endIndex;
  //    private Context context;
  //
  //    private Expression node;
  //    private Match parent;
  //    private List<Match> children;
  //
  //    public Match addChild(Match match) {
  //      if (children == null) {
  //        children = new ArrayList<>();
  //      }
  //      children.add(match);
  //      return this;
  //    }
  //
  //    public String getText() {
  //      return context.getInput().getText(startIndex, endIndex);
  //    }
  //  }

  class PegRuntimeException extends RuntimeException {
    public PegRuntimeException(String message) {
      super(message);
    }

    public PegRuntimeException(String format, Object... args) {
      super(String.format(format, args));
    }

    public PegRuntimeException(String message, Throwable cause) {
      super(message, cause);
    }

    public PegRuntimeException(Throwable cause) {
      super(cause);
    }
  }

  class GrammarException extends PegRuntimeException {

    public GrammarException(String format, Object... args) {
      super(format, args);
    }

    public GrammarException(String message) {
      super(message);
    }

    public GrammarException(String message, Throwable cause) {
      super(message, cause);
    }

    public GrammarException(Throwable cause) {
      super(cause);
    }
  }
}
