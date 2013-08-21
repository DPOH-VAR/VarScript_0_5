package org.dpohvar.varscript.vs;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:37
 */
public interface Scope {
    public Object getVar(String varName);

    public Scope defineVar(String varName, Object value);

    public Scope defineConst(String varName, Object value);

    public Scope setVar(String varName, Object value);

    public Scope delVar(String varName);
}
