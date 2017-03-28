package us.mccode.scripts.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mccode.scripts.MCCS.MCCS;
import us.mccode.scripts.MCCode;

/**
 * Created by Alec Dusheck on 3/26/2017.
 */
public class execute implements CommandExecutor{
    public execute(){
        MCCode.getPlugin().getCommand("/").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(MCCode.currentlyCompiling.contains(player)){
                player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You are already executing a script. Please stop the current one by executing \"/stopscript\".");
                return true;
            }
            sender.sendMessage("this is a debug command");
            try{
                MCCS.run(player, "furkansen26_test.txt");
            }catch (Exception e){
                //Bukkit with their errors....
            }
        }else{
            sender.sendMessage("You can't execute a script as console!");
        }
        return true;
    }
}
