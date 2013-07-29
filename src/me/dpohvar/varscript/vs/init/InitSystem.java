package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.converter.ConvertException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitSystem {
    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "ARRAY",
                "ARRAY [] [",
                "",
                "ArrayList",
                "list",
                "create new list",
                new SimpleWorker(new int[]{0xD0}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(new ArrayList());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ADD",
                "ADD , ]",
                "Collection Object",
                "Collection",
                "collection list",
                "add object to list",
                new SimpleWorker(new int[]{0xD1}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object t = v.pop();
                        Collection c = v.pop(Collection.class);
                        c.add(t);
                        v.push(c);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REM",
                "REM",
                "Collection Object",
                "Collection",
                "collection list",
                "remove object from list",
                new SimpleWorker(new int[]{0xD2}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object t = v.pop();
                        Collection c = v.pop(Collection.class);
                        c.remove(t);
                        v.push(c);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "DEL",
                "DEL",
                "List Integer(position)",
                "List",
                "list",
                "remove object from list by index",
                new SimpleWorker(new int[]{0xD3}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int t = v.pop(Integer.class);
                        List c = v.pop(List.class);
                        c.remove(t);
                        v.push(c);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REMALL",
                "REMALL",
                "Collection Collection(to_remove)",
                "Collection",
                "list collection",
                "remove all objects from list",
                new SimpleWorker(new int[]{0xD4}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection t = v.pop(Collection.class);
                        Collection c = v.pop(Collection.class);
                        c.removeAll(t);
                        v.push(c);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ADDALL",
                "ADDALL A+",
                "Collection Collection(to_add)",
                "Collection",
                "list collection",
                "add all objects to list",
                new SimpleWorker(new int[]{0xD5}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection t = v.pop(Collection.class);
                        Collection c = v.pop(Collection.class);
                        c.addAll(t);
                        v.push(c);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ELEMENT",
                "ELEMENT EL",
                "Collection Collection(to_add)",
                "Collection",
                "list collection",
                "add all objects to list",
                new SimpleWorker(new int[]{0xD6}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int t = v.pop(Integer.class);
                        List c = v.pop(List.class);
                        v.push(c.get(t));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SIZE",
                "SIZE",
                "Collection",
                "Integer",
                "list collection",
                "get size of collection",
                new SimpleWorker(new int[]{0xD7}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection c = v.pop(Collection.class);
                        v.push(c.size());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ACONTAINS",
                "ACONTAINS AHAS CNT",
                "Collection Object",
                "Boolean",
                "list collection",
                "true if Collection contains Object",
                new SimpleWorker(new int[]{0xD8}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object t = v.pop();
                        Collection c = v.pop(Collection.class);
                        v.push(c.contains(t));
                    }
                }
        ));




    }


}
