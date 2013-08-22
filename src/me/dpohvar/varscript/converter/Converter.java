package me.dpohvar.varscript.converter;

import me.dpohvar.varscript.converter.rule.ConvertRule;
import me.dpohvar.varscript.utils.reflect.NBTTagWrapper;
import me.dpohvar.varscript.utils.reflect.ReflectObject;
import me.dpohvar.varscript.vs.Scope;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 0:58
 */
public class Converter {

    public static Map<String, Class> classes = new HashMap<String, Class>();

    static {
        classes.put("int", int.class);
        classes.put("byte", byte.class);
        classes.put("short", short.class);
        classes.put("long", long.class);
        classes.put("double", double.class);
        classes.put("float", float.class);
        classes.put("char", char.class);
        classes.put("int[]", int[].class);
        classes.put("int!", int[].class);
        classes.put("byte[]", byte[].class);
        classes.put("byte!", byte[].class);
        classes.put("short[]", short[].class);
        classes.put("short!", short[].class);
        classes.put("long[]", long[].class);
        classes.put("long!", long[].class);
        classes.put("double[]", double[].class);
        classes.put("double!", double[].class);
        classes.put("float[]", float[].class);
        classes.put("float!", float[].class);
        classes.put("char[]", char[].class);
        classes.put("char!", char[].class);
        classes.put("Object[]", Object[].class);
        classes.put("Object!", Object[].class);
        classes.put("String", String.class);
        classes.put("Player", org.bukkit.entity.Player.class);
    }


    HashMap<Class, TreeSet<ConvertRule>> classRules = new HashMap<Class, TreeSet<ConvertRule>>();

    public Class getClassForName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignored) {
        }
        if (classes.containsKey(name)) return classes.get(name);
        for (Class c : classRules.keySet()) {
            if (c.getSimpleName().equals(name)) return c;
        }
        for (Class c : classRules.keySet()) {
            for (Class t : getAllSubClasses(c)) {
                if (t.getSimpleName().equals(name)) return t;
            }
        }
        return null;
    }

    public String getNameForClass(Class clazz) {
        for (Class c : classRules.keySet()) {
            if (c == clazz) return c.getSimpleName();
        }
        for (Class c : classRules.keySet()) {
            for (Class t : getAllSubClasses(c)) {
                if (t == clazz) return c.getSimpleName();
            }
        }
        return clazz.getName();
    }

    public Set<Class> getAllSubClasses(Class clazz) {
        HashSet<Class> classes = new HashSet<Class>();
        classes.add(clazz);
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            classes.addAll(getAllSubClasses(superClass));
        }
        for (Class face : clazz.getInterfaces()) {
            classes.addAll(getAllSubClasses(face));
        }
        return classes;
    }

    public <T> void addRule(ConvertRule<T> rule) {
        TreeSet<ConvertRule> rules = classRules.get(rule.getClassTo());
        if (rules == null) {
            rules = new TreeSet<ConvertRule>();
            classRules.put(rule.getClassTo(), rules);
        }
        rules.add(rule);
    }

    public <V, T> T convert(Class<T> classTo, V object, me.dpohvar.varscript.vs.Thread thread, Scope scope) throws ConvertException {

        if (classTo == boolean.class) classTo = (Class<T>) Boolean.class; // try to boxing
        if (classTo == byte.class) classTo = (Class<T>) Byte.class; // try to boxing
        if (classTo == short.class) classTo = (Class<T>) Short.class; // try to boxing
        if (classTo == int.class) classTo = (Class<T>) Integer.class; // try to boxing
        if (classTo == long.class) classTo = (Class<T>) Long.class; // try to boxing
        if (classTo == float.class) classTo = (Class<T>) Float.class; // try to boxing
        if (classTo == double.class) classTo = (Class<T>) Double.class; // try to boxing
        if (classTo == char.class) classTo = (Class<T>) Character.class; // try to boxing
        if (classTo.isInstance(object)) return (T) object;
        Collection<ConvertRule> rules = classRules.get(classTo);
        if (rules != null) for (ConvertRule rule : rules) {
            try {
                return (T) rule.convert(object, thread, scope);
            } catch (NextRule ignored) {
            }
        }
        if (object instanceof ReflectObject) {
            return convert(classTo, ((ReflectObject) object).getObject(), thread, scope);
        }
        if (object instanceof NBTTagWrapper) {
            return convert(classTo, ((NBTTagWrapper) object).getTag(), thread, scope);
        }
        try {
            Method m_values = classTo.getMethod("values");
            Object[] values = (Object[]) m_values.invoke(null);
            return (T) convert(values, object);
        } catch (Exception ignored) {
        }
        Class<? super T> superClass = classTo.getSuperclass();
        if (superClass != null && superClass != Object.class) try {
            return (T) convert(superClass, object, thread, scope);
        } catch (Exception ignored) {
        }
        try {
            Class[] interfaces = classTo.getInterfaces();
            for (Class face : interfaces) {
                try {
                    return (T) convert(face, object, thread, scope);
                } catch (Exception _IGNORED_) {
                }
            }
        } catch (Exception ignored) {
        }

        throw new ConvertException(object, classTo, "no variants for object");
    }


    public static <V> V convert(V[] examples, Object o) {
        try {
            if (o instanceof Enum) o = ((Enum) o).ordinal();
            if (o instanceof Double) o = ((Double) o).intValue();
            if (o instanceof Integer) return examples[(Integer) o];
            V tra = null;
            V example = null;
            if (o instanceof String) {
                for (int i = 0; i < examples.length; i++) {
                    String s = ((String) o).toUpperCase();
                    if (examples[i] == null) continue;
                    example = examples[i];
                    String e = example.toString();
                    if (e.toUpperCase().equals(s)) return example;
                    if (e.toUpperCase().startsWith(s)) tra = example;
                    if (s.equals(((Integer) i).toString())) tra = example;
                }
                if (tra != null) return tra;
                if (example == null) return null;
                Class<?> c = example.getClass();
                Method m;
                try {
                    m = c.getMethod("getByName", String.class);
                    return (V) m.invoke(null, o);
                } catch (NoSuchMethodException ignored) {
                }
            }
        } catch (Throwable ignore) {
            return null;
        }
        return null;
    }

}
