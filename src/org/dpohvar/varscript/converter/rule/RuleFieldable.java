package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTBase;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.utils.reflect.NBTTagWrapper;
import org.dpohvar.varscript.utils.reflect.ReflectObject;
import org.dpohvar.varscript.vs.Fieldable;
import org.dpohvar.varscript.vs.FieldableObject;
import org.dpohvar.varscript.vs.Scope;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleFieldable extends ConvertRule<Fieldable> {

    public RuleFieldable() {
        super(10);
    }

    @Override
    public <V> Fieldable convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof Map) return new FieldableObject(scope, (Map) object);
        try {
            if (object instanceof NBTTagCompound) return new NBTTagWrapper((NBTBase) object);
            if (object instanceof NBTTagList) return new NBTTagWrapper((NBTBase) object);
        } catch (NoClassDefFoundError ignored) {
        }
        return new ReflectObject(object, scope);
    }

    @Override
    public Class<Fieldable> getClassTo() {
        return Fieldable.class;
    }
}
