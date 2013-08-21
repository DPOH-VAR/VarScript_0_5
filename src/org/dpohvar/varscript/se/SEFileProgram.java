package org.dpohvar.varscript.se;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.dpohvar.varscript.Runtime;
import org.dpohvar.varscript.caller.Caller;
import org.dpohvar.varscript.converter.Converter;
import org.dpohvar.varscript.se.trigger.*;

import javax.script.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.07.13
 * Time: 22:49
 */
public class SEFileProgram implements SEProgram {
    private final Runtime runtime;
    private int pid = -1;
    private boolean finished = false;
    private final Caller caller;
    private final ScriptEngine engine;
    private final ScriptContext context;

    private HashSet<Trigger> triggers = new HashSet<Trigger>();

    public Trigger registerTrigger(Trigger trigger) {
        if (finished) {
            trigger.unregister();
        } else {
            triggers.add(trigger);
        }
        return trigger;
    }

    void clearTriggers() {
        for (Trigger t : triggers) t.unregister();
        triggers.clear();
    }

    public void removeTrigger(Trigger trigger) {
        triggers.remove(trigger);
        checkFinished();
    }

    public SEFileProgram(Runtime runtime, Caller caller, ScriptEngine engine, String[] args) {
        this.runtime = runtime;
        this.caller = caller;
        this.engine = engine;
        this.context = new SimpleScriptContext();
        Map<String, Object> environment = new HashMap<String, Object>();
        Bindings globalBind = new WrapBindings(runtime.getBindings(), environment);
        Bindings localBind = new SimpleBindings();
        context.setBindings(globalBind, ScriptContext.GLOBAL_SCOPE);
        context.setBindings(localBind, ScriptContext.ENGINE_SCOPE);
        localBind.put("args", args);
        environment.put("program", this);
        environment.put("me", caller.getInstance());
        environment.put("caller", caller);
        runtime.registerProgram(this);
    }

    public Trigger onTicks(Runnable runnable, long ticks) {
        return registerTrigger(new TicksTrigger(this, ticks, runnable));
    }

    public Trigger onTimeout(Runnable runnable, long ticks) {
        return registerTrigger(new TimeTrigger(this, ticks, runnable));
    }

    public Trigger everyTicks(Runnable runnable, long ticks) {
        return registerTrigger(new TicksRepeatTrigger(this, 0, ticks, runnable));
    }

    public Trigger everyTicks(Runnable runnable, long ticks, long delay) {
        return registerTrigger(new TicksRepeatTrigger(this, delay, ticks, runnable));
    }

    public Trigger everyTimeout(Runnable runnable, long ticks) {
        return registerTrigger(new TimeRepeatTrigger(this, ticks, runnable));
    }

    public Trigger onEvent(SERunnable runnable, String event) {
        Class<? extends Event> clazz = EventTrigger.getEventClass(event);
        return registerTrigger(new EventTrigger(this, clazz, EventPriority.NORMAL, runnable));
    }

    public Trigger onEvent(SERunnable runnable, String event, Object prior) {
        Class<? extends Event> clazz = EventTrigger.getEventClass(event);
        EventPriority priority = Converter.convert(EventPriority.values(), prior);
        return registerTrigger(new EventTrigger(this, clazz, priority, runnable));
    }

    public Trigger onEvent(SERunnable runnable, Class<? extends Event> clazz) {
        return registerTrigger(new EventTrigger(this, clazz, EventPriority.NORMAL, runnable));
    }

    public Trigger onEvent(SERunnable runnable, Class<? extends Event> clazz, Object prior) {
        EventPriority priority = Converter.convert(EventPriority.values(), prior);
        return registerTrigger(new EventTrigger(this, clazz, priority, runnable));
    }


    @Override
    public int getPID() {
        return pid;
    }

    public Object runScript(String script) throws ScriptException {
        return engine.eval(script, context);
    }

    @Override
    public void setPID(int pid) {
        if (this.pid == -1 && pid >= 0) this.pid = pid;
    }

    @Override
    public boolean isFinished() {
        return finished || checkFinished();
    }

    boolean checkFinished() {
        for (Trigger t : triggers) {
            if (t.isRegistered()) return false;
        }
        clearTriggers();
        return true;
    }

    @Override
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
        clearTriggers();
    }
}