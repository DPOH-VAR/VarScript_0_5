package org.dpohvar.varscript.converter.rule;

import org.dpohvar.varscript.converter.ConvertException;
import org.dpohvar.varscript.converter.NextRule;
import org.dpohvar.varscript.vs.Scope;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public abstract class ConvertRule<T> implements Comparable<ConvertRule> {

    protected NextRule nextRule = new NextRule();
    private final int priority;

    protected ConvertRule(int priority) {
        this.priority = priority;
    }

    public abstract <V> T convert(V object, org.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule, ConvertException;

    public abstract Class<T> getClassTo();

    @Override
    public int compareTo(ConvertRule o) {
        return priority - o.priority;
    }
}
