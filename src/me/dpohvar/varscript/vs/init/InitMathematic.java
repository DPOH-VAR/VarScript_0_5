package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.vs.VSContext;
import me.dpohvar.varscript.vs.VSSimpleWorker;
import me.dpohvar.varscript.vs.VSThread;
import me.dpohvar.varscript.vs.VSThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.converter.ConvertException;
import me.dpohvar.varscript.vs.converter.Converter;


/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitMathematic {
    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "+",
                "+",
                "Integer(A) Integer(B)",
                "Integer(C)",
                "math",
                "put to stack (A+B)",
                new VSSimpleWorker(new int[]{0x30}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x31}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x32}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x33}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x34}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x35}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x36}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x37}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x38}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x39}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3A}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3B}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x00}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x01}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x02}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x03}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x04}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x05}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x06}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x07}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x08}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x09}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
                        double a = v.pop(Double.class);
                        v.push(Math.exp(a));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EXP",
                "EXP",
                "Double(A)",
                "Double(B)",
                "math",
                "Returns the largest",
                new VSSimpleWorker(new int[]{0x3F,0x0A}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x0B}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x0C}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x0D}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3F,0x0E}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                "math",
                "Put to stack a random number (0 <= B < 1)",
                new VSSimpleWorker(new int[]{0x3F,0x0F}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
                        v.push(Math.random());
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
                new VSSimpleWorker(new int[]{0x3C}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
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
                new VSSimpleWorker(new int[]{0x3D}){
                    @Override public void run(VSThreadRunner r, VSThread v, VSContext f, Void d) throws ConvertException {
                        Object a = v.pop();
                        Object b = v.pop();
                        Converter c = v.getProgram().getRuntime().converter;
                        Caller e = v.getProgram().getCaller();
                        if(v.convert(Double.class,a)<v.convert(Double.class,b)){v.push(b);} else {v.push(a);}
                    }
                }
        ));


    }
}
