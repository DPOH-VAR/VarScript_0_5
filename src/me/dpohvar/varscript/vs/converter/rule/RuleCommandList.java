package me.dpohvar.varscript.vs.converter.rule;

import me.dpohvar.varscript.utils.region.Region;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.converter.NextRule;
import me.dpohvar.varscript.vs.exception.SourceException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleCommandList extends ConvertRule<VSNamedCommandList>{

    public RuleCommandList() {
        super(10);
    }

    @Override
    public <V> VSNamedCommandList convert(V object, VSThread thread,VSScope scope) throws NextRule {
        if (object==null) return new VSFunction(null,"",scope);
        if (object instanceof String) {
            try {
                return VSCompiler.compile(object.toString(),"");
            } catch (Exception ignored) {
            }
        }
        if (object instanceof byte[]) {
            try {
                ByteArrayInputStream is = new ByteArrayInputStream((byte[])object);
                return VSCompiler.read(is);
            } catch (Exception ignored) {
            }
        }
        throw nextRule;
   }

    @Override
    public Class<VSNamedCommandList> getClassTo() {
        return VSNamedCommandList.class;
    }
}
