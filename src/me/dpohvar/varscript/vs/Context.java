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
public class Context implements Fieldable {
    private Object apply = this;
    private Scope scope;
    int pointer = 0;
    private HashMap<String,Object> fields = new HashMap<String,Object>();
    private Object a,b,c,d,e,f;
    private final Runnable constructor;
    private Fieldable proto;

    ////////////////////// GETTERS AND SETTERS FOR REGISTERS A...F //////////////////////////////////////////
    public Object getRegisterA(){return a;} public void setRegisterA(Object val){a=val;} // FUNCTION ON WHILE
    public Object getRegisterB(){return b;} public void setRegisterB(Object val){b=val;} // ITERATOR
    public Object getRegisterC(){return c;} public void setRegisterC(Object val){c=val;} // WHILE RESULT
    public Object getRegisterD(){return d;} public void setRegisterD(Object val){d=val;} // BOOLEAN FUN
    public Object getRegisterE(){return e;} public void setRegisterE(Object val){e=val;} // CALLER
    public Object getRegisterF(){return f;} public void setRegisterF(Object val){f=val;} // CALL NEW RESULT
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Context(Runnable function, Object apply) {
        this.scope = new SimpleScope(function.getDelegatedScope());
        this.proto = function.getPrototype();
        this.constructor = function;
        this.apply = apply;
    }

    public Context(Runnable function) {
        this.scope = new SimpleScope(function.getDelegatedScope());
        this.proto = function.getPrototype();
        this.constructor = function;
    }

    public Object getApply(){
        return apply;
    }

    public void runCommands(ThreadRunner threadRunner, Thread thread) throws Exception {
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

    public Scope getScope(){
        return scope;
    }

    public void setScope(Scope scope){
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
    public Runnable getConstructor(){
        return constructor;
    }

    @Override
    public Fieldable getProto(){
        return proto;
    }

    @Override
    public void setProto(Fieldable proto){
        this.proto = proto;
    }

    @Override public String toString(){
        if (constructor!=null) {
            String name = constructor.getName();
            if(name.isEmpty()) return "[Anonymous context]";
            return name;
        }
        else return "[Context]";
    }

}
