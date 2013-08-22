package me.dpohvar.varscript.scheduler.event;

import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskEvent;
import me.dpohvar.varscript.trigger.Trigger;
import me.dpohvar.varscript.trigger.TriggerDelay;
import me.dpohvar.varscript.trigger.TriggerRunner;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 0:47
 */
public class TaskLoadEvent extends TaskEvent {

    private final String param;
    private Trigger trigger;

    public TaskLoadEvent(Task task, String param) {
        super(task);
        if (param == null) param = "";
        this.param = param;
    }

    @Override
    protected boolean register() {
        if (trigger != null) return true;
        try {
            long delay = 0;
            if (param != null && !param.isEmpty()) delay = Long.parseLong(param);
            final TaskEvent taskEvent = this;
            TriggerRunner<Trigger> runner = new TriggerRunner<Trigger>() {
                @Override
                public void run(Trigger t) {
                    HashMap<String, Object> environment = new HashMap<String, Object>();
                    environment.put("TaskEvent", taskEvent);
                    if (task.check(environment)) task.run(environment);
                }
            };
            trigger = new TriggerDelay(delay, runner);
            return true;
        } catch (Exception ignored) {
            return false;
        }

    }

    @Override
    protected boolean unregister() {
        if (trigger != null) {
            trigger.unregister();
            trigger = null;
            return true;
        }
        return false;
    }

    public static String getType() {
        return "LOAD";
    }

    @Override
    public String toString() {
        return "LOAD" + (param == null || param.isEmpty() ? "" : " " + param);
    }
}
