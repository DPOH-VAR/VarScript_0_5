package me.dpohvar.varscript.converter;

import me.dpohvar.varscript.utils.ReflectObject;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.rule.ConvertRule;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 0:58
 */
public class Converter {

    HashMap<Class,TreeSet<ConvertRule>> classRules = new HashMap<Class, TreeSet<ConvertRule>>();

    public Class getClassForName(String name){
        try{
            return Class.forName(name);
        } catch (ClassNotFoundException ignored) {
        }
        for(Class c:classRules.keySet()){
            if(c.getSimpleName().equals(name)) return c;
        }
        for(Class c:classRules.keySet()){
            for(Class t:getAllSubClasses(c)){
                if(t.getSimpleName().equals(name)) return t;
            }
        }
        return null;
    }

    public String getNameForClass(Class clazz){
        for(Class c:classRules.keySet()){
            if(c == clazz) return c.getSimpleName();
        }
        for(Class c:classRules.keySet()){
            for(Class t:getAllSubClasses(c)){
                if(t==clazz) return c.getSimpleName();
            }
        }
        return clazz.getName();
    }

    public Set<Class> getAllSubClasses(Class clazz){
        HashSet<Class> classes = new HashSet<Class>();
        classes.add(clazz);
        Class superClass = clazz.getSuperclass();
        if(superClass!=null){
            classes.addAll(getAllSubClasses(superClass));
        }
        for(Class face:clazz.getInterfaces()){
            classes.addAll(getAllSubClasses(face));
        }
        return classes;
    }

    public <T> void addRule(ConvertRule<T> rule){
        TreeSet<ConvertRule> rules = classRules.get(rule.getClassTo());
        if(rules==null){
            rules = new TreeSet<ConvertRule>();
            classRules.put(rule.getClassTo(),rules);
        }
        rules.add(rule);
    }

    public <V,T>T convert(Class<T> classTo, V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws ConvertException {

        if (classTo == boolean.class) classTo = (Class<T>) Boolean.class; // try to boxing
        if (classTo == byte.class) classTo = (Class<T>) Byte.class; // try to boxing
        if (classTo == short.class) classTo = (Class<T>) Short.class; // try to boxing
        if (classTo == int.class) classTo = (Class<T>) Integer.class; // try to boxing
        if (classTo == long.class) classTo = (Class<T>) Long.class; // try to boxing
        if (classTo == float.class) classTo = (Class<T>) Float.class; // try to boxing
        if (classTo == double.class) classTo = (Class<T>) Double.class; // try to boxing
        if (classTo == char.class) classTo = (Class<T>) Character.class; // try to boxing
        if(classTo.isInstance(object)) return (T) object;
        Collection<ConvertRule> rules = classRules.get(classTo);
        if(rules!=null) for(ConvertRule rule:rules){
            try{
                return (T) rule.convert(object,thread,scope);
            } catch (NextRule ignored){}
        }
        if (object instanceof ReflectObject) {
            return convert(classTo, ((ReflectObject)object).getObject(), thread, scope);
        }
        try {
            Method m_values = classTo.getMethod("values");
            Enum[] values = (Enum[]) m_values.invoke(null);
            return (T) convert(values,object);
        } catch (Exception ignored) {
        }
        Class<? super T> superClass = classTo.getSuperclass();
        if(superClass!=null && superClass!=Object.class) try{
            return (T) convert(superClass, object, thread, scope);
        } catch (Exception ignored){
        }
        try{
            Class[] interfaces = classTo.getInterfaces();
            for(Class face:interfaces){
                try{
                    return (T) convert(face, object, thread, scope);
                } catch (Exception _IGNORED_){}
            }
        } catch (Exception ignored){
        }

        throw new ConvertException(object,classTo,"no variants for object");
    }



    public static <V> V convert(V[] examples,Object o){
        try {
            if (o instanceof Enum) o = ((Enum) o).ordinal();
            if (o instanceof Double) o = ((Double) o).intValue();
            if (o instanceof Integer) return examples[(Integer) o];
            V tra = null;
            if (o instanceof String) {
                for (int i = 0; i < examples.length; i++) {
                    String s = ((String) o).toUpperCase();
                    String e = examples[i].toString();
                    if (e.toUpperCase().equals(s)) return examples[i];
                    if (e.toUpperCase().startsWith(s)) tra = examples[i];
                    if (s.equals(((Integer) i).toString())) tra = examples[i];
                }
                if (tra != null) return tra;
            }
        } catch (Throwable ignore) {
        }
        return null;
    }

}
