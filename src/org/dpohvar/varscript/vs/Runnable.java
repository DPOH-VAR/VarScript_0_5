package org.dpohvar.varscript.vs;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 23.06.13
 * Time: 12:13
 */
public interface Runnable {

    public String getName();

    void runCommands(ThreadRunner threadRunner, Thread thread, Context context) throws Exception;

    public Fieldable getPrototype();

    public void setPrototype(Fieldable prototype);

    public Scope getDelegatedScope();
}
