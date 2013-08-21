package org.dpohvar.varscript.utils.reflect;

import org.bukkit.Bukkit;
import org.bukkit.Server;

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

    private static String verB = null;
    private static String verM = null;
    private static String preClassB = "org.bukkit.craftbukkit.";
    private static String preClassM = "net.minecraft.server.";

    static {
        Server server = Bukkit.getServer();
        Class<?> bukkitServerClass = server.getClass();
        String[] pas = bukkitServerClass.getName().split("\\.");
        if (pas.length == 5) {
            verB = pas[3];
            preClassB += verB + ".";
        }
        try {
            Method getHandle = bukkitServerClass.getDeclaredMethod("getHandle");
            Object handle = getHandle.invoke(server);
            Class handleServerClass = handle.getClass();
            pas = handleServerClass.getName().split("\\.");
            if (pas.length == 5) {
                verM = pas[3];
                preClassM += verM + ".";
            }
        } catch (Exception ignored) {
        }
    }

    public static Class getBukkitClass(String name) {
        try {
            return Class.forName(preClassB + name);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }

    public static Class getMinecraftClass(String name) {
        try {
            return Class.forName(preClassM + name);
        } catch (ClassNotFoundException ignored) {
            return null;
        }
    }


}
