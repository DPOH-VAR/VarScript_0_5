package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Runnable;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.ComplexCompileRule;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.compiler.VSSmartParser;
import me.dpohvar.varscript.vs.exception.SourceException;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitCallback {

    private static class MinecraftLogHandler extends Handler {
        private final Writer writer;
        MinecraftLogHandler( final Writer writer ){
            this.writer = writer;
        }
        @Override public void publish( LogRecord record ){
            try {
                writer.write( String.format( "%s\n", record.getMessage() ) );
            } catch( IOException e ) {
            }
        }
        public void close() {
            try {
                writer.close();
            }
            catch( IOException e ) {
            }
        }
        public void flush() {
            try {
                writer.flush();
            } catch( IOException e ) {
            }
        }
    }


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
            if(iterator!=null && iterator.hasNext()){
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
            if(iterator!=null && iterator.hasNext()){
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
            if(iterator!=null && iterator.hasNext()){
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

    public static Worker<Void> wDoStart = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            int iterate = v.pop(Integer.class);
            int limit = v.pop(Integer.class);
            Runnable run = v.pop(f.getScope());
            f.setRegisterA(run);
            f.setRegisterB(limit);
            f.setRegisterC(limit>iterate);
            f.setRegisterD(iterate);
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE5);
        }
        @Override public byte[] getBytes() {
            return new byte[]{(byte)0xE5};
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wEachCheck,null);
            readSession.addCommandAfter(wEachAgain,null);
            return null;
        }
    };

    public static Worker<Void> wDoCheck = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Runnable run = (Runnable)f.getRegisterA();
            Integer limit = (Integer)f.getRegisterB();
            boolean cond = (Boolean)f.getRegisterC();
            Integer iterate = (Integer)f.getRegisterD();
            if((iterate!=null)&&(limit!=null)&&(iterate<limit == cond)){
                v.pushFunction(run,f.getApply());
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

    public static Worker<Void> wDoAgain = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterD((Integer) f.getRegisterD() + 1);
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

    public static Worker<Void> wDoI = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            v.push(f.getRegisterD());
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
        }
        @Override public byte[] getBytes() {
            return new byte[]{(byte)0xE6};
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wEndLoop = new Worker<Void>() {
        @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Stack<Context> stack = v.getContextStack();
            if(stack.size()>1) {
                Context c = stack.get(stack.size()-2);
                c.setRegisterB(null);
            }
            throw stopFunction;
        }
        @Override public void save(OutputStream out, Void data) throws IOException {
        }
        @Override public byte[] getBytes() {
            return new byte[]{(byte)0xE7};
        }
        @Override public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };














    public static void load(){

        VSCompiler.addRule(new ComplexCompileRule(":MAP{...}","function while collection list collection","Collection(old)","ArrayList(new)","map all elements in collection by function. Example: [1,2,3]:MAP{100 +} ## [101,102,103]"){ //0x10
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

        VSCompiler.addRule(new ComplexCompileRule("MAP","function while collection","Collection(old) Runnable(F)","ArrayList(new)","map all elements in collection with function F\nExample: [1,2,3] {100 +} MAP ## [101,102,103]"){ //0x10
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

        VSCompiler.addRule(new ComplexCompileRule(":SELECT{...}","function while list collection","Collection(old)","ArrayList(new)","choose from old collection only that satisfy the condition\nExample: [1,12,3,15]:SELECT{10 >} ## [12, 15]"){ //0x10
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

        VSCompiler.addRule(new ComplexCompileRule("SELECT","function while list collection","Collection(old) Runnable(F)","ArrayList(new)","choose from old collection only that satisfy the condition F\nExample: [1,12,3,15] {10 >} SELECT ## [12, 15]"){ //0x10
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


        VSCompiler.addRule(new ComplexCompileRule(":EACH{...}","function while list collection","Collection(old)","","apply function for each entry\nExample: [1,12,3,15]:EACH{PRINT}"){ //0x10
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

        VSCompiler.addRule(new ComplexCompileRule("EACH","function while list collection","Collection(old) Runnable(F)","","apply function for each entry\nExample: [1,12,3,15] {PRINT} EACH"){ //0x10
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

        VSCompiler.addRule(new ComplexCompileRule(":FOLD{...}","function while list collection","Collection(old)","Object(result)","fold collection to left\nExample: [1,5,100]:FOLD{+} ## returns 106"){ //0x10
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

        VSCompiler.addRule(new ComplexCompileRule("FOLD","function while list collection","Collection(old)","Object(result)","fold collection to left\nExample: [1,5,100] {+} FOLD ## returns 106"){ //0x10
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

        VSCompiler.addRule(new ComplexCompileRule("@DO{...}","loop","Integer(end) Integer(start)","","loop for values from start to end\nExample: 10 0 @DO{I PRINT}"){
            @Override public boolean checkCondition(String string) {
                return string.equals("@DO{");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0,op.length()-1);
                functionSession.addCommand(InitDynamic.wPutFunction,VSCompiler.compile(name,compileSession,false),operand);
                functionSession.addCommand(wDoStart,null,operand);
                functionSession.addCommand(wDoCheck,null,operand);
                functionSession.addCommand(wDoAgain,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wDoStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("I","loop iterator","","Integer(iterator)","get iterator value inside loop\nExample: 10 0 @DO{I PRINT}"){
            @Override public boolean checkCondition(String string) {
                return string.equals("I");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wDoI,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wDoI};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("ENDLOOP","loop","","Integer(iterator)","break loop with inside function\n"){
            @Override public boolean checkCondition(String string) {
                return string.equals("ENDLOOP")||string.equals("ENDLOOP");
            }
            @Override public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wEndLoop,null,operand);
            }
            @Override public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wEndLoop};
            }
        });

        VSCompiler.addRule(new SimpleCompileRule(
                "CONSOLE",
                "CONSOLE CON",
                "String(command)",
                "String(result)",
                "console",
                "execute console command",
                new SimpleWorker(new int[]{0xEF,0x10}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String command = v.pop(String.class);
                        // --- from plugin HTTPConsole
                        ConsoleCommandSender sender = Bukkit.getConsoleSender();
                        StringWriter command_output = new StringWriter();
                        Logger minecraft_logger = Logger.getLogger( "Minecraft" );
                        MinecraftLogHandler minecraft_log_handler = new MinecraftLogHandler( command_output );
                        minecraft_logger.addHandler( minecraft_log_handler );
                        Bukkit.dispatchCommand( sender, command );
                        minecraft_logger.removeHandler( minecraft_log_handler );
                        minecraft_log_handler.flush();
                        minecraft_log_handler.close();
                        // ---
                        v.push(command_output.toString());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BROADCAST",
                "BROADCAST BC",
                "String(message)",
                "",
                "console",
                "broadcast message",
                new SimpleWorker(new int[]{0xEF,0x11}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Bukkit.broadcastMessage(
                                v.pop(String.class)
                        );
                    }
                }
        ));




    }
}