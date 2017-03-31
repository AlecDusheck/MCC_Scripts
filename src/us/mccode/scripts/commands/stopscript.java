package us.mccode.scripts.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.mccode.scripts.MCCode;

/**
 * Created by Alec Dusheck on 3/26/2017.
 */
public class stopscript implements CommandExecutor {
    public stopscript() {
        MCCode.getPlugin().getCommand("stopscript").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (MCCode.currentlyCompiling.contains(player)) {
                MCCode.currentlyCompiling.remove(player);
                player.sendMessage(net.md_5.bungee.api.ChatColor.DARK_GREEN + "" + net.md_5.bungee.api.ChatColor.BOLD + "(!) " + net.md_5.bungee.api.ChatColor.RESET + "" + net.md_5.bungee.api.ChatColor.GREEN + "Script stopped.");
                return true;
            }
            player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You are not currently executing a script.");
        } else {
            sender.sendMessage("You stop a script from console!");
        }
        return true;
    }
}
