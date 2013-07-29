package me.dpohvar.varscript.scheduler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 17.07.13
 * Time: 0:32
 */
public class TaskList extends ArrayList<Task> {

    public TaskList(){
    }

    public TaskList(Collection<? extends Task> c){
        super(c);
    }

}
