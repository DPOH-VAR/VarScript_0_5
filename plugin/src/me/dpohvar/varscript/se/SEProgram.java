package me.dpohvar.varscript.se;

import me.dpohvar.varscript.Program;
import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.se.trigger.*;
import me.dpohvar.varscript.utils.Module;
import me.dpohvar.varscript.utils.ModuleManager;
import me.dpohvar.varscript.utils.ScopeBindings;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.SimpleScriptContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 16:32
 */
public abstract class SEProgram implements Program {

    private final me.dpohvar.varscript.Runtime runtime;
    private int pid = -1;
    private boolean finished = false;
    private final Caller caller;
    private final ScriptEngine engine;
    private final ScopeBindings engineBindings;
    private final Map<String, Object> programEnvironment = new HashMap<String, Object>();
    protected final ScriptContext context;
    private HashSet<Trigger> triggers = new HashSet<Trigger>();
    private HashSet<FinishTrigger> finishTriggers = new HashSet<FinishTrigger>();

    public FinishTrigger onFinish(SERunnable runnable) {
        return registerFinishTrigger(new FinishTrigger(this, runnable));
    }

    public SEProgram(Runtime runtime, Caller caller, ScriptEngine engine, Map<String, Object> scope) {
        this.runtime = runtime;
        this.caller = caller;
        this.engine = engine;
        if (engine == null) throw new RuntimeException("Program with no engine");
        this.context = new SimpleScriptContext();
        if (scope == null) scope = caller.getFields();
        Map<String, Object> engineBindings = runtime.getEngineBindings(engine);
        this.engineBindings = new ScopeBindings(engineBindings)
                .listen(runtime.getGlobalBindings())
                .listen(programEnvironment)
                .listen(runtime.getModuleBindings(engine))
                .listen(scope)
                .listen(engineBindings)
                .listen(runtime.getRuntimeBindings());
        this.context.setBindings(this.engineBindings, ScriptContext.GLOBAL_SCOPE);
        this.context.setBindings(new ScopeBindings(scope).listen(scope), ScriptContext.ENGINE_SCOPE);
        this.programEnvironment.put("program", this);
        this.programEnvironment.put("me", caller.getInstance());
        this.programEnvironment.put("caller", caller);
        runtime.registerProgram(this);
    }

    public Object require(String moduleName) {
        ModuleManager manager = runtime.getModuleManager();
        Module module = manager.getModule(engine, moduleName);
        if (module == null) module = manager.reload(engine, moduleName);
        if (module == null) return null;
        if (!module.isLoaded()) return null;
        return module.getLangModule();
    }

    public void error(String reason) {
        VarScript.instance.getLogger().warning("[" + engine.getFactory().getEngineName() + "] " + reason);
        throw new RuntimeException(reason);
    }

    public Object runScript(String script) {
        Object result;
        try {
            result = engine.eval(script, context);
        } catch (Throwable e) {
            getCaller().handleException(e);
            result = null;
        }
        checkFinished();
        return result;
    }

    public Trigger registerTrigger(Trigger trigger) {
        if (finished) {
            trigger.setUnregistered();
        } else {
            triggers.add(trigger);
        }
        return trigger;
    }

    public void setConstant(String name, Object value) {
        engineBindings.put(name, value);
    }

    public void remConstant(String name) {
        engineBindings.remove(name);
    }

    public Object getConstant(String name) {
        return engineBindings.get(name);
    }

    public void putToEnvironment(String key, Object val) {
        this.programEnvironment.put(key, val);
    }

    public FinishTrigger registerFinishTrigger(FinishTrigger trigger) {
        if (finished) {
            trigger.run();
            trigger.setUnregistered();
        } else {
            finishTriggers.add(trigger);
        }
        return trigger;
    }

    void clearTriggers() {
        for (Trigger t : triggers) t.setUnregistered();
        triggers.clear();
        for (Trigger t : finishTriggers) t.setUnregistered();
        finishTriggers.clear();
    }

    public void removeTrigger(Trigger trigger) {
        triggers.remove(trigger);
        checkFinished();
    }

