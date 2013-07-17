package me.dpohvar.varscript;

import me.dpohvar.varscript.utils.ReflectClass;
import me.dpohvar.varscript.utils.ScriptManager;
import me.dpohvar.varscript.vs.VSFieldable;
import me.dpohvar.varscript.vs.VSRunnable;
import me.dpohvar.varscript.vs.VSSimpleScope;
import me.dpohvar.varscript.vs.converter.Converter;
import me.dpohvar.varscript.vs.converter.rule.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 21.02.13 23:05
 * @author DPOH-VAR
 */
public class Runtime implements VSFieldable {
    public final Converter converter = new Converter();
    public final VarScript plugin;
    private ArrayList<Program> programs = new ArrayList<Program>();
    public final ScriptManager scriptManager;
    private final VSSimpleScope scope = new VSSimpleScope(){{
        defineConst("Server",Bukkit.getServer());
        defineConst("Runtime",this);
        defineConst("VarScript",VarScript.instance);
        defineConst("PluginManager",Bukkit.getPluginManager());
        for(Plugin plugin:Bukkit.getPluginManager().getPlugins()){
            defineConst(plugin.getName(),plugin);
        }
    }};

    public VSSimpleScope getScope(){
        return scope;
    }

    public void registerProgram(Program program){
        if(program.getPID()!= -1) throw new RuntimeException("program already registered with PID="+program.getPID());
        programs.add(program);
        program.setPID(programs.size()-1);
    }

    public Runtime(VarScript plugin){
        this.plugin = plugin;
        this.scriptManager = new ScriptManager(plugin!=null?new File(plugin.getDataFolder(),"scripts"):new File("scripts"));
        converter.addRule(new RuleBoolean());
        RuleByte ruleByte = new RuleByte();
        converter.addRule(ruleByte);
        converter.addRule(new RuleBytes(ruleByte));
        converter.addRule(new RuleCharacter());
        converter.addRule(new RuleDouble());
        converter.addRule(new RuleEntity());
        converter.addRule(new RuleFieldable());
        converter.addRule(new RuleFloat());
        converter.addRule(new RuleInteger());
        converter.addRule(new RuleList());
        converter.addRule(new RuleLocation());
        converter.addRule(new RuleLong());
        converter.addRule(new RuleShort());
        converter.addRule(new RuleString());
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

    @Override
    public VSRunnable getConstructor() {
        try {
            return new ReflectClass(Runtime.class.getConstructor(VarScript.class),scope);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public VSFieldable getProto() {
        return null;
    }

    @Override
    public void setProto(VSFieldable proto) {
    }
}
