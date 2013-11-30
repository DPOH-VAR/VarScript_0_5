package me.dpohvar.varscript.command;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandTask implements CommandExecutor {

    private final Runtime runtime;
    public static ChatColor SUCCESS = ChatColor.AQUA;
    public static ChatColor ERROR = ChatColor.RED;
    public static ChatColor RESET = ChatColor.RESET;
    public static ChatColor INFO = ChatColor.YELLOW;

    public CommandTask(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try {
            Scheduler scheduler = runtime.getScheduler();
            Queue<String> words = new LinkedList<String>(Arrays.asList(strings));
            String taskname = words.poll();
            if (taskname == null) taskname = "*";
            String op = words.poll();
            if (taskname.endsWith("*")) {
                String query = taskname.substring(0, taskname.length() - 1);
                List<Task> tasks = scheduler.getTasks(query);
                if (op == null) {
                    StringBuilder buffer = new StringBuilder("Tasks for ").append(taskname).append(" :");
                    for (Task task : tasks) {
                        buffer.append('\n').append(task).append(RESET);
                        String desc = task.getDescription();
                        if (desc != null && !desc.isEmpty()) buffer.append(' ').append(INFO).append(desc);
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op, "disable", "off", "dis")) {
                    StringBuilder buffer = new StringBuilder("Disable tasks ").append(taskname).append(" :");
                    for (Task task : tasks) {
                        buffer.append('\n').append(task).append(RESET);
                        if (!task.isEnabled()) continue;
                        buffer.append(SUCCESS).append(" OK");
                        task.setEnabled(false);
                        task.save();
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op, "enable", "on", "en")) {
                    StringBuilder buffer = new StringBuilder("Enable tasks ").append(taskname).append(" :");
                    for (Task task : tasks) {
                        buffer.append('\n').append(task).append(RESET);
                        if (task.isEnabled()) continue;
                        buffer.append(SUCCESS).append(" OK");
                        task.setEnabled(true);
                        task.save();
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op, "remove")) {
                    StringBuilder buffer = new StringBuilder("Remove tasks ").append(taskname).append(" :");
                    for (Task task : tasks) {
                        buffer.append('\n').append(task).append(RESET).append(' ');
                        if (task.remove()) {
                            buffer.append(SUCCESS).append("OK");
                        } else {
                            buffer.append(ERROR).append("ERROR");
                        }
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op, "save", "s")) {
                    StringBuilder buffer = new StringBuilder("Save tasks ").append(taskname).append(" :");
                    for (Task task : tasks) {
                        buffer.append('\n').append(task).append(INFO).append(' ').append(task.getDescription());
                    }
                    caller.send(buffer);
                    return true;
                } else {
                    caller.send("unknown action with tasks: " + op);
                    return true;
                }
            } else { // name is single string
                Task task = scheduler.getTask(taskname);
                if (op != null && checkNoCase(op, "create", "cr", "new", "n")) {
                    if (task != null) {
                        caller.send("task " + task + RESET + " already exists");
                        return true;
                    }
                    task = scheduler.createTask(taskname);
                    if (task == null) {
                        caller.send("can't create task " + taskname);
                        return true;
                    }
                    if (!words.isEmpty()) task.setDescription(StringUtils.join(words, ' '));
                    caller.send("task " + task + RESET + " is created");
                    return true;
                }
                if (task == null) {
                    caller.send("task " + taskname + " not found");
                    return true;
                }
                if (op == null) {
                    if (task == null) {
                        caller.send("no task with name " + taskname);
                        return true;
                    } else {
                        caller.send(task.display());
                        return true;
                    }
                } else if (checkNoCase(op, "reload", "reset", "!")) {
                    task.reload();
                    StringBuilder buffer = new StringBuilder();
                    buffer.append(task).append(RESET).append(" reloaded");
                    buffer.append('\n').append(task.display());
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op, "check")) {
                    caller.send(task.toString() + RESET + " checked: " + task.checkConditions(caller.getFields()));
                    return true;
                } else if (checkNoCase(op, "run")) {
                    caller.send(task.toString() + RESET + " run");
                    task.run(caller.getFields());
                    return true;
                } else if (checkNoCase(op, "disable", "off", "dis")) {
                    StringBuilder buffer = new StringBuilder("disable task ");
                    buffer.append(task).append(RESET).append(" :");
                    if (task.isEnabled()) {
                        task.setEnabled(false);
                        task.save();
                        buffer.append(SUCCESS).append(" OK");
                    } else {
                        buffer.append(RESET).append(" Already disabled");
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op, "enable", "on", "en")) {
                    StringBuilder buffer = new StringBuilder("enable task ");
                    buffer.append(task).append(RESET).append(" :");
                    if (!task.isEnabled()) {
                        task.setEnabled(true);
                        task.save();
                        buffer.append(SUCCESS).append(" OK");
                    } else {
                        buffer.append(RESET).append(" Already enabled");
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op, "remove")) {
                    StringBuilder buffer = new StringBuilder("Remove task ").append(task).append(RESET).append(" : ");
                    if (task.remove()) {
                        buffer.append(SUCCESS).append("OK");
                    } else {
                        buffer.append(ERROR).append("ERROR");
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op, "save", "s")) {
                    task.save();
                    caller.send("Task " + task + RESET + " saved");
                    return true;
                } else if (checkNoCase(op, "description", "desc", "d")) {
                    StringBuilder buffer = new StringBuilder();
                    if (words.isEmpty()) {
                        String desc = task.getDescription();
                        if (desc != null) {
                            buffer.append(task).append(RESET).append(" description:\n");
                            buffer.append(INFO).append(task.getDescription());
                        } else {
                            buffer.append(task).append(RESET).append(" has no description");
                        }
                    } else {
                        String desc = StringUtils.join(words, ' ');
                        if (desc.isEmpty()) {
                            task.setDescription(null);
                            buffer.append("description for ").append(task).append(RESET);
                            buffer.append(" is removed");
                        } else {
                            task.setDescription(desc);
                            buffer.append("New description for task ").append(task);
                            buffer.append(RESET).append(" is set:\n").append(INFO).append(task.getDescription());
                        }
                        task.save();
                    }
                    caller.send(buffer.toString());
                    return true;
                } else if (checkNoCase(op, "copy")) {
                    String name = words.poll();
                    if (name == null) {
                        caller.send("enter a name of new task");
                        return true;
                    }
                    StringBuilder buffer = new StringBuilder("Copy task ").append(task).append(RESET);
                    buffer.append(" to ").append(name).append(" : ");
                    Task des = scheduler.copyTask(task.getName(), name);
                    if (des != null) {
                        buffer.append(SUCCESS).append("OK");
                    } else {
                        buffer.append(ERROR).append("ERROR");
                    }
                    caller.send(buffer);
                    task.save();
                    return true;
                }
                EntrySlot slot = EntrySlot.getByName(op);

                if (slot != null) {
                    String entryIdString = words.poll();
                    String entryOperation = words.poll();
                    int entryId = -1;
                    try {
                        entryId = Integer.parseInt(entryIdString);
                    } catch (NumberFormatException ignored) {
                    }
                    if (entryIdString == null || checkNoCase(entryIdString, "*", "all")) {
                        if (entryOperation == null || checkNoCase(entryOperation, "view", "v")) {
                            caller.send(task.display(slot));
                            return true;
                        } else if (checkNoCase(entryOperation, "remove")) {
                            StringBuilder buffer = new StringBuilder("all ").append(slot.setName);
                            buffer.append(" removed in ").append(task).append(RESET).append(" :");
                            for (; ; ) {
                                Entry e = task.getEntry(slot, 0);
                                if (e == null) break;
                                e.remove();
                            }
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if (checkNoCase(entryOperation, "enable", "en", "on")) {
                            StringBuilder buffer = new StringBuilder("all ").append(slot.setName);
                            buffer.append(" enabled in ").append(task).append(RESET).append(" :");
                            for (int i = 0; ; i++) {
                                Entry t = task.getEntry(slot, i);
                                if (t == null) break;
                                t.setEnabled(true);
                            }
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if (checkNoCase(entryOperation, "disable", "dis", "off")) {
                            StringBuilder buffer = new StringBuilder("all ").append(slot.setName);
                            buffer.append(" disabled in ").append(task).append(RESET).append(" :");
                            for (int i = 0; ; i++) {
                                Entry t = task.getEntry(slot, i);
                                if (t == null) break;
                                t.setEnabled(false);
                            }
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else {
                            caller.send("unknown operation with task entries: " + op);
                            return true;
                        }
                    } else if (checkNoCase(entryIdString, "create", "c")) {
                        if (entryOperation == null) {
                            caller.send(slot + " constructor is empty");
                            return true;
                        }
                        StringBuilder buffer = new StringBuilder();
                        String constructor = entryOperation.concat(" ").concat(StringUtils.join(words, ' '));
                        Entry entry = task.addEntry(slot, constructor, commandSender);
                        buffer.append(slot.name).append(" created in ").append(task).append(": \n");
                        buffer.append("- ").append(entry);
                        caller.send(buffer.toString());
                        task.save();
                        return true;
                    } else if (checkNoCase(entryIdString, "run")) {
                        switch (slot) {
                            case INIT:
                                caller.send(task.toString() + RESET + " run init");
                                task.runInit(caller.getFields());
                                return true;
                            case ACTION:
                                caller.send(task.toString() + RESET + " run actions");
                                task.runActions(caller.getFields());
                                return true;
                            case REACTION:
                                caller.send(task.toString() + RESET + " run reactions");
                                task.runReactions(caller.getFields());
                                return true;
                            case CONDITION:
                                caller.send(task.toString() + RESET + " checked: " + task.checkConditions(caller.getFields()));
                                return true;
                            default:
                                caller.send(task.toString() + RESET + " can't run " + slot.setName);
                                return false;
                        }
                    } else if (checkNoCase(entryIdString, "add", "a", "+")) {
                        if (entryOperation == null) {
                            caller.send(slot + " constructor is empty");
                            return true;
                        }
                        StringBuilder buffer = new StringBuilder();
                        String constructor = entryOperation.concat(" ").concat(StringUtils.join(words, ' '));
                        Entry entry = task.addEntry(slot, constructor, commandSender);
                        entry.setEnabled(true);
                        buffer.append(slot.name).append(" added to ").append(task).append(RESET).append(": \n");
                        buffer.append("- ").append(entry);
                        caller.send(buffer.toString());
                        task.save();
                        return true;
                    } else if (entryId >= 0 && entryId < task.getEntryLength(slot)) {
                        Entry entry = task.getEntry(slot, entryId);
                        if (entryOperation == null) {
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(slot.name).append(" [").append(entryId).append("] in ");
                            buffer.append(task).append(RESET).append(" :\n").append("- ").append(entry);
                            caller.send(buffer.toString());
                            return true;
                        } else if (checkNoCase(entryOperation, "enable", "en", "on")) {
                            entry.setEnabled(true);
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(slot).append(" [").append(entryId).append("] enabled in ");
                            buffer.append(task).append(" :\n").append("- ").append(entry);
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if (checkNoCase(entryOperation, "run", "check")) {
                            switch (slot.type) {
                                case ACTION:
                                    caller.send(task.toString() + RESET + " run " + slot.name + " [" + entryId + "]");
                                    ((Action) entry).run(caller.getFields());
                                    return true;
                                case CONDITION:
                                    caller.send(task.toString() + RESET + " check " + slot.name + " [" + entryId + "]:" + ((Condition) entry).test(caller.getFields()));
                                    return true;
                                default:
                                    caller.send(task.toString() + RESET + " can't run " + slot.name + " [" + entryId + "]");
                                    return false;
                            }
                        } else if (checkNoCase(entryOperation, "disable", "dis", "off")) {
                            entry.setEnabled(false);
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(slot).append(" [").append(entryId).append("] disabled in ");
                            buffer.append(task).append(" :\n").append("- ").append(entry);
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if (checkNoCase(entryOperation, "remove")) {
                            entry.remove();
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(slot).append(" [").append(entryId).append("] removed from ");
                            buffer.append(task).append(RESET).append(" :\n").append("- ").append(entry);
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if (checkNoCase(entryOperation, "edit", "=")) {
                            if (words.isEmpty()) {
                                caller.send(slot + " constructor is empty");
                                return true;
                            }
                            entry = task.editEntry(slot, entryId, StringUtils.join(words, " "), commandSender);
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(slot).append(" [").append(entryId).append("] in ").append(task);
                            buffer.append(RESET).append(" now is :\n").append("- ").append(entry);
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else {
                            caller.send("wrong operation with " + slot + " [" + entryId + "]");
                            return true;
                        }
                    } else {
                        caller.send("wrong index or operation: " + entryIdString);
                        return true;
                    }
                } else {
                    caller.send("unknown action with task: " + op);
                    return true;
                }
            }
        } catch (Throwable e) {
            caller.handleException(e);
        }
        return true;
    }


    public static boolean check(String g, String... p) {
        for (String t : p) if (g.equals(t)) return true;
        return false;
    }

    public static boolean checkNoCase(String g, String... p) {
        for (String t : p) if (g.equalsIgnoreCase(t)) return true;
        return false;
    }
}
