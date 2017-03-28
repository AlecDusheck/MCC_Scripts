package us.mccode.scripts.MCCS.Expressions;

import us.mccode.scripts.MCCS.Values.NumberValue;
import us.mccode.scripts.MCCS.Values.StringValue;
import us.mccode.scripts.MCCS.Values.Value;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class OperatorExpression implements Expression {
    public OperatorExpression(Expression left, char operator,
                              Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public Value evaluate() {
        Value leftVal = left.evaluate();
        Value rightVal = right.evaluate();

        switch (operator) {
            case '=':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber() ==
                            rightVal.toNumber()) ? 1 : 0);
                } else {
                    return new NumberValue(leftVal.toString().equals(
                            rightVal.toString()) ? 1 : 0);
                }
            case '+':
                // Addition if the left argument is a number, otherwise do
                // string concatenation.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue(leftVal.toNumber() +
                            rightVal.toNumber());
                } else {
                    return new StringValue(leftVal.toString() +
                            rightVal.toString());
                }
            case '-':
                return new NumberValue(leftVal.toNumber() -
                        rightVal.toNumber());
            case '*':
                return new NumberValue(leftVal.toNumber() *
                        rightVal.toNumber());
            case '/':
                return new NumberValue(leftVal.toNumber() /
                        rightVal.toNumber());
            case '<':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber() <
                            rightVal.toNumber()) ? 1 : 0);
                } else {
                    return new NumberValue((leftVal.toString().compareTo(
                            rightVal.toString()) < 0) ? 1 : 0);
                }
            case '>':
                // Coerce to the left argument's type, then compare.
                if (leftVal instanceof NumberValue) {
                    return new NumberValue((leftVal.toNumber() >
                            rightVal.toNumber()) ? 1 : 0);
                } else {
                    return new NumberValue((leftVal.toString().compareTo(
                            rightVal.toString()) > 0) ? 1 : 0);
                }
        }
        throw new Error("Unknown operator.");
    }

    private final Expression left;
    private final char operator;
    private final Expression right;
}
