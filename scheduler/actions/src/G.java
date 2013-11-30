import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Action;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.se.SECallerProgram;
import org.bukkit.command.CommandSender;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;

public class G extends Action {

    static ScriptEngine engine = Runtime.getEngine("groovy");

    static {
        assert engine != null;
    }

    CompiledScript script;

    public G(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
    }

    @Override
    public void run(Map<String, Object> environment) {
        Caller caller = Caller.getCallerFor(task);
        SECallerProgram program = new SECallerProgram(task.scheduler.getRuntime(), caller, engine, null, environment);
        program.runScript(script);
    }

    public boolean register() {
        if (script != null) return false;
        try {
            script = ((Compilable) engine).compile(getParams());
        } catch (ScriptException e) {
            return false;
        }
        return true;
    }

    public void unregister() {
        script = null;
    }

    public static String help() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("format: G {script}\n");
        buffer.append("run groovy script\n");
        buffer.append("groovy lib must be installed\n");
        buffer.append("example: G Player.kickPlayer 'kick'");
        return buffer.toString();
    }

    public static String description() {
        return "run groovy script";
    }
}
