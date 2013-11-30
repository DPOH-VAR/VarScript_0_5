import me.dpohvar.varscript.scheduler.Action;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskParser;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashMap;
import java.util.Map;

public class VAR extends Action {

    public VAR(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
    }

    @Override
    public void run(Map<String, Object> environment) {
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

    public static String help() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("format: VAR {var}={$value} [{var2}={$value2}...]\n");
        buffer.append("- sets variables for this task\n");
        buffer.append("format: VAR {task} {var}={$value} [{task2} {var}={$value}...]\n");
        buffer.append("- sets variables for other tasks\n");
        buffer.append("does not overwrite the environment\n");
        buffer.append("example: VAR lastMessage=$Message lastPlayer=$Player");
        return buffer.toString();
    }

    public static String description() {
        return "set variables";
    }
}
