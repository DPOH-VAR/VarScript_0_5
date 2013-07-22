package me.dpohvar.varscript.scheduler;

import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 19.07.13
 * Time: 23:27
 */
public enum Status {

    RUN(GREEN),
    ERROR(GOLD),
    INVALID(RED),
    HOLD(GRAY),
    DISABLED(DARK_GRAY);

    private ChatColor color;
    Status(ChatColor c){
        color=c;
    }
    public String toString(){
        return color.toString();
    }
}
