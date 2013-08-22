package me.dpohvar.varscript.caller;

import me.dpohvar.varscript.VarScript;

import java.io.PrintStream;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:44
 */
public class PrintStreamCaller extends Caller {

    final private PrintStream instance;

    public PrintStreamCaller(PrintStream instance) {
        this.instance = instance;
    }

    @Override
    public PrintStream getInstance() {
        return instance;
    }

    @Override
    public void send(Object object) {
        instance.println(VarScript.prefix_normal + " " + object);
    }

    @Override
    protected void onHandleException(Throwable exception) {
        exception.printStackTrace(instance);
    }
}
