package us.mccode.scripts.MCCS.Values;

import us.mccode.scripts.MCCS.Expressions.Expression;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public interface Value extends Expression {
    String toString();

    double toNumber();
}
