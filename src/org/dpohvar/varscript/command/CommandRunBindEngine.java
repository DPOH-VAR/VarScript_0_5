package org.dpohvar.varscript.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dpohvar.varscript.Runtime;
import org.dpohvar.varscript.caller.Caller;
import org.dpohvar.varscript.se.SECallerProgram;

import javax.script.ScriptEngine;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandRunBindEngine implements CommandExecutor {

    private final Runtime runtime;
    private final ScriptEngine engine;

    public CommandRunBindEngine(Runtime runtime, String language) {
        this.runtime = runtime;
        engine = runtime.getScriptEngine(language);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try {
            String source = StringUtils.join(strings, ' ');
            SECallerProgram program = new SECallerProgram(runtime, caller, engine);
            program.runScript(source);
        } catch (Throwable e) {
            caller.handleException(e);
        }
        return true;
    }
}
