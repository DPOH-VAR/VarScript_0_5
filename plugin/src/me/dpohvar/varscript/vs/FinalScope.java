package me.dpohvar.varscript.vs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:37
 */
public class FinalScope extends SimpleScope {

    public FinalScope() {
        this(null, new HashMap<String, Object>(), null);
    }

    public FinalScope(Scope nextScope) {
        this(nextScope, new HashMap<String, Object>(), null);
    }

    public FinalScope(Map<String, Object> variables) {
        this(null, variables, null);
    }

    public FinalScope(Scope nextScope, Map<String, Object> variables) {
        this(nextScope, variables, null);
    }

    public FinalScope(Scope nextScope, Map<String, Object> variables, Map<String, Object> constants) {
        this.nextScope = nextScope;
        this.variables = variables;
        this.constants = constants;
    }

    public FinalScope setVar(String varName, Object value) {
        return (FinalScope) defineVar(varName, value);
    }
}
