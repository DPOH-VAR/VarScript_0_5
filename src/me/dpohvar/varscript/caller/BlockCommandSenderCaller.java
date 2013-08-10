package me.dpohvar.varscript.caller;

import me.dpohvar.varscript.VarScript;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class BlockCommandSenderCaller extends CommandSenderCaller {

    BlockCommandSenderCaller(BlockCommandSender sender){
        super(sender);
    }

    @Override public BlockCommandSender getInstance() {
        return (BlockCommandSender)sender;
    }

    public Location getLocation(){
        return getInstance().getBlock().getLocation();
    }





}
