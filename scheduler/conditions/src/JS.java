import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Condition;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.se.SECallerProgram;
import org.bukkit.command.CommandSender;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.Map;

public class JS extends Condition {

    static ScriptEngine engine = Runtime.getEngine("js");

    static {
        assert engine != null;
    }

    CompiledScript script;

    public JS(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
    }

    @Override
    protected boolean check(Map<String, Object> environment) {
        Caller caller = Caller.getCallerFor(task);
        SECallerProgram program = new SECallerProgram(task.scheduler.getRuntime(), caller, engine, null, environment);
        Object result = program.runScript(script);
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
        buffer.append("format: JS {script}\n");
        buffer.append("run javascript\n");
        buffer.append("example: JS Player.getHealth() < 5");
        return buffer.toString();
    }

    public static String description() {
        return "check javascript condition";
    }
}
