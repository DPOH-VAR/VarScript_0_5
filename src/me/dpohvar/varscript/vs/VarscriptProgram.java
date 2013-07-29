package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.trigger.Trigger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:34
 */
public class VarscriptProgram implements me.dpohvar.varscript.Program, Iterable<Thread>, Fieldable {
    HashSet<Thread> threads = new HashSet<Thread>();
    private final Caller caller;
    private final Runtime runtime;
    private int pid = -1;
    private boolean finished = false;
    private Scope scope;
    Fieldable proto;
    Runnable constructor;

    public Scope getScope(){
        return scope;
    }

    @Override public String toString(){
        return "Program{"+caller.getInstance().toString()+"}";
    }

    public VarscriptProgram(Runtime runtime, final Caller caller) {
        this.caller = caller;
        this.runtime = runtime;
        runtime.registerProgram(this);
        FinalScope finalScope = new FinalScope(runtime,caller.getFields());
        this.scope = new SimpleScope(finalScope);
        Runnable function = new Function(null,"Function",scope);
        Runnable object = new Function(null,"FieldableObject",scope);
        Runnable thread = new Function(null,"FieldableObject",scope);
        Runnable reflect = new Function(null,"Reflect",scope);
        this.constructor = new Function(null,"Program",scope);
        this.proto = constructor.getPrototype();
        scope.defineVar("Function",function);
        scope.defineVar("FieldableObject",object);
        scope.defineVar("Location",caller.getLocation());
        scope.defineVar("Caller", caller);
        scope.defineVar("Me", caller.getInstance());
        scope.defineConst("[[Function]]", function);
        scope.defineConst("[[FieldableObject]]", object);
        scope.defineConst("[[Thread]]", thread);
        scope.defineConst("[[Reflect]]", reflect);
    }

    @Override
    public Caller getCaller(){
        return caller;
    }

    @Override
    public Runtime getRuntime() {
        return runtime;
    }

    @Override
    public int getPID() {
        return pid;
    }

    @Override
    public void setPID(int pid) {
        if(this.pid==-1 && pid>=0) this.pid=pid;
    }

    @Override
    public boolean isFinished(){
        return finished || checkFinished();
    }

    boolean checkFinished(){
        for(Thread thread:threads){
            if (!thread.isFinished()) return false;
            // TODO: <DEBUG>
            else System.out.print("THREAD IS NOT REMOVED:" + thread);
            // </DEBUG>
        }
        threads.clear();
        setFinished();
        return true;
    }

    @Override
    public void setFinished(){
        finished = true;
        for(Thread thread:threads) {
            thread.finished = true;
            thread.clearTriggers();
        }
        threads.clear();
        runtime.removeProgram(this.pid);
    }


    @Override
    public Iterator<Thread> iterator() {
        return new Iterator<Thread>() {
            public Iterator<Thread> iterator = threads.iterator();
            Thread temp = null;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Thread next() {
                temp = iterator().next();
                return temp;
            }

            @Override
            public void remove() {
                iterator().remove();
                temp.setFinished();
            }
        };
    }


    private HashMap<String,Object> fields = new HashMap<String, Object>();
    @Override
    public Set<String> getAllFields() {
        HashSet<String> names = new HashSet<String>();
        names.addAll(fields.keySet());
        names.add("constructor");
        if (proto != null) names.addAll(proto.getAllFields());
        return names;
    }

    @Override
    public Object getField(String key) {
        if("constructor".equals(key)) return constructor;
        if(fields.containsKey(key)) return fields.get(key);
        if (proto==null) return null;
        return proto.getField(key);
    }

    @Override
    public void setField(String key, Object value) {
        fields.put(key,value);
    }

    @Override
    public void removeField(String key) {
        fields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        if("constructor".equals(key)) return true;
        return fields.containsKey(key);
    }

    @Override
    public Runnable getConstructor() {
        return constructor;
    }

    @Override
    public Fieldable getProto() {
        return proto;
    }

    @Override
    public void setProto(Fieldable proto) {
        this.proto=proto;
    }
}
