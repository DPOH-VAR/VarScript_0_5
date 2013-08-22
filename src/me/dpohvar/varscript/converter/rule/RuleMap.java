package me.dpohvar.varscript.converter.rule;

import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.vs.Fieldable;
import me.dpohvar.varscript.vs.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleMap extends ConvertRule<Map> {

    public RuleMap() {
        super(10);
    }

    @Override
    public <V> Map convert(V object, me.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule {
        if (object == null) return null;
        if (object instanceof Fieldable) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            for (String s : ((Fieldable) object).getAllFields()) {
                map.put(s, ((Fieldable) object).getField(s));
            }
            return map;
        }

        throw nextRule;
    }

    @Override
    public Class<Map> getClassTo() {
        return Map.class;
    }
}
