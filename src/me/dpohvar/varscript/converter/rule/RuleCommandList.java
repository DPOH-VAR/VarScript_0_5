package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.converter.NextRule;

import java.io.ByteArrayInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleCommandList extends ConvertRule<CommandList>{

    public RuleCommandList() {
        super(10);
    }

    @Override
    public <V> CommandList convert(V object, Thread thread,Scope scope) throws NextRule {
        if (object==null) return new Function(null,"",scope);
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
        if (object instanceof int[]) {
            try {
                int[] integers = (int[]) object;
                byte[] bytes = new byte[integers.length];
                int i=0;for(int t:integers)bytes[i++]=(byte)t;
                ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                return VSCompiler.read(is);
            } catch (Exception ignored) {
            }
        }
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        throw nextRule;
   }

    @Override
    public Class<CommandList> getClassTo() {
        return CommandList.class;
    }
}
