import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Action;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.vs.CommandList;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.VarscriptProgram;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.exception.SourceException;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class VS extends Action {

    Runtime runtime = task.scheduler.getRuntime();
    CommandList cmd;

    public VS(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
    }

    @Override
    public void run(Map<String, Object> environment) {
        Caller caller = Caller.getCallerFor(task);
        VarscriptProgram program = new VarscriptProgram(runtime, caller, null, environment);
        Thread thread = new Thread(program);
        thread.pushFunction(cmd.build(program.getScope()), program);
        new ThreadRunner(thread).runThreads();
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
        buffer.append("run varscript code\n");
        buffer.append("example: VS @Player \"kick\" KICK");
        return buffer.toString();
    }

    public static String description() {
        return "run varscript";
    }
}
