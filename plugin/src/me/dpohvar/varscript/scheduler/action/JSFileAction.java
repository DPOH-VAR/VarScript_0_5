package me.dpohvar.varscript.scheduler.action;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskAction;
import me.dpohvar.varscript.se.SEFileProgram;
import me.dpohvar.varscript.se.SEProgram;

import javax.script.ScriptEngine;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 9:38
 */
public class JSFileAction extends TaskAction {

    final String param;
    private String file;
    private String[] params;
    ScriptEngine engine;
    private me.dpohvar.varscript.Runtime runtime;

    public JSFileAction(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    public void run(Map<String, Object> environment) {
        if (engine == null) return;
        Caller caller = Caller.getCallerFor(getTask());
        SEProgram program = new SEFileProgram(runtime, caller, engine, params);
        for (Map.Entry<String, Object> e : environment.entrySet()) {
            program.putToEnvironment(e.getKey(), e.getValue());
        }
        try {
            program.runScript(runtime.scriptManager.readScriptFile(engine.getFactory().getLanguageName(), file));
        } catch (Exception e) {
            caller.handleException(e);
        }
    }

    @Override
    protected boolean register() {
        runtime = task.getScheduler().runtime;
        String[] pp = param.split(" ");
        if (pp.length < 1) return false;
        file = pp[0];
        if (!file.matches("[a-zA-Z][a-zA-Z0-9_]*")) return false;
        params = new String[pp.length - 1];
        System.arraycopy(pp, 1, params, 0, params.length);
        engine = runtime.getEngine("js");
        if (engine == null) return false;
        return true;
    }

    @Override
    protected boolean unregister() {
        if (engine == null) return false;
        engine = null;
        return true;
    }

    public static String getType() {
        return "JSFILE";
    }

    @Override
    public String toString() {
        return "JSFILE" + (param == null || param.isEmpty() ? "" : " " + param);
    }

}
