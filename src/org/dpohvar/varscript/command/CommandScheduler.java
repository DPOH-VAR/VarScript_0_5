package org.dpohvar.varscript.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dpohvar.varscript.Runtime;
import org.dpohvar.varscript.caller.Caller;
import org.dpohvar.varscript.scheduler.Scheduler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandScheduler implements CommandExecutor {

    private final org.dpohvar.varscript.Runtime runtime;
    public static ChatColor SUCCESS = ChatColor.AQUA;
    public static ChatColor ERROR = ChatColor.RED;
    public static ChatColor RESET = ChatColor.RESET;
    public static ChatColor INFO = ChatColor.YELLOW;

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
                String r = scheduler.getStatus() + "scheduler" + RESET + (scheduler.isEnabled() ? " is enabled" : "is disabled");
                caller.send(r);
                return true;
            } else if (checkNoCase(cmd, "enable", "en", "on")) {
                scheduler.enable();
                String r = scheduler.getStatus() + "scheduler" + RESET + (scheduler.isEnabled() ? " is enabled" : "is disabled");
                caller.send(r);
                return true;
            } else if (checkNoCase(cmd, "disable", "dis", "off")) {
                scheduler.disable();
                String r = scheduler.getStatus() + "scheduler" + RESET + (scheduler.isEnabled() ? " is enabled" : "is disabled");
                caller.send(r);
                return true;
            } else if (checkNoCase(cmd, "reload", "r")) {
                scheduler.reload();
                String r = scheduler.getStatus() + "scheduler" + RESET + " reloaded";
                caller.send(r);
                return true;
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
