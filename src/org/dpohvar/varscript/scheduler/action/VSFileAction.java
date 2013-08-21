package org.dpohvar.varscript.scheduler.action;

import org.dpohvar.varscript.caller.Caller;
import org.dpohvar.varscript.scheduler.Task;
import org.dpohvar.varscript.scheduler.TaskAction;
import org.dpohvar.varscript.utils.ScriptManager;
import org.dpohvar.varscript.vs.*;
import org.dpohvar.varscript.vs.Thread;
import org.dpohvar.varscript.vs.compiler.VSCompiler;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 9:38
 */
public class VSFileAction extends TaskAction {

    final String param;
    ScriptManager manager;
    private org.dpohvar.varscript.Runtime runtime;

    public VSFileAction(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    public void run(Map<String, Object> environment) {
        InputStream input = null;
        try {
            VarscriptProgram program = new VarscriptProgram(runtime, Caller.getCallerFor(getTask()));
            Scope scope = program.getScope();
            Function function = null;
            input = manager.openScriptFile("vsbin", param);
            if (input != null) {
                function = VSCompiler.read(input).build(scope);
                input.close();
            }
            String source = manager.readScriptFile("vs", param);
            if (source != null) {
                function = VSCompiler.compile(source, param).build(scope);
            }
            if (function == null) return;
            org.dpohvar.varscript.vs.Thread thread = new Thread(program);
            Scope mainScope = thread.pushFunction(function, program).getScope();
            for (Map.Entry<String, Object> e : environment.entrySet()) {
                mainScope.defineVar(e.getKey(), e.getValue());
            }
            new ThreadRunner(thread).runThreads();
        } catch (Exception ignored) {
        } finally {
            if (input != null) try {
                input.close();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected boolean register() {
        if (param == null) return false;
        if (param.contains(File.separator)) return false;
        runtime = task.getScheduler().runtime;
        manager = runtime.scriptManager;
        return true;
    }

    @Override
    protected boolean unregister() {
        manager = null;
        return true;
    }

    public static String getType() {
        return "VSFILE";
    }

    @Override
    public String toString() {
        return "VSFILE" + (param == null || param.isEmpty() ? "" : " " + param);
    }

}
