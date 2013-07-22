package me.dpohvar.varscript;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.command.CommandCommandVS;
import me.dpohvar.varscript.command.CommandRunVS;
import me.dpohvar.varscript.command.CommandTagVS;
import me.dpohvar.varscript.command.CommandTask;
import me.dpohvar.varscript.config.ConfigKey;
import me.dpohvar.varscript.config.ConfigManager;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Program;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.Bukkit;
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
        getServer().getPluginCommand("task").setExecutor(new CommandTask(runtime));
        getServer().getPluginCommand("vs>").setExecutor(new CommandRunVS(runtime));
        getServer().getPluginCommand("vs>tag").setExecutor(new CommandTagVS());
        getServer().getPluginCommand("vs>cmd").setExecutor(new CommandCommandVS());
    }
































    public static void main(String[] args) throws IOException {

        Runtime runtime = new Runtime(null);
        Caller caller = Caller.getCallerFor(System.out);

        try{
            NamedCommandList cmd;
            FileInputStream fis = new FileInputStream("function.txt");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int t;
            while( (t = fis.read()) != -1 ) bos.write(t);
            cmd = VSCompiler.compile(new String(bos.toByteArray(),UTF8));
            Program program = new Program(runtime,caller);
            Function fun = cmd.build(program.getScope());
            Thread thread = new Thread(program);
            thread.push("STRING1").push("string2").push("string3").push("string4").push("string5");
            thread.pushFunction(fun,null);
            ThreadRunner runner = new ThreadRunner(thread);
            runner.runThreads();
            System.out.println("Bytecode is:");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            fun.save(out);
            byte[] bytes = out.toByteArray();
            for(byte b:bytes) {
                System.out.print("0x");
                System.out.print(Integer.toHexString(b));
                System.out.print(',');
            }
        } catch (Exception ex){
            caller.handleException(ex);
        }
        System.out.println("<END>");
    }

    public boolean isDebug() {
        return configManager.<Boolean>get(ConfigKey.DEBUG);
    }
}
