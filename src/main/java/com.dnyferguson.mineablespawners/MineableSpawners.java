package com.dnyferguson.mineablespawners;

import com.dnyferguson.mineablespawners.api.API;
import com.dnyferguson.mineablespawners.commands.MineableSpawnersCommand;
import com.dnyferguson.mineablespawners.listeners.WitherBreakSpawnerListener;
import com.dnyferguson.mineablespawners.listeners.EggChangeListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerExplodeListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerMineListener;
import com.dnyferguson.mineablespawners.listeners.SpawnerPlaceListener;
import com.dnyferguson.mineablespawners.listeners.AnvilRenameListener;
import com.dnyferguson.mineablespawners.utils.ConfigurationHandler;
import com.dnyferguson.mineablespawners.utils.LanguageHandler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineableSpawners extends JavaPlugin {
    private ConfigurationHandler configurationHandler;
    private Economy econ;
    private static API api;
    private static MineableSpawners instance;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        configurationHandler = new ConfigurationHandler(this);
        LanguageHandler.loadConfig();

        if (!setupEconomy()) {
            getLogger().info("vault not found, economy features disabled.");
        }

        getCommand("mineablespawners").setExecutor(new MineableSpawnersCommand(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new SpawnerMineListener(this), this);
        pm.registerEvents(new SpawnerPlaceListener(this), this);
        pm.registerEvents(new EggChangeListener(this), this);
        pm.registerEvents(new AnvilRenameListener(this), this);
        pm.registerEvents(new SpawnerExplodeListener(this), this);
        pm.registerEvents(new WitherBreakSpawnerListener(this), this);

        if (getConfigurationHandler().getBoolean("global", "show-available")) {
            StringBuilder str = new StringBuilder("Available mob types: \n");
            for (EntityType type : EntityType.values()) {
                str.append("- ");
                str.append(type.name());
                str.append("\n");
            }
            getLogger().info(str.toString());
        }

        api = new API(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }

    public Economy getEcon() {
        return econ;
    }

    public static API getApi() {
        return api;
    }

    public static MineableSpawners getInstance() {
        return instance;
    }
}
