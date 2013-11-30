import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.scheduler.Event;
import me.dpohvar.varscript.scheduler.Task;
import me.dpohvar.varscript.scheduler.TaskParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused deprecation")
public class CLICK extends Event {

    private Listener listener;
    private String worldName;
    private int x;
    private int y;
    private int z;
    private Material blockType = null;
    private byte data = -1;
    private String action;

    public CLICK(Task task, String type, String constructor, CommandSender sender) {
        super(task, type);
        LinkedHashMap<String, String> params = TaskParser.parseMap(constructor);
        boolean needGetTargetBlock = sender instanceof Player
                && !params.containsKey("x")
                && !params.containsKey("y")
                && !params.containsKey("z")
                && !params.containsKey("world");
        if (needGetTargetBlock) {
            Player player = (Player) sender;
            Block b = player.getTargetBlock(null, 16);
            x = b.getX();
            y = b.getY();
            z = b.getZ();
            worldName = b.getWorld().getName();
            if (params.containsKey("type")) {
                String t = params.get("type");
                if (t != null && t.matches("[0-9]+")) blockType = Material.getMaterial(Integer.parseInt(t));
                else blockType = Converter.convert(Material.values(), t);
            }
            if (params.containsKey("data")) {
                if (params.get("data") == null) data = b.getData();
                else data = (byte) Integer.parseInt(params.get("data"));
            }
            action = params.get("action");
        } else {
            x = Integer.parseInt(params.get("x"));
            y = Integer.parseInt(params.get("y"));
            z = Integer.parseInt(params.get("z"));
            worldName = task.parseString(params.get("world"));
            action = params.get("action");
            if (params.containsKey("type")) {
                String t = params.get("type");
                if (t != null && t.matches("[0-9]+")) blockType = Material.getMaterial(Integer.parseInt(t));
                else blockType = Converter.convert(Material.values(), t);
            }

            if (params.containsKey("data")) data = (byte) Integer.parseInt(params.get("data"));
        }
        String saving = "x=" + x + " y=" + y + " z=" + z + " world=" + worldName;
        if (blockType != null) saving += " type=" + blockType.name();
        if (action != null) saving += " action=" + action;
        if (data >= 0) saving += " data=" + data;
        if (needGetTargetBlock) sender.sendMessage("<selected> " + saving);
        setParams(saving);
    }

    public void callIt(Map<String, Object> environment) {
        call(environment);
    }

    public boolean register() {
        assert listener == null;
        World world = Bukkit.getWorld(worldName);
        if (world == null) return false;
        final Action a;
        if (action == null) {
            a = null;
        } else {
            if (action.equals("left")) a = Action.LEFT_CLICK_BLOCK;
            else if (action.equals("right")) a = Action.RIGHT_CLICK_BLOCK;
            else if (action.equals("physical")) a = Action.PHYSICAL;
            else if (action.equals("any")) a = null;
            else return false;
        }
        final Block block = world.getBlockAt(x, y, z);

        listener = new Listener() {
            @EventHandler(priority = EventPriority.NORMAL)
            public void onClick(PlayerInteractEvent event) {

                Block clicked = event.getClickedBlock();
                if (!block.equals(clicked)) return;
                if (blockType != null && !clicked.getType().equals(blockType)) return;
                if (data >= 0 && clicked.getData() != data) return;
                if (a != null && !a.equals(event.getAction())) return;

                HashMap<String, Object> environment = new HashMap<String, Object>();
                environment.put("Event", event);
                environment.put("Player", event.getPlayer());
                environment.put("Block", clicked);
                environment.put("Action", event.getAction());
                environment.put("BlockId", clicked.getTypeId());
                environment.put("BlockData", clicked.getData());
                callIt(environment);
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, VarScript.instance);
        return true;
    }

    public void unregister() {
        if (listener != null) PlayerInteractEvent.getHandlerList().unregister(listener);
        listener = null;
    }

    public static String help() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("format: CLICK x={x} y={y} z={z} world={world} [type={material}] [data={data}]\n");
        buffer.append("format for player: CLICK [type | type={material}] [data | data={data}]\n");
        buffer.append("called when player clicks to block at x,y,z,world\n");
        buffer.append("optional arguments: type and data\n");
        buffer.append("Environment:\n");
        buffer.append("$Event - PlayerInteractEvent\n");
        buffer.append("$Player - player who clicked\n");
        buffer.append("$Block - clicked block\n");
        buffer.append("$Action - action (LEFT_CLICK_BLOCK, RIGHT_CLICK_AIR, PHYSICAL, etc)\n");
        buffer.append("$BlockId - block id\n");
        buffer.append("$BlockData - block data");
        return buffer.toString();
    }

    public static String description() {
        return "called on click to block";
    }

}
