package me.dpohvar.varscript.utils;

import javax.script.Bindings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 23.10.13
 * Time: 3:10
 */
public class ScopeBindings implements Bindings {

    private Map<String, Object> bWrite;
    private Collection<Map<String, Object>> bRead = new ArrayList<Map<String, Object>>(5);

    public ScopeBindings(Map<String, Object> map) {
        this.bWrite = map;
    }

    public ScopeBindings listen(Map<String, Object> map) {
        bRead.add(map);
        return this;
    }

    @Override
    public Object put(String key, Object value) {
        return bWrite.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> toMerge) {
        bWrite.putAll(toMerge);
    }

    @Override
    public void clear() {
        bWrite.clear();
    }

    @Override
    public Set<String> keySet() {
        return bWrite.keySet();
    }

    @Override
    public Collection<Object> values() {
        return bWrite.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return bWrite.entrySet();
    }

    @Override
    public int size() {
        return -1;
    }

    @Override
    public boolean isEmpty() {
        for (Map map : bRead) {
            if (!map.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        for (Map map : bRead) {
            if (map.containsKey(key)) return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map map : bRead) {
            if (map.containsValue(value)) return true;
        }
        return false;
    }

    @Override
    public Object get(Object key) {
        for (Map scope : bRead) {
            if (scope.containsKey(key)) return scope.get(key);
        }
        return null;
    }

    @Override
    public Object remove(Object key) {
        return bWrite.remove(key);
    }
}
