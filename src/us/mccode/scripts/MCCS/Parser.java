package us.mccode.scripts.MCCS;

import us.mccode.scripts.MCCS.Expressions.Expression;
import us.mccode.scripts.MCCS.Expressions.OperatorExpression;
import us.mccode.scripts.MCCS.Expressions.VariableExpression;
import us.mccode.scripts.MCCS.Statements.*;
import us.mccode.scripts.MCCS.Values.NumberValue;
import us.mccode.scripts.MCCS.Values.StringValue;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import us.mccode.scripts.TitleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class Parser {
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        position = 0;
    }

    /**
     * The top-level function to start parsing. This will keep consuming
     * tokens and routing to the other parse functions for the different
     * grammar syntax until we run out of code to parse.
     *
     * @param  labels   A map of label names to statement indexes. The
     *                  parser will fill this in as it scans the code.
     * @return          The list of parsed statements.
     */
    public List<Statement> parse(Player player, Map<String, Integer> labels) {

        TitleManager.sendActionBar(player, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "" + ChatColor.GREEN + "Parsing script...");

        List<Statement> statements = new ArrayList<>();

        while (true) {
            // Ignore empty lines.
            while (match(TokenType.LINE));

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

    // The following functions each represent one grammatical part of the
    // language. If this parsed English, these functions would be named like
    // noun() and verb().

    /**
     * Parses a single expression. Recursive descent parsers start with the
     * lowest-precedent term and moves towards higher precedence. For Jasic,
     * binary operators (+, -, etc.) are the lowest.
     *
     * @return The parsed expression.
     */
    private Expression expression(Player player) {
        return operator(player);
    }

    /**
     * Parses a series of binary operator expressions into a single
     * expression. In Jasic, all operators have the same predecence and
     * associate left-to-right. That means it will interpret:
     *    1 + 2 * 3 - 4 / 5
     * like:
     *    ((((1 + 2) * 3) - 4) / 5)
     *
     * It works by building the expression tree one at a time. So, given
     * this code: 1 + 2 * 3, this will:
     *
     * 1. Parse (1) as an atomic expression.
     * 2. See the (+) and start a new operator expression.
     * 3. Parse (2) as an atomic expression.
     * 4. Build a (1 + 2) expression and replace (1) with it.
     * 5. See the (*) and start a new operator expression.
     * 6. Parse (3) as an atomic expression.
     * 7. Build a ((1 + 2) * 3) expression and replace (1 + 2) with it.
     * 8. Return the last expression built.
     *
     * @return The parsed expression.
     */
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

    /**
     * Parses an "atomic" expression. This is the highest level of
     * precedence and contains single literal tokens like 123 and "foo", as
     * well as parenthesized expressions.
     *
     * @return The parsed expression.
     */
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

    // The following functions are the core low-level operations that the
    // grammar parser is built in terms of. They match and consume tokens in
    // the token stream.

    /**
     * Consumes the next two tokens if they are the given type (in order).
     * Consumes no tokens if either check fais.
     *
     * @param  type1 Expected type of the next token.
     * @param  type2 Expected type of the subsequent token.
     * @return       True if tokens were consumed.
     */
    private boolean match(TokenType type1, TokenType type2) {
        if (get(0).type != type1) return false;
        if (get(1).type != type2) return false;
        position += 2;
        return true;
    }

    /**
     * Consumes the next token if it's the given type.
     *
     * @param  type  Expected type of the next token.
     * @return       True if the token was consumed.
     */
    private boolean match(TokenType type) {
        if (get(0).type != type) return false;
        position++;
        return true;
    }

    /**
     * Consumes the next token if it's a word token with the given name.
     *
     * @param  name  Expected name of the next word token.
     * @return       True if the token was consumed.
     */
    private boolean match(String name) {
        if (get(0).type != TokenType.WORD) return false;
        if (!get(0).text.equals(name)) return false;
        position++;
        return true;
    }

    /**
     * Consumes the next token if it's the given type. If not, throws an
     * exception. This is for cases where the parser demands a token of a
     * certain type in a certain position, for example a matching ) after
     * an opening (.
     *
     * @param  type  Expected type of the next token.
     * @return       The consumed token.
     */
    private Token consume(Player player, TokenType type) {
        if (get(0).type != type) throw new MCCError(player, "Expected " + type + ".", MCCS.getCurrentStatement(player));
        return tokens.get(position++);
    }

    /**
     * Consumes the next token if it's a word with the given name. If not,
     * throws an exception.
     *
     * @param  name  Expected name of the next word token.
     * @return       The consumed token.
     */
    private Token consume(Player player, String name) {
        if (!match(name)) throw new MCCError(player, "Expected " + name + ".", MCCS.getCurrentStatement(player));
        return last(1);
    }

    /**
     * Gets a previously consumed token, indexing backwards. last(1) will
     * be the token just consumed, last(2) the one before that, etc.
     *
     * @param  offset How far back in the token stream to look.
     * @return        The consumed token.
     */
    private Token last(int offset) {
        return tokens.get(position - offset);
    }

    /**
     * Gets an unconsumed token, indexing forward. get(0) will be the next
     * token to be consumed, get(1) the one after that, etc.
     *
     * @param  offset How far forward in the token stream to look.
     * @return        The yet-to-be-consumed token.
     */
    private Token get(int offset) {
        if (position + offset >= tokens.size()) {
            return new Token("", TokenType.EOF);
        }
        return tokens.get(position + offset);
    }

    private final List<Token> tokens;
    private int position;
}
