package me.dpohvar.varscript.command;

import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.scheduler.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 6:14
 */
public class CommandTask implements CommandExecutor {

    private final me.dpohvar.varscript.Runtime runtime;
    public static ChatColor SUCCESS = ChatColor.AQUA;
    public static ChatColor ERROR = ChatColor.RED;
    public static ChatColor RESET = ChatColor.RESET;
    public static ChatColor INFO = ChatColor.YELLOW;

    public CommandTask(me.dpohvar.varscript.Runtime runtime){
        this.runtime = runtime;
    }

    @Override public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Caller caller = Caller.getCallerFor(commandSender);
        try{
            Scheduler scheduler = runtime.scheduler;
            Queue<String> words = new LinkedList<String>(Arrays.asList(strings));
            String taskname = words.poll();
            if(taskname == null) taskname = "*";
            String op = words.poll();
            if(taskname.endsWith("*")){
                String query = taskname.substring(0,taskname.length()-1);
                TaskList tasks = scheduler.getTasks(query);
                if(op==null||checkNoCase(op,"view","v")){
                    StringBuilder buffer = new StringBuilder("Tasks for ").append(taskname).append(" :");
                    for(Task task:tasks){
                        buffer.append('\n').append(task.getStatus()).append(task).append(RESET);
                        String desc = task.getDescription();
                        if(desc!=null) buffer.append(' ').append(INFO).append(desc);
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op,"disable","off","dis")){
                    StringBuilder buffer = new StringBuilder("Disable tasks ").append(taskname).append(" :");
                    for(Task task:tasks){
                        //if(!task.isEnabled()) continue;
                        buffer.append('\n').append(task.getStatus()).append(task).append(RESET);
                        if(!task.isEnabled()) continue;
                        buffer.append(SUCCESS).append(" OK");
                        task.setEnabled(false);
                        task.save();
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op,"enable","on","en")){
                    StringBuilder buffer = new StringBuilder("Enable tasks ").append(taskname).append(" :");
                    for(Task task:tasks){
                        buffer.append('\n').append(task.getStatus()).append(task).append(RESET);
                        if(task.isEnabled()) continue;
                        buffer.append(SUCCESS).append(" OK");
                        task.setEnabled(true);
                        task.save();
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op,"remove")){
                    StringBuilder buffer = new StringBuilder("Remove tasks ").append(taskname).append(" :");
                    for(Task task:tasks){
                        buffer.append('\n').append(task.getStatus()).append(task).append(RESET).append(' ');
                        if(task.remove()){
                            buffer.append(SUCCESS).append("OK");
                        } else {
                            buffer.append(ERROR).append("ERROR");
                        }
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op,"save","s")){
                    StringBuilder buffer = new StringBuilder("Save tasks ").append(taskname).append(" :");
                    for(Task task:tasks){
                        buffer.append('\n').append(task.getStatus()).append(task).append(RESET)
                                .append(' ').append(task.getDescription());
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op,"rename","ren","rn")){
                    String name = words.poll();
                    if(name==null) {
                        caller.send("enter a new prefix for tasks");
                        return true;
                    }
                    StringBuilder buffer = new StringBuilder("Rename tasks ").append(taskname).append(" :");
                    for(Task task:tasks){
                        String suffix = task.getName().substring(query.length());
                        buffer.append('\n').append(task.getStatus()).append(task).append(RESET).append(' ');
                        if(task.rename(name+suffix)){
                            buffer.append(SUCCESS).append("RENAMED TO ").append(RESET)
                            .append(name).append(suffix);
                        } else {
                            buffer.append(ERROR).append("ERROR");
                        }
                        task.save();
                    }
                    caller.send(buffer);
                    return true;
                } else {
                    caller.send("unknown action with tasks: "+op);
                    return true;
                }
            } else { // name is single string
                Task task = scheduler.getTask(taskname);
                if (op!=null && checkNoCase(op,"create","cr","new","n")){
                    if(task!=null){
                        caller.send("task "+task.getStatus()+task+RESET+" already exists");
                        return true;
                    }
                    task = scheduler.loadTask(taskname);
                    if(task==null){
                        caller.send("can't create task "+taskname);
                        return true;
                    }
                    if(!words.isEmpty()) task.setDescription(StringUtils.join(words,' '));
                    caller.send("task "+task.getStatus()+task+RESET+" is created");
                    return true;
                }
                if(task==null){
                    caller.send("task "+taskname+" not found");
                    return true;
                }
                if(op==null||checkNoCase(op,"view","v")){
                    if(task==null){
                        caller.send("no task with name "+taskname);
                        return true;
                    } else {
                        StringBuilder buffer = new StringBuilder();
                        buffer.append(task.getStatus()).append(task);
                        String description = task.getDescription();
                        if(description!=null) buffer.append('\n').append(INFO).append(description);
                        int eventCount = task.getEventCount();
                        if(eventCount>0){
                            buffer.append('\n').append(RESET).append("Events:");
                            for(int i=0;i<eventCount;i++){
                                TaskEvent t = task.getEvent(i);
                                buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                            }
                        }
                        int conditionCount = task.getConditionCount();
                        if(conditionCount>0){
                            buffer.append('\n').append(RESET).append("Conditions:");
                            for(int i=0;i<conditionCount;i++){
                                TaskCondition t = task.getCondition(i);
                                buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                            }
                        }
                        int actionsCount = task.getActionCount();
                        if(actionsCount>0){
                            buffer.append('\n').append(RESET).append("Actions:");
                            for(int i=0;i<actionsCount;i++){
                                TaskAction t = task.getAction(i);
                                buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                            }
                        }
                        caller.send(buffer);
                        return true;
                    }
                } else if (checkNoCase(op,"reload","r","reset")){
                    task = scheduler.loadTask(taskname);
                    if(task==null){
                        caller.send("can't reload task "+taskname);
                        return true;
                    }
                    StringBuilder buffer = new StringBuilder();
                    buffer.append(task.getStatus()).append(task).append(RESET).append(" reloaded");
                    String description = task.getDescription();
                    if(description!=null) buffer.append('\n').append(INFO).append(description);
                    int eventCount = task.getEventCount();
                    if(eventCount>0){
                        buffer.append('\n').append(RESET).append("Events:");
                        for(int i=0;i<eventCount;i++){
                            TaskEvent t = task.getEvent(i);
                            buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                        }
                    }
                    int conditionCount = task.getConditionCount();
                    if(conditionCount>0){
                        buffer.append('\n').append(RESET).append("Conditions:");
                        for(int i=0;i<conditionCount;i++){
                            TaskCondition t = task.getCondition(i);
                            buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                        }
                    }
                    int actionsCount = task.getActionCount();
                    if(actionsCount>0){
                        buffer.append('\n').append(RESET).append("Actions:");
                        for(int i=0;i<actionsCount;i++){
                            TaskAction t = task.getAction(i);
                            buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                        }
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op,"disable","off","dis")){
                    StringBuilder buffer = new StringBuilder("disable task ");
                    buffer.append(task.getStatus()).append(task).append(RESET).append(" :");
                    if(task.isEnabled()) {
                        task.disable();
                        task.save();
                        buffer.append(SUCCESS).append(" OK");
                    } else {
                        buffer.append(RESET).append(" Already disabled");
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op,"enable","on","en")){
                    StringBuilder buffer = new StringBuilder("enable task ");
                    buffer.append(task.getStatus()).append(task).append(RESET).append(" :");
                    if(!task.isEnabled()) {
                        task.enable();
                        task.save();
                        buffer.append(SUCCESS).append(" OK");
                    }else{
                        buffer.append(RESET).append(" Already enabled");
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op,"remove")){
                    StringBuilder buffer = new StringBuilder("Remove task ")
                    .append(task.getStatus()).append(task).append(RESET).append(" : ");
                    if(task.remove()){
                        buffer.append(SUCCESS).append("OK");
                    } else {
                        buffer.append(ERROR).append("ERROR");
                    }
                    caller.send(buffer);
                    return true;
                } else if (checkNoCase(op,"save","s")){
                    task.save();
                    StringBuilder buffer = new StringBuilder("Task ")
                            .append(task.getStatus()).append(task).append(RESET).append(" saved");
                    caller.send(buffer.toString());
                    return true;
                } else if (checkNoCase(op,"description","desc","d")){
                    StringBuilder buffer = new StringBuilder();
                    if(words.isEmpty()) {
                        String desc = task.getDescription();
                        if(desc==null){
                            buffer.append(task.getStatus()).append(task).append(RESET).append(" description:\n");
                            buffer.append(INFO).append(task.getDescription());
                        } else {
                            buffer.append(task.getStatus()).append(task).append(RESET).append(" has no description");
                        }
                    } else {
                        String desc = StringUtils.join(words,' ');
                        if(desc.isEmpty()){
                            task.setDescription(null);
                            buffer.append("description for ").append(task.getStatus()).append(task).append(RESET);
                            buffer.append(" is removed");
                        } else {
                            task.setDescription(desc);
                            buffer.append("New description for task ").append(task.getStatus()).append(task);
                            buffer.append(RESET).append(" is set:\n").append(INFO).append(task.getDescription());
                        }
                        task.save();
                    }
                    caller.send(buffer.toString());
                    return true;
                } else if (checkNoCase(op,"rename","ren","rn")){
                    String name = words.poll();
                    if(name==null) {
                        caller.send("enter a new name for task");
                        return true;
                    }
                    StringBuilder buffer = new StringBuilder("Rename task ").append(task.getStatus()).append(task)
                    .append(RESET).append(" to ").append(name).append(" : ");
                    if(task.rename(name)){
                        buffer.append(SUCCESS).append("OK");
                    } else {
                        buffer.append(ERROR).append("ERROR");
                    }
                    caller.send(buffer);
                    task.save();
                    return true;
                } else if (checkNoCase(op,"copy")){
                    String name = words.poll();
                    if(name==null) {
                        caller.send("enter a name of new task");
                        return true;
                    }
                    StringBuilder buffer = new StringBuilder("Copy task ").append(task.getStatus()).append(task)
                            .append(RESET).append(" to ").append(name).append(" : ");
                    if(task.copy(name)!=null){
                        buffer.append(SUCCESS).append("OK");
                    } else {
                        buffer.append(ERROR).append("ERROR");
                    }
                    caller.send(buffer);
                    task.save();
                    return true;
                }
                TaskEntryType entryType = null;
                if(checkNoCase(op,"events","event","e")) entryType = TaskEntryType.EVENT;
                if(checkNoCase(op,"conditions","condition","c")) entryType = TaskEntryType.CONDITION;
                if(checkNoCase(op,"actions","action","a")) entryType = TaskEntryType.ACTION;

                if (entryType!=null){
                    String entryIdString = words.poll();
                    String entryOperation = words.poll();
                    int entryId = -1;
                    try{
                        entryId = Integer.parseInt(entryIdString);    
                    } catch (NumberFormatException ignored){
                    }
                    int count = task.getCount(entryType);
                    if(entryIdString==null||checkNoCase(entryIdString,"*","all")) {
                        if(entryOperation==null||checkNoCase(entryOperation,"view","v")){
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(task.getStatus()).append(task).append(' ').append(RESET);
                            switch (entryType){
                                case EVENT: buffer.append("events"); break;
                                case CONDITION: buffer.append("conditions"); break;
                                case ACTION: buffer.append("actions"); break;
                            }
                            for(int i=0;i<count;i++){
                                TaskEntry t = task.get(entryType,i);
                                buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                            }
                            caller.send(buffer.toString());
                            return true;
                        } else if(checkNoCase(entryOperation, "remove")) {
                            StringBuilder buffer = new StringBuilder("all ");
                            switch (entryType){
                                case EVENT: buffer.append("events"); break;
                                case CONDITION: buffer.append("conditions"); break;
                                case ACTION: buffer.append("actions"); break;
                            }
                            buffer.append(" removed in ").append(task.getStatus()).append(task).append(RESET).append(" :");
                            while(task.getCount(entryType)>0){
                                TaskEntry t = task.get(entryType,0);
                                buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                                t.remove();
                            }
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if(checkNoCase(entryOperation, "enable","en","on")) {
                            StringBuilder buffer = new StringBuilder("all ");
                            switch (entryType){
                                case EVENT: buffer.append("events"); break;
                                case CONDITION: buffer.append("conditions"); break;
                                case ACTION: buffer.append("actions"); break;
                            }
                            buffer.append(" enabled in ").append(task.getStatus()).append(task).append(RESET).append(" :");
                            for (int i=0;i<count;i++){
                                TaskEntry t = task.get(entryType,i);
                                t.enable();
                                buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                            }
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if(checkNoCase(entryOperation, "disable","dis","off")) {
                            StringBuilder buffer = new StringBuilder("all ");
                            switch (entryType){
                                case EVENT: buffer.append("events"); break;
                                case CONDITION: buffer.append("conditions"); break;
                                case ACTION: buffer.append("actions"); break;
                            }
                            buffer.append(" disabled in ").append(task.getStatus()).append(task).append(RESET).append(" :");
                            for (int i=0;i<count;i++){
                                TaskEntry t = task.get(entryType,i);
                                t.disable();
                                buffer.append('\n').append(t.getStatus()).append("- ").append(t);
                            }
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else {
                            caller.send("unknown operation with task entries: "+op);
                            return true;
                        }
                    } else if (checkNoCase(entryIdString,"create","c")){
                        if(entryOperation==null) {
                            caller.send(entryType+" constructor is empty");
                            return true;
                        }
                        StringBuilder buffer = new StringBuilder();
                        String constructor = entryOperation.concat(" ").concat(StringUtils.join(words,' '));
                        TaskEntry entry = task.add(entryType,constructor);
                        buffer.append(entryType).append(" created in ").append(task.getStatus()).append(task)
                        .append(": \n").append(entry.getStatus()).append("- ").append(entry);
                        caller.send(buffer.toString());
                        task.save();
                        return true;
                    } else if (checkNoCase(entryIdString,"add","a")){
                        if(entryOperation==null) {
                            caller.send(entryType+" constructor is empty");
                            return true;
                        }
                        StringBuilder buffer = new StringBuilder();
                        String constructor = entryOperation.concat(" ").concat(StringUtils.join(words,' '));
                        TaskEntry entry = task.add(entryType,constructor);
                        entry.setEnabled(true);
                        buffer.append(entryType).append(" added to ").append(task.getStatus()).append(task)
                        .append(": \n").append(entry.getStatus()).append("- ").append(entry);
                        caller.send(buffer.toString());
                        task.save();
                        return true;
                    } else if(entryId>=0 && entryId<count ) {
                        TaskEntry entry = task.get(entryType,entryId);
                        if(entryOperation==null||checkNoCase(entryOperation,"view","v")){
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(entryType).append(" [").append(entryId).append("] in ")
                            .append(task.getStatus()).append(task).append(" :\n")
                            .append(entry.getStatus()).append("- ").append(entry);
                            caller.send(buffer.toString());
                            return true;
                        } else if (checkNoCase(entryOperation,"enable","en","on")){
                            entry.enable();
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(entryType).append(" [").append(entryId).append("] enabled in ")
                            .append(task.getStatus()).append(task).append(" :\n")
                            .append(entry.getStatus()).append("- ").append(entry);
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if (checkNoCase(entryOperation,"disable","dis","off")){
                            entry.disable();
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(entryType).append(" [").append(entryId).append("] disabled in ")
                            .append(task.getStatus()).append(task).append(" :\n")
                            .append(entry.getStatus()).append("- ").append(entry);
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if (checkNoCase(entryOperation,"remove")){
                            entry.remove();
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(entryType).append(" [").append(entryId).append("] removed from ")
                                    .append(task.getStatus()).append(task).append(RESET).append(" :\n")
                                    .append(entry.getStatus()).append("- ").append(entry);
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else if (checkNoCase(entryOperation,"edit")){
                            if(words.isEmpty()) {
                                caller.send(entryType+" constructor is empty");
                                return true;
                            }
                            entry = task.edit(entryType,entryId,StringUtils.join(words," "));
                            StringBuilder buffer = new StringBuilder();
                            buffer.append(entryType).append(" [").append(entryId).append("] in ")
                                    .append(task.getStatus()).append(task).append(RESET).append(" now is :\n")
                                    .append(entry.getStatus()).append("- ").append(entry);
                            caller.send(buffer.toString());
                            task.save();
                            return true;
                        } else {
                            caller.send("wrong operation with "+entryType+" ["+entryId+"]");
                            return true;
                        }
                    } else {
                        caller.send("wrong index or operation: "+entryIdString);
                        return true;
                    }
                } else {
                    caller.send("unknown action with task: "+op);
                    return true;
                }
            }
        } catch (Throwable e){
            caller.handleException(e);
        }
        return true;
    }








    public static boolean check(String g,String... p){
        for(String t:p) if(g.equals(t)) return true;
        return false;
    }

    public static boolean checkNoCase(String g,String... p){
        for(String t:p) if(g.equalsIgnoreCase(t)) return true;
        return false;
    }
}
