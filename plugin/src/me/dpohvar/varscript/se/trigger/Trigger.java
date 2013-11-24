package me.dpohvar.varscript.se.trigger;

import me.dpohvar.varscript.se.SEProgram;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 14:39
 */
public abstract class Trigger {

    private final SEProgram program;

    public SEProgram getProgram() {
        return program;
    }

    public Trigger(SEProgram program) {
        this.program = program;
    }

    public void stop() {
        setUnregistered();
        program.removeTrigger(this);
    }


    public abstract void setUnregistered();

    public abstract boolean isRegistered();
}
