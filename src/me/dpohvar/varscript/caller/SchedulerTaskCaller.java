package me.dpohvar.varscript.caller;

import me.dpohvar.varscript.scheduler.Task;
import org.bukkit.Bukkit;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class SchedulerTaskCaller extends Caller {

    protected Task action;

    SchedulerTaskCaller(Task action){
        this.action = action;
    }

    @Override public Task getInstance() {
        return action;
    }

    public void send(Object message){
        Bukkit.getLogger().info("["+action+"] "+message);
    }
}
