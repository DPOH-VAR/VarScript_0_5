package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitStack {
    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "DROP",
                "DROP DR",
                "Object",
                "",
                "stack basic",
                "drop last object from stack",
                new SimpleWorker(new int[]{0x00}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) {
                        v.pop();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "DUP",
                "DUP",
                "Object(A)",
                "Object(A) Object(A)",
                "stack basic",
                "duplicate last value",
                new SimpleWorker(new int[]{0x01}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) {
                        v.push(v.peek());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SWAP",
                "SWAP SW",
                "Object(A) Object(B)",
                "Object(B) Object(A)",
                "stack basic",
                "swap last values in stack",
                new SimpleWorker(new int[]{0x02}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) {
                        java.lang.Object b = v.pop();
                        java.lang.Object a = v.pop();
                        v.push(b).push(a);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "OVER",
                "OVER OV",
                "Object(A) Object(B)",
                "Object(A) Object(B) Object(A) ",
                "stack basic",
                "copy the previous value to stack",
                new SimpleWorker(new int[]{0x03}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) {
                        java.lang.Object b = v.pop();
                        java.lang.Object a = v.pop();
                        v.push(a).push(b).push(a);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ROT",
                "ROT",
                "Object(A) Object(B) Object(C)",
                "Object(B) Object(C) Object(A) ",
                "stack basic",
                "rotate 3 elements in stack",
                new SimpleWorker(new int[]{0x04}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) {
                        java.lang.Object c = v.pop();
                        java.lang.Object b = v.pop();
                        java.lang.Object a = v.pop();
                        v.push(b).push(c).push(a);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "PICK",
                "PICK",
                "Objects... Integer",
                "Objects... Object",
                "stack",
                "pick object from stack and put it on top",
                new SimpleWorker(new int[]{0x05}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int pos = v.pop(Integer.class);
                        Stack<java.lang.Object> stack = v.getStack();
                        v.push(stack.get(stack.size()-1-pos));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ROLL",
                "ROLL",
                "Objects... Integer",
                "Objects(turned)...",
                "stack",
                "turn positions of objects in stack",
                new SimpleWorker(new int[]{0x06}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int pos = v.pop(Integer.class);
                        Stack<java.lang.Object> stack = v.getStack();
                        java.lang.Object a = stack.remove(stack.size()-1-pos);
                        v.push(a);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "THIS",
                "THIS",
                "",
                "Fieldable",
                "field",
                "put applied object to stack",
                new SimpleWorker(new int[]{0x07}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(f.getApply());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "OBJECT",
                "OBJECT",
                "",
                "Fieldable",
                "object",
                "create new object",
                new SimpleWorker(new int[]{0x08}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(new FieldableObject(f.getScope()));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CALLER",
                "CALLER",
                "",
                "Caller",
                "stack",
                "get current caller",
                new SimpleWorker(new int[]{0x09}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.getProgram().getCaller());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ME",
                "ME",
                "",
                "Object",
                "stack",
                "get caller instance (who calling)",
                new SimpleWorker(new int[]{0x0A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.getProgram().getCaller().getInstance());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MYLOCATION",
                "MYLOCATION MYLOC ML",
                "",
                "Location",
                "stack location",
                "get current caller's location",
                new SimpleWorker(new int[]{0x0B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.getProgram().getCaller().getLocation());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "PROGRAM",
                "PROGRAM PRG",
                "",
                "Program",
                "stack",
                "get current program",
                new SimpleWorker(new int[]{0x0C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.getProgram());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CLONE",
                "CLONE",
                "Object",
                "Object(clone)",
                "stack",
                "try to clone object",
                new SimpleWorker(new int[]{0x0D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object t = v.pop();
                        try{
                            t = t.getClass().getMethod("clone").invoke(t);
                        } catch (Exception ignored){
                        }
                        v.push(t);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "PRINT",
                "PRINT .",
                "Object",
                "",
                "stack string print",
                "print object to caller",
                new SimpleWorker(new int[]{0x0E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.getProgram().getCaller().send(v.pop());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "DUP2",
                "DUP2",
                "Object(A) Object(B)",
                "Object(A) Object(B) Object(A) Object(B)",
                "stack basic",
                "duplicate last 2 values",
                new SimpleWorker(new int[]{0x0F,0x00}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) {
                        Object b = v.pop();
                        Object a = v.pop();
                        v.push(a).push(b).push(a).push(b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "RUNTIME",
                "RUNTIME",
                "",
                "Runtime",
                "stack",
                "get current runtime",
                new SimpleWorker(new int[]{0x0F,0x01}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.getProgram().getRuntime());
                    }
                }
        ));

    }
}
