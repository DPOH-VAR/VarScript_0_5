package me.dpohvar.varscript.scheduler;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 03.11.13
 * Time: 22:55
 */
public enum EntrySlot {

    INIT(EntryType.ACTION, "init", "init"),
    EVENT(EntryType.EVENT, "event", "events"),
    CONDITION(EntryType.CONDITION, "condition", "conditions"),
    ACTION(EntryType.ACTION, "action", "actions"),
    REACTION(EntryType.ACTION, "reaction", "reactions"),;

    public final EntryType type;
    public final String name;
    public final String setName;

    EntrySlot(EntryType type, String name, String setName) {
        this.type = type;
        this.name = name;
        this.setName = setName;
    }

    public static EntrySlot getByName(String name) {
        for (EntrySlot e : values()) {
            if (e.setName.startsWith(name)) return e;
        }
        return null;
    }
}
