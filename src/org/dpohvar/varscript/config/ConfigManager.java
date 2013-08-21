package org.dpohvar.varscript.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 12:13
 */
public class ConfigManager {

    public final Plugin plugin;
    FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    public <T> T get(ConfigKey key) {
        switch (key.configType) {
            case BOOLEAN:
                return (T) (Boolean) config.getBoolean(key.configPath);
            case BOOLEANLIST:
                return (T) config.getBooleanList(key.configPath);
            case BYTELIST:
                return (T) config.getByteList(key.configPath);
            case CHARACTERLIST:
                return (T) config.getCharacterList(key.configPath);
            case COLOR:
                return (T) config.getColor(key.configPath);
            case DOUBLE:
                return (T) (Double) config.getDouble(key.configPath);
            case DOUBLELIST:
                return (T) config.getDoubleList(key.configPath);
            case FLOATLIST:
                return (T) config.getFloatList(key.configPath);
            case INT:
                return (T) (Integer) config.getInt(key.configPath);
            case INTEGERLIST:
                return (T) config.getIntegerList(key.configPath);
            case ITEMSTACK:
                return (T) config.getItemStack(key.configPath);
            case LONG:
                return (T) (Long) config.getLong(key.configPath);
            case LONGLIST:
                return (T) config.getLongList(key.configPath);
            case MAPLIST:
                return (T) config.getMapList(key.configPath);
            case SHORTLIST:
                return (T) config.getShortList(key.configPath);
            case STRING:
                return (T) config.getString(key.configPath);
            case STRINGLIST:
                return (T) config.getStringList(key.configPath);
            case OFFLINEPLAYER:
                return (T) config.getOfflinePlayer(key.configPath);
            case VECTOR:
                return (T) config.getVector(key.configPath);
            default:
                return null;
        }
    }

    public <T> void set(ConfigKey key, T value) {
        config.set(key.configPath, value);
        plugin.saveConfig();
    }


}

