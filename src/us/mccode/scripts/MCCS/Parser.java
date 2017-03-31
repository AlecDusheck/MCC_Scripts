package us.mccode.scripts.MCCS;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import us.mccode.scripts.MCCS.Expressions.Expression;
import us.mccode.scripts.MCCS.Expressions.OperatorExpression;
import us.mccode.scripts.MCCS.Expressions.VariableExpression;
import us.mccode.scripts.MCCS.Statements.*;
import us.mccode.scripts.MCCS.Values.NumberValue;
import us.mccode.scripts.MCCS.Values.StringValue;
import us.mccode.scripts.TitleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class Parser {
    private final List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        position = 0;
    }

    /**
     * The top-level function to start parsing. This will keep consuming
     * tokens and routing to the other parse functions for the different
     * grammar syntax until we run out of code to parse.
     *
     * @param labels A map of label names to statement indexes. The
     *               parser will fill this in as it scans the code.
     * @return The list of parsed statements.
     */
    public List<Statement> parse(Player player, Map<String, Integer> labels) {

        TitleManager.sendActionBar(player, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "" + ChatColor.GREEN + "Parsing script...");

        List<Statement> statements = new ArrayList<>();

        while (true) {
            // Ignore empty lines.
            while (match(TokenType.LINE)) ;

            if (match(TokenType.LABEL)) {
                // Mark the index of the statement after the label.
                labels.put(last(1).text, statements.size());
            } else if (match(TokenType.WORD, TokenType.EQUALS)) {
                String name = last(2).text;
                Expression value = expression(player);
                statements.add(new AssignStatement(name, value, player));
            } else if (match("print")) {
                statements.add(new PrintStatement(expression(player), player));
            } else if (match("heal")) {
                statements.add(new healStatement(expression(player), player));
            } else if (match("stop")) {
                statements.add(new stopStatement(player));
            } else if (match("input")) {
                statements.add(new InputStatement(
                        consume(player, TokenType.WORD).text, player));
            } else if (match("goto")) {
                statements.add(new GotoStatement(
                        consume(player, TokenType.WORD).text, player));
            } else if (match("if")) {
                Expression condition = expression(player);
                consume(player, "then");
                String label = consume(player, TokenType.WORD).text;
                statements.add(new IfThenStatement(condition, label, player));
            } else {
                break;
            }
        }

        return statements;
    }

    private Expression expression(Player player) {
        return operator(player);
    }

    private Expression operator(Player player) {
        Expression expression = atomic(player);

        // Keep building operator expressions as long as we have operators.
        while (match(TokenType.OPERATOR) ||
                match(TokenType.EQUALS)) {
            char operator = last(1).text.charAt(0);
            Expression right = atomic(player);
            expression = new OperatorExpression(expression, operator, right);
        }

        return expression;
    }

    private Expression atomic(Player player) {
        if (match(TokenType.WORD)) {
            // A word is a reference to a variable.
            return new VariableExpression(last(1).text, player);
        } else if (match(TokenType.NUMBER)) {
            return new NumberValue(Double.parseDouble(last(1).text));
        } else if (match(TokenType.STRING)) {
            return new StringValue(last(1).text);
        } else if (match(TokenType.LEFT_PAREN)) {
            // The contents of a parenthesized expression can be any
            // expression. This lets us "restart" the precedence cascade
            // so that you can have a lower precedence expression inside
            // the parentheses.
            Expression expression = expression(player);
            consume(player, TokenType.RIGHT_PAREN);
            return expression;
        }
        throw new MCCError(player, "Error occurred during parsing script.", MCCS.getCurrentStatement(player));
    }

    private boolean match(TokenType type1, TokenType type2) {
        if (get(0).type != type1) return false;
        if (get(1).type != type2) return false;
        position += 2;
        return true;
    }

    private boolean match(TokenType type) {
        if (get(0).type != type) return false;
        position++;
        return true;
    }

    private boolean match(String name) {
        if (get(0).type != TokenType.WORD) return false;
        if (!get(0).text.equals(name)) return false;
        position++;
        return true;
    }

    private Token consume(Player player, TokenType type) {
        if (get(0).type != type) throw new MCCError(player, "Expected " + type + ".", MCCS.getCurrentStatement(player));
        return tokens.get(position++);
    }

    private Token consume(Player player, String name) {
        if (!match(name)) throw new MCCError(player, "Expected " + name + ".", MCCS.getCurrentStatement(player));
        return last(1);
    }

    private Token last(int offset) {
        return tokens.get(position - offset);
    }

    private Token get(int offset) {
        if (position + offset >= tokens.size()) {
            return new Token("", TokenType.EOF);
        }
        return tokens.get(position + offset);
    }
}
