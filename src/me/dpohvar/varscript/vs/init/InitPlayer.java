package me.dpohvar.varscript.vs.init;


import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.utils.reflect.ReflectBukkitUtils;
import me.dpohvar.varscript.utils.reflect.ReflectUtils;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitPlayer {
    public static void load() {
        VSCompiler.addRule(new SimpleCompileRule(
                "ALLOWFLY",
                "ALLOWFLY AFLY",
                "Player",
                "Boolean",
                "player",
                "Returns true if player can fly",
                new SimpleWorker(new int[]{0x5F, 0x80}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getAllowFlight());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETALLOWFLY",
                "SETALLOWFLY >AFLY",
                "Player Boolean",
                "Player",
                "player",
                "Set allow flight for player",
                new SimpleWorker(new int[]{0x5F, 0x81}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setAllowFlight(b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CANSEE",
                "CANSEE SEE",
                "Player(A) Player(B)",
                "Boolean",
                "player",
                "Returns true if Player A can see Player B",
                new SimpleWorker(new int[]{0x5F, 0x82}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p2 = v.pop(Player.class);
                        Player p = v.pop(Player.class);
                        v.push(p.canSee(p2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HIDE",
                "HIDE",
                "Player(A) Player(B)",
                "Player",
                "player",
                "Hide Player B for Player A",
                new SimpleWorker(new int[]{0x5F, 0x83}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p2 = v.pop(Player.class);
                        Player p = v.peek(Player.class);
                        p.hidePlayer(p2);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HIDEFORALL",
                "HIDEFORALL HIDEA",
                "Player",
                "Player",
                "player",
                "Hide Player for all other players",
                new SimpleWorker(new int[]{0x5F, 0x84}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player h = v.peek(Player.class);
                        for (Player p : Bukkit.getOnlinePlayers()) p.hidePlayer(h);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SHOW",
                "SHOW",
                "Player(A) Player(B)",
                "Player",
                "player",
                "Show Player B for Player A",
                new SimpleWorker(new int[]{0x5F, 0x85}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p2 = v.pop(Player.class);
                        Player p = v.peek(Player.class);
                        p.showPlayer(p2);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SAY",
                "SAY",
                "Player String",
                "Player",
                "player",
                "Send chat message from player",
                new SimpleWorker(new int[]{0x5F, 0x86}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.chat(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "BEDSPAWN",
                "BEDSPAWN BED",
                "Player",
                "Location",
                "player offline",
                "Get player bed spawn location",
                new SimpleWorker(new int[]{0x5F, 0x87}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.getBedSpawnLocation());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBEDSPAWN",
                "SETBEDSPAWN SETBED >BED",
                "Player Location",
                "Player",
                "player",
                "Set bed spawn location for player",
                new SimpleWorker(new int[]{0x5F, 0x88}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.setBedSpawnLocation(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "COMPASSTARGET",
                "COMPASSTARGET COMP",
                "Player",
                "Location",
                "player",
                "Get player's compass target",
                new SimpleWorker(new int[]{0x5F, 0x89}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getCompassTarget());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCOMPASSTARGET",
                "SETCOMPASSTARGET SETCOMP >COMP",
                "Player Location",
                "Player",
                "player",
                "Set player's compass target",
                new SimpleWorker(new int[]{0x5F, 0x8A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.setCompassTarget(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "NAME",
                "NAME",
                "Player",
                "String",
                "player offline",
                "Get player name",
                new SimpleWorker(new int[]{0x5F, 0x8B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.getName());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LASTPLAYED",
                "LASTPLAYED",
                "Player",
                "Long",
                "player offline",
                "Gets the last date and time that this player was witnessed on server",
                new SimpleWorker(new int[]{0x5F, 0x8C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.getLastPlayed());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LASTPLAYEDDATE",
                "LASTPLAYEDDATE",
                "Player",
                "Date",
                "player offline",
                "Gets the last date that this player was witnessed on server",
                new SimpleWorker(new int[]{0x5F, 0x8D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(new Date(p.getLastPlayed()));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYEDBEFORE",
                "PLAYEDBEFORE",
                "Player",
                "Boolean",
                "player offline",
                "Checks if this player has played on this server before",
                new SimpleWorker(new int[]{0x5F, 0x8E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.hasPlayedBefore());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISBANNED",
                "BANNED ISBANNED",
                "Player",
                "Boolean",
                "player offline",
                "Checks if this player is banned or not",
                new SimpleWorker(new int[]{0x5F, 0x8F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.isBanned());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ONLINE",
                "ONLINE",
                "Player",
                "Boolean",
                "player offline",
                "Checks if this player is currently online",
                new SimpleWorker(new int[]{0x5F, 0x90}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.isOnline());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WHITELISTED",
                "WHITELISTED",
                "Player",
                "Boolean",
                "player offline",
                "Checks if this player is whitelisted or not",
                new SimpleWorker(new int[]{0x5F, 0x91}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.isWhitelisted());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "BAN",
                "BAN",
                "Player",
                "Player",
                "player offline",
                "Ban player",
                new SimpleWorker(new int[]{0x5F, 0x92}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.peek(OfflinePlayer.class);
                        p.setBanned(true);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "UNBAN",
                "UNBAN",
                "Player",
                "Player",
                "player offline",
                "UnBan player",
                new SimpleWorker(new int[]{0x5F, 0x93}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.peek(OfflinePlayer.class);
                        p.setBanned(false);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WHITELISTADD",
                "WHITELISTADD WLADD",
                "Player",
                "Player",
                "player offline",
                "Add player to whitelist",
                new SimpleWorker(new int[]{0x5F, 0x94}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.peek(OfflinePlayer.class);
                        p.setWhitelisted(true);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WHITELISTREMOVE",
                "WHITELISTREMOVE WLREM",
                "Player",
                "Player",
                "player offline",
                "Remove player from whitelist",
                new SimpleWorker(new int[]{0x5F, 0x95}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.peek(OfflinePlayer.class);
                        p.setWhitelisted(false);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "OPPED",
                "ISOP OPPED",
                "Player",
                "Boolean",
                "player offline",
                "Returns true if player is op",
                new SimpleWorker(new int[]{0x5F, 0x96}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.isOp());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "OP",
                "OP >OP",
                "Player",
                "Player",
                "player offline",
                "OP player",
                new SimpleWorker(new int[]{0x5F, 0x97}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.peek(OfflinePlayer.class);
                        p.setOp(true);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "DEOP",
                "DEOP",
                "Player",
                "Player",
                "player offline",
                "DEOP player",
                new SimpleWorker(new int[]{0x5F, 0x98}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.peek(OfflinePlayer.class);
                        p.setOp(false);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "IP",
                "IP",
                "Player",
                "Player",
                "player",
                "Get player's IP",
                new SimpleWorker(new int[]{0x5F, 0x99}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getAddress().getAddress());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "DISPLAYNAME",
                "DISPLAYNAME DNAME",
                "Player",
                "String",
                "player",
                "Get player's display name",
                new SimpleWorker(new int[]{0x5F, 0x9A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getDisplayName());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EXHAUSTION",
                "EXHAUSTION EXH",
                "Player",
                "Float",
                "player",
                "Get player's exhaustion level",
                new SimpleWorker(new int[]{0x5F, 0x9B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getExhaustion());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "XP",
                "XP",
                "Player",
                "Float",
                "player",
                "Get player's experience points",
                new SimpleWorker(new int[]{0x5F, 0x9C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getExp());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FLYSPEED",
                "FLYSPEED FSPD",
                "Player",
                "Float",
                "player",
                "Get player's fly speed",
                new SimpleWorker(new int[]{0x5F, 0x9D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getFlySpeed());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FOOD",
                "FOOD",
                "Player",
                "Float",
                "player",
                "Get player's food lvl",
                new SimpleWorker(new int[]{0x5F, 0x9E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getFoodLevel());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LEVEL",
                "LEVEL LVL",
                "Player",
                "Integer",
                "player",
                "Get player's experience level",
                new SimpleWorker(new int[]{0x5F, 0x9F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getLevel());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "TABNAME",
                "TABNAME TNAME",
                "Player",
                "String",
                "player",
                "Gets the name that is shown on the player list",
                new SimpleWorker(new int[]{0x5F, 0xA0}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getPlayerListName());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERTIME",
                "PLAYERTIME PTIME",
                "Player",
                "Long",
                "player",
                "Returns the player's current timestamp.",
                new SimpleWorker(new int[]{0x5F, 0xA1}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getPlayerTime());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERTIMEOFFSET",
                "PLAYERTIMEOFFSET PTIMEF",
                "Player",
                "Long",
                "player",
                "Returns the player's current time offset relative to server time, or the current player's fixed time if the player's time is absolute",
                new SimpleWorker(new int[]{0x5F, 0xA2}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getPlayerTimeOffset());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SATURATION",
                "SATURATION SAT",
                "Player",
                "String",
                "player",
                "Get player's saturation level",
                new SimpleWorker(new int[]{0x5F, 0xA3}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getSaturation());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SCOREBOARD",
                "SCOREBOARD SCB",
                "Player",
                "ScoreBoard",
                "scoreboard",
                "Get player's scoreboard",
                new SimpleWorker(new int[]{0x5F, 0xA4}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getScoreboard());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WALKSPEED",
                "WALKSPEED WSPD",
                "Player",
                "Float",
                "player",
                "Get player's walk speed",
                new SimpleWorker(new int[]{0x5F, 0xA5}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getWalkSpeed());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "XPADD",
                "XPADD XP+",
                "Player Integer",
                "Player",
                "player",
                "Give exp",
                new SimpleWorker(new int[]{0x5F, 0xA6}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Player p = v.peek(Player.class);
                        p.giveExp(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "XPLEVELADD",
                "XPLEVELADD XPLADD XPL+",
                "Player Integer",
                "Player",
                "player",
                "Give exp level",
                new SimpleWorker(new int[]{0x5F, 0xA7}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Player p = v.peek(Player.class);
                        p.giveExpLevels(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISFLYING",
                "ISFLYING FLY",
                "Player",
                "Boolean",
                "player",
                "Returns true if player fly",
                new SimpleWorker(new int[]{0x5F, 0xA8}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.isFlying());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ONGROUND",
                "ONGROUND GND",
                "Entity",
                "Boolean",
                "entity",
                "Returns true if entity on ground",
                new SimpleWorker(new int[]{0x5F, 0xA9}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity p = v.pop(Entity.class);
                        v.push(p.isOnGround());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISSCALEDHEALTH",
                "ISSCALEDHEALTH SCHP",
                "Player",
                "Boolean",
                "player",
                "Gets if the client is displayed a 'scaled' health, that is, health on a scale from 0-20.",
                new SimpleWorker(new int[]{0x5F, 0xAA}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.isScaledHealth());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SLEEPIGNORED",
                "SLEEPIGNORED",
                "Player",
                "Boolean",
                "player",
                "Returns whether the player is sleeping ignored.",
                new SimpleWorker(new int[]{0x5F, 0xAB}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.isSleepingIgnored());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SNEAK",
                "SNEAK SNK",
                "Player",
                "Boolean",
                "player",
                "Returns true if player in sneak mode",
                new SimpleWorker(new int[]{0x5F, 0xAC}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.isSneaking());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SPRINTING",
                "SPRINTING SPRT",
                "Player",
                "Boolean",
                "player",
                "Returns true if player in sprint mode",
                new SimpleWorker(new int[]{0x5F, 0xAD}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.isSprinting());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "KICK",
                "KICK",
                "Player String(Reason)",
                "Player",
                "player",
                "Kick player",
                new SimpleWorker(new int[]{0x5F, 0xAE}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.kickPlayer(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "COMMAND",
                "COMMAND",
                "Player String(Command)",
                "Player",
                "player",
                "Makes the player perform the given command",
                new SimpleWorker(new int[]{0x5F, 0xAF}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.performCommand(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SENDEFFECT",
                "SENDEFFECT SEF",
                "Player Location #Effect",
                "Player",
                "player effect",
                "Play effect for player",
                new SimpleWorker(new int[]{0x5F, 0xB0}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Effect ef = v.pop(Effect.values());
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.playEffect(l, ef, 0);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SENDCEFFECT",
                "SENDCEFFECT SCEF",
                "Player Location #Effect Integer(data)",
                "Player",
                "player effect",
                "Play custom effect for player",
                new SimpleWorker(new int[]{0x5F, 0xB1}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer data = v.pop(Integer.class);
                        Effect ef = v.pop(Effect.values());
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.playEffect(l, ef, data);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYNOTE",
                "PLAYNOTE PLNOTE",
                "Player Location #Instrument Integer(note)",
                "Player",
                "player",
                "Play note for player",
                new SimpleWorker(new int[]{0x5F, 0xB2}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Note note = new Note(v.pop(Integer.class));
                        Instrument ins = v.pop(Instrument.values());
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.playNote(l, ins, note);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SENDSOUND",
                "SENDSOUND SSND",
                "Player Location #Sound Float(volume) Float(pitch)",
                "Player",
                "player sound",
                "Play sound for player",
                new SimpleWorker(new int[]{0x5F, 0xB3}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float pitch = v.pop(Float.class);
                        Float volume = v.pop(Float.class);
                        Sound s = v.pop(Sound.values());
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.playSound(l, s, volume, pitch);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "RESETPLAYERTIME",
                "RESETPLAYERTIME RSTPTIME",
                "Player",
                "Player",
                "player",
                "Reset player's time",
                new SimpleWorker(new int[]{0x5F, 0xB4}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.peek(Player.class);
                        p.resetPlayerTime();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "RESETPLAYERWEATHER",
                "RESETPLAYERWEATHER RSTPWTR",
                "Player",
                "Player",
                "player",
                "Reset player's weather",
                new SimpleWorker(new int[]{0x5F, 0xB5}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.peek(Player.class);
                        p.resetPlayerWeather();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SENDBLOCK",
                "SENDBLOCK SBL",
                "Player Block ItemStack",
                "Player",
                "player",
                "Send a block change",
                new SimpleWorker(new int[]{0x5F, 0xB6}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack item = v.pop(ItemStack.class);
                        Block l = v.pop(Block.class);
                        Player p = v.peek(Player.class);
                        p.sendBlockChange(l.getLocation(), item.getTypeId(), item.getData().getData());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "MESSAGE",
                "MESSAGE MSG",
                "Player String(message)",
                "Player",
                "player",
                "Send message to player",
                new SimpleWorker(new int[]{0x5F, 0xB7}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.sendRawMessage(s);
                    }
                }
        ));

        // here: insert command 0x5F,0xB8

        VSCompiler.addRule(new SimpleCompileRule(
                "SETDISPLAYNAME",
                "SETDISPLAYNAME >DNAME",
                "Player String(name)",
                "Player",
                "player",
                "Set display name for player",
                new SimpleWorker(new int[]{0x5F, 0xB9}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.setDisplayName(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETEXHAUSTION",
                "SETEXHAUSTION >EXH",
                "Player Float",
                "Player",
                "player",
                "Set Exhaustion lvl",
                new SimpleWorker(new int[]{0x5F, 0xBA}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class);
                        Player p = v.peek(Player.class);
                        p.setExhaustion(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETXP",
                "SETXP >XP",
                "Player Float(level)",
                "Player",
                "player",
                "Set experience level",
                new SimpleWorker(new int[]{0x5F, 0xBB}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class);
                        Player p = v.peek(Player.class);
                        p.setExp(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFLYING",
                "SETFLYING >FLY",
                "Player Boolean(start/stop)",
                "Player",
                "player",
                "Makes this player start or stop flying",
                new SimpleWorker(new int[]{0x5F, 0xBC}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setFlying(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFLYSPEED",
                "SETFLYSPEED >FLYSPEED >FSPD",
                "Player Float",
                "Player",
                "player",
                "Set fly speed",
                new SimpleWorker(new int[]{0x5F, 0xBD}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class);
                        Player p = v.peek(Player.class);
                        p.setFlySpeed(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFOOD",
                "SETFOOD >FOOD",
                "Player Integer",
                "Player",
                "player",
                "Set food level for player",
                new SimpleWorker(new int[]{0x5F, 0xBE}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer s = v.pop(Integer.class);
                        Player p = v.peek(Player.class);
                        p.setFoodLevel(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETLEVEL",
                "SETLEVEL SETLVL >LVL",
                "Player Integer",
                "Player",
                "player",
                "Set exp level for player",
                new SimpleWorker(new int[]{0x5F, 0xBF}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer s = v.pop(Integer.class);
                        Player p = v.peek(Player.class);
                        p.setLevel(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETTABNAME",
                "SETTABNAME SETTNAME >TNAME",
                "Player String",
                "Player",
                "player",
                "Set list name for player",
                new SimpleWorker(new int[]{0x5F, 0xC0}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.setPlayerListName(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPLAYERTIME",
                "SETPLAYERTIME SETPTIME >PTIME",
                "Player Long Boolean",
                "Player",
                "player",
                "Set time for player",
                new SimpleWorker(new int[]{0x5F, 0xC1}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s2 = v.pop(Boolean.class);
                        Long s = v.pop(Long.class);
                        Player p = v.peek(Player.class);
                        p.setPlayerTime(s, s2);
                    }
                }
        ));
        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "SETPLAYERWEATHER",
                    "SETPLAYERWEATHER SETPWTR >PWTR",
                    "Player #WeatherType",
                    "Player",
                    "player",
                    "Set weather for player",
                    new SimpleWorker(new int[]{0x5F, 0xC2}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            WeatherType s = v.pop(WeatherType.values());
                            Player p = v.peek(Player.class);
                            p.setPlayerWeather(s);
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        VSCompiler.addRule(new SimpleCompileRule(
                "SETSATURATION",
                "SETSATURATION >SAT",
                "Player Float",
                "Player",
                "player",
                "Set saturation level for player",
                new SimpleWorker(new int[]{0x5F, 0xC3}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class);
                        Player p = v.peek(Player.class);
                        p.setSaturation(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSCALEHEALTH",
                "SETSCALEHEALTH SETSCHP SCHP",
                "Player Boolean",
                "Player",
                "player",
                "Sets if the client is displayed a 'scaled' health, that is, health on a scale from 0-20",
                new SimpleWorker(new int[]{0x5F, 0xC4}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setScaleHealth(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSLEEPIGNORED",
                "SETSLEEPIGNORED",
                "Player Boolean",
                "Player",
                "player",
                "Sets whether the player is ignored as not sleeping",
                new SimpleWorker(new int[]{0x5F, 0xC5}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setSleepingIgnored(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSNEAK",
                "SETSNEAK SETSNK >SNK",
                "Player Boolean",
                "Player",
                "player",
                "Sets the sneak mode the player",
                new SimpleWorker(new int[]{0x5F, 0xC6}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setSneaking(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSPRINTING",
                "SETSPRINTING SETSPRT >SPRT",
                "Player Boolean",
                "Player",
                "player",
                "Sets whether the player is sprinting or not",
                new SimpleWorker(new int[]{0x5F, 0xC7}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setSneaking(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETTEXTUREPACK",
                "SETTEXTUREPACK",
                "Player String(URL)",
                "Player",
                "player",
                "Request that the player's client download and switch texture packs",
                new SimpleWorker(new int[]{0x5F, 0xC8}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.setTexturePack(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FIRSTPLAYED",
                "FIRSTPLAYED",
                "Player",
                "Long",
                "player",
                "Gets the first date and time that this player was witnessed on server",
                new SimpleWorker(new int[]{0x5F, 0xC9}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.getFirstPlayed());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FIRSTPLAYEDDATE",
                "FIRSTPLAYEDDATE",
                "Player",
                "Date",
                "player",
                "Gets the first date that this player was witnessed on server",
                new SimpleWorker(new int[]{0x5F, 0xCA}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(new Date(p.getFirstPlayed()));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERWEATHER",
                "PLAYERWEATHER PWTR",
                "Player",
                "String",
                "player",
                "Get player's weather type",
                new SimpleWorker(new int[]{0x5F, 0xCB}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getPlayerWeather());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETTOTALEXP",
                "SETTOTALEXP SETTXP >TXP",
                "Player Integer",
                "Player",
                "player",
                "Sets the players current experience level",
                new SimpleWorker(new int[]{0x5F, 0xCC}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer s = v.pop(Integer.class);
                        Player p = v.peek(Player.class);
                        p.setTotalExperience(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETWALKSPEED",
                "SETWALKSPEED >WALKSPEED >WSPD",
                "Player Float",
                "Player",
                "player",
                "Sets the speed at which a client will walk",
                new SimpleWorker(new int[]{0x5F, 0xCD}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class);
                        Player p = v.peek(Player.class);
                        p.setWalkSpeed(s);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "RESPAWN",
                "RESPAWN RSP",
                "Player",
                "Player",
                "player",
                "Respawn player. Works only if player has 0 hp",
                new SimpleWorker(new int[]{0x5F, 0xCE}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.peek(Player.class);
                        try {
                            Object ph = ReflectUtils.callMethod(p, "getHandle", null);
                            Class classPacket = ReflectBukkitUtils.getMinecraftClass("Packet205ClientCommand");
                            Object packet = ReflectUtils.callConstructor(classPacket, null);
                            ReflectUtils.setField(packet, "a", 1);
                            Object connection = ReflectUtils.getField(ph, "playerConnection");
                            ReflectUtils.callMethod(connection, "a", new Class[]{classPacket}, packet);
                        } catch (Exception ignored) {
                        }
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETSCOREBOARD",
                "SETSCOREBOARD SETSCB >SCB",
                "Player Scoreboard",
                "Player",
                "scoreboard",
                "Set player's scoreboard",
                new SimpleWorker(new int[]{0x5F, 0xCF}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Scoreboard sc = v.pop(Scoreboard.class);
                        v.peek(Player.class)
                                .setScoreboard(sc);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NEWSCOREBOARD",
                "NEWSCOREBOARD NSCB",
                "",
                "Scoreboard",
                "scoreboard",
                "Create new scoreboard",
                new SimpleWorker(new int[]{0x5F, 0xD0}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                Bukkit.getScoreboardManager().getNewScoreboard()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "MAINSCOREBOARD",
                "MAINSCOREBOARD MSCB",
                "",
                "Scoreboard",
                "scoreboard",
                "Get main server scoreboard",
                new SimpleWorker(new int[]{0x5F, 0xD1}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                Bukkit.getScoreboardManager().getMainScoreboard()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERSCORES",
                "PLAYERSCORES",
                "Scoreboard OfflinePlayer",
                "Set(Score)",
                "scoreboard",
                "Get all player scores",
                new SimpleWorker(new int[]{0x5F, 0xD2}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(
                                v.pop(Scoreboard.class).getScores(p)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SCBPLAYERS",
                "SCBPLAYERS",
                "Scoreboard",
                "Set(OfflinePlayer)",
                "scoreboard",
                "Get all players in scoreboard",
                new SimpleWorker(new int[]{0x5F, 0xD3}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Scoreboard.class).getPlayers()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TEAMS",
                "TEAMS",
                "Scoreboard",
                "Set(Team)",
                "scoreboard",
                "Get all teams in scoreboard",
                new SimpleWorker(new int[]{0x5F, 0xD4}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Scoreboard.class).getTeams()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TEAM",
                "TEAM",
                "Scoreboard String(team)",
                "Team",
                "scoreboard",
                "Get scoreboard team",
                new SimpleWorker(new int[]{0x5F, 0xD5}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String team = v.pop(String.class);
                        v.push(
                                v.pop(Scoreboard.class).getTeam(team)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "OBJECTIVES",
                "OBJECTIVES",
                "Scoreboard",
                "Set(Objectives)",
                "scoreboard",
                "Get all objectives of scoreboard",
                new SimpleWorker(new int[]{0x5F, 0xD6}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Scoreboard.class).getObjectives()
                        );
                    }
                }
        ));

        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "OBJECTIVESLOT",
                    "OBJECTIVESLOT",
                    "Scoreboard #DisplaySlot",
                    "Objective",
                    "scoreboard",
                    "get objective in special slot",
                    new SimpleWorker(new int[]{0x5F, 0xD7}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            DisplaySlot slot = v.pop(DisplaySlot.values());
                            v.push(
                                    v.pop(Scoreboard.class).getObjective(slot)
                            );
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "OBJECTIVE",
                    "OBJECTIVE",
                    "Scoreboard String(name)",
                    "Objective",
                    "scoreboard",
                    "get objective by name",
                    new SimpleWorker(new int[]{0x5F, 0xD8}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            String name = v.pop(String.class);
                            v.push(
                                    v.pop(Scoreboard.class).getObjective(name)
                            );
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        VSCompiler.addRule(new SimpleCompileRule(
                "OBJECTIVECRIT",
                "OBJECTIVECRIT",
                "Scoreboard String(criteria)",
                "Objective",
                "scoreboard",
                "get objective by criteria",
                new SimpleWorker(new int[]{0x5F, 0xD9}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String name = v.pop(String.class);
                        v.push(
                                v.pop(Scoreboard.class).getObjectivesByCriteria(name)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERTEAM",
                "PLAYERTEAM",
                "Scoreboard OfflinePlayer",
                "Team",
                "scoreboard",
                "get player's team",
                new SimpleWorker(new int[]{0x5F, 0xDA}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(
                                v.pop(Scoreboard.class).getPlayerTeam(p)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "RESETPLAYERSCORES",
                "RESETPLAYERSCORES",
                "Scoreboard OfflinePlayer",
                "Scoreboard",
                "scoreboard",
                "reset player's scores",
                new SimpleWorker(new int[]{0x5F, 0xDB}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.peek(Scoreboard.class)
                                .resetScores(p);
                    }
                }
        ));

        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "CLEARSLOT",
                    "CLEARSLOT",
                    "Scoreboard #DisplaySlot",
                    "Scoreboard",
                    "scoreboard",
                    "clear display slot",
                    new SimpleWorker(new int[]{0x5F, 0xDC}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            DisplaySlot s = v.pop(DisplaySlot.values());
                            v.peek(Scoreboard.class).clearSlot(s);
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        VSCompiler.addRule(new SimpleCompileRule(
                "REGTEAM",
                "REGTEAM",
                "Scoreboard String(team)",
                "Scoreboard",
                "scoreboard",
                "register new team for scoreboard",
                new SimpleWorker(new int[]{0x5F, 0xDD}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String val = v.pop(String.class);
                        v.peek(Scoreboard.class).registerNewTeam(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REGOBJECTIVE",
                "REGOBJECTIVE",
                "Scoreboard String(name) String(criteria)",
                "Scoreboard",
                "scoreboard",
                "register new objective for scoreboard",
                new SimpleWorker(new int[]{0x5F, 0xDE}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String cr = v.pop(String.class);
                        String name = v.pop(String.class);
                        v.peek(Scoreboard.class).registerNewObjective(name, cr);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ADDTOTEAM",
                "ADDTOTEAM TEAM+",
                "Team OfflinePlayer",
                "Team",
                "scoreboard",
                "add player to team",
                new SimpleWorker(new int[]{0x5F, 0xDF}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer val = v.pop(OfflinePlayer.class);
                        v.peek(Team.class).addPlayer(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REMFROMTEAM",
                "REMFROMTEAM TEAM-",
                "Team OfflinePlayer",
                "Team",
                "scoreboard",
                "remove player from team",
                new SimpleWorker(new int[]{0x5F, 0xE0}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer val = v.pop(OfflinePlayer.class);
                        v.peek(Team.class).removePlayer(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "REMOVETEAM",
                "REMOVETEAM RMTEAM",
                "Team",
                "",
                "scoreboard",
                "unregister team",
                new SimpleWorker(new int[]{0x5F, 0xE1}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.pop(Team.class).unregister();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FRIENDLYFIRE",
                "FRIENDLYFIRE FFIRE",
                "Team",
                "Boolean",
                "scoreboard",
                "get friendlyfire state",
                new SimpleWorker(new int[]{0x5F, 0xE2}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Team.class).allowFriendlyFire()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TEAMNAME",
                "TEAMNAME",
                "Team",
                "String",
                "scoreboard",
                "get team name",
                new SimpleWorker(new int[]{0x5F, 0xE3}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Team.class).getName()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FRIENDLYSEE",
                "FRIENDLYSEE FSEE",
                "Team",
                "Boolean",
                "scoreboard",
                "Gets the team's ability to see invisible teammates",
                new SimpleWorker(new int[]{0x5F, 0xE4}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Team.class).canSeeFriendlyInvisibles()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TEAMDISPLAYNAME",
                "TEAMDISPLAYNAME TEAMDNAME",
                "Team",
                "String",
                "scoreboard",
                "Gets the name displayed to players for this team",
                new SimpleWorker(new int[]{0x5F, 0xE5}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Team.class).getDisplayName()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TEAMPLAYERS",
                "TEAMPLAYERS",
                "Team",
                "Set(OfflinePlayer)",
                "scoreboard",
                "Gets the Set of players on the team",
                new SimpleWorker(new int[]{0x5F, 0xE6}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Team.class).getPlayers()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TEAMPREFIX",
                "TEAMPREFIX",
                "Team",
                "String",
                "scoreboard",
                "Gets the prefix prepended to the display of players on this team.",
                new SimpleWorker(new int[]{0x5F, 0xE7}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Team.class).getPrefix()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TEAMSUFFIX",
                "TEAMSUFFIX",
                "Team",
                "String",
                "scoreboard",
                "Gets the suffix appended to the display of players on this team",
                new SimpleWorker(new int[]{0x5F, 0xE8}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Team.class).getSuffix()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TEAMSIZE",
                "TEAMSIZE",
                "Team",
                "Integer",
                "scoreboard",
                "get team size",
                new SimpleWorker(new int[]{0x5F, 0xE9}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Team.class).getSize()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETFRIENDLYFIRE",
                "SETFRIENDLYFIRE SETFFIRE >FFIRE",
                "Team Boolean",
                "Team",
                "scoreboard",
                "Sets the team friendly fire state",
                new SimpleWorker(new int[]{0x5F, 0xEA}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean val = v.pop(Boolean.class);
                        v.peek(Team.class).setAllowFriendlyFire(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETFRIENDLYSEE",
                "SETFRIENDLYSEE SETFSEE >FSEE",
                "Team Boolean",
                "Team",
                "scoreboard",
                "sets the team's ability to see invisible invisible teammates",
                new SimpleWorker(new int[]{0x5F, 0xEB}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean val = v.pop(Boolean.class);
                        v.peek(Team.class).setCanSeeFriendlyInvisibles(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETTEAMDISPLAYNAME",
                "SETTEAMDISPLAYNAME SETTEAMDNAME >TEAMDNAME",
                "Team String(name)",
                "Team",
                "scoreboard",
                "Sets the name displayed to players for this team",
                new SimpleWorker(new int[]{0x5F, 0xEC}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String val = v.pop(String.class);
                        v.peek(Team.class).setDisplayName(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETTEAMPREFIX",
                "SETTEAMPREFIX >TEAMPREFIX",
                "Team String(prefix)",
                "Team",
                "scoreboard",
                "Sets the prefix prepended to the display of players on this team",
                new SimpleWorker(new int[]{0x5F, 0xED}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String val = v.pop(String.class);
                        v.peek(Team.class).setPrefix(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETTEAMSUFFIX",
                "SETTEAMSUFFIX >TEAMSUFFIX",
                "Team String(suffix)",
                "Team",
                "scoreboard",
                "Sets the suffix appended to the display of players on this team",
                new SimpleWorker(new int[]{0x5F, 0xEE}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String val = v.pop(String.class);
                        v.peek(Team.class).setSuffix(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "HASPLAYER",
                "HASPLAYER",
                "Team OfflinePlayer",
                "Boolean",
                "scoreboard",
                "Checks to see if the specified player is a member of team",
                new SimpleWorker(new int[]{0x5F, 0xEF}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer val = v.pop(OfflinePlayer.class);
                        v.push(
                                v.pop(Team.class)
                                        .hasPlayer(val)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CRITERIA",
                "CRITERIA",
                "Objective",
                "String",
                "scoreboard",
                "Gets the criteria objective tracks",
                new SimpleWorker(new int[]{0x5F, 0xF0}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Objective.class)
                                        .getCriteria()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "OBJDISPLAYNAME",
                "OBJDISPLAYNAME OBJDNAME",
                "Objective",
                "String",
                "scoreboard",
                "Gets the name displayed to players for objective",
                new SimpleWorker(new int[]{0x5F, 0xF1}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Objective.class)
                                        .getDisplayName()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "OBJSLOT",
                "OBJSLOT",
                "Objective",
                "#DisplaySlot",
                "scoreboard",
                "Gets the display slot objective is displayed at",
                new SimpleWorker(new int[]{0x5F, 0xF2}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Objective.class)
                                        .getDisplaySlot()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "OBJNAME",
                "OBJNAME",
                "Objective",
                "String(name)",
                "scoreboard",
                "get name of objective",
                new SimpleWorker(new int[]{0x5F, 0xF3}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Objective.class)
                                        .getName()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "OBJMODIFIABLE",
                "OBJMODIFIABLE",
                "Objective",
                "Boolean",
                "scoreboard",
                "gets if the objective's scores can be modified directly",
                new SimpleWorker(new int[]{0x5F, 0xF4}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Objective.class)
                                        .isModifiable()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETOBJDISPLAYNAME",
                "SETOBJDISPLAYNAME >OBJDNAME",
                "Objective String(name)",
                "Objective",
                "scoreboard",
                "Sets the name displayed to players for this objective",
                new SimpleWorker(new int[]{0x5F, 0xF5}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String val = v.pop(String.class);
                        v.peek(Objective.class).setDisplayName(val);
                    }
                }
        ));

        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "SETOBJSLOT",
                    "SETOBJSLOT >OBJSLOT",
                    "Objective #DisplaySlot",
                    "Objective",
                    "scoreboard",
                    "sets this objective to display on the specified slot for the scoreboard, removing it from any other display slot",
                    new SimpleWorker(new int[]{0x5F, 0xF6}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            DisplaySlot val = v.pop(DisplaySlot.values());
                            v.peek(Objective.class).setDisplaySlot(val);
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        VSCompiler.addRule(new SimpleCompileRule(
                "REMOVEOBJ",
                "REMOVEOBJ",
                "Objective",
                "",
                "scoreboard",
                "Unregister this objective",
                new SimpleWorker(new int[]{0x5F, 0xF7}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.pop(Objective.class).unregister();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SCORE",
                "SCORE",
                "Score",
                "Integer",
                "scoreboard",
                "Get score value",
                new SimpleWorker(new int[]{0x5F, 0xF8}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Score.class).getScore()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETSCORE",
                "SETSCORE >SCORE",
                "Score Integer",
                "Score",
                "scoreboard",
                "Set score value",
                new SimpleWorker(new int[]{0x5F, 0xF9}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        int val = v.pop(Integer.class);
                        v.peek(Score.class).setScore(val);
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "OBJSCORE",
                "OBJSCORE OBJSCORE",
                "Objective OfflinePlayer",
                "Score",
                "scoreboard",
                "Get score of offline player in objective",
                new SimpleWorker(new int[]{0x5F, 0xFA}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer val = v.pop(OfflinePlayer.class);
                        v.push(
                                v.pop(Objective.class).getScore(val)
                        );
                    }
                }
        ));


    }
}
