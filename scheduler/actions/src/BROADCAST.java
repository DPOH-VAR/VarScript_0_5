import me.dpohvar.varscript.task.Action;
import me.dpohvar.varscript.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class BROADCAST extends Action {

    public BROADCAST(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
    }

    @Override
    protected void run(Map<String, Object> environment) {
        Object v = task.parseObject(getParams(), environment);
        Bukkit.broadcastMessage(v.toString());
    }

    public boolean register() {
        return true;
    }

    public void unregister() {
    }
}
