package me.dpohvar.varscript.scheduler;

import org.bukkit.ChatColor;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 03.11.13
 * Time: 22:55
 */
public abstract class Entry {

    public static final String prefixUnactiveEnabledValid = ChatColor.GRAY.toString();
    public static final String prefixUnactiveDisabledValid = ChatColor.DARK_GRAY.toString();
    public static final String prefixActiveEnabledValid = ChatColor.GREEN.toString();
    public static final String prefixActiveDisabledValid = ChatColor.DARK_GRAY.toString();
    public static final String prefixUnactiveEnabledError = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString();
    public static final String prefixUnactiveDisabledError = ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString();
    public static final String prefixActiveEnabledError = ChatColor.RED.toString() + ChatColor.STRIKETHROUGH.toString();
    public static final String prefixActiveDisabledError = ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString();

    public final String type;
    public final Task task;
    private String params;
    private boolean error;
    private boolean lockParams = false;
    EntrySlot slot;

    boolean enabled;
    private boolean removed = false;

    public Entry(Task task, String type, String params) {
        this.type = type;
        this.params = params;
        this.task = task;
        lockParams = true;
    }

    public Entry(Task task, String type) {
        this.type = type;
        this.task = task;
    }

    protected void setParams(String params) {
        if (lockParams) return;
        this.params = params;
        lockParams = true;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        if (this.removed) return;
        this.enabled = enabled;
        if (enabled) {
            boolean success;
            try {
                success = register();
            } catch (Exception e) {
                success = false;
                try {
                    unregister();
                } catch (Exception ignored) {
                }
            }
            if (success) setError(false);
            else setError(true);
        } else unregister();
    }

    public final String getType() {
        return type;
    }

    public final String getParams() {
        return params;
    }

    public final String getText() {
        return type + " " + params;
    }

    public final Task getTask() {
        return task;
    }

    public final boolean isError() {
        return error;
    }

    private void setError(boolean error) {
        this.error = error;
    }

    abstract protected boolean register();

    abstract protected void unregister();

    public final String getConstructor() {
        if (params == null) return type;
        else return type + ' ' + params;
    }

    public final String toString() {
        String prefix;

        if (isError()) {
            if (task.scheduler.isEnabled() && task.isEnabled()) { // error-active
                if (isEnabled()) prefix = prefixActiveEnabledError;
                else prefix = prefixActiveDisabledError;
            } else { // error-unactive
                if (isEnabled()) prefix = prefixUnactiveEnabledError;
                else prefix = prefixUnactiveDisabledError;
            }
        } else { // valid-active
            if (task.scheduler.isEnabled() && task.isEnabled()) {
                if (isEnabled()) prefix = prefixActiveEnabledValid;
                else prefix = prefixActiveDisabledValid;
            } else { // valid-unactive
                if (isEnabled()) prefix = prefixUnactiveEnabledValid;
                else prefix = prefixUnactiveDisabledValid;
            }
        }

        if (params == null) return prefix + type;
        else return prefix + type + " " + params;
    }

    final void setRemoved() {
        removed = true;
        if (enabled) unregister();
        enabled = false;
    }

    public final void remove() {
        task.removeEntry(this);
    }

    final void checkRegister() {
        boolean success;
        try {
            success = register();
        } catch (Exception e) {
            success = false;
            try {
                unregister();
            } catch (Exception ignored) {
            }
        }
        if (success) setError(false);
        else setError(true);
    }

}
