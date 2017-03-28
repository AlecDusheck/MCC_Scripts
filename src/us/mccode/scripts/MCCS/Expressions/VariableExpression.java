package us.mccode.scripts.MCCS.Expressions;

import org.bukkit.entity.Player;
import us.mccode.scripts.MCCS.MCCS;
import us.mccode.scripts.MCCS.Values.NumberValue;
import us.mccode.scripts.MCCS.Values.Value;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class VariableExpression implements Expression {
    private final String name;
    private final Player player;

    public VariableExpression(String name, Player player) {
        this.name = name;
        this.player = player;
    }

    public Value evaluate() {
        if (MCCS.getScriptVariables(player).containsKey(name)) {
            return MCCS.getScriptVariables(player).get(name);
        }
        return new NumberValue(0);
    }
}
