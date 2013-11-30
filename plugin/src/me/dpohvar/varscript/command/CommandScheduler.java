package me.dpohvar.varscript.command;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.EntrySlot;
import me.dpohvar.varscript.scheduler.Scheduler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static org.bukkit.ChatColor.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandScheduler implements CommandExecutor {

    private final me.dpohvar.varscript.Runtime runtime;

    public CommandScheduler(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try {
            Scheduler scheduler = runtime.getScheduler();
            Queue<String> words = new LinkedList<String>(Arrays.asList(strings));
            String cmd = words.poll();
            if (cmd == null || cmd.isEmpty()) {
                String r = "scheduler" + (scheduler.isEnabled() ? " is enabled" : "is disabled");
                caller.send(r);
                return true;
            } else if (checkNoCase(cmd, "enable", "on")) {
                scheduler.setEnabled(true);
                String r = "scheduler" + (scheduler.isEnabled() ? " is enabled" : "is disabled");
                caller.send(r);
                return true;
            } else if (checkNoCase(cmd, "disable", "off")) {
                scheduler.setEnabled(false);
                String r = "scheduler" + (scheduler.isEnabled() ? " is enabled" : "is disabled");
                caller.send(r);
                return true;
            } else if (checkNoCase(cmd, "reload", "r", "!")) {
                scheduler.reload();
                String r = "scheduler" + " reloaded";
                caller.send(r);
                return true;
            }
            EntrySlot slot = EntrySlot.getByName(cmd);
            if (slot == null) {
                caller.send("unknown parameter: " + cmd);
                return true;
            }
            Map<String, Constructor> constructors = scheduler.getConstructors(slot.type);
            cmd = words.poll();
            if (cmd == null) {
                StringBuilder buffer = new StringBuilder();
                buffer.append(slot.type.setName).append(':');
                for (Map.Entry<String, Constructor> e : constructors.entrySet()) {
                    buffer.append('\n').append(GREEN).append(e.getKey());
                    try {
                        Class clazz = e.getValue().getDeclaringClass();
                        String description = (String) clazz.getMethod("description").invoke(null);
                        buffer.append(RESET).append(" - ").append(YELLOW).append(description);
                    } catch (Exception ignored) {
                    }
                }
                caller.send(buffer.toString());
                return true;
            }
            Constructor constructor = constructors.get(cmd);
            if (constructor == null) {
                caller.send("no " + slot.setName + " with name: " + cmd);
                return true;
            }
            StringBuilder buffer = new StringBuilder();
            buffer.append(slot.type.name).append(' ').append(GREEN).append(cmd);
            Class clazz = constructor.getDeclaringClass();
            try {
                String description = (String) clazz.getMethod("description").invoke(null);
                buffer.append(RESET).append(" - ").append(YELLOW).append(description);
            } catch (Exception ignored) {
            }
            try {
                String help = (String) clazz.getMethod("help").invoke(null);
                buffer.append('\n').append(RESET).append(help);
            } catch (Exception ignored) {
            }
            caller.send(buffer.toString());
            return true;
        } catch (Throwable e) {
            caller.handleException(e);
        }
        return true;
    }

    public static boolean checkNoCase(String g, String... p) {
        for (String t : p) if (g.equalsIgnoreCase(t)) return true;
        return false;
    }
}
