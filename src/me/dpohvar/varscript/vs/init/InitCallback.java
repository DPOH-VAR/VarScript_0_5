package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Runnable;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.ComplexCompileRule;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.vs.compiler.VSSmartParser;
import me.dpohvar.varscript.vs.exception.SourceException;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitCallback {

    public static void sortAsc(List orig, List sort, int low, int high) {
        int i = low;
        int j = high;
        Comparable x = (Comparable) sort.get((low + high) / 2);
        do {
            while (((Comparable) sort.get(i)).compareTo(x) < 0) ++i;
            while (((Comparable) sort.get(j)).compareTo(x) > 0) --j;
            if (i <= j) {
                Object tempD = sort.get(i);
                sort.set(i, sort.get(j));
                sort.set(j, tempD);
                Object tempO = orig.get(i);
                orig.set(i, orig.get(j));
                orig.set(j, tempO);
                i++;
                j--;
            }
        } while (i < j);
        if (low < j) sortAsc(orig, sort, low, j);
        if (i < high) sortAsc(orig, sort, i, high);
    }

    public static void sortDesc(List orig, List sort, int low, int high) {
        int i = low;
        int j = high;
        Comparable x = (Comparable) sort.get((low + high) / 2);
        do {
            while (((Comparable) sort.get(i)).compareTo(x) > 0) ++i;
            while (((Comparable) sort.get(j)).compareTo(x) < 0) --j;
            if (i <= j) {
                Object tempD = sort.get(i);
                sort.set(i, sort.get(j));
                sort.set(j, tempD);
                Object tempO = orig.get(i);
                orig.set(i, orig.get(j));
                orig.set(j, tempO);
                i++;
                j--;
            }
        } while (i < j);
        if (low < j) sortDesc(orig, sort, low, j);
        if (i < high) sortDesc(orig, sort, i, high);
    }

    private static class VarscriptCommandSender implements CommandSender {

        StringBuilder buffer = new StringBuilder();

        public String getAnswer() {
            int size = buffer.length();
            if (size != 0) {
                if (buffer.charAt(size - 1) == '\n') {
                    buffer.deleteCharAt(size - 1);
                }
            }
            return buffer.toString();
        }

        @Override
        public void sendMessage(String s) {
            buffer.append(s).append('\n');
        }

        @Override
        public void sendMessage(String[] strings) {
            for (String s : strings) buffer.append(s).append('\n');
        }

        @Override
        public Server getServer() {
            return Bukkit.getServer();
        }

        @Override
        public String getName() {
            return "VarScript";
        }

        @Override
        public boolean isPermissionSet(String s) {
            return true;
        }

        @Override
        public boolean isPermissionSet(Permission permission) {
            return true;
        }

        @Override
        public boolean hasPermission(String s) {
            return true;
        }

        @Override
        public boolean hasPermission(Permission permission) {
            return true;
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
            return null;
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin) {
            return null;
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
            return null;
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin, int i) {
            return null;
        }

        @Override
        public void removeAttachment(PermissionAttachment permissionAttachment) {
        }

        @Override
        public void recalculatePermissions() {
        }

        @Override
        public Set<PermissionAttachmentInfo> getEffectivePermissions() {
            return null;
        }

        @Override
        public boolean isOp() {
            return true;
        }

        @Override
        public void setOp(boolean b) {
        }
    }


    public static Worker<Void> wMapStart = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws Exception {
            f.setRegisterA(v.pop(f.getScope()));
            f.setRegisterB(v.pop(Iterable.class).iterator());
            f.setRegisterC(new ArrayList());
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE0);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE0};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wMapCheck, null);
            readSession.addCommandAfter(wMapAgain, null);
            return null;
        }
    };

    public static Worker<Void> wMapCheck = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Iterator iterator = (Iterator) f.getRegisterB();
            if (iterator != null && iterator.hasNext()) {
                Object apply = iterator.next();
                v.push(apply);
                v.pushFunction((Runnable) f.getRegisterA(), apply).setRegisterE(f);
                throw interruptFunction;
            } else {
                v.push(f.getRegisterC());
                f.jumpPointer(1);
            }
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wMapAgain = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Collection c = (Collection) f.getRegisterC();
            c.add(v.pop());
            f.jumpPointer(-2);

        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wSelectStart = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterA(v.pop(f.getScope()));
            f.setRegisterB(v.pop(Iterable.class).iterator());
            f.setRegisterC(new ArrayList());
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE1);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE1};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wSelectCheck, null);
            readSession.addCommandAfter(wSelectAgain, null);
            return null;
        }
    };
    public static Worker<Void> wSelectCheck = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Iterator iterator = (Iterator) f.getRegisterB();
            if (iterator != null && iterator.hasNext()) {
                Object apply = iterator.next();
                f.setRegisterF(apply);
                v.push(apply);
                v.pushFunction((Runnable) f.getRegisterA(), apply).setRegisterE(f);
                throw interruptFunction;
            } else {
                v.push(f.getRegisterC());
                f.jumpPointer(1);
            }
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wSelectAgain = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            boolean bool = v.pop(Boolean.class);
            Collection c = (Collection) f.getRegisterC();
            if (bool) c.add(f.getRegisterF());
            f.jumpPointer(-2);

        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wTry = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Runnable fun = v.pop(f.getScope());
            if (fun instanceof Function) ((Function) fun).ignoreExceptions = true;
            v.pushFunction(fun, f.getApply()).setRegisterE(f);
            throw interruptThread;
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE2);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE2};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wSelectCheck, null);
            readSession.addCommandAfter(wSelectAgain, null);
            return null;
        }
    };

    public static Worker<Void> wEachStart = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterA(v.pop(f.getScope()));
            f.setRegisterB(v.pop(Iterable.class).iterator());
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE3);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE3};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wEachCheck, null);
            readSession.addCommandAfter(wEachAgain, null);
            return null;
        }
    };

    public static Worker<Void> wEachCheck = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Iterator iterator = (Iterator) f.getRegisterB();
            if (iterator != null && iterator.hasNext()) {
                Object apply = iterator.next();
                f.setRegisterF(apply);
                v.push(apply);
                v.pushFunction((Runnable) f.getRegisterA(), apply).setRegisterE(f);
                throw interruptFunction;
            } else {
                f.jumpPointer(1);
            }
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wEachAgain = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.jumpPointer(-2);

        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wFoldStart = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Runnable run = v.pop(f.getScope());
            Iterator iterator = v.pop(Iterable.class).iterator();
            if (!iterator.hasNext()) {
                v.push(null);
                f.jumpPointer(2);
                return;
            }
            v.push(iterator.next());
            f.setRegisterA(run);
            f.setRegisterB(iterator);
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE4);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE4};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wEachCheck, null);
            readSession.addCommandAfter(wEachAgain, null);
            return null;
        }
    };

    public static Worker<Void> wDoStart = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Runnable run = v.pop(f.getScope());
            int iterate = v.pop(Integer.class);
            int limit = v.pop(Integer.class);
            if (iterate == limit) {
                f.jumpPointer(2);
                return;
            }
            f.setRegisterA(run);
            f.setRegisterB(limit);
            f.setRegisterC(limit > iterate);
            f.setRegisterD(iterate);
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE5);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE5};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wEachCheck, null);
            readSession.addCommandAfter(wEachAgain, null);
            return null;
        }
    };

    public static Worker<Void> wDoCheck = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Runnable run = (Runnable) f.getRegisterA();
            Integer limit = (Integer) f.getRegisterB();
            boolean cond = (Boolean) f.getRegisterC();
            Integer iterate = (Integer) f.getRegisterD();
            if ((iterate != null) && (limit != null) && (iterate < limit == cond)) {
                v.pushFunction(run, f.getApply()).setRegisterE(f);
                throw interruptFunction;
            } else {
                f.jumpPointer(1);
            }
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wDoAgain = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterD((Integer) f.getRegisterD() + 1);
            f.jumpPointer(-2);
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wDoI = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Object i = null;
            Context top = (Context) f.getRegisterE();
            if (top != null) i = top.getRegisterD();
            v.push(i);
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE6};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wEndLoop = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Context top = (Context) f.getRegisterE();
            if (top != null) {
                top.setRegisterB(null);
            }
            throw stopFunction;
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE7};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };


    public static Worker<Void> wFilterStart = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterA(v.pop(f.getScope())); // A = program
            Iterable itr = v.pop(Iterable.class);
            f.setRegisterB(itr.iterator()); // B = iterator
            f.setRegisterC(itr); // C = source collection
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE8);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE8};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wFilterCheck, null);
            readSession.addCommandAfter(wFilterAgain, null);
            return null;
        }
    };
    public static Worker<Void> wFilterCheck = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Iterator iterator = (Iterator) f.getRegisterB();
            if (iterator != null && iterator.hasNext()) {
                Object apply = iterator.next();
                f.setRegisterF(apply); // F = apply
                v.push(apply);
                v.pushFunction((Runnable) f.getRegisterA(), apply).setRegisterE(f); // E - callback
                throw interruptFunction;
            } else {
                v.push(f.getRegisterC()); // C = source array
                f.jumpPointer(1);
            }
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wFilterAgain = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            boolean bool = v.pop(Boolean.class);
            Iterator iterator = (Iterator) f.getRegisterB();
            if (!bool) iterator.remove();
            f.jumpPointer(-2);

        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };


    public static Worker<Void> wSortAscStart = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterA(v.pop(f.getScope()));
            List orig = v.pop(List.class);
            f.setRegisterB(orig.iterator());
            f.setRegisterC(new ArrayList());
            f.setRegisterD(orig);
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE9);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xE9};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wSortAscCheck, null);
            readSession.addCommandAfter(wSortAscAgain, null);
            return null;
        }
    };

    public static Worker<Void> wSortAscCheck = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Iterator iterator = (Iterator) f.getRegisterB();
            if (iterator != null && iterator.hasNext()) {
                Object apply = iterator.next();
                v.push(apply);
                v.pushFunction((Runnable) f.getRegisterA(), apply).setRegisterE(f);
                throw interruptFunction;
            } else {
                List sort = (List) f.getRegisterC();
                List orig = (List) f.getRegisterD();
                if (sort.size() == orig.size() && sort.size() > 0) {
                    sortAsc(orig, sort, 0, sort.size() - 1);
                }
                v.push(orig);
                f.jumpPointer(1);
            }
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wSortAscAgain = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            ArrayList c = (ArrayList) f.getRegisterC();
            c.add(v.pop());
            f.jumpPointer(-2);
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wSortDescStart = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            f.setRegisterA(v.pop(f.getScope()));
            List orig = v.pop(List.class);
            f.setRegisterB(orig.iterator());
            f.setRegisterC(new ArrayList());
            f.setRegisterD(orig);
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
            out.write(0xE9);
        }

        @Override
        public byte[] getBytes() {
            return new byte[]{(byte) 0xEA};
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            readSession.addCommandAfter(wSortDescCheck, null);
            readSession.addCommandAfter(wSortDescAgain, null);
            return null;
        }
    };

    public static Worker<Void> wSortDescCheck = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            Iterator iterator = (Iterator) f.getRegisterB();
            if (iterator != null && iterator.hasNext()) {
                Object apply = iterator.next();
                v.push(apply);
                v.pushFunction((Runnable) f.getRegisterA(), apply).setRegisterE(f);
                throw interruptFunction;
            } else {
                List sort = (List) f.getRegisterC();
                List orig = (List) f.getRegisterD();
                if (sort.size() == orig.size() && sort.size() > 0) {
                    sortDesc(orig, sort, 0, sort.size() - 1);
                }
                v.push(orig);
                f.jumpPointer(1);
            }
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };

    public static Worker<Void> wSortDescAgain = new Worker<Void>() {
        @Override
        public void run(ThreadRunner r, Thread v, Context f, Void d) throws Exception {
            ArrayList c = (ArrayList) f.getRegisterC();
            c.add(v.pop());
            f.jumpPointer(-2);
        }

        @Override
        public void save(OutputStream out, Void data) throws IOException {
        }

        @Override
        public byte[] getBytes() {
            return null;
        }

        @Override
        public Void readObject(InputStream input, VSCompiler.ReadSession readSession) throws IOException {
            return null;
        }
    };


    public static void load() {

        VSCompiler.addRule(new ComplexCompileRule(":MAP{...}", "while collection", "Collection(old)", "ArrayList(new)", "map all elements in collection by function. Example: [1,2,3]:MAP{100 +} ## [101,102,103]") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.matches(":(MAP)?\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(InitDynamic.wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
                functionSession.addCommand(wMapStart, null, operand);
                functionSession.addCommand(wMapCheck, null, operand);
                functionSession.addCommand(wMapAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wMapStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("MAP", "while collection", "Collection(old) Runnable(F)", "ArrayList(new)", "map all elements in collection with function F\nExample: [1,2,3] {100 +} MAP ## [101,102,103]") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.equals("MAP");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wMapStart, null, operand);
                functionSession.addCommand(wMapCheck, null, operand);
                functionSession.addCommand(wMapAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(":SELECT{...}", "while collection", "Collection(old)", "ArrayList(new)", "choose from old collection only that satisfy the condition\nExample: [1,12,3,15]:SELECT{10 >} ## [12, 15]") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.matches(":(SELECT|\\?)\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(InitDynamic.wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
                functionSession.addCommand(wSelectStart, null, operand);
                functionSession.addCommand(wSelectCheck, null, operand);
                functionSession.addCommand(wSelectAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wSelectStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("SELECT", "while collection", "Collection(old) Runnable(F)", "ArrayList(new)", "choose from old collection only that satisfy the condition F\nExample: [1,12,3,15] {10 >} SELECT ## [12, 15]") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.equals("SELECT");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wSelectStart, null, operand);
                functionSession.addCommand(wSelectCheck, null, operand);
                functionSession.addCommand(wSelectAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("@TRY{...}", "function runtime", "...", "...", "run function, ignore exceptions") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.matches("@(TRY)?\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(InitDynamic.wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
                functionSession.addCommand(wTry, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wTry};
            }
        });


        VSCompiler.addRule(new ComplexCompileRule(":EACH{...}", "function list", "Collection(old)", "", "apply function for each entry\nExample: [1,12,3,15]:EACH{PRINT}") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.matches(":(?:EACH|@)\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(InitDynamic.wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
                functionSession.addCommand(wEachStart, null, operand);
                functionSession.addCommand(wEachCheck, null, operand);
                functionSession.addCommand(wEachAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wEachStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("EACH", "while collection", "Collection(old) Runnable(F)", "", "apply function for each entry\nExample: [1,12,3,15] {PRINT} EACH") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.equals("EACH");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wEachStart, null, operand);
                functionSession.addCommand(wEachCheck, null, operand);
                functionSession.addCommand(wEachAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(":FOLD{...}", "while collection", "Collection(old)", "Object(result)", "fold collection to left\nExample: [1,5,100]:FOLD{+} ## returns 106") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.matches(":FOLD\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(InitDynamic.wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
                functionSession.addCommand(wFoldStart, null, operand);
                functionSession.addCommand(wEachCheck, null, operand);
                functionSession.addCommand(wEachAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wFoldStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("FOLD", "while collection", "Collection(old)", "Object(result)", "fold collection to left\nExample: [1,5,100] {+} FOLD ## returns 106") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.equals("FOLD");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wFoldStart, null, operand);
                functionSession.addCommand(wEachCheck, null, operand);
                functionSession.addCommand(wEachAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("@DO{...}", "loop", "Integer(end) Integer(start)", "", "loop for values from start to end\nExample: 10 0 @DO{I PRINT}") {
            @Override
            public boolean checkCondition(String string) {
                return string.equals("@DO{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(InitDynamic.wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
                functionSession.addCommand(wDoStart, null, operand);
                functionSession.addCommand(wDoCheck, null, operand);
                functionSession.addCommand(wDoAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wDoStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("I", "loop iterator", "", "Integer(iterator)", "get iterator value inside loop\nExample: 10 0 @DO{I PRINT}") {
            @Override
            public boolean checkCondition(String string) {
                return string.equals("I");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wDoI, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wDoI};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("ENDLOOP", "loop while", "", "", "break loop with inside function\n") {
            @Override
            public boolean checkCondition(String string) {
                return string.equals("ENDLOOP") || string.equals("ENDLOOP");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wEndLoop, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wEndLoop};
            }
        });

        VSCompiler.addRule(new SimpleCompileRule(
                "CONSOLE",
                "CONSOLE CON",
                "String(command)",
                "String(result)",
                "console",
                "execute console command",
                new SimpleWorker(new int[]{0xEF, 0x10}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String command = v.pop(String.class);
                        VarscriptCommandSender sender = new VarscriptCommandSender();
                        Bukkit.dispatchCommand(sender, command);
                        v.push(sender.getAnswer());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BROADCAST",
                "BROADCAST BC",
                "String(message)",
                "",
                "console",
                "broadcast message",
                new SimpleWorker(new int[]{0xEF, 0x11}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Bukkit.broadcastMessage(
                                v.pop(String.class)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new ComplexCompileRule(":FILTER{...}", "while collection", "Collection", "Collection", "filtering array by condition\nExample: [1,12,3,15]:FILTER{10 >} ## [12, 15]\nThis command changes the original collection") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.matches(":(?:FILTER|=)\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(InitDynamic.wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
                functionSession.addCommand(wFilterStart, null, operand);
                functionSession.addCommand(wFilterCheck, null, operand);
                functionSession.addCommand(wFilterAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wFilterStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule("FILTER", "while collection", "Collection", "Collection", "filtering array by condition\nExample: [1,12,3,15] {10 >} FILTER ## [12, 15]\nThis command changes the original collection") { //0x10
            @Override
            public boolean checkCondition(String string) {
                return string.equals("FILTER");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                functionSession.addCommand(wFilterStart, null, operand);
                functionSession.addCommand(wFilterCheck, null, operand);
                functionSession.addCommand(wFilterAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return null;
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(":SORTASC{...}", "while collection", "Collection", "Collection", "sorting array in ascending order by criteria") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches(":SORT(?:ASC|\\+)?\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(InitDynamic.wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
                functionSession.addCommand(wSortAscStart, null, operand);
                functionSession.addCommand(wSortAscCheck, null, operand);
                functionSession.addCommand(wSortAscAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wSortAscStart};
            }
        });

        VSCompiler.addRule(new ComplexCompileRule(":SORTDESC{...}", "while collection", "Collection", "Collection", "sorting array in descending order by criteria") {
            @Override
            public boolean checkCondition(String string) {
                return string.matches(":SORT(?:DESC|\\-)\\{");
            }

            @Override
            public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
                String op = operand.toString();
                String name = op.substring(0, op.length() - 1);
                functionSession.addCommand(InitDynamic.wPutFunction, VSCompiler.compile(name, compileSession, false), operand);
                functionSession.addCommand(wSortDescStart, null, operand);
                functionSession.addCommand(wSortDescCheck, null, operand);
                functionSession.addCommand(wSortDescAgain, null, operand);
            }

            @Override
            public Worker[] getNewWorkersWithRules() {
                return new Worker[]{wSortDescStart};
            }
        });


    }
}