package me.dpohvar.varscript.task;

import me.dpohvar.varscript.VarScript;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 03.11.13
 * Time: 0:49
 */
public class Scheduler {

    private static Scheduler instance;

    {
        Scheduler.instance = this;
    }

    public static final File dirHome = new File(VarScript.instance.getDataFolder(), "scheduler");
    public static final File dirTasks = new File(dirHome, "tasks");
    public static final File dirEvents = new File(dirHome, "events");
    public static final File dirConditions = new File(dirHome, "conditions");
    public static final File dirActions = new File(dirHome, "actions");

    public static final ClassLoader classLoaderEvents;
    public static final ClassLoader classLoaderConditions;
    public static final ClassLoader classLoaderActions;

    static {

        if (!dirEvents.isDirectory()) dirEvents.mkdirs();
        if (!dirConditions.isDirectory()) dirConditions.mkdirs();
        if (!dirActions.isDirectory()) dirActions.mkdirs();
        if (!dirTasks.isDirectory()) dirTasks.mkdirs();

        ClassLoader temp = null;
        try {
            temp = new URLClassLoader(new URL[]{dirEvents.toURI().toURL()}, me.dpohvar.varscript.Runtime.libLoader);
        } catch (Exception ignored) {
        }
        classLoaderEvents = temp;
        try {
            temp = new URLClassLoader(new URL[]{dirConditions.toURI().toURL()}, me.dpohvar.varscript.Runtime.libLoader);
        } catch (Exception ignored) {
        }
        classLoaderConditions = temp;
        try {
            temp = new URLClassLoader(new URL[]{dirActions.toURI().toURL()}, me.dpohvar.varscript.Runtime.libLoader);
        } catch (Exception ignored) {
        }
        classLoaderActions = temp;
    }

    private Map<String, Task> tasks = new HashMap<String, Task>();

    private Map<String, Constructor<? extends Event>> eventMakers = new HashMap<String, Constructor<? extends Event>>();
    private Map<String, Constructor<? extends Condition>> conditionMakers = new HashMap<String, Constructor<? extends Condition>>();
    private Map<String, Constructor<? extends Action>> actionMakers = new HashMap<String, Constructor<? extends Action>>();

    private boolean enabled;

    public static Scheduler getInstance() {
        return instance;
    }

