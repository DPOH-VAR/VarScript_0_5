package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.vs.Scope;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Score;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleOfflinePlayer extends ConvertRule<OfflinePlayer> {

    public RuleOfflinePlayer() {
        super(10);
    }

    @Override
    public <V> OfflinePlayer convert(V object, me.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof Number) {
            int id = ((Number) object).intValue();
            for (World w : Bukkit.getWorlds()) {
                for (Entity e : w.getEntities()) {
                    if (e.getEntityId() == id) return (Player) e;
                }
            }
        }
        if (object instanceof String) return Bukkit.getOfflinePlayer(((String) object).trim());
        if (object instanceof Inventory) return convert(((Inventory) object).getHolder(), thread, scope);
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        try {
            if (object instanceof Score) return ((Score) object).getPlayer();
        } catch (NoClassDefFoundError ignored) {
        }

        throw nextRule;
    }

    @Override
    public Class<OfflinePlayer> getClassTo() {
        return OfflinePlayer.class;
    }
}
