package org.dpohvar.varscript.scheduler.condition;

import org.dpohvar.varscript.caller.Caller;
import org.dpohvar.varscript.scheduler.Task;
import org.dpohvar.varscript.scheduler.TaskCondition;
import org.dpohvar.varscript.vs.*;
import org.dpohvar.varscript.vs.Thread;
import org.dpohvar.varscript.vs.compiler.VSCompiler;
import org.dpohvar.varscript.vs.exception.SourceException;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 9:38
 */
public class VSRunCondition extends TaskCondition {

    final String param;
    CommandList commandList;
    private org.dpohvar.varscript.Runtime runtime;

    public VSRunCondition(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override
    public boolean check(Map<String, Object> environment) {
        if (commandList == null) return false;
        VarscriptProgram program = new VarscriptProgram(runtime, Caller.getCallerFor(getTask()));
        org.dpohvar.varscript.vs.Thread thread = new Thread(program);
        Scope scope = thread.pushFunction(commandList.build(program.getScope()), program).getScope();
        for (Map.Entry<String, Object> e : environment.entrySet()) {
            scope.defineVar(e.getKey(), e.getValue());
        }
        new ThreadRunner(thread).runThreads();
        try {
            return thread.pop(Boolean.class);
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    protected boolean register() {
        runtime = task.getScheduler().runtime;
        try {
            commandList = VSCompiler.compile(param);
            return true;
        } catch (SourceException e) {
            return false;
        }
    }

    @Override
    protected boolean unregister() {
        if (commandList == null) return false;
        commandList = null;
        return true;
    }

    public static String getType() {
        return "VS";
    }

    @Override
    public String toString() {
        return "VS" + (param == null || param.isEmpty() ? "" : " " + param);
    }

}
