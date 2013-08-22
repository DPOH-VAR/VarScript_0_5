package me.dpohvar.varscript.se;

import javax.script.Bindings;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.08.13
 * Time: 10:48
 */
public class WrapBindings implements Bindings {

    private Map<String, Object> main;
    private Collection<Map<String, Object>> sub;

    public WrapBindings(Map<String, Object> main, Map<String, Object>... sub) {
        this.main = main;
        this.sub = Arrays.asList(sub);
    }

    @Override
    public Object put(String name, Object value) {
        return main.put(name, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> toMerge) {
        main.putAll(toMerge);
    }

    @Override
    public void clear() {
        main.clear();
    }

    @Override
    public Set<String> keySet() {
        return main.keySet();
    }

    @Override
    public Collection<Object> values() {
        return main.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return main.entrySet();
    }

    @Override
    public int size() {
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return main.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (main.containsKey(key)) return true;
        for (Map m : sub) if (m.containsKey(key)) return true;
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return main.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        if (main.containsKey(key)) return main.get(key);
        for (Map m : sub) if (m.containsKey(key)) return m.get(key);
        return null;
    }

    @Override
    public Object remove(Object key) {
        return main.remove(key);
    }
}
