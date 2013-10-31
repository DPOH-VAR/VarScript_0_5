package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.reflect.ReflectClass;
import me.dpohvar.varscript.vs.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleClass extends ConvertRule<Class> {


    public RuleClass() {
        super(10);
    }

    @Override
    public <V> Class convert(V object, me.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof ReflectClass) {
            return ((ReflectClass) object).getInnerClass();
        }
        if (object instanceof String) {
            if (Converter.classes.containsKey(object)) return Converter.classes.get(object);
            try {
                return me.dpohvar.varscript.Runtime.libLoader.loadClass((String) object);
            } catch (ClassNotFoundException ignored) {
            }
            try {
                return me.dpohvar.varscript.Runtime.libLoader.loadClass("java.util." + object);
            } catch (ClassNotFoundException ignored) {
            }
        }
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        return object.getClass();
    }

    @Override
    public Class<Class> getClassTo() {
        return Class.class;
    }

}
