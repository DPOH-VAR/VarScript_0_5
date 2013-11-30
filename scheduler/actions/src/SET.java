import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.scheduler.Action;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class SET extends Action {

    LinkedHashMap<String, String> data;

    public SET(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
        data = TaskParser.parseMap(params);
    }

    @Override
    public void run(Map<String, Object> environment) {
        Task.InsertResult binded = task.insertValues(data, environment);
        for (Object a : binded.list) {
            for (Map.Entry<String, Object> e : binded.map.entrySet())
                beanSet(a, e.getKey(), e.getValue());
        }
    }

    public boolean register() {
        if (data.size() < 2) return false;
        return true;
    }

    public void unregister() {
    }

    private void beanSet(Object a, String key, Object value) {
        Class clazz = a.getClass();
        String setter = "set" + String.valueOf(key.charAt(0)).toUpperCase() + key.substring(1);
        for (Method m : clazz.getMethods()) {
            String name = m.getName();
            if (!name.equals(setter) && !name.equals(key)) continue;
            Class[] params = m.getParameterTypes();
            if (params.length != 1) continue;
            Class param = params[0];
            value = convert(value, param);
            try {
                m.invoke(a, value);
            } catch (Exception ignored) {
            }
            return;
        }
    }

    private Object convert(Object a, Class clazz) {
        if (clazz.isInstance(a)) return a;
        if (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(boolean.class)) {
            if (a instanceof String) return !((String) a).isEmpty();
            if (a instanceof Number) return ((Number) a).doubleValue() != 0;
            if (a instanceof Boolean) return a;
        }
        if (clazz.isAssignableFrom(String.class)) {
            if (a instanceof Player) return ((Player) a).getName();
            return a.toString();
        }
        if (clazz.isAssignableFrom(byte.class) || clazz.isAssignableFrom(Byte.class)) {
            if (a instanceof Number) return ((Number) a).byteValue();
            return Byte.parseByte(a.toString());
        }
        if (clazz.isAssignableFrom(short.class) || clazz.isAssignableFrom(Short.class)) {
            if (a instanceof Number) return ((Number) a).shortValue();
            return Short.parseShort(a.toString());
        }
        if (clazz.isAssignableFrom(int.class) || clazz.isAssignableFrom(Integer.class)) {
            if (a instanceof Number) return ((Number) a).intValue();
            return Integer.parseInt(a.toString());
        }
        if (clazz.isAssignableFrom(long.class) || clazz.isAssignableFrom(Long.class)) {
            if (a instanceof Number) return ((Number) a).longValue();
            return Long.parseLong(a.toString());
        }
        if (clazz.isAssignableFrom(double.class) || clazz.isAssignableFrom(Double.class)) {
            if (a instanceof Number) return ((Number) a).doubleValue();
            return Double.parseDouble(a.toString());
        }
        if (clazz.isAssignableFrom(float.class) || clazz.isAssignableFrom(Float.class)) {
            if (a instanceof Number) return ((Number) a).floatValue();
            return Float.parseFloat(a.toString());
        }
        if (clazz.isAssignableFrom(boolean.class) || clazz.isAssignableFrom(Boolean.class)) {
            if (a instanceof Number) return !(a).equals(0);
            if (a instanceof String) return !((String) a).isEmpty();
            return true;
        }
        if (clazz.isAssignableFrom(Player.class)) {
            return Bukkit.getPlayer(a.toString());
        }
        if (clazz.isAssignableFrom(ItemStack.class)) {
            if (a instanceof Number) {
                return new ItemStack(((Number) a).intValue());
            }
            if (a instanceof Player) {
                return ((Player) a).getItemInHand();
            }
            Material m = Material.valueOf(a.toString());
            return new ItemStack(m);
        }
        if (clazz.isAssignableFrom(World.class)) {
            return Bukkit.getWorld(a.toString());
        }
        if (clazz.isAssignableFrom(org.bukkit.util.Vector.class)) {
            double up = 0;
            if (a instanceof String) {
                up = Double.parseDouble(a.toString());
            }
            if (a instanceof Number) {
                up = ((Number) a).doubleValue();
            }
            return new org.bukkit.util.Vector(0, up, 0);
        }
        if (clazz.isAssignableFrom(Location.class)) {
            if (a instanceof Entity) return ((Entity) a).getLocation();
            if (a instanceof Block) return ((Block) a).getLocation();
            if (a instanceof World) return ((World) a).getSpawnLocation();
        }
        if (clazz.isAssignableFrom(Block.class)) {
            if (a instanceof Entity) return ((Entity) a).getLocation().getBlock();
            if (a instanceof Location) return ((Location) a).getBlock();
            if (a instanceof World) return ((World) a).getSpawnLocation().getBlock();
        }
        if (clazz.isEnum()) {
            try {
                Object[] values = (Object[]) clazz.getMethod("values").invoke(null);
                return Converter.convert(values, a);
            } catch (Exception ignored) {
            }
        }
        throw new RuntimeException("can't convert to " + clazz.getSimpleName());
    }

    public static String help() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("format: SET {object} {property}={$value} [{property2}={$value2}...]\n");
        buffer.append("change object properties\n");
        buffer.append("example: SET $Event cancelled=true\n");
        buffer.append("example: SET $Player health=15 gameMode=ADVENTURE");
        return buffer.toString();
    }

    public static String description() {
        return "set properties of object";
    }
}
