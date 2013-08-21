package org.dpohvar.varscript.utils.reflect;

import org.apache.commons.lang.StringUtils;
import org.dpohvar.varscript.vs.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 7:15
 */
public class ReflectClass implements org.dpohvar.varscript.vs.Runnable, Fieldable {
    private final Class<?> clazz;
    private final Constructor<?> con;
    private final Scope scope;
    private org.dpohvar.varscript.vs.Runnable constructor;
    private Fieldable proto;

    public Class getInnerClass() {
        return clazz;
    }

    public ReflectClass(String key, Scope scope) throws Exception {
        this.scope = scope;
        try {
            this.constructor = (org.dpohvar.varscript.vs.Runnable) scope.getVar("[[Class]]");
            this.proto = constructor.getPrototype();
        } catch (Exception ignored) {
            proto = new FieldableObject(scope);
        }

        String name = null;
        String[] args = null;
        if (key.matches("[^()]+")) {
            name = key;
        } else if (key.matches("[^()]+\\([^()]*\\)")) {
            int open = key.indexOf('(');
            name = key.substring(0, open);
            String line = key.substring(open + 1, key.length() - 1);
            if (line.isEmpty()) args = new String[0];
            else args = line.split(",");
        }
        clazz = Class.forName(name);
        Constructor<?> tConstructor = null;
        if (args == null) {
            try {
                tConstructor = clazz.getConstructor();
            } catch (Exception ignored) {
            }
            if (tConstructor == null) try {
                tConstructor = clazz.getDeclaredConstructor();
            } catch (Exception ignored) {
            }
            if (tConstructor == null) try {
                tConstructor = clazz.getConstructors()[0];
            } catch (Exception ignored) {
            }
            if (tConstructor == null) try {
                tConstructor = clazz.getDeclaredConstructors()[0];
            } catch (Exception ignored) {
            }
        } else {
            List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();
            constructors.addAll(Arrays.asList(clazz.getConstructors()));
            constructors.addAll(Arrays.asList(clazz.getDeclaredConstructors()));
            find_constructor:
            for (Constructor<?> c : constructors) {
                Class[] params = c.getParameterTypes();
                if (params.length != args.length) continue;
                for (int i = 0; i < params.length; i++) {
                    if (!params[i].getName().endsWith(args[i])) continue find_constructor;
                }
                tConstructor = c;
                break;
            }
            if (tConstructor == null) throw new Exception("constructor not found for " + key);
        }
        con = tConstructor;
        if (con != null) con.setAccessible(true);
    }

    public Constructor getCon() {
        return con;
    }

    public ReflectClass(Constructor con, Scope scope) {
        this.scope = scope;
        this.clazz = con.getDeclaringClass();
        this.con = con;
        con.setAccessible(true);
    }

    @Override
    public String toString() {
        if (con == null || con.getParameterTypes().length == 0) {
            return clazz.getSimpleName() + "{}";
        } else {
            Class[] params = con.getParameterTypes();
            List<String> args = new ArrayList<String>();
            for (Class c : params) args.add(c.getSimpleName());
            return clazz.getSimpleName() + '(' + StringUtils.join(args, ',') + "){}";
        }
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public void runCommands(ThreadRunner threadRunner, org.dpohvar.varscript.vs.Thread thread, Context context) throws Exception {
        java.lang.Object result;
        if (con == null) {
            result = clazz.newInstance();
        } else {
            Class[] types = con.getParameterTypes();
            java.lang.Object[] pops = new java.lang.Object[types.length];
            java.lang.Object[] params = new java.lang.Object[types.length];
            for (int i = params.length - 1; i >= 0; i--) {
                pops[i] = thread.pop();
            }
            for (int i = 0; i < params.length; i++) {
                params[i] = thread.convert(types[i], pops[i]);
            }
            result = con.newInstance(params);
        }
        Context topContext = (Context) context.getRegisterE();
        if (topContext == null) return;
        topContext.setRegisterF(result);
    }

    @Override
    public Fieldable getPrototype() {
        return null;
    }

    @Override
    public void setPrototype(Fieldable prototype) {
    }

    @Override
    public Scope getDelegatedScope() {
        return scope;
    }

    private HashMap<String, java.lang.Object> additionalFields = new HashMap<String, java.lang.Object>();

    @Override
    public Set<String> getAllFields() {
        HashSet<String> fields = new HashSet<String>();
        for (Field f : clazz.getFields()) fields.add(f.getName());
        for (Field f : clazz.getDeclaredFields()) fields.add(f.getName());
        List<Method> methods = Arrays.asList(clazz.getMethods());
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

    @Override
    public java.lang.Object getField(String key) {
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
                    return f.get(null);
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

    @Override
    public void setField(String key, java.lang.Object value) {
        Field f = getReflectField(key);
        if (f != null) try {
            f.setAccessible(true);
            f.set(null, value);
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
    public org.dpohvar.varscript.vs.Runnable getConstructor() {
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

    private static ArrayList<Method> staticMethodsByName(Class clazz, String name) {
        ArrayList<Method> methods = new ArrayList<Method>();
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name)) {
                methods.add(m);
            }
        }
        for (Method m : clazz.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && m.getName().equals(name)) {
                if (!methods.contains(m)) methods.add(m);
            }
        }
        return methods;
    }
}
