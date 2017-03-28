package us.mccode.scripts.MCCS;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Created by Alec Dusheck on 3/23/2017.
 */
public class MCCError extends RuntimeException {

    public MCCError(Player player, String error, int ln) {
        player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "An error occurred in your script. The error was \"" + error + "\" (LN: " + ln + ").");
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
    }

    @Override
    public Throwable fillInStackTrace() {
        return null;
    }
}
