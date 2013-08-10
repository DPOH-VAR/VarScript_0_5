package me.dpohvar.varscript.scheduler;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

import static me.dpohvar.varscript.scheduler.Status.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 17.07.13
 * Time: 0:31
 */
public class Task {

    boolean enabled = false;
    String description = "";
    ArrayList<TaskAction> init = new ArrayList<TaskAction>();
    ArrayList<TaskEvent> events = new ArrayList<TaskEvent>();
    ArrayList<TaskCondition> conditions = new ArrayList<TaskCondition>();
    ArrayList<TaskAction> actions = new ArrayList<TaskAction>();
    final Scheduler scheduler;
    String name;
    private File file;
    private boolean markRemove = false;

    public boolean isEnabled(){
        return enabled;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        if(description==null) this.description="";
        else this.description=description;
    }

    public boolean isRemoved(){
        return markRemove;
    }

    public String getName(){
        return name;
    }

    public boolean remove(){
        if(markRemove) return false;
        try{
            if(!file.exists()||file.delete()){
                unregister();
                scheduler.tasks.remove(name);
                return true;
            } else {
                return false;
            }
        } catch (Exception ignored){
            return false;
        }
    }

    public boolean rename(String toName){
        if(!toName.matches("[a-zA-Z0-9\\.\\-_&%]+")) return false;
        if(markRemove) return false;
        File toFile = new File(scheduler.home,toName+".yml");
        if(toFile.exists()) return false;
        if(scheduler.tasks.containsKey(toName)) return false;
        try{
            if(file.renameTo(toFile)){
                file = toFile;
                scheduler.tasks.remove(name);
                name = toName;
                scheduler.tasks.put(name,this);
                return true;
            } else {
                return false;
            }
        } catch (Exception ignored){
            return false;
        }
    }

    public Task copy(String toName){
        File toFile = new File(scheduler.home,toName+".yml");
        try{
            if(toFile.exists()) if(!toFile.delete()) return null;
            Task newTask = scheduler.tasks.get(toName);
            if(newTask!=null) newTask.free();
            newTask = new Task(scheduler,toName);
            newTask.description = description;
            for(TaskEvent t:events){
                newTask.events.add(TaskEvent.fromString(newTask, t.toString()));
            }
            for(TaskCondition t:conditions){
                newTask.conditions.add(TaskCondition.fromString(newTask, t.toString()));
            }
            for(TaskAction t:actions){
                newTask.actions.add(TaskAction.fromString(newTask, t.toString()));
            }
            scheduler.tasks.put(toName,newTask);
            return newTask;
        } catch (Exception ignored){
            return null;
        }
    }

    void free(){
        for(TaskEvent e:events) e.unregister();
        events = null;
        conditions = null;
        actions = null;
        markRemove = true;
    }

    Task(Scheduler scheduler,String name) {
        this.scheduler = scheduler;
        this.name = name;
        this.file = new File(scheduler.home,name+".yml");
        boolean needRead = true;
        if(!file.isFile()){
            try {
                if(!file.createNewFile()) throw new IOException("can't create file "+file);
                needRead = false;
            } catch (IOException e) {
                throw new RuntimeException("can't create file",e);
            }
        }

        if(needRead) try {
            List<String> ymlInit = new ArrayList<String>();
            List<String> ymlEvents = new ArrayList<String>();
            List<String> ymlConditions = new ArrayList<String>();
            List<String> ymlActions = new ArrayList<String>();

            InputStream in = new FileInputStream(file);
            Yaml yaml = new Yaml();
            Map<String, Object> map = (Map<String, Object>) yaml.load(in);
            in.close();
            if(map==null) throw new RuntimeException("task file "+file+" is empty");
            if (map.containsKey("enabled")) enabled = (Boolean) map.get("enabled");
            if (map.containsKey("description")) description = (String) map.get("description");

            if (map.containsKey("init")) ymlInit = (List<String>) map.get("init");
            if (map.containsKey("events")) ymlEvents = (List<String>) map.get("events");
            if (map.containsKey("conditions")) ymlConditions = (List<String>) map.get("conditions");
            if (map.containsKey("actions")) ymlActions = (List<String>) map.get("actions");

            Collection<TaskEntry> toEnable = new ArrayList<TaskEntry>();

            if (ymlInit != null) for (String s : ymlInit) if(s!=null) {
                boolean currentEnabled = true;
                if(s.startsWith("<disabled> ")){
                    currentEnabled = false;
                    s = s.substring(11);
                }
                TaskAction t = TaskAction.fromString(this,s);
                init.add(t);
                if(currentEnabled) toEnable.add(t);
            }
            if (ymlEvents != null) for (String s : ymlEvents) if(s!=null) {
                boolean currentEnabled = true;
                if(s.startsWith("<disabled> ")){
                    currentEnabled = false;
                    s = s.substring(11);
                }
                TaskEvent t = TaskEvent.fromString(this,s);
                events.add(t);
                if(currentEnabled) toEnable.add(t);
            }
            if (ymlConditions != null) for (String s : ymlConditions) if(s!=null) {
                boolean currentEnabled = true;
                if(s.startsWith("<disabled> ")){
                    currentEnabled = false;
                    s = s.substring(11);
                }
                TaskCondition t = TaskCondition.fromString(this,s);
                conditions.add(t);
                if(currentEnabled) toEnable.add(t);
            }
            if (ymlActions != null) for (String s : ymlActions) if(s!=null) {
                boolean currentEnabled = true;
                if(s.startsWith("<disabled> ")){
                    currentEnabled = false;
                    s = s.substring(11);
                }
                TaskAction t = TaskAction.fromString(this,s);
                actions.add(t);
                if(currentEnabled) toEnable.add(t);
            }
            for(TaskEntry t:toEnable) t.setEnabled(true);
            if(enabled && scheduler.enabled){
                HashMap<String,Object> environment = new HashMap<String,Object>();
                environment.put("Task",this);
                for(TaskAction t:init){
                    environment.put("TaskAction",t);
                    if(!t.enabled) continue;
                    if(t.error) continue;
                    t.run(environment);
                }
            }

        } catch (Exception e) {
            free();
            throw new RuntimeException("can't read yaml file "+file,e);
        } else {
            save();
        }
    }

