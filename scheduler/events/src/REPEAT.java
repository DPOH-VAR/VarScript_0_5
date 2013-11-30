import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.scheduler.Event;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskParser;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused deprecation")
public class REPEAT extends Event {

    private Listener listener;
    long period = 1;
    long delay = 0;
    BukkitTask task;

    public REPEAT(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
        Map<String, String> data = TaskParser.parseMap(params);
        period = Long.parseLong(data.get("period"));
        if (data.containsKey("delay")) Long.parseLong(data.get("delay"));
    }

    public boolean register() {
        if (task != null) return false;
        task = new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<String, Object> environment = new HashMap<String, Object>();
                environment.put("BukkitTask", task);
                call(environment);
            }
        }.runTaskTimer(VarScript.instance, delay, period);
        return true;
    }

    @SuppressWarnings("deprecation")
    public void unregister() {
        if (task != null) task.cancel();
        task = null;
    }

    public static String help() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("format: REPEAT period={ticks} [delay={ticks}]\n");
        buffer.append("optional arguments: delay after initialization\n");
        buffer.append("Environment:\n");
        buffer.append("$BukkitTask - BukkitTask object\n");
        buffer.append("$Player - player\n");
        buffer.append("$Message - sent message");
        return buffer.toString();
    }

    public static String description() {
        return "called on player chat";
    }
}
