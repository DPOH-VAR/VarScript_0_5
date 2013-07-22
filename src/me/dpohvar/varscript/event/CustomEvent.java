package me.dpohvar.varscript.event;

import me.dpohvar.varscript.vs.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 22.07.13
 * Time: 14:40
 */
public class CustomEvent extends Event implements Fieldable{
    private static final HandlerList handlers = new HandlerList();

    private final Map<String,Object> params;

    public CustomEvent fire(){
        Bukkit.getServer().getPluginManager().callEvent(this);
        return this;
    }

    public Map<String,Object> getAllParams(){
        return params;
    }

    public CustomEvent(String name){
        this.params = new HashMap<String, Object>();
        params.put("Name",name);
    }

    public CustomEvent(String name,Map<String,Object> params){
        this.params = params;
        params.put("Name",name);
    }

    public String getName(){
        Object t = params.get("Name");
        if(t==null) return null;
        return t.toString();
    }


    @Override public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public Set<String> getAllFields() {
        return params.keySet();
    }

    @Override
    public Object getField(String key) {
        return params.get(key);
    }

    @Override
    public void setField(String key, Object value) {
        params.put(key,value);
    }

    @Override
    public void removeField(String key) {
       params.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        return params.containsKey(key);
    }

    @Override
    public me.dpohvar.varscript.vs.Runnable getConstructor() {
        return null;
    }

    @Override
    public Fieldable getProto() {
        return null;
    }

    @Override
    public void setProto(Fieldable proto) {
    }
}
