package me.dpohvar.varscript;

import com.google.common.io.Files;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.converter.rule.*;
import me.dpohvar.varscript.scheduler.Scheduler;
import me.dpohvar.varscript.utils.ScriptManager;
import me.dpohvar.varscript.utils.reflect.ReflectClass;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.script.ScriptEngineManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 21.02.13 23:05
 * @author DPOH-VAR
 */
public class Runtime implements Fieldable,Scope {
    public final Converter converter = new Converter();
    public final VarScript plugin;
    private HashMap<Integer,Program> programs = new HashMap<Integer,Program>();
    private int freeId = 0;
    public final ScriptManager scriptManager;
    private Scheduler scheduler;
    private HashMap<String,Object> constants;

    private final String folder_autorun = "autorun";
    private final String folder_module = "modules";


    public final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    public Scheduler getScheduler(){
        return scheduler;
    }

    public void registerProgram(Program program){
        if(program.getPID()!= -1) throw new RuntimeException("program already registered with PID="+program.getPID());
        programs.put(freeId,program);
        program.setPID(freeId++);
    }

    HashMap<String,Object> fields = new HashMap<String, Object>();


    public void disable() {
        for(Program program:new ArrayList<Program>(programs.values())){
            program.setFinished();
        }
    }

    public Runtime(VarScript plugin){
        this.plugin = plugin;
        plugin.runtime = this;
        this.scriptManager = new ScriptManager(plugin.getScriptHome());
        PluginManager pm = Bukkit.getPluginManager();

        converter.addRule(new RuleBlock());
        converter.addRule(new RuleBoolean());
        RuleByte ruleByte = new RuleByte();
        converter.addRule(ruleByte);
        converter.addRule(new RuleBytes(ruleByte));
        converter.addRule(new RuleCharacter());
        converter.addRule(new RuleCaller());
        converter.addRule(new RuleClass());
        converter.addRule(new RuleCollection());
        converter.addRule(new RuleCommandList());
        converter.addRule(new RuleDouble());
        converter.addRule(new RuleEntity());
        converter.addRule(new RuleFieldable());
        converter.addRule(new RuleFloat());
        converter.addRule(new RuleInteger());
        converter.addRule(new RuleInventory());
        converter.addRule(new RuleItemStack());
        converter.addRule(new RuleIterable());
        converter.addRule(new RuleList());
        converter.addRule(new RuleLocation());
        converter.addRule(new RuleLong());
        converter.addRule(new RuleMap());
        converter.addRule(new RuleOfflinePlayer());
        converter.addRule(new RuleScoreboard());
        converter.addRule(new RuleShort());
        converter.addRule(new RuleString());
        converter.addRule(new RuleVector());
        converter.addRule(new RuleWorld());
        if(pm.getPlugin("PowerNBT")!=null) {
            converter.addRule(new RuleMapNBT());
            converter.addRule(new RuleNBTContainer());
            converter.addRule(new RuleNBTTag());
        }

        VSCompiler.init(converter);

        load();

    }


