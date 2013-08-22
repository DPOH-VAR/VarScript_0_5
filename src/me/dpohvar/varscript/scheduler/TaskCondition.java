package me.dpohvar.varscript.scheduler;

import me.dpohvar.varscript.scheduler.condition.ChanceCondition;
import me.dpohvar.varscript.scheduler.condition.EventStatusCondition;
import me.dpohvar.varscript.scheduler.condition.JSCondition;
import me.dpohvar.varscript.scheduler.condition.VSCondition;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 17.07.13
 * Time: 2:28
 */
public abstract class TaskCondition extends TaskEntry {

    public TaskCondition(Task task) {
        super(task, TaskEntryType.CONDITION);
    }

    protected abstract boolean register();

    protected abstract boolean unregister();

    abstract public boolean check(Map<String, Object> environment);

    public static TaskCondition fromString(Task task, String s) {
        String type = s;
        String argument = null;
        if (s.contains(" ")) {
            int ind = s.indexOf(' ');
            type = s.substring(0, ind);
            argument = s.substring(ind + 1, s.length());
        }
        if ("CHANCE".equals(type)) {
            return new ChanceCondition(task, argument);
        } else if ("EVENT".equals(type)) {
            return new EventStatusCondition(task, argument);
        } else if ("VS".equals(type)) {
            return new VSCondition(task, argument);
        } else if ("JS".equals(type)) {
            return new JSCondition(task, argument);
        }
        return new TaskConditionError(task, s);
    }
}

class TaskConditionError extends TaskCondition {
    final String param;

    protected TaskConditionError(Task task, String param) {
        super(task);
        this.param = param;
        this.error = true;
    }

    @Override
    public String toString() {
        return param;
    }

    @Override
    protected boolean register() {
        return false;
    }

    @Override
    protected boolean unregister() {
        return false;
    }

    @Override
    public boolean check(Map<String, Object> environment) {
        return false;
    }
}
