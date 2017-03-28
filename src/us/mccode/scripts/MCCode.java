package us.mccode.scripts;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import us.mccode.scripts.commands.execute;
import us.mccode.scripts.commands.stopscript;
import us.mccode.scripts.events.playerLeave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by Alec Dusheck on 3/26/2017.
 */
public class MCCode extends JavaPlugin {

    //Version
    public static final double VERSION = 0.8;
    //Currently compiling
    public static ArrayList<Player> currentlyCompiling = new ArrayList<Player>();
    public static HashMap<Player, Long> startedTimes = new HashMap<Player, Long>();
    //Bukkit Loggers
    public static ConsoleCommandSender console; //Defined in onEnable()
    public static Logger log; //Same here
    //Instance
    static MCCode plugin;
    //Startup time
    private long startupTimer; //Defined in onEnable()

    public static MCCode getPlugin() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;

        //Loggers defined
        log = Bukkit.getLogger();
        console = Bukkit.getServer().getConsoleSender();
        //Message
        console.sendMessage(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "---------------------------");
        console.sendMessage(ChatColor.BLUE + "Starting up MCCode_Scripts(rel-" + VERSION + ")");
        console.sendMessage(ChatColor.BLUE + "Don't distribute!");
        console.sendMessage(ChatColor.BLUE + "Bukkit plugin by: Alec Dusheck");
        console.sendMessage(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "---------------------------");
        startupTimer = System.currentTimeMillis();

        new execute();
        new stopscript();
        new playerLeave();
        new scriptRunning();

        //Message (Done)
        console.sendMessage(ChatColor.BLUE + "Done, took " + (System.currentTimeMillis() - startupTimer) + "ms.");
    }
}
