package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTBase;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.reflect.NBTTagWrapper;
import me.dpohvar.varscript.utils.reflect.ReflectObject;
import me.dpohvar.varscript.vs.Fieldable;
import me.dpohvar.varscript.vs.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleFieldable extends ConvertRule<Fieldable>{

    public RuleFieldable() {
        super(10);
    }

    @Override
    public <V> Fieldable convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return null;
        if (object instanceof NBTTagCompound) return new NBTTagWrapper((NBTBase)object);
        if (object instanceof NBTTagList) return new NBTTagWrapper((NBTBase)object);
        return new ReflectObject(object,scope);
	}

    @Override
    public Class<Fieldable> getClassTo() {
        return Fieldable.class;
    }
}
