package me.dpohvar.varscript.utils;

import me.dpohvar.varscript.vs.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 7:12
 */
public class ReflectObject implements VSFieldable{

    private final Object object;
    private final Class clazz;
    private final VSScope scope;
    private VSRunnable constructor;
    private VSFieldable proto;
    public ReflectObject(Object object,VSScope scope){
        this.object = object;
        this.clazz = object.getClass();
        this.scope = scope;
        try{
            this.constructor = (VSRunnable) scope.getVar("[[Reflect]]");
            this.proto = constructor.getPrototype();
        } catch (Exception ignored){
            proto = new VSObject(scope);
        }
    }

    public Object getObject(){
        return object;
    }

    @Override public String toString(){
        return object.toString();
    }

    @Override public Set<String> getAllFields() {
        HashSet<String> names = new HashSet<String>();
        for(Method m:methodsAll(clazz)){
            String methodName = m.getName();
            String name = methodName;
            int t = 0;
            while(names.contains(name)){
                name=methodName+"/"+(++t);
            }
            names.add(name);
        }
        for(Field f:clazz.getDeclaredFields())names.add(f.getName());
        for(Field f:clazz.getFields())names.add(f.getName());
        return names;
    }

    private Field getReflectField(String key){
        for(Field f: clazz.getDeclaredFields()){
            if(f.getName().equals(key)) {
                try {
                    f.setAccessible(true);
                    return f;
                } catch (Throwable ignored){
                    return null;
                }
            }
        }
        for(Field f: clazz.getFields()){
            if(f.getName().equals(key)) {
                try {
                    f.setAccessible(true);
                    return f;
                } catch (Throwable ignored){
                    return null;
                }
            }
        }
        return null;
    }

    @Override public Object getField(String key) {
        int diver = 0;
        if(key!=null) if (key.contains("/")) {
            String[] t = key.split("/");
            if(t.length==2){
                key=t[0];
                try{
                    diver = Integer.parseInt(t[1]);
                } catch (Exception ignored){
                }
            }
        }
        Field f = getReflectField(key);
        if (f!=null) try {
            f.setAccessible(true);
            return f.get(object);
        } catch (Throwable ignored){
            return null;
        }
        ArrayList<Method> methods = methodsByName(clazz,key);
        try{
            Method m = methods.get(diver);
            m.setAccessible(true);
            return new ReflectRunnable(methods.get(diver),scope);
        } catch (Exception ignored){
            return null;
        }
    }

    private static ArrayList<Method> methodsByName(Class clazz,String name){
        ArrayList<Method> methods = new ArrayList<Method>();
        for(Method m:clazz.getMethods()){
            if(m.getName().equals(name)) {
                methods.add(m);
            }
        }
        for(Method m:clazz.getDeclaredMethods()){
            if(m.getName().equals(name)) {
                if(!methods.contains(m)) methods.add(m);
            }
        }
        return methods;
    }

    public static ArrayList<Method> methodsAll(Class clazz){
        ArrayList<Method> methods = new ArrayList<Method>();
        Collections.addAll(methods, clazz.getMethods());
        for(Method m:clazz.getDeclaredMethods()){
            if(!methods.contains(m)) methods.add(m);
        }
        return methods;
    }

    HashMap<String,Object> additionalFields = new HashMap<String, Object>();
    @Override public void setField(String key, Object value) {
        Field f = getReflectField(key);
        if (f!=null) try {
            f.setAccessible(true);
            f.set(object,value);
            return;
        } catch (IllegalAccessException ignored) {
        }
        additionalFields.put(key,value);
    }

    @Override public void removeField(String key) {
        additionalFields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        if(additionalFields.containsKey(key)) return true;
        //todo: simplify
        Set<String> names = getAllFields();
        if(names.contains(key)) return true;
        return false;
    }

    @Override public VSRunnable getConstructor() {
        return constructor;
    }

    @Override public VSFieldable getProto() {
        return proto;
    }

    @Override public void setProto(VSFieldable proto) {
        this.proto = proto;
    }
}
