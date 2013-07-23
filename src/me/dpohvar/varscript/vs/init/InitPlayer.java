package me.dpohvar.varscript.vs.init;


import me.dpohvar.varscript.utils.reflect.ReflectBukkitUtils;
import me.dpohvar.varscript.utils.reflect.ReflectUtils;
import me.dpohvar.varscript.vs.*;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.converter.ConvertException;
import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.Packet205ClientCommand;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitPlayer {
    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "ALLOWFLIGHT",
                "ALLOWFLIGHT",
                "Player",
                "Boolean",
                "player",
                "Returns true if player can fly",
                new SimpleWorker(new int[]{0x5F,0x27}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getAllowFlight());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETALLOWFLIGHT",
                "SETALLOWFLIGHT >ALLOWFLIGHT",
                "Player Boolean",
                "Player",
                "player",
                "Set allow flight for palyer",
                new SimpleWorker(new int[]{0x5F,0x28}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setAllowFlight(b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CANSEE",
                "CANSEE",
                "Player Player2",
                "Boolean",
                "player",
                "Returns true if Player can see Player2",
                new SimpleWorker(new int[]{0x5F,0x29}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p2 = v.pop(Player.class);
                        Player p = v.pop(Player.class);
                        v.push(p.canSee(p2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HIDE",
                "HIDE",
                "Player Player2",
                "Player",
                "player",
                "Hide Player2 for Player",
                new SimpleWorker(new int[]{0x5F,0x2A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p2 = v.pop(Player.class);
                        Player p = v.peek(Player.class);
                        p.hidePlayer(p2);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SHOW",
                "SHOW",
                "Player Player2",
                "Player",
                "player",
                "Show Player2 for Player",
                new SimpleWorker(new int[]{0x5F,0x2B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                "Chat from player",
                new SimpleWorker(new int[]{0x5F,0x2C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.chat(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "BEDSPAWN",
                "BEDSPAWN",
                "Player",
                "Location",
                "player offline",
                "Get player bed spawn location",
                new SimpleWorker(new int[]{0x5F,0x2D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.getBedSpawnLocation());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBEDSPAWN",
                "SETBEDSPAWN",
                "Player Location",
                "Player",
                "player",
                "Set bed spawn location for player",
                new SimpleWorker(new int[]{0x5F,0x2E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.setBedSpawnLocation(l);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "COMPASSTARGET",
                "COMPASSTARGET COMTARGET",
                "Player",
                "Location",
                "player",
                "Get player's compass target",
                new SimpleWorker(new int[]{0x5F,0x2F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getCompassTarget());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCOMPASSTARGET",
                "SETCOMPASSTARGET SETCOMTARGET >COMTARGET",
                "Player Location",
                "Player",
                "player",
                "Set player's compass target",
                new SimpleWorker(new int[]{0x5F,0x30}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x31}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x32}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.getLastPlayed());
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
                new SimpleWorker(new int[]{0x5F,0x33}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x34}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x35}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x36}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x37}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x38}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.peek(OfflinePlayer.class);
                        p.setBanned(false);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WHITELISTADD",
                "WHITELISTADD",
                "Player",
                "Player",
                "player offline",
                "Add player to whitelist",
                new SimpleWorker(new int[]{0x5F,0x39}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.peek(OfflinePlayer.class);
                        p.setWhitelisted(true);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WHITELISTREMOVE",
                "WHITELISTREMOVE WHITELISTREM",
                "Player",
                "Player",
                "player offline",
                "Remove player from whitelist",
                new SimpleWorker(new int[]{0x5F,0x3A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.peek(OfflinePlayer.class);
                        p.setWhitelisted(false);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISOP",
                "ISOP OPPED",
                "Player",
                "Boolean",
                "player offline",
                "Returns true if player is op",
                new SimpleWorker(new int[]{0x5F,0x3B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.isOp());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "OP",
                "OP",
                "Player",
                "Player",
                "player offline",
                "OP player",
                new SimpleWorker(new int[]{0x5F,0x3C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x3D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x3E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getAddress().getAddress());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "DISPLAYNAME",
                "DISPLAYNAME",
                "Player",
                "String",
                "player",
                "Get player's display name",
                new SimpleWorker(new int[]{0x5F,0x3F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getDisplayName());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EXHAUSTION",
                "EXHAUSTION",
                "Player",
                "Float",
                "player",
                "Get player's exhaustion level",
                new SimpleWorker(new int[]{0x5F,0x40}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x41}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x42}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x43}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x44}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getLevel());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "TABNAME",
                "TABNAME",
                "Player",
                "String",
                "player",
                "Gets the name that is shown on the player list",
                new SimpleWorker(new int[]{0x5F,0x45}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getPlayerListName());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERTIME",
                "PLAYERTIME",
                "Player",
                "Long",
                "player",
                "Returns the player's current timestamp.",
                new SimpleWorker(new int[]{0x5F,0x46}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getPlayerTime());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERTIMEOFFSET",
                "PLAYERTIMEOFFSET",
                "Player",
                "Long",
                "player",
                "Returns the player's current time offset relative to server time, or the current player's fixed time if the player's time is absolute",
                new SimpleWorker(new int[]{0x5F,0x47}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getPlayerTimeOffset());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SATURATION",
                "SATURATION",
                "Player",
                "String",
                "player",
                "Get player's saturation level",
                new SimpleWorker(new int[]{0x5F,0x48}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getSaturation());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SCOREBOARD",
                "SCOREBOARD",
                "Player",
                "ScoreBoard",
                "player",
                "Get player's saturation level",
                new SimpleWorker(new int[]{0x5F,0x49}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getScoreboard());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WALKSPEED",
                "WALKSPEED",
                "Player",
                "Float",
                "player",
                "Get player's walk speed",
                new SimpleWorker(new int[]{0x5F,0x4A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.getWalkSpeed());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "GIVEEXP",
                "GIVEEXP GIVEXP",
                "Player Integer",
                "Player",
                "player",
                "Give exp",
                new SimpleWorker(new int[]{0x5F,0x4B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Player p = v.peek(Player.class);
                        p.giveExp(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "GIVEEXPLEVEL",
                "GIVEEXPLEVEL GIVEEXPLVL GIVEXPLVL",
                "Player Integer",
                "Player",
                "player",
                "Give exp level",
                new SimpleWorker(new int[]{0x5F,0x4C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Player p = v.peek(Player.class);
                        p.giveExpLevels(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISFLYING",
                "ISFLY ISFLYING",
                "Player",
                "Boolean",
                "player",
                "Returns true if player fly",
                new SimpleWorker(new int[]{0x5F,0x4D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.isFlying());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ONGROUND",
                "ONGROUND",
                "Entity",
                "Boolean",
                "entity",
                "Returns true if entity on ground",
                new SimpleWorker(new int[]{0x5F,0x4E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity p = v.pop(Entity.class);
                        v.push(p.isOnGround());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISSCALEDHEALTH",
                "ISSCALEDHEALTH",
                "Player",
                "Boolean",
                "player",
                "Gets if the client is displayed a 'scaled' health, that is, health on a scale from 0-20.",
                new SimpleWorker(new int[]{0x5F,0x4F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x50}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.isSleepingIgnored());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SNEAKING",
                "SNEAKING",
                "Player",
                "Boolean",
                "player",
                "Returns true if player in sneak mode",
                new SimpleWorker(new int[]{0x5F,0x51}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.pop(Player.class);
                        v.push(p.isSneaking());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SPRINTING",
                "SPRINTING",
                "Player",
                "Boolean",
                "player",
                "Returns true if player in sprint mode",
                new SimpleWorker(new int[]{0x5F,0x52}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x53}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x54}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.performCommand(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EFFECT",
                "EFFECT",
                "Player Location Effect",
                "Player",
                "player",
                "Play effect for player",
                new SimpleWorker(new int[]{0x5F,0x55}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Effect ef = v.pop(Effect.values());
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.playEffect(l,ef,0);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "XEFFECT",
                "XEFFECT",
                "Player Location Effect Integer(data)",
                "Player",
                "player",
                "Play effect for player",
                new SimpleWorker(new int[]{0x5F,0x56}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer data = v.pop(Integer.class);
                        Effect ef = v.pop(Effect.values());
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.playEffect(l,ef,data);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYNOTE",
                "PLAYNOTE NOTE",
                "Player Location Instrument Note",
                "Player",
                "player",
                "Play note for player",
                new SimpleWorker(new int[]{0x5F,0x57}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Note note = v.pop(Note.class);
                        Instrument ins = v.pop(Instrument.values());
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.playNote(l,ins,note);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYSOUND",
                "PLAYSOUND SOUND",
                "Player Location Sound Float(volume) Float(pitch)",
                "Player",
                "player",
                "Play sound for player",
                new SimpleWorker(new int[]{0x5F,0x58}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float pitch = v.pop(Float.class);
                        Float volume = v.pop(Float.class);
                        Sound s = v.pop(Sound.values());
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.playSound(l,s,volume,pitch);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "RESETPLAYERTIME",
                "RESETPLAYERTIME",
                "Player",
                "Player",
                "player",
                "Reset player's time",
                new SimpleWorker(new int[]{0x5F,0x59}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.peek(Player.class);
                        p.resetPlayerTime();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "RESETPLAYERWEATHER",
                "RESETPLAYERWEATHER",
                "Player",
                "Player",
                "player",
                "Reset player's weather",
                new SimpleWorker(new int[]{0x5F,0x5A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.peek(Player.class);
                        p.resetPlayerWeather();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FALEBLOCK",
                "FBLOCK",
                "Player Location Integer(material) Byte(data)",
                "Player",
                "player",
                "Send a block change",
                new SimpleWorker(new int[]{0x5F,0x5B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Byte data = v.pop(Byte.class);
                        Integer mat = v.pop(Integer.class);
                        Location l = v.pop(Location.class);
                        Player p = v.peek(Player.class);
                        p.sendBlockChange(l,mat,data);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "MESSAGE",
                "MESSAGE MSG",
                "Player String",
                "Player",
                "player",
                "Send msg to player",
                new SimpleWorker(new int[]{0x5F,0x5C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class);
                        Player p = v.peek(Player.class);
                        p.sendRawMessage(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETALLOWFLY",
                "SETALLOWFLY",
                "Player Boolean",
                "Player",
                "player",
                "Sets if the Player is allowed to fly via jump key double-tap like in creative mode.",
                new SimpleWorker(new int[]{0x5F,0x5D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setAllowFlight(b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETDISPLAYNAME",
                "SETDISPLAYNAME",
                "Player String",
                "Player",
                "player",
                "Set displayname",
                new SimpleWorker(new int[]{0x5F,0x5E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class) ;
                        Player p = v.peek(Player.class);
                        p.setDisplayName(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETEXHAUSTION",
                "SETEXHAUSTION",
                "Player Float",
                "Player",
                "player",
                "Set Exhaustion lvl",
                new SimpleWorker(new int[]{0x5F,0x5F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class) ;
                        Player p = v.peek(Player.class);
                        p.setExhaustion(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETEXP",
                "SETEXP SETXP >XP >EXP",
                "Player Float",
                "Player",
                "player",
                "Set experience lvl",
                new SimpleWorker(new int[]{0x5F,0x60}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class) ;
                        Player p = v.peek(Player.class);
                        p.setExp(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFLYING",
                "SETFLYING",
                "Player Boolean",
                "Player",
                "player",
                "Makes this player start or stop flying",
                new SimpleWorker(new int[]{0x5F,0x61}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s = v.pop(Boolean.class) ;
                        Player p = v.peek(Player.class);
                        p.setFlying(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFLYSPEED",
                "SETFLYSPEED >FLYSPEED FSPD",
                "Player Float",
                "Player",
                "player",
                "Set fly speed",
                new SimpleWorker(new int[]{0x5F,0x62}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class) ;
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
                "Set fly speed",
                new SimpleWorker(new int[]{0x5F,0x63}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer s = v.pop(Integer.class) ;
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
                new SimpleWorker(new int[]{0x5F,0x64}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer s = v.pop(Integer.class) ;
                        Player p = v.peek(Player.class);
                        p.setLevel(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETTABNAME",
                "SETTABNAME >TABNAME",
                "Player String",
                "Player",
                "player",
                "Set list name for player",
                new SimpleWorker(new int[]{0x5F,0x65}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String s = v.pop(String.class) ;
                        Player p = v.peek(Player.class);
                        p.setPlayerListName(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPLAYERTIME",
                "SETPLAYERTIME",
                "Player Long Boolean",
                "Player",
                "player",
                "Set time for palyer",
                new SimpleWorker(new int[]{0x5F,0x66}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s2 = v.pop(Boolean.class);
                        Long s = v.pop(Long.class) ;
                        Player p = v.peek(Player.class);
                        p.setPlayerTime(s,s2);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPLAYERWEATHER",
                "SETPLAYERWEATHER",
                "Player WeatherType",
                "Player",
                "player",
                "Set weather for palyer",
                new SimpleWorker(new int[]{0x5F,0x67}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        WeatherType s = v.pop(WeatherType.values());
                        Player p = v.peek(Player.class);
                        p.setPlayerWeather(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSATURATION",
                "SETSATURATION",
                "Player Float",
                "Player",
                "player",
                "Set saturation level for palyer",
                new SimpleWorker(new int[]{0x5F,0x68}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class);
                        Player p = v.peek(Player.class);
                        p.setSaturation(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSCALEHEALTH",
                "SETSCALEHEALTH",
                "Player Boolean",
                "Player",
                "player",
                "Sets if the client is displayed a 'scaled' health, that is, health on a scale from 0-20",
                new SimpleWorker(new int[]{0x5F,0x69}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x6A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setSleepingIgnored(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSNEAKING",
                "SETSNEAKING SETSNEAK >SNEAK",
                "Player Boolean",
                "Player",
                "player",
                "Sets the sneak mode the player",
                new SimpleWorker(new int[]{0x5F,0x6B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean s = v.pop(Boolean.class);
                        Player p = v.peek(Player.class);
                        p.setSneaking(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSPRINTING",
                "SETSPRINTING SETSPRINT >SPRINT",
                "Player Boolean",
                "Player",
                "player",
                "Sets whether the player is sprinting or not",
                new SimpleWorker(new int[]{0x5F,0x6C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x6D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                "Get ",
                "Gets the first date and time that this player was witnessed on server",
                new SimpleWorker(new int[]{0x5F,0x6E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        OfflinePlayer p = v.pop(OfflinePlayer.class);
                        v.push(p.getFirstPlayed());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERWEATHER",
                "PLAYERWEATHER",
                "Player",
                "String",
                "player",
                "Get player's weather type",
                new SimpleWorker(new int[]{0x5F,0x6F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x70}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x71}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float s = v.pop(Float.class);
                        Player p = v.peek(Player.class);
                        p.setWalkSpeed(s);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "EQUIPMENT",
                "EQUIPMENT EQP",
                "LivingEntity",
                "Equipment",
                "entity equipment",
                "Get equipment",
                new SimpleWorker(new int[]{0x5F,0x72}){
                    @Override public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(LivingEntity.class).getEquipment());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "RESPAWN",
                "RESPAWN RSP",
                "Player",
                "Player",
                "player",
                "Respawn player. Works only with died players",
                new SimpleWorker(new int[]{0x5F,0x73}){
                    @Override public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Player p = v.peek(Player.class);
                        try{
                            Object ph = ReflectUtils.callMethod(p,"getHandle",null);
                            Class classPacket = ReflectBukkitUtils.getMinecraftClass("Packet205ClientCommand");
                            Object packet = ReflectUtils.callConstructor(classPacket,null);
                            ReflectUtils.setField(packet,"a",1);
                            Object connection = ReflectUtils.getField(ph,"playerConnection");
                            ReflectUtils.callMethod(connection,"a",new Class[]{classPacket},packet);
                        } catch (Exception ignored){
                        }
                    }
                }
        ));













    }
}
