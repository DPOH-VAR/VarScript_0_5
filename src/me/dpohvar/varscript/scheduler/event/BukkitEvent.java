package me.dpohvar.varscript.scheduler.event;

import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskEvent;
import me.dpohvar.varscript.trigger.Trigger;
import me.dpohvar.varscript.trigger.TriggerBukkitEvent;
import me.dpohvar.varscript.trigger.TriggerRunner;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 0:47
 */
public class BukkitEvent extends TaskEvent {

    private final String param;
    private Trigger trigger;

    public BukkitEvent(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    protected boolean register() {
        if (trigger != null) return true;
        if (param.isEmpty()) {
            error = true;
            return false;
        }
        String[] ss = param.split(" ");
        EventPriority priority = EventPriority.NORMAL;
        if (ss.length < 1 || ss.length > 2) {
            error = true;
            return false;
        }
        Class<? extends Event> eventClass = TriggerBukkitEvent.getEventClass(ss[0]);
        if (ss.length >= 2) priority = Converter.convert(EventPriority.values(), ss[1]);
        if (eventClass == null || priority == null) {
            error = true;
            return false;
        }
        final TaskEvent taskEvent = this;
        TriggerRunner<Event> runner = new TriggerRunner<Event>() {
            @Override
            public void run(Event event) {
                HashMap<String, Object> environment = new HashMap<String, Object>();
                TriggerBukkitEvent.makeEnvironment(event, environment);
                environment.put("TaskEvent", taskEvent);
                if (task.check(environment)) task.run(environment);
            }
        };
        trigger = new TriggerBukkitEvent(eventClass, priority, runner);
        error = false;
        return true;
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
        return "BUKKIT";
    }

    @Override
    public String toString() {
        return "BUKKIT" + (param == null || param.isEmpty() ? "" : " " + param);
    }
}
