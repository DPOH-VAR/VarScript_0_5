package me.dpohvar.varscript.utils;

import me.dpohvar.varscript.vs.*;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 7:15
 */
public class ReflectRunnable implements VSRunnable{
    private final Method method;
    private final VSScope scope;
    public ReflectRunnable(Method method,VSScope scope){
        this.method = method;
        this.scope = scope;
    }
    @Override public String toString(){return method.toString();}
    @Override public String getName() {
        return method.getName();
    }
    @Override public void runCommands(VSThreadRunner threadRunner, VSThread thread, VSContext vsContext) throws Exception {
        Class[] types = method.getParameterTypes();
        Object[] pops = new Object[types.length];
        Object[] params = new Object[types.length];
        for(int i=params.length-1;i>=0;i--){
            pops[i]=thread.pop();
        }
        for(int i=0;i<params.length;i++){
            params[i]=thread.convert(types[i], pops[i]);
        }
        Object apply = vsContext.getApply();
        if (apply instanceof ReflectObject) apply = ((ReflectObject) apply).getObject();
        Object result = method.invoke(apply,params);
        if(method.getReturnType() != void.class) thread.push(result);
    }

    @Override public VSFieldable getPrototype() {
        return null;
    }

    @Override public void setPrototype(VSFieldable prototype) {
    }

    @Override public VSScope getDelegatedScope() {
        return scope;
    }
}
