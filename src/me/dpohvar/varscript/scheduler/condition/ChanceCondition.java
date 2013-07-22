package me.dpohvar.varscript.scheduler.condition;

import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskCondition;

import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 22.07.13
 * Time: 1:44
 */
public class ChanceCondition extends TaskCondition{

    private final String param;
    private double chance = 1;
    private Random random = null;

    public ChanceCondition(Task task,String param) {
        super(task);
        this.param = param;
    }

    @Override protected boolean register() {
        try{
            if(param.matches("[0-9]{0,3}%")){
                String t = param.substring(0,param.length()-1);
                int c = Integer.parseInt(t);
                if(c<0||c>100){
                    error = true;
                    return false;
                }
                chance = c/100.0;
            } else {
                double t = Double.parseDouble(param);
                if(t<0||t>1){
                    error = true;
                    return false;
                }
                chance = t;
            }
            random = new Random();
            error = false;
            return true;
        }catch (Exception ignored){
            error = true;
            return false;
        }
    }

    @Override protected boolean unregister() {
        if(random==null) return false;
        random = null;
        return true;
    }

    @Override public boolean check(Map<String, Object> environment) {
        if (random==null) return true;
        return random.nextDouble()>chance;
    }

    @Override public String toString(){
        return "CHANCE "+param;
    }
}
