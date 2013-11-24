package me.dpohvar.varscript.converter;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:08
 */
public class ConvertException extends Exception {
    public <V, T> ConvertException(V obj, Class<T> classTo, String s) {
        super("can't convert " + (obj == null ? "null" : obj.getClass().getSimpleName()) + " to " + classTo.getSimpleName() + ": " + s);
    }
}
