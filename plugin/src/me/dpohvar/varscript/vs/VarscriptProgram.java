package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.Program;
import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.utils.Module;
import me.dpohvar.varscript.utils.ModuleManager;
import me.dpohvar.varscript.utils.ScopeBindings;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:34
 */
public class VarscriptProgram implements Program, Iterable<Thread>, Fieldable {
    HashSet<Thread> threads = new HashSet<Thread>();
    private final Caller caller;
    private final Runtime runtime;
    private int pid = -1;
    private boolean finished = false;
    private Scope scope;
    private HashMap<String, Object> programEnvironment = new HashMap<String, Object>();
    Fieldable proto;
    Runnable constructor;
    private HashSet<Runnable> onFinishRun = new HashSet<Runnable>();

    public void onFinish(Runnable runnable) {
        if (isFinished()) {
            if (this.isFinished()) {
                startNewThread(runnable);
            }
        } else {
            onFinishRun.add(runnable);
        }
    }

    public Object require(String moduleName) {
        ModuleManager manager = runtime.getModuleManager();
        Module module = manager.getModule(null, moduleName);
        if (module == null) module = manager.reload(null, moduleName);
        if (module == null) return null;
        if (!module.isLoaded()) return null;
        return module.getLangModule();
    }

    public void onFinishRemove(Runnable runnable) {
        onFinishRun.remove(runnable);
    }

    private void startNewThread(Runnable runnable) {
        Thread t = new Thread(this);
        t.pushFunction(runnable, this);
        new ThreadRunner(t).runThreads();
        t.setFinished();
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "Program{" + caller.getInstance().toString() + "}";
    }

    public VarscriptProgram(Runtime runtime, final Caller caller, Map<String, Object> bindings) {
        this.caller = caller;
        this.runtime = runtime;
        if (bindings == null) bindings = caller.getFields();
        runtime.registerProgram(this);
        ScopeBindings scopeBindings = new ScopeBindings(bindings)
                .listen(runtime.getGlobalBindings())
                .listen(programEnvironment)
                .listen(runtime.getModuleBindings(null))
                .listen(bindings)
                .listen(runtime.getEngineBindings(null))
                .listen(runtime.getRuntimeBindings());
        this.scope = new SimpleScope(scopeBindings);
        Runnable function = new Function(null, "Function", scope);
        Runnable object = new Function(null, "FieldableObject", scope);
        Runnable thread = new Function(null, "FieldableObject", scope);
        Runnable reflect = new Function(null, "Reflect", scope);
        this.constructor = new Function(null, "Program", scope);
        this.proto = constructor.getPrototype();
        programEnvironment.put("Function", function);
        programEnvironment.put("FieldableObject", object);
        programEnvironment.put("Location", caller.getLocation());
        programEnvironment.put("Caller", caller);
        programEnvironment.put("Me", caller.getInstance());
        programEnvironment.put("[[Function]]", function);
        programEnvironment.put("[[FieldableObject]]", object);
        programEnvironment.put("[[Thread]]", thread);
        programEnvironment.put("[[Reflect]]", reflect);
    }

    @Override
    public Caller getCaller() {
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
        if (this.pid == -1 && pid >= 0) this.pid = pid;
    }

    @Override
    public boolean isFinished() {
        return finished || checkFinished();
    }

    boolean checkFinished() {
        for (Thread thread : threads) {
            if (!thread.isFinished()) return false;
        }
        threads.clear();
        setFinished();
        return true;
    }

    @Override
    public void setFinished() {
        finished = true;
        while (!onFinishRun.isEmpty()) {
            Runnable r = onFinishRun.iterator().next();
            onFinishRun.remove(r);
            startNewThread(r);
        }
        for (Thread thread : threads) {
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


    private HashMap<String, Object> fields = new HashMap<String, Object>();

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
        if ("constructor".equals(key)) return constructor;
        if (fields.containsKey(key)) return fields.get(key);
        if (proto == null) return null;
        return proto.getField(key);
    }

    @Override
    public void setField(String key, Object value) {
        fields.put(key, value);
    }

    @Override
    public void removeField(String key) {
        fields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        if ("constructor".equals(key)) return true;
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
        this.proto = proto;
    }
}
