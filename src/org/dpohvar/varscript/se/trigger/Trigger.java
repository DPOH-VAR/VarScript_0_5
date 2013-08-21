package org.dpohvar.varscript.se.trigger;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 14:39
 */
public interface Trigger {
    public void unregister();

    public boolean isRegistered();
}
