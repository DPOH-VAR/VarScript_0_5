package me.dpohvar.varscript.utils.reflect;

import me.dpohvar.varscript.VarScript;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 23.07.13
 * Time: 2:17
 */
public class ReflectBukkitUtils {
    public static void init() {
    }

    private static String preClassB = "org.bukkit.craftbukkit.";
    private static String preClassM = "net.minecraft.server.";
    private static boolean is_mcpc = false;

    static {
        if (Bukkit.getVersion().contains("MCPC-Plus")) is_mcpc = true;
        Server server = Bukkit.getServer();
        Class<?> bukkitServerClass = server.getClass();
        String[] pas = bukkitServerClass.getName().split("\\.");
        if (pas.length == 5) {
            String verB = pas[3];
            preClassB += verB + ".";
        }
        try {
            Method getHandle = bukkitServerClass.getDeclaredMethod("getHandle");
            Object handle = getHandle.invoke(server);
            Class handleServerClass = handle.getClass();
            pas = handleServerClass.getName().split("\\.");
            if (pas.length == 5) {
                String verM = pas[3];
                preClassM += verM + ".";
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * @param bukkitClass bukkit class
     * @param mcpcClass   mcpc class
     * @return class
     */
    public static Class getClass(String bukkitClass, String mcpcClass) {
        try {
            if (is_mcpc) {
                mcpcClass = mcpcClass.replaceAll("<cb>", preClassB);
                mcpcClass = mcpcClass.replaceAll("<nms>", preClassM);
                return Class.forName(mcpcClass);
            } else {
                bukkitClass = bukkitClass.replaceAll("<cb>", preClassB);
                bukkitClass = bukkitClass.replaceAll("<nms>", preClassM);
                return Class.forName(bukkitClass);
            }
        } catch (ClassNotFoundException e) {
            if (VarScript.instance.isDebug()) e.printStackTrace();
            return null;
        }
    }

    public Object getField(Object o, Class fieldClass) {
        Class c = o.getClass();
        Field field = null;
        for (Field f : c.getFields()) {
            if (f.getDeclaringClass().equals(fieldClass)) {
                field = f;
                break;
            }
        }
        if (field != null) for (Field f : c.getDeclaredFields()) {
            if (f.getDeclaringClass().equals(fieldClass)) {
                field = f;
                break;
            }
        }
        try {
            return field.get(o);
        } catch (Exception e) {
            if (VarScript.instance.isDebug()) e.printStackTrace();
            return null;
        }
    }

    public void setField(Object o, Class fieldClass, Object val) {
        Class c = o.getClass();
        Field field = null;
        for (Field f : c.getFields()) {
            if (f.getDeclaringClass().equals(fieldClass)) {
                field = f;
                break;
            }
        }
        if (field != null) for (Field f : c.getDeclaredFields()) {
            if (f.getDeclaringClass().equals(fieldClass)) {
                field = f;
                break;
            }
        }
        try {
            field.set(o, val);
        } catch (Exception e) {
            if (VarScript.instance.isDebug()) e.printStackTrace();
        }
    }


}