    public void removeFinishTrigger(FinishTrigger trigger) {
        finishTriggers.remove(trigger);
    }

    public boolean isFinished() {
        return finished || checkFinished();
    }

    boolean checkFinished() {
        for (Trigger t : triggers) {
            if (t.isRegistered()) return false;
        }
        setFinished();
        return true;
    }

    public Trigger onTicks(Runnable runnable, long ticks) {
        return registerTrigger(new TicksTrigger(this, ticks, runnable));
    }

    public Trigger onTimeout(Runnable runnable, long timeout) {
        return registerTrigger(new TimeTrigger(this, timeout, runnable));
    }

    public Trigger everyTicks(Runnable runnable, long ticks) {
        return registerTrigger(new TicksRepeatTrigger(this, 0, ticks, runnable));
    }

    public Trigger everyTicks(Runnable runnable, long ticks, long delay) {
        return registerTrigger(new TicksRepeatTrigger(this, delay, ticks, runnable));
    }

    public Trigger everyTimeout(Runnable runnable, long timeout) {
        return registerTrigger(new TimeRepeatTrigger(this, timeout, runnable));
    }

    public Trigger onSleep() {
        return registerTrigger(new SleepTrigger(this));
    }

    public Trigger onEvent(SERunnable runnable, String event, boolean ignoreCancelled) {
        Class<? extends Event> clazz = EventTrigger.getEventClass(event);
        return registerTrigger(new EventTrigger(this, clazz, EventPriority.NORMAL, runnable, ignoreCancelled));
    }

    public Trigger onEvent(SERunnable runnable, String event) {
        Class<? extends Event> clazz = EventTrigger.getEventClass(event);
        return registerTrigger(new EventTrigger(this, clazz, EventPriority.NORMAL, runnable, true));
    }

    public Trigger onEvent(SERunnable runnable, String event, Object prior) {
        Class<? extends Event> clazz = EventTrigger.getEventClass(event);
        EventPriority priority = Converter.convert(EventPriority.values(), prior);
        return registerTrigger(new EventTrigger(this, clazz, priority, runnable, true));
    }

    public Trigger onEvent(SERunnable runnable, String event, Object prior, boolean ignoreCancelled) {
        Class<? extends Event> clazz = EventTrigger.getEventClass(event);
        EventPriority priority = Converter.convert(EventPriority.values(), prior);
        return registerTrigger(new EventTrigger(this, clazz, priority, runnable, ignoreCancelled));
    }

    public Trigger onEvent(SERunnable runnable, Class<? extends Event> clazz) {
        return registerTrigger(new EventTrigger(this, clazz, EventPriority.NORMAL, runnable, true));
    }

    public Trigger onEvent(SERunnable runnable, Class<? extends Event> clazz, boolean ignoreCancelled) {
        return registerTrigger(new EventTrigger(this, clazz, EventPriority.NORMAL, runnable, ignoreCancelled));
    }

    public Trigger onEvent(SERunnable runnable, Class<? extends Event> clazz, Object prior) {
        EventPriority priority = Converter.convert(EventPriority.values(), prior);
        return registerTrigger(new EventTrigger(this, clazz, priority, runnable, true));
    }

    public Trigger onEvent(SERunnable runnable, Class<? extends Event> clazz, Object prior, boolean ignoreCancelled) {
        EventPriority priority = Converter.convert(EventPriority.values(), prior);
        return registerTrigger(new EventTrigger(this, clazz, priority, runnable, ignoreCancelled));
    }

    public Caller getCaller() {
        return caller;
    }

    @Override
    public Runtime getRuntime() {
        return runtime;
    }

    @Override
    public void setFinished() {
        finished = true;
        for (FinishTrigger t : finishTriggers) {
            t.run();
        }
        clearTriggers();
    }

    public void stop() {
        setFinished();
    }

    @Override
    public void setPID(int pid) {
        if (this.pid == -1 && pid >= 0) this.pid = pid;
    }

    @Override
    public int getPID() {
        return pid;
    }
}
