package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import org.dpohvar.varscript.converter.Converter;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.utils.reflect.ReflectClass;
import org.dpohvar.varscript.vs.Scope;

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
    public <V> Class convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof ReflectClass) {
            return ((ReflectClass) object).getInnerClass();
        }
        if (object instanceof String) {
            if (Converter.classes.containsKey(object)) return Converter.classes.get(object);
            try {
                return Class.forName((String) object);
            } catch (ClassNotFoundException ignored) {
            }
            try {
                return Class.forName("java.util." + object);
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
