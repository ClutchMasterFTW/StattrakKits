package me.clutchmasterftw.stattrakkits.events;

import me.clutchmasterftw.stattrakkits.StattrakKits;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;

import java.util.Arrays;

import static me.clutchmasterftw.stattrakkits.utilities.Utilities.manipulateStat;

public class PrisonBlockBreaks implements Listener {
    @EventHandler
    public void onPrisonMineBlockBreak(PrisonMinesBlockBreakEvent e) {
        if(!e.isCancelled()) {
            ItemStack item = e.getItemInHand().getBukkitStack();
            if(item == null) return;

            Material material = item.getType();
            if(material.equals(Material.AIR)) return;
            if(!Arrays.asList(ApplyStattrakKit.pickaxes).contains(material)) return;

            ItemMeta meta = item.getItemMeta();;
            if(meta == null) return;

            PersistentDataContainer data = meta.getPersistentDataContainer();
            if(data.has(StattrakKits.hasStatTrak)) {
                if(data.get(StattrakKits.hasStatTrak, PersistentDataType.BOOLEAN)) {
                    // Item has StatTrak
                    Player player = e.getPlayer();
                    manipulateStat(item, StattrakKits.kills, 1, 100000, "Blocks Broken", player);
                }
            }
        }
    }
}
