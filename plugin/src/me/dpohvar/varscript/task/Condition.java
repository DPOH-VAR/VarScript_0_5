package me.dpohvar.varscript.task;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 14.11.13
 * Time: 0:07
 */
public abstract class Condition extends Entry {

    public static class WrongCondition extends Condition {

        public WrongCondition(Task task, String type, String param) {
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
        protected boolean check(Map<String, Object> environment) {
            return false;
        }
    }

    public Condition(@Nonnull Task task, @Nonnull String type, String params) {
        super(task, type, params);
    }

    public Condition(@Nonnull Task task, @Nonnull String type) {
        super(task, type);
    }

    abstract boolean check(Map<String, Object> environment);
}
