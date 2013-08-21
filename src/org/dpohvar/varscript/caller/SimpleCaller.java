package org.dpohvar.varscript.caller;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 27.06.13
 * Time: 23:44
 */
public class SimpleCaller extends Caller {

    final private Object instance;

    public SimpleCaller(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object getInstance() {
        return instance;
    }
}
