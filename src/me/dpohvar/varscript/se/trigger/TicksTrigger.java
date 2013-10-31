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
public class TicksTrigger extends Trigger {

    private BukkitTask task;
    boolean registered = true;

    public TicksTrigger(SEProgram program, long ticks, final Runnable runner) {
        super(program);
        task = Bukkit.getScheduler().runTaskLater(VarScript.instance, new Runnable() {
            @Override
            public void run() {
                try {
                    runner.run();
                } catch (Exception e) {
                    getProgram().getCaller().handleException(e);
                }
                stop();
            }
        }, ticks);
    }

    public void setUnregistered() {
        task.cancel();
        registered = false;
    }

    public boolean isRegistered() {
        return registered;
    }

}
