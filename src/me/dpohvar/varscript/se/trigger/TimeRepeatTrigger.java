package me.dpohvar.varscript.se.trigger;

import me.dpohvar.varscript.se.SEProgram;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 14:33
 */
public class TimeRepeatTrigger implements Trigger {

    boolean registered = true;
    final Runnable runnable;
    final SEProgram program;

    public TimeRepeatTrigger(SEProgram program, final long delay, final Runnable runner) {
        this.program = program;
        runnable = new Runnable() {
            @Override
            public void run() {
                if (delay == 0) synchronized (this) {
                    try {
                        this.wait(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (registered) {
                    runner.run();
                    run();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void unregister() {
        registered = false;
        synchronized (runnable) {
            runnable.notify();
        }
        program.removeTrigger(this);
    }

    public boolean isRegistered() {
        return registered;
    }

}
