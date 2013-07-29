package me.dpohvar.varscript.scheduler.action;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskAction;
import me.dpohvar.varscript.utils.ScriptManager;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.VSCompiler;

import java.io.*;
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
    private me.dpohvar.varscript.Runtime runtime;

    public VSFileAction(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override public void run(Map<String,Object> environment) {
        InputStream input = null;
        try {
            VarscriptProgram program = new VarscriptProgram(runtime, Caller.getCallerFor(getTask()));
            Scope scope = program.getScope();
            Function function = null;
            input = manager.openScriptFile("vsbin",param);
            if(input!=null) {
                function = VSCompiler.read(input).build(scope);
                input.close();
            }
            String source = manager.readScriptFile("vs",param);
            if(source!=null){
                function = VSCompiler.compile(source,param).build(scope);
            }
            if(function==null) return;
            Thread thread = new Thread(program);
            Scope mainScope = thread.pushFunction(function,program).getScope();
            for(Map.Entry<String,Object> e:environment.entrySet()){
                mainScope.defineVar(e.getKey(), e.getValue());
            }
            new ThreadRunner(thread).runThreads();
        } catch (Exception ignored) {
        } finally {
            if(input!=null) try {
                input.close();
            } catch (Exception ignored) {
            }
        }
    }

    @Override protected boolean register() {
        if(param==null) return false;
        if(!param.matches("[A-Za-z0-9_\\-]+")) return false;
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

    @Override public String toString(){
        return "VSFILE "+param;
    }

}
