package me.dpohvar.varscript.vs;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:37
 */
public class VSTopObjectScope implements VSScope {
    private final VSFieldable fieldable;
    private VSScope nextScope;
    private HashMap<String,Object> constants;
    public VSTopObjectScope(VSFieldable fieldable, VSScope readonlyScope){
        this.fieldable = fieldable;
        this.nextScope = readonlyScope;
    }

    public VSFieldable getObject(){
        return fieldable;
    }

    public Object getVar(String varName){
        if(constants.containsKey(varName)) return constants.get(varName);
        if(fieldable.hasField(varName)) return fieldable.getField(varName);
        if(nextScope!=null) return nextScope.getVar(varName);
        return null;
    }
    public VSTopObjectScope defineVar(String varName, Object value){
        fieldable.setField(varName,value);
        return this;
    }
    public VSTopObjectScope defineConst(String varName, Object value){
        if(constants==null) constants = new HashMap<String, Object>();
        constants.put(varName,value);
        return this;
    }
    public VSTopObjectScope setVar(String varName, Object value){
        if(constants!=null && constants.containsKey(varName)) return this;
        fieldable.setField(varName,value);
        return this;
    }
    public VSTopObjectScope delVar(String varName){
        fieldable.removeField(varName);
        return this;
    }
}
