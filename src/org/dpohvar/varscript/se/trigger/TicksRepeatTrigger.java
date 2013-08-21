package org.dpohvar.varscript.se.trigger;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.dpohvar.varscript.VarScript;
import org.dpohvar.varscript.se.SEProgram;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 14:33
 */
public class TicksRepeatTrigger implements Trigger {

    private BukkitTask task;
    boolean registered = true;
    final SEProgram program;

    public TicksRepeatTrigger(SEProgram program, long delay, long period, final Runnable runner) {
        this.program = program;
        task = Bukkit.getScheduler().runTaskTimer(VarScript.instance, new Runnable() {
            @Override
            public void run() {
                runner.run();
            }
        }, delay, period);
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
