package me.dpohvar.varscript.scheduler;

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
        public boolean check(Map<String, Object> environment) {
            return false;
        }
    }

    private boolean inverse = false;

    public Condition(@Nonnull Task task, @Nonnull String type, String params) {
        super(task, type, params);
        if (type.startsWith("~")) inverse = true;
    }

    public boolean isInverse() {
        return inverse;
    }

    public Condition(@Nonnull Task task, @Nonnull String type) {
        super(task, type);
    }

    public boolean test(Map<String, Object> environment) {
        return check(environment) != inverse;
    }

    abstract protected boolean check(Map<String, Object> environment);
}
