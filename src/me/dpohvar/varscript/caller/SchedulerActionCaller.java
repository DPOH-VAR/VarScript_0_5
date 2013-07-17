package me.dpohvar.varscript.caller;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class SchedulerActionCaller extends Caller {

    protected Object action;

    SchedulerActionCaller(Object action){
        this.action = action;
    }

    @Override
    public Object getInstance() {
        return action;
    }
}
