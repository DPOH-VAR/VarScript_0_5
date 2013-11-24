package me.dpohvar.varscript.task.event;

import me.dpohvar.varscript.task.Event;
import me.dpohvar.varscript.task.Task;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 16.11.13
 * Time: 23:04
 */
public class ChatEvent extends Event {

    private Listener listener;
    private String message;

    public ChatEvent(Task task, String type, String params) {
        super(task, type, params);
        message = params;
    }

    public boolean register() {
        assert listener == null;
        listener = new Listener() {
            @SuppressWarnings("deprecation")
            @EventHandler(priority = EventPriority.NORMAL)
            public void onChat(PlayerChatEvent event) {
                String message = event.getMessage();
                if (message != null && !message.equals(ChatEvent.this.message)) return;

                HashMap<String, Object> environment = new HashMap<String, Object>();
                environment.put("Event", event);
                environment.put("Player", event.getPlayer());
                environment.put("Message", message);
                call(environment);
            }
        };
        return true;
    }

    public void unregister() {
        if (listener != null) PlayerInteractEvent.getHandlerList().unregister(listener);
        listener = null;
    }
}
