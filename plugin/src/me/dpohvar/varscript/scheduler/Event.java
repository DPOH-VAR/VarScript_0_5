package me.dpohvar.varscript.scheduler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 14.11.13
 * Time: 0:07
 */
public abstract class Event extends Entry {

    public static class WrongEvent extends Event {

        public WrongEvent(Task task, String name, String param) {
            super(task, name, param);
        }

        @Override
        protected boolean register() {
            return false;
        }

        @Override
        protected void unregister() {
        }
    }

    public Event(Task task, String type, String params) {
        super(task, type, params);
    }

    public Event(Task task, String type) {
        super(task, type);
    }

    protected final void call(Map<String, Object> environment) {
        if (environment == null) environment = new HashMap<String, Object>();
        environment.put("TaskEvent", this);
        task.run(environment);
    }

    protected final void call() {
        call(new HashMap<String, Object>());
    }

}
