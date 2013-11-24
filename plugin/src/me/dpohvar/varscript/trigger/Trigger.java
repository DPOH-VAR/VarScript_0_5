package me.dpohvar.varscript.trigger;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 1:33
 */
public interface Trigger<T> {

    public void unregister();

    public boolean isRegistered();

    public void handle(T event);
}
