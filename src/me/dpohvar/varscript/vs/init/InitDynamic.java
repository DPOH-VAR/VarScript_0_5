package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.caller.Caller;
import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.converter.rule.RuleItemStack;
import me.dpohvar.varscript.utils.ScriptManager;
import me.dpohvar.varscript.utils.reflect.ReflectClass;
import me.dpohvar.varscript.utils.reflect.ReflectObject;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Runnable;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.*;
import me.dpohvar.varscript.vs.exception.CloseFunction;
import me.dpohvar.varscript.vs.exception.CommandException;
import me.dpohvar.varscript.vs.exception.ParseException;
import me.dpohvar.varscript.vs.exception.SourceException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Map;

import static me.dpohvar.varscript.utils.VarScriptIOUtils.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 09.07.13
 * Time: 18:05
 */
public class InitDynamic {
    public static Worker<String> wGetVariable = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, String d) throws Exception {
            v.push(f.getScope().getVar(d));
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x10);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x10};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<String> wSetVariable = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            f.getScope().setVar(d, v.pop());
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x11);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x11};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<String> wDefineVariable = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            f.getScope().defineVar(d, v.pop());
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x12);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x12};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<Object> wPutObject = new Worker<Object>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Object d) throws Exception {
            v.push(d);
        }

        @Override
        public void save(OutputStream out, Object data) throws IOException {
            out.write(0x13);
            saveObject(out, data);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x13};
        }

        @Override
        public Object readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            return loadObject(input);
        }
    };

    public static Worker<CommandList> wPutFunction = new Worker<CommandList>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, CommandList d) throws Exception {
            v.push(d.build(f.getScope()));
        }

        @Override
        public void save(OutputStream out, CommandList data) throws IOException {
            out.write(0x14);
            data.save(out);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x14};
        }

        @Override
        public CommandList readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            return VSCompiler.read(input);
        }
    };

    public static Worker<CommandList> wDefineFunction = new Worker<CommandList>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, CommandList d) throws Exception {
            f.getScope().defineVar(d.getName(), d.build(f.getScope()));
        }

        @Override
        public void save(OutputStream out, CommandList data) throws IOException {
            out.write(0x15);
            data.save(out);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x15};
        }

        @Override
        public CommandList readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            return VSCompiler.read(input);
        }
    };

    public static Worker<Void> wRunNew = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Runnable runnable = v.pop(f.getScope());
            Fieldable o = new Context(runnable, null);
            f.setRegisterF(o);
            v.pushFunction(runnable, o).setRegisterE(f);
            throw interruptFunction;
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0x16);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x16};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            session.addCommandAfter(wReadRegisterF, null);
            return null;
        }
    };

    public static Worker<Void> wReadRegisterF = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            v.push(f.getRegisterF());
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            return null;
        }
    };

    public static Worker<String> wGetField = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            v.push(v.pop(Fieldable.class).getField(d));
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x17);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x17};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<String> wSetField = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            v.pop(Fieldable.class).setField(d, v.pop());
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x18);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x18};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<String> wRunField = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            Fieldable obj = v.pop(Fieldable.class);
            Runnable runnable = (Runnable) obj.getField(d);
            if (runnable == null) throw new Exception("function is null");
            v.pushFunction(runnable, obj).setRegisterE(f);
            throw interruptFunction;
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x19);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x19};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static SimpleWorker wRun = new SimpleWorker(new int[]{0x1A}) {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Runnable runnable = v.pop(f.getScope());
            if (runnable == null) throw new Exception("function is null");
            v.pushFunction(runnable, f.getApply()).setRegisterE(f);
            throw interruptFunction;
        }
    };

    public static Worker<Integer> wJump = new Worker<Integer>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Integer d) throws Exception {
            f.jumpPointer(d);

        }

        @Override
        public void save(OutputStream out, Integer data) throws IOException {
            out.write(0x1B);
            if (Byte.MIN_VALUE < data && data <= Byte.MAX_VALUE) {
                out.write(data);
            } else {
                out.write(Byte.MIN_VALUE);
                out.write(ByteBuffer.allocate(4).putInt(data).array());
            }
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1B};
        }

        @Override
        public Integer readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int first = input.read();
            if ((byte) first != Byte.MIN_VALUE) return (int) (byte) first;
            byte[] buffer = new byte[4];
            input.read(buffer);
            return ByteBuffer.wrap(buffer).getInt();
        }
    };

    public static Worker<Integer> wJumpFalse = new Worker<Integer>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Integer d) throws Exception {
            if (!v.pop(Boolean.class)) f.jumpPointer(d);
        }

        @Override
        public void save(OutputStream out, Integer data) throws IOException {
            out.write(0x1C);
            if (Byte.MIN_VALUE < data && data <= Byte.MAX_VALUE) {
                out.write(data);
            } else {
                out.write(Byte.MIN_VALUE);
                out.write(ByteBuffer.allocate(4).putInt(data).array());
            }
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1C};
        }

        @Override
        public Integer readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int first = input.read();
            if ((byte) first != Byte.MIN_VALUE) return (int) (byte) first;
            byte[] buffer = new byte[4];
            input.read(buffer);
            return ByteBuffer.wrap(buffer).getInt();
        }
    };

    public static Worker<Integer> wJumpTrue = new Worker<Integer>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Integer d) throws Exception {
            if (v.pop(Boolean.class)) f.jumpPointer(d);
        }

        @Override
        public void save(OutputStream out, Integer data) throws IOException {
            out.write(0x1D);
            if (Byte.MIN_VALUE < data && data <= Byte.MAX_VALUE) {
                out.write(data);
            } else {
                out.write(Byte.MIN_VALUE);
                out.write(ByteBuffer.allocate(4).putInt(data).array());
            }
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1D};
        }

        @Override
        public Integer readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int first = input.read();
            if ((byte) first != Byte.MIN_VALUE) return (int) (byte) first;
            byte[] buffer = new byte[4];
            input.read(buffer);
            return ByteBuffer.wrap(buffer).getInt();
        }
    };

    public static Worker<Integer> wJumpInt = new Worker<Integer>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Integer d) throws Exception {
            f.jumpPointer(v.pop(Integer.class));
        }

        @Override
        public void save(OutputStream out, Integer data) throws IOException {
            out.write(0x1E);
            if (Byte.MIN_VALUE < data && data <= Byte.MAX_VALUE) {
                out.write(data);
            } else {
                out.write(Byte.MIN_VALUE);
                out.write(ByteBuffer.allocate(4).putInt(data).array());
            }
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1E};
        }

        @Override
        public Integer readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int first = input.read();
            if ((byte) first != Byte.MIN_VALUE) return (int) (byte) first;
            byte[] buffer = new byte[4];
            input.read(buffer);
            return ByteBuffer.wrap(buffer).getInt();
        }
    };

    public static Worker<CommandList> wSetFunctionForThis = new Worker<CommandList>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, CommandList d) throws Exception {
            v.convert(Fieldable.class, f.getApply()).setField(d.getName(), d.build(f.getScope()));
        }

        @Override
        public void save(OutputStream out, CommandList data) throws IOException {
            out.write(0x1F);
            out.write(0x10);
            data.save(out);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x10};
        }

        @Override
        public CommandList readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            return VSCompiler.read(input);
        }
    };

    public static Worker<String> wSetFieldForThis = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            v.convert(Fieldable.class, f.getApply()).setField(d, v.pop());
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x1F);
            out.write(0x11);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x11};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<Void> wApply = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Fieldable obj = v.pop(Fieldable.class);
            Runnable runnable = v.pop(f.getScope());
            v.pushFunction(runnable, obj).setRegisterE(f);
            throw interruptFunction;
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0x1F);
            out.write(0x12);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x12};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            return null;
        }
    };

    public static Worker<String> wFunctionFromFile = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            ScriptManager manager = v.getProgram().getRuntime().scriptManager;
            InputStream input = manager.openScriptFile("vsbin", d);
            if (input != null) {
                v.push(VSCompiler.read(input).build(f.getScope()));
                input.close();
                return;
            }
            String source = manager.readScriptFile("vs", d);
            if (source != null) {
                v.push(VSCompiler.compile(source, d).build(f.getScope()));
                return;
            }
            v.push(null);
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x1F);
            out.write(0x11);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x13};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<String> wDelVariable = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            f.getScope().delVar(d);
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x1F);
            out.write(0x14);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x14};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<String> wRemoveField = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            v.pop(Fieldable.class).removeField(d);
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x1F);
            out.write(0x15);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x15};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<Class> wConvert = new Worker<Class>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Class d) throws Exception {
            v.push(v.pop(d));
        }

        @Override
        public void save(OutputStream out, Class data) throws IOException {
            out.write(0x1F);
            out.write(0x16);
            byte[] bytes = data.getName().getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x16};
        }

        @Override
        public Class readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            String name = new String(bytes, VarScript.UTF8);
            try {
                return me.dpohvar.varscript.Runtime.libLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
                throw new IOException("can't read class " + name, e);
            }
        }
    };

    public static Worker<String> wStoreField = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            Object val = v.pop();
            v.peek(Fieldable.class).setField(d, val);
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x1F);
            out.write(0x17);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x17};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };

    public static Worker<String> wApplyFunction = new Worker<String>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, String d) throws Exception {
            Object apply = v.pop();
            Object fun = f.getScope().getVar(d);
            Runnable run = v.convert(f.getScope(), fun);
            v.pushFunction(run, apply).setRegisterE(f);
            throw interruptFunction;
        }

        @Override
        public void save(OutputStream out, String data) throws IOException {
            out.write(0x1F);
            out.write(0x18);
            byte[] bytes = data.getBytes(VarScript.UTF8);
            out.write(bytes.length);
            out.write(bytes);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x18};
        }

        @Override
        public String readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            int len = input.read();
            byte[] bytes = new byte[len];
            input.read(bytes);
            return new String(bytes, VarScript.UTF8);
        }
    };
    public static Worker<ItemStack> wPutItemStack = new Worker<ItemStack>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, ItemStack d) throws Exception {
            v.push(d.clone());
        }

        @Override
        public void save(OutputStream out, ItemStack data) throws IOException {
            out.write(0x1F);
            out.write(0x19);
            saveObject(out, data);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{0x1F, 0x19};
        }

        @Override
        public ItemStack readObject(InputStream input, VSCompiler.ReadSession session) throws IOException {
            return (ItemStack) loadObject(input);
        }
    };


    private static IOException endOfThread = new IOException("end of thread");

    public static Object loadObject(InputStream input) throws IOException {
        int first = input.read();
        byte[] buffer;
        if (first == -1) throw endOfThread;
        if (first < 0xF0) {
            buffer = new byte[first];
            if (input.read(buffer) < first) throw endOfThread;
            return new String(buffer, VarScript.UTF8);
        }
        switch (first) {
            case 0xF0: /* null */
                return null;
            case 0xF1: /* byte */
                return (byte) input.read();
            case 0xF2: /* short */ {
                buffer = new byte[2];
                if (input.read(buffer) < 2) throw endOfThread;
                return ByteBuffer.wrap(buffer).getShort();
            }
            case 0xF3: /* int full */ {
                buffer = new byte[4];
                if (input.read(buffer) < 4) throw endOfThread;
                return ByteBuffer.wrap(buffer).getInt();
            }
            case 0xF4: /* long */ {
                buffer = new byte[8];
                if (input.read(buffer) < 8) throw endOfThread;
                return ByteBuffer.wrap(buffer).getLong();
            }
            case 0xF5: /* float */ {
                buffer = new byte[4];
                if (input.read(buffer) < 4) throw endOfThread;
                return ByteBuffer.wrap(buffer).getFloat();
            }
            case 0xF6: /* double */ {
                buffer = new byte[8];
                if (input.read(buffer) < 8) throw endOfThread;
                return ByteBuffer.wrap(buffer).getDouble();
            }
            case 0xF7: /* string */ {
                buffer = new byte[4];
                if (input.read(buffer) < 4) throw endOfThread;
                int len = ByteBuffer.wrap(buffer).getInt();
                buffer = new byte[len];
                if (input.read(buffer) < len) throw endOfThread;
                return new String(buffer, VarScript.UTF8);
            }
            case 0xF8: /* bytearray full size */ {
                buffer = new byte[4];
                if (input.read(buffer) < 4) throw endOfThread;
                int size = ByteBuffer.wrap(buffer).getInt();
                buffer = new byte[size];
                if (input.read(buffer) < size) throw endOfThread;
                return buffer;
            }
            case 0xF9: /* char */ {
                buffer = new byte[2];
                if (input.read(buffer) < 2) throw endOfThread;
                return ByteBuffer.wrap(buffer).getChar();
            }
            case 0xFA: /* int mini */ {
                int read = input.read();
                if (read < 0) throw endOfThread;
                return (int) (byte) read;
            }
            case 0xFB: /* bytearray mini */ {
                int size = input.read();
                if (size < 0) throw endOfThread;
                buffer = new byte[size];
                if (input.read(buffer) < size) throw endOfThread;
                return buffer;
            }
            case 0xFC: /* vector */ {
                double x = getDouble(input);
                double y = getDouble(input);
                double z = getDouble(input);
                return new org.bukkit.util.Vector(x, y, z);
            }
            case 0xFD: /* location */ {
                double x = getDouble(input);
                double y = getDouble(input);
                double z = getDouble(input);
                String w = getString(input);
                return new Location(Bukkit.getWorld(w), x, y, z);
            }
            case 0xFE: /* block */ {
                int x = getInt(input);
                int y = getInt(input);
                int z = getInt(input);
                String w = getString(input);
                return Bukkit.getWorld(w).getBlockAt(x, y, z);
            }
            case 0xFF: /* ItemStack */ {
                int id = getInt(input);
                int data = getInt(input);
                int amount = getInt(input);
                return RuleItemStack.newItem(id, amount, data);
            }
            default:
                throw new IOException("unknown object type: " + first);
        }

    }

    public static void saveObject(OutputStream out, Object object) throws IOException {
        if (object == null) {
            out.write(0xF0);
        } else if (object instanceof Byte) {
            out.write(0xF1);
            out.write((Byte) object);
        } else if (object instanceof Short) {
            out.write(0xF2);
            out.write(ByteBuffer.allocate(2).putShort((Short) object).array());
        } else if (object instanceof Integer) {
            int value = (Integer) object;
            if (Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE) {
                out.write(0xFA);
                out.write(value);
            } else {
                out.write(0xF3);
                out.write(ByteBuffer.allocate(4).putInt((Integer) object).array());
            }
        } else if (object instanceof Long) {
            out.write(0xF4);
            out.write(ByteBuffer.allocate(8).putLong((Long) object).array());

        } else if (object instanceof Float) {
            out.write(0xF5);
            out.write(ByteBuffer.allocate(4).putFloat((Float) object).array());
        } else if (object instanceof Double) {
            out.write(0xF6);
            out.write(ByteBuffer.allocate(8).putDouble((Double) object).array());
        } else if (object instanceof String) {
            byte[] buffer = ((String) object).getBytes(VarScript.UTF8);
            if (buffer.length < 0xF0) {
                out.write(buffer.length);
                out.write(buffer);
            } else {
                out.write(0xF7);
                out.write(ByteBuffer.allocate(4).putInt(buffer.length).array());
                out.write(buffer);

            }
        } else if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            if (bytes.length > 255) {
                out.write(0xF8);
                out.write(ByteBuffer.allocate(4).putInt(bytes.length).array());
                out.write(bytes);
            } else {
                out.write(0xFB);
                out.write(bytes.length);
                out.write(bytes);
            }
        } else if (object instanceof Character) {
            out.write(0xF8);
            out.write(ByteBuffer.allocate(2).putChar((Character) object).array());
        } else if (object instanceof org.bukkit.util.Vector) {
            org.bukkit.util.Vector v = (org.bukkit.util.Vector) object;
            out.write(0xFC);
            put(out, v.getX());
            put(out, v.getY());
            put(out, v.getZ());
        } else if (object instanceof Location) {
            Location l = (Location) object;
            out.write(0xFD);
            put(out, l.getX());
            put(out, l.getY());
            put(out, l.getZ());
            put(out, l.getWorld().getName());
        } else if (object instanceof Block) {
            Block b = (Block) object;
            out.write(0xFE);
            put(out, b.getX());
            put(out, b.getY());
            put(out, b.getZ());
            put(out, b.getWorld().getName());
        } else if (object instanceof ItemStack) {
            ItemStack b = (ItemStack) object;
            out.write(0xFF);
            put(out, b.getTypeId());
            put(out, b.getDurability());
            put(out, b.getAmount());
        } else throw new IOException("unknown object type: " + object.getClass().getName());
    }


    public static void load() {

        VSCompiler.addRule(new ComplexCompileRule("@variable", "variable", "", "Object", "get value of variable") { //0x10
            @Override
            public boolean checkCondition(String string) {

                return string.matches("@[A-Za-z0-9_\\-/]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                String object = operand.builder.toString().substring(1);
                functionSession.addCommand(wGetVariable, object, operand);
                compileSession.addVarName(object);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wGetVariable};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("%variable", "variable", "Object", "", "set value of variable") { //0x11
            @Override
            public boolean checkCondition(String string) {
                return string.matches("%[A-Za-z0-9_\\-/]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                String object = operand.builder.toString().substring(1);
                functionSession.addCommand(wSetVariable, object, operand);
                compileSession.addVarName(object);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wSetVariable};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("%~variable", "variable", "", "", "delete variable") { //0x11
            @Override
            public boolean checkCondition(String string) {
                return string.matches("%~[A-Za-z0-9_\\-/]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                String object = operand.builder.toString().substring(1);
                functionSession.addCommand(wDelVariable, object, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wDelVariable};
            }
        });


        VSCompiler.addRule(new ComplexCompileRule("%%variable", "variable", "Object", "", "define variable and set value") { //0x12
            @Override
            public boolean checkCondition(String string) {
                return string.matches("%%[A-Za-z0-9_\\-/]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                String object = operand.builder.toString().substring(2);
                functionSession.addCommand(wDefineVariable, object, operand);
                compileSession.addVarName(object);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wDefineVariable};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("\"string\"", "stack string", "", "String", "put string to stack. Example: \"Hello World\"") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.startsWith("\"") && string.endsWith("\"") && string.length() > 1;
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws ParseException {
                String op = operand.toString();
                String str = op.substring(1, op.length() - 1);
                functionSession.addCommand(wPutObject, VSStringParser.parse(str), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wPutObject};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("+\"string\"", "stack string", "String", "String", "append string. Example: \"Hello \" +\" World\"") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.startsWith("+\"") && string.endsWith("\"") && string.length() > 1;
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws ParseException {
                String op = operand.toString();
                String str = op.substring(2, op.length() - 1);
                functionSession.addCommand(wPutObject, VSStringParser.parse(str), operand);
                functionSession.addCommand(InitString.wConcat, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("Integer", "stack integer", "", "Integer", "put integer to stack.\nExample: 42") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.matches("-?[0-9]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                functionSession.addCommand(wPutObject, Integer.parseInt(operand.toString()), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("Long", "stack integer", "", "Long", "put long to stack.\nExample: 123456789012345L") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.matches("-?[0-9]+L");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                String op = operand.toString();
                String val = op.substring(0, op.length() - 1);
                functionSession.addCommand(wPutObject, Long.parseLong(val), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("Short", "stack integer", "", "Short", "put short to stack.\nExample: 335S") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.matches("-?[0-9]+S");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                String op = operand.toString();
                String val = op.substring(0, op.length() - 1);
                functionSession.addCommand(wPutObject, Short.parseShort(val), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("Double", "stack", "", "Double", "put double to stack.\nExample: 3.14") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.matches("-?[0-9]+\\.[0-9]*");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                String op = operand.toString();
                functionSession.addCommand(wPutObject, Double.parseDouble(op), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("0x<hex_value>", "stack integer", "", "Integer", "put integer to stack (in hex mode).\nExample: 0xFF ## 255") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.matches("0x[0-9A-Fa-f]");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                String op = operand.toString();
                String val = op.substring(2);
                functionSession.addCommand(wPutObject, Integer.parseInt(val, 16), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("^ItemStack", "stack item", "", "ItemStack", "create new ItemStack by id,data,amount. Format: ^ID_or_MATERIAL:DATA:AMOUNT\n" +
                "Examples: ^WOOL:5:64 ^35 ^STONE::64") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches("\\^[a-zA-Z0-9:_]+") && string.split(":").length <= 3;
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws ParseException {
                String op = operand.toString();
                String str = op.substring(1);
                int id = 0, data = 0, amount = 1;
                String[] ss = str.split(":");
                try {
                    if (ss.length > 0) {
                        if (ss[0].matches("[0-9]+")) {
                            id = Integer.parseInt(ss[0]);
                        } else {
                            id = Converter.convert(Material.values(), ss[0]).getId();
                        }
                    }
                    if (ss.length > 1 && ss[1].matches("[0-9]+")) {
                        data = Integer.parseInt(ss[1]);
                    }
                    if (ss.length > 2 && ss[2].matches("[0-9]+")) {
                        amount = Integer.parseInt(ss[2]);
                    }
                } catch (Exception ignored) {
                }
                functionSession.addCommand(wPutItemStack, RuleItemStack.newItem(id, amount, data), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wPutItemStack};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("NULL", "stack", "", "null", "put null to stack") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.equals("NULL");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                functionSession.addCommand(wPutObject, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("x:y:z", "stack vector", "", "Vector", "put to stack new vector.\nExample: 0:1.5:0") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.matches("-?\\d+(?:\\.\\d+)?:-?\\d+(?:\\.\\d+)?:-?\\d+(?:\\.\\d+)?");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) {
                String op = operand.toString();
                String[] val = op.split(":");
                double x = Double.parseDouble(val[0]);
                double y = Double.parseDouble(val[1]);
                double z = Double.parseDouble(val[2]);
                functionSession.addCommand(wPutObject, new Vector(x, y, z), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("X:Y:Z:world", "stack block", "", "Block", "put to stack new block.\nExample: 3:15:5:world") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.matches("-?\\d+:-?\\d+:-?\\d+:[^:]*(?::B)?");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws CommandException {
                String op = operand.toString();
                String[] val = op.split(":");
                int x = Integer.parseInt(val[0]);
                int y = Integer.parseInt(val[1]);
                int z = Integer.parseInt(val[2]);
                String w = val[3];
                World world;
                if (w.isEmpty()) world = Bukkit.getWorlds().get(0);
                else world = Bukkit.getWorld(w);
                if (world == null)
                    throw new CommandException(operand, compileSession.getSource(), "no world for name " + w);
                functionSession.addCommand(wPutObject, world.getBlockAt(x, y, z), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("x:y:z:world", "stack location", "", "Location", "put to stack new location.\nExample: 0:1.5:0:world") { //0x13
            @Override
            public boolean checkCondition(String string) {
                return string.matches("-?\\d+(?:\\.\\d+)?:-?\\d+(?:\\.\\d+)?:-?\\d+(?:\\.\\d+)?:[^:]*(?::L)?");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws CommandException {
                String op = operand.toString();
                String[] val = op.split(":");
                double x = Double.parseDouble(val[0]);
                double y = Double.parseDouble(val[1]);
                double z = Double.parseDouble(val[2]);
                String w = val[3];
                World world;
                if (w.isEmpty()) world = Bukkit.getWorlds().get(0);
                else world = Bukkit.getWorld(w);
                if (world == null)
                    throw new CommandException(operand, compileSession.getSource(), "no world for name " + w);
                functionSession.addCommand(wPutObject, new Location(world, x, y, z), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("Function{...}", "stack function", "", "Function", "put new function to stack") { //0x14
            @Override
            public boolean checkCondition(String string) {
                return string.matches("[A-Za-z0-9_\\-/]*\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wPutFunction};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("Function{...} ", "stack function", "", "Function", "put new function to stack ") { //0x14
            @Override
            public boolean checkCondition(String string) {
                return string.equals("}");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                throw new CloseFunction(compileSession.getSource(), operand.row, operand.col);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("%Function{...}", "stack function", "", "", "define function as local variable") { //0x14
            @Override
            public boolean checkCondition(String string) {
                return string.matches("%[A-Za-z0-9_\\-/]*\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(1, op.length() - 1);
                functionSession.addCommand(wDefineFunction, VSCompiler.compile(name, compileSession, false), operand);
                compileSession.addVarName(name);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wDefineFunction};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("RUN", "function", "Function", "...", "handle function") { //0x14
            @Override
            public boolean checkCondition(String string) {
                return string.equals("RUN");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wRun, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wRun};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("NEW", "function object", "Function(constructor)", "Fieldable", "create new object with constructor") { //0x14
            @Override
            public boolean checkCondition(String string) {
                return string.equals("NEW");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wRunNew, null, operand);
                functionSession.addCommand(wReadRegisterF, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wRunNew};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("@@constructor", "function object", "", "Fieldable", "create new object with constructor") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches("@@[A-Za-z0-9_\\-/]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String name = operand.builder.toString().substring(2);
                functionSession.addCommand(wGetVariable, name, operand);
                functionSession.addCommand(wRunNew, null, operand);
                functionSession.addCommand(wReadRegisterF, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(".field", "field", "Fieldable", "Object", "get field of object") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches("\\.[A-Za-z0-9_\\-]+(?:\\([A-Za-z0-9_\\-,\\.]*\\))?");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String name = operand.builder.toString().substring(1);
                functionSession.addCommand(wGetField, name, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wGetField};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(".>field", "field", "Object(value) Fieldable", "", "set field of object") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches("\\.>[A-Za-z0-9_\\-]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String name = operand.builder.toString().substring(2);
                functionSession.addCommand(wSetField, name, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wSetField};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(".~field", "field", "Fieldable", "", "delete field of object") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches("\\.~[A-Za-z0-9_\\-]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String name = operand.builder.toString().substring(2);
                functionSession.addCommand(wRemoveField, name, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wRemoveField};
            }
        });


        VSCompiler.addRule(new ComplexCompileRule(":field", "field function", "Fieldable", "...", "apply method for object") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches(":[A-Za-z0-9_\\-]+(?:\\([A-Za-z0-9_\\-,\\.]*\\))?");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String name = operand.builder.toString().substring(1);
                functionSession.addCommand(wRunField, name, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wRunField};
            }
        });


        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic"};

            @Override
            public String toString() {
                return "IF";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("IF");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("IF").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("IF statement\nExample: (condition) IF (action) THEN\n(condition) IF (action1) ELSE (action2) THEN");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addJump(operand, "IF");
                functionSession.addCommand(null);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic"};

            @Override
            public String toString() {
                return "THEN";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("THEN");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("THEN").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("THEN statement\nExample: (condition) IF (action) THEN\n(condition) IF (action1) ELSE (action2) THEN");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String jumpType = functionSession.peekJumpType();
                if (jumpType == null)
                    throw new CommandException(operand, compileSession.getSource(), new RuntimeException("unexpected " + operand));
                int pos = functionSession.getCurrentPos();
                int lastPos = functionSession.peekJumpPosition();
                if (jumpType.equals("IF")) {
                    functionSession.setCommand(lastPos, wJumpFalse, pos - lastPos - 1, operand);
                } else if (jumpType.equals("ELSE")) {
                    functionSession.setCommand(lastPos, wJump, pos - lastPos - 1, operand);
                } else {
                    throw new CommandException(functionSession.peekJumpOperand(), compileSession.getSource(), new RuntimeException("unterminated " + functionSession.peekJumpOperand()));
                }
                functionSession.popJump();
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wJumpFalse, wJump};
            }
        });

        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic"};

            @Override
            public String toString() {
                return "ELSE";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("ELSE");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("ELSE").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("ELSE statement\nExample: (condition) IF (action1) ELSE (action2) THEN");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String jumpType = functionSession.peekJumpType();
                if (jumpType == null)
                    throw new CommandException(operand, compileSession.getSource(), new RuntimeException("unexpected " + operand));
                int pos = functionSession.getCurrentPos();
                int lastPos = functionSession.peekJumpPosition();
                if (jumpType.equals("IF")) {
                    functionSession.setCommand(lastPos, wJumpFalse, pos - lastPos, operand);
                } else {
                    throw new CommandException(functionSession.peekJumpOperand(), compileSession.getSource(), new RuntimeException("unterminated " + functionSession.peekJumpOperand()));
                }
                functionSession.popJump();
                functionSession.addJump(operand, "ELSE");
                functionSession.addCommand(null);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });


        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic", "while"};

            @Override
            public String toString() {
                return "BEGIN";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("BEGIN") || string.equals("BGN");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("BEGIN").append('\n')
                        .append(ChatColor.YELLOW).append("aliases: ").append(ChatColor.WHITE).append("BEGIN BGN").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("BEGIN statement\nExample: BEGIN (condition) WHILE (action) REPEAT\n BEGIN (action) (condition-not) UNTIL");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addJump(operand, "BEGIN");
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic", "while"};

            @Override
            public String toString() {
                return "WHILE";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("WHILE") || string.equals("WHL");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("WHILE").append('\n')
                        .append(ChatColor.YELLOW).append("aliases: ").append(ChatColor.WHITE).append("WHILE WHL").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("WHILE statement\nExample: BEGIN (condition) WHILE (action) REPEAT");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addMetaJump(operand, "WHILE");
                functionSession.addCommand(null);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic", "while"};

            @Override
            public String toString() {
                return "CONTINUE";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("CONTINUE") || string.equals("CNT");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("CONTINUE").append('\n')
                        .append(ChatColor.YELLOW).append("aliases: ").append(ChatColor.WHITE).append("CONTINUE CNT").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("CONTINUE statement\nExample: BEGIN ... (confition) IF CONTINUE THEN ... REPEAT");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addMetaJump(operand, "CONTINUE");
                functionSession.addCommand(null);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic", "while"};

            @Override
            public String toString() {
                return "IFCONTINUE";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("IFCONTINUE") || string.equals("IFCNT");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("IFCONTINUE").append('\n')
                        .append(ChatColor.YELLOW).append("aliases: ").append(ChatColor.WHITE).append("IFCONTINUE IFCNT").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("IFCONTINUE statement\nExample: BEGIN ... (condition-false) IFCONTINUE ... REPEAT");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addMetaJump(operand, "IFCONTINUE");
                functionSession.addCommand(null);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic", "while"};

            @Override
            public String toString() {
                return "BREAK";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("BREAK") || string.equals("BRK");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("BREAK").append('\n')
                        .append(ChatColor.YELLOW).append("aliases: ").append(ChatColor.WHITE).append("BREAK BRK").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("BREAK statement\nExample: BEGIN ... (condition) IF BREAK THEN ... REPEAT");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addMetaJump(operand, "BREAK");
                functionSession.addCommand(null);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic", "while"};

            @Override
            public String toString() {
                return "REPEAT";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("REPEAT") || string.equals("RPT");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("REPEAT").append('\n')
                        .append(ChatColor.YELLOW).append("aliases: ").append(ChatColor.WHITE).append("REPEAT RPT").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("REPEAT statement\nExample: BEGIN (condition) WHILE (action) REPEAT");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String jumpType = functionSession.peekJumpType();
                int beginPos;
                int pos = functionSession.getCurrentPos();
                if (jumpType == null) {
                    throw new CommandException(operand, compileSession.getSource(), new RuntimeException("unexpected " + operand));
                } else if (!jumpType.equals("BEGIN")) {
                    throw new CommandException(functionSession.peekJumpOperand(), compileSession.getSource(), new RuntimeException("unterminated " + functionSession.peekJumpOperand()));
                } else {
                    beginPos = functionSession.peekJumpPosition();
                }
                functionSession.popJump();
                functionSession.addCommand(wJump, beginPos - pos - 1, operand);
                while (true) {
                    jumpType = functionSession.peekMetaJumpType();
                    if (jumpType == null) break;
                    int jumpPos = functionSession.peekMetaJumpPosition();
                    if (jumpPos <= beginPos) break;
                    if (jumpType.equals("WHILE")) {
                        functionSession.setCommand(jumpPos, wJumpFalse, pos - jumpPos, operand);
                    } else if (jumpType.equals("CONTINUE")) {
                        functionSession.setCommand(jumpPos, wJump, beginPos - jumpPos - 1, operand);
                    } else if (jumpType.equals("IFCONTINUE")) {
                        functionSession.setCommand(jumpPos, wJumpTrue, beginPos - jumpPos - 1, operand);
                    } else if (jumpType.equals("BREAK")) {
                        functionSession.setCommand(jumpPos, wJump, pos - jumpPos, operand);
                    } else {
                        throw new CommandException(functionSession.peekMetaJumpOperand(), compileSession.getSource(), new RuntimeException("unexpected " + functionSession.peekMetaJumpOperand()));
                    }
                    functionSession.popMetaJump();
                }
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });


        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic", "while"};

            @Override
            public String toString() {
                return "UNTIL";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.equals("UNTIL") || string.equals("UNT");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("REPEAT").append('\n')
                        .append(ChatColor.YELLOW).append("aliases: ").append(ChatColor.WHITE).append("UNTIL UNT").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("UNTIL statement\nExample: BEGIN (action) (condition) UNTIL");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String jumpType = functionSession.peekJumpType();
                int beginPos;
                int pos = functionSession.getCurrentPos();
                if (jumpType == null) {
                    throw new CommandException(operand, compileSession.getSource(), new RuntimeException("unexpected " + operand));
                } else if (!jumpType.equals("BEGIN")) {
                    throw new CommandException(functionSession.peekJumpOperand(), compileSession.getSource(), new RuntimeException("unterminated " + functionSession.peekJumpOperand()));
                } else {
                    beginPos = functionSession.peekJumpPosition();
                }
                functionSession.popJump();
                functionSession.addCommand(wJumpTrue, beginPos - pos - 1, operand);
                while (true) {
                    jumpType = functionSession.peekMetaJumpType();
                    if (jumpType == null) break;
                    int jumpPos = functionSession.peekMetaJumpPosition();
                    if (jumpPos <= beginPos) break;
                    if (jumpType.equals("WHILE")) {
                        functionSession.setCommand(jumpPos, wJumpFalse, pos - jumpPos, operand);
                    } else if (jumpType.equals("CONTINUE")) {
                        functionSession.setCommand(jumpPos, wJump, beginPos - jumpPos - 1, operand);
                    } else if (jumpType.equals("IFCONTINUE")) {
                        functionSession.setCommand(jumpPos, wJumpTrue, beginPos - jumpPos - 1, operand);
                    } else if (jumpType.equals("BREAK")) {
                        functionSession.setCommand(jumpPos, wJump, pos - jumpPos, operand);
                    } else {
                        throw new CommandException(functionSession.peekMetaJumpOperand(), compileSession.getSource(), new RuntimeException("unexpected " + functionSession.peekMetaJumpOperand()));
                    }
                    functionSession.popMetaJump();
                }
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wJumpTrue};
            }
        });


        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic", "goto"};

            @Override
            public String toString() {
                return "(label)";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.matches("\\([a-zA-Z0-9+\\-_]+\\)");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("(label)").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("label\nExample: (label1) ... GOTO(label1)");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String label = op.substring(1, op.length() - 1);
                Integer labelPos = functionSession.getLabelPos(label);
                if (labelPos != null)
                    throw new CommandException(operand, compileSession.getSource(), new RuntimeException("label " + operand + " is already defined in function"));
                functionSession.addLabel(label);
                int pos = functionSession.getCurrentPos();
                HashSet<Integer> posToRemove = new HashSet<Integer>();
                for (Map.Entry<Integer, String> e : functionSession.labelJumps.entrySet()) {
                    if (!e.getValue().equals(label)) continue;
                    int jumpPos = e.getKey();
                    functionSession.setCommand(e.getKey(), wJump, pos - jumpPos - 1, operand);
                    posToRemove.add(jumpPos);
                }
                for (Integer p : posToRemove) functionSession.labelJumps.remove(p);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new CompileRule() {
            private final String[] tags = new String[]{"structure", "logic", "goto"};

            @Override
            public String toString() {
                return "GOTO(label)";
            }

            @Override
            public boolean checkCondition(String string) {
                return string.matches("GOTO\\([a-zA-Z0-9+\\-_]+\\)");
            }

            @Override
            public String[] getTags() {
                return tags;
            }

            @Override
            public String getDescription() {
                StringBuilder builder = new StringBuilder();
                builder.append(ChatColor.YELLOW).append(ChatColor.BOLD).append("GOTO(label)").append('\n')
                        .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                        .append(ChatColor.WHITE).append(ChatColor.WHITE).append("go to label\nExample: (label1) ... GOTO(label1)");
                return builder.toString();
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String label = op.substring(5, op.length() - 1);
                Integer labelPos = functionSession.getLabelPos(label);
                int pos = functionSession.getCurrentPos();
                if (labelPos == null) {
                    functionSession.labelJumps.put(pos, label);
                    functionSession.addCommand(null);
                } else {
                    functionSession.addCommand(wJump, labelPos - pos - 1, operand);
                }
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new SimpleCompileRule(
                "SETPROTOTYPE",
                "SETPROTOTYPE >PRT",
                "Runnable(constructor) Fieldable(prototype)",
                "Function",
                "function object",
                "set prototype of constructor",
                new SimpleWorker(new int[]{0x1F, 0x00}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Fieldable fieldable = v.pop(Fieldable.class);
                        Runnable runnable;
                        try {
                            runnable = v.peek(Runnable.class);
                        } catch (ConvertException ignored) {
                            runnable = v.peek(CommandList.class).build(f.getScope());
                        }
                        runnable.setPrototype(fieldable);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "PROTOTYPE",
                "PROTOTYPE PRT",
                "Runnable(constructor)",
                "Fieldable(prototype)",
                "function object",
                "get prototype of constructor",
                new SimpleWorker(new int[]{0x1F, 0x01}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object t = v.pop();
                        Runnable runnable;
                        try {
                            runnable = v.convert(Runnable.class, t);
                        } catch (ConvertException ignored) {
                            runnable = v.convert(CommandList.class, t).build(f.getScope());
                        }
                        v.push(runnable.getPrototype());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CONSTRUCTOR",
                "CONSTRUCTOR CONSTR",
                "Fieldable",
                "Function(constructor)",
                "function object",
                "get constructor of object",
                new SimpleWorker(new int[]{0x1F, 0x02}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Fieldable fieldable = v.pop(Fieldable.class);
                        v.push(fieldable.getConstructor());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REFLECT",
                "REFLECT :",
                "Object",
                "Fieldable",
                "field reflection",
                "get reflection object",
                new SimpleWorker(new int[]{0x1F, 0x03}) {
                    @Override
                    public void run(final ThreadRunner r, final Thread v, final Context f, Void d) throws ConvertException {
                        v.push(new ReflectObject(v.pop(), f.getScope()));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LOAD",
                "LOAD",
                "String(filename)",
                "Function",
                "function file",
                "load function from file",
                new SimpleWorker(new int[]{0x1F, 0x04}) {
                    @Override
                    public void run(final ThreadRunner r, final Thread v, final Context f, Void d) throws Exception {
                        String name = v.pop(String.class);
                        ScriptManager manager = v.getProgram().getRuntime().scriptManager;
                        InputStream input = manager.openScriptFile("vsbin", name);
                        if (input != null) {
                            v.push(VSCompiler.read(input).build(f.getScope()));
                            return;
                        }
                        String source = manager.readScriptFile("vs", name);
                        if (source != null) {
                            v.push(VSCompiler.compile(source).build(f.getScope()));
                        }
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CLASS",
                "CLASS",
                "String(className/constructor)",
                "Runnable(constructor)",
                "field reflection function",
                "get constructor of class",
                new SimpleWorker(new int[]{0x1F, 0x05}) {
                    @Override
                    public void run(final ThreadRunner r, final Thread v, final Context f, Void d) throws Exception {
                        String className = v.pop(String.class);
                        v.push(new ReflectClass(className, f.getScope()));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CALL",
                "CALL",
                "Runnable Caller",
                "Program",
                "runtime",
                "call function from other caller",
                new SimpleWorker(new int[]{0x1F, 0x06}) {
                    @Override
                    public void run(final ThreadRunner r, final Thread v, final Context f, Void d) throws Exception {
                        Caller caller = v.pop(Caller.class);
                        Runnable runnable = v.pop(f.getScope());
                        VarscriptProgram program = new VarscriptProgram(v.getProgram().getRuntime(), caller, null);
                        Thread thread = new Thread(program);
                        thread.pushFunction(runnable, null);
                        new ThreadRunner(thread).runThreads();
                        v.push(program);
                    }
                }
        ));

        VSCompiler.addRule(new ComplexCompileRule("$method{...}", "function object", "", "", "define method for this or apply object") { //0x14
            @Override
            public boolean checkCondition(String string) {
                return string.matches("\\$[A-Za-z0-9_\\-/]*\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(1, op.length() - 1);
                functionSession.addCommand(wSetFunctionForThis, VSCompiler.compile(name, compileSession, false), operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wSetFunctionForThis};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("$field", "field", "Object(value)", "", "set field for this or apply object") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches("\\$[A-Za-z0-9_\\-/]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String name = operand.builder.toString().substring(1);
                functionSession.addCommand(wSetFieldForThis, name, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wSetFieldForThis};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("APPLY", "field function", "Runnable(function) Fieldable(object)", "...", "apply function for object") {
            @Override
            public boolean checkCondition(String string) {
                return string.equals("APPLY");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wApply, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wApply};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("*function", "function file", "", "", "load functuion from file and run it") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches("\\*[A-Za-z0-9_\\-]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(1);
                functionSession.addCommand(wFunctionFromFile, name, operand);
                functionSession.addCommand(wRun, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wFunctionFromFile};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("@*function", "function file", "", "Function", "load function from file") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches("@\\*[A-Za-z0-9_\\-]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(2);
                functionSession.addCommand(wFunctionFromFile, name, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("<Class>", "convert", "Object", "Object(converted)", "convert object to specific class") {
            @Override
            public boolean checkCondition(String string) {
                return string.startsWith("<") && string.length() > 2 && string.endsWith(">");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(1, op.length() - 1);
                Class clazz = compileSession.converter.getClassForName(name);
                if (clazz == null) {
                    throw new CommandException(operand, compileSession.getSource(), "class " + name + " not found");
                }
                functionSession.addCommand(wConvert, clazz, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(">>field", "field", "Fieldable(A) Object(value)", "Fieldable(A)", "set field of object A") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches(">>[A-Za-z0-9_\\-/]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String name = operand.builder.toString().substring(2);
                functionSession.addCommand(wStoreField, name, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wStoreField};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(":@function", "function", "", "", "apply function to object\n" +
                "Example:\n {THIS 1 THROW} %throw\n ME:@throw") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches(":@[A-Za-z0-9_\\-]+");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(2);
                functionSession.addCommand(wApplyFunction, name, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wApplyFunction};
            }
        });

    }
}
