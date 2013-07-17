package me.dpohvar.varscript.scheduler;

import org.bukkit.event.Event;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 1:33
 */
public interface VSTrigger<T> {

    public void unregister();
    public void handle(T event);
}
