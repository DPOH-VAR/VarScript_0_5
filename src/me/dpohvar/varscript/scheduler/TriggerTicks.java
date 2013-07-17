package me.dpohvar.varscript.scheduler;

import me.dpohvar.varscript.VarScript;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 2:09
 */
public class TriggerTicks implements VSTrigger<VSTrigger> {

    private TriggerRunner<VSTrigger> runner;
    private BukkitTask task;
    public TriggerTicks(long ticks, TriggerRunner<VSTrigger> runner){
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

    @Override public void handle(VSTrigger event) {
        runner.run(this);
    }
}
