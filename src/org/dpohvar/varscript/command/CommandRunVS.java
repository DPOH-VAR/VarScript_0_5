package org.dpohvar.varscript.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dpohvar.varscript.Runtime;
import org.dpohvar.varscript.caller.Caller;
import org.dpohvar.varscript.vs.CommandList;
import org.dpohvar.varscript.vs.Thread;
import org.dpohvar.varscript.vs.ThreadRunner;
import org.dpohvar.varscript.vs.VarscriptProgram;
import org.dpohvar.varscript.vs.compiler.VSCompiler;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandRunVS implements CommandExecutor {

    private final org.dpohvar.varscript.Runtime runtime;

    public CommandRunVS(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try {
            String source = StringUtils.join(strings, ' ');
            CommandList cmd = VSCompiler.compile(source);
            VarscriptProgram program = new VarscriptProgram(runtime, caller);
            org.dpohvar.varscript.vs.Thread thread = new Thread(program);
            thread.pushFunction(cmd.build(program.getScope()), program);
            new ThreadRunner(thread).runThreads();
        } catch (Throwable e) {
            caller.handleException(e);
        }
        return true;
    }
}
