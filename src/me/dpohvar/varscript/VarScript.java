package me.dpohvar.varscript;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.command.CommandCommandVS;
import me.dpohvar.varscript.command.CommandRunVS;
import me.dpohvar.varscript.command.CommandTagVS;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.Charset;

/**
 * DPOH-VAR(c)
 * 05.01.13 16:54
 */
public class VarScript extends JavaPlugin {

    public static File scriptHome;
    public static String prefix_normal = ChatColor.translateAlternateColorCodes('&',"&3&l[&bvarscript&3&l]");
    public static String prefix_error = ChatColor.translateAlternateColorCodes('&',"&4&l[&cvarscript&4&l]");
    public static Charset UTF8 = Charset.forName("UTF8");
    public static VarScript instance; { instance = this; }
    public Runtime runtime;


    public void onEnable(){
        scriptHome = new File(this.getDataFolder(),"scripts");
        runtime = new Runtime(this);
        getServer().getPluginCommand("vs>").setExecutor(new CommandRunVS());
        getServer().getPluginCommand("vs>tag").setExecutor(new CommandTagVS());
        getServer().getPluginCommand("vs>cmd").setExecutor(new CommandCommandVS());
    }
































    public static void main(String[] args) throws IOException {

        Runtime runtime = new Runtime(null);
        Caller caller = Caller.getCallerFor(System.out);

        try{
            VSNamedCommandList cmd;
            FileInputStream fis = new FileInputStream("function.txt");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int t;
            while( (t = fis.read()) != -1 ) bos.write(t);
            cmd = VSCompiler.compile(new String(bos.toByteArray(),UTF8));
            VSProgram program = new VSProgram(runtime,caller);
            VSFunction fun = cmd.build(program.getScope());
            VSThread thread = new VSThread(program);
            thread.push("STRING1").push("string2").push("string3").push("string4").push("string5");
            thread.pushFunction(fun,null);
            VSThreadRunner runner = new VSThreadRunner(thread);
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
}
