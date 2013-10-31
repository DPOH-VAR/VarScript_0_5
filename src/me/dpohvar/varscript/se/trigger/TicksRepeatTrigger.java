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
public class TicksRepeatTrigger extends Trigger {

    private BukkitTask task;
    boolean registered = true;

    public TicksRepeatTrigger(SEProgram program, long delay, long period, final Runnable runner) {
        super(program);
        task = Bukkit.getScheduler().runTaskTimer(VarScript.instance, new Runnable() {
            @Override
            public void run() {
                try {
                    runner.run();
                } catch (Exception e) {
                    getProgram().getCaller().handleException(e);
                }
            }
        }, delay, period);
    }

    public void setUnregistered() {
        task.cancel();
        registered = false;
    }

    public boolean isRegistered() {
        return registered;
    }

}
