package me.dpohvar.varscript.converter.rule;

import me.dpohvar.powernbt.nbt.NBTTagDatable;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.NextRule;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleCharacter extends ConvertRule<Character>{

    public RuleCharacter() {
        super(10);
    }

    @Override
    public <V> Character convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule {
        if (object==null) return '\0';
        if (object instanceof Number) return (char)((Number)object).intValue();
        if (object instanceof String) return ((String)object).charAt(0) ;
        if (object instanceof Boolean) return ((Boolean)object)?(char)1:(char)0;
        if (object instanceof byte[]) {
            byte[] bytes = (byte[])object;
            if (bytes.length==0) return 0;
            return ByteBuffer.wrap(bytes).getChar();
        }
        if (object instanceof NBTTagDatable) return convert(((NBTTagDatable)object).get(),thread,scope);
        throw nextRule;
    }

    @Override
    public Class<Character> getClassTo() {
        return Character.class;
    }
}
