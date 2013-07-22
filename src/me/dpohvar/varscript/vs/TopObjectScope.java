package me.dpohvar.varscript.vs;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:37
 */
public class TopObjectScope implements Scope {
    private final Fieldable fieldable;
    private Scope nextScope;
    private HashMap<String,Object> constants;
    public TopObjectScope(Fieldable fieldable, Scope readonlyScope){
        this.fieldable = fieldable;
        this.nextScope = readonlyScope;
    }

    public Object getVar(String varName){
        if(constants.containsKey(varName)) return constants.get(varName);
        if(fieldable.hasField(varName)) return fieldable.getField(varName);
        if(nextScope!=null) return nextScope.getVar(varName);
        return null;
    }
    public TopObjectScope defineVar(String varName, Object value){
        fieldable.setField(varName,value);
        return this;
    }
    public TopObjectScope defineConst(String varName, Object value){
        if(constants==null) constants = new HashMap<String, Object>();
        constants.put(varName,value);
        return this;
    }
    public TopObjectScope setVar(String varName, Object value){
        if(constants!=null && constants.containsKey(varName)) return this;
        fieldable.setField(varName,value);
        return this;
    }
    public TopObjectScope delVar(String varName){
        fieldable.removeField(varName);
        return this;
    }
}
