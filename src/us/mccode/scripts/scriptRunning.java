package us.mccode.scripts;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

/**
 * Created by Alec Dusheck on 3/27/2017.
 */
public class scriptRunning extends BukkitRunnable {
    public scriptRunning() {
        this.runTaskTimer(MCCode.getPlugin(), 0L, 1L);
    }

    public void run() {
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (MCCode.currentlyCompiling.contains(all)) {
                double timeRunning = System.currentTimeMillis() - MCCode.startedTimes.get(all);
                double timeRunningSecs = timeRunning / 1000;

                DecimalFormat numberFormat = new DecimalFormat("#.0");

                if (timeRunningSecs < 1) {
                    TitleManager.sendActionBar(all, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "" + ChatColor.GREEN + "Currently running script. (" + numberFormat.format(timeRunningSecs) + " sec)");
                } else {
                    TitleManager.sendActionBar(all, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "(!) " + ChatColor.RESET + "" + ChatColor.GREEN + "Currently running script. (" + numberFormat.format(timeRunningSecs) + " secs)");
                }

                if (timeRunningSecs >= 60) {
                    all.sendMessage(org.bukkit.ChatColor.DARK_RED + "" + org.bukkit.ChatColor.BOLD + "(!) " + org.bukkit.ChatColor.RED + "Your script was automatically terminated. (60 sec limit).");
                    MCCode.currentlyCompiling.remove(all);
                    all.sendMessage(org.bukkit.ChatColor.DARK_RED + "" + org.bukkit.ChatColor.BOLD + "(!) " + org.bukkit.ChatColor.RED + "Upgrade to PRO to remove this limit.");

                    all.getWorld().playSound(all.getLocation(), Sound.ENTITY_VILLAGER_NO, 10, 1);
                }
            }
        }
    }
}
