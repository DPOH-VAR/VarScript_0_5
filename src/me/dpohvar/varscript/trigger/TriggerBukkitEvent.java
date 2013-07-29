package me.dpohvar.varscript.trigger;

import me.dpohvar.varscript.VarScript;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.server.PluginEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.ChunkEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 1:34
 */
public class TriggerBukkitEvent<T extends Event> implements Trigger<T> {

    private RegisteredListener registeredListener;
    private HandlerList handlerList;
    private TriggerRunner<T> runner;

    public TriggerBukkitEvent(final Class<T> clazz, EventPriority priority, TriggerRunner<T> runner){
        this.runner = runner;
        EventExecutor executor = new EventExecutor() {
            public void execute(Listener listener, Event event) throws EventException {if(clazz.isInstance(event)) handle((T)event);
            }
        };
        registeredListener = new RegisteredListener(null,executor,priority, VarScript.instance,false);
        handlerList = getEventListeners(getRegistrationClass(clazz));
        handlerList.register(registeredListener);
    }

    @Override public void unregister() {
        handlerList.unregister(registeredListener);
    }

    @Override public void handle(T event) {
        runner.run(event);
    }


    public static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName());
            }
        }
    }

    public static HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }






    private static final String[] classPrefix = new String[]{
            "org.bukkit.event.",
            "org.bukkit.event.enchantment.",
            "org.bukkit.event.entity.",
            "org.bukkit.event.hanging.",
            "org.bukkit.event.inventory.",
            "org.bukkit.event.painting.",
            "org.bukkit.event.player.",
            "org.bukkit.event.server.",
            "org.bukkit.event.vehicle.",
            "org.bukkit.event.weather.",
            "org.bukkit.event.world.",
            "me.dpohvar.varscript.event.",
    };

    public static void makeEnvironment(Event event,Map<String,Object> environment){
        environment.put("Event",event);
        if(event instanceof PlayerEvent) {
            environment.put("Player",((PlayerEvent)event).getPlayer());
        } else if (event instanceof BlockEvent) {
            environment.put("Block",((BlockEvent)event).getBlock());
        } else if (event instanceof InventoryEvent) {
            environment.put("Inventory",((InventoryEvent)event).getInventory());
            environment.put("Viewers",((InventoryEvent)event).getViewers());
            if (event instanceof InventoryInteractEvent) {
                environment.put("Entity",((InventoryInteractEvent)event).getWhoClicked());
            }
        } else if (event instanceof EntityEvent) {
            environment.put("Entity",((EntityEvent)event).getEntity());
            environment.put("EntityType",((EntityEvent)event).getEntityType());
        } else if (event instanceof HangingEvent) {
            environment.put("Entity",((HangingEvent)event).getEntity());
        } else if (event instanceof PluginEvent) {
            environment.put("Plugin",((PluginEvent)event).getPlugin());
        } else if (event instanceof VehicleEvent) {
            environment.put("Entity",((VehicleEvent)event).getVehicle());
        } else if (event instanceof WeatherEvent) {
            environment.put("World",((WeatherEvent)event).getWorld());
        } else if (event instanceof WorldEvent) {
            environment.put("World",((WorldEvent)event).getWorld());
            if (event instanceof ChunkEvent) {
                environment.put("Chunk",((ChunkEvent)event).getChunk());
            }
        }
    }

    public static Class<? extends Event> getEventClass(String className){
        Class<? extends Event> eventClass = null;
        try {
            return (Class<? extends Event>) Class.forName(className);
        } catch (Exception ignored){}
        if (eventClass==null) for(String prefix:classPrefix) try {
            try{
                return (Class<? extends Event>) Class.forName(prefix+className);
            } catch (Exception ignored){
                return (Class<? extends Event>) Class.forName(prefix+className+"TaskEvent");
            }
        } catch (Exception ignored){}
        return null;
    }
}
