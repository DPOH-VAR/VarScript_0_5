package me.dpohvar.varscript.converter.rule;

import com.google.common.io.Files;
import me.dpohvar.powernbt.nbt.NBTBase;
import me.dpohvar.powernbt.nbt.NBTContainer;
import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.converter.NextRule;
import me.dpohvar.varscript.vs.CommandList;
import me.dpohvar.varscript.vs.Scope;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 28.06.13
 * Time: 1:03
 */
public class RuleBytes extends ConvertRule<byte[]> {
    private final ConvertRule<Byte> con;

    public RuleBytes(ConvertRule<Byte> con) {
        super(10);
        this.con = con;
    }

    @Override
    public <V> byte[] convert(V object, me.dpohvar.varscript.vs.Thread thread, Scope scope) throws NextRule, ConvertException {
        if (object == null) return null;
        if (object instanceof Byte) {
            return ByteBuffer.allocate(1).put((Byte) object).array();
        }
        if (object instanceof Short) {
            return ByteBuffer.allocate(2).putShort((Short) object).array();
        }
        if (object instanceof Integer) {
            return ByteBuffer.allocate(4).putInt((Integer) object).array();
        }
        if (object instanceof Long) {
            return ByteBuffer.allocate(8).putLong((Long) object).array();
        }
        if (object instanceof Float) {
            return ByteBuffer.allocate(4).putFloat((Float) object).array();
        }
        if (object instanceof Double) {
            return ByteBuffer.allocate(8).putDouble((Double) object).array();
        }
        if (object instanceof Character) {
            return ByteBuffer.allocate(2).putChar((Character) object).array();
        }
        if (object instanceof String) return ((String) object).getBytes(VarScript.UTF8);
        if (object instanceof Collection) {
            ArrayList<Object> a = new ArrayList<Object>((Collection) object);
            ArrayList<Byte> b = new ArrayList<Byte>(a.size());
            for (Object o : a) {
                b.add(con.convert(o, thread, scope));
            }
            byte[] bytes = new byte[b.size()];
            int i = 0;
            for (Byte g : b) bytes[i++] = g;
            return bytes;
        }
        if (object instanceof Boolean) return ((Boolean) object) ? new byte[]{-1} : new byte[]{0};
        if (object instanceof CommandList) return ((CommandList) object).toBytes();
        if (object instanceof int[]) {
            int[] integers = (int[]) object;
            byte[] bytes = new byte[integers.length];
            int i = 0;
            for (int t : integers) bytes[i++] = (byte) t;
            return bytes;
        }
        if (object instanceof File) {
            try {
                return Files.toByteArray((File) object);
            } catch (IOException ignored) {
            }
        }
        try {
            if (object instanceof NBTBase) return ((NBTBase) object).toBytes();
            if (object instanceof NBTContainer) return ((NBTContainer) object).getTag().toBytes();
        } catch (NoClassDefFoundError ignored) {
        }
        throw nextRule;
    }

    @Override
    public Class<byte[]> getClassTo() {
        return byte[].class;
    }
}
