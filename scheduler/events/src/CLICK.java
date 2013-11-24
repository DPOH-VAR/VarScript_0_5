import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.task.Event;
import me.dpohvar.varscript.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class CLICK extends Event {

    private Listener listener;
    private String message;
    private String worldName;
    private int x;
    private int y;
    private int z;

    public CLICK(Task task, String type, String params, CommandSender sender) {
        super(task, type);
        if (sender instanceof Player && params == null) {
            Player player = (Player) sender;
            Block b = player.getTargetBlock(null, 16);
            x = b.getX();
            y = b.getY();
            z = b.getZ();
            worldName = b.getWorld().getName();
            params = x + " " + y + " " + z + " " + worldName;
            player.sendMessage("block type: " + b.getType() + " parameters: " + params);
        } else {
            String[] args = params.split(" ");
            if (args.length != 4) throw new RuntimeException("wrong block format! use X Y Z World");
            worldName = args[3];
            x = Integer.parseInt(args[0]);
            y = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);
        }
        setParams(params);
    }

    public static String parse(String params, CommandSender sender) {
        if (sender instanceof Player && params == null) {
            Player player = (Player) sender;
            Block b = player.getTargetBlock(null, 16);
            params = b.getX() + " " + b.getY() + " " + b.getZ() + " " + b.getWorld().getName();
            player.sendMessage("block type: " + b.getType() + " parameters: " + params);
            return params;
        } else {
            return params;
        }
    }

    public boolean register() {
        assert listener == null;
        World world = Bukkit.getWorld(worldName);
        if (world == null) return false;
        final Block block = world.getBlockAt(x, y, z);
        listener = new Listener() {
            @SuppressWarnings("deprecation")
            @EventHandler(priority = EventPriority.NORMAL)
            public void onClick(PlayerInteractEvent event) {

                Block clicked = event.getClickedBlock();

                if (!block.equals(clicked)) return;

                HashMap<String, Object> environment = new HashMap<String, Object>();
                environment.put("Player", event.getPlayer());
                environment.put("Block", clicked);
                environment.put("Action", event.getAction());
                call(environment);
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, VarScript.instance);
        return true;
    }

    public void unregister() {
        if (listener != null) PlayerInteractEvent.getHandlerList().unregister(listener);
        listener = null;
    }
}
