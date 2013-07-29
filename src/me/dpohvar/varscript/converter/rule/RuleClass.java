package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.reflect.ReflectClass;
import me.dpohvar.varscript.vs.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleClass extends ConvertRule<Class>{


    public RuleClass() {
        super(10);
    }

    @Override public <V> Class convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return null;
        if (object instanceof ReflectClass) {
            return ((ReflectClass)object).getInnerClass();
        }
        if (object instanceof String) {
            if(Converter.classes.containsKey(object)) return Converter.classes.get(object);
            try{
                return Class.forName((String)object);
            } catch (ClassNotFoundException ignored) {
            }
            try{
                return Class.forName("java.util."+object);
            } catch (ClassNotFoundException ignored) {
            }
        }
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        return object.getClass();
    }

    @Override public Class<Class> getClassTo() {
        return Class.class;
    }

}
