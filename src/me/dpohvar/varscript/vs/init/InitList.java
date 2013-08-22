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

    public static void load() {
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
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
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

        VSCompiler.addRule(new SimpleCompileRule(
                "SETELEMENT",
                "SETELEMENT SETEL >EL",
                "List Object Integer(index)",
                "List",
                "list",
                "set element in list",
                new SimpleWorker(new int[]{0xD9}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object val = v.pop();
                        int t = v.pop(Integer.class);
                        List c = v.pop(List.class);
                        c.set(t, val);
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
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection c = v.pop(Collection.class);
                        int item = random.nextInt(c.size());
                        if (c instanceof List) {
                            v.push(((List) c).get(item));
                            return;
                        }
                        int i = 0;
                        for (Object o : c) {
                            if (i++ == item) {
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
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                new HashMap()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "POP",
                "POP",
                "Collection",
                "Object(last)",
                "collection",
                "pop last object from Collection (with reduce Collection)",
                new SimpleWorker(new int[]{0xDD}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection col = v.pop(Collection.class);
                        Object val = null;
                        if (col.size() == 0) {
                            // do nothing
                        } else if (col instanceof Stack) {
                            val = ((Stack) col).pop();
                        } else if (col instanceof LinkedList) {
                            val = ((LinkedList) col).pop();
                        } else if (col instanceof List) {
                            int size = col.size();
                            val = ((List) col).get(size - 1);
                            ((List) col).remove(size - 1);
                        } else {
                            for (Object aCol : col) val = aCol;
                            col.remove(val);
                        }
                        v.push(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "POLL",
                "POLL",
                "Collection",
                "Object(first)",
                "collection",
                "poll first object from Collection (with reduce Collection)",
                new SimpleWorker(new int[]{0xDE}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection col = v.pop(Collection.class);
                        Object val = null;
                        if (col.size() == 0) {
                            // do nothing
                        } else if (col instanceof LinkedList) {
                            val = ((LinkedList) col).pollFirst();
                        } else if (col instanceof List) {
                            val = ((List) col).get(0);
                            ((List) col).remove(0);
                        } else {
                            val = col.iterator().next();
                            col.remove(val);
                        }
                        v.push(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FIRST",
                "FIRST",
                "Collection",
                "Object(first)",
                "collection",
                "get first element in Collection",
                new SimpleWorker(new int[]{0xDF, 0x00}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection col = v.pop(Collection.class);
                        Object val = null;
                        if (col.size() == 0) {
                            // do nothing
                        } else if (col instanceof List) {
                            val = ((List) col).get(0);
                        } else {
                            val = col.iterator().next();
                        }
                        v.push(val);
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "LAST",
                "LAST",
                "Collection",
                "Object(last)",
                "collection",
                "get last element in Collection",
                new SimpleWorker(new int[]{0xDF, 0x01}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection col = v.pop(Collection.class);
                        Object val = null;
                        if (col.size() == 0) {
                            // do nothing
                        } else if (col instanceof List) {
                            val = ((List) col).get(col.size() - 1);
                        } else {
                            for (Object c : col) val = c;
                        }
                        v.push(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "POPRANDOM",
                "POPRANDOM POPRND",
                "Collection",
                "Object",
                "collection list",
                "get random element from collection",
                new SimpleWorker(new int[]{0xDF, 0x02}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Collection c = v.pop(Collection.class);
                        int item = random.nextInt(c.size());
                        if (c instanceof List) {
                            v.push(((List) c).get(item));
                            ((List) c).remove(item);
                            return;
                        }
                        int i = 0;
                        for (Object o : c) {
                            if (i++ != item) continue;
                            v.push(o);
                            c.remove(o);
                            return;
                        }
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MPUT",
                "MPUT",
                "Map Object(key) Object(value)",
                "Map",
                "map",
                "put key-value pair to map",
                new SimpleWorker(new int[]{0xDF, 0x03}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object value = v.pop();
                        Object key = v.pop();
                        Map map = v.pop(Map.class);
                        map.put(key, value);
                        v.push(map);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MGET",
                "MGET",
                "Map Object(key)",
                "Object(value)",
                "map",
                "get value by key from Map",
                new SimpleWorker(new int[]{0xDF, 0x04}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object key = v.pop();
                        Map map = v.pop(Map.class);
                        v.push(map.get(key));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MCONTAINS",
                "MCONTAINS MHAS",
                "Map Object(key)",
                "Boolean",
                "map",
                "true if map contains the key",
                new SimpleWorker(new int[]{0xDF, 0x05}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object key = v.pop();
                        Map map = v.pop(Map.class);
                        v.push(map.containsKey(key));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MCONTAINSVAL",
                "MCONTAINSVAL MHASVAL",
                "Map Object(value)",
                "Boolean",
                "map",
                "true if map contains the value",
                new SimpleWorker(new int[]{0xDF, 0x06}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object val = v.pop();
                        Map map = v.pop(Map.class);
                        v.push(map.containsValue(val));
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "MRANDOM",
                "MRANDOM MRND",
                "Map",
                "Object(key)",
                "map random",
                "get random key from map",
                new SimpleWorker(new int[]{0xDF, 0x07}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Map map = v.pop(Map.class);
                        Collection c = map.keySet();
                        int x = random.nextInt(c.size());
                        Object val = null;
                        for (Object o : c) if (x-- == 0) val = o;
                        v.push(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MRANDOMVAL",
                "MRANDOMVAL MRNDVAL",
                "Map",
                "Object(value)",
                "map random",
                "get random value from map",
                new SimpleWorker(new int[]{0xDF, 0x08}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Map map = v.pop(Map.class);
                        Collection c = map.values();
                        int x = random.nextInt(c.size());
                        Object val = null;
                        for (Object o : c) if (x-- == 0) val = o;
                        v.push(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REVERSE",
                "REVERSE",
                "List(original)",
                "List(reversed)",
                "list",
                "reverse original list",
                new SimpleWorker(new int[]{0xDF, 0x09}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        List list = v.pop(List.class);
                        Collections.reverse(list);
                        v.push(list);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SHUFFLE",
                "SHUFFLE SHF",
                "List(original)",
                "List(reversed)",
                "list",
                "Randomly permutes original list",
                new SimpleWorker(new int[]{0xDF, 0x0A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        List list = v.pop(List.class);
                        Collections.shuffle(list);
                        v.push(list);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MAPKEYS",
                "MAPKEYS MKEYS",
                "Map",
                "Set(keys)",
                "map",
                "Get all keys for map",
                new SimpleWorker(new int[]{0xDF, 0x0B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Map.class).keySet()
                        );
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "MAPVALUES",
                "MAPVALUES MVALS",
                "Map",
                "Collection(values)",
                "map",
                "Get all values for map",
                new SimpleWorker(new int[]{0xDF, 0x0C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Map.class).values()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "INDEX",
                "INDEX",
                "List Object",
                "Integer(position)",
                "list",
                "Returns the index of the first occurrence of the specified element in list",
                new SimpleWorker(new int[]{0xDF, 0x0D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object t = v.pop();
                        List c = v.pop(List.class);
                        v.push(c.indexOf(t));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LINDEX",
                "LINDEX",
                "List Object",
                "Integer(position)",
                "list",
                "Returns the index of the first occurrence of the specified element in list",
                new SimpleWorker(new int[]{0xDF, 0x0E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object t = v.pop();
                        List c = v.pop(List.class);
                        v.push(c.lastIndexOf(t));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MREM",
                "MREM",
                "Map Object(key)",
                "Map",
                "map",
                "remove key from map",
                new SimpleWorker(new int[]{0xDF, 0x0F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object key = v.pop();
                        v.peek(Map.class).remove(key);
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "MSIZE",
                "MSIZE",
                "Map",
                "Integer",
                "map",
                "get size of map",
                new SimpleWorker(new int[]{0xDF, 0x10}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Map.class).size()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MCLEAR",
                "MCLEAR",
                "Map",
                "Map",
                "map",
                "clear map",
                new SimpleWorker(new int[]{0xDF, 0x11}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Map.class).clear();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ACLEAR",
                "ACLEAR",
                "Collection",
                "Collection",
                "collection",
                "clear collection",
                new SimpleWorker(new int[]{0xDF, 0x12}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Collection.class).clear();
                    }
                }
        ));

    }


}
