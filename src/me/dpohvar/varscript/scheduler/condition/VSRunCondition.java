package me.dpohvar.varscript.scheduler.condition;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskCondition;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.exception.SourceException;

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
    private me.dpohvar.varscript.Runtime runtime;

    public VSRunCondition(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override public boolean check(Map<String,Object> environment) {
        if(commandList==null) return false;
        VarscriptProgram program = new VarscriptProgram(runtime, Caller.getCallerFor(getTask()));
        Thread thread = new Thread(program);
        Scope scope = thread.pushFunction(commandList.build(program.getScope()),program).getScope();
        for(Map.Entry<String,Object> e:environment.entrySet()){
            scope.defineVar(e.getKey(),e.getValue());
        }
        new ThreadRunner(thread).runThreads();
        try{
            return thread.pop(Boolean.class);
        } catch (Exception ignored){
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
        if(commandList==null) return false;
        commandList=null;
        return true;
    }

    public static String getType() {
        return "VS";
    }

    @Override public String toString(){
        return "VS "+param;
    }

}
