package com.dnyferguson.mineablespawners.utils;

import com.cryptomorin.xseries.XMaterial;
import com.dnyferguson.mineablespawners.MineableSpawners;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpawnerUtils {

    public static ItemStack generateSpawnerItem(EntityType entityType, int amount) {

        ItemStack item = new ItemStack(XMaterial.SPAWNER.parseMaterial());
        ItemMeta meta = item.getItemMeta();
        item.setAmount(amount);

        String mobFormatted = Chat.uppercaseStartingLetters(entityType.name());
        meta.setDisplayName(Chat.format(MineableSpawners.getInstance().getConfigurationHandler().getMessage("global", "name").replace("%mob%", mobFormatted)));
        List<String> newLore = new ArrayList<>();
        if (MineableSpawners.getInstance().getConfigurationHandler().getList("global", "lore") != null && MineableSpawners.getInstance().getConfigurationHandler().getBoolean("global", "lore-enabled")) {
            for (String line : MineableSpawners.getInstance().getConfigurationHandler().getList("global", "lore")) {
                newLore.add(Chat.format(line).replace("%mob%", mobFormatted));
            }
            meta.setLore(newLore);
        }

        item.setItemMeta(meta);

        NBTItem nbti = new NBTItem(item);
        nbti.setString("ms_mob", entityType.name());

        return nbti.getItem();
    }
}
