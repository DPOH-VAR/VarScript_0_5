package me.dpohvar.varscript.converter.rule;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.utils.ReflectObject;

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
        return new ReflectObject(object,scope);
	}

    @Override
    public Class<Fieldable> getClassTo() {
        return Fieldable.class;
    }
}
