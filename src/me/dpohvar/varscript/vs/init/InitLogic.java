package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */

public class InitLogic {
    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "AND",
                "AND",
                "Boolean(A) Boolean(B)",
                "Boolean(A&&B)",
                "boolean",
                "boolean 'AND'",
                new SimpleWorker(new int[]{0x20}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Boolean a = v.pop(Boolean.class);
                        v.push(a&&b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "OR",
                "OR",
                "Boolean(A) Boolean(B)",
                "Boolean(A||B)",
                "boolean",
                "boolean 'OR'",
                new SimpleWorker(new int[]{0x21}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Boolean a = v.pop(Boolean.class);
                        v.push(a||b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "XOR",
                "XOR",
                "Boolean(A) Boolean(B)",
                "Boolean(A!=B)",
                "boolean",
                "boolean 'XOR'",
                new SimpleWorker(new int[]{0x22}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Boolean a = v.pop(Boolean.class);
                        v.push(a!=b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "XNOR",
                "XNOR",
                "Boolean(A) Boolean(B)",
                "Boolean(A==B)",
                "boolean",
                "boolean 'XNOR'",
                new SimpleWorker(new int[]{0x23}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Boolean a = v.pop(Boolean.class);
                        v.push(a==b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NOT",
                "NOT !",
                "Boolean(A)",
                "Boolean(not_A)",
                "boolean",
                "boolean 'NOT'",
                new SimpleWorker(new int[]{0x24}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean a = v.pop(Boolean.class);
                        v.push(!a);
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "TRUE",
                "TRUE",
                "",
                "Boolean(true)",
                "boolean",
                "put to stack boolean true",
                new SimpleWorker(new int[]{0x25}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(true);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FALSE",
                "FALSE",
                "",
                "Boolean(false)",
                "boolean",
                "put to stack boolean false",
                new SimpleWorker(new int[]{0x26}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(false);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "=",
                "=",
                "Object(A) Object(B)",
                "Boolean(A_equals_B)",
                "boolean",
                "compare objects",
                new SimpleWorker(new int[]{0x27}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object b = v.pop();
                        Object a = v.pop();
                        if(a==null&&b==null) {
                            v.push(true);
                        } else if((b==null) != (a==null)){
                            v.push(false);
                        } else if (a instanceof Number && b instanceof Number){
                            if (a instanceof Float || a instanceof Double || b instanceof Float || b instanceof Double){
                                double val_a = ((Number)a).doubleValue();
                                double val_b = ((Number)b).doubleValue();
                                v.push(val_a==val_b);
                            } else {
                                long val_a = ((Number)a).longValue();
                                long val_b = ((Number)b).longValue();
                                v.push(val_a==val_b);
                            }
                        } else if (a instanceof Enum || b instanceof Enum){
                            Enum e;
                            Object x;
                            if (a instanceof Enum) {
                                e=(Enum)a;
                                x=b;
                            } else {
                                e=(Enum)b;
                                x=a;
                            }
                            if (x instanceof Number){
                                v.push(e.ordinal()==((Number)x).intValue());
                            } else {
                                v.push(e.name().toUpperCase().equals(x.toString().toUpperCase()));
                            }
                        } else {
                            v.push( (a==b) || (a.equals(b)) || (b.equals(a)) );
                        }
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "==",
                "==",
                "Integer(a) Integer(b)",
                "Boolean",
                "boolean",
                "put to stack true if a==b",
                new SimpleWorker(new int[]{0x28}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer b = v.pop(Integer.class);
                        Integer a = v.pop(Integer.class);
                        if(a==null) v.push(b==null);
                        else v.push(a.equals(b));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                ">",
                ">",
                "Integer(a) Integer(b)",
                "Boolean",
                "boolean",
                "put to stack true if a>b",
                new SimpleWorker(new int[]{0x29}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer b = v.pop(Integer.class);
                        Integer a = v.pop(Integer.class);
                        if(a==null) v.push(false);
                        else v.push(a>b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                ">=",
                ">=",
                "Integer(a) Integer(b)",
                "Boolean",
                "boolean",
                "put to stack true if a>=b",
                new SimpleWorker(new int[]{0x2A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer b = v.pop(Integer.class);
                        Integer a = v.pop(Integer.class);
                        if(a==null) v.push(b==null);
                        else v.push(a>=b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "<",
                "<",
                "Integer(a) Integer(b)",
                "Boolean",
                "boolean",
                "put to stack true if a<b",
                new SimpleWorker(new int[]{0x2B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer b = v.pop(Integer.class);
                        Integer a = v.pop(Integer.class);
                        if(a==null) v.push(false);
                        else v.push(a<b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "<=",
                "<=",
                "Integer(a) Integer(b)",
                "Boolean",
                "boolean",
                "put to stack true if a<=b",
                new SimpleWorker(new int[]{0x2C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer b = v.pop(Integer.class);
                        Integer a = v.pop(Integer.class);
                        if(a==null) v.push(b==null);
                        else v.push(a<=b);
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "D=",
                "D=",
                "Double(a) Double(b)",
                "Boolean",
                "boolean",
                "put to stack true if a==b",
                new SimpleWorker(new int[]{0x2F,0x00}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double b = v.pop(Double.class);
                        Double a = v.pop(Double.class);
                        if(a==null) v.push(b==null);
                        else v.push(a.equals(b));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "D>",
                "D>",
                "Double(a) Double(b)",
                "Boolean",
                "boolean",
                "put to stack true if a>b",
                new SimpleWorker(new int[]{0x2F,0x01}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double b = v.pop(Double.class);
                        Double a = v.pop(Double.class);
                        if(a==null) v.push(false);
                        else v.push(a>b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "D>=",
                "D>=",
                "Double(a) Double(b)",
                "Boolean",
                "boolean",
                "put to stack true if a>=b",
                new SimpleWorker(new int[]{0x2F,0x02}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double b = v.pop(Double.class);
                        Double a = v.pop(Double.class);
                        if(a==null) v.push(b==null);
                        else v.push(a>=b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "D<",
                "D<",
                "Double(a) Double(b)",
                "Boolean",
                "boolean",
                "put to stack true if a<b",
                new SimpleWorker(new int[]{0x2F,0x03}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double b = v.pop(Double.class);
                        Double a = v.pop(Double.class);
                        if(a==null) v.push(false);
                        else v.push(a<b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "D<=",
                "D<=",
                "Double(a) Double(b)",
                "Boolean",
                "boolean",
                "put to stack true if a<=b",
                new SimpleWorker(new int[]{0x2F,0x04}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double b = v.pop(Double.class);
                        Double a = v.pop(Double.class);
                        if(a==null) v.push(b==null);
                        else v.push(a<=b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "L=",
                "L=",
                "Long(a) Long(b)",
                "Boolean",
                "boolean",
                "put to stack true if a==b",
                new SimpleWorker(new int[]{0x2F,0x05}){
                    @Override public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Long b = v.pop(Long.class);
                        Long a = v.pop(Long.class);
                        if(a==null) v.push(b==null);
                        else v.push(a.equals(b));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "L>",
                "L>",
                "Double(a) Double(b)",
                "Boolean",
                "boolean",
                "put to stack true if a>b",
                new SimpleWorker(new int[]{0x2F,0x06}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Long b = v.pop(Long.class);
                        Long a = v.pop(Long.class);
                        if(a==null) v.push(false);
                        else v.push(a>b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "L>=",
                "L>=",
                "Double(a) Double(b)",
                "Boolean",
                "boolean",
                "put to stack true if a>=b",
                new SimpleWorker(new int[]{0x2F,0x07}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Long b = v.pop(Long.class);
                        Long a = v.pop(Long.class);
                        if(a==null) v.push(b==null);
                        else v.push(a>=b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "L<",
                "L<",
                "Long(a) Long(b)",
                "Boolean",
                "boolean",
                "put to stack true if a<b",
                new SimpleWorker(new int[]{0x2F,0x08}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Long b = v.pop(Long.class);
                        Long a = v.pop(Long.class);
                        if(a==null) v.push(false);
                        else v.push(a<b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "L<=",
                "L<=",
                "Long(a) Long(b)",
                "Boolean",
                "boolean",
                "put to stack true if a<=b",
                new SimpleWorker(new int[]{0x2F,0x09}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Long b = v.pop(Long.class);
                        Long a = v.pop(Long.class);
                        if(a==null) v.push(b==null);
                        else v.push(a<=b);
                    }
                }
        ));

    }
}
