package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.exception.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:33
 */
public abstract class VSWorker<T> {

    public static final InterruptFunction interruptFunction = new InterruptFunction();
    public static final InterruptRunner interruptRunner = new InterruptRunner();
    public static final InterruptThread interruptThread = new InterruptThread();
    public static final StopFunction stopFunction = new StopFunction();
    public static final StopThread stopThread = new StopThread();

    public abstract void run(VSThreadRunner r, VSThread v, VSContext f, T d) throws Exception;

    public abstract void save(OutputStream out, T data) throws IOException;

    public abstract byte[] getBytes();

    public abstract T readObject(InputStream input,VSCompiler.ReadSession readSession) throws IOException;
}
