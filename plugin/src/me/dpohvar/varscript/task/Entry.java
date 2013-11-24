package me.dpohvar.varscript.task;

import org.bukkit.ChatColor;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 03.11.13
 * Time: 22:55
 */
public abstract class Entry {

    public static final String prefixUnactiveEnabled = ChatColor.GRAY.toString();
    public static final String prefixUnactiveDisabled = ChatColor.DARK_GRAY.toString();
    public static final String prefixActiveEnabled = ChatColor.GREEN.toString();
    public static final String prefixActiveDisabled = ChatColor.GOLD.toString();
    public static final String prefixError = ChatColor.STRIKETHROUGH.toString();
    public static final String prefixNoError = "";

    public final String type;
    public final Task task;
    private String params;
    private boolean error;
    private boolean lockParams = false;

    boolean enabled;

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
        this.enabled = enabled;
        if (enabled) {
            boolean success = register();
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

    public String getConstructor() {
        if (params == null) return type;
        else return type + ' ' + params;
    }

    public final String toString() {
        String prefix;

        if (task.scheduler.isEnabled() && task.isEnabled()) {
            if (isEnabled()) prefix = prefixActiveEnabled;
            else prefix = prefixActiveDisabled;
        } else {
            if (isEnabled()) prefix = prefixUnactiveEnabled;
            else prefix = prefixUnactiveDisabled;
        }

        if (isError()) prefix += prefixError;
        else prefix += prefixNoError;

        if (params == null) return prefix + type;
        else return prefix + type + " " + params;
    }

    void checkRegister() {
        boolean success = register();
        if (success) setError(false);
        else setError(true);
    }
}
