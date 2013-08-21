package org.dpohvar.varscript.trigger;

import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;
import org.dpohvar.varscript.VarScript;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 13.07.13
 * Time: 1:34
 */
public class TriggerWaitFor implements Trigger<TriggerWaitFor.Container> {

    private RegisteredListener registeredListener;
    private HandlerList handlerList;
    private TriggerRunner<TriggerWaitFor.Container> runner;
    private boolean registered = true;

    public TriggerWaitFor(final Class<? extends Event> clazz, EventPriority priority, TriggerRunner<TriggerWaitFor.Container> runner) {
        this.runner = runner;
        final TriggerWaitFor trigger = this;
        EventExecutor executor = new EventExecutor() {
            public void execute(Listener listener, Event event) throws EventException {
                if (clazz.isInstance(event)) handle(new Container(event, trigger));
            }
        };
        registeredListener = new RegisteredListener(null, executor, priority, VarScript.instance, false);
        handlerList = TriggerBukkitEvent.getEventListeners(TriggerBukkitEvent.getRegistrationClass(clazz));
        handlerList.register(registeredListener);
    }

    @Override
    public void unregister() {
        handlerList.unregister(registeredListener);
        registered = false;
    }

    @Override
    public boolean isRegistered() {
        return registered;
    }

    @Override
    public void handle(TriggerWaitFor.Container container) {
        unregister();
        runner.run(container);
    }

    public class Container {
        public final Event event;
        public final TriggerWaitFor trigger;

        public Container(Event event, TriggerWaitFor trigger) {
            this.event = event;
            this.trigger = trigger;
        }
    }
}
