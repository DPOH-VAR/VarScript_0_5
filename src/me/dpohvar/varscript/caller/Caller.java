package me.dpohvar.varscript.caller;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.vs.VSFieldable;
import me.dpohvar.varscript.vs.VSRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:14
 */
public abstract class Caller implements VSFieldable {

    private static HashMap<Object,Caller> callers = new HashMap<Object, Caller>();
    private Throwable lastException;
    private Runtime runtime;

    public Caller(){
    }

    public Runtime getRuntime(){
        return runtime;
    }

    public static Caller getCallerFor(Object object){

        if (object instanceof Caller){
            Object ins = ((Caller)object).getInstance();
            if(ins instanceof Player) ins = ((Player) ins).getName();
            callers.put(ins,(Caller)object);
            return (Caller)object;
        }

        else if (object instanceof Player){
            Caller c = callers.get(((Player) object).getName());
            if(c!=null){
                ((PlayerCaller)c).setPlayer((Player) object);
                return c;
            }
            c = new PlayerCaller((Player) object);
            callers.put(((Player) object).getName(),c);
            return c;
        }

        else if (object instanceof Entity){
            Caller c = callers.get(object);
            if(c!=null) return c;
            c = new EntityCaller((Entity) object);
            callers.put(object,c);
            return c;
        }

        else if (object instanceof PrintStream){
            Caller c = callers.get(object);
            if(c!=null) return c;
            c = new PrintStreamCaller((PrintStream) object);
            callers.put(object,c);
            return c;
        }

        else if (object instanceof Block){
            Caller c = callers.get(object);
            if(c!=null) return c;
            c = new BlockCaller((Block) object);
            callers.put(object,c);
            return c;
        }

        else {
            Caller c = callers.get(object);
            if(c!=null) return c;
            c = new SimpleCaller(object);
            callers.put(object,c);
            return c;
        }
    }

    public abstract Object getInstance();

    public void send(Object message){
        Bukkit.getLogger().info(""+message);
    }

    final public Throwable getLastException(){
        return lastException;
    }

    final public void handleException(Throwable exception){
        lastException = exception;
        onHandleException(exception);
    }

    protected void onHandleException(Throwable exception){
        Bukkit.getLogger().log(Level.WARNING,exception.getMessage());
        exception.printStackTrace();
    }

    public Location getLocation(){
        return new Location(Bukkit.getWorlds().get(0),0,0,0);
    }



    HashMap<String,Object> fields = new HashMap<String, Object>();
    @Override
    public Set<String> getAllFields() {
        return fields.keySet();
    }

    @Override
    public Object getField(String key) {
        return fields.get(key);
    }

    @Override
    public void setField(String key, Object value) {
        fields.put(key,value);
    }

    @Override
    public void removeField(String key) {
        fields.remove(key);
    }

    @Override
    public VSRunnable getConstructor() {
        return null;
    }

    @Override
    public VSFieldable getProto() {
        return null;
    }

    @Override
    public void setProto(VSFieldable proto) {
    }

    @Override
    public boolean hasField(String key) {
        return fields.containsKey(key);
    }

}
