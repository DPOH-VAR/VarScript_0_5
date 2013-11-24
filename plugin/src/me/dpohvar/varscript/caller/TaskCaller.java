package me.dpohvar.varscript.caller;

import me.dpohvar.varscript.scheduler.Scheduler;
import me.dpohvar.varscript.scheduler.Task;
import org.bukkit.Bukkit;

import javax.script.Bindings;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class TaskCaller extends Caller {

    protected Task task;
    protected Scheduler scheduler;
    TaskBindings bindings;

    void setTask(Task task) {
        this.task = task;
    }

    TaskCaller(Task task) {
        this.task = task;
        this.scheduler = task.getScheduler();
        this.bindings = new TaskBindings(task.getName(), fields);
    }

    @Override
    public Map<String, Object> getFields() {
        return bindings;
    }

    @Override
    public Task getInstance() {
        return task;
    }

    public void send(Object message) {
        Bukkit.broadcastMessage("[" + task + "] " + message);
    }
}

class TaskBindings implements Bindings {
    private Map<String, Object> fields;
    private List<String> parents = new ArrayList<String>(4);

    public TaskBindings(String task, Map<String, Object> fields) {
        this.fields = fields;
        String name = task;
        while (name.contains(".")) {
            int point = name.lastIndexOf('.');
            name = name.substring(0, point);
            parents.add(name);
        }
    }

    @Override
    public int size() {
        return -1;
    }

    @Override
    public boolean isEmpty() {
        if (!fields.isEmpty()) return false;
        for (String s : parents) {
            Caller c = Caller.taskCallers.get(s);
            if (c != null) if (!c.getFields().isEmpty()) return false;
        }
        return true;
    }

    @Override
    public boolean containsKey(Object key) {
        if (fields.containsKey(key)) return true;
        for (String s : parents) {
            Caller c = Caller.taskCallers.get(s);
            if (c != null) if (c.getFields().containsKey(key)) return true;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return fields.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        if (fields.containsKey(key)) return fields.get(key);
        for (String s : parents) {
            Caller c = Caller.taskCallers.get(s);
            if (c != null) if (c.getFields().containsKey(key)) return c.getFields().get(key);
        }
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        return fields.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return fields.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        fields.putAll(m);
    }

    @Override
    public void clear() {
        fields.clear();
    }

    @Override
    public Set<String> keySet() {
        return fields.keySet();
    }

    @Override
    public Collection<Object> values() {
        return fields.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return fields.entrySet();
    }
}








