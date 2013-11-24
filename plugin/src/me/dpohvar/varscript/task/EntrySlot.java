package me.dpohvar.varscript.task;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 03.11.13
 * Time: 22:55
 */
public enum EntrySlot {

    INIT(EntryType.ACTION),
    EVENT(EntryType.EVENT),
    CONDITION(EntryType.CONDITION),
    ACTION(EntryType.ACTION),
    REACTION(EntryType.ACTION),;

    public final EntryType type;

    EntrySlot(EntryType type) {
        this.type = type;
    }
}
