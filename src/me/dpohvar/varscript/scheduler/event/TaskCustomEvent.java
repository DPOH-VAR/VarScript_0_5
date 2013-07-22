package me.dpohvar.varscript.scheduler.event;

import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.event.CustomEvent;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskEvent;
import me.dpohvar.varscript.trigger.Trigger;
import me.dpohvar.varscript.trigger.TriggerBukkitEvent;
import me.dpohvar.varscript.trigger.TriggerRunner;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 0:47
 */
public class TaskCustomEvent extends TaskEvent {

    private final String param;
    private Trigger trigger;

    public TaskCustomEvent(Task task, String param){
        super(task);
        this.param = param;
    }

    @Override protected boolean register() {
        if(trigger!=null)return true;
        if(param.isEmpty()) {
            error = true;
            return false;
        }
        String[] ss = param.split(" ");
        EventPriority priority = EventPriority.NORMAL;
        if(ss.length<1 ||ss.length>2) {
            error = true;
            return false;
        }
        final String name = ss[0];
        if(ss.length>=2) priority = Converter.convert(EventPriority.values(),ss[1]);
        if(name==null || name.isEmpty() || priority==null) {
            error = true;
            return false;
        }
        final TaskEvent taskEvent = this;
        TriggerRunner<CustomEvent> runner = new TriggerRunner<CustomEvent>() {
            @Override public void run(CustomEvent event) {
                if(!name.equals(event.getName())) return;
                Map<String,Object> environment = event.getAllParams();
                environment.put("TaskEvent",taskEvent);
                if(task.check(environment)) task.run(environment);
            }
        };
        trigger = new TriggerBukkitEvent<CustomEvent>(CustomEvent.class,priority,runner);
        error = false;
        return true;
    }
    @Override protected boolean unregister(){
        if(trigger!=null) {
            trigger.unregister();
            trigger = null;
            return true;
        }
        return false;
    }

    public static String getType() {
        return "CUSTOMEVENT";
    }

    @Override public String toString(){
        return "CUSTOMEVENT "+param;
    }
}
