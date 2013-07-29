package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;

import java.util.Random;


/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitMathematic {
    public static void load(){
        final Random random = new Random();

        VSCompiler.addRule(new SimpleCompileRule(
                "+",
                "+",
                "Integer(A) Integer(B)",
                "Integer(C)",
                "math",
                "put to stack (A+B)",
                new SimpleWorker(new int[]{0x30}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int b = v.pop(Integer.class);
                        int a = v.pop(Integer.class);
                        v.push(a+b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "-",
                "-",
                "Integer(A) Integer(B)",
                "Integer(C)",
                "math",
                "put to stack (A-B)",
                new SimpleWorker(new int[]{0x31}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int b = v.pop(Integer.class);
                        int a = v.pop(Integer.class);
                        v.push(a-b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "*",
                "*",
                "Integer(A) Integer(B)",
                "Integer(C)",
                "math",
                "put to stack (A*B)",
                new SimpleWorker(new int[]{0x32}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int b = v.pop(Integer.class);
                        int a = v.pop(Integer.class);
                        v.push(a*b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "/",
                "/",
                "Integer(A) Integer(B)",
                "Integer(C)",
                "math",
                "put to stack (A/B)",
                new SimpleWorker(new int[]{0x33}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int b = v.pop(Integer.class);
                        int a = v.pop(Integer.class);
                        v.push(a/b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "D+",
                "D+",
                "Double(A) Double(B)",
                "Double(C)",
                "math",
                "put to stack (A+B)",
                new SimpleWorker(new int[]{0x34}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double b = v.pop(Double.class);
                        double a = v.pop(Double.class);
                        v.push(a+b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "D-",
                "D-",
                "Double(A) Double(B)",
                "Double(C)",
                "math",
                "put to stack (A-B)",
                new SimpleWorker(new int[]{0x35}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double b = v.pop(Double.class);
                        double a = v.pop(Double.class);
                        v.push(a-b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "D*",
                "D*",
                "Double(A) Double(B)",
                "Double(C)",
                "math",
                "put to stack (A*B)",
                new SimpleWorker(new int[]{0x36}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double b = v.pop(Double.class);
                        double a = v.pop(Double.class);
                        v.push(a*b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "D/",
                "D/",
                "Double(A) Double(B)",
                "Double(C)",
                "math",
                "put to stack (A/B)",
                new SimpleWorker(new int[]{0x37}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double b = v.pop(Double.class);
                        double a = v.pop(Double.class);
                        v.push(a/b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "L+",
                "L+",
                "Long(A) Long(B)",
                "Long(C)",
                "math",
                "put to stack (A+B)",
                new SimpleWorker(new int[]{0x38}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        long b = v.pop(Long.class);
                        long a = v.pop(Long.class);
                        v.push(a+b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "L-",
                "L-",
                "Long(A) Long(B)",
                "Long(C)",
                "math",
                "put to stack (A-B)",
                new SimpleWorker(new int[]{0x39}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        long b = v.pop(Long.class);
                        long a = v.pop(Long.class);
                        v.push(a-b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "L*",
                "L*",
                "Long(A) Long(B)",
                "Long(C)",
                "math",
                "put to stack (A*B)",
                new SimpleWorker(new int[]{0x3A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        long b = v.pop(Long.class);
                        long a = v.pop(Long.class);
                        v.push(a*b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "L/",
                "L/",
                "Long(A) Long(B)",
                "Long(C)",
                "math",
                "put to stack (A/B)",
                new SimpleWorker(new int[]{0x3B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        long b = v.pop(Long.class);
                        long a = v.pop(Long.class);
                        v.push(a/b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ABS",
                "ABS",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the absolute value of A",
                new SimpleWorker(new int[]{0x3F,0x00}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.abs(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ACOS",
                "ACOS ARCCOS",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the arc cosine of A",
                new SimpleWorker(new int[]{0x3F,0x01}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.acos(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ATAN",
                "ATAN ARCTAN",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the arc tangent of A",
                new SimpleWorker(new int[]{0x3F,0x02}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.atan(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ASIN",
                "ASIN ARCSIN",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the arc sine of A",
                new SimpleWorker(new int[]{0x3F,0x03}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.asin(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CBRT",
                "CBRT",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the cube root of A",
                new SimpleWorker(new int[]{0x3F,0x04}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.cbrt(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CEIL",
                "CEIL",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the smallest",
                new SimpleWorker(new int[]{0x3F,0x05}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.ceil(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "COS",
                "COS",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the trigonometric cosine of an angle",
                new SimpleWorker(new int[]{0x3F,0x06}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.cos(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "COSH",
                "COSH",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the hyperbolic cosine of A",
                new SimpleWorker(new int[]{0x3F,0x07}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.cosh(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SINH",
                "SINH",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the hyperbolic sine of A",
                new SimpleWorker(new int[]{0x3F,0x08}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.sinh(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EXP",
                "EXP",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns Euler's number",
                new SimpleWorker(new int[]{0x3F,0x09}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.exp(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FLOOR",
                "FLOOR",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the largest double value that is less than or equal to the " +
                "argument and is equal to a mathematical integer",
                new SimpleWorker(new int[]{0x3F,0x0A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.floor(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LOG",
                "LOG",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the natural logarithm of A",
                new SimpleWorker(new int[]{0x3F,0x0B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.log(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PI",
                "PI",
                "",
                "PI",
                "math",
                "Put to stack PI",
                new SimpleWorker(new int[]{0x3F,0x0C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(Math.PI);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "TANH",
                "TANH",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the hyperbolic tangent of A",
                new SimpleWorker(new int[]{0x3F,0x0D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.tanh(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SQRT",
                "SQRT",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the correctly rounded positive square root of A",
                new SimpleWorker(new int[]{0x3F,0x0E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.sqrt(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "RANDOM",
                "RANDOM RND",
                "",
                "Double(B)",
                "math random",
                "Put to stack a random number (0 <= B < 1)",
                new SimpleWorker(new int[]{0x3F,0x0F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(random.nextDouble());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "MIN",
                "MIN",
                "Double(A) Double(B)",
                "Double",
                "math",
                "Put to stack a min number of A and B",
                new SimpleWorker(new int[]{0x3C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object a = v.pop();
                        Object b = v.pop();
                        Converter c = v.getProgram().getRuntime().converter;
                        if(v.convert(Double.class,a)>v.convert(Double.class,b)){v.push(b);} else {v.push(a);}
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "MAX",
                "MAX",
                "Double(A) Double(B)",
                "Double",
                "math",
                "Put to stack a max number of A and B",
                new SimpleWorker(new int[]{0x3D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object a = v.pop();
                        Object b = v.pop();
                        Converter c = v.getProgram().getRuntime().converter;
                        Caller e = v.getProgram().getCaller();
                        if(v.convert(Double.class,a)<v.convert(Double.class,b)){v.push(b);} else {v.push(a);}
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "DMOD",
                "DMOD",
                "Double(A) Double(B)",
                "Double",
                "math",
                "Put to stack A % B",
                new SimpleWorker(new int[]{0x3F,0x10}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double b = v.pop(Double.class);
                        Double a = v.pop(Double.class);
                        v.push(a%b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MOD",
                "MOD %",
                "Integer(A) Integer(B)",
                "Integer",
                "math",
                "Put to stack A % B ",
                new SimpleWorker(new int[]{0x3F,0x11}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer b = v.pop(Integer.class);
                        Integer a = v.pop(Integer.class);
                        v.push(a%b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LMOD",
                "LMOD",
                "Long(A) Long(B)",
                "Long",
                "math",
                "Put to stack A % B  ",
                new SimpleWorker(new int[]{0x3F,0x12}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Long b = v.pop(Long.class);
                        Long a = v.pop(Long.class);
                        v.push(a%b);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "RANDOMINT",
                "RANDOMINT RNDI",
                "Integer(A)",
                "Integer",
                "random",
                "Put to stack random Integer from 0 to A",
                new SimpleWorker(new int[]{0x3F,0x13}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                random.nextInt(
                                        v.pop(Integer.class)
                                )
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "RANDOMFOR",
                "RANDOMFOR",
                "Double(Start) Double(End)",
                "Double",
                "random",
                "Put to stack random Double from Start to End",
                new SimpleWorker(new int[]{0x3F,0x14}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        double end = v.pop(Double.class);
                        double start = v.pop(Double.class);
                        double len = end-start;
                        v.push(
                                random.nextDouble()*len+start
                        );
                    }
                }
        ));




    }
}
