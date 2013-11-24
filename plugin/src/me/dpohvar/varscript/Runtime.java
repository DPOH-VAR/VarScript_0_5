package me.dpohvar.varscript;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.converter.rule.*;
import me.dpohvar.varscript.scheduler.Scheduler;
import me.dpohvar.varscript.se.SECallerProgram;
import me.dpohvar.varscript.utils.ModuleManager;
import me.dpohvar.varscript.utils.ScriptManager;
import me.dpohvar.varscript.utils.reflect.ReflectClass;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import sun.misc.JarFilter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * 21.02.13 23:05
 *
 * @author DPOH-VAR
 */
public class Runtime implements Fieldable, Scope {

    /* Инициалазация всех библиотек при загрузке, а также загрузчиков классов */
    public static final URLClassLoader libLoader;
    private static final ClassLoader contextClassLoader = VarScript.instance.getVarscriptClassLoader();

    static {
        List<URL> urlList = new ArrayList<URL>();
        File dir = new File("lib");
        if (!dir.isDirectory()) {
            if (dir.mkdir()) {
                throw new RuntimeException("can not create 'dir' folder");
            }
        }
        try {
            urlList.add(dir.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File[] files = dir.listFiles(new JarFilter());
        if (files != null) for (File f : files)
            try {
                urlList.add(f.toURI().toURL());
                VarScript.instance.getLogger().info("jar file " + f.getName() + " loaded");
            } catch (IOException e) {
                e.printStackTrace();
            }
        URL[] urlArray = new URL[urlList.size()];
        for (int i = 0; i < urlArray.length; i++) urlArray[i] = urlList.get(i);
        libLoader = new URLClassLoader(urlArray, contextClassLoader);
    }

    /* Менеджер скриптов. По идее должен быть только один.. пусть грузится тут */
    public static final ScriptManager scriptManager = new ScriptManager(VarScript.instance.getScriptHome());
    /* Менеджер модулей. Срать на него пока что, пусть работает с багами */
    private final ModuleManager moduleManager = new ModuleManager(scriptManager, this);
    /* Инициалазация всех скриптовых движков, по одному экземпляру */
    private static ScriptEngineManager engineManager = new ScriptEngineManager(libLoader);
    private static HashMap<String, ScriptEngineFactory> engineFactories = new HashMap<String, ScriptEngineFactory>();
    private static HashMap<String, ScriptEngine> engines = new HashMap<String, ScriptEngine>();
    private static HashMap<ScriptEngineFactory, ScriptEngine> enginesByFactory = new HashMap<ScriptEngineFactory, ScriptEngine>();

    static {
        for (ScriptEngineFactory factory : engineManager.getEngineFactories()) {
            VarScript.instance.getLogger().info(
                    "load " + factory.getEngineName() + " " + factory.getEngineVersion() +
                            "\nlang: " + factory.getLanguageName() + " " + factory.getLanguageVersion() +
                            "\nname: " + StringUtils.join(factory.getNames(), ",") +
                            "\nextension: " + StringUtils.join(factory.getExtensions(), ",")
            );
            ScriptEngine engine = factory.getScriptEngine();
            scriptManager.createEnginesFolder(factory);
            enginesByFactory.put(factory, engine);
            for (String name : factory.getNames()) {
                engineFactories.put(name, factory);
                engines.put(name, engine);
            }
        }
    }

    /* Методы для получения загруженных скриптодвижков и их фабрик */
    public static ScriptEngine getEngine(String name) {
        return engines.get(name);
    }

    public static Collection<ScriptEngine> getEngines() {
        return enginesByFactory.values();
    }

    public static ScriptEngineFactory getScriptEngineFactory(String name) {
        return engineFactories.get(name);
    }

    public static ScriptEngine getScriptEngineByFactory(ScriptEngineFactory factory) {
        return enginesByFactory.get(factory);
    }

    // просто так. Ссылка на инициализирующий плагин.
    public final VarScript plugin;

    // Список запущенных программ. А также свободный идентификатор, чтобы дать следующей проге.
    private HashMap<Integer, Program> programs = new HashMap<Integer, Program>();
    private int freeId = 0;

    // Конвертер. Синглтон он у нас, вроде как.
    public final Converter converter = new Converter();

    // Планировщик. Инициализация в конструкторе.
    private Scheduler scheduler;

    /* это биндинги, используются в таком порядке:
       GlobalBinding - глобальные константы
       ProgramBinding - константы текущей программы
       ModuleBinding - модули для текущего языка
       UserBinding - переменные пользователя
       EngineBinding - переменные текущего языка
       RuntimeBinding - глобальные переменные для всех языков
     */
    private HashMap<String, Object> globalBindings = new HashMap<String, Object>();
    private HashMap<String, Object> runtimeBindings = new HashMap<String, Object>();
    private HashMap<ScriptEngine, Map<String, Object>> engineBindings = new HashMap<ScriptEngine, Map<String, Object>>();
    private HashMap<ScriptEngine, Map<String, Object>> moduleBindings = new HashMap<ScriptEngine, Map<String, Object>>();

    /* Задаем глобальные биндинги бля каждого скриптового языка */ {
        for (ScriptEngine engine : enginesByFactory.values()) {
            engineBindings.put(engine, new HashMap<String, Object>());
            moduleBindings.put(engine, new HashMap<String, Object>());
        }
        engineBindings.put(null, new HashMap<String, Object>());
        moduleBindings.put(null, new HashMap<String, Object>());
    }

    // метод чтобы получить пользовательски биндинги для скриптодвижка. Если не указать - получим биндинги варскрипта
    public Map<String, Object> getEngineBindings(ScriptEngine engine) {
        return engineBindings.get(engine);
    }

    // метод чтобы получить биндинги модулей для скриптодвижка. Если не указать - получим биндинги варскрипта
    public Map<String, Object> getModuleBindings(ScriptEngine engine) {
        return moduleBindings.get(engine);
    }

    // метод чтобы получить главные биндинги
    public Map<String, Object> getGlobalBindings() {
        return globalBindings;
    }

    // метод чтобы получить главные биндинги
    public Map<String, Object> getRuntimeBindings() {
        return runtimeBindings;
    }

    public void startScript(Object caller, String script, String lang, Map<String, Object> scope) {
        startScript(Caller.getCallerFor(caller), script, lang, scope);
    }

    public void startScript(Caller caller, String script, String lang, Map<String, Object> bindings) {
        try {
            if (lang == null || lang.equalsIgnoreCase("varscript") || lang.equalsIgnoreCase("vs")) {
                CommandList cmd = VSCompiler.compile(script);
                VarscriptProgram program = new VarscriptProgram(this, caller, bindings);
                me.dpohvar.varscript.vs.Thread thread = new Thread(program);
                thread.pushFunction(cmd.build(program.getScope()), program);
                new ThreadRunner(thread).runThreads();
                return;
            }
            ScriptEngine engine = getEngine(lang);
            if (engine == null) {
                caller.send(ChatColor.RED + "no script engine with name: " + ChatColor.YELLOW + lang);
                return;
            }
            SECallerProgram program = new SECallerProgram(this, caller, engine, bindings);
            program.runScript(script);
        } catch (Throwable e) {
            caller.handleException(e);
        }
    }

    public Object runScript(Object caller, String script, String lang, Map<String, Object> bindings) {
        return runScript(Caller.getCallerFor(caller), script, lang, bindings);
    }

    public Object runScript(Caller caller, String script, String lang, Map<String, Object> bindings) {
        try {
            if (lang.equalsIgnoreCase("varscript")) {
                CommandList cmd = VSCompiler.compile(script);
                VarscriptProgram program = new VarscriptProgram(this, caller, bindings);
                me.dpohvar.varscript.vs.Thread thread = new Thread(program);
                thread.pushFunction(cmd.build(program.getScope()), program);
                new ThreadRunner(thread).runThreads();
                return thread.pop();
            }
            ScriptEngine engine = getEngine(lang);
            if (engine == null) {
                caller.send(ChatColor.RED + "no script engine with name: " + ChatColor.YELLOW + lang);
                return null;
            }
            SECallerProgram program = new SECallerProgram(this, caller, engine, bindings);
            return program.runScript(script);
        } catch (Throwable e) {
            caller.handleException(e);
            return null;
        }
    }

    public Object runFile(Object caller, String filename, String lang, Map<String, Object> bindings) {
        return runFile(Caller.getCallerFor(caller), filename, lang, bindings);
    }

    public Object runFile(Caller caller, String filename, String lang, Map<String, Object> bindings) {
        try {
            if (lang.equalsIgnoreCase("varscript")) {

                VarscriptProgram program = new VarscriptProgram(this, caller, bindings);
                me.dpohvar.varscript.vs.Thread thread = new Thread(program);
                InputStream input = scriptManager.openScriptFile("vsbin", filename);
                CommandList cmd;
                if (input != null) {
                    cmd = VSCompiler.read(input);
                    input.close();
                } else {
                    String source = scriptManager.readScriptFile("vs", filename);
                    cmd = VSCompiler.compile(source, filename);
                }
                thread.pushFunction(cmd.build(program.getScope()), program);
                new ThreadRunner(thread).runThreads();
                return thread.pop();
            }
            ScriptEngine engine = getEngine(lang);
            if (engine == null) {
                caller.send(ChatColor.RED + "no script engine with name: " + ChatColor.YELLOW + lang);
                return null;
            }
            String script = Runtime.scriptManager.readScriptFile(engine.getFactory(), filename);
            SECallerProgram program = new SECallerProgram(this, caller, engine, bindings);
            return program.runScript(script);
        } catch (Throwable e) {
            caller.handleException(e);
            return null;
        }
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public void registerProgram(Program program) {
        if (program.getPID() != -1)
            throw new RuntimeException("program already registered with PID=" + program.getPID());
        programs.put(freeId, program);
        program.setPID(freeId++);
    }


    public void disable() {
        for (Program program : new ArrayList<Program>(programs.values())) {
            program.setFinished();
        }
        moduleManager.unload();
    }

    public Runtime(VarScript plugin) {
        this.plugin = plugin;
        this.plugin.runtime = this;
        this.scheduler = new Scheduler(this, plugin.getSchedulerHome());
        PluginManager pm = Bukkit.getPluginManager();

        RuleByte ruleByte = new RuleByte();
        this.converter.addRule(new RuleBoolean());
        this.converter.addRule(new RuleBlock());
        this.converter.addRule(new RuleBlockState());
        this.converter.addRule(ruleByte);
        this.converter.addRule(new RuleBytes(ruleByte));
        this.converter.addRule(new RuleCharacter());
        this.converter.addRule(new RuleCaller());
        this.converter.addRule(new RuleClass());
        this.converter.addRule(new RuleCollection());
        this.converter.addRule(new RuleCommandList());
        this.converter.addRule(new RuleDouble());
        this.converter.addRule(new RuleEntity());
        this.converter.addRule(new RuleFieldable());
        this.converter.addRule(new RuleFloat());
        this.converter.addRule(new RuleInteger());
        this.converter.addRule(new RuleInventory());
        this.converter.addRule(new RuleItemStack());
        this.converter.addRule(new RuleIterable());
        this.converter.addRule(new RuleList());
        this.converter.addRule(new RuleLocation());
        this.converter.addRule(new RuleLong());
        this.converter.addRule(new RuleMap());
        this.converter.addRule(new RuleOfflinePlayer());
        try {
            converter.addRule(new RuleScoreboard());
        } catch (NoClassDefFoundError ignored) {
        }
        this.converter.addRule(new RuleShort());
        this.converter.addRule(new RuleString());
        this.converter.addRule(new RuleVector());
        this.converter.addRule(new RuleWorld());
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

        globalBindings = new HashMap<String, Object>();

        defineConst("Server", Bukkit.getServer());
        defineConst("Runtime", runtime);
        defineConst("VarscriptRuntime", runtime);
        defineConst("PluginManager", pm);
        defineConst("Scheduler", scheduler);
        defineConst("VarscriptScheduler", scheduler);
        defineConst("EnginesManager", engineManager);
        defineConst("Engines", engines);
        for (Plugin p : pm.getPlugins()) {
            defineConst(p.getName(), p);
        }

        moduleManager.reload();
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
        return globalBindings.keySet();
    }

    @Override
    public Object getField(String key) {
        return globalBindings.get(key);
    }

    @Override
    public void setField(String key, Object value) {
        globalBindings.put(key, value);
    }

    @Override
    public void removeField(String key) {
        globalBindings.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        return globalBindings.containsKey(key);
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
        Map<String, Object> vsBindings = getEngineBindings(null);
        if (vsBindings.containsKey(varName)) return vsBindings.get(varName);
        if (vsBindings.containsKey(varName)) return vsBindings.get(varName);

        return engineBindings.get(varName);
    }

    @Override
    public Runtime defineVar(String varName, Object value) {
        globalBindings.put(varName, value);
        return this;
    }

    @Override
    public Runtime defineConst(String varName, Object value) {
        return defineVar(varName, value);
    }

    @Override
    public Runtime setVar(String varName, Object value) {
        return defineVar(varName, value);
    }

    @Override
    public Runtime delVar(String varName) {
        globalBindings.remove(varName);
        return this;
    }
}
