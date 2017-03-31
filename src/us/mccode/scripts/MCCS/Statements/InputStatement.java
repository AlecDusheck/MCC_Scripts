package us.mccode.scripts.MCCS.Statements;

import org.bukkit.entity.Player;
import us.mccode.scripts.MCCS.MCCError;
import us.mccode.scripts.MCCS.MCCS;
import us.mccode.scripts.MCCS.Values.NumberValue;
import us.mccode.scripts.MCCS.Values.StringValue;

import java.io.IOException;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class InputStatement implements Statement {
    private final String name;
    private final Player player;

    public InputStatement(String name, Player player) {
        this.name = name;
        this.player = player;
    }

    public void execute() {
        try {
            //String input = MCCS.lineIn.readLine();
            String input = MCCS.getLineIn(player).readLine();

            // Store it as a number if possible, otherwise use a string.
            try {
                double value = Double.parseDouble(input);
                //MCCS.variables.put(name, new NumberValue(value));
                MCCS.putScriptVariables(player, name, new NumberValue(value));
            } catch (NumberFormatException e) {
                //MCCS.variables.put(name, new StringValue(input));
                MCCS.putScriptVariables(player, name, new StringValue(input));
            }
        } catch (IOException e1) {
            throw new MCCError(player, "An input error occurred.", MCCS.getCurrentStatement(player));
        }
    }
}