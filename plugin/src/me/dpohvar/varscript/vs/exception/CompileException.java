package me.dpohvar.varscript.vs.exception;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 25.06.13
 * Time: 2:12
 */
public class CompileException extends SourceException {

    public CompileException(String string, int row, int col, Throwable exception) {
        super(string, row, col, exception);
    }

    @Override
    public String getMessage() {
        return getCause().getMessage() + " at [" + row + ':' + col + "]\n" + getErrorString();
    }

}
