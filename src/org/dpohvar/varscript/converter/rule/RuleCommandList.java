package org.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import org.dpohvar.varscript.converter.ConvertException;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.vs.CommandList;
import org.dpohvar.varscript.vs.Scope;
import org.dpohvar.varscript.vs.compiler.VSCompiler;

import java.io.ByteArrayInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleCommandList extends ConvertRule<CommandList> {

    public RuleCommandList() {
        super(10);
    }

    @Override
    public <V> CommandList convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule, ConvertException {
        if (object == null) return null;
        if (object instanceof String) {
            try {
                return VSCompiler.compile(object.toString(), "");
            } catch (Exception ignored) {
            }
        }
        if (object instanceof byte[]) {
            try {
                ByteArrayInputStream is = new ByteArrayInputStream((byte[]) object);
                return VSCompiler.read(is);
            } catch (Exception e) {
                throw new ConvertException(object, CommandList.class, "can't compile bytecode");
            }
        }
        if (object instanceof int[]) {
            try {
                int[] integers = (int[]) object;
                byte[] bytes = new byte[integers.length];
                int i = 0;
                for (int t : integers) bytes[i++] = (byte) t;
                ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                return VSCompiler.read(is);
            } catch (Exception ignored) {
            }
        }
        try {
            if (object instanceof NBTTagDatable) return convert(((NBTTagDatable) object).get(), thread, scope);
        } catch (NoClassDefFoundError ignored) {
        }
        throw nextRule;
    }

    @Override
    public Class<CommandList> getClassTo() {
        return CommandList.class;
    }
}
