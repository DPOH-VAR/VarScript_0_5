package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Runnable;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.ComplexCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.compiler.VSSmartParser;
import me.dpohvar.varscript.vs.exception.SourceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitCallback {

    public static Worker<Void> wMapStart = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterA(v.pop(f.getScope()));
            f.setRegisterB(v.pop(Iterable.class).iterator());
            f.setRegisterC(new ArrayList());
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE0);
        }
        @Override public byte[] getBytes() {
            return new byte[]{(byte)0xE0};
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wMapCheck,null);
            readSession.addCommandAfter(wMapAgain,null);
            return null;
        }
    };

    public static Worker<Void> wMapCheck = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Iterator iterator = (Iterator) f.getRegisterB();
            if(iterator.hasNext()){
                Object apply = iterator.next();
                v.push(apply);
                v.pushFunction((Runnable)f.getRegisterA(),apply);
                throw interruptFunction;
            } else {
                v.push(f.getRegisterC());
                f.jumpPointer(1);
            }
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
        }
        @Override public byte[] getBytes() {
            return null;
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wMapAgain = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Collection c = (Collection) f.getRegisterC();
            c.add(v.pop());
            f.jumpPointer(-2);

        }
        @Override public void save(OutputStream out, Void data) throws IOException {
        }
        @Override public byte[] getBytes() {
            return null;
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wSelectStart = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterA(v.pop(f.getScope()));
            f.setRegisterB(v.pop(Iterable.class).iterator());
            f.setRegisterC(new ArrayList());
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE1);
        }
        @Override public byte[] getBytes() {
            return new byte[]{(byte)0xE1};
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wSelectCheck,null);
            readSession.addCommandAfter(wSelectAgain,null);
            return null;
        }
    };
    public static Worker<Void> wSelectCheck = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Iterator iterator = (Iterator) f.getRegisterB();
            if(iterator.hasNext()){
                Object apply = iterator.next();
                f.setRegisterF(apply);
                v.push(apply);
                v.pushFunction((Runnable)f.getRegisterA(),apply);
                throw interruptFunction;
            } else {
                v.push(f.getRegisterC());
                f.jumpPointer(1);
            }
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
        }
        @Override public byte[] getBytes() {
            return null;
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wSelectAgain = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            boolean bool = v.pop(Boolean.class);
            Collection c = (Collection) f.getRegisterC();
            if(bool) c.add(f.getRegisterF());
            f.jumpPointer(-2);

        }
        @Override public void save(OutputStream out, Void data) throws IOException {
        }
        @Override public byte[] getBytes() {
            return null;
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wTry = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Runnable fun = v.pop(f.getScope());
            if (fun instanceof Function) ((Function)fun).ignoreExceptions = true;
            v.pushFunction(fun,f.getApply());
            throw interruptThread;
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE2);
        }
        @Override public byte[] getBytes() {
            return new byte[]{(byte)0xE2};
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wSelectCheck,null);
            readSession.addCommandAfter(wSelectAgain,null);
            return null;
        }
    };

    public static Worker<Void> wEachStart = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterA(v.pop(f.getScope()));
            f.setRegisterB(v.pop(Iterable.class).iterator());
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE3);
        }
        @Override public byte[] getBytes() {
            return new byte[]{(byte)0xE3};
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wEachCheck,null);
            readSession.addCommandAfter(wEachAgain,null);
            return null;
        }
    };

    public static Worker<Void> wEachCheck = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Iterator iterator = (Iterator) f.getRegisterB();
            if(iterator.hasNext()){
                Object apply = iterator.next();
                f.setRegisterF(apply);
                v.push(apply);
                v.pushFunction((Runnable)f.getRegisterA(),apply);
                throw interruptFunction;
            } else {
                f.jumpPointer(1);
            }
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
        }
        @Override public byte[] getBytes() {
            return null;
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wEachAgain = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.jumpPointer(-2);

        }
        @Override public void save(OutputStream out, Void data) throws IOException {
        }
        @Override public byte[] getBytes() {
            return null;
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wFoldStart = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Runnable run = v.pop(f.getScope());
            Iterator iterator = v.pop(Iterable.class).iterator();
            if(!iterator.hasNext()){
                v.push(null);
                f.jumpPointer(2);
                return;
            }
            v.push(iterator.next());
            f.setRegisterA(run);
            f.setRegisterB(iterator);
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE4);
        }
        @Override public byte[] getBytes() {
            return new byte[]{(byte)0xE4};
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wEachCheck,null);
            readSession.addCommandAfter(wEachAgain,null);
            return null;
        }
    };














    public static void load(){

        VSCompiler.addRule(new ComplexCompileRule(":MAP{...}","callback function while collection list collection","Collection(old)","ArrayList(new)","map all elements in collection by function. Example: [1,2,3]:MAP{100 +} ## [101,102,103]"){ //0x10
            @Override public boolean checkCondition(String string) {
                return string.matches(":(MAP)?\\{");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0,op.length()-1);
                functionSession.addCommand(InitDynamic.wPutFunction,VSCompiler.compile(name,compileSession,false),operand);
                functionSession.addCommand(wMapStart,null,operand);
                functionSession.addCommand(wMapCheck,null,operand);
                functionSession.addCommand(wMapAgain,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wMapStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("MAP","callback function while collection","Collection(old) Runnable(F)","ArrayList(new)","map all elements in collection with function F\nExample: [1,2,3] {100 +} MAP ## [101,102,103]"){ //0x10
            @Override public boolean checkCondition(String string) {
                return string.equals("MAP");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wMapStart,null,operand);
                functionSession.addCommand(wMapCheck,null,operand);
                functionSession.addCommand(wMapAgain,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(":SELECT{...}","callback function while list collection","Collection(old)","ArrayList(new)","choose from old collection only that satisfy the condition\nExample: [1,12,3,15]:SELECT{10 >} ## [12, 15]"){ //0x10
            @Override public boolean checkCondition(String string) {
                return string.matches(":(SELECT|\\?)\\{");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0,op.length()-1);
                functionSession.addCommand(InitDynamic.wPutFunction,VSCompiler.compile(name,compileSession,false),operand);
                functionSession.addCommand(wSelectStart,null,operand);
                functionSession.addCommand(wSelectCheck,null,operand);
                functionSession.addCommand(wSelectAgain,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wSelectStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("SELECT","callback function while list collection","Collection(old) Runnable(F)","ArrayList(new)","choose from old collection only that satisfy the condition F\nExample: [1,12,3,15] {10 >} SELECT ## [12, 15]"){ //0x10
            @Override public boolean checkCondition(String string) {
                return string.equals("SELECT");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wSelectStart,null,operand);
                functionSession.addCommand(wSelectCheck,null,operand);
                functionSession.addCommand(wSelectAgain,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("@TRY{...}","function runtime","...","...","run function, ignore exceptions"){ //0x10
            @Override public boolean checkCondition(String string) {
                return string.matches("@(TRY)?\\{");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0,op.length()-1);
                functionSession.addCommand(InitDynamic.wPutFunction,VSCompiler.compile(name,compileSession,false),operand);
                functionSession.addCommand(wTry,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wTry};
            }
        });


        VSCompiler.addRule(new ComplexCompileRule(":EACH{...}","callback function while list collection","Collection(old)","","apply function for each entry\nExample: [1,12,3,15]:EACH{PRINT}"){ //0x10
            @Override public boolean checkCondition(String string) {
                return string.matches(":EACH\\{");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0,op.length()-1);
                functionSession.addCommand(InitDynamic.wPutFunction,VSCompiler.compile(name,compileSession,false),operand);
                functionSession.addCommand(wEachStart,null,operand);
                functionSession.addCommand(wEachCheck,null,operand);
                functionSession.addCommand(wEachAgain,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wEachStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("EACH","callback function while list collection","Collection(old) Runnable(F)","","apply function for each entry\nExample: [1,12,3,15] {PRINT} EACH"){ //0x10
            @Override public boolean checkCondition(String string) {
                return string.equals("EACH");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wEachStart,null,operand);
                functionSession.addCommand(wEachCheck,null,operand);
                functionSession.addCommand(wEachAgain,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(":FOLD{...}","callback function while list collection","Collection(old)","Object(result)","fold collection to left\nExample: [1,5,100]:FOLD{+} ## returns 106"){ //0x10
            @Override public boolean checkCondition(String string) {
                return string.matches(":FOLD\\{");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0,op.length()-1);
                functionSession.addCommand(InitDynamic.wPutFunction,VSCompiler.compile(name,compileSession,false),operand);
                functionSession.addCommand(wFoldStart,null,operand);
                functionSession.addCommand(wEachCheck,null,operand);
                functionSession.addCommand(wEachAgain,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wFoldStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("FOLD","callback function while list collection","Collection(old)","Object(result)","fold collection to left\nExample: [1,5,100] {+} FOLD ## returns 106"){ //0x10
            @Override public boolean checkCondition(String string) {
                return string.equals("FOLD");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wFoldStart,null,operand);
                functionSession.addCommand(wEachCheck,null,operand);
                functionSession.addCommand(wEachAgain,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

    }
}
