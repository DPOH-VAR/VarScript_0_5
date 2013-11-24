package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.event.CustomEvent;
import me.dpohvar.varscript.trigger.*;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Runnable;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitMultiThread {


    public static void load() {
        VSCompiler.addRule(new SimpleCompileRule(
                "TICK",
                "TICK TICKS",
                "Long(delay)",
                "",
                "runtime",
                "wait for (delay) ticks",
                new SimpleWorker(new int[]{0xF0}) {
                    @Override
                    public void run(ThreadRunner r, final me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws Exception {
                        if (v.isFinished()) throw stopThread;
                        final Long delay = v.pop(Long.class);
                        TriggerRunner<Trigger> runner = new TriggerRunner<Trigger>() {
                            @Override
                            public void run(Trigger trigger) {
                                ThreadRunner r = new ThreadRunner();
                                r.pushThread(v);
                                v.removeTrigger(trigger);
                                r.runThreads();
                            }
                        };
                        v.addTrigger(new TriggerDelay(delay, runner));
                        throw stopThread;
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REGISTER",
                "REGISTER REG",
                "Runnable String(event)",
                "Trigger",
                "event listener trigger",
                "register new event listener",
                new SimpleWorker(new int[]{0xF1}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, final Context f, Void d) throws Exception {
                        String eventName = v.pop(String.class);

                        final Runnable function = v.pop(f.getScope());
                        Class<? extends Event> eventClass = TriggerBukkitEvent.getEventClass(eventName);

                        TriggerRunner<Event> runner = new TriggerRunner<Event>() {
                            @Override
                            public void run(Event event) {
                                ThreadRunner r = new ThreadRunner();
                                Thread thread = new Thread(v.getProgram());
                                r.pushThread(thread);
                                Context context = thread.pushFunction(function, f.getApply());
                                context.setRegisterE(f);
                                context.getScope().setVar("Event", event);
                                r.runThreads();
                            }
                        };
                        Trigger trigger = new TriggerBukkitEvent(eventClass, EventPriority.NORMAL, runner);
                        v.push(trigger);
                        v.addTrigger(trigger);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "WAIT",
                "WAIT",
                "Double(N)",
                "",
                "runtime",
                "wait for N seconds",
                new SimpleWorker(new int[]{0xF2}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        if (v.isFinished()) throw stopThread;
                        final Double delay = v.pop(Double.class);
                        TriggerRunner<Trigger> runner = new TriggerRunner<Trigger>() {
                            @Override
                            public void run(Trigger trigger) {
                                ThreadRunner r = new ThreadRunner();
                                r.pushThread(v);
                                v.removeTrigger(trigger);
                                r.runThreads();
                            }
                        };
                        v.addTrigger(new TriggerWait((long) (delay * 1000.0), runner));
                        throw stopThread;
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REGISTERPRIORITY",
                "REGISTERPRIORITY REGP",
                "Runnable String(event) #EventPriority",
                "Trigger",
                "event listener trigger",
                "register new event listener with custom priority",
                new SimpleWorker(new int[]{0xF3}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, final Context f, Void d) throws Exception {
                        EventPriority priority = v.pop(EventPriority.values());
                        String eventName = v.pop(String.class);

                        final Runnable function = v.pop(f.getScope());
                        Class<? extends Event> eventClass = TriggerBukkitEvent.getEventClass(eventName);

                        TriggerRunner<Event> runner = new TriggerRunner<Event>() {
                            @Override
                            public void run(Event event) {
                                ThreadRunner r = new ThreadRunner();
                                Thread thread = new Thread(v.getProgram());
                                r.pushThread(thread);
                                Context context = thread.pushFunction(function, f.getApply());
                                context.setRegisterE(f);
                                context.getScope().setVar("Event", event);
                                r.runThreads();
                            }
                        };
                        Trigger trigger = new TriggerBukkitEvent(eventClass, priority, runner);
                        v.push(trigger);
                        v.addTrigger(trigger);
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "START",
                "START",
                "Runnable(function)",
                "Thread",
                "thread runtime function",
                "run function in new thread",
                new SimpleWorker(new int[]{0xF4}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        Runnable runnable = v.pop(f.getScope());
                        Thread newThread = new Thread(v.getProgram());
                        newThread.pushFunction(runnable, f.getApply()).setRegisterE(f);
                        r.pushThread(newThread);
                        v.push(newThread);
                        throw interruptThread;
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "STOP",
                "STOP",
                "Thread",
                "",
                "thread runtime",
                "stop thread",
                new SimpleWorker(new int[]{0xF5}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        Thread thread = v.pop(Thread.class);
                        thread.setFinished();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "WAITFOR",
                "WAITFOR",
                "String(event)",
                "Event",
                "event trigger runtime",
                "wait for event",
                new SimpleWorker(new int[]{0xF6}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, final Context f, Void d) throws Exception {
                        String eventName = v.pop(String.class);
                        if (v.isFinished()) throw stopThread;
                        Class<? extends Event> eventClass = TriggerBukkitEvent.getEventClass(eventName);

                        TriggerRunner<TriggerWaitFor.Container> runner = new TriggerRunner<TriggerWaitFor.Container>() {
                            @Override
                            public void run(TriggerWaitFor.Container container) {
                                container.trigger.unregister();
                                ThreadRunner r = new ThreadRunner();
                                r.pushThread(v);
                                v.push(container.event);
                                r.runThreads();
                            }
                        };
                        Trigger trigger = new TriggerWaitFor(eventClass, EventPriority.NORMAL, runner);
                        v.addTrigger(trigger);
                        throw stopThread;
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "THREAD",
                "THREAD",
                "",
                "Thread",
                "thread",
                "put to stack this thread",
                new SimpleWorker(new int[]{0xF7}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        v.push(v);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "RETURN",
                "RETURN RET",
                "",
                "",
                "function",
                "stop current function",
                new SimpleWorker(new int[]{0xF8}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        throw stopFunction;
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SLEEP",
                "SLEEP",
                "",
                "",
                "thread runtime",
                "wait for WAKEUP from other thread",
                new SimpleWorker(new int[]{0xF9}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        if (v.isFinished()) throw stopThread;
                        v.setSleep();
                        throw stopThread;
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "WAKEUP",
                "WAKEUP",
                "Thread",
                "",
                "thread runtime",
                "wake up other sleeping thread",
                new SimpleWorker(new int[]{0xFA}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        Thread thread = v.pop(Thread.class);
                        if (thread.isSleeping() && !thread.isFinished()) {
                            r.pushThread(thread);
                            throw interruptThread;
                        }
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FINISH",
                "FINISH FIN",
                "",
                "",
                "thread runtime",
                "finish this thread",
                new SimpleWorker(new int[]{0xFB}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        v.setFinished();
                        throw interruptThread;
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "END",
                "END",
                "",
                "",
                "runtime",
                "stop this program",
                new SimpleWorker(new int[]{0xFC}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        v.getProgram().setFinished();
                        throw interruptRunner;
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CUSTOMEVENT",
                "CUSTOMEVENT",
                "String(name)",
                "CustomEvent",
                "runtime",
                "create custom event",
                new SimpleWorker(new int[]{0xFD}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        v.push(new CustomEvent(v.pop(String.class)));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CALLEVENT",
                "CALLEVENT",
                "Event",
                "Event",
                "runtime",
                "call event",
                new SimpleWorker(new int[]{0xFE}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, Context f, Void d) throws Exception {
                        Bukkit.getServer().getPluginManager().callEvent(v.pop(Event.class));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REGISTERCUSTOM",
                "REGISTERCUSTOM REGC",
                "Runnable String(name)",
                "Trigger",
                "event listener trigger",
                "register new custom event listener",
                new SimpleWorker(new int[]{0xFF, 0x00}) {
                    @Override
                    public void run(ThreadRunner r, final Thread v, final Context f, Void d) throws Exception {
                        final String name = v.pop(String.class);

                        final Runnable function = v.pop(f.getScope());

                        TriggerRunner<CustomEvent> runner = new TriggerRunner<CustomEvent>() {
                            @Override
                            public void run(CustomEvent event) {
                                if (!name.equals(event.getName())) return;
                                ThreadRunner r = new ThreadRunner();
                                Thread thread = new Thread(v.getProgram());
                                r.pushThread(thread);
                                Context context = thread.pushFunction(function, f.getApply());
                                context.setRegisterE(f);
                                context.getScope().setVar("Event", event);
                                r.runThreads();
                            }
                        };
                        Trigger trigger = new TriggerBukkitEvent<CustomEvent>(CustomEvent.class, EventPriority.NORMAL, runner);
                        v.push(trigger);
                        v.addTrigger(trigger);
                    }
                }
        ));


    }
}
