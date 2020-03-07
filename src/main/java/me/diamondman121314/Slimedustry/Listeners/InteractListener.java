package me.diamondman121314.Slimedustry.Listeners;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.diamondman121314.Slimedustry.Slimedustry;
import me.diamondman121314.Slimedustry.Setup;
import me.mrCookieSlime.Slimefun.cscorelib2.inventory.InvUtils;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


public class InteractListener implements Listener {
    Plugin plugin;

    public InteractListener(Slimedustry plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
        this.plugin = (Plugin)plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (b.getType() == Material.CRAFTING_TABLE && b.getRelative(BlockFace.UP).getType() == Material.DISPENSER && b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE && Slimefun.hasUnlocked(p, new CustomItem(Material.CRAFTING_TABLE, "&bIndustrial Crafting Table", "", "&a&oUsed for Industrial Crafting"), true)) {
                e.setCancelled(true);
                Dispenser d = (Dispenser)b.getRelative(BlockFace.UP).getState();
                final Inventory inv = d.getInventory();
                List<ItemStack[]> recipes = Setup.iwMachine.getRecipes();



                List<ItemStack[]> convertable = new ArrayList();

                int i;
                for (i = 0; i < recipes.size(); i++) {
                    if (i % 2 == 0) {
                        convertable.add(recipes.get(i));
                    }
                }

                for (i = 0; i < convertable.size(); i++) {
                    boolean craftable = true;
                    for (int j = 0; j < (inv.getContents()).length; j++) {
                        if (!SlimefunManager.isItemSimilar(inv.getContents()[j], ((ItemStack[])convertable.get(i))[j], true)) {
                            craftable = false;
                            break;
                        }
                    }
                    if (craftable) {
                        ItemStack adding = ((ItemStack[])recipes.get(recipes.indexOf(convertable.get(i)) + 1))[0];
                        if (Slimefun.hasUnlocked(p, adding, true)) {
                            Inventory inv2 = Bukkit.createInventory(null, 9, "test"); int k;
                            for (k = 0; k < (inv.getContents()).length; k++) {
                                inv2.setItem(k, inv.getContents()[k]);
                            }
                            for (k = 0; k < 9; k++) {
                                if (inv2.getContents()[k] != null && inv2.getContents()[k].getType() != Material.AIR) {
                                    if (inv2.getContents()[k].getAmount() > 1) { inv2.setItem(k, (ItemStack)new CustomItem(inv2.getContents()[k], inv2.getContents()[k].getAmount() - 1)); }
                                    else { inv2.setItem(k, null); }

                                }
                            }
                            if (InvUtils.fits(inv2, adding)) {
                                for (k = 0; k < 9; k++) {
                                    if (inv.getContents()[k] != null && inv.getContents()[k].getType() != Material.AIR) {
                                        if (inv.getContents()[k].getAmount() > 1) { inv.setItem(k, (ItemStack)new CustomItem(inv.getContents()[k], inv.getContents()[k].getAmount() - 1)); }
                                        else { inv.setItem(k, null); }

                                    }
                                }
                                p.getWorld().playSound(b.getLocation(), Sound.BLOCK_LAVA_POP, 1.0F, 1.0F); // used to be WOOD_CLICK
                                inv.addItem(new ItemStack[] { adding });
                            } else {
                                //Messages.local.sendTranslation(p, Slimefun.getPrefix(true), "machines.full-inventory");
                                SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
                            }
                        }
                        return;
                    }
                }
                //Messages.local.sendTranslation(p, Slimefun.getPrefix(true), "machines.pattern-not-found");
                SlimefunPlugin.getLocal().sendMessage(p, "machines.pattern-not-found", true);

            }


            if (b.getType() == Material.PISTON && b.getRelative(BlockFace.DOWN).getType() == Material.DISPENSER && Slimefun.hasUnlocked(p, new CustomItem(Material.PISTON, "&bPlate Bender", "", "&a&oCan bend Ingots into Plates"), true)) {
                e.setCancelled(true);
                Dispenser d = (Dispenser)b.getRelative(BlockFace.DOWN).getState();
                final Inventory inv = d.getInventory();
                List<ItemStack[]> recipes = Setup.pbMachine.getRecipes();



                List<ItemStack> convertible = new ArrayList();
                for (int i = 0; i < recipes.size(); i++) {
                    if (i % 2 == 0)
                        convertible.add(((ItemStack[])recipes.get(i))[0]);
                }
                byte b1;
                int j;
                ItemStack[] arrayOfItemStack;
                for (j = (arrayOfItemStack = inv.getContents()).length, b1 = 0; b1 < j; ) { ItemStack current = arrayOfItemStack[b1];
                    for (Iterator<ItemStack> localIterator1 = convertible.iterator(); localIterator1.hasNext(); ) {
                        ItemStack convert = localIterator1.next();
                        if (current != null &&
                                SlimefunManager.isItemSimilar(current, convert, true)) {
                            List<ItemStack> newRecipes = new ArrayList();
                            for (ItemStack[] recipe : recipes) {
                                newRecipes.add(recipe[0]);
                            }
                            ItemStack adding = newRecipes.get(newRecipes.indexOf(convert) + 1);
                            if (InvUtils.fits(inv, adding)) {
                                Object removing = current.clone();
                                ((ItemStack)removing).setAmount(1);
                                inv.removeItem(new ItemStack[] { (ItemStack)removing });
                                inv.addItem(new ItemStack[] { adding });
                                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
                            } else {
                                //Messages.local.sendTranslation(p, Slimefun.getPrefix(true), "machines.full-inventory");
                                SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
                            }  return;
                        }  }

                    b1++; }

                //Messages.local.sendTranslation(p, Slimefun.getPrefix(true), "machines.unknown-material");
                SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
            }


            if (b.getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE && b.getRelative(BlockFace.DOWN).getType() == Material.DISPENSER && b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN).getType() == Material.BEACON) {
                if (b.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getType() == Material.IRON_BLOCK && b.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getType() == Material.IRON_BLOCK) {
                    if (Slimefun.hasUnlocked(p, new CustomItem(Material.BEACON, "&9&lMass Fabricator","&a&oGenerates UU-Matter"), true)) {
                        e.setCancelled(true);
                        Dispenser d = (Dispenser)b.getRelative(BlockFace.DOWN).getState();
                        final Inventory inv = d.getInventory();
                        List<ItemStack[]> recipes = Setup.mfMachine.getRecipes();



                        List<ItemStack> convertable = new ArrayList();
                        for (int i = 0; i < recipes.size(); i++) {
                            if (i % 2 == 0)
                                convertable.add(((ItemStack[])recipes.get(i))[0]);
                        }  byte b1; int j;
                        ItemStack[] arrayOfItemStack;
                        for (j = (arrayOfItemStack = inv.getContents()).length, b1 = 0; b1 < j; ) { ItemStack current = arrayOfItemStack[b1];
                            for (Iterator<ItemStack> localIterator1 = convertable.iterator(); localIterator1.hasNext(); ) { ItemStack convert = localIterator1.next();
                                if (current != null &&
                                        SlimefunManager.isItemSimilar(current, convert, true)) {
                                    List<ItemStack> newRecipes = new ArrayList();
                                    for (ItemStack[] recipe : recipes) {
                                        newRecipes.add(recipe[0]);
                                    }
                                    ItemStack adding = newRecipes.get(newRecipes.indexOf(convert) + 1);
                                    if (InvUtils.fits(inv, adding)) {
                                        Object removing = current.clone();
                                        ((ItemStack)removing).setAmount(1);
                                        inv.removeItem(new ItemStack[] { (ItemStack)removing });
                                        adding.setAmount(3);
                                        inv.addItem(new ItemStack[] { adding });
                                        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                                    } else {
                                        //Messages.local.sendTranslation(p, Slimefun.getPrefix(true), "machines.full-inventory");
                                        SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
                                    }  return;
                                }  }
                            b1++; }

                        //Messages.local.sendTranslation(p, Slimefun.getPrefix(true), "machines.unknown-material");
                        SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
                    }
                } else if (b.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getType() == Material.IRON_BLOCK && b.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getType() == Material.IRON_BLOCK && Slimefun.hasUnlocked(p, new CustomItem(Material.BEACON, "&9&lMass Fabricator", "&a&oGenerates UU-Matter"), true)) {
                    Furnace f = (Furnace)b.getRelative(BlockFace.DOWN).getState();
                    final FurnaceInventory inv = f.getInventory();
                    if (inv.getFuel().getType() == Material.DIAMOND && inv.getFuel().getItemMeta().getDisplayName().equalsIgnoreCase("&c&lPower Crystal")) {
                        if (inv.getSmelting() == null || inv.getSmelting().getItemMeta().getDisplayName().equalsIgnoreCase("&dUU-Matter")) {
                            if (inv.getFuel().getAmount() > 1) {
                                inv.getFuel().setAmount(inv.getFuel().getAmount() - 1);
                            } else {
                                inv.setFuel(null);
                            }
                            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable()
                            {
                                public void run() {
                                    if (inv.getSmelting() == null) {
                                        CustomItem customItem = new CustomItem(Material.INK_SAC, "&dUU-Matter"); // TODO: RECEIVE 3
                                        inv.setSmelting((ItemStack)customItem);
                                    } else {
                                        inv.getSmelting().setAmount(inv.getSmelting().getAmount() + 3);
                                    }
                                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                }
                                //}100L);
                            });
                        } else {
                            //Messages.local.sendTranslation(p, Slimefun.getPrefix(true), "machines.full-inventory");
                            SlimefunPlugin.getLocal().sendMessage(p, "machines.full-inventory", true);
                        }
                    } else {
                        //Messages.local.sendTranslation(p, Slimefun.getPrefix(true), "machines.unknown-material");
                        SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
                    }
                }
            }


            if (b.getType() == Material.WHITE_STAINED_GLASS) {
                Block down = b.getRelative(BlockFace.DOWN);
                if (down.getType() == Material.DISPENSER && down.getData() == 1 && Slimefun.hasUnlocked(p, new CustomItem(Material.WHITE_STAINED_GLASS, "&6Tank", "", "&a&oStores Liquids"), true)) {
                    e.setCancelled(true);
                    ItemStack HandItem = p.getInventory().getItemInMainHand();
                    Dispenser dispenser = (Dispenser)down.getState();
                    if (HandItem.getType() == Material.LAVA_BUCKET || HandItem.getType() == Material.WATER_BUCKET) {
                        if (HandItem.getType() == Material.LAVA_BUCKET) {
                            if (b.getData() == 1 || b.getData() == 0) {
                                dispenser.getInventory().addItem(new CustomItem(Material.TERRACOTTA, "&6Lava"));
                                dispenser.update();
                                //b.setData((byte)1);
                                //b.setMetadata().setData((byte)0);
                                b.getLocation().getWorld().playSound(b.getLocation(), Sound.BLOCK_LAVA_AMBIENT, 1.0F, 0.0F);
                            } else {
                                p.sendMessage("&a&lSlimefun &7> &cThis Tank already has Lava in it, don't mix it!");
                                return;
                            }
                        }
                        if (HandItem.getType() == Material.WATER_BUCKET) {
                            if (b.getData() == 3 || b.getData() == 0) {
                                dispenser.getInventory().addItem(new CustomItem(Material.TERRACOTTA, "&bWater"));
                                dispenser.update();
                                //b.setData((byte)3);
                                //b.setMetadata().setData((byte)3);
                                b.getLocation().getWorld().playSound(b.getLocation(), Sound.BLOCK_WATER_AMBIENT, 1.0F, 0.0F);
                            } else {
                                p.sendMessage("&a&lSlimefun &7> &cThis Tank already has Water in it, don't mix it!");
                                SlimefunPlugin.getLocal().sendMessage(p, "machines.unknown-material", true);
                                return;
                            }
                        }
                        HandItem.setType(Material.BUCKET);
                        return;
                    }
                    if (HandItem.getType() == Material.BUCKET) {
                        if (b.getData() == 1 && down.getData() == 1) {
                            p.getInventory().getItemInMainHand().setType(Material.LAVA_BUCKET);
                            dispenser.getInventory().removeItem(new CustomItem(Material.TERRACOTTA, "&6Lava"));
                            dispenser.update();
                            if (isDispenserEmpty(dispenser)) {
                                //b.setData((byte)0);
                                //b.setMetadata().setData((byte)0);
                            }
                        }
                        if (b.getData() == 3 && down.getData() == 1) {
                            p.getInventory().getItemInMainHand().setType(Material.WATER_BUCKET);
                            dispenser.getInventory().removeItem(new CustomItem(Material.TERRACOTTA, "&bWater"));
                            dispenser.update();
                            if (isDispenserEmpty(dispenser)) {
                                //b.setData((byte)0);
                                //b.setMetadata().setData((byte)0);
                            }
                        }
                    }
                    int i = 0; byte b1; int j; ItemStack[] arrayOfItemStack;
                    for (j = (arrayOfItemStack = dispenser.getInventory().getContents()).length, b1 = 0; b1 < j; ) { ItemStack is = arrayOfItemStack[b1];
                        if (is != null && is.getType() != Material.AIR) {
                            i += is.getAmount();
                        }
                        b1++; }

                    p.sendMessage("&a&lSlimefun &7> &eTank capacity: &6" + String.valueOf(i) + "&e/&6576 &eBuckets");
                }
            }
        }

        e.setCancelled(true);
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HORSE_ARMOR && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("&bMining Laser") && Slimefun.hasUnlocked(p, new CustomItem(Material.DIAMOND_HORSE_ARMOR, "&bMining Laser", "&6Mode: &1Mining", "&7Charge: &b0.0 J", "&7Capacity: &b40.0 J", "&a&oRight click to shoot, left click to change mode"), true)) {
            List<String> lore = p.getInventory().getItemInMainHand().getItemMeta().getLore();
            double charge = Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue();
            if (charge <= 0.0) {
                return;
            }
            if (((String)lore.get(0)).equalsIgnoreCase("&6Mode: &1Mining") && charge - this.plugin.getConfig().getInt("MiningLaser.MiningCharge") < 0.0) {
                return;
            }
            if (((String)lore.get(0)).equalsIgnoreCase("&6Mode: &1Explosive") && charge - this.plugin.getConfig().getInt("MiningLaser.ExplosiveCharge") < 0.0) {
                return;
            }
            if (((String)lore.get(0)).equalsIgnoreCase("&6Mode: &1SuperHeat") && charge - this.plugin.getConfig().getInt("MiningLaser.SuperHeatCharge") < 0.0) {
                return;
            }
            Snowball snowball = (Snowball)p.launchProjectile(Snowball.class);
            snowball.setShooter((LivingEntity)p);
            snowball.setVelocity(snowball.getVelocity().multiply(3));
            if (((String)lore.get(0)).equalsIgnoreCase("&6Mode: &1Mining")) {
                charge = Double.valueOf((new DecimalFormat("##.##")).format(charge - this.plugin.getConfig().getInt("MiningLaser.MiningCharge")).replace(",", ".")).doubleValue();
            }
            if (((String)lore.get(0)).equalsIgnoreCase("&6Mode: &1Explosive")) {
                charge = Double.valueOf((new DecimalFormat("##.##")).format(charge - this.plugin.getConfig().getInt("MiningLaser.ExplosiveCharge")).replace(",", ".")).doubleValue();
            }
            if (((String)lore.get(0)).equalsIgnoreCase("&6Mode: &1SuperHeat")) {
                charge = Double.valueOf((new DecimalFormat("##.##")).format(charge - this.plugin.getConfig().getInt("MiningLaser.SuperHeatCharge")).replace(",", ".")).doubleValue();
            }
            lore.set(1, "&7Charge: &b" + String.valueOf(charge) + " J");
            ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
            im.setLore(lore);
            p.getInventory().getItemInMainHand().setItemMeta(im);
        }

        e.setCancelled(true);
        if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && p.getInventory().getItemInMainHand().getType() == Material.DIAMOND_HORSE_ARMOR && p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("&bMining Laser") && Slimefun.hasUnlocked(p, new CustomItem(Material.DIAMOND_HORSE_ARMOR, "&bMining Laser", "&6Mode: &1Mining", "&7Charge: &b0.0 J", "&7Capacity: &b40.0 J", "&a&oRight click to shoot, left click to change mode" ), true)) {
            ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
            List<String> lore = im.getLore();
            if (((String)lore.get(0)).equalsIgnoreCase("&6Mode: &1Mining")) {
                lore.set(0, "&6Mode: &1Explosive");
                im.setLore(lore);
                p.getInventory().getItemInMainHand().setItemMeta(im);
                return;
            }
            if (((String)lore.get(0)).equalsIgnoreCase("&6Mode: &1Explosive")) {
                lore.set(0, "&6Mode: &1SuperHeat");
                im.setLore(lore);
                p.getInventory().getItemInMainHand().setItemMeta(im);
                return;
            }
            if (((String)lore.get(0)).equalsIgnoreCase("&6Mode: &1SuperHeat")) {
                lore.set(0, "&6Mode: &1Mining");
                im.setLore(lore);
                p.getInventory().getItemInMainHand().setItemMeta(im);
                return;
            }
        }
    } public static boolean isDispenserEmpty(Dispenser d) {
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = d.getInventory().getContents()).length, b = 0; b < i; ) { ItemStack item = arrayOfItemStack[b];
            if (item != null)
                return false;
            b++; }

        return true;
    }
}