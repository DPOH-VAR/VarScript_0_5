package me.dpohvar.varscript.caller;

import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.vs.Fieldable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.PrintStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivilegedActionException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:14
 */
public abstract class Caller implements Fieldable, Listener {

    private static HashMap<Object, Caller> callers = new HashMap<Object, Caller>();
    protected static HashMap<String, Caller> taskCallers = new HashMap<String, Caller>();
    private Throwable lastException;

    static { /* Register auto-update-caller on player join */
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                updatePlayer(event.getPlayer());
            }
        }, VarScript.instance);
    }


    public static void updatePlayer(Player player) {
        Caller caller = callers.get(player.getName());
        if (caller instanceof PlayerCaller) {
            ((PlayerCaller) caller).setPlayer(player);
        }
    }

    protected Caller() {
    }

    @Override
    public String toString() {
        return "Caller{" + getInstance() + "}";
    }

    public static void refresh() {
        HashSet<Entity> mark = new HashSet<Entity>();
        for (Map.Entry<Object, Caller> e : callers.entrySet()) {
            Object k = e.getKey();
            if (k instanceof Entity && ((Entity) k).isDead()) mark.add((Entity) k);
        }
        for (Object e : mark) callers.remove(e);
    }

    public static Caller getCallerFor(Object object) {

        if (object instanceof Caller) {
            Object ins = ((Caller) object).getInstance();
            if (ins instanceof Player) ins = ((Player) ins).getName();
            callers.put(ins, (Caller) object);
            return (Caller) object;
        } else if (object instanceof Task) {
            Caller c = taskCallers.get(((Task) object).getName());
            if (c != null) {
                ((TaskCaller) c).setTask((Task) object);
                return c;
            }
            c = new TaskCaller((Task) object);
            taskCallers.put(((Task) object).getName(), c);
            return c;
        } else if (object instanceof Player) {
            Caller c = callers.get(((Player) object).getName());
            if (c != null) {
                ((PlayerCaller) c).setPlayer((Player) object);
                return c;
            }
            c = new PlayerCaller((Player) object);
            callers.put(((Player) object).getName(), c);
            return c;
        } else if (object instanceof Task) {
            Caller c = callers.get(object);
            if (c != null) return c;
            c = new TaskCaller((Task) object);
            callers.put(object, c);
            return c;
        } else if (object instanceof Entity) {
            Caller c = callers.get(object);
            if (c != null) return c;
            c = new EntityCaller((Entity) object);
            callers.put(object, c);
            refresh();
            return c;
        } else if (object instanceof PrintStream) {
            Caller c = callers.get(object);
            if (c != null) return c;
            c = new PrintStreamCaller((PrintStream) object);
            callers.put(object, c);
            return c;
        } else if (object instanceof Block) {
            Caller c = callers.get(object);
            if (c != null) return c;
            c = new BlockCaller((Block) object);
            callers.put(object, c);
            return c;
        } else if (object instanceof BlockCommandSender) {
            Caller c = callers.get(object);
            if (c != null) return c;
            c = new BlockCaller(((BlockCommandSender) object).getBlock());
            callers.put(object, c);
            return c;
        } else if (object instanceof CommandSender) {
            Caller c = callers.get(object);
            if (c != null) return c;
            c = new CommandSenderCaller((CommandSender) object);
            callers.put(object, c);
            return c;
        } else {
            Caller c = callers.get(object);
            if (c != null) return c;
            c = new SimpleCaller(object);
            callers.put(object, c);
            return c;
        }
    }

    public abstract Object getInstance();

    public void send(Object message) {
        Bukkit.getLogger().info("" + message);
    }

    final public Throwable getLastException() {
        return lastException;
    }

    final public void handleException(Throwable exception) {
        try {
            if (exception instanceof UndeclaredThrowableException) {
                exception = exception.getCause();
            }
            if (exception instanceof PrivilegedActionException) {
                exception = exception.getCause();
            }
        } catch (Exception ignored) {
        }
        lastException = exception;
        onHandleException(exception);
    }

    protected void onHandleException(Throwable exception) {
        Bukkit.getConsoleSender().sendMessage(exception.getMessage());
        if (VarScript.instance.isDebug()) exception.printStackTrace();
    }

    public Location getLocation() {
        try {
            return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
        } catch (Exception e) {
            return null;
        }
    }


    Map<String, Object> fields = new HashMap<String, Object>();

    public Map<String, Object> getFields() {
        return fields;
    }

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
        fields.put(key, value);
    }

    @Override
    public void removeField(String key) {
        fields.remove(key);
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

    @Override
    public boolean hasField(String key) {
        return fields.containsKey(key);
    }
}