    public Status getStatus(){
        if(!enabled) return DISABLED;
        if(!scheduler.enabled) return HOLD;
        boolean error = false;
        boolean has = false;
        for(TaskEntry e:events){
            if (e.getStatus()== INVALID) error = true;
            else has = true;
        }
        if(!has) return INVALID;
        for(TaskEntry e:conditions){
            if (e.getStatus()== INVALID) error = true;
        }
        has = false;
        for(TaskEntry e:actions){
            if (e.getStatus()== INVALID) error = true;
            else has = true;
        }
        if(!has) return INVALID;
        if(error) return ERROR;
        return RUN;
    }

    public boolean setEnabled(boolean enabled){
        if(this.enabled==enabled) return false;
        this.enabled = enabled;
        if(enabled){
            if(scheduler.enabled) register();
        } else {
            unregister();
        }
        return true;
    }

    public void enable(){
        setEnabled(true);
    }

    public void disable(){
        setEnabled(false);
    }

    public void run(){
        run(new HashMap<String, Object>());
    }

    public void run(Map<String,Object> environment){
        environment.put("Task",this);
        for(TaskAction t:actions){
            environment.put("TaskAction",t);
            if(!t.enabled) continue;
            if(t.error) continue;
            t.run(environment);
        }
        environment.remove("TaskAction");
    }

    public boolean check(Map<String,Object> environment){
        environment.put("Task",this);
        for(TaskCondition t:conditions){
            environment.put("TaskCondition",t);
            if(!t.enabled) continue;
            if(t.error) continue;
            if(!t.check(environment)) return false;
        }
        environment.remove("TaskCondition");
        return true;
    }



    void register(){
        for(TaskAction t:init){
            if(t.enabled) t.error=!t.register();
        }
        for(TaskEvent t:events){
            if(t.enabled) t.error=!t.register();
        }
        for(TaskCondition t:conditions){
            if(t.enabled) t.error=!t.register();
        }
        for(TaskAction t:actions){
            if(t.enabled) t.error=!t.register();
        }
        HashMap<String,Object> environment = new HashMap<String,Object>();
        environment.put("Task",this);
        for(TaskAction t:init){
            environment.put("TaskAction",t);
            if(!t.enabled) continue;
            if(t.error) continue;
            t.run(environment);
        }
    }

    void unregister(){
        for(TaskEvent t:events){
            t.unregister();
        }
        for(TaskCondition t:conditions){
            t.unregister();
        }
        for(TaskAction t:actions){
            t.unregister();
        }
    }

