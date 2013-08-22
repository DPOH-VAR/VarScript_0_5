package me.dpohvar.varscript.se.trigger;

import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.se.SEProgram;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 14:33
 */
public class TicksTrigger implements Trigger {

    private BukkitTask task;
    boolean registered = true;
    final SEProgram program;

    public TicksTrigger(SEProgram program, long ticks, final Runnable runner) {
        this.program = program;
        task = Bukkit.getScheduler().runTaskLater(VarScript.instance, new Runnable() {
            @Override
            public void run() {
                runner.run();
                unregister();
            }
        }, ticks);
    }

    public void unregister() {
        task.cancel();
        registered = false;
        program.removeTrigger(this);
    }

    public boolean isRegistered() {
        return registered;
    }

}
