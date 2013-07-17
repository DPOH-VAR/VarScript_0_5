package me.dpohvar.varscript.vs.converter;

import me.dpohvar.varscript.utils.ReflectObject;
import me.dpohvar.varscript.vs.VSScope;
import me.dpohvar.varscript.vs.VSThread;
import me.dpohvar.varscript.vs.converter.rule.ConvertRule;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 0:58
 */
public class Converter {

    HashMap<Class,TreeSet<ConvertRule>> classRules = new HashMap<Class, TreeSet<ConvertRule>>();

    public <T> void addRule(ConvertRule<T> rule){
        TreeSet<ConvertRule> rules = classRules.get(rule.getClassTo());
        if(rules==null){
            rules = new TreeSet<ConvertRule>();
            classRules.put(rule.getClassTo(),rules);
        }
        rules.add(rule);
    }

    public <V,T>T convert(Class<T> classTo, V object,VSThread thread,VSScope scope) throws ConvertException {

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
        if (classTo.isEnum()) try {
            Enum[] values;
            Method m_values = classTo.getMethod("values");
            values = (Enum[]) m_values.invoke(null);
            return (T) convert(values,object);
        } catch (Exception e) {
            return null;
        } else {
            Class<? super T> superClass = classTo.getSuperclass();
            if(superClass!=null && superClass!=Object.class) try{
                return (T) convert(superClass, object, thread, scope);
            } catch (Exception ignored){
                Class[] interfaces = classTo.getInterfaces();
                for(Class face:interfaces){
                    try{
                        return (T) convert(superClass, object, thread, scope);
                    } catch (Exception _IGNORED_){}
                }
            }
        }
        throw new ConvertException(object,classTo,"no variants for object");
    }



    public static <V extends Enum> V convert(V[] enums,Object o){
        try {
            if (o instanceof Enum) o = ((Enum) o).ordinal();
            if (o instanceof Double) o = ((Double) o).intValue();
            if (o instanceof Integer) return enums[(Integer) o];
            V tra = null;
            if (o instanceof String) {
                for (int i = 0; i < enums.length; i++) {
                    String s = ((String) o).toUpperCase();
                    String e = enums[i].toString();
                    if (e.toUpperCase().equals(s)) return enums[i];
                    if (e.toUpperCase().startsWith(s)) tra = enums[i];
                    if (s.equals(((Integer) i).toString())) tra = enums[i];
                }
                if (tra != null) return tra;
            }
        } catch (Throwable ignore) {
        }
        return null;
    }

}
