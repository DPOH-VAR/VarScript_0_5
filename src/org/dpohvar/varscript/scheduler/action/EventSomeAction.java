package org.dpohvar.varscript.scheduler.action;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.dpohvar.varscript.scheduler.Task;
import org.dpohvar.varscript.scheduler.TaskAction;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 9:38
 */
public class EventSomeAction extends TaskAction {

    final String param;
    private int operation = -1;

    public EventSomeAction(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    public void run(Map<String, Object> environment) {
        Event event = (Event) environment.get("Event");
        if (event == null) return;
        switch (operation) {
            case 1:
                ((Cancellable) event).setCancelled(true);
                break;
            case 2:
                ((Cancellable) event).setCancelled(false);
                break;
        }
    }

    @Override
    protected boolean register() {
        if (param == null || param.isEmpty()) return false;
        else if ("CANCEL".equalsIgnoreCase(param)) operation = 1;
        else if ("UNCANCEL".equalsIgnoreCase(param)) operation = 2;
        else return false;

        return true;
    }

    @Override
    protected boolean unregister() {
        if (operation == 0) return false;
        operation = 0;
        return true;
    }

    public static String getType() {
        return "EVENT";
    }

    @Override
    public String toString() {
        return "EVENT" + (param == null || param.isEmpty() ? "" : " " + param);
    }

}
