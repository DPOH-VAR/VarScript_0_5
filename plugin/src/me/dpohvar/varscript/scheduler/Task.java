package me.dpohvar.varscript.scheduler;

import me.dpohvar.varscript.vs.compiler.VSStringParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nonnull;
import javax.script.Bindings;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR                                                                                                [fak]
 * Date: 03.11.13
 * Time: 0:50
 */
public class Task {

    public final class TaskBinding implements Bindings {
        public final Task task;
        private final HashMap<String, Object> fields = new HashMap<String, Object>();
        private List<String> parents = new ArrayList<String>(4);

        public TaskBinding(Task task) {
            this.task = task;
            String name = task.name;
            while (name.contains(".")) {
                int point = name.lastIndexOf('.');
                name = name.substring(0, point);
                parents.add(name);
            }
        }

        @Override
        public Object put(String name, Object value) {
            return fields.put(name, value);
        }

        @Override
        public void putAll(Map<? extends String, ? extends Object> toMerge) {
            fields.putAll(toMerge);
        }

        @Override
        public void clear() {
            fields.clear();
        }

        @Override
        public Set<String> keySet() {
            return fields.keySet();
        }

        @Override
        public Collection<Object> values() {
            return fields.values();
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            return fields.entrySet();
        }

        @Override
        public int size() {
            return -1;
        }

        @Override
        public boolean isEmpty() {
            if (!fields.isEmpty()) return false;
            for (String s : parents) {
                Task t = task.scheduler.getTask(s);
                if (t != null) if (!t.bindings.fields.isEmpty()) return false;
            }
            return true;
        }

        @Override
        public boolean containsKey(Object key) {
            if (fields.containsKey(key)) return true;
            for (String s : parents) {
                Task t = task.scheduler.getTask(s);
                if (t != null) if (t.bindings.fields.containsKey(key)) return true;
            }
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return fields.containsValue(value);
        }

        @Override
        public Object get(Object key) {
            if (fields.containsKey(key)) return fields.get(key);
            for (String s : parents) {
                Task t = task.scheduler.getTask(s);
                if (t != null && t.bindings.fields.containsKey(key)) {
                    return t.bindings.fields.get(key);
                }
            }
            return null;
        }

        @Override
        public Object remove(Object key) {
            return fields.remove(key);
        }
    }

    public static final String prefixUnactiveEnabledValid = ChatColor.GRAY.toString();
    public static final String prefixUnactiveDisabledValid = ChatColor.DARK_GRAY.toString();
    public static final String prefixActiveEnabledValid = ChatColor.GREEN.toString();
    public static final String prefixActiveDisabledValid = ChatColor.DARK_GRAY.toString();
    public static final String prefixUnactiveEnabledError = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString();
    public static final String prefixUnactiveDisabledError = ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString();
    public static final String prefixActiveEnabledError = ChatColor.RED.toString();
    public static final String prefixActiveDisabledError = ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH.toString();
    public static final String prefixDisabled = "<disabled>";

    public final TaskBinding bindings;
    public final String name;
    public final Scheduler scheduler;

    private File file;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    private List<Action> init = new ArrayList<Action>();
    private List<Event> events = new ArrayList<Event>();
    private List<Condition> conditions = new ArrayList<Condition>();
    private List<Action> actions = new ArrayList<Action>();
    private List<Action> reactions = new ArrayList<Action>();

    private boolean removed;
    private boolean enabled = false;

    Task(Scheduler scheduler, String name, File file) {
        this.scheduler = scheduler;
        this.name = name;
        this.file = file;
        this.bindings = new TaskBinding(this);
        load(file);
    }

