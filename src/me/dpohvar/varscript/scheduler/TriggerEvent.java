package me.dpohvar.varscript.scheduler;

import me.dpohvar.varscript.VarScript;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.event.Event;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 1:34
 */
public class TriggerEvent implements VSTrigger<Event>{

    private RegisteredListener registeredListener;
    private HandlerList handlerList;
    private TriggerRunner<Event> runner;

    public TriggerEvent(final Class<? extends Event> clazz, EventPriority priority, TriggerRunner<Event> runner){
        this.runner = runner;
        EventExecutor executor = new EventExecutor() {
            public void execute(Listener listener, Event event) throws EventException {
                if(clazz.isInstance(event)) handle(event);
            }
        };
        registeredListener = new RegisteredListener(null,executor,priority, VarScript.instance,false);
        handlerList = getEventListeners(getRegistrationClass(clazz));
        handlerList.register(registeredListener);
    }

    @Override public void unregister() {
        handlerList.unregister(registeredListener);
    }

    @Override public void handle(Event event) {
        runner.run(event);
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

    public static HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }
}
