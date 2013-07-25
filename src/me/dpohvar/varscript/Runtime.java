package me.dpohvar.varscript;

import me.dpohvar.varscript.scheduler.Scheduler;
import me.dpohvar.varscript.utils.reflect.ReflectClass;
import me.dpohvar.varscript.utils.ScriptManager;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.converter.rule.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 21.02.13 23:05
 * @author DPOH-VAR
 */
public class Runtime implements Fieldable,Scope {
    public final Converter converter = new Converter();
    public final VarScript plugin;
    private ArrayList<Program> programs = new ArrayList<Program>();
    public final ScriptManager scriptManager;
    public final Scheduler scheduler;

    public void registerProgram(Program program){
        if(program.getPID()!= -1) throw new RuntimeException("program already registered with PID="+program.getPID());
        programs.add(program);
        program.setPID(programs.size()-1);
    }

    public Runtime(VarScript plugin){
        this.plugin = plugin;
        plugin.runtime = this;
        this.scriptManager = new ScriptManager(plugin.getScriptHome());
        converter.addRule(new RuleBoolean());
        RuleByte ruleByte = new RuleByte();
        converter.addRule(ruleByte);
        converter.addRule(new RuleBytes(ruleByte));
        converter.addRule(new RuleCharacter());
        converter.addRule(new RuleClass());
        converter.addRule(new RuleCollection());
        converter.addRule(new RuleDouble());
        converter.addRule(new RuleEntity());
        converter.addRule(new RuleFieldable());
        converter.addRule(new RuleFloat());
        converter.addRule(new RuleInteger());
        converter.addRule(new RuleInventory());
        converter.addRule(new RuleIterable());
        converter.addRule(new RuleList());
        converter.addRule(new RuleLocation());
        converter.addRule(new RuleLong());
        converter.addRule(new RuleNBTContainer());
        converter.addRule(new RuleNBTTag());
        converter.addRule(new RuleShort());
        converter.addRule(new RuleString());


        final Runtime runtime = this;
        scheduler = new Scheduler(this,plugin.getSchedulerHome());
        PluginManager pm = Bukkit.getPluginManager();
        defineConst("Server", Bukkit.getServer());
        defineConst("Runtime", runtime);
        defineConst("VarScript", VarScript.instance);
        defineConst("PluginManager", pm);
        defineConst("Scheduler", scheduler);
        for(Plugin p:pm.getPlugins()){
            defineConst(p.getName(), plugin);
        }

        VSCompiler.init(converter);

        scheduler.loadTasks("");
    }

    public Program getProgram(int pid){
        return programs.get(pid);
    }


    public void removeProgram(int id){
        Program p = programs.get(id);
        if(p!=null && p.isFinished()){
            programs.set(id,null);
        }
    }
    HashMap<String,Object> variables = new HashMap<String, Object>();


    HashMap<String,Object> fields = new HashMap<String, Object>();
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

    HashMap<String,Object> constants = new HashMap<String, Object>();

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
