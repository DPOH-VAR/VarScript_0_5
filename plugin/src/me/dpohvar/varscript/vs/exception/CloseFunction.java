package me.dpohvar.varscript.vs.exception;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 02.07.13
 * Time: 21:30
 */
public class CloseFunction extends SourceException {
    public CloseFunction(String source, int row, int col) {
        super(source, row, col, new RuntimeException("unexpected end of function"));
    }
}
