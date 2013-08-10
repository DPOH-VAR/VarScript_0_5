package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.vs.Scope;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleScoreboard extends ConvertRule<Scoreboard>{

    public RuleScoreboard() {
        super(10);
    }

    @Override
    public <V> Scoreboard convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return null;
        if (object instanceof Number) {
            int id = ((Number) object).intValue();
            for(World w:Bukkit.getWorlds()){
                for(Entity e:w.getEntities()){
                    if(e.getEntityId()==id) return ((Player)e).getScoreboard();
                }
            }
        }
        if (object instanceof String) return Bukkit.getPlayer(((String)object).trim()).getScoreboard();
        if (object instanceof Entity) return ((Player)object).getScoreboard();
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        if (object instanceof Score) return ((Score)object).getScoreboard();
        if (object instanceof Team) return ((Team)object).getScoreboard();
        if (object instanceof Objective) return ((Objective)object).getScoreboard();
        throw nextRule;
    }

    @Override
    public Class<Scoreboard> getClassTo() {
        return Scoreboard.class;
    }
}
