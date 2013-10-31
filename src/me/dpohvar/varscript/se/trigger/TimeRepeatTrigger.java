package me.dpohvar.varscript.se.trigger;

import me.dpohvar.varscript.se.SEProgram;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 14:33
 */
public class TimeRepeatTrigger extends Trigger {

    boolean registered = true;
    final Runnable runnable;

    public TimeRepeatTrigger(SEProgram program, final long delay, final Runnable runner) {
        super(program);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (delay != 0) synchronized (this) {
                    try {
                        this.wait(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (registered) {
                    try {
                        runner.run();
                    } catch (Exception e) {
                        getProgram().getCaller().handleException(e);
                    }
                    run();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void setUnregistered() {
        registered = false;
        synchronized (runnable) {
            runnable.notify();
        }
    }

    public boolean isRegistered() {
        return registered;
    }

}
