package us.mccode.scripts.MCCS.Statements;

import us.mccode.scripts.MCCS.Expressions.Expression;
import us.mccode.scripts.MCCS.MCCError;
import us.mccode.scripts.MCCS.MCCS;
import org.bukkit.entity.Player;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class healStatement implements Statement{
    public healStatement(Expression expression, Player player){
        this.expression = expression;
        this.player = player;
    }

    public void execute(){
        try{
            double add = expression.evaluate().toNumber();
            if(player.getHealth() + add > 20){
                player.setHealth(20);
            }else{
                player.setHealth(player.getHealth() + add);
            }
        }catch (Exception e){
            throw new MCCError(player, "An error occurred while executing healStatement. (" + e.toString() + ")", MCCS.getCurrentStatement(player));
        }
    }
    private final Expression expression;
    private final Player player;
}
