package me.clutchmasterftw.stattrakkits.events;

import me.clutchmasterftw.stattrakkits.StattrakKits;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Map;

import static me.clutchmasterftw.stattrakkits.utilities.Utilities.manipulateStat;

public class PlayerEvents implements Listener {
    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        EntityDamageEvent lastDamageCause = victim.getLastDamageCause();

        if(lastDamageCause != null && lastDamageCause.getEntity() instanceof Player) {
            Player killer = victim.getKiller();

            if(killer != null) {
                PlayerInventory killerInventory = killer.getInventory();
                ItemStack item = killerInventory.getItemInMainHand();

                Material material = item.getType();
                if(material.equals(Material.AIR)) return;
                if(!Arrays.asList(ApplyStattrakKit.swords).contains(material) || !Arrays.asList(ApplyStattrakKit.axes).contains(material)) return;

                ItemMeta meta = item.getItemMeta();
                if(meta == null) return;

                PersistentDataContainer data = meta.getPersistentDataContainer();
                if(data.has(StattrakKits.hasStatTrak)) {
                    if(data.get(StattrakKits.hasStatTrak, PersistentDataType.BOOLEAN)) {
                        // Item has StatTrak
                        manipulateStat(item, StattrakKits.blocksBroken, 1, 100, "Confirmed Kills", killer);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            int damage = (int) Math.ceil(e.getDamage());

            PlayerInventory inventory = player.getInventory();
            ItemStack[] armorSet = new ItemStack[] {inventory.getHelmet(), inventory.getChestplate(), inventory.getLeggings(), inventory.getBoots()};

            for(ItemStack item:armorSet) {
                if(item != null) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        PersistentDataContainer data = meta.getPersistentDataContainer();
                        if (data.has(StattrakKits.hasStatTrak)) {
                            if (data.get(StattrakKits.hasStatTrak, PersistentDataType.BOOLEAN)) {
                                // Item has StatTrak
                                manipulateStat(item, StattrakKits.damageTaken, damage, 10000, "Damage Taken", player);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDealDamage(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();

            if(e.getEntity() instanceof Player) {
                Player victim = (Player) e.getEntity();
                if(victim.isBlocking() && e.getFinalDamage() < e.getDamage()) {
                    // A shield successfully blocked the hit
                    PlayerInventory victimInventory = victim.getInventory();
                    ItemStack shield;
                    if(victimInventory.getItemInMainHand().getType().equals(Material.SHIELD)) {
                        shield = victimInventory.getItemInMainHand();
                    } else if(victimInventory.getItemInOffHand().getType().equals(Material.SHIELD)) {
                        shield = victimInventory.getItemInOffHand();
                    } else {
                        return;
                    }

                    ItemMeta meta = shield.getItemMeta();
                    if(meta == null) return;

                    PersistentDataContainer data = meta.getPersistentDataContainer();
                    if(data.has(StattrakKits.hasStatTrak)) {
                        if(data.get(StattrakKits.hasStatTrak, PersistentDataType.BOOLEAN)) {
                            // Item has StatTrak
                            manipulateStat(shield, StattrakKits.shields, 1, 100, "Shield Blocks", victim);
                        }
                    }

                    return;
                }

                int damage = (int) Math.ceil(e.getDamage());

                if(damager != null) {
                    PlayerInventory killerInventory = damager.getInventory();
                    ItemStack item = killerInventory.getItemInMainHand();

                    Material material = item.getType();
                    if(material.equals(Material.AIR)) return;
                    if(!Arrays.asList(ApplyStattrakKit.swords).contains(material) || !Arrays.asList(ApplyStattrakKit.axes).contains(material)) return;

                    ItemMeta meta = item.getItemMeta();
                    if(meta == null) return;

                    PersistentDataContainer data = meta.getPersistentDataContainer();
                    if(data.has(StattrakKits.hasStatTrak)) {
                        if(data.get(StattrakKits.hasStatTrak, PersistentDataType.BOOLEAN)) {
                            // Item has StatTrak
                            manipulateStat(item, StattrakKits.damageDealt, damage, 10000, "Damage Dealt", damager);
                        }
                    }
                }
            }
        }
    }

    private final Material[] fishAllowed = new Material[] {Material.COD, Material.SALMON, Material.TROPICAL_FISH, Material.PUFFERFISH};

    @EventHandler
    public void onPlayerFish(PlayerFishEvent e) {
        if(e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            if(e.getCaught() instanceof Item) {
                Item caughtEntity = (Item) e.getCaught();
                ItemStack caughtFish = caughtEntity.getItemStack();

                for(Material fish:fishAllowed) {
                    if(fish.equals(caughtFish.getType())) {
                        Player player = e.getPlayer();
                        PlayerInventory playerInventory = player.getInventory();
                        ItemStack fishingRod;
                        if(playerInventory.getItemInMainHand().getType().equals(Material.FISHING_ROD)) {
                            fishingRod = playerInventory.getItemInMainHand();
                        } else if(playerInventory.getItemInOffHand().getType().equals(Material.FISHING_ROD)) {
                            fishingRod = playerInventory.getItemInOffHand();
                        } else {
                            return;
                        }

                        ItemMeta meta = fishingRod.getItemMeta();
                        if(meta == null) return;

                        PersistentDataContainer data = meta.getPersistentDataContainer();
                        if(data.has(StattrakKits.hasStatTrak)) {
                            if(data.get(StattrakKits.hasStatTrak, PersistentDataType.BOOLEAN)) {
                                // Item has StatTrak
                                manipulateStat(fishingRod, StattrakKits.fishCaught, 1, 1000, "Fish Reeled", player);
                            }
                        }

                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWreckRegenBlockBreak(BlockBreakEvent e) {
        if(!e.isCancelled()) {
            Player player = e.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            if(meta == null) return;

            PersistentDataContainer data = meta.getPersistentDataContainer();
            if(data.has(StattrakKits.hasStatTrak)) {
                if(data.get(StattrakKits.hasStatTrak, PersistentDataType.BOOLEAN)) {
                    // Item has StatTrak
                    Material material = item.getType();
                    if(!Arrays.asList(ApplyStattrakKit.axes).contains(material)) return;

                    Location location = e.getBlock().getLocation();
                    for(Map<?, ?> block:StattrakKits.wreckRegenConfig.getMapList("blocks-to-regen")) {
                        Location blockLocation = new Location(Bukkit.getWorld("world"), (int) block.get("x"), (int) block.get("y"), (int) block.get("z"));

                        if(location.equals(blockLocation)) {
                            manipulateStat(item, StattrakKits.blocksBroken, 1, 10000, "Blocks Broken", player);

                            break;
                        }
                    }
                }
            }
        }
    }
}
