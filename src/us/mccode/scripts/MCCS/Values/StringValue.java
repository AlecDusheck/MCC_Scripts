package us.mccode.scripts.MCCS.Values;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class StringValue implements Value {
    public StringValue(String value) {
        this.value = value;
    }

    @Override public String toString() { return value; }
    public double toNumber() {
        try {
            double vav = Double.parseDouble(value);
            return vav;
        }catch (NumberFormatException e){
            return 0;
        }
    }
    public Value evaluate() { return this; }

    private final String value;
}