    public void save(){
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("enabled", this.enabled);
        if (description!=null && !description.isEmpty()) data.put("description", description);

        List<String> ymlInit = new ArrayList<String>();
        List<String> ymlEvents = new ArrayList<String>();
        List<String> ymlConditions = new ArrayList<String>();
        List<String> ymlActions = new ArrayList<String>();
        for (TaskAction t : init) {
            if(t.enabled) ymlInit.add(t.toString());
            else ymlInit.add("<disabled> "+t.toString());
        }
        for (TaskEvent t : events) {
            if(t.enabled) ymlEvents.add(t.toString());
            else ymlEvents.add("<disabled> "+t.toString());
        }
        for (TaskCondition t : conditions) {
            if(t.enabled) ymlConditions.add(t.toString());
            else ymlConditions.add("<disabled> "+t.toString());
        }
        for (TaskAction t : actions) {
            if(t.enabled) ymlActions.add(t.toString());
            else ymlActions.add("<disabled> "+t.toString());
        }

        if (!ymlInit.isEmpty()) data.put("init", ymlInit);
        if (!ymlEvents.isEmpty()) data.put("events", ymlEvents);
        if (!ymlConditions.isEmpty()) data.put("conditions", ymlConditions);
        if (!ymlActions.isEmpty()) data.put("actions", ymlActions);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            yaml.dump(data, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("can't save task to yml",e);
        } finally {
            if(writer!=null) try {
                writer.close();
            } catch (IOException ignored) {
            }
        }

    }

    public Scheduler getScheduler(){
        return scheduler;
    }
    public TaskAction getInit(int index){
        return init.get(index);
    }
    public TaskEvent getEvent(int index){
        return events.get(index);
    }
    public TaskCondition getCondition(int index){
        return conditions.get(index);
    }
    public TaskAction getAction(int index){
        return actions.get(index);
    }

    public TaskEntry get(TaskEntryType type,int index){
        switch (type){
            case INIT: return getInit(index);
            case EVENT: return getEvent(index);
            case CONDITION: return getCondition(index);
            case ACTION: return getAction(index);
        }
        return null;
    }
    public int getInitCount(){
        return init.size();
    }
    public int getEventCount(){
        return events.size();
    }
    public int getConditionCount(){
        return conditions.size();
    }
    public int getActionCount(){
        return actions.size();
    }
    public int getCount(TaskEntryType type){
        switch (type){
            case INIT: return getInitCount();
            case EVENT: return getEventCount();
            case CONDITION: return getConditionCount();
            case ACTION: return getActionCount();
        }
        return -1;
    }

    public TaskAction addInit(String constructor){
        TaskAction t = TaskAction.fromString(this,constructor);
        init.add(t);
        return t;
    }
    public TaskEvent addEvent(String constructor){
        TaskEvent t = TaskEvent.fromString(this,constructor);
        events.add(t);
        return t;
    }
    public TaskCondition addCondition(String constructor){
        TaskCondition t = TaskCondition.fromString(this,constructor);
        conditions.add(t);
        return t;
    }
    public TaskAction addAction(String constructor){
        TaskAction t = TaskAction.fromString(this,constructor);
        actions.add(t);
        return t;
    }
    public TaskEntry add(TaskEntryType type,String constructor){
        switch (type){
            case INIT: return addInit(constructor);
            case EVENT: return addEvent(constructor);
            case CONDITION: return addCondition(constructor);
            case ACTION: return addAction(constructor);
        }
        return null;
    }

    public TaskAction editInit(int index,String constructor){
        TaskAction t = init.get(index);
        boolean needEnable = t.enabled;
        t.remove();
        t = TaskAction.fromString(this,constructor);
        init.add(index,t);
        if(needEnable) t.enable();
        return t;
    }
    public TaskEvent editEvent(int index,String constructor){
        TaskEvent t = events.get(index);
        boolean needEnable = t.enabled;
        t.remove();
        t = TaskEvent.fromString(this,constructor);
        events.add(index,t);
        if(needEnable) t.enable();
        return t;
    }

    public TaskCondition editCondition(int index,String constructor){
        TaskCondition t = conditions.get(index);
        boolean needEnable = t.enabled;
        t.remove();
        t = TaskCondition.fromString(this,constructor);
        conditions.add(index,t);
        if(needEnable) t.enable();
        return t;
    }
    public TaskAction editAction(int index,String constructor){
        TaskAction t = actions.get(index);
        boolean needEnable = t.enabled;
        t.remove();
        t = TaskAction.fromString(this,constructor);
        actions.add(index,t);
        if(needEnable) t.enable();
        return t;
    }
    public TaskEntry edit(TaskEntryType type, int index, String constructor){
        switch (type){
            case INIT: return editInit(index, constructor);
            case EVENT: return editEvent(index, constructor);
            case CONDITION: return  editCondition(index, constructor);
            case ACTION: return  editAction(index, constructor);
        }
        return null;
    }

    @Override public String toString(){
        return name;
    }

}
