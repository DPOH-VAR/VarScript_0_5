package me.dpohvar.varscript.utils.reflect;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Runnable;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 7:15
 */
public class ReflectRunnable implements Runnable {
    private final Method method;
    private final Scope scope;
    private final String fullName;

    public ReflectRunnable(Method method, Scope scope, String fullName) {
        this.method = method;
        this.scope = scope;
        this.fullName = fullName;
        if (method != null) method.setAccessible(true);
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return fullName + "{}";
    }

    @Override
    public String getName() {
        return fullName;
    }

    @Override
    public void runCommands(ThreadRunner threadRunner, me.dpohvar.varscript.vs.Thread thread, Context context) throws Exception {
        Class[] types = method.getParameterTypes();
        Object[] pops = new Object[types.length];
        Object[] params = new Object[types.length];
        for (int i = params.length - 1; i >= 0; i--) {
            pops[i] = thread.pop();
        }
        for (int i = 0; i < params.length; i++) {
            params[i] = thread.convert(types[i], pops[i]);
        }
        Object apply = context.getApply();
        Object result;
        if (Modifier.isStatic(method.getModifiers())) {
            result = method.invoke(apply, params);
        } else {
            if (apply instanceof ReflectClass) {
                apply = null;
            } else {
                if (apply instanceof ReflectObject) apply = ((ReflectObject) apply).getObject();
                apply = thread.convert(method.getDeclaringClass(), apply);
            }
            result = method.invoke(apply, params);
        }
        if (method.getReturnType() != void.class) thread.push(result);
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
}
