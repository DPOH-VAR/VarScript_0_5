package me.dpohvar.varscript.se.trigger;

import me.dpohvar.varscript.se.SEProgram;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 14:33
 */
public class FinishTrigger extends Trigger {

    boolean registered = true;
    final SERunnable runnable;

    public FinishTrigger(SEProgram program, SERunnable runner) {
        super(program);
        this.runnable = runner;
    }

    public void setUnregistered() {
        registered = false;
        synchronized (runnable) {
            runnable.notify();
        }
    }

    public void run() {
        try {
            runnable.run(getProgram());
        } catch (Exception e) {
            getProgram().getCaller().handleException(e);
        }
    }

    public boolean isRegistered() {
        return registered;
    }

}