    public String getName() {
        return name;
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public boolean remove() {
        if (this.removed) return true;
        if (this.enabled) unregister();
        this.removed = true;
        scheduler.deleteTask(this.name);
        return file.delete();
    }

    //@SuppressWarnings("unchecked")
    public Entry addEntry(EntrySlot slot, String constructor, CommandSender sender) {
        if (this.removed) return null;
        boolean currentEnabled = true;
        if (constructor == null) return null;
        if (constructor.startsWith(prefixDisabled + " ")) {
            currentEnabled = false;
            constructor = constructor.substring(prefixDisabled.length() + 1);
        }
        String name, param = null;
        int index = constructor.indexOf(' ');
        if (index == -1) {
            name = constructor;
        } else {
            name = constructor.substring(0, index);
            param = constructor.substring(index + 1);
        }
        Entry e = createEntry(slot, name, param, sender);
        List entries = (List) getEntries(slot);
        entries.add(e);
        if (this.enabled) {
            if (currentEnabled) e.setEnabled(true);
        } else {
            e.enabled = currentEnabled;
        }
        return e;
    }

    @SuppressWarnings("unchcked")
    public Entry editEntry(EntrySlot slot, int pos, String constructor, CommandSender sender) {
        if (this.removed) return null;
        boolean currentEnabled = true;
        if (constructor == null) return null;
        if (constructor.startsWith(prefixDisabled + " ")) {
            currentEnabled = false;
            constructor = constructor.substring(prefixDisabled.length() + 1);
        }
        List<Entry> entries = (List<Entry>) getEntries(slot);
        if (pos >= entries.size()) return null;
        String name, param = null;
        int index = constructor.indexOf(' ');
        if (index == -1) {
            name = constructor;
        } else {
            name = constructor.substring(0, index);
            param = constructor.substring(index + 1);
        }
        Entry e = createEntry(slot, name, param, sender);
        e.enabled = currentEnabled;
        entries.remove(pos).setRemoved();
        entries.add(pos, e);
        if (this.enabled) e.register();
        return e;
    }

    private Entry createEntry(EntrySlot slot, String name, String param, CommandSender sender) {
        return scheduler.build(slot, this, name, param, sender);
    }

    public void removeEntry(Entry entry) {
        getEntries(entry.slot).remove(entry);
        entry.setRemoved();
    }

    public Entry getEntry(EntrySlot slot, int pos) {
        List<? extends Entry> entries = getEntries(slot);
        if (pos >= entries.size()) return null;
        return entries.get(pos);
    }

    public int getEntryLength(EntrySlot slot) {
        List<? extends Entry> entries = getEntries(slot);
        if (entries != null) return getEntries(slot).size();
        else return -1;
    }

    private List<? extends Entry> getEntries(EntrySlot slot) {
        switch (slot) {
            case INIT:
                return init;
            case EVENT:
                return events;
            case CONDITION:
                return conditions;
            case ACTION:
                return actions;
            case REACTION:
                return reactions;
            default:
                return null;
        }
    }

    void register() {
        if (this.removed) return;
        for (EntrySlot slot : EntrySlot.values()) {
            List<? extends Entry> entries = getEntries(slot);
            for (Entry e : entries) {
                if (e.isEnabled()) e.checkRegister();
            }
        }
        runInit();
    }

    void unregister() {
        if (this.removed) return;
        for (EntrySlot slot : EntrySlot.values()) {
            List<? extends Entry> entries = getEntries(slot);
            for (Entry e : entries) {
                if (e.isEnabled()) e.unregister();
            }
        }
    }

    public boolean run(Map<String, Object> environment) {
        boolean checked = checkConditions(environment);

        if (checked) runActions(environment);
        else runReactions(environment);
        return checked;
    }

    public boolean run() {
        return run(new HashMap<String, Object>());
    }

    public final String toString() {
        String prefix;

        boolean hasError = false;
        hasError:
        for (EntrySlot slot : EntrySlot.values()) {
            for (Entry e : getEntries(slot)) {
                if (e.isEnabled() && e.isError()) {
                    hasError = true;
                    break hasError;
                }
            }
        }

        if (hasError) {
            if (scheduler.isEnabled()) {
                if (isEnabled()) prefix = prefixActiveEnabledError;
                else prefix = prefixActiveDisabledError;
            } else {
                if (isEnabled()) prefix = prefixUnactiveEnabledError;
                else prefix = prefixUnactiveDisabledError;
            }
        } else {
            if (scheduler.isEnabled()) {
                if (isEnabled()) prefix = prefixActiveEnabledValid;
                else prefix = prefixActiveDisabledValid;
            } else {
                if (isEnabled()) prefix = prefixUnactiveEnabledValid;
                else prefix = prefixUnactiveDisabledValid;
            }
        }

        return prefix + name;
    }

    public boolean checkConditions(@Nonnull Map<String, Object> environment) {
        environment.put("Environment", environment);
        environment.put("Task", this);
        for (Entry e : getEntries(EntrySlot.CONDITION)) {
            environment.put("TaskCondition", e);
            if (e.isError()) return false;
            boolean checked = ((Condition) e).test(environment);
            if (!checked) return false;
        }
        return true;
    }

    public boolean checkConditions() {
        return checkConditions(new HashMap<String, Object>());
    }

    public void runActions(@Nonnull Map<String, Object> environment) {
        environment.put("Environment", environment);
        environment.put("Task", this);
        for (Entry e : getEntries(EntrySlot.ACTION)) {
            if (e.isEnabled() && !e.isError()) {
                environment.put("TaskAction", e);
                ((Action) e).run(environment);
            }
        }
    }

    public void runActions() {
        runActions(new HashMap<String, Object>());
    }

    public void runReactions(@Nonnull Map<String, Object> environment) {
        environment.put("Environment", environment);
        environment.put("Task", this);
        for (Entry e : getEntries(EntrySlot.REACTION)) {
            if (e.isEnabled() && !e.isError()) {
                environment.put("TaskAction", e);
                ((Action) e).run(environment);
            }
        }
    }

    public void runReactions() {
        runReactions(new HashMap<String, Object>());
    }

    public void runInit(@Nonnull Map<String, Object> environment) {
        environment.put("Environment", environment);
        environment.put("Task", this);
        for (Entry e : getEntries(EntrySlot.INIT)) {
            if (e.isEnabled() && !e.isError()) {
                environment.put("TaskAction", e);
                ((Action) e).run(environment);
            }
        }
    }

    public void runInit() {
        runInit(new HashMap<String, Object>());
    }

    public void load() {
        load(file);
    }

    private void load(File file) {

        if (this.enabled) unregister();
        this.enabled = false;
        this.description = null;
        this.init.clear();
        this.events.clear();
        this.conditions.clear();
        this.actions.clear();
        this.reactions.clear();

        try {
            if (!file.isFile()) file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException("[" + name + "] can not create task file", e);
        }

        Map map;
        List ymlInit = new ArrayList();
        List ymlEvents = new ArrayList();
        List ymlConditions = new ArrayList();
        List ymlActions = new ArrayList();
        List ymlReactions = new ArrayList();
        boolean enabled = false;

        try {
            InputStream in = new FileInputStream(file);
            Yaml yaml = new Yaml();
            map = (Map) yaml.load(in);
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("can't load task from file: " + file, e);
        }
        if (map == null) return;

        if (map.containsKey("enabled")) enabled = (Boolean) map.get("enabled");
        if (map.containsKey("description")) description = (String) map.get("description");
        if (map.containsKey("init")) ymlInit = (List) map.get("init");
        if (map.containsKey("events")) ymlEvents = (List) map.get("events");
        if (map.containsKey("conditions")) ymlConditions = (List) map.get("conditions");
        if (map.containsKey("actions")) ymlActions = (List) map.get("actions");
        if (map.containsKey("reactions")) ymlReactions = (List) map.get("reactions");

        if (ymlInit != null) for (Object s : ymlInit) {
            addEntry(EntrySlot.INIT, s.toString(), null);
        }
        if (ymlEvents != null) for (Object s : ymlEvents) {
            addEntry(EntrySlot.EVENT, s.toString(), null);
        }
        if (ymlConditions != null) for (Object s : ymlConditions) {
            addEntry(EntrySlot.CONDITION, s.toString(), null);
        }
        if (ymlActions != null) for (Object s : ymlActions) {
            addEntry(EntrySlot.ACTION, s.toString(), null);
        }
        if (ymlReactions != null) for (Object s : ymlReactions) {
            addEntry(EntrySlot.REACTION, s.toString(), null);
        }

        this.enabled = enabled;
    }

    public void save() {
        save(file);
    }

    private void save(File file) {

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("enabled", this.enabled);
        if (description != null && !description.isEmpty()) data.put("description", description);

        List<String> ymlInit = new ArrayList<String>();
        List<String> ymlEvents = new ArrayList<String>();
        List<String> ymlConditions = new ArrayList<String>();
        List<String> ymlActions = new ArrayList<String>();
        List<String> ymlReactions = new ArrayList<String>();

        for (Entry t : getEntries(EntrySlot.INIT)) {
            String line = t.getType();
            if (t.getParams() != null) line += " " + t.getParams();
            if (!t.isEnabled()) line = prefixDisabled + " " + line;
            ymlInit.add(line);
        }

        for (Entry t : getEntries(EntrySlot.EVENT)) {
            String line = t.getType();
            if (t.getParams() != null) line += " " + t.getParams();
            if (!t.isEnabled()) line = prefixDisabled + " " + line;
            ymlEvents.add(line);
        }

        for (Entry t : getEntries(EntrySlot.CONDITION)) {
            String line = t.getType();
            if (t.getParams() != null) line += " " + t.getParams();
            if (!t.isEnabled()) line = prefixDisabled + " " + line;
            ymlConditions.add(line);
        }

        for (Entry t : getEntries(EntrySlot.ACTION)) {
            String line = t.getType();
            if (t.getParams() != null) line += " " + t.getParams();
            if (!t.isEnabled()) line = prefixDisabled + " " + line;
            ymlActions.add(line);
        }

        for (Entry t : getEntries(EntrySlot.REACTION)) {
            String line = t.getType();
            if (t.getParams() != null) line += " " + t.getParams();
            if (!t.isEnabled()) line = prefixDisabled + " " + line;
            ymlReactions.add(line);
        }

        if (!ymlInit.isEmpty()) data.put("init", ymlInit);
        if (!ymlEvents.isEmpty()) data.put("events", ymlEvents);
        if (!ymlConditions.isEmpty()) data.put("conditions", ymlConditions);
        if (!ymlActions.isEmpty()) data.put("actions", ymlActions);
        if (!ymlReactions.isEmpty()) data.put("reactions", ymlReactions);

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            yaml.dump(data, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("can not save task to yml", e);
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (IOException ignored) {
            }
        }
    }


    public class InsertResult {
        public Map<String, Object> map = new HashMap<String, Object>();
        public List<Object> list = new ArrayList<Object>();
    }

    public InsertResult insertValues(LinkedHashMap<String, String> params, Map<String, Object> environment) {
        InsertResult result = new InsertResult();
        for (Map.Entry<String, String> e : params.entrySet()) {
            if (e.getValue() == null) {
                result.list.add(parseObject(e.getKey(), environment));
            } else {
                result.map.put(parseString(e.getKey()), parseObject(e.getValue(), environment));
            }
        }
        return result;
    }

    public Object parseObject(String val, Map<String, Object> environment) {
        if (val == null) return null;
        if (val.equals("true")) return true;
        if (val.equals("false")) return false;
        if (val.equals("null")) return null;
        if (val.matches("[0-9]+")) {
            try {
                return Integer.parseInt(val);
            } catch (Exception e) {
                return Long.parseLong(val);
            }
        }
        if (val.matches("[0-9]+.[0-9]?")) {
            try {
                return Float.parseFloat(val);
            } catch (Exception e) {
                return Double.parseDouble(val);
            }
        }
        if (val.startsWith("\"") && val.endsWith("\"")) {
            try {
                val = val.substring(1, val.length() - 1);
                return VSStringParser.parse(val);
            } catch (Exception e) {
                return val;
            }
        }
        if (val.matches("[0-9]+(:?\\.[0-9]*)?:[0-9]+(:?\\.[0-9]*)?:[0-9]+(:?\\.[0-9]*)?")) {
            String[] ss = val.split(":");
            org.bukkit.util.Vector vector = new Vector();
            vector.setX(Double.parseDouble(ss[0]));
            vector.setY(Double.parseDouble(ss[1]));
            vector.setZ(Double.parseDouble(ss[2]));
            return vector;
        }
        if (val.matches("[0-9]+:[0-9]+:[0-9]+:.*")) {
            String[] ss = val.split(":");
            World w = Bukkit.getWorld(ss[3]);
            return w.getBlockAt(
                    Integer.parseInt(ss[0]),
                    Integer.parseInt(ss[1]),
                    Integer.parseInt(ss[2])
            );
        }
        if (val.matches("[0-9]+(:?\\.[0-9]*)?:[0-9]+(:?\\.[0-9]*)?:[0-9]+(:?\\.[0-9]*)?:.*")) {
            String[] ss = val.split(":");
            World w = Bukkit.getWorld(ss[3]);
            return new Location(w,
                    Double.parseDouble(ss[0]),
                    Double.parseDouble(ss[1]),
                    Double.parseDouble(ss[2])
            );
        }
        if (val.startsWith("@")) {
            return Bukkit.getPlayer(val.substring(1));
        }
        if (val.startsWith("$")) {
            String var = val.substring(1);
            if (environment.containsKey(var)) {
                return environment.get(var);
            } else if (bindings.containsKey(var)) {
                return bindings.get(var);
            } else {
                return null;
            }
        }
        return val;
    }

    public String parseString(String val) {
        if (val == null) return null;
        if (val.equals("null")) return null;
        if (val.startsWith("\"") && val.endsWith("\"")) {
            try {
                val = val.substring(1, val.length() - 1);
                return VSStringParser.parse(val);
            } catch (Exception e) {
                return val;
            }
        }
        return val;
    }

    public void reload() {
        load(file);
        if (enabled) register();
    }

    @SuppressWarnings("unused")
    public String display() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.toString());
        buffer.append(ChatColor.RESET).append('\n');
        if (description != null && !description.isEmpty()) {
            buffer.append(ChatColor.YELLOW).append(description);
            buffer.append(ChatColor.RESET).append('\n');
        }

        for (EntrySlot slot : EntrySlot.values()) {
            List<? extends Entry> entries = getEntries(slot);
            if (entries.size() <= 0) continue;
            buffer.append(slot.setName).append('\n');
            for (Entry e : entries) {
                buffer.append("- ");
                buffer.append(e);
                buffer.append(ChatColor.RESET).append('\n');
            }
        }
        return buffer.substring(0, buffer.length() - 1);
    }

    @SuppressWarnings("unused")
    public String display(EntrySlot slot) {
        List<? extends Entry> entries = getEntries(slot);
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.toString());
        buffer.append(ChatColor.RESET).append(' ');
        buffer.append(slot.setName).append('\n');
        for (Entry e : entries) {
            buffer.append("- ");
            buffer.append(e);
            buffer.append(ChatColor.RESET).append('\n');
        }
        return buffer.substring(0, buffer.length() - 1);

    }


}
