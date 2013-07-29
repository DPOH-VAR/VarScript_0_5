package me.dpohvar.varscript.command;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.VarscriptProgram;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandRunVS implements CommandExecutor {

    private final me.dpohvar.varscript.Runtime runtime;

    public CommandRunVS(me.dpohvar.varscript.Runtime runtime){
        this.runtime = runtime;
    }

    @Override public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try{
            String source = StringUtils.join(strings,' ');
            CommandList cmd = VSCompiler.compile(source);
            VarscriptProgram program = new VarscriptProgram(runtime,caller);
            Thread thread = new me.dpohvar.varscript.vs.Thread(program);
            Context cc = thread.pushFunction(cmd.build(program.getScope()),program);
            new ThreadRunner(thread).runThreads();
        } catch (Throwable e){
            caller.handleException(e);
        }
        return true;
    }
}
