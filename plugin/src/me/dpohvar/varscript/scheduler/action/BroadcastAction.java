package me.dpohvar.varscript.scheduler.action;

import me.dpohvar.powernbt.utils.StringParser;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskAction;
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
    private String message;

    public BroadcastAction(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    public void run(Map<String, Object> environment) {
        if (message != null) Bukkit.broadcastMessage(message);
    }

    @Override
    protected boolean register() {
        try {
            message = StringParser.parse(param);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    protected boolean unregister() {
        if (message == null) return false;
        message = null;
        return true;
    }

    public static String getType() {
        return "BROADCAST";
    }

    @Override
    public String toString() {
        return "BROADCAST" + (param == null || param.isEmpty() ? "" : " " + param);
    }

}
