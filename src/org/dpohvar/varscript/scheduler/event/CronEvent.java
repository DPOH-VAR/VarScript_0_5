package org.dpohvar.varscript.scheduler.event;

import it.sauronsoftware.cron4j.InvalidPatternException;
import org.dpohvar.varscript.scheduler.Task;
import org.dpohvar.varscript.scheduler.TaskEvent;
import org.dpohvar.varscript.trigger.Trigger;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 0:47
 */
public class CronEvent extends TaskEvent {

    private final String param;
    private Trigger trigger;
    private String taskID;
    final it.sauronsoftware.cron4j.Scheduler cron;

    public CronEvent(Task task, String param) {
        super(task);
        this.param = param;
        cron = task.getScheduler().runtime.getCron();
    }

    @Override
    protected boolean register() {

        final TaskEvent taskEvent = this;
        try {
            taskID = cron.schedule(param, new Runnable() {
                @Override
                public void run() {
                    HashMap<String, Object> environment = new HashMap<String, Object>();
                    environment.put("TaskEvent", taskEvent);
                    environment.put("Cron", cron);
                    environment.put("TaskID", taskID);
                    if (task.check(environment)) task.run(environment);
                }
            });
            return true;
        } catch (InvalidPatternException e) {
            return false;
        }

    }

    @Override
    protected boolean unregister() {
        if (taskID == null) return false;
        cron.deschedule(taskID);
        taskID = null;
        return true;
    }

    public static String getType() {
        return "CRON";
    }

    @Override
    public String toString() {
        return "CRON" + (param == null || param.isEmpty() ? "" : " " + param);
    }
}
