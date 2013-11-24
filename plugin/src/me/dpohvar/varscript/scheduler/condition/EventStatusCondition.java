package me.dpohvar.varscript.scheduler.condition;

import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskCondition;
import me.dpohvar.varscript.trigger.TriggerBukkitEvent;
import org.bukkit.event.Cancellable;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 22.07.13
 * Time: 1:44
 */
public class EventStatusCondition extends TaskCondition {

    private final String param;
    private int operation = 0;
    private Class type = null;

    public EventStatusCondition(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    protected boolean register() {
        if (checkNoCase(param, "IS CANCELED", "CANCELED", "IS CANCELLED", "CANCELLED")) {
            operation = 0;
            return true;
        }
        if (checkNoCase(param, "IS NOT CANCELED", "NOT CANCELED", "IS NOT CANCELLED", "NOT CANCELLED")) {
            operation = 1;
            return true;
        }
        if (param.toUpperCase().startsWith("TYPE ")) {
            type = TriggerBukkitEvent.getEventClass(param.substring(5));
            operation = 2;
            return true;
        }
        if (param.toUpperCase().startsWith("TYPE IS")) {
            type = TriggerBukkitEvent.getEventClass(param.substring(7));
            operation = 2;
            return true;
        }
        return false;
    }

    @Override
    protected boolean unregister() {
        return true;
    }

    @Override
    public boolean check(Map<String, Object> environment) {
        Object e = environment.get("Event");
        if (operation == 0 && e instanceof Cancellable) {
            return ((Cancellable) e).isCancelled();
        } else if (operation == 1 && e instanceof Cancellable) {
            return !((Cancellable) e).isCancelled();
        } else if (operation == 2) {
            return (type.isInstance(e));
        }
        return false;
    }

    @Override
    public String toString() {
        return "EVENT" + (param == null || param.isEmpty() ? "" : " " + param);
    }

    public static boolean checkNoCase(String check, String... args) {
        for (String t : args) if (check.equalsIgnoreCase(t)) return true;
        return false;
    }
}
