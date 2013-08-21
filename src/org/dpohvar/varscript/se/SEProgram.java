package org.dpohvar.varscript.se;

import org.bukkit.event.Event;
import org.dpohvar.varscript.Program;
import org.dpohvar.varscript.se.trigger.SERunnable;
import org.dpohvar.varscript.se.trigger.Trigger;

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
