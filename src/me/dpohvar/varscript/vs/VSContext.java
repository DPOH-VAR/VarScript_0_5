package me.dpohvar.varscript.vs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:44
 */
public class VSContext implements VSFieldable {
    private Object apply = this;
    private VSScope scope;
    int pointer = 0;
    public HashMap<String,Object> fields = new HashMap<String,Object>();
    public HashMap<String,Object> variables = new HashMap<String,Object>();
    private Object a,b,c,d,e,f;
    private final VSRunnable constructor;
    private VSFieldable proto;

    ////////////////////// GETTERS AND SETTERS FOR REGISTERS A...F //////////////////////
    public Object getRegisterA(){return a;} public void setRegisterA(Object val){a=val;}
    public Object getRegisterB(){return b;} public void setRegisterB(Object val){b=val;}
    public Object getRegisterC(){return c;} public void setRegisterC(Object val){c=val;}
    public Object getRegisterD(){return d;} public void setRegisterD(Object val){d=val;}
    public Object getRegisterE(){return e;} public void setRegisterE(Object val){e=val;}
    public Object getRegisterF(){return f;} public void setRegisterF(Object val){f=val;}
    /////////////////////////////////////////////////////////////////////////////////////

    public VSContext(VSRunnable function, Object apply) {
        this.scope = new VSSimpleScope(function.getDelegatedScope());
        this.proto = function.getPrototype();
        this.constructor = function;
        this.apply = apply;
    }

    public VSContext(VSRunnable function) {
        this.scope = new VSSimpleScope(function.getDelegatedScope());
        this.proto = function.getPrototype();
        this.constructor = function;
    }

    public Object getApply(){
        return apply;
    }

    public void runCommands(VSThreadRunner threadRunner, VSThread thread) throws Exception {
        constructor.runCommands(threadRunner, thread, this);
    }

    public int getPointer(){
        return pointer;
    }

    public void setPointer(int val){
        pointer = val;
    }

    public int nextPointer(){
        return pointer++;
    }

    public void jumpPointer(int val){
        pointer+=val;
    }

    public VSScope getScope(){
        return scope;
    }

    public void setScope(VSScope scope){
        this.scope = scope;
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
        else return "[Context]";
    }

}
