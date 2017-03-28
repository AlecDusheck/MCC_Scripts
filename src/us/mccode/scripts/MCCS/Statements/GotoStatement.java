package us.mccode.scripts.MCCS.Statements;

import org.bukkit.entity.Player;
import us.mccode.scripts.MCCS.MCCS;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class GotoStatement implements Statement {
    private final String label;
    private final Player player;

    public GotoStatement(String label, Player player) {
        this.label = label;
        this.player = player;
    }

    public void execute() {
        if (MCCS.getScriptLabels(player).containsKey(label)) {
            //MCCS.currentStatement = MCCS.labels.get(label).intValue();
            MCCS.setCurrentStatement(player, MCCS.getScriptLabels(player).get(label).intValue());
        }
    }
}