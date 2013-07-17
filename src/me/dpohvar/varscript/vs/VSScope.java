package me.dpohvar.varscript.vs;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:37
 */
public interface VSScope {
    public Object getVar(String varName);
    public VSScope defineVar(String varName, Object value);
    public VSScope defineConst(String varName, Object value);
    public VSScope setVar(String varName, Object value);
    public VSScope delVar(String varName);
}
