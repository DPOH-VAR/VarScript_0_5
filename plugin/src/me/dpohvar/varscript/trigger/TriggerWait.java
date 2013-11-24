package me.dpohvar.varscript.trigger;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 2:09
 */
public class TriggerWait implements Trigger<Trigger> {

    private TriggerRunner<Trigger> runner;
    private final Runnable runnable;
    public boolean registered = true;

    public TriggerWait(final long timeout, final TriggerRunner<Trigger> runner) {
        this.runner = runner;
        runnable = new Runnable() {
            @Override
            public void run() {
                if (timeout != 0) synchronized (this) {
                    try {
                        this.wait(timeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (registered) handle(null);
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void unregister() {
        registered = false;
        synchronized (runnable) {
            runnable.notify();
        }
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    @Override
    public void handle(Trigger event) {
        runner.run(this);
    }
}
