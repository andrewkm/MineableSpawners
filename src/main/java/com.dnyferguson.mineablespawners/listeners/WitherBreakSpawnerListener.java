package com.dnyferguson.mineablespawners.listeners;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import com.dnyferguson.mineablespawners.utils.Chat;
import com.dnyferguson.mineablespawners.utils.SpawnerUtils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WitherBreakSpawnerListener implements Listener {
    private MineableSpawners plugin;

    public WitherBreakSpawnerListener(MineableSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onWitherBlockChange(EntityChangeBlockEvent event) {
        if (!event.getEntity().getType().equals(EntityType.WITHER)) {
            return;
        }

        if (event.getBlock().getLocation().getWorld() != null && plugin.getConfigurationHandler().getList("wither", "blacklisted-worlds").contains(event.getBlock().getWorld().getName())) {
            return;
        }

        if (!plugin.getConfigurationHandler().getBooleanOrDefault("wither", "drop", false)) {
            return;
        }

        Block block = event.getBlock();

        if (!block.getType().equals(XMaterial.SPAWNER.parseMaterial())) {
            return;
        }

        double dropChance = plugin.getConfigurationHandler().getDoubleOrDefault("wither", "chance", 100)/100;

        if (dropChance != 1) {
            double random = Math.random();
            if (random >= dropChance) {
                return;
            }
        }

        CreatureSpawner spawner = (CreatureSpawner) block.getState();

        ItemStack item = SpawnerUtils.generateSpawnerItem(spawner.getSpawnedType(), 1);
        if (block.getLocation().getWorld() != null) {
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), item);
        }
    }
}
