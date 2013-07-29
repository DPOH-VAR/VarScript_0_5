package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitList {

    public static void load(){
        final Random random = new Random();

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
                "List Integer(index)",
                "Object",
                "list",
                "get element from list by index",
                new SimpleWorker(new int[]{0xD6}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int t = v.pop(Integer.class);
                        v.push(
                                v.pop(List.class)
                                        .get(t)
                        );
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
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object t = v.pop();
                        Collection c = v.pop(Collection.class);
                        v.push(c.contains(t));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETELEMENT",
                "SETELEMENT SETEL >EL",
                "List Object Integer(index)",
                "List",
                "list",
                "set element in list",
                new SimpleWorker(new int[]{0xD9}) {
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object val = v.pop();
                        int t = v.pop(Integer.class);
                        List c = v.pop(List.class);
                        c.set(t,val);
                        v.push(c);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ARANDOM",
                "ARANDOM ARND",
                "Collection",
                "Object",
                "collection list",
                "get random element from collection",
                new SimpleWorker(new int[]{0xDA}) {
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection c = v.pop(Collection.class);
                        int item = random.nextInt(c.size());
                        int i=0; for(Object o:c){
                            if(i++ == item) {
                                v.push(o);
                                return;
                            }
                        }
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "HASHSET",
                "HASHSET",
                "",
                "HashSet",
                "collection",
                "create new HashSet",
                new SimpleWorker(new int[]{0xDB}) {
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                new HashSet()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "HASHMAP",
                "HASHMAP",
                "",
                "HashMap",
                "map",
                "create new HashMap",
                new SimpleWorker(new int[]{0xDC}) {
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                new HashMap()
                        );
                    }
                }
        ));




    }


}
