package us.mccode.scripts.MCCS.Statements;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import us.mccode.scripts.MCCS.Expressions.Expression;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class PrintStatement implements Statement {
    private final Expression expression;
    private final Player player;

    public PrintStatement(Expression expression, Player player) {
        this.expression = expression;
        this.player = player;
    }

    public void execute() {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', expression.evaluate().toString()));
    }
}
