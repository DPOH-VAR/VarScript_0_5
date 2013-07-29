package me.dpohvar.varscript.js;

import me.dpohvar.varscript.Program;
import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.trigger.Trigger;

import javax.script.ScriptEngine;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.07.13
 * Time: 22:49
 */
public class SEProgram<T extends ScriptEngine> implements Program {
    private final Runtime runtime;
    private int pid = -1;
    private boolean finished = false;
    private final Caller caller;

    private HashSet<Trigger<?>> triggers = new HashSet<Trigger<?>>();

    public void addTrigger(Trigger<?> trigger){
        if(isFinished()){
            trigger.unregister();
        }else{
            triggers.add(trigger);
        }
    }

    void clearTriggers(){
        for (Trigger t:triggers) t.unregister();
        triggers.clear();
    }

    public void removeTrigger(Trigger<?> trigger){
        triggers.remove(trigger);
    }

    public SEProgram(Runtime runtime,Caller caller){
        this.runtime = runtime;
        this.caller = caller;
        runtime.registerProgram(this);
    }

    @Override public int getPID() {
        return pid;
    }

    @Override
    public void setPID(int pid) {
        if(this.pid==-1 && pid>=0) this.pid=pid;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Caller getCaller() {
        return caller;
    }

    @Override
    public me.dpohvar.varscript.Runtime getRuntime() {
        return runtime;
    }

    public void setFinished(){
        finished = true;
        clearTriggers();
    }
}