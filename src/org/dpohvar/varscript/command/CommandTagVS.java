package org.dpohvar.varscript.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.dpohvar.varscript.caller.Caller;
import org.dpohvar.varscript.vs.compiler.CompileRule;
import org.dpohvar.varscript.vs.compiler.VSCompiler;

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
        try {
            ChatColor[] colors = new ChatColor[]{ChatColor.YELLOW, ChatColor.AQUA};
            int i = 0;
            if (strings.length == 0) {
                StringBuilder buffer = new StringBuilder("Tags:");
                for (String tag : VSCompiler.geTags()) {
                    buffer.append(' ').append(colors[i++ % 2]).append(tag);
                }
                caller.send(buffer.toString());
            } else {
                HashSet<CompileRule> rules = new HashSet<CompileRule>();
                for (String tag : strings) {
                    rules.addAll(VSCompiler.getRules(tag));
                }
                StringBuilder buffer = new StringBuilder("Commands:");
                for (CompileRule rule : rules) {
                    buffer.append(' ').append(colors[i++ % 2]).append(rule);
                }
                caller.send(buffer.toString());
            }
        } catch (Throwable e) {
            caller.handleException(e);
        }
        return true;
    }
}
