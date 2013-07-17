package me.dpohvar.varscript.vs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 30.06.13
 * Time: 1:10
 */
public class VSObject implements VSFieldable {

    HashMap<String,Object> fields = new HashMap<String,Object>();
    VSFieldable proto;
    VSRunnable constructor = null;

    public VSObject(VSScope scope){
        try{
            this.constructor = (VSRunnable) scope.getVar("[[Object]]");
            this.proto = constructor.getPrototype();
        } catch (Exception ignored){
        }
    }

    @Override public Set<String> getAllFields() {
        HashSet<String> names = new HashSet<String>();
        names.addAll(fields.keySet());
        names.add("constructor");
        if (proto != null) names.addAll(proto.getAllFields());
        return names;
    }

    @Override
    public Object getField(String key){
        if("constructor".equals(key)) return constructor;
        if(fields.containsKey(key)) return fields.get(key);
        if (proto == null) return null;
        return proto.getField(key);
    }

    @Override
    public void setField(String key,Object value){
        fields.put(key,value);
    }

    @Override
    public void removeField(String key){
        fields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        if("constructor".equals(key)) return true;
        return fields.containsKey(key);
    }

    @Override
    public VSRunnable getConstructor(){
        return constructor;
    }

    @Override
    public VSFieldable getProto(){
        return proto;
    }

    @Override
    public void setProto(VSFieldable proto){
        this.proto = proto;
    }

    @Override public String toString(){
        if (constructor!=null) return constructor.getName();
        else return "[Object]";
    }
}
