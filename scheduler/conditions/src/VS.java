import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Condition;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.vs.CommandList;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.VarscriptProgram;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.exception.SourceException;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class VS extends Condition {

    Runtime runtime = task.scheduler.getRuntime();
    CommandList cmd;

    public VS(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
    }

    @Override
    protected boolean check(Map<String, Object> environment) {
        Caller caller = Caller.getCallerFor(task);
        VarscriptProgram program = new VarscriptProgram(runtime, caller, null, environment);
        Thread thread = new Thread(program);
        thread.pushFunction(cmd.build(program.getScope()), program);
        new ThreadRunner(thread).runThreads();
        Object result;
        try {
            result = thread.pop();
        } catch (Exception e) {
            return false;
        }
        if (result == null) return false;
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        if (result instanceof Number) {
            return !(result.equals(0) || result.equals(0.0));
        }
        if (result instanceof String) return !((String) result).isEmpty();
        return true;
    }

    public boolean register() {
        if (cmd != null) return false;
        try {
            cmd = VSCompiler.compile(getParams());
        } catch (SourceException e) {
            return false;
        }
        return true;
    }

    public void unregister() {
        cmd = null;
    }

    public static String help() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("format: VS {script}\n");
        buffer.append("run varscript\n");
        buffer.append("example: VS @Player HEALTH 5 <");
        return buffer.toString();
    }

    public static String description() {
        return "check varscript condition";
    }
}
