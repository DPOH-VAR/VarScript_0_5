package org.dpohvar.varscript.converter.rule;

import org.dpohvar.varscript.caller.Caller;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.vs.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleCaller extends ConvertRule<Caller> {

    public RuleCaller() {
        super(10);
    }

    @Override
    public <V> Caller convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        return Caller.getCallerFor(object);
    }

    @Override
    public Class<Caller> getClassTo() {
        return Caller.class;
    }
}
