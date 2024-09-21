package me.clutchmasterftw.stattrakkits.events;

import dev.lone.itemsadder.api.CustomStack;
import me.clutchmasterftw.stattrakkits.StattrakKits;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ApplyStattrakKit implements Listener {
    // Allowable StatTrak applicable items
    public static final Material[] axes = new Material[] {
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.GOLDEN_AXE,
        Material.IRON_AXE,
        Material.DIAMOND_AXE,
        Material.NETHERITE_AXE
    };
    public static final Material[] pickaxes = new Material[] {
        Material.WOODEN_PICKAXE,
        Material.STONE_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.IRON_PICKAXE,
        Material.DIAMOND_PICKAXE,
        Material.NETHERITE_PICKAXE
    };
    public static final Material[] swords = new Material[] {
        Material.WOODEN_SWORD,
        Material.STONE_SWORD,
        Material.GOLDEN_SWORD,
        Material.IRON_SWORD,
        Material.DIAMOND_SWORD,
        Material.NETHERITE_SWORD
    };
    public static final Material[] armors = new Material[] {
        Material.LEATHER_HELMET,
        Material.LEATHER_CHESTPLATE,
        Material.LEATHER_LEGGINGS,
        Material.LEATHER_BOOTS,
        Material.GOLDEN_HELMET,
        Material.GOLDEN_CHESTPLATE,
        Material.GOLDEN_LEGGINGS,
        Material.GOLDEN_BOOTS,
        Material.CHAINMAIL_HELMET,
        Material.CHAINMAIL_CHESTPLATE,
        Material.CHAINMAIL_LEGGINGS,
        Material.CHAINMAIL_BOOTS,
        Material.IRON_HELMET,
        Material.IRON_CHESTPLATE,
        Material.IRON_LEGGINGS,
        Material.IRON_BOOTS,
        Material.DIAMOND_HELMET,
        Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS,
        Material.DIAMOND_BOOTS,
        Material.NETHERITE_HELMET,
        Material.NETHERITE_CHESTPLATE,
        Material.NETHERITE_LEGGINGS,
        Material.NETHERITE_BOOTS
    };
    public static final Material shield = Material.SHIELD;
    public static final Material fishingRod = Material.FISHING_ROD;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack primaryHand = inventory.getItemInMainHand();
        ItemStack offhand = inventory.getItemInOffHand();
        if(e.getAction().isRightClick() && e.getHand() == EquipmentSlot.OFF_HAND && !primaryHand.getType().equals(Material.AIR) && !offhand.getType().equals(Material.AIR)) {
            CustomStack stattrakKitInstance = CustomStack.getInstance("stattrakkit");
            if(stattrakKitInstance.getItemStack().isSimilar(offhand)) {
                // Item is a StatTrak kit
                e.setCancelled(true);
                Material primaryHandMaterial = primaryHand.getType();

                for(Material axe:axes) {
                    if(primaryHandMaterial.equals(axe)) {
                        applyStattrak("axe", player);
                        return;
                    }
                }
                for(Material pickaxe:pickaxes) {
                    if(primaryHandMaterial.equals(pickaxe)) {
                        applyStattrak("pickaxe", player);
                        return;
                    }
                }
                for(Material sword:swords) {
                    if(primaryHandMaterial.equals(sword)) {
                        applyStattrak("sword", player);
                        return;
                    }
                }
                for(Material armor:armors) {
                    if(primaryHandMaterial.equals(armor)) {
                        applyStattrak("armor", player);
                        return;
                    }
                }
                if(primaryHandMaterial.equals(shield)) {
                    applyStattrak("shield", player);
                } else if(primaryHandMaterial.equals(fishingRod)) {
                    applyStattrak("fishingRod", player);
                } else {
                    player.sendMessage(StattrakKits.PREFIX + "Please use an acceptable item to successfully apply a " + ChatColor.GOLD + "StatTrak™" + ChatColor.RESET + " Kit. Acceptable items are the following:");
                    player.sendMessage("Axes, Pickaxes, Swords, Armor, Fishing Rods, or Shields.");
                }
            }
        }
    }

    public void applyStattrak(String type, Player player) {
        PlayerInventory playerInventory = player.getInventory();
        ItemStack item = playerInventory.getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore == null)  {
            lore = new ArrayList<>();
        }

        String itemName;
        if(meta.hasDisplayName()) {
            itemName = meta.getDisplayName();
        } else {
            // Form the item's name from the default material name
            String[] words = item.getType().name().split("_");
            StringBuilder formattedName = new StringBuilder();
            for(String word:words) {
                formattedName.append(word.charAt(0)).append(word.substring(1).toLowerCase()).append(" ");
            }

            if(!item.getEnchantments().isEmpty()) {
                itemName = ChatColor.AQUA + String.valueOf(formattedName);
            } else {
                itemName = String.valueOf(formattedName);
            }
        }

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(StattrakKits.hasStatTrak, PersistentDataType.BOOLEAN, true);

        meta.setDisplayName(ChatColor.GOLD + "StatTrak™ " + ChatColor.RESET + itemName);

        lore.add("");
        lore.add(ChatColor.GOLD + "StatTrak™ Kit Applied");
        if(type.equals("axe")) {
            // Tracks player kills, damage dealt, and blocks broken (from worldguard)
        } else if(type.equals("pickaxe")) {
            // Tracks blocks broken (from prison)
            lore = initializeStat(new String[]{"blocks"}, lore);
            data.set(StattrakKits.blocksBroken, PersistentDataType.INTEGER, 0);
        } else if(type.equals("sword")) {
            // Tracks player kills and damage dealt
        } else if(type.equals("armor")) {
            // Tracks protection
        } else if(type.equals("shield")) {
            // Tracks amount of blocks
        } else if(type.equals("fishingRod")) {
            // Tracks amount of fish found
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        int offhandAmount = playerInventory.getItemInOffHand().getAmount();
        if(offhandAmount > 1) {
            playerInventory.getItemInOffHand().setAmount(offhandAmount - 1);
        } else {
            playerInventory.setItemInOffHand(null);
        }
        playerInventory.setItemInMainHand(item);

        StattrakKits.getPlugin().getLogger().info(player.getName() + " (" + player.getUniqueId() + ") has applied a StatTrak Kit to a " + itemName + ".");
        player.sendMessage(StattrakKits.PREFIX + "Successfully applied a " + ChatColor.GOLD + "StatTrak™" + ChatColor.RESET + " Kit to your " + itemName + ChatColor.RESET + "!");
    }

    public List<String> initializeStat(String[] stats, List<String> lore) {
        for(String stat:stats) {
            if(stat.equals("kills")) {
                lore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "0" + ChatColor.WHITE + " Confirmed Kills");
            } else if(stat.equals("damage")) {
                lore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "0" + ChatColor.WHITE + " Damage Dealt");
            } else if(stat.equals("blocks")) {
                lore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "0" + ChatColor.WHITE + " Blocks Broken");
            } else if(stat.equals("protection")) {
                lore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "0" + ChatColor.WHITE + " Damage Taken");
            } else if(stat.equals("shields")) {
                lore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "0" + ChatColor.WHITE + " Shield Blocks");
            } else if(stat.equals("fish")) {
                lore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "0" + ChatColor.WHITE + " Fish Reeled");
            }
        }
        return lore;
    }
}