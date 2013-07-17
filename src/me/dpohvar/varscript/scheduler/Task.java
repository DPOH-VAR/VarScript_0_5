package me.dpohvar.varscript.scheduler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 17.07.13
 * Time: 0:31
 */
public class Task {
    public enum EntryType {EVENT,CONDITION,ACTION}
    boolean enabled = false;
    ArrayList<Event> events = new ArrayList<Event>();
    ArrayList<Condition> condtions = new ArrayList<Condition>();
    ArrayList<Action> actions = new ArrayList<Action>();
    Scheduler scheduler;

    public boolean isEnabled(){
        return enabled;
    }

    public Scheduler getScheduler(){
        return scheduler;
    }

    public void setEnabled(boolean enabled){
        //todo: set enabled task
    }
    public <T extends TaskEntry> ArrayList<T> getEntries(Class<T> clazz){
        if (clazz==Event.class) return (ArrayList<T>) events;
        if (clazz==Condition.class) return (ArrayList<T>) condtions;
        if (clazz==Action.class) return (ArrayList<T>) actions;
        return null;
    }

    public <T extends TaskEntry> T getEntry(Class<T> clazz,int id){
        if (clazz==Event.class) return (T) events.get(id);
        if (clazz==Condition.class) return (T) condtions.get(id);
        if (clazz==Action.class) return (T) actions.get(id);
        return null;
    }

}
