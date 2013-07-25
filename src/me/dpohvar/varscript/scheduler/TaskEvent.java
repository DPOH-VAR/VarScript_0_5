package me.dpohvar.varscript.scheduler;

import me.dpohvar.varscript.scheduler.event.BukkitEvent;
import me.dpohvar.varscript.scheduler.event.PluginLoadEvent;
import me.dpohvar.varscript.scheduler.event.RepeatEvent;
import me.dpohvar.varscript.scheduler.event.TaskLoadEvent;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 17.07.13
 * Time: 2:28
 */
public abstract class TaskEvent extends TaskEntry {

    protected TaskEvent(Task task){
        super(task,TaskEntryType.EVENT);
    }

    protected abstract boolean register();

    protected abstract boolean unregister();

    public static TaskEvent fromString(Task task,String s) {
        String type = s;
        String argument = null;
        if (s.contains(" ")){
            int ind = s.indexOf(' ');
            type = s.substring(0,ind);
            argument = s.substring(ind+1,s.length());
        }
        if("BUKKIT".equals(type)){
            return new BukkitEvent(task,argument);
        } else if ("REPEAT".equals(type)){
            return new RepeatEvent(task,argument);
        } else if ("LOAD".equals(type)||"TASKLOAD".equals(type)){
            return new TaskLoadEvent(task,argument);
        } else if ("PLUGINLOAD".equals(type)){
            return new PluginLoadEvent(task,argument);
        }
        return new TaskEventError(task,s);
    }
}

class TaskEventError extends TaskEvent{
    final String param;
    protected TaskEventError(Task task,String param) {
        super(task);
        this.param = param;
        this.error = true;
    }

    @Override public String toString(){
        return param;
    }

    @Override protected boolean register() {
        return false;
    }

    @Override protected boolean unregister() {
        return false;
    }
}
