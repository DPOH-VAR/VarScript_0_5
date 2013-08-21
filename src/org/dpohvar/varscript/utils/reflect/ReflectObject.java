package org.dpohvar.varscript.utils.reflect;

import org.apache.commons.lang.StringUtils;
import org.dpohvar.varscript.vs.Fieldable;
import org.dpohvar.varscript.vs.FieldableObject;
import org.dpohvar.varscript.vs.Runnable;
import org.dpohvar.varscript.vs.Scope;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 7:12
 */
public class ReflectObject implements Fieldable {

    protected final java.lang.Object object;
    protected final Class<?> clazz;
    protected final Scope scope;
    protected Runnable constructor;
    protected Fieldable proto;

    public ReflectObject(java.lang.Object object, Scope scope) {
        this.object = object;
        this.clazz = object.getClass();
        this.scope = scope;
        try {
            this.constructor = (Runnable) scope.getVar("[[Reflect]]");
            this.proto = constructor.getPrototype();
        } catch (Exception ignored) {
            proto = new FieldableObject(scope);
        }
    }

    public java.lang.Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return object.toString();
    }

    @Override
    public Set<String> getAllFields() {
        HashSet<String> fields = new HashSet<String>();
        for (Field f : clazz.getFields()) fields.add(f.getName());
        for (Field f : clazz.getDeclaredFields()) fields.add(f.getName());
        List<Method> methods = new ArrayList<Method>();
        methods.addAll(Arrays.asList(clazz.getMethods()));
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        for (Method m : methods) {
            Class[] params = m.getParameterTypes();
            if (params.length == 0) {
                fields.add(m.getName() + "()");
            } else {
                List<String> args = new ArrayList<String>();
                for (Class c : params) args.add(c.getName());
                fields.add(
                        m.getName() + '(' + StringUtils.join(args, ',') + ')'
                );
            }
        }
        return fields;
    }

    private Field getReflectField(String key) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.getName().equals(key)) {
                try {
                    f.setAccessible(true);
                    return f;
                } catch (Throwable ignored) {
                    return null;
                }
            }
        }
        for (Field f : clazz.getFields()) {
            if (f.getName().equals(key)) {
                try {
                    f.setAccessible(true);
                    return f;
                } catch (Throwable ignored) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public java.lang.Object getField(final String key) {
        String name;
        String[] args = null;
        if (key.matches("[A-Za-z0-9_\\-/]+")) {
            name = key;
        } else if (key.matches("[A-Za-z0-9_\\-]+\\([A-Za-z0-9_\\-,\\.]*\\)")) {
            int open = key.indexOf('(');
            name = key.substring(0, open);
            String line = key.substring(open + 1, key.length() - 1);
            if (line.isEmpty()) args = new String[0];
            else args = line.split(",");
        } else {
            return null;
        }
        if (args == null) {
            getField:
            {
                Field f;
                try {
                    f = clazz.getField(name);
                } catch (Exception e) {
                    try {
                        f = clazz.getDeclaredField(name);
                    } catch (Exception ignored) {
                        break getField;
                    }
                }
                try {
                    f.setAccessible(true);
                    return f.get(object);
                } catch (Exception ignored) {
                    return additionalFields.get(key);
                }
            }
            Method m = null;
            try {
                m = clazz.getMethod(name);
            } catch (NoSuchMethodException e) {
                try {
                    m = clazz.getDeclaredMethod(name);
                } catch (NoSuchMethodException ignored) {
                }
            }
            if (m != null) return new ReflectRunnable(m, scope, name);
            for (Method t : clazz.getMethods()) {
                if (t.getName().equals(name)) {
                    return new ReflectRunnable(t, scope, name);
                }
            }
            for (Method t : clazz.getDeclaredMethods()) {
                if (t.getName().equals(name)) {
                    return new ReflectRunnable(t, scope, name);
                }
            }
            return additionalFields.get(key);
        } else {
            List<Method> methods = new ArrayList<Method>();
            methods.addAll(Arrays.asList(clazz.getMethods()));
            methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            find_method:
            for (Method m : methods) {
                if (!m.getName().equals(name)) continue;
                Class[] params = m.getParameterTypes();
                if (params.length != args.length) continue;
                for (int i = 0; i < params.length; i++) {
                    if (!params[i].getName().endsWith(args[i])) continue find_method;
                }
                return new ReflectRunnable(m, scope, name);
            }
            return additionalFields.get(key);
        }
    }

    HashMap<String, java.lang.Object> additionalFields = new HashMap<String, java.lang.Object>();

    @Override
    public void setField(String key, java.lang.Object value) {
        Field f = getReflectField(key);
        if (f != null) try {
            f.setAccessible(true);
            f.set(object, value);
            return;
        } catch (IllegalAccessException ignored) {
        }
        additionalFields.put(key, value);
    }

    @Override
    public void removeField(String key) {
        additionalFields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        if (additionalFields.containsKey(key)) return true;
        try {
            return (clazz.getDeclaredField(key) != null);
        } catch (Exception ignored) {
        }
        try {
            String name;
            String[] args;
            if (key.matches("[A-Za-z0-9_\\-]+\\([A-Za-z0-9_\\-,\\.]*\\)")) {
                int open = key.indexOf('(');
                name = key.substring(0, open);
                String line = key.substring(open + 1, key.length() - 1);
                if (line.isEmpty()) args = new String[0];
                else args = line.split(",");
            } else {
                return false;
            }
            List<Method> methods = Arrays.asList(clazz.getMethods());
            methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            find_method:
            for (Method m : methods) {
                if (!m.getName().equals(name)) continue;
                Class[] params = m.getParameterTypes();
                if (params.length != args.length) continue;
                for (int i = 0; i < params.length; i++) {
                    if (!params[i].getName().endsWith(args[i])) continue find_method;
                }
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;

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
