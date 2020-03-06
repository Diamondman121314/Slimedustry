package me.diamondman121314.Slimedustry.Listeners;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import me.diamondman121314.Slimedustry.Main;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;


public class EntitiesListener implements Listener {
  private static HashMap<Player, Vector> Locations = new HashMap<>();

  public EntitiesListener(Main plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
    this.plugin = (Plugin)plugin;
  }
  Plugin plugin;
  @EventHandler(priority = EventPriority.LOWEST)
  public void onProjectileHit(ProjectileHitEvent e) {
    if (e.getEntityType() == EntityType.SNOWBALL) {
      Snowball s = (Snowball)e.getEntity();
      Player p = (Player)s.getShooter();
      if (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HORSE_ARMOR && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("&bMining Laser")) {
        BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0, 4);
        Block b = null;
        while (iterator.hasNext()) {
          b = iterator.next();
          if (b.getType() != Material.AIR) {
            break;
          }
        }
        if (b.getType() == Material.BEDROCK) {
          return;
        }
        Vector vec = s.getVelocity();
        s.remove();
        if (((String)p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0)).equalsIgnoreCase("&6Mode: &1Mining")) {
          b.breakNaturally();
        }

        if (((String)p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0)).equalsIgnoreCase("&6Mode: &1Explosive")) {
          b.getWorld().createExplosion(b.getLocation(), this.plugin.getConfig().getInt("MiningLaser.ExplosionStrength"));
        }
        if (((String)p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0)).equalsIgnoreCase("&6Mode: &1SuperHeat"))
          b.setType(Material.FIRE);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerMove(PlayerMoveEvent e) {
    Player p = e.getPlayer();
    if (p.getInventory().getBoots() == null) {
      return;
    }
    if (!p.getInventory().getBoots().hasItemMeta()) {
      return;
    }
    if (!p.getInventory().getBoots().getItemMeta().hasDisplayName()) {
      return;
    }
    if (p.getInventory().getBoots().getType() == Material.LEATHER_BOOTS && p.getInventory().getBoots().getItemMeta().getDisplayName().equalsIgnoreCase("&6Static Boots") && !e.getFrom().toVector().equals(e.getTo().toVector()) && p.getLocation().getBlock().getType() != Material.AIR && Slimefun.hasUnlocked(p, p.getInventory().getBoots(), true) && p.getInventory().getItemInMainHand().getItemMeta() != null && p.getInventory().getItemInMainHand().getItemMeta().getLore() != null) {
      List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();


      double charge = Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue();
      double capacity = Double.valueOf(((String)lore.get(2)).replace("Capacity: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue();
      if (lore.size() > 2 && ((String)lore.get(1)).contains("Charge:") && ((String)lore.get(2)).contains("Capacity:") && Double.valueOf((new DecimalFormat("##.##")).format(charge + this.plugin.getConfig().getDouble("StaticBootsGeneration")).replace(",", ".")).doubleValue() <= capacity) {
        charge = Double.valueOf((new DecimalFormat("##.##")).format(charge + this.plugin.getConfig().getDouble("StaticBootsGeneration")).replace(",", ".")).doubleValue();
        lore.set(1, "&7Charge: &b" + String.valueOf(charge) + " J");
        ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
        im.setLore(lore);
        p.getInventory().getItemInMainHand().setItemMeta(im);
      }
    }
  }


  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerDamage(EntityDamageEvent e) {
    Entity ent = e.getEntity();
    if (ent.getType() == EntityType.PLAYER) {
      Player p = (Player)ent;
      if (p.getInventory().getBoots() != null && p.getInventory().getBoots().hasItemMeta() && p.getInventory().getBoots().getItemMeta().hasDisplayName() && p.getInventory().getBoots().getItemMeta().getDisplayName().equalsIgnoreCase("&8&lNanoSuit Boots")) {
        p.getInventory().getBoots().setDurability((short)0);
        if (Slimefun.hasUnlocked(p, p.getInventory().getBoots(), true)) {
          ItemMeta im = p.getInventory().getBoots().getItemMeta();
          List<String> lore = im.getLore();
          if (Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue() - this.plugin.getConfig().getDouble("NanoSuitCharge") >= 0.0) {
            e.setDamage(e.getDamage() - 2.0);
            lore.set(1, "&7Charge: &b" + Double.valueOf((new DecimalFormat("##.##")).format(Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue() - this.plugin.getConfig().getDouble("NanoSuitCharge"))));
            im.setLore(lore);
            p.getInventory().getBoots().setItemMeta(im);
          }
        }
      }


      if (p.getInventory().getLeggings() != null && p.getInventory().getLeggings().hasItemMeta() && p.getInventory().getLeggings().getItemMeta().hasDisplayName() && p.getInventory().getLeggings().getItemMeta().getDisplayName().equalsIgnoreCase("&8&lNanoSuit Leggings")) {
        p.getInventory().getLeggings().setDurability((short)0);
        if (Slimefun.hasUnlocked(p, p.getInventory().getLeggings(), true)) {
          ItemMeta im = p.getInventory().getLeggings().getItemMeta();
          List<String> lore = im.getLore();
          if (Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue() - this.plugin.getConfig().getDouble("NanoSuitCharge") >= 0.0) {
            e.setDamage(e.getDamage() - 3.0);
            lore.set(1, "&7Charge: &b" + Double.valueOf((new DecimalFormat("##.##")).format(Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue() - this.plugin.getConfig().getDouble("NanoSuitCharge"))));
            im.setLore(lore);
            p.getInventory().getLeggings().setItemMeta(im);
          }
        }
      }


      if (p.getInventory().getChestplate() != null && p.getInventory().getChestplate().hasItemMeta() && p.getInventory().getChestplate().getItemMeta().hasDisplayName() && p.getInventory().getChestplate().getItemMeta().getDisplayName().equalsIgnoreCase("&8&lNanoSuit Chestplate")) {
        p.getInventory().getChestplate().setDurability((short)0);
        if (Slimefun.hasUnlocked(p, p.getInventory().getChestplate(), true)) {
          ItemMeta im = p.getInventory().getChestplate().getItemMeta();
          List<String> lore = im.getLore();
          if (Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue() - this.plugin.getConfig().getDouble("NanoSuitCharge") >= 0.0) {
            e.setDamage(e.getDamage() - 4.0);
            lore.set(1, "&7Charge: &b" + Double.valueOf((new DecimalFormat("##.##")).format(Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue() - this.plugin.getConfig().getDouble("NanoSuitCharge"))));
            im.setLore(lore);
            p.getInventory().getChestplate().setItemMeta(im);
          }
        }
      }


      if (p.getInventory().getHelmet() != null && p.getInventory().getHelmet().hasItemMeta() && p.getInventory().getHelmet().getItemMeta().hasDisplayName() && p.getInventory().getHelmet().getItemMeta().getDisplayName().equalsIgnoreCase("&8&lNanoSuit Helmet")) {
        p.getInventory().getHelmet().setDurability((short)0);
        if (Slimefun.hasUnlocked(p, p.getInventory().getHelmet(), true)) {
          ItemMeta im = p.getInventory().getHelmet().getItemMeta();
          List<String> lore = im.getLore();
          if (Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue() - this.plugin.getConfig().getDouble("NanoSuitCharge") >= 0.0) {
            e.setDamage(e.getDamage() - 2.0);
            lore.set(1, "&7Charge: &b" + Double.valueOf((new DecimalFormat("##.##")).format(Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue() - this.plugin.getConfig().getDouble("NanoSuitCharge"))));
            im.setLore(lore);
            p.getInventory().getHelmet().setItemMeta(im);
          }
        }
      }


      if (p.getInventory().getBoots() != null && p.getInventory().getBoots().hasItemMeta() && p.getInventory().getBoots().getItemMeta().hasDisplayName() && p.getInventory().getBoots().getItemMeta().getDisplayName().equalsIgnoreCase("&6Rubber Boots")) {
        p.getInventory().getBoots().setDurability((short)0);
        if (Slimefun.hasUnlocked(p, p.getInventory().getBoots(), true) && e.getCause() == EntityDamageEvent.DamageCause.FALL)
          e.setCancelled(true);
      }
    }
  }
}
