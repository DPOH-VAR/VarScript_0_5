package org.dpohvar.varscript.vs;

import org.dpohvar.varscript.vs.compiler.VSCompiler;
import org.dpohvar.varscript.vs.exception.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:33
 */
public abstract class Worker<T> {

    public static final InterruptFunction interruptFunction = new InterruptFunction();
    public static final InterruptRunner interruptRunner = new InterruptRunner();
    public static final InterruptThread interruptThread = new InterruptThread();
    public static final StopFunction stopFunction = new StopFunction();
    public static final StopThread stopThread = new StopThread();

    public abstract void run(ThreadRunner r, Thread v, Context f, T d) throws Exception;

    public abstract void save(OutputStream out, T data) throws IOException;

    public abstract byte[] getBytes();

    public abstract T readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException;
}
