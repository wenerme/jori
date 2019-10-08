package me.wener.jori.peg;

import static me.wener.jori.peg.PegBuilder.Char;
import static me.wener.jori.peg.PegBuilder.CharRange;
import static me.wener.jori.peg.PegBuilder.FirstOfChars;
import static me.wener.jori.peg.PegBuilder.FirstOfRules;
import static me.wener.jori.peg.PegBuilder.OneOrMore;
import static me.wener.jori.peg.PegBuilder.Parentheses;
import static me.wener.jori.peg.PegBuilder.Rule;
import static me.wener.jori.peg.PegBuilder.RuleOf;
import static me.wener.jori.peg.PegBuilder.Sequence;
import static me.wener.jori.peg.PegBuilder.Unit;
import static me.wener.jori.peg.PegBuilder.ZeroOrMore;

import java.util.Arrays;
import me.wener.jori.peg.Peg.CharMatch;
import me.wener.jori.peg.Peg.CharRangeMatch;
import me.wener.jori.peg.Peg.FirstOf;
import me.wener.jori.peg.Peg.Rule;
import me.wener.jori.peg.Peg.RuleMatch;
import me.wener.jori.peg.Peg.Sequence;
import me.wener.jori.peg.Peg.Unit;
import me.wener.jori.peg.Peg.ZeroOrMore;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/10/7
 */
public interface PegGrammars {
  /*
  Expression ← Term ((‘+’ / ‘-’) Term)*
  Term ← Factor ((‘*’ / ‘/’) Factor)*
  Factor ← Number / Parens
  Number ← Digit+
  Parens ← ‘(’ Expression ‘)’
  Digit ← [0-9]
     */
  static Unit calculatorBuilder() {
    return Unit(
        Rule(
            "Expression",
            Sequence(
                RuleOf("Term"),
                ZeroOrMore(Parentheses(Sequence(FirstOfChars("+-"), RuleOf("Term")))))),
        Rule(
            "Term",
            Sequence(
                RuleOf("Factor"),
                ZeroOrMore(Parentheses(Sequence(FirstOfChars("*/"), RuleOf("Factor")))))),
        Rule("Factor", FirstOfRules("Number", "Parens")),
        Rule("Number", OneOrMore(RuleOf("Digit"))),
        Rule("Parens", Sequence(Char('('), RuleOf("Expression"), Char(')'))),
        Rule("Digit", CharRange('0', '9')));
  }

  static Unit calculator() {
    return new Unit()
        .setRules(
            Arrays.asList(
                new Rule()
                    .setName("Expression")
                    .setExpression(
                        new Peg.Sequence()
                            .add(new Peg.RuleMatch().setName("Term"))
                            .add(
                                new ZeroOrMore()
                                    .setExpression(
                                        new Sequence()
                                            .add(
                                                new FirstOf()
                                                    .add(new CharMatch().setMatch('+'))
                                                    .add(new CharMatch().setMatch('-')))
                                            .add(new RuleMatch().setName("Term"))))),
                new Rule()
                    .setName("Term")
                    .setExpression(
                        new Peg.Sequence()
                            .add(new Peg.RuleMatch().setName("Factor"))
                            .add(
                                new ZeroOrMore()
                                    .setExpression(
                                        new Sequence()
                                            .add(
                                                new FirstOf()
                                                    .add(new CharMatch().setMatch('*'))
                                                    .add(new CharMatch().setMatch('/')))
                                            .add(new RuleMatch().setName("Factor"))))),
                new Rule()
                    .setName("Factor")
                    .setExpression(
                        new Sequence()
                            .add(new RuleMatch().setName("Number"))
                            .add(new RuleMatch().setName("Parens"))),
                new Rule()
                    .setName("Number")
                    .setExpression(
                        new ZeroOrMore().setExpression(new RuleMatch().setName("Digit"))),
                new Rule()
                    .setName("Parens")
                    .setExpression(
                        new Sequence()
                            .add(new CharMatch().setMatch('('))
                            .add(new RuleMatch().setName("Expression"))
                            .add(new CharMatch().setMatch(')'))),
                new Rule()
                    .setName("Digit")
                    .setExpression(
                        new CharRangeMatch().setStartInclusive('0').setEndInclusive('9'))));
  }
}
