package me.dpohvar.varscript.vs.converter.rule;

import me.dpohvar.varscript.vs.VSFieldable;
import me.dpohvar.varscript.vs.VSScope;
import me.dpohvar.varscript.vs.VSSimpleScope;
import me.dpohvar.varscript.vs.VSThread;
import me.dpohvar.varscript.vs.converter.NextRule;
import me.dpohvar.varscript.utils.ReflectObject;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleFieldable extends ConvertRule<VSFieldable>{

    public RuleFieldable() {
        super(10);
    }

    @Override
    public <V> VSFieldable convert(V object, VSThread thread,VSScope scope) throws NextRule {
        if (object==null) return null;
        return new ReflectObject(object,scope);
	}

    @Override
    public Class<VSFieldable> getClassTo() {
        return VSFieldable.class;
    }
}
