package me.dpohvar.varscript.command;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.vs.compiler.CompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandCommandVS implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try{
            Set<CompileRule> rules = VSCompiler.getRules();
            if (strings.length==0){
                String commands = StringUtils.join(rules,' ');
                caller.send("Commands: ".concat(commands));
            } else {
                String query = StringUtils.join(strings,"");
                for (CompileRule rule:rules){
                    if (rule.checkCondition(query)){
                        caller.send(rule.getDescription());
                        return true;
                    }
                }
                caller.send(ChatColor.RED+"command "+query+" not exists");
            }
        } catch (Throwable e){
            caller.handleException(e);
        }
        return true;
    }
}
