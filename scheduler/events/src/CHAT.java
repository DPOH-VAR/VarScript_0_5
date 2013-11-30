import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.scheduler.Event;
import me.dpohvar.varscript.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.HashMap;

@SuppressWarnings("unused deprecation")
public class CHAT extends Event {

    private Listener listener;

    public CHAT(Task task, String type, String params, CommandSender sender) {
        super(task, type, params);
    }

    public boolean register() {
        assert listener == null;
        listener = new Listener() {
            @EventHandler(priority = EventPriority.NORMAL)
            public void onChat(PlayerChatEvent event) {
                String message = event.getMessage();

                HashMap<String, Object> environment = new HashMap<String, Object>();
                environment.put("Event", event);
                environment.put("Player", event.getPlayer());
                environment.put("Message", message);

                if (getParams() != null) {
                    String template = task.parseObject(getParams(), environment).toString();
                    if (!message.equals(template)) return;
                }

                call(environment);
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, VarScript.instance);
        return true;
    }

    @SuppressWarnings("deprecation")
    public void unregister() {
        if (listener != null) PlayerChatEvent.getHandlerList().unregister(listener);
        listener = null;
    }

    public static String help() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("format: CHAT\n");
        buffer.append("format: CHAT {$message}\n");
        buffer.append("called on player chat\n");
        buffer.append("optional arguments: message to compare\n");
        buffer.append("Environment:\n");
        buffer.append("$Event - PlayerChatEvent\n");
        buffer.append("$Player - player\n");
        buffer.append("$Message - sent message");
        return buffer.toString();
    }

    public static String description() {
        return "called on player chat";
    }
}
