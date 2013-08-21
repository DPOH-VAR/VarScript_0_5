package org.dpohvar.varscript.vs;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:36
 */
public interface Fieldable {

    public Set<String> getAllFields();

    public Object getField(String key);

    public void setField(String key, Object value);

    public void removeField(String key);

    public boolean hasField(String key);

    public Runnable getConstructor();

    public Fieldable getProto();

    public void setProto(Fieldable proto);
}
