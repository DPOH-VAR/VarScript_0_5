package me.dpohvar.varscript.caller;

import me.dpohvar.varscript.VarScript;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:24
 */
public class PlayerCaller extends EntityCaller {

    PlayerCaller(Player player){
        super(player);
    }

    void setPlayer(Player player){
        this.entity = player;
    }

    @Override
    public Player getInstance() {
        return (Player)entity;
    }

    @Override
    public void send(Object object){
        getInstance().sendRawMessage(VarScript.prefix_normal + ChatColor.RESET + ' '+  object);
    }

    @Override
    protected void onHandleException(Throwable exception){
        getInstance().sendRawMessage(
                VarScript.prefix_error + ChatColor.RESET+ ' ' + exception.getClass().getSimpleName()
                        + '\n' + exception.getMessage()
        );
        exception.printStackTrace();
    }

}
