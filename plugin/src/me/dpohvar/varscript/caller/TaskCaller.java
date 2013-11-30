package me.dpohvar.varscript.caller;

import me.dpohvar.varscript.scheduler.Scheduler;
import me.dpohvar.varscript.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class TaskCaller extends Caller {

    protected Task task;
    protected Scheduler scheduler;

    void setTask(Task task) {
        this.task = task;
    }

    TaskCaller(Task task) {
        this.task = task;
        this.fields = task.bindings;
    }

    @Override
    public Task getInstance() {
        return task;
    }

    public void send(Object message) {
        Bukkit.broadcastMessage("[" + task + ChatColor.RESET + "] " + message);
    }

    @Override
    public Map<String, Object> getFields() {
        return task.bindings;
    }

    @Override
    public Set<String> getAllFields() {
        return task.bindings.keySet();
    }

    @Override
    public Object getField(String key) {
        return task.bindings.get(key);
    }

    @Override
    public void setField(String key, Object value) {
        task.bindings.put(key, value);
    }

    @Override
    public void removeField(String key) {
        task.bindings.remove(key);
    }

}








