package me.dpohvar.varscript.se.trigger;

import me.dpohvar.varscript.se.SEProgram;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 14:33
 */
public class SleepTrigger extends Trigger {

    boolean registered = true;

    public SleepTrigger(SEProgram program) {
        super(program);
    }

    public void setUnregistered() {
        registered = false;
    }

    public void run() {
    }

    public boolean isRegistered() {
        return registered;
    }

}
