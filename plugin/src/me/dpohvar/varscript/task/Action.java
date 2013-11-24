package me.dpohvar.varscript.task;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 14.11.13
 * Time: 0:07
 */
public abstract class Action extends Entry {

    public static class WrongAction extends Action {

        public WrongAction(Task task, String type, String param) {
            super(task, type, param);
        }

        @Override
        protected boolean register() {
            return false;
        }

        @Override
        protected void unregister() {
        }

        @Override
        protected void run(Map<String, Object> environment) {
        }
    }

    public Action(@Nonnull Task task, @Nonnull String type, String params) {
        super(task, type, params);
    }

    public Action(@Nonnull Task task, @Nonnull String type) {
        super(task, type);
    }

    abstract protected void run(Map<String, Object> environment);
}
