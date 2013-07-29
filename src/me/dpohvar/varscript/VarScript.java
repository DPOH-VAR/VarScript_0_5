package me.dpohvar.varscript;

import me.dpohvar.varscript.command.*;
import me.dpohvar.varscript.config.ConfigKey;
import me.dpohvar.varscript.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.Charset;

/**
 * DPOH-VAR(c)
 * 05.01.13 16:54
 */
public class VarScript extends JavaPlugin {

    private File scriptHome;
    private File schedulerHome;
    public static String prefix_normal = ChatColor.translateAlternateColorCodes('&',"&3&l[&bvarscript&3&l]");
    public static String prefix_error = ChatColor.translateAlternateColorCodes('&',"&4&l[&cvarscript&4&l]");
    public static Charset UTF8 = Charset.forName("UTF8");
    public static VarScript instance;
    public Runtime runtime;
    private ConfigManager configManager;

    public VarScript(){
        instance = this;
    }

    public ConfigManager getConfigManager(){
        return configManager;
    }

    public File getScriptHome(){
        return scriptHome;
    }
    public File getSchedulerHome(){
        return schedulerHome;
    }

    @Override public void onLoad(){
        instance = this;
        configManager = new ConfigManager(this);
        scriptHome = new File(this.getDataFolder(),"scripts");
        schedulerHome = new File(this.getDataFolder(),"tasks");
    }

    @Override public void onEnable(){
        runtime = new Runtime(this);
        getServer().getPluginCommand("scheduler").setExecutor(new CommandScheduler(runtime));
        getServer().getPluginCommand("task").setExecutor(new CommandTask(runtime));
        getServer().getPluginCommand("vs>").setExecutor(new CommandRunVS(runtime));
        getServer().getPluginCommand("vs>tag").setExecutor(new CommandTagVS());
        getServer().getPluginCommand("vs>cmd").setExecutor(new CommandCommandVS());
    }


    public boolean isDebug() {
        return configManager.<Boolean>get(ConfigKey.DEBUG);
    }
}
