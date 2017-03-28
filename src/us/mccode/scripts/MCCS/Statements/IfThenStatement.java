package us.mccode.scripts.MCCS.Statements;

import us.mccode.scripts.MCCS.Expressions.Expression;
import us.mccode.scripts.MCCS.MCCS;
import org.bukkit.entity.Player;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class IfThenStatement implements Statement {
    public IfThenStatement(Expression condition, String label, Player player) {
        this.condition = condition;
        this.label = label;
        this.player = player;
    }

    public void execute() {
        if (MCCS.getScriptLabels(player).containsKey(label)) {
            double value = condition.evaluate().toNumber();
            if (value != 0) {
                //MCCS.currentStatement = MCCS.labels.get(label).intValue();
                MCCS.setCurrentStatement(player, MCCS.getScriptLabels(player).get(label).intValue());
            }
        }
    }

    private final Expression condition;
    private final String label;
    private final Player player;
}
