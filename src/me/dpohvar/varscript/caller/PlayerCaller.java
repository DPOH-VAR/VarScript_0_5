package me.dpohvar.varscript.caller;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class PlayerCaller extends CommandSenderCaller {

    PlayerCaller(Player player){
        super(player);
    }

    void setPlayer(Player player){
        this.sender = player;
    }

    @Override
    public Player getInstance() {
        return (Player)sender;
    }

    public Location getLocation(){
        return ((Player)sender).getLocation();
    }

}
