package me.dpohvar.varscript.command;

import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.vs.VSNamedCommandList;
import me.dpohvar.varscript.vs.VSProgram;
import me.dpohvar.varscript.vs.VSThread;
import me.dpohvar.varscript.vs.VSThreadRunner;
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
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try{
            String source = StringUtils.join(strings,' ');
            VSNamedCommandList cmd = VSCompiler.compile(source);
            VSProgram program = new VSProgram(VarScript.instance.runtime,caller);
            VSThread thread = new VSThread(program);
            thread.pushFunction(cmd.build(program.getScope()),null);
            new VSThreadRunner(thread).runThreads();
        } catch (Throwable e){
            caller.handleException(e);
        }
        return true;
    }
}
