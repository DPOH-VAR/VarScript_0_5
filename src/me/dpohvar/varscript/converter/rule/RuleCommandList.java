package me.dpohvar.varscript.converter.rule;

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
public class RuleCommandList extends ConvertRule<NamedCommandList>{

    public RuleCommandList() {
        super(10);
    }

    @Override
    public <V> NamedCommandList convert(V object, Thread thread,Scope scope) throws NextRule {
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
        throw nextRule;
   }

    @Override
    public Class<NamedCommandList> getClassTo() {
        return NamedCommandList.class;
    }
}
