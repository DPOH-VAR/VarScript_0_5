package me.dpohvar.varscript.utils.reflect;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.FieldableObject;
import me.dpohvar.varscript.vs.Runnable;
import me.dpohvar.varscript.vs.Thread;

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
public class ReflectClass implements Runnable,Fieldable {
    private final Class clazz;
    private final Constructor constr;
    private final Scope scope;
    private Runnable constructor;
    private Fieldable proto;
    int diver = 0;
    public Class getInnerClass(){
        return clazz;
    }
    public ReflectClass(String key, Scope scope) throws Exception{
        this.scope = scope;
        try{
            this.constructor = (Runnable) scope.getVar("[[Class]]");
            this.proto = constructor.getPrototype();
        } catch (Exception ignored){
            proto = new FieldableObject(scope);
        }

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
        this.clazz = Class.forName(key);
        Constructor temp = null;
        try{
            temp = allConstructors(clazz).get(diver);
        } catch (Exception ignored){
        }
        constr = temp;
    }

    public Constructor getConstr(){
        return constr;
    }

    public ReflectClass(Constructor constr, Scope scope) throws Exception{
        this.scope = scope;
        this.clazz = constr.getDeclaringClass();
        this.constr = constr;
    }

    @Override public String toString(){return clazz.getName()+(diver==0?"{}":("/"+diver+"{}"));}
    @Override public String getName() {
        return clazz.getSimpleName();
    }
    @Override public void runCommands(ThreadRunner threadRunner, Thread thread, Context context) throws Exception {
        Class[] types = constr.getParameterTypes();
        java.lang.Object[] pops = new java.lang.Object[types.length];
        java.lang.Object[] params = new java.lang.Object[types.length];
        for(int i=params.length-1;i>=0;i--){
            pops[i]=thread.pop();
        }
        for(int i=0;i<params.length;i++){
            params[i]=thread.convert(types[i], pops[i]);
        }
        java.lang.Object result = constr.newInstance(params);
        Stack<Context> runners = thread.getContextStack();
        if(runners.size()<2) return;
        Context topContext = runners.get(runners.size()-2);
        topContext.setRegisterF(result);
    }

    @Override public Fieldable getPrototype() {
        return null;
    }

    @Override public void setPrototype(Fieldable prototype) {
    }

    @Override public Scope getDelegatedScope() {
        return scope;
    }



    private static ArrayList<Constructor> allConstructors(Class clazz){
        ArrayList<Constructor> constructors = new ArrayList<Constructor>();
        Collections.addAll(constructors, clazz.getConstructors());
        for(Constructor m:clazz.getDeclaredConstructors()){
            if(!constructors.contains(m)) constructors.add(m);
        }
        return constructors;
    }


    HashMap<String, java.lang.Object> additionalFields = new HashMap<String, java.lang.Object>();
    @Override public Set<String> getAllFields() {
        HashSet<String> names = new HashSet<String>();
        for(Method m:ReflectObject.methodsAll(clazz)){
            if(Modifier.isStatic(m.getModifiers())){
                String methodName = m.getName();
                String name = methodName;
                int t = 0;
                while(names.contains(name)){
                    name=methodName+"/"+(++t);
                }
                names.add(name);
            }
        }
        for(Field f:clazz.getDeclaredFields()){
            if(Modifier.isStatic(f.getModifiers())) names.add(f.getName());
        }
        for(Field f:clazz.getFields()){
            if(Modifier.isStatic(f.getModifiers())) names.add(f.getName());
        }
        return names;
    }

    @Override public java.lang.Object getField(String key) {
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
        Field f = getStaticReflectField(key);
        if (f!=null) try {
            f.setAccessible(true);
            return f.get(null);
        } catch (Throwable ignored){
            return null;
        }
        ArrayList<Method> methods = staticMethodsByName(clazz, key);
        try{
            Method m = methods.get(diver);
            m.setAccessible(true);
            return new ReflectRunnable(methods.get(diver),scope,key);
        } catch (Exception ignored){
            return null;
        }
    }

    @Override public void setField(String key, java.lang.Object value) {
        Field f = getStaticReflectField(key);
        if (f!=null) try {
            f.setAccessible(true);
            f.set(null,value);
            return;
        } catch (IllegalAccessException ignored) {
        }
        additionalFields.put(key,value);
    }

    @Override
    public void removeField(String key) {
        additionalFields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        if(additionalFields.containsKey(key)) return true;
        //todo: simplify
        Set<String> names = getAllFields();
        if (names.contains(key)) return true;
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

    private Field getStaticReflectField(String key){
        for(Field f: clazz.getDeclaredFields()){
            if(Modifier.isStatic(f.getModifiers()) && f.getName().equals(key)) {
                try {
                    f.setAccessible(true);
                    return f;
                } catch (Throwable ignored){
                    return null;
                }
            }
        }
        for(Field f: clazz.getFields()){
            if(Modifier.isStatic(f.getModifiers()) && f.getName().equals(key)) {
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

    private static ArrayList<Method> staticMethodsByName(Class clazz,String name){
        ArrayList<Method> methods = new ArrayList<Method>();
        for(Method m:clazz.getMethods()){
            if(m.getName().equals(name)) {
                methods.add(m);
            }
        }
        for(Method m:clazz.getDeclaredMethods()){
            if(Modifier.isStatic(m.getModifiers()) && m.getName().equals(name)) {
                if(!methods.contains(m)) methods.add(m);
            }
        }
        return methods;
    }
}