    public void load(){
        PluginManager pm = Bukkit.getPluginManager();

        final Runtime runtime = this;
        if(scheduler!=null) scheduler.disable();
        scheduler = new Scheduler(this,plugin.getSchedulerHome());

        constants = new HashMap<String, Object>();
        fields = new HashMap<String, Object>();

        defineConst("Server", Bukkit.getServer());
        defineConst("Runtime", runtime);
        defineConst("VarScript", VarScript.instance);
        defineConst("PluginManager", pm);
        defineConst("Scheduler", scheduler);
        for(Plugin p:pm.getPlugins()){
            defineConst(p.getName(), p);
        }

        Map<String,File> files;
        files = scriptManager.getModuleFiles("vs", folder_autorun);
        if(files != null) for(File file: files.values()){
            try {
                String script = new String(
                        IOUtils.toByteArray(file.toURI()),
                        VarScript.UTF8
                );
                Caller caller = Caller.getCallerFor(this);
                VarscriptProgram program = new VarscriptProgram(this,caller);
                Thread thread = new Thread(program);
                Function function = VSCompiler.compile(script).build(program.getScope());
                thread.pushFunction(function, program);
                new ThreadRunner(thread).runThreads();
            } catch (Exception ignored) {
            }
        }
        files = scriptManager.getModuleFiles("vsbin", folder_autorun);
        if(files != null ) for(File file: files.values()){
            try {
                byte[] bytes = IOUtils.toByteArray(file.toURI());
                Caller caller = Caller.getCallerFor(this);
                VarscriptProgram program = new VarscriptProgram(this,caller);
                Thread thread = new Thread(program);
                Function function = VSCompiler.read(new ByteArrayInputStream(bytes)).build(program.getScope());
                thread.pushFunction(function, program);
                new ThreadRunner(thread).runThreads();
            } catch (Exception ignored) {
            }
        }
        files = scriptManager.getModuleFiles("vs", folder_module);
        if(files != null ) for(Map.Entry<String,File> e: files.entrySet()){
            try {
                String name = e.getKey();
                if(!name.matches("[A-Za-z0-9_\\-]+")) {
                    Bukkit.getLogger().warning(
                            "varsript module " + name + " has incorrect name"
                    );
                    continue;
                }
                File file = e.getValue();
                String script = new String(
                        IOUtils.toByteArray(file.toURI()),
                        VarScript.UTF8
                );
                Caller caller = Caller.getCallerFor(this);
                VarscriptProgram program = new VarscriptProgram(this,caller);
                Thread thread = new Thread(program);
                Function function = VSCompiler.compile(script,name).build(program.getScope());
                Context context = thread.pushFunction(function);
                defineConst(name, context);
                new ThreadRunner(thread).runThreads();
            } catch (Exception ignored) {
            }
        }
        files = scriptManager.getModuleFiles("vsbin", folder_module);
        if(files != null ) for(Map.Entry<String,File> e: files.entrySet()){
            try {
                String name = e.getKey();
                if(!name.matches("[A-Za-z0-9_\\-]+")) {
                    Bukkit.getLogger().warning(
                            "varsript binary module " + name + " has incorrect name"
                    );
                    continue;
                }
                File file = e.getValue();
                byte[] bytes = IOUtils.toByteArray(file.toURI());
                Caller caller = Caller.getCallerFor(this);
                VarscriptProgram program = new VarscriptProgram(this,caller);
                Thread thread = new Thread(program);
                Function function = VSCompiler.read(new ByteArrayInputStream(bytes)).build(program.getScope());
                if(!function.getName().equals(name)) {
                    Bukkit.getLogger().warning(
                            "varsript binary module " + name + " has incorrect inner name"
                    );
                    continue;
                }
                Context context = thread.pushFunction(function);
                defineConst(name, context);
                new ThreadRunner(thread).runThreads();
            } catch (Exception ignored) {
            }
        }

        scheduler.loadTasks("");
    }
    public Program getProgram(int pid){
        return programs.get(pid);
    }

    public void removeProgram(int id){
        Program p = programs.get(id);
        if(p!=null && p.isFinished()){
            programs.remove(id);
        }
    }

    @Override
    public Set<String> getAllFields() {
        return fields.keySet();
    }

    @Override
    public Object getField(String key) {
        return fields.get(key);
    }

    @Override
    public void setField(String key, Object value) {
        fields.put(key,value);
    }

    @Override
    public void removeField(String key) {
        fields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        return fields.containsKey(key);
    }

    @Override public me.dpohvar.varscript.vs.Runnable getConstructor() {
        try {
            return new ReflectClass(Runtime.class.getConstructor(VarScript.class),this);
        } catch (Exception e) {
            return null;
        }
    }

    @Override public Fieldable getProto() {
        return null;
    }

    @Override public void setProto(Fieldable proto) {
    }

    @Override public Object getVar(String varName) {
        if(constants.containsKey(varName)) return constants.get(varName);
        return fields.get(varName);
    }

    @Override public Runtime defineVar(String varName, Object value) {
        fields.put(varName,value);
        return this;
    }

    @Override public Runtime defineConst(String varName, Object value) {
        constants.put(varName,value);
        return this;
    }

    @Override public Runtime setVar(String varName, Object value) {
        return defineVar(varName,value);
    }

    @Override public Runtime delVar(String varName) {
        fields.remove(varName);
        return this;
    }
}
