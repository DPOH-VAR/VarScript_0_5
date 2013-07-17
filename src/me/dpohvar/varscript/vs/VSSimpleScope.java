package me.dpohvar.varscript.vs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:37
 */
public class VSSimpleScope implements VSScope {
    protected VSScope nextScope;
    protected Map<String,Object> variables = new HashMap<String, Object>();
    protected Map<String,Object> constants;

    public VSSimpleScope(){
        this(null, new HashMap<String, Object>(),null);
    }
    public VSSimpleScope(VSScope nextScope){
        this(nextScope, new HashMap<String, Object>(),null);
    }
    public VSSimpleScope(Map<String, Object> variables){
        this(null,variables, null);
    }
    public VSSimpleScope(VSScope nextScope, Map<String, Object> variables){
        this(nextScope,variables, null);
    }
    public VSSimpleScope(VSScope nextScope, Map<String, Object> variables, Map<String, Object> constants){
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
    public VSSimpleScope defineVar(String varName, Object value){
        if(constants!=null && constants.containsKey(varName)) return this;
        variables.put(varName,value);
        return this;
    }
    public VSSimpleScope defineConst(String varName, Object value){
        if(constants==null) constants = new HashMap<String, Object>();
        constants.put(varName,value);
        return this;
    }
    public VSSimpleScope setVar(String varName, Object value){
        if(constants!=null && constants.containsKey(varName)) return this;
        else if(variables.containsKey(varName) || nextScope==null) variables.put(varName,value);
        else nextScope.setVar(varName,value);
        return this;
    }
    public VSSimpleScope delVar(String varName){
        variables.remove(varName);
        return this;
    }
}
