package us.mccode.scripts.MCCS.Expressions;

import us.mccode.scripts.MCCS.MCCS;
import us.mccode.scripts.MCCS.Values.NumberValue;
import us.mccode.scripts.MCCS.Values.Value;
import org.bukkit.entity.Player;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class VariableExpression implements Expression {
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

    private final String name;
    private final Player player;
}
