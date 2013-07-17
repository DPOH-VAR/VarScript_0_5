package me.dpohvar.varscript.vs;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 23.06.13
 * Time: 12:13
 */
public interface VSRunnable {

    public String getName();

    void runCommands(VSThreadRunner threadRunner, VSThread thread, VSContext vsContext) throws Exception;

    public VSFieldable getPrototype();

    public void setPrototype(VSFieldable prototype);

    public VSScope getDelegatedScope();
}
