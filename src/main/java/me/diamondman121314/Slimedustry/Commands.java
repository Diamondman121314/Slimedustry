package me.diamondman121314.Slimedustry;

import java.util.List;

import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Commands implements CommandExecutor {
    private final Plugin plugin;

    public Commands(Slimedustry plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (args.length == 0) {
                p.sendMessage(ChatColors.color("&r\n&aSlimedustry &2v" + plugin.getDescription().getVersion() + "\n&r\n&3/sd reload &bReloads the config\n&3/sd charge {Player} &bFully charges the item in the main hand"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (!p.hasPermission("Slimedustry.reload")) {
                    p.sendMessage(ChatColor.DARK_RED + "You are not allowed to do this!");
                    return true;
                }
                this.plugin.getConfig().options().copyDefaults(true);
                this.plugin.reloadConfig();
                p.sendMessage(ChatColor.DARK_GREEN + "Config Reloaded!");
            }
            if (args[0].equalsIgnoreCase("charge")) {
                if (!p.hasPermission("Slimedustry.charge")) {
                    p.sendMessage(ChatColor.DARK_RED + "You are not allowed to do this!");
                    return true;
                }
                if (args.length == 1) {
                    p.sendMessage(ChatColor.RED + "Usage: /sd charge {Player}");
                    return true;
                }
                Player target = null;
                //byte b;
                //int i;
                //Player[] arrayOfPlayer;
                //for (i = (arrayOfPlayer = Bukkit.getServer().getOnlinePlayers()).length, b = 0; b < i; ) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    //Player online = arrayOfPlayer[b];
                    if (player.getName().equalsIgnoreCase(args[1])) {
                        target = Bukkit.getPlayer(args[1]);
                        //b++;
                    }
                }

                if (target == null) {
                    p.sendMessage(ChatColor.DARK_RED + "Player not found!");
                } else {
                    if (target.getInventory().getItemInMainHand() == null) {
                        return true;
                    }
                    if (target.getInventory().getItemInMainHand().getItemMeta().getLore() == null) {
                        return true;
                    }
                    List<String> lore = target.getInventory().getItemInMainHand().getItemMeta().getLore();
                    if (lore.size() < 3) {
                        return true;
                    }
                    if (((String)lore.get(1)).contains("Charge:") && ((String)lore.get(2)).contains("Capacity:")) {
                        double charge = Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue();
                        double capacity = Double.valueOf(((String)lore.get(2)).replace("Capacity: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue();
                        charge = capacity;
                        lore.set(1, "&7Charge: &b" + String.valueOf(charge) + " J");
                        ItemMeta im = target.getInventory().getItemInMainHand().getItemMeta();
                        im.setLore(lore);
                        target.getInventory().getItemInMainHand().setItemMeta(im);
                    }
                }
            }
        } else {

            ConsoleCommandSender c = (ConsoleCommandSender)sender;
            if (args.length == 0) {
                c.sendMessage(ChatColors.color("&r\n&aSlimedustry &2v" + plugin.getDescription().getVersion() + "\n&r\n&3/sd reload &bReloads the config\n&3/sd charge {Player} &bFully charges the item in the main hand"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                this.plugin.getConfig().options().copyDefaults(true);
                this.plugin.reloadConfig();
                c.sendMessage(ChatColor.GREEN + "Config Reloaded!");
            }
            if (args[0].equalsIgnoreCase("charge")) {
                if (args.length == 1) {
                    c.sendMessage(ChatColor.RED + "Usage: /sd charge {PlayerName}");
                    return true;
                }
                Player target = null;
                //byte b;
                //int i;
                //Player[] arrayOfPlayer;
                //for (i = (arrayOfPlayer = Bukkit.getServer().getOnlinePlayers()).length, b = 0; b < i; ) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    //Player online = arrayOfPlayer[b];
                    if (player.getName().equalsIgnoreCase(args[1])) {
                        target = Bukkit.getPlayer(args[1]);
                        //b++;
                    }
                }

                if (target == null) {
                    c.sendMessage(ChatColor.RED + "Player not found!");
                } else {
                    if (target.getInventory().getItemInMainHand() == null) {
                        return true;
                    }
                    if (target.getInventory().getItemInMainHand().getItemMeta().getLore() == null) {
                        return true;
                    }
                    List<String> lore = target.getInventory().getItemInMainHand().getItemMeta().getLore();
                    if (lore.size() < 3) {
                        return true;
                    }
                    if (((String)lore.get(1)).contains("Charge:") && ((String)lore.get(2)).contains("Capacity:")) {
                        double charge = Double.valueOf(((String)lore.get(1)).replace("Charge: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue();
                        double capacity = Double.valueOf(((String)lore.get(2)).replace("Capacity: ", "").replace(" J", "").replace("&7", "").replace("&b", "")).doubleValue();
                        charge = capacity;
                        lore.set(1, "&7Charge: &b" + String.valueOf(charge) + " J");
                        ItemMeta im = target.getInventory().getItemInMainHand().getItemMeta();
                        im.setLore(lore);
                        target.getInventory().getItemInMainHand().setItemMeta(im);
                    }
                }
            }
        }
        return true;
    }
}
