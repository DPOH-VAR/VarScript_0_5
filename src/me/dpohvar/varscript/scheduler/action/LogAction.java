package me.dpohvar.varscript.scheduler.action;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskAction;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 9:38
 */
public class LogAction extends TaskAction {

    final String param;
    private String log;

    public LogAction(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    public void run(Map<String, Object> environment) {
        if (log == null) return;
        Caller.getCallerFor(task).send(log);
    }

    @Override
    protected boolean register() {
        try {
            log = param;
            error = false;
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    protected boolean unregister() {
        if (log == null) return false;
        log = null;
        return true;
    }

    public static String getType() {
        return "LOG";
    }

    @Override
    public String toString() {
        return "LOG" + (param == null || param.isEmpty() ? "" : " " + param);
    }

}
