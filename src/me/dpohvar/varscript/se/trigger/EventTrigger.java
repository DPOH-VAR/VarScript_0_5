package me.dpohvar.varscript.se.trigger;

import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.se.SEProgram;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 14:33
 */
public class EventTrigger implements Trigger {

    final SEProgram program;
    private RegisteredListener registeredListener;
    private HandlerList handlerList;
    boolean registered = true;


    public EventTrigger(SEProgram program, final Class<? extends Event> clazz, EventPriority priority, final SERunnable runner) {
        this.program = program;
        EventExecutor executor = new EventExecutor() {
            public void execute(Listener listener, Event event) throws EventException {
                runner.run(event);
            }
        };
        registeredListener = new RegisteredListener(null, executor, priority, VarScript.instance, false);
        handlerList = getEventListeners(getRegistrationClass(clazz));
        handlerList.register(registeredListener);
    }

    public void unregister() {
        handlerList.unregister(registeredListener);
        registered = false;
        program.removeTrigger(this);
    }

    public boolean isRegistered() {
        return registered;
    }


    public static HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    public static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName());
            }
        }
    }


    private static final String[] classPrefix = new String[]{
            "org.bukkit.event.",
            "org.bukkit.event.block.",
            "org.bukkit.event.enchantment.",
            "org.bukkit.event.entity.",
            "org.bukkit.event.hanging.",
            "org.bukkit.event.inventory.",
            "org.bukkit.event.painting.",
            "org.bukkit.event.player.",
            "org.bukkit.event.server.",
            "org.bukkit.event.vehicle.",
            "org.bukkit.event.weather.",
            "org.bukkit.event.world.",
            "me.dpohvar.varscript.event.",
    };

    public static Class<? extends Event> getEventClass(String className) {
        Class<? extends Event> eventClass = null;
        try {
            return (Class<? extends Event>) Class.forName(className);
        } catch (Exception ignored) {
        }
        if (eventClass == null) for (String prefix : classPrefix)
            try {
                try {
                    return (Class<? extends Event>) Class.forName(prefix + className);
                } catch (Exception ignored) {
                }
            } catch (Exception ignored) {
            }
        return null;
    }

}
