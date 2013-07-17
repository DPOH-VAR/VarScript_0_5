package me.dpohvar.varscript.scheduler;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 2:05
 */
public interface TriggerRunner<T> {
    public void run(T some);
}
