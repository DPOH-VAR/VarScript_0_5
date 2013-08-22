package me.dpohvar.varscript.se;

import me.dpohvar.varscript.Program;
import me.dpohvar.varscript.se.trigger.SERunnable;
import me.dpohvar.varscript.se.trigger.Trigger;
import org.bukkit.event.Event;

import javax.script.ScriptException;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 16:32
 */
public interface SEProgram extends Program {

    public Trigger registerTrigger(Trigger trigger);

    public void removeTrigger(Trigger trigger);

    public void putToEnvironment(String key, Object val);

    public Trigger onTicks(Runnable runnable, long ticks);

    public Trigger onTimeout(Runnable runnable, long timeout);

    public Trigger everyTicks(Runnable runnable, long ticks);

    public Trigger everyTimeout(Runnable runnable, long timeout);

    public Trigger everyTicks(Runnable runnable, long ticks, long delay);

    public Trigger onEvent(SERunnable runnable, String event);

    public Trigger onEvent(SERunnable runnable, String event, Object prior);

    public Trigger onEvent(SERunnable runnable, Class<? extends Event> clazz);

    public Trigger onEvent(SERunnable runnable, Class<? extends Event> clazz, Object prior);

    public Object runScript(String script) throws ScriptException;
}
