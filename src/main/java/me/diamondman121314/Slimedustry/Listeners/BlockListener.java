package me.diamondman121314.Slimedustry.Listeners;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import me.diamondman121314.Slimedustry.Main;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


public class BlockListener implements Listener {
  Plugin plugin;

  public BlockListener(Main plugin) {
    this.plugin = (Plugin)plugin;
    plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockBreak(BlockBreakEvent e) {
    Block b = e.getBlock();
    Block down = b.getRelative(BlockFace.DOWN);
    Player p = e.getPlayer();
    if (b.getType() == Material.WHITE_STAINED_GLASS && down.getType() == Material.DISPENSER) {
      Dispenser d = (Dispenser)down.getState();
      if (down.getData() == 1) {
        d.getInventory().clear();
        down.breakNaturally();
      }
    }
    if (b.getType() == Material.DISPENSER && b.getRelative(BlockFace.UP).getType() == Material.WHITE_STAINED_GLASS && b.getData() == 1) {
      Dispenser d = (Dispenser)b.getState();
      if (d.getInventory().contains(new CustomItem(Material.TERRACOTTA, "&6Lava")) || d.getInventory().contains(new CustomItem(Material.TERRACOTTA, "&bWater"))) {
        d.getInventory().clear();
      }
      b.getRelative(BlockFace.UP).setType(Material.AIR);
    }
    if (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE && p.getInventory().getItemInMainHand().hasItemMeta()) {
      if (!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
        return;
      }
      if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("&bRock Cutter")) {
        if (Slimefun.hasUnlocked(p, p.getInventory().getItemInMainHand(), true)) {
          List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();
          double charge = Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue();
          if (charge - this.plugin.getConfig().getInt("RockCutterCharge") >= 0.0) {
            charge = Double.valueOf((new DecimalFormat("##.##")).format(charge - this.plugin.getConfig().getInt("RockCutterCharge")).replace(",", ".")).doubleValue();
            lore.set(1, "&7Charge: &b" + String.valueOf(charge) + " J");
            ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
            im.setLore(lore);
            p.getInventory().getItemInMainHand().setItemMeta(im);
            p.getInventory().getItemInMainHand().setDurability((short)0);
          } else {
            e.setCancelled(true);
            p.getInventory().getItemInMainHand().setDurability((short)0);
          }
        }
        p.getInventory().getItemInMainHand().setDurability((short)0);
      }
    }
  }
  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockPlace(BlockPlaceEvent e) {
    Player p = e.getPlayer();
    List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();
    String name = p.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
    if (lore == null) {
      return;
    }
    if (((String)lore.get(0)).equalsIgnoreCase("&cAre you sure this is a good idea?") && name.equalsIgnoreCase("&4Nuke")) {
      e.setCancelled(true);
      if (Slimefun.hasUnlocked(p, p.getInventory().getItemInMainHand(), true)) {
        if (p.getInventory().getItemInMainHand().getAmount() > 1) {
          p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
        } else {
          p.getInventory().removeItem(p.getInventory().getItemInMainHand());
        }
        Location l = e.getBlock().getLocation();
        l.setX(e.getBlock().getLocation().getX() + 0.5D);
        l.setZ(e.getBlock().getLocation().getZ() + 0.5D);
        final TNTPrimed tnt = (TNTPrimed)p.getWorld().spawnEntity(l, EntityType.PRIMED_TNT);
        tnt.setFuseTicks(this.plugin.getConfig().getInt("Nuke.Delay") * 20 + 20);
        Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable()
            {
              public void run() {
                tnt.remove();
                Location l = tnt.getLocation();
                l.setX(tnt.getLocation().getX() + 0.5D);
                l.setZ(tnt.getLocation().getZ() + 0.5D);
                tnt.getWorld().createExplosion(l, BlockListener.this.plugin.getConfig().getInt("Nuke.ExplosionStrength"));
              }
            }, this.plugin.getConfig().getInt("Nuke.Delay") * 20);
      }
    }
  }
  @EventHandler(priority = EventPriority.LOWEST)
  public void onLeavesDecay(LeavesDecayEvent e) {
    Block b = e.getBlock();
    int RandomNumber = (new Random()).nextInt(this.plugin.getConfig().getInt("StickyResinDropChance") - 1) + 1;
    if (RandomNumber == 3)
      b.getWorld().dropItemNaturally(b.getLocation(), new CustomItem(Material.CLAY_BALL, "&bSticky Resin"));
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onDispenserShoot(BlockDispenseEvent e) {
    if (e.getBlock().getType() == Material.DISPENSER) {
      Dispenser d = (Dispenser)e.getBlock().getState();
      if (e.getItem().getType() == Material.TERRACOTTA && e.getItem().hasItemMeta() && (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("&bWater") || e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("&6Lava"))) e.setCancelled(true);
    }
  }
}
