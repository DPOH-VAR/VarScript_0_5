package me.dpohvar.varscript.utils.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 23.07.13
 * Time: 2:01
 */
public class ReflectUtils {
    public static Object getField(Object o,String field){
        try{
            Field f;
            try{
                f = o.getClass().getField(field);
            }catch (NoSuchFieldException ignored){
                f = o.getClass().getDeclaredField(field);
            }
            f.setAccessible(true);
            return f.get(o);
        } catch (Throwable ignored){
        }
        return null;
    }
    public static boolean setField(Object o,String field,Object value){
        try{
            Field f;
            try{
                f = o.getClass().getField(field);
            }catch (NoSuchFieldException ignored){
                f = o.getClass().getDeclaredField(field);
            }
            f.setAccessible(true);
            f.set(o,value);
            return true;
        } catch (Throwable ignored){
        }
        return false;
    }
    public static Object callMethod(Object o,String method,Class[] classes,Object... params){
        try{
            Method m;
            try{
                if(classes==null) m=o.getClass().getMethod(method);
                else m = o.getClass().getMethod(method,classes);
            }catch (NoSuchMethodException ignored){
                if(classes==null) m=o.getClass().getDeclaredMethod(method);
                else m = o.getClass().getDeclaredMethod(method,classes);
            }
            m.setAccessible(true);
            return m.invoke(o,params);
        } catch (Throwable ignored){
        }
        return false;
    }

    public static<T> T callConstructor(Class<T> clazz,Class[] classes,Object... params){
        try{
            Constructor<T> c;
            try{
                if(classes==null) c = clazz.getConstructor();
                else c = clazz.getConstructor(classes);
            }catch (NoSuchMethodException ignored){
                if(classes==null) c = clazz.getDeclaredConstructor();
                else c = clazz.getDeclaredConstructor(classes);
            }
            c.setAccessible(true);
            return c.newInstance(params);
        } catch (Throwable ignored){
        }
        return null;
    }
}
