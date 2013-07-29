package me.dpohvar.varscript.scheduler;

import me.dpohvar.varscript.config.ConfigKey;
import me.dpohvar.varscript.config.ConfigManager;
import org.apache.commons.io.FileExistsException;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static me.dpohvar.varscript.config.ConfigKey.SCHEDULER_ENABLED;
import static me.dpohvar.varscript.scheduler.Status.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 15.07.13
 * Time: 21:51
 */
public class Scheduler {

    HashMap<String,Task> tasks = new HashMap<String, Task>();
    boolean enabled = false;
    public final me.dpohvar.varscript.Runtime runtime;
    private final ConfigManager config;
    final File home;

    public boolean isEnabled(){
        return enabled;
    }

    public String toString(){
        return "Varscript scheduler, Tasks: "+getTasks().size();
    }

    public Scheduler(me.dpohvar.varscript.Runtime runtime,File home) {
        this.runtime = runtime;
        config = runtime.plugin.getConfigManager();
        this.home = home;
        if(!home.isDirectory()){
            if(!home.mkdirs()) throw new RuntimeException(new FileExistsException(home));
        }
        if(config.<Boolean>get(ConfigKey.SCHEDULER_ENABLED)) this.setEnabled(true);
    }

    public Collection<Task> getTasks(){
        return tasks.values();
    }

    public Status getStatus(){
        if(!enabled) return DISABLED;
        for(Task task:getTasks()){
            Status s = task.getStatus();
            if(s==ERROR||s==INVALID) return ERROR;
        }
        return RUN;
    }

    public boolean setEnabled(boolean enabled){
        if(this.enabled==enabled) return false;
        config.set(SCHEDULER_ENABLED,enabled);
        this.enabled = enabled;
        if(enabled){
            for(Task t:getTasks()){
                if(t.enabled) t.register();
            }
        } else {
            for(Task t:getTasks()) t.unregister();
        }
        return true;
    }

    public void enable(){
        setEnabled(true);
    }

    public void disable(){
        setEnabled(false);
    }

    public Task getTask(String name){
        return tasks.get(name);
    }

    public TaskList getTasks(String start){
        TaskList list = new TaskList();
        for(Map.Entry<String,Task> entry:tasks.entrySet()){
            if(entry.getKey().startsWith(start)) list.add(entry.getValue());
        }
        return list;
    }

    private void registerTask(Task task){
        tasks.put(task.getName(),task);
    }

    private void unregisterTask(Task task){
        tasks.remove(task.getName());
    }

    public Task loadTask(String name){
        if(!name.matches("[a-zA-Z0-9\\.\\-_&%]+")) return null;
        Task oldTask = getTask(name);
        if(oldTask!=null){
            oldTask.free();
            unregisterTask(oldTask);
        }
        Task task = new Task(this,name);
        registerTask(task);
        return task;
    }

    public void loadTasks(String prefix){
        File[] files = home.listFiles();
        if (files!=null) for (File file:files){
            String t = file.getName();
            int p = t.lastIndexOf('.');
            if( p == -1 ) continue;
            String ext = t.substring(p+1);
            if( ! ext.equalsIgnoreCase("yml")) continue;
            String name = t.substring(0,p);
            if(name.startsWith(prefix)) loadTask(name);
        }
    }

    public void reload() {
        for(Task t:getTasks()) t.free();
        tasks.clear();
        loadTasks("");
    }
}
