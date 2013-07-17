package me.dpohvar.varscript.scheduler;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 2:09
 */
public class TriggerWait implements VSTrigger<VSTrigger> {

    private TriggerRunner<VSTrigger> runner;
    private final Runnable runnable;
    public boolean registeged = true;

    public TriggerWait(final long timeout,final TriggerRunner<VSTrigger> runner){
        this.runner = runner;
        runnable = new Runnable() {
            @Override public void run() {
                if (timeout!=0) synchronized (this){
                    try {
                        this.wait(timeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (registeged) handle(null);
            }
        };
        new Thread(runnable).start();
    }

    @Override public void unregister() {
        registeged=false;
        synchronized (runnable) {
            runnable.notify();
        }
    }

    @Override public void handle(VSTrigger event) {
        runner.run(this);
    }
}