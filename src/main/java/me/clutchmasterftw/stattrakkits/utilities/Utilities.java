package me.clutchmasterftw.stattrakkits.utilities;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Utilities {
    public static String getShadeOfRed(int val, int max) {
        // Ensure the value of x is clamped between 0 and the like max
        val = Math.max(0, Math.min(val, max));

        // Calculate green and blue components
        int greenBlueValue = 255 - (255 * val / max);

        // Red is constant at 255
        int red = 255;
        int green = greenBlueValue;
        int blue = greenBlueValue;

        // Convert the RGB values to a hex string
        String formatted = String.format("#%02X%02X%02X", red, green, blue);
        if(formatted.equals("#FFFFFE") || formatted.equals("#FFFFFD") || formatted.equals("#FFFFFB") || formatted.equals("#FFFFFE") || formatted.equals("#FFFFFC") || formatted.equals("#FFFEFE")) return "#FFFFFF";
        return formatted;
    }

    public static void manipulateStat(ItemStack item, NamespacedKey key, int amount, int shadeMax, String searchTerm) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        int loreLine = -1;
        for(int i = 0; i < lore.size(); i++) {
            if(lore.get(i).contains(searchTerm)) {
                loreLine = i;
                break;
            }
        }

        int currentStatAmount = data.get(key, PersistentDataType.INTEGER);
        currentStatAmount += amount;
        data.set(key, PersistentDataType.INTEGER, currentStatAmount);

        lore.set(loreLine, net.md_5.bungee.api.ChatColor.of((currentStatAmount / shadeMax > 10 ? "#FFFFFE" : getShadeOfRed(currentStatAmount, shadeMax))) + "" + ChatColor.BOLD + String.format("%,d", currentStatAmount) + ChatColor.WHITE + " " + searchTerm);

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
