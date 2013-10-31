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
public class CommandPrintBindEngine implements CommandExecutor {

    private final Runtime runtime;
    private final ScriptEngine engine;
    private final String language;

    public CommandPrintBindEngine(Runtime runtime, String language) {
        this.runtime = runtime;
        this.language = language;
        engine = runtime.getEngine(language);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        if (engine == null) {
            caller.send(ChatColor.RED + "no script engine with name: " + ChatColor.YELLOW + language);
            return true;
        }
        try {
            String source = StringUtils.join(strings, ' ');
            SECallerProgram program = new SECallerProgram(runtime, caller, engine, null);
            caller.send(program.runScript(source));
        } catch (Throwable e) {
            caller.handleException(e);
        }
        return true;
    }
}
