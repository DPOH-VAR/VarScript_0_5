package org.dpohvar.varscript.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dpohvar.varscript.Runtime;
import org.dpohvar.varscript.caller.Caller;
import org.dpohvar.varscript.se.SEFileProgram;

import javax.script.ScriptEngine;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandFileBindEngine implements CommandExecutor {

    private final Runtime runtime;
    private final ScriptEngine engine;
    private final String alias;

    public CommandFileBindEngine(Runtime runtime, String language, String alias) {
        this.runtime = runtime;
        this.alias = alias;
        engine = runtime.getScriptEngine(language);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try {
            if (strings.length == 0) return false;
            String script = runtime.scriptManager.readScriptFile(alias, strings[0]);
            if (script == null) {
                caller.send(ChatColor.RED + "no script file with name: " + ChatColor.YELLOW + strings[0]);
                return true;
            }
            String[] args = new String[strings.length - 1];
            System.arraycopy(strings, 1, args, 0, args.length);
            SEFileProgram program = new SEFileProgram(runtime, caller, engine, args);
            program.runScript(script);
        } catch (Throwable e) {
            caller.handleException(e);
        }
        return true;
    }
}
