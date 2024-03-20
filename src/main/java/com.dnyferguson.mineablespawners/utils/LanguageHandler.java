package com.dnyferguson.mineablespawners.utils;

import com.dnyferguson.mineablespawners.MineableSpawners;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LanguageHandler {

    private static File languageConfigFile;
    private static FileConfiguration languageConfig;
    private static HashMap<EntityType, String> translations = new HashMap<>();

    public static String getEntityTranslation(EntityType entityType) {
        if (languageConfigFile == null || languageConfig == null) {
            loadConfig();
        }
        return translations.getOrDefault(entityType, "Unknown");
    }

    private static void loadConfig() {
        FileConfiguration config = MineableSpawners.getInstance().getConfig();
        String selectedLanguage = config.getString("global.spawner-entity-lang", "en_US");
        languageConfigFile = new File(MineableSpawners.getInstance().getDataFolder(), "translation/entitytypes_" + selectedLanguage + "_.yml");
        languageConfig = new YamlConfiguration();
        try {
            languageConfig.load(languageConfigFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Load the translations. Map is "translation"
        ConfigurationSection translationSection = languageConfig.getConfigurationSection("translation");
        for (String key : translationSection.getKeys(false)) {
            EntityType entityType = EntityType.valueOf(key);
            String translation = translationSection.getString(key);
            translations.put(entityType, translation);
        }
    }

    public static void reloadConfig() {
        translations.clear();
        loadConfig();
    }
}
