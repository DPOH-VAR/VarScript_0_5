package me.dpohvar.varscript.trigger;

import me.dpohvar.varscript.VarScript;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 2:09
 */
public class TriggerDelay implements Trigger<Trigger> {

    private TriggerRunner<Trigger> runner;
    private BukkitTask task;
    public TriggerDelay(long ticks, TriggerRunner<Trigger> runner){
        this.runner = runner;
        task = Bukkit.getScheduler().runTaskLater(VarScript.instance,new Runnable() {
            @Override public void run() {
                handle(null);
            }
        },ticks);
    }

    @Override public void unregister() {
        task.cancel();
    }

    @Override public void handle(Trigger event) {
        runner.run(this);
    }
}
