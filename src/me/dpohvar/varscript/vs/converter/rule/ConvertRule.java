package me.dpohvar.varscript.vs.converter.rule;

import me.dpohvar.varscript.vs.VSScope;
import me.dpohvar.varscript.vs.VSThread;
import me.dpohvar.varscript.vs.converter.NextRule;

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

    public abstract <V> T convert(V object,VSThread thread,VSScope scope) throws NextRule;

    public abstract Class<T> getClassTo();

    @Override
    public int compareTo(ConvertRule o) {
        return priority-o.priority;
    }
}
