package me.dpohvar.varscript.caller;

import me.dpohvar.varscript.VarScript;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class CommandSenderCaller extends Caller {

    protected CommandSender sender;

    CommandSenderCaller(CommandSender sender){
        this.sender=sender;
    }

    @Override public CommandSender getInstance() {
        return sender;
    }

    @Override public void send(Object object){
        getInstance().sendMessage(VarScript.prefix_normal + ChatColor.RESET + ' '+  object);
    }

    @Override protected void onHandleException(Throwable exception){
        getInstance().sendMessage(
                VarScript.prefix_error + ChatColor.RED+ ' ' + exception.getClass().getSimpleName()
                        + '\n' + ChatColor.RESET+ exception.getMessage()
        );
        if(VarScript.instance.isDebug()) exception.printStackTrace();
    }

}
