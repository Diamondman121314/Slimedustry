package me.diamondman121314.Slimedustry;

import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.MultiBlockMachine;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MassFabricator extends MultiBlockMachine {
    public MassFabricator(Category category, SlimefunItemStack item, ItemStack[] recipe, ItemStack[] machineRecipes, BlockFace trigger) {
        super(category, item, recipe, machineRecipes, trigger);
    }

    @Override
    public void onInteract(Player player, Block block) {

    }

    @Override
    public void onPlace(Player p, Block b) {

    }

    @Override
    public boolean onBreak(Player p, Block b) {
        return false;
    }

    @Override
    public boolean onExplode(Block b) {
        return false;
    }

    @Override
    public String getLabelLocalPath() {
        return null;
    }

    @Override
    public String getRecipeSectionLabel(Player p) {
        return null;
    }
}
