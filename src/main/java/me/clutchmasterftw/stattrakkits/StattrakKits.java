package me.clutchmasterftw.stattrakkits;

import me.clutchmasterftw.stattrakkits.events.ApplyStattrakKit;
import me.clutchmasterftw.stattrakkits.events.PlayerEvents;
import me.clutchmasterftw.stattrakkits.events.PrisonBlockBreaks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class StattrakKits extends JavaPlugin {
    public static final String PREFIX = ChatColor.BLUE + "Wreck" + ChatColor.DARK_BLUE + "MC" + ChatColor.GRAY + " » " + ChatColor.RESET;

    public static StattrakKits getPlugin() {
        return plugin;
    }

    private static StattrakKits plugin;

    public static FileConfiguration wreckRegenConfig;

    public static NamespacedKey hasStatTrak;
    public static NamespacedKey enabledStatTrakSounds;
    public static NamespacedKey blocksBroken;
    public static NamespacedKey damageTaken;
    public static NamespacedKey damageDealt;
    public static NamespacedKey kills;
    public static NamespacedKey shields;
    public static NamespacedKey fishCaught;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        Logger logger = getLogger();

        Bukkit.getServer().getPluginManager().registerEvents(new ApplyStattrakKit(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PrisonBlockBreaks(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        getCommand("stattraksoundtoggle").setExecutor(new DisableSound());

        // NamespacedKeys:
        hasStatTrak = new NamespacedKey(getPlugin(), "hasStatTrak");
        enabledStatTrakSounds = new NamespacedKey(getPlugin(), "enabledStatTrakSounds");
        blocksBroken = new NamespacedKey(getPlugin(), "blocksBroken");
        damageTaken = new NamespacedKey(getPlugin(), "damageTaken");
        damageTaken = new NamespacedKey(getPlugin(), "damageDealt");
        kills = new NamespacedKey(getPlugin(), "kills");
        shields = new NamespacedKey(getPlugin(), "shields");
        fishCaught = new NamespacedKey(getPlugin(), "fishCaught");

        // Get WreckRegen Config file
        Plugin wreckRegen = Bukkit.getServer().getPluginManager().getPlugin("WreckRegen");
        if(wreckRegen != null && wreckRegen.isEnabled()) {
            wreckRegenConfig = wreckRegen.getConfig();
        } else {
            logger.severe("There was an issue attempting to hook into WreckRegen.");
        }


        logger.info("\n  _________ __          __ ___________              __      ____  __.__  __          \n" +
                " /   _____//  |______ _/  |\\__    ___/___________  |  | __ |    |/ _|__|/  |_  ______\n" +
                " \\_____  \\\\   __\\__  \\\\   __\\|    |  \\_  __ \\__  \\ |  |/ / |      < |  \\   __\\/  ___/\n" +
                " /        \\|  |  / __ \\|  |  |    |   |  | \\// __ \\|    <  |    |  \\|  ||  |  \\___ \\ \n" +
                "/_______  /|__| (____  /__|  |____|   |__|  (____  /__|_ \\ |____|__ \\__||__| /____  >\n" +
                "        \\/           \\/                          \\/     \\/         \\/             \\/ ");

        logger.info("---------------------------------------");
        logger.info("StatTrakKits has been successfully enabled.");
        logger.info("This plugin was developed by ClutchMasterFTW.");
        logger.info("All rights reserved.");
        logger.info("---------------------------------------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

// Plans:
// Make an admin permission command where they can change the value of certain stats when holding the tool and typing in certain parameters.