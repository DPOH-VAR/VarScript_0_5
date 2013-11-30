import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.scheduler.Event;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskParser;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.InventoryEvent;
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
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused deprecation")
public class BUKKIT extends Event {

    private RegisteredListener registeredListener;
    private HandlerList handlerList;
    boolean registered = true;

    public BUKKIT(Task task, String type, String constructor, CommandSender sender) {
        super(task, type, constructor);
        String eventClassName = constructor.split(" ")[0];
        final Class<? extends org.bukkit.event.Event> clazz = getEventClass(eventClassName);
        Map<String, String> params = TaskParser.parseMap(constructor.substring(eventClassName.length()));
        EventPriority priority = Converter.convert(EventPriority.values(), params.get("priority"));
        boolean ignoreCancelled = params.containsKey("ignorecancelled") || params.containsKey("listen");

        EventExecutor executor = new EventExecutor() {
            public void execute(Listener listener, org.bukkit.event.Event event) throws EventException {
                if (!clazz.isInstance(event)) return;
                HashMap<String, Object> environment = new HashMap<String, Object>();
                makeEnvironment(event, environment);
                call(environment);
            }
        };
        if (priority == null) priority = EventPriority.NORMAL;
        registeredListener = new RegisteredListener(null, executor, priority, VarScript.instance, ignoreCancelled);
        handlerList = getEventListeners(getRegistrationClass(clazz));
    }

    public boolean register() {
        handlerList.register(registeredListener);
        return true;
    }

    public void unregister() {
        handlerList.unregister(registeredListener);
    }


    public static HandlerList getEventListeners(Class<? extends org.bukkit.event.Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    public static Class<? extends org.bukkit.event.Event> getRegistrationClass(Class<? extends org.bukkit.event.Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(org.bukkit.event.Event.class)
                    && org.bukkit.event.Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(org.bukkit.event.Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName());
            }
        }
    }

    private static final String[] classPrefix = new String[]{
            "org.bukkit.event.",
            "org.bukkit.event.block.",
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

    public static Class<? extends org.bukkit.event.Event> getEventClass(String className) {
        Class<? extends org.bukkit.event.Event> eventClass = null;
        try {
            return (Class<? extends org.bukkit.event.Event>) me.dpohvar.varscript.Runtime.libLoader.loadClass(className);
        } catch (Exception ignored) {
        }
        if (eventClass == null) for (String prefix : classPrefix)
            try {
                try {
                    return (Class<? extends org.bukkit.event.Event>) me.dpohvar.varscript.Runtime.libLoader.loadClass(prefix + className);
                } catch (Exception ignored) {
                }
            } catch (Exception ignored) {
            }
        return null;
    }

    public static void makeEnvironment(org.bukkit.event.Event event, Map<String, Object> environment) {
        environment.put("Event", event);
        if (event instanceof PlayerEvent) {
            environment.put("Player", ((PlayerEvent) event).getPlayer());
        } else if (event instanceof BlockEvent) {
            environment.put("Block", ((BlockEvent) event).getBlock());
        } else if (event instanceof InventoryEvent) {
            environment.put("Inventory", ((InventoryEvent) event).getInventory());
            environment.put("Viewers", ((InventoryEvent) event).getViewers());
        } else if (event instanceof EntityEvent) {
            environment.put("Entity", ((EntityEvent) event).getEntity());
            environment.put("EntityType", ((EntityEvent) event).getEntityType());
        } else if (event instanceof HangingEvent) {
            environment.put("Entity", ((HangingEvent) event).getEntity());
        } else if (event instanceof PluginEvent) {
            environment.put("Plugin", ((PluginEvent) event).getPlugin());
        } else if (event instanceof VehicleEvent) {
            environment.put("Entity", ((VehicleEvent) event).getVehicle());
        } else if (event instanceof WeatherEvent) {
            environment.put("World", ((WeatherEvent) event).getWorld());
        } else if (event instanceof WorldEvent) {
            environment.put("World", ((WorldEvent) event).getWorld());
            if (event instanceof ChunkEvent) {
                environment.put("Chunk", ((ChunkEvent) event).getChunk());
            }
        }
    }


    public static String help() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("format: BUKKIT {event class} [priority={priority}] [ignorecalcelled]\n");
        buffer.append("default priority: NORMAL\n");
        buffer.append("Example: EntityTeleportEvent priority=LOW \n");
        buffer.append("Environment:\n");
        buffer.append("$Event - fired event\n");
        buffer.append("$Player - player (only for player events)\n");
        buffer.append("$Block - block (only for block events)\n");
        return buffer.toString();
    }

    public static String description() {
        return "called on click to block";
    }

}
