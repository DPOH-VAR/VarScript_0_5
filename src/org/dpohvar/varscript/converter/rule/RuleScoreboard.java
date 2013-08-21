package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.vs.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleScoreboard extends ConvertRule<Scoreboard> {

    public RuleScoreboard() {
        super(10);
    }

    @Override
    public <V> Scoreboard convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof Number) {
            int id = ((Number) object).intValue();
            for (World w : Bukkit.getWorlds()) {
                for (Entity e : w.getEntities()) {
                    if (e.getEntityId() == id) return ((Player) e).getScoreboard();
                }
            }
        }
        if (object instanceof String) return Bukkit.getPlayer(((String) object).trim()).getScoreboard();
        if (object instanceof Entity) return ((Player) object).getScoreboard();
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        if (object instanceof Score) return ((Score) object).getScoreboard();
        if (object instanceof Team) return ((Team) object).getScoreboard();
        if (object instanceof Objective) return ((Objective) object).getScoreboard();
        throw nextRule;
    }

    @Override
    public Class<Scoreboard> getClassTo() {
        return Scoreboard.class;
    }
}
