package us.mccode.scripts.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.mccode.scripts.MCCode;

/**
 * Created by Alec Dusheck on 3/26/2017.
 */
public class playerLeave implements Listener{
    public playerLeave(){
        MCCode.getPlugin().getServer().getPluginManager().registerEvents(this, MCCode.getPlugin());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (MCCode.currentlyCompiling.contains(player)) {
            MCCode.currentlyCompiling.remove(player);
        }
    }
}
