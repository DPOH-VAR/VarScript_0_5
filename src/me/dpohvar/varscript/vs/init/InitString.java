package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitString {

    public static SimpleWorker wConcat = new SimpleWorker(new int[]{0x4E}) {
        @Override
        public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
            String s2 = v.pop(String.class);
            String s = v.pop(String.class);
            v.push(s.concat(s2));
        }
    };


    public static void load() {
        VSCompiler.addRule(new SimpleCompileRule(
                "ENDSWITH",
                "ENDS ENDSWITH",
                "String(A) String(B)",
                "Boolean",
                "string",
                "Returns true if string A ends with string B",
                new SimpleWorker(new int[]{0x40}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.endsWith(s2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "REGEX",
                "REGEX",                                 // ##################################################
                "String(A) String(B)",
                "Boolean",
                "string",
                "Check string A to regular expression B",
                new SimpleWorker(new int[]{0x41}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.matches(s2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "REPLACE",
                "REPLACE REP",
                "String(A) String(B) String(C)",
                "String",
                "string",
                "Replace B to C in string A",
                new SimpleWorker(new int[]{0x42}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s3 = v.pop(String.class);
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.replace(s2, s3));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "UPPERCASE",
                "UPPERCASE UCASE",
                "String(A)",
                "String",
                "string",
                "Replace string to UpperCase",
                new SimpleWorker(new int[]{0x43}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        v.push(s.toUpperCase());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LOWERCASE",
                "LOWERCASE LCASE",
                "String(A)",
                "String",
                "string",
                "Replace string to LowerCase",
                new SimpleWorker(new int[]{0x44}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        v.push(s.toLowerCase());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "STARTSWITH",
                "STARTSWITH STARTS",
                "String(A) String(B)",
                "Boolean",
                "string",
                "Returns true if String A starts with String B",
                new SimpleWorker(new int[]{0x45}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.startsWith(s2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CONTAINS",
                "CONTAINS SHAS",
                "String(A) String(B)",
                "Boolean",
                "string",
                "Returns true if String A contains B",
                new SimpleWorker(new int[]{0x46}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.contains(s2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EQUALSIGNORECASE",
                "EQUALSIGNORECASE S=",
                "String(A) String(B)",
                "Boolean",
                "string",
                "Returns true if A equals B (Ignore case)",
                new SimpleWorker(new int[]{0x47}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.equalsIgnoreCase(s2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SINDEX",
                "SINDEX",
                "String(A) String(B)",
                "Integer",
                "string",
                "Returns the index within this string of the first occurrence of the specified substring.",
                new SimpleWorker(new int[]{0x48}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.indexOf(s2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SINDEXFFROM",
                "SINDEXFFROM",
                "String(A) String(B) Integer(Start)",
                "Integer",
                "string",
                "Returns the index within this string of the first occurrence of the specified substring.",
                new SimpleWorker(new int[]{0x49}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer pos = v.pop(Integer.class);
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.indexOf(s2, pos));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SLINDEX",
                "SLINDEX",
                "String(A) String(B)",
                "Integer",
                "string",
                "Returns the index within this string of the rightmost occurrence of the specified substring.",
                new SimpleWorker(new int[]{0x4A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.lastIndexOf(s2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SLINDEXFROM",
                "SLINDEXFROM",
                "String(A) String(B) Integer(Start)",
                "Integer",
                "string",
                "Returns the index within this string of the rightmost occurrence of the specified substring.",
                new SimpleWorker(new int[]{0x4B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer pos = v.pop(Integer.class);
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.indexOf(s2, pos));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SLENGTH",
                "SLENGTH SLEN",
                "String",
                "Integer",
                "string",
                "Returns string length.",
                new SimpleWorker(new int[]{0x4C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        v.push(s.length());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "REPLACEALL",
                "REPLACEALL REPA",
                "String(A) String(Regex) String(C)",
                "String",
                "string",
                "Replace Regex to C in string A",
                new SimpleWorker(new int[]{0x4D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s3 = v.pop(String.class);
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.replaceAll(s2, s3));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CONCAT",
                "CONCAT S+",
                "String(A) String(B)",
                "String(AB)",
                "string",
                "Concatenates strings",
                wConcat
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "REPLACEFIRST",
                "REPLACEFIRST REP1",
                "String(A) String(Regex) String(C)",
                "ArrayList",
                "string",
                "Replace first Regex to C in string A",
                new SimpleWorker(new int[]{0x4F, 0x00}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s3 = v.pop(String.class);
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(s.replaceFirst(s2, s3));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SUBSTRING",
                "SUBSTRING SUBSTR",
                "String(A) Integer(Begin) Integer(End)",
                "String",
                "string",
                "Get substring of A",
                new SimpleWorker(new int[]{0x4F, 0x01}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer end = v.pop(Integer.class);
                        Integer begin = v.pop(Integer.class);
                        String s = v.pop(String.class);
                        if (begin < 0) begin = 0;
                        if (begin > s.length()) begin = s.length();
                        if (end < begin) end = begin;
                        if (end > s.length()) end = s.length();
                        v.push(s.substring(begin, end));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "TRIM",
                "TRIM",
                "String(A)",
                "String",
                "string",
                "Trim string",
                new SimpleWorker(new int[]{0x4F, 0x02}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        v.push(s.trim());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SBYTES",
                "SBYTES",
                "String(A)",
                "byte[]",
                "string",
                "Get bytes of string (UTF8)",
                new SimpleWorker(new int[]{0x4F, 0x03}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        v.push(s.getBytes(VarScript.UTF8));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SPLIT",
                "SPLIT",
                "String(A) String(Regex)",
                "ArrayList",
                "string",
                "Split string A",
                new SimpleWorker(new int[]{0x4F, 0x04}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s2 = v.pop(String.class);
                        String s = v.pop(String.class);
                        v.push(Arrays.asList(s.split(s2)));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SJOIN",
                "SJOIN",
                "List String(separator)",
                "String",
                "string",
                "joins the elements of an array into a string",
                new SimpleWorker(new int[]{0x4F, 0x05}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String separator = v.pop(String.class);
                        List l = v.pop(List.class);
                        ArrayList<String> strings = new ArrayList<String>();
                        for (Object t : l) {
                            strings.add(v.convert(String.class, t));
                        }
                        v.push(StringUtils.join(strings, separator));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CHAR",
                "CHAR",
                "Object",
                "Character",
                "string char",
                "get char by code or by string",
                new SimpleWorker(new int[]{0x4F, 0x06}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(Character.class));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CHARCODE",
                "CHARCODE CC",
                "Character",
                "Integer",
                "char",
                "get character code",
                new SimpleWorker(new int[]{0x4F, 0x07}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push((int) v.pop(Character.class));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CHARS",
                "CHARS",
                "String",
                "List<Character>",
                "char",
                "get characters",
                new SimpleWorker(new int[]{0x4F, 0x08}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        List a = new ArrayList();
                        for (char c : v.pop(String.class).toCharArray()) a.add(c);
                        v.push(a);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FORMAT",
                "FORMAT",
                "String(values)... String(pattern)",
                "String",
                "string",
                "Replace each '$' char in pattern by values\nExample: 10 20 30 \"$ + $ = $\" FORMAT ",
                new SimpleWorker(new int[]{0x4F, 0x09}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String base = v.pop(String.class);
                        char c = '$';
                        int count = 0;
                        for (char t : base.toCharArray()) if (c == t) count++;
                        Stack<String> insert = new Stack<String>();
                        for (int i = 0; i < count; i++) insert.push(v.pop(String.class));
                        StringBuilder buffer = new StringBuilder();
                        for (char t : base.toCharArray()) {
                            if (c != t) buffer.append(t);
                            else {
                                buffer.append(insert.pop());
                            }
                        }
                        v.push(buffer.toString());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FORMATCHAR",
                "FORMATCHAR",
                "String(values)... String(pattern) Character(key)",
                "String",
                "string",
                "Replace each key in pattern by values\nExample: 10 20 30 \"X + X = X$\" \"X\" FORMATCHAR",
                new SimpleWorker(new int[]{0x4F, 0x0A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        char c = v.pop(Character.class);
                        String base = v.pop(String.class);
                        int count = 0;
                        for (char t : base.toCharArray()) if (c == t) count++;
                        Stack<String> insert = new Stack<String>();
                        for (int i = 0; i < count; i++) insert.push(v.pop(String.class));
                        StringBuilder buffer = new StringBuilder();
                        for (char t : base.toCharArray()) {
                            if (c != t) buffer.append(t);
                            else {
                                buffer.append(insert.pop());
                            }
                        }
                        v.push(buffer.toString());
                    }
                }
        ));


    }
}
