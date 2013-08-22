package me.dpohvar.varscript.command;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.se.SECallerProgram;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.script.ScriptEngine;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandRun implements CommandExecutor {

    private final Runtime runtime;

    public CommandRun(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try {
            if (strings.length == 0) return false;
            ScriptEngine engine = runtime.getScriptEngine(strings[0]);
            if (engine == null) {
                caller.send(ChatColor.RED + "no script engine with name: " + ChatColor.YELLOW + strings[0]);
                return true;
            }
            SECallerProgram program = new SECallerProgram(runtime, caller, engine);
            String source = StringUtils.join(strings, ' ', 1, strings.length);
            program.runScript(source);
        } catch (Throwable e) {
            caller.handleException(e);
        }
        return true;
    }
}
