package us.mccode.scripts.MCCS.Statements;

import org.bukkit.entity.Player;
import us.mccode.scripts.MCCS.Expressions.Expression;
import us.mccode.scripts.MCCS.MCCError;
import us.mccode.scripts.MCCS.MCCS;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class healStatement implements Statement {
    private final Expression expression;
    private final Player player;

    public healStatement(Expression expression, Player player) {
        this.expression = expression;
        this.player = player;
    }

    public void execute() {
        try {
            double add = expression.evaluate().toNumber();
            if (player.getHealth() + add > 20) {
                player.setHealth(20);
            } else {
                player.setHealth(player.getHealth() + add);
            }
        } catch (Exception e) {
            throw new MCCError(player, "An error occurred while executing healStatement. (" + e.toString() + ")", MCCS.getCurrentStatement(player));
        }
    }
}