    public void addEvents(String name, Class<? extends Event> factory) {
        assert Event.class.isAssignableFrom(factory);
        try {
            eventMakers.put(name, factory.getConstructor(Task.class, String.class, String.class, CommandSender.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void addConditions(String name, Class<? extends Condition> factory) {
        assert Condition.class.isAssignableFrom(factory);
        try {
            conditionMakers.put(name, factory.getConstructor(Task.class, String.class, String.class, CommandSender.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void addActions(String name, Class<? extends Action> factory) {
        assert Action.class.isAssignableFrom(factory);
        try {
            actionMakers.put(name, factory.getConstructor(Task.class, String.class, String.class, CommandSender.class));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Task getTask(String task) {
        return tasks.get(task);
    }

    public ArrayList<Task> getTasks(String query) {
        ArrayList<Task> result = new ArrayList<Task>();
        for (Map.Entry<String, Task> e : tasks.entrySet()) {
            if (e.getKey().startsWith(query)) result.add(e.getValue());
        }
        return result;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<Task>(tasks.values());
    }

    private Collection<Task> tasks() {
        return tasks.values();
    }

    private void clearTasks() {
        tasks.clear();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) register();
        else unregister();
    }

    void register() {
        for (Task t : getTasks()) {
            if (t.isEnabled()) t.register();
        }
    }

    void unregister() {
        for (Task t : getTasks()) {
            if (t.isEnabled()) t.unregister();
        }
    }

    Entry build(EntryType type, Task task, String name, String param, CommandSender sender) {
        switch (type) {
            case EVENT:
                return buildEvent(task, name, param, sender);
            case CONDITION:
                return buildCondition(task, name, param, sender);
            case ACTION:
                return buildAction(task, name, param, sender);
            default:
                throw new RuntimeException("entry type is not defined");
        }
    }

    Event buildEvent(Task task, String name, String param, CommandSender sender) {
        Constructor<? extends Event> constructor = eventMakers.get(name);
        if (constructor == null) return new Event.WrongEvent(task, name, param);
        try {
            return constructor.newInstance(task, name, param, sender);
        } catch (Exception e) {
            throw new RuntimeException("can't create event " + name + " " + param, e);
        }
    }

    Condition buildCondition(Task task, String name, String param, CommandSender sender) {
        Constructor<? extends Condition> constructor = conditionMakers.get(name);
        if (constructor == null) return new Condition.WrongCondition(task, name, param);
        try {
            return constructor.newInstance(task, name, param, sender);
        } catch (Exception e) {
            throw new RuntimeException("can't create condition " + name + " " + param, e);
        }
    }

    Action buildAction(Task task, String name, String param, CommandSender sender) {
        Constructor<? extends Action> constructor = actionMakers.get(name);
        if (constructor == null) return new Action.WrongAction(task, name, param);
        try {
            return constructor.newInstance(task, name, param, sender);
        } catch (Exception e) {
            throw new RuntimeException("can't create action " + name + " " + param, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void reloadClasses() {

        File[] files;

        files = dirEvents.listFiles();
        if (files != null) for (File file : files) {
            if (!file.isFile()) return;
            loadEventClassFromFile(file);
        }

        files = dirConditions.listFiles();
        if (files != null) for (File file : files) {
            if (!file.isFile()) return;
            loadConditionClassFromFile(file);
        }

        files = dirActions.listFiles();
        if (files != null) for (File file : files) {
            if (!file.isFile()) return;
            loadActionClassFromFile(file);
        }
    }

    private void loadEventClassFromFile(File file) {

        String fileName = file.getName();
        if (!fileName.endsWith(".class")) return;
        String name = fileName.substring(0, fileName.length() - 6);

        try {
            Class loadedClass = classLoaderEvents.loadClass(name);
            if (Event.class.isAssignableFrom(loadedClass)) {
                addEvents(name, loadedClass);
                if (VarScript.instance.isDebug()) {
                    VarScript.instance.getLogger().info("[Scheduler] event loaded: " + name);
                }
            }
        } catch (Exception e) {
            VarScript.instance.getLogger().warning("[Scheduler] can not load event: " + name);
            if (VarScript.instance.isDebug()) e.printStackTrace();
        }
    }

    private void loadConditionClassFromFile(File file) {

        String fileName = file.getName();
        if (!fileName.endsWith(".class")) return;
        String name = fileName.substring(0, fileName.length() - 6);

        try {
            Class loadedClass = classLoaderConditions.loadClass(name);
            if (Condition.class.isAssignableFrom(loadedClass)) {
                addConditions(name, loadedClass);
                if (VarScript.instance.isDebug()) {
                    VarScript.instance.getLogger().info("[Scheduler] condition loaded: " + name);
                }
            }
        } catch (Exception e) {
            VarScript.instance.getLogger().warning("[Scheduler] can not load condition: " + name);
            if (VarScript.instance.isDebug()) e.printStackTrace();
        }
    }

    private void loadActionClassFromFile(File file) {

        String fileName = file.getName();
        if (!fileName.endsWith(".class")) return;
        String name = fileName.substring(0, fileName.length() - 6);

        try {
            Class loadedClass = classLoaderActions.loadClass(name);
            if (Action.class.isAssignableFrom(loadedClass)) {
                addActions(name, loadedClass);
                if (VarScript.instance.isDebug()) {
                    VarScript.instance.getLogger().info("[Scheduler] action loaded: " + name);
                }
            }
        } catch (Exception e) {
            VarScript.instance.getLogger().warning("[Scheduler] can not load action: " + name);
            if (VarScript.instance.isDebug()) e.printStackTrace();
        }
    }

    private void reload() {
        if (enabled) unregister();
        reloadClasses();
        reloadTasks();
        if (enabled) register();
    }

    private void reloadTasks() {
        clearTasks();

        File[] files = dirTasks.listFiles();
        if (files != null) for (File file : files) {
            addTask(file);
        }

    }

    private Task addTask(File file) {
        String fileName = file.getName();
        if (!fileName.endsWith(".yml")) return null;
        String name = fileName.substring(0, fileName.length() - 4);

        Task task = new Task(this, name, file);
        addTask(task);
        return task;
    }

    private void addTask(Task task) {
        tasks.put(task.name, task);
    }

    void deleteTask(String name) {
        tasks.remove(name);
    }

    public Task createTask(String name) {
        Task old = tasks.get(name);
        if (old != null) old.unregister();
        tasks.remove(name);
        return addTask(new File(dirTasks, name + ".yml"));
    }


}
