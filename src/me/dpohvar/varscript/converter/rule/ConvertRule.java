package me.dpohvar.varscript.converter.rule;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.vs.exception.InterruptThread;

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

    public abstract <V> T convert(V object, me.dpohvar.varscript.vs.Thread thread,Scope scope) throws NextRule;

    public abstract Class<T> getClassTo();

    @Override
    public int compareTo(ConvertRule o) {
        return priority-o.priority;
    }
}
