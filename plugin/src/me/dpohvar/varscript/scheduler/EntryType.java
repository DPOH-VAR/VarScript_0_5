package me.dpohvar.varscript.scheduler;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 03.11.13
 * Time: 22:55
 */
public enum EntryType {
    EVENT("event", "events"),
    CONDITION("condition", "conditions"),
    ACTION("action", "actions"),;

    public final String name;
    public final String setName;

    EntryType(String name, String setName) {
        this.name = name;
        this.setName = setName;
    }

    public static EntryType getByName(String name) {
        for (EntryType e : values()) {
            if (e.setName.startsWith(name)) return e;
        }
        return null;
    }
}
