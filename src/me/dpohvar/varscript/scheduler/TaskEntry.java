package me.dpohvar.varscript.scheduler;

import static me.dpohvar.varscript.scheduler.Status.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 17.07.13
 * Time: 0:44
 */

public abstract class TaskEntry {

    protected Task task;
    boolean enabled;
    protected boolean error;
    public final TaskEntryType type;
    private boolean markRemove = false;

    protected TaskEntry(Task task,TaskEntryType type){
        this.task = task;
        this.type = type;
    }

    public boolean isRemoved(){
        return markRemove;
    }

    public final Task getTask() {
        return task;
    }

    public final void remove() {
        if(markRemove) return;
        unregister();
        switch (type){
            case EVENT: task.events.remove(this); break;
            case CONDITION: task.conditions.remove(this); break;
            case ACTION: task.actions.remove(this); break;
        }
        markRemove = true;
    }

    public final Status getStatus() {
        if(!enabled) return DISABLED;
        if(!task.enabled || !task.scheduler.enabled) return HOLD;
        if(error) return INVALID;
        return RUN;
    }

    public final boolean setEnabled(boolean enabled) {
        if(this.markRemove) return false;
        if(this.enabled==enabled) return true;
        this.enabled = enabled;
        if(!enabled){
            unregister();
        } else {
            if(task.enabled && task.scheduler.enabled) error=!register();
        }
        return true;
    }

    public final boolean enable(){
        return setEnabled(true);
    }
    public final boolean disable(){
        return setEnabled(false);
    }

    protected abstract boolean register();

    protected abstract boolean unregister();
}
