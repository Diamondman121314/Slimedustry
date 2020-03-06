package me.diamondman121314.Slimedustry;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Slimedustry extends JavaPlugin implements Listener, SlimefunAddon {
  public void onEnable() {
    if (getServer().getPluginManager().isPluginEnabled("Slimefun")) {
      System.out.println("[" + getName() + "] " + getName() + " v" + getDescription().getVersion() + " has been enabled!");
    } else {
      System.err.println("[" + getName() + "] Slimefun not found!");
      System.err.println("Please install Slimefun");
      System.err.println("Without it, this Plugin will not work");
      System.err.println("You can download it here:");
      System.err.println("http://dev.bukkit.org/bukkit-plugins/slimefun");
      getServer().getPluginManager().disablePlugin(this);
      return;
    }
    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(this, this);
    getCommand("Slimedustry").setExecutor(new Commands(this));

    getConfig().options().copyDefaults(true);
    saveConfig();
    Setup.setupMisc(this);
    Setup.setupItems(this);
    Setup.setupResearches(this);
    Setup.changeRecipes();
    Setup.changeCategories();
  }

  @Override
  public JavaPlugin getJavaPlugin() {
    return null;
  }

  @Override
  public String getBugTrackerURL() {
    return null;
  }
}
