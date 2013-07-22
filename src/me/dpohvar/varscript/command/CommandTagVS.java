package me.dpohvar.varscript.command;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.vs.compiler.CompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandTagVS implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try{
            if (strings.length==0){
                String tags = StringUtils.join(VSCompiler.geTags(),' ');
                caller.send("Tags: ".concat(tags));
            } else {
                HashSet<CompileRule> rules = new HashSet<CompileRule>();
                for (String tag:strings){
                    rules.addAll(VSCompiler.getRules(tag));
                }
                String commands = StringUtils.join(rules,' ');
                caller.send("Commands: ".concat(commands));
            }
        } catch (Throwable e){
            caller.handleException(e);
        }
        return true;
    }
}
