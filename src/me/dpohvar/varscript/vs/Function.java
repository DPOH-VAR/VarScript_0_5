package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.vs.exception.RuntimeControl;
import me.dpohvar.varscript.vs.exception.StopFunction;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:48
 */
public class Function extends CommandList implements Runnable, Fieldable, Cloneable {

    Fieldable prototype;
    Runnable constructor = null;
    Fieldable proto;
    Scope scope;
    public boolean ignoreExceptions;

    public Function(List<Command> commands, String name, Scope delegateScope) {
        super(commands, name);
        this.scope = delegateScope;
        try {
            this.constructor = (Runnable) scope.getVar("[[Function]]");
            this.proto = constructor.getPrototype();
        } catch (Exception ignored) {
            proto = new FieldableObject(scope);
        }
        this.prototype = new FieldableObject(this.scope);
    }

    @Override
    public String toString() {
        return getName() + "{}";
    }

    @Override
    public Fieldable getPrototype() {
        return prototype;
    }

    @Override
    public void setPrototype(Fieldable prototype) {
        this.prototype = prototype;
    }

    @Override
    public Scope getDelegatedScope() {
        return scope;
    }

    public void runCommands(ThreadRunner threadRunner, Thread thread, Context runner) throws Exception {
        if (commands == null) return;
        if (ignoreExceptions) try {
            while (runner.getPointer() < commands.size()) {
                Command command = commands.get(runner.nextPointer());
                command.runWorker(threadRunner, thread, runner);
            }
        } catch (StopFunction ignored) {
        } catch (Throwable e) {
            if (e instanceof RuntimeControl) throw (RuntimeControl) e;
        }
        else try {
            while (runner.getPointer() < commands.size()) {
                Command command = commands.get(runner.nextPointer());
                command.runWorker(threadRunner, thread, runner);
            }
        } catch (StopFunction ignored) {
        }
    }


    public void save(OutputStream out) throws IOException {
        String name = this.name;
        if (name == null) name = "";
        byte[] bytes = name.getBytes(VarScript.UTF8);
        if (bytes.length > 254) {
            out.write(255);
            byte[] temp = new byte[4];
            ByteBuffer.wrap(temp).putInt(commands.size());
            out.write(bytes);
        } else {
            out.write(bytes.length);
        }
        out.write(bytes);
        if (commands.size() > 254) {
            byte[] temp = new byte[4];
            ByteBuffer.wrap(temp).putInt(commands.size());
            out.write(bytes);
        } else {
            out.write(commands.size());
        }
        for (Command command : commands) {
            command.save(out);
        }
    }

    HashMap<String, java.lang.Object> fields = new HashMap<String, java.lang.Object>();

    @Override
    public Set<String> getAllFields() {
        HashSet<String> names = new HashSet<String>();
        names.addAll(fields.keySet());
        names.add("prototype");
        names.add("constructor");
        if (proto != null) names.addAll(proto.getAllFields());
        return names;
    }

    @Override
    public java.lang.Object getField(String key) {
        if ("prototype".equals(key)) return prototype;
        if ("constructor".equals(key)) return constructor;
        if (fields.containsKey(key)) return fields.get(key);
        if (proto == null) return null;
        return proto.getField(key);
    }

    @Override
    public void setField(String key, java.lang.Object value) {
        fields.put(key, value);
    }

    @Override
    public void removeField(String key) {
        fields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        if ("prototype".equals(key)) return true;
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
