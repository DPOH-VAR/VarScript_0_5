package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.scheduler.*;
import me.dpohvar.varscript.vs.converter.ConvertException;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitMultiThread {

    private static final String[] classPrefix = new String[]{
            "org.bukkit.event.",
            "org.bukkit.event.enchantment.",
            "org.bukkit.event.entity.",
            "org.bukkit.event.hanging.",
            "org.bukkit.event.inventory.",
            "org.bukkit.event.painting.",
            "org.bukkit.event.player.",
            "org.bukkit.event.server.",
            "org.bukkit.event.vehicle.",
            "org.bukkit.event.weather.",
            "org.bukkit.event.world.",
    };


    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "TICK",
                "TICK TICKS",
                "Long(delay)",
                "",
                "runtime",
                "wait for (delay) ticks",
                new VSSimpleWorker(new int[]{0xF0}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
                        final Long delay = v.pop(Long.class);
                        TriggerRunner<VSTrigger> runner = new TriggerRunner<VSTrigger>() {
                            @Override public void run(VSTrigger trigger) {
                                VSThreadRunner r = new VSThreadRunner();
                                r.pushThread(v);
                                v.removeTrigger(trigger);
                                r.runThreads();
                            }
                        };
                        v.addTrigger(new TriggerTicks(delay,runner));
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
                new VSSimpleWorker(new int[]{0xF1}){
                    @Override public void run(VSThreadRunner r,final VSThread v, final VSContext f, Void d) throws Exception {
                        String eventName = v.pop(String.class);

                        VSFieldable obj = v.pop(VSFieldable.class);
                        VSRunnable runnable;
                        try{
                            runnable = v.peek(VSRunnable.class);
                        } catch (ConvertException ignored){
                            runnable = v.peek(VSNamedCommandList.class).build(f.getScope());
                        }
                        final VSRunnable function = runnable;

                        Class<? extends Event> eventClass = null;
                        try {
                            eventClass = (Class<? extends Event>) Class.forName(eventName);
                        } catch (Exception ignored){}
                        if (eventClass==null) for(String prefix:classPrefix) try {
                            try{
                                eventClass = (Class<? extends Event>) Class.forName(prefix+eventName);
                            } catch (Exception ignored){
                                eventClass = (Class<? extends Event>) Class.forName(prefix+eventName+"Event");
                            }
                        } catch (Exception ignored){}

                        TriggerRunner<Event> runner = new TriggerRunner<Event>() {
                            @Override public void run(Event event) {
                                VSThreadRunner r =new VSThreadRunner();
                                VSThread thread = new VSThread(v.getProgram());
                                r.pushThread(thread);
                                VSContext context = thread.pushFunction(function, f.getApply());
                                context.getScope().setVar("Event", event);
                                r.runThreads();
                            }
                        };
                        VSTrigger trigger = new TriggerEvent(eventClass, EventPriority.NORMAL,runner);
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
                new VSSimpleWorker(new int[]{0xF2}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
                        final Double delay = v.pop(Double.class);
                        TriggerRunner<VSTrigger> runner = new TriggerRunner<VSTrigger>() {
                            @Override public void run(VSTrigger trigger) {
                                VSThreadRunner r = new VSThreadRunner();
                                r.pushThread(v);
                                v.removeTrigger(trigger);
                                r.runThreads();
                            }
                        };
                        v.addTrigger(new TriggerWait((long)(delay.doubleValue()*1000.0),runner));
                        throw stopThread;
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REGISTERPRIORITY",
                "REGISTERPRIORITY REGP",
                "Runnable String(event) EventPriority(Enum)",
                "Trigger",
                "event listener trigger",
                "register new event listener with custom priority",
                new VSSimpleWorker(new int[]{0xF3}){
                    @Override public void run(VSThreadRunner r,final VSThread v, final VSContext f, Void d) throws Exception {
                        EventPriority priority = v.pop(EventPriority.values());
                        String eventName = v.pop(String.class);

                        VSFieldable obj = v.pop(VSFieldable.class);
                        VSRunnable runnable;
                        try{
                            runnable = v.peek(VSRunnable.class);
                        } catch (ConvertException ignored){
                            runnable = v.peek(VSNamedCommandList.class).build(f.getScope());
                        }
                        final VSRunnable function = runnable;

                        Class<? extends Event> eventClass = null;
                        try {
                            eventClass = (Class<? extends Event>) Class.forName(eventName);
                        } catch (Exception ignored){}
                        if (eventClass==null) for(String prefix:classPrefix) try {
                            try{
                                eventClass = (Class<? extends Event>) Class.forName(prefix+eventName);
                            } catch (Exception ignored){
                                eventClass = (Class<? extends Event>) Class.forName(prefix+eventName+"Event");
                            }
                        } catch (Exception ignored){}

                        TriggerRunner<Event> runner = new TriggerRunner<Event>() {
                            @Override public void run(Event event) {
                                VSThreadRunner r =new VSThreadRunner();
                                VSThread thread = new VSThread(v.getProgram());
                                r.pushThread(thread);
                                VSContext context = thread.pushFunction(function, f.getApply());
                                context.getScope().setVar("Event", event);
                                r.runThreads();
                            }
                        };
                        VSTrigger trigger = new TriggerEvent(eventClass, priority,runner);
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
                new VSSimpleWorker(new int[]{0xF4}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
                        VSFieldable obj = v.pop(VSFieldable.class);
                        VSRunnable runnable;
                        try{
                            runnable = v.peek(VSRunnable.class);
                        } catch (ConvertException ignored){
                            runnable = v.peek(VSNamedCommandList.class).build(f.getScope());
                        }
                        VSThread newThread = new VSThread(v.getProgram());
                        newThread.pushFunction(runnable, f.getApply());
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
                new VSSimpleWorker(new int[]{0xF5}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
                        VSThread thread = v.pop(VSThread.class);
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
                new VSSimpleWorker(new int[]{0xF6}){
                    @Override public void run(VSThreadRunner r,final VSThread v, final VSContext f, Void d) throws Exception {
                        String eventName = v.pop(String.class);

                        Class<? extends Event> eventClass = null;
                        try {
                            eventClass = (Class<? extends Event>) Class.forName(eventName);
                        } catch (Exception ignored){}
                        if (eventClass==null) for(String prefix:classPrefix) try {
                            try{
                                eventClass = (Class<? extends Event>) Class.forName(prefix+eventName);
                            } catch (Exception ignored){
                                eventClass = (Class<? extends Event>) Class.forName(prefix+eventName+"Event");
                            }
                        } catch (Exception ignored){}

                        TriggerRunner<TriggerWaitFor.Container> runner = new TriggerRunner<TriggerWaitFor.Container>() {
                            @Override public void run(TriggerWaitFor.Container container) {
                                container.trigger.unregister();
                                VSThreadRunner r =new VSThreadRunner();
                                r.pushThread(v);
                                v.push(container.event);
                                r.runThreads();
                            }
                        };
                        VSTrigger trigger = new TriggerWaitFor(eventClass, EventPriority.NORMAL, runner);
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
                new VSSimpleWorker(new int[]{0xF7}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
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
                new VSSimpleWorker(new int[]{0xF8}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
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
                new VSSimpleWorker(new int[]{0xF9}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
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
                new VSSimpleWorker(new int[]{0xFA}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
                        VSThread thread = v.pop(VSThread.class);
                        if (thread.isSleeping() && !thread.isFinished()){
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
                new VSSimpleWorker(new int[]{0xFB}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
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
                new VSSimpleWorker(new int[]{0xFC}){
                    @Override public void run(VSThreadRunner r,final VSThread v, VSContext f, Void d) throws Exception {
                        v.getProgram().setFinished();
                        throw interruptRunner;
                    }
                }
        ));



    }
}
