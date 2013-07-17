package me.dpohvar.varscript.scheduler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 15.07.13
 * Time: 21:51
 */
public class Scheduler {

    HashMap<String,Task> tasks = new HashMap<String, Task>();
    public boolean enabled;

    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean enabled){
        //todo: Scheduler setEnabled(boolean)
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

    public void loadTask(String name){
        //todo: Scheduler load task from file
    }
}
