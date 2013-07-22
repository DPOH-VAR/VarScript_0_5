package me.dpohvar.varscript.scheduler.event;

import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskEvent;
import me.dpohvar.varscript.trigger.Trigger;
import me.dpohvar.varscript.trigger.TriggerRepeatDelay;
import me.dpohvar.varscript.trigger.TriggerRunner;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 0:47
 */
public class RepeatEvent extends TaskEvent {

    private final String param;
    private Trigger trigger;

    public RepeatEvent(Task task, String param){
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

        if(ss.length<1 ||ss.length>2) {
            error = true;
            return false;
        }
        try{
            long repeat = Long.parseLong(ss[0]);
            long delay = 0;
            if(ss.length==2) delay = Long.parseLong(ss[1]);
            final TaskEvent taskEvent = this;
            TriggerRunner<Trigger> runner = new TriggerRunner<Trigger>() {
                @Override public void run(Trigger t) {
                    HashMap<String,Object> environment = new HashMap<String, Object>();
                    environment.put("TaskEvent",taskEvent);
                    if(task.check(environment)) task.run(environment);
                }
            };
            trigger = new TriggerRepeatDelay(repeat,delay,runner);
            error = false;
            return true;
        } catch (Exception ignored){
            error = true;
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
        return "REPEAT";
    }

    @Override public String toString(){
        return "REPEAT "+param;
    }
}
