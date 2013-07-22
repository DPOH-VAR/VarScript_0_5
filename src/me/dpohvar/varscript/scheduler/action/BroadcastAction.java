package me.dpohvar.varscript.scheduler.action;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskAction;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.exception.SourceException;
import org.bukkit.Bukkit;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 9:38
 */
public class BroadcastAction extends TaskAction {

    final String param;
    private String broadcast;

    public BroadcastAction(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override public void run(Map<String,Object> environment) {
        if(broadcast!=null) Bukkit.broadcastMessage(param);
    }

    @Override
    protected boolean register() {
        try{
            broadcast = param;
            error = false;
            return true;
        } catch (Exception ignored){
            return true;
        }
    }

    @Override
    protected boolean unregister() {
        if(broadcast==null) return false;
        broadcast = null;
        return true;
    }

    public static String getType() {
        return "BROADCAST";
    }

    @Override public String toString(){
        return "BROADCAST "+param;
    }

}
