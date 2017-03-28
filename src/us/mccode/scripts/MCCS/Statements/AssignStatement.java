package us.mccode.scripts.MCCS.Statements;

import org.bukkit.entity.Player;
import us.mccode.scripts.MCCS.Expressions.Expression;
import us.mccode.scripts.MCCS.MCCS;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class AssignStatement implements Statement {
    private final String name;
    private final Expression value;
    private final Player player;
    public AssignStatement(String name, Expression value, Player player) {
        this.name = name;
        this.value = value;
        this.player = player;
    }

    public void execute() {
        //MCCS.variables.put(name, value.evaluate());
        MCCS.putScriptVariables(player, name, value.evaluate());
    }
}
