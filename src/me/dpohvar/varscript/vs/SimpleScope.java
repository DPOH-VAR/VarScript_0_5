package me.dpohvar.varscript.vs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:37
 */
public class SimpleScope implements Scope {
    protected Scope nextScope;
    protected Map<String,Object> variables = new HashMap<String, Object>();
    protected Map<String,Object> constants;

    public SimpleScope(){
        this(null, new HashMap<String, Object>(),null);
    }
    public SimpleScope(Scope nextScope){
        this(nextScope, new HashMap<String, Object>(),null);
    }
    public SimpleScope(Map<String, Object> variables){
        this(null,variables, null);
    }
    public SimpleScope(Scope nextScope, Map<String, Object> variables){
        this(nextScope,variables, null);
    }
    public SimpleScope(Scope nextScope, Map<String, Object> variables, Map<String, Object> constants){
        this.nextScope=nextScope;
        this.variables=variables;
        this.constants=constants;
    }

    public Object getVar(String varName){
        if(constants!=null && constants.containsKey(varName)) return constants.get(varName);
        if(variables.containsKey(varName)) return variables.get(varName);
        if(nextScope!=null) return nextScope.getVar(varName);
        return null;
    }
    public SimpleScope defineVar(String varName, Object value){
        if(constants!=null && constants.containsKey(varName)) return this;
        variables.put(varName,value);
        return this;
    }
    public SimpleScope defineConst(String varName, Object value){
        if(constants==null) constants = new HashMap<String, Object>();
        constants.put(varName,value);
        return this;
    }
    public SimpleScope setVar(String varName, Object value){
        if(constants!=null && constants.containsKey(varName)) return this;
        else if(variables.containsKey(varName) || nextScope==null) variables.put(varName,value);
        else nextScope.setVar(varName,value);
        return this;
    }
    public SimpleScope delVar(String varName){
        variables.remove(varName);
        return this;
    }
}
