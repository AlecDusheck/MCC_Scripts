package us.mccode.scripts.MCCS.Values;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class NumberValue implements Value {
    public NumberValue(double value) {
        this.value = value;
    }

    @Override public String toString() { return Double.toString(value); }
    public double toNumber() { return value; }
    public Value evaluate() { return this; }

    private final double value;
}
