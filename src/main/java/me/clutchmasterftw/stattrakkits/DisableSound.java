package me.clutchmasterftw.stattrakkits;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class DisableSound implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;

            PersistentDataContainer data = player.getPersistentDataContainer();
            if(data.has(StattrakKits.enabledStatTrakSounds)) {
                if(data.get(StattrakKits.enabledStatTrakSounds, PersistentDataType.BOOLEAN) == true) {
                    data.set(StattrakKits.enabledStatTrakSounds, PersistentDataType.BOOLEAN, false);
                    player.sendMessage(StattrakKits.PREFIX + ChatColor.DARK_RED + "Disabled StatTrak™ sounds.");
                } else {
                    data.set(StattrakKits.enabledStatTrakSounds, PersistentDataType.BOOLEAN, true);
                    player.sendMessage(StattrakKits.PREFIX + ChatColor.DARK_GREEN + "Enabled StatTrak™ sounds.");
                }
            } else {
                data.set(StattrakKits.enabledStatTrakSounds, PersistentDataType.BOOLEAN, false);
                player.sendMessage(StattrakKits.PREFIX + ChatColor.DARK_RED + "Disabled StatTrak™ sounds.");
            }

            return true;
        } else {
            commandSender.sendMessage(ChatColor.RED + "You need to be a player to use this command!");
            return true;
        }
    }
}
