import me.dpohvar.varscript.scheduler.Action;
import me.dpohvar.varscript.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class BROADCAST extends Action {

    public BROADCAST(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
    }

    @Override
    public void run(Map<String, Object> environment) {
        Object v = task.parseObject(getParams(), environment);
        if (v == null) Bukkit.broadcastMessage("");
        else Bukkit.broadcastMessage(v.toString());
    }

    public boolean register() {
        return true;
    }

    public void unregister() {
    }

    public static String help() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("format: BROADCAST {$message}\n");
        buffer.append("send a broadcast message\n");
        buffer.append("arguments: message");
        return buffer.toString();
    }

    public static String description() {
        return "broadcast message";
    }
}
