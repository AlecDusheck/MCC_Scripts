package us.mccode.scripts.MCCS.Statements;

import org.bukkit.entity.Player;
import us.mccode.scripts.MCCS.Expressions.Expression;
import us.mccode.scripts.MCCS.MCCError;
import us.mccode.scripts.MCCS.MCCS;
import us.mccode.scripts.MCCode;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class stopStatement implements Statement {
    private final Player player;

    public stopStatement(Player player) {
        this.player = player;
    }

    public void execute() {
        MCCode.currentlyCompiling.remove(player);
    }
}
