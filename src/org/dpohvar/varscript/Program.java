package org.dpohvar.varscript;

import org.dpohvar.varscript.caller.Caller;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 0:58
 */
public interface Program {
    public int getPID();

    public void setPID(int pid);

    public boolean isFinished();

    public Caller getCaller();

    public Runtime getRuntime();

    public void setFinished();
}
