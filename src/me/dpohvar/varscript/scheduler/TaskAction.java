package me.dpohvar.varscript.scheduler;

import me.dpohvar.varscript.scheduler.action.*;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 17.07.13
 * Time: 2:28
 */
public abstract class TaskAction extends TaskEntry {

    protected TaskAction(Task task) {
        super(task, TaskEntryType.ACTION);
    }

    public abstract void run(Map<String, Object> environment);

    protected abstract boolean register();

    protected abstract boolean unregister();

    public static TaskAction fromString(Task task, String s) {
        String type = s;
        String argument = null;
        if (s.contains(" ")) {
            int ind = s.indexOf(' ');
            type = s.substring(0, ind);
            argument = s.substring(ind + 1, s.length());
        }
        if ("VS".equals(type)) {
            return new VSAction(task, argument);
        } else if ("JS".equals(type)) {
            return new JSAction(task, argument);
        } else if ("LOG".equals(type)) {
            return new LogAction(task, argument);
        } else if ("BROADCAST".equals(type)) {
            return new BroadcastAction(task, argument);
        } else if ("TASK".equals(type)) {
            return new TaskOperateAction(task, argument);
        } else if ("VSFILE".equals(type)) {
            return new VSFileAction(task, argument);
        } else if ("EVENT".equals(type)) {
            return new EventSomeAction(task, argument);
        }
        return new TaskActionError(task, s);
    }
}

class TaskActionError extends TaskAction {
    final String param;

    protected TaskActionError(Task task, String param) {
        super(task);
        this.param = param;
        this.error = true;
    }

    @Override
    public String toString() {
        return param;
    }

    @Override
    public void run(Map<String, Object> environment) {
    }

    @Override
    protected boolean register() {
        return false;
    }

    @Override
    protected boolean unregister() {
        return false;
    }

}