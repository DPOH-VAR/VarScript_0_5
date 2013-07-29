package me.dpohvar.varscript.scheduler.action;

import me.dpohvar.varscript.scheduler.Scheduler;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskList;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 9:38
 */
public class TaskOperateAction extends me.dpohvar.varscript.scheduler.TaskAction {

    final String param;
    private int operation;
    private String target;

    public TaskOperateAction(Task task, String param) {
        super(task);
        this.param = param;
    }

    @Override public void run(Map<String,Object> environment) {
        if(param==null) return;
        if(target.isEmpty()) target = task.getName();
        TaskList list;
        Scheduler scheduler = task.getScheduler();
        if(target.endsWith("*")) {
            list = scheduler.getTasks(target.substring(0,target.length()-1));
        } else {
            list = new TaskList();
            Task t = scheduler.getTask(target);
            if(t!=null) list.add(t);
        }
        if(operation==0) for(Task t:list){
            t.setEnabled(true);
        }
        else if(operation==1) for(Task t:list){
            t.setEnabled(false);
        }
        else if(operation==3) for(Task t:list){
            if(t.check(environment)) t.run(environment);
        }
    }

    @Override protected boolean register() {
        try{
            if(param==null||param.isEmpty()) return false;
            String[] ss = param.split(" ");
            if(ss.length==0 || ss.length>2) return false;
            if("ENABLE".equalsIgnoreCase(ss[0])) operation = 0;
            else if("DISABLE".equalsIgnoreCase(ss[0])) operation = 1;
            else if("RUN".equalsIgnoreCase(ss[0])) operation = 2;
            else if("CHECKRUN".equalsIgnoreCase(ss[0])) operation = 3;
            else return false;
            if(ss.length==2) target = ss[1];
            else target = "";
            return true;
        } catch (Exception ignored){
            return false;
        }
    }

    @Override
    protected boolean unregister() {
        if(target==null) return false;
        target = null;
        return true;
    }

    public static String getType() {
        return "TASK";
    }

    @Override public String toString(){
        return "TASK "+param;
    }

}
