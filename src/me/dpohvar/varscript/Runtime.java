package me.dpohvar.varscript;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.converter.rule.*;
import me.dpohvar.varscript.scheduler.Scheduler;
import me.dpohvar.varscript.se.SEFileProgram;
import me.dpohvar.varscript.utils.ScriptManager;
import me.dpohvar.varscript.utils.VarScriptIOUtils;
import me.dpohvar.varscript.utils.reflect.ReflectClass;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.*;

/**
 * 21.02.13 23:05
 *
 * @author DPOH-VAR
 */
public class Runtime implements Fieldable, Scope {

    public final Converter converter = new Converter();
    public final VarScript plugin;
    private HashMap<Integer, Program> programs = new HashMap<Integer, Program>();
    private int freeId = 0;
    public final ScriptManager scriptManager;
    private Scheduler scheduler;
    private HashMap<String, Object> constants;

    private it.sauronsoftware.cron4j.Scheduler cron;

    private final String folder_autorun = "autorun";
    private final String folder_module = "modules";

    private final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    Map<String, ScriptEngine> engines = new HashMap<String, ScriptEngine>();

    public ScriptEngine getScriptEngine(String name) {
        if (engines.containsKey(name)) return engines.get(name);
        ScriptEngine engine = scriptEngineManager.getEngineByName(name);
        engines.put(name, engine);
        engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);
        return engine;
    }


    private final Bindings bindings = new Bindings() {
        @Override
        public Object put(String name, Object value) {
            return get(name);
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> toMerge) {
        }

        @Override
        public boolean containsKey(Object key) {
            if (fields.containsKey(key)) return true;
            if (constants.containsKey(key)) return true;
            return false;
        }

        @Override
        public Object get(Object key) {
            if (key instanceof String) return getVar((String) key);
            else return null;
        }

        @Override
        public Object remove(Object key) {
            return get(key);
        }

        @Override
        public int size() {
            return -1;
        }

        @Override
        public boolean isEmpty() {
            return constants.isEmpty() && fields.isEmpty();
        }

        @Override
        public boolean containsValue(Object value) {
            return constants.containsValue(value) || fields.containsValue(value);
        }

        @Override
        public void clear() {
        }

        @Override
        public Set<String> keySet() {
            return fields.keySet();
        }

        @Override
        public Collection<Object> values() {
            return fields.values();
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return fields.entrySet();
        }
    };

    public Bindings getBindings() {
        return bindings;
    }

    public it.sauronsoftware.cron4j.Scheduler getCron() {
        return cron;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void registerProgram(Program program) {
        if (program.getPID() != -1)
            throw new RuntimeException("program already registered with PID=" + program.getPID());
        programs.put(freeId, program);
        program.setPID(freeId++);
    }

    HashMap<String, Object> fields = new HashMap<String, Object>();


    public void disable() {
        for (Program program : new ArrayList<Program>(programs.values())) {
            program.setFinished();
        }
    }

    public Runtime(VarScript plugin) {
        this.plugin = plugin;
        plugin.runtime = this;
        scheduler = new Scheduler(this, plugin.getSchedulerHome());
        this.scriptManager = new ScriptManager(plugin.getScriptHome());
        PluginManager pm = Bukkit.getPluginManager();

        converter.addRule(new RuleBoolean());
        converter.addRule(new RuleBlock());
        converter.addRule(new RuleBlockState());
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
        try {
            converter.addRule(new RuleScoreboard());
        } catch (NoClassDefFoundError ignored) {
        }
        converter.addRule(new RuleShort());
        converter.addRule(new RuleString());
        converter.addRule(new RuleVector());
        converter.addRule(new RuleWorld());
        if (pm.getPlugin("PowerNBT") != null) {
            converter.addRule(new RuleMapNBT());
            converter.addRule(new RuleNBTContainer());
            converter.addRule(new RuleNBTTag());
        }

        VSCompiler.init(converter);

        load();

    }


    public void load() {
        PluginManager pm = Bukkit.getPluginManager();

        final Runtime runtime = this;
        if (cron != null) cron.stop();
        cron = new it.sauronsoftware.cron4j.Scheduler();
        cron.start();

        constants = new HashMap<String, Object>();
        fields = new HashMap<String, Object>();

        defineConst("Server", Bukkit.getServer());
        defineConst("Runtime", runtime);
        defineConst("PluginManager", pm);
        defineConst("Scheduler", scheduler);
        for (Plugin p : pm.getPlugins()) {
            defineConst(p.getName(), p);
        }

        Map<String, File> files;

        files = scriptManager.getModuleFiles("js", folder_autorun);
        if (files != null) for (File file : files.values())
            try {
                String script = new String(
                        IOUtils.toByteArray(file.toURI()),
                        VarScript.UTF8
                );
                Caller caller = Caller.getCallerFor(this);
                SEFileProgram program = new SEFileProgram(this, caller, getScriptEngine("JavaScript"), null);
                program.runScript(script);
            } catch (Exception ignored) {
            }
        files = scriptManager.getModuleFiles("js", folder_module);
        if (files != null) for (Map.Entry<String, File> e : files.entrySet())
            try {
                String name = e.getKey();
                if (!name.matches("[A-Za-z0-9_\\-]+")) {
                    Bukkit.getLogger().warning(
                            "javascript module " + name + " has incorrect name"
                    );
                    continue;
                }
                File file = e.getValue();
                String script = VarScriptIOUtils.readFile(file);
                if (script == null) {
                    Bukkit.getLogger().warning(
                            "can not read javascript module " + name
                    );
                }
                Caller caller = Caller.getCallerFor(this);
                SEFileProgram program = new SEFileProgram(this, caller, getScriptEngine("JavaScript"), null);
                Object v = program.runScript("new function " + name + "(){\n" + script + "\n}()");
                defineConst(name, v);
            } catch (Exception ignored) {
            }
        files = scriptManager.getModuleFiles("vs", folder_autorun);
        if (files != null) for (File file : files.values())
            try {
                String script = new String(
                        IOUtils.toByteArray(file.toURI()),
                        VarScript.UTF8
                );
                Caller caller = Caller.getCallerFor(this);
                VarscriptProgram program = new VarscriptProgram(this, caller);
                me.dpohvar.varscript.vs.Thread thread = new Thread(program);
                Function function = VSCompiler.compile(script).build(program.getScope());
                thread.pushFunction(function, program);
                new ThreadRunner(thread).runThreads();
            } catch (Exception ignored) {
            }
        files = scriptManager.getModuleFiles("vsbin", folder_autorun);
        if (files != null) for (File file : files.values())
            try {
                byte[] bytes = IOUtils.toByteArray(file.toURI());
                Caller caller = Caller.getCallerFor(this);
                VarscriptProgram program = new VarscriptProgram(this, caller);
                Thread thread = new Thread(program);
                Function function = VSCompiler.read(new ByteArrayInputStream(bytes)).build(program.getScope());
                thread.pushFunction(function, program);
                new ThreadRunner(thread).runThreads();
            } catch (Exception ignored) {
            }
        files = scriptManager.getModuleFiles("vs", folder_module);
        if (files != null) for (Map.Entry<String, File> e : files.entrySet())
            try {
                String name = e.getKey();
                if (!name.matches("[A-Za-z0-9_\\-]+")) {
                    Bukkit.getLogger().warning(
                            "varsript module " + name + " has incorrect name"
                    );
                    continue;
                }
                File file = e.getValue();
                String script = VarScriptIOUtils.readFile(file);
                Caller caller = Caller.getCallerFor(this);
                VarscriptProgram program = new VarscriptProgram(this, caller);
                Thread thread = new Thread(program);
                Function function = VSCompiler.compile(script, name).build(program.getScope());
                Context context = thread.pushFunction(function);
                defineConst(name, context);
                new ThreadRunner(thread).runThreads();
            } catch (Exception ignored) {
            }
        files = scriptManager.getModuleFiles("vsbin", folder_module);
        if (files != null) for (Map.Entry<String, File> e : files.entrySet())
            try {
                String name = e.getKey();
                if (!name.matches("[A-Za-z0-9_\\-]+")) {
                    Bukkit.getLogger().warning(
                            "varsript binary module " + name + " has incorrect name"
                    );
                    continue;
                }
                File file = e.getValue();
                byte[] bytes = IOUtils.toByteArray(file.toURI());
                Caller caller = Caller.getCallerFor(this);
                VarscriptProgram program = new VarscriptProgram(this, caller);
                Thread thread = new Thread(program);
                Function function = VSCompiler.read(new ByteArrayInputStream(bytes)).build(program.getScope());
                if (!function.getName().equals(name)) {
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

        scheduler.reload();

    }

    public Program getProgram(int pid) {
        return programs.get(pid);
    }

    public void removeProgram(int id) {
        Program p = programs.get(id);
        if (p != null && p.isFinished()) {
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
        fields.put(key, value);
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
    public me.dpohvar.varscript.vs.Runnable getConstructor() {
        try {
            return new ReflectClass(Runtime.class.getConstructor(VarScript.class), this);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Fieldable getProto() {
        return null;
    }

    @Override
    public void setProto(Fieldable proto) {
    }

    @Override
    public Object getVar(String varName) {
        if (constants.containsKey(varName)) return constants.get(varName);
        return fields.get(varName);
    }

    @Override
    public Runtime defineVar(String varName, Object value) {
        fields.put(varName, value);
        return this;
    }

    @Override
    public Runtime defineConst(String varName, Object value) {
        constants.put(varName, value);
        return this;
    }

    @Override
    public Runtime setVar(String varName, Object value) {
        return defineVar(varName, value);
    }

    @Override
    public Runtime delVar(String varName) {
        fields.remove(varName);
        return this;
    }
}
