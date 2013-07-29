package me.dpohvar.varscript.scheduler.event;

import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskEvent;
import me.dpohvar.varscript.trigger.Trigger;
import me.dpohvar.varscript.trigger.TriggerBukkitEvent;
import me.dpohvar.varscript.trigger.TriggerRunner;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 0:47
 */
public class PluginLoadEvent extends TaskEvent {

    private final String param;
    private Trigger trigger;

    public PluginLoadEvent(Task task, String param){
        super(task);
        if(param==null) param="";
        this.param = param;
    }

    @Override protected boolean register() {
        if(trigger!=null)return true;
        try{
            EventPriority priority;
            if( param==null || param.isEmpty()) priority = EventPriority.NORMAL;
            else priority = EventPriority.valueOf(param);
            final TaskEvent taskEvent = this;
            TriggerRunner<PluginEnableEvent> runner = new TriggerRunner<PluginEnableEvent>() {
                @Override public void run(PluginEnableEvent t) {
                    HashMap<String,Object> environment = new HashMap<String, Object>();
                    environment.put("TaskEvent",taskEvent);
                    environment.put("Event",t);
                    if(task.check(environment)) task.run(environment);
                }
            };
            trigger = new TriggerBukkitEvent<PluginEnableEvent>(PluginEnableEvent.class, priority,runner);
            return true;
        } catch (Exception ignored){
            return false;
        }

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
        return "";
    }

    @Override public String toString(){
        return "PLUGINLOAD "+param;
    }
}
