import me.dpohvar.varscript.task.Action;
import me.dpohvar.varscript.task.Task;
import me.dpohvar.varscript.task.TaskParser;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashMap;
import java.util.Map;

public class SET extends Action {

    public SET(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
    }

    @Override
    protected void run(Map<String, Object> environment) {
        LinkedHashMap<String, String> result = TaskParser.parseMap(getParams());
        Task current = task;
        for (Map.Entry<String, String> e : result.entrySet()) {
            if (e.getValue() == null) {
                current = task.scheduler.getTask(e.getKey());
                continue;
            }
            current.bindings.put(task.parseString(e.getKey()), task.parseObject(e.getValue(), environment));
        }
    }

    public boolean register() {
        if (getParams() == null || getParams().isEmpty()) return false;
        return true;
    }

    public void unregister() {
    }
}
