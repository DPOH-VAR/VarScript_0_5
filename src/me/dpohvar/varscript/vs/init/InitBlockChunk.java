package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitBlockChunk {
    public static void load(){

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCK",
                "BLOCK BL",
                "Integer(x) Integer(y) Integer(z) World(W)",
                "Block",
                "block",
                "get block in world W at location x:y:z",
                new SimpleWorker(new int[]{0x90}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        World w = v.pop(World.class);
                        Integer z = v.pop(Integer.class);
                        Integer y = v.pop(Integer.class);
                        Integer x = v.pop(Integer.class);
                        v.push(w.getBlockAt(x,y,z));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBLOCK",
                "SETBLOCK SETBL >BL",
                "Block ItemStack",
                "Block",
                "block item",
                "change block to given itemstack",
                new SimpleWorker(new int[]{0x91}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack item = v.pop(ItemStack.class);
                        Block b = v.peek(Block.class);
                        try{
                            b.setTypeId(item.getTypeId());
                            b.setData(item.getData().getData());
                        }catch (Exception ignored){
                        }
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "BIOME",
                "BIOME BI",
                "Block",
                "Biome",
                "block",
                "get biome of block",
                new SimpleWorker(new int[]{0x92}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getBiome());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETBIOME",
                "SETBIOME SETBI >BI",
                "Block #Biome",
                "Block",
                "block",
                "set biome of block",
                new SimpleWorker(new int[]{0x93}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Biome biome = v.pop(Biome.values());
                        Block b = v.peek(Block.class);
                        b.setBiome(biome);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FAKEBLOCK",
                "FAKEBLOCK FBL",
                "Block ItemStack",
                "Block",
                "block",
                "send fake block to all players",
                new SimpleWorker(new int[]{0x94}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack item = v.pop(ItemStack.class);
                        Block b = v.peek(Block.class);
                        for(Player p: b.getWorld().getPlayers()) try{
                            double distance = p.getLocation().distance(b.getLocation());
                            if(distance > Bukkit.getViewDistance()*16) break;
                            p.sendBlockChange(b.getLocation(),item.getTypeId(),item.getData().getData());
                        }catch (Exception ignored){
                        }
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "STATE",
                "STATE",
                "Block",
                "BlockState",
                "block",
                "get block state",
                new SimpleWorker(new int[]{0x95}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getState());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "UPDATESTATE",
                "UPDATESTATE BSUPD",
                "BlockState",
                "",
                "blockstate",
                "update block state",
                new SimpleWorker(new int[]{0x96}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        BlockState b = v.pop(BlockState.class);
                        b.update();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BREAKBLOCK",
                "BREAKBLOCK BRKB",
                "Block",
                "Block",
                "block",
                "break block naturally",
                new SimpleWorker(new int[]{0x97}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.peek(Block.class);
                        b.breakNaturally();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BREAKBLOCKITEM",
                "BREAKBLOCKITEM BRKBI",
                "Block ItemStack",
                "Block",
                "block",
                "break block naturally and spawn item",
                new SimpleWorker(new int[]{0x98}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack item = v.pop(ItemStack.class);
                        Block b = v.peek(Block.class);
                        b.breakNaturally(item);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKX",
                "BLOCKX BX",
                "Block",
                "Integer",
                "block",
                "get block x",
                new SimpleWorker(new int[]{0x99}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getX());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKY",
                "BLOCKY BY",
                "Block",
                "Integer",
                "block",
                "get block y",
                new SimpleWorker(new int[]{0x9A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getY());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKZ",
                "BLOCKZ BZ",
                "Block",
                "Integer",
                "block",
                "get block z",
                new SimpleWorker(new int[]{0x9B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getZ());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKPOWER",
                "BLOCKPOWER BLP",
                "Block",
                "Integer",
                "block",
                "get block power",
                new SimpleWorker(new int[]{0x9C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getBlockPower());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKPOWERFACE",
                "BLOCKPOWERFACE BLPF",
                "Block BlockFace",
                "Integer",
                "block",
                "get power of block face",
                new SimpleWorker(new int[]{0x9D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        BlockFace face = v.pop(BlockFace.class);
                        Block b = v.pop(Block.class);
                        v.push(b.getBlockPower(face));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKREL",
                "BLOCKREL BLR",
                "Block(A) Integer(X) Integer(Y) Integer(Z)",
                "Block(B)",
                "block",
                "get block B relative to A by X,Y,Z",
                new SimpleWorker(new int[]{0x9E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer z = v.pop(Integer.class);
                        Integer y = v.pop(Integer.class);
                        Integer x = v.pop(Integer.class);
                        Block b = v.pop(Block.class);
                        v.push(b.getRelative(x,y,z));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKRELX",
                "BLOCKRELX BLRX",
                "Block(A) Integer(X)",
                "Block(B)",
                "block",
                "get block B relative to A by X",
                new SimpleWorker(new int[]{0x9F,0x00}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        Block b = v.pop(Block.class);
                        v.push(b.getRelative(val,0,0));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKRELY",
                "BLOCKRELY BLRY",
                "Block(A) Integer(Y)",
                "Block(B)",
                "block",
                "get block B relative to A by Y",
                new SimpleWorker(new int[]{0x9F,0x01}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        Block b = v.pop(Block.class);
                        v.push(b.getRelative(0,val,0));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKRELZ",
                "BLOCKRELZ BLRZ",
                "Block(A) Integer(Z)",
                "Block(B)",
                "block",
                "get block B relative to A by Z",
                new SimpleWorker(new int[]{0x9F,0x02}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        Block b = v.pop(Block.class);
                        v.push(b.getRelative(0,0,val));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKRELV",
                "BLOCKRELV BLRV",
                "Block(A) Vector(V)",
                "Block(B)",
                "block",
                "get block B relative to A by vector V",
                new SimpleWorker(new int[]{0x9F,0x03}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec = v.pop(Vector.class);
                        Block b = v.pop(Block.class);
                        v.push(b.getRelative((int)vec.getX(), (int)vec.getY(), (int)vec.getZ()));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKFACE",
                "BLOCKFACE FACE",
                "Block(A) Block(B)",
                "BlockFace",
                "block",
                "get face of block A to block B",
                new SimpleWorker(new int[]{0x9F,0x04}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        Block a = v.pop(Block.class);
                        v.push(a.getFace(b));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LIGHT",
                "LIGHT",
                "Block",
                "Byte",
                "block",
                "get light level in block",
                new SimpleWorker(new int[]{0x9F,0x05}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getLightLevel());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LIGHTBLOCKS",
                "LIGHTBLOCKS LIGHTBL",
                "Block",
                "Byte",
                "block",
                "get level of light from blocks",
                new SimpleWorker(new int[]{0x9F,0x06}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getLightFromBlocks());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LIGHTSKY",
                "LIGHTSKY",
                "Block",
                "Byte",
                "block",
                "get level of light from sky",
                new SimpleWorker(new int[]{0x9F,0x07}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getLightFromSky());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TEMPERATURE",
                "TEMPERATURE",
                "Block",
                "Double",
                "block",
                "get temperature of block",
                new SimpleWorker(new int[]{0x9F,0x08}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getTemperature());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "HUMIDITY",
                "HUMIDITY",
                "Block",
                "Double",
                "block",
                "get humidity of block",
                new SimpleWorker(new int[]{0x9F,0x09}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getHumidity());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKUP",
                "BLOCKUP BUP",
                "Block(A)",
                "Block(B)",
                "block",
                "get the block above",
                new SimpleWorker(new int[]{0x9F,0x0A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getRelative(0,1,0));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKDOWN",
                "BLOCKDOWN BDOWN",
                "Block(A)",
                "Block(B)",
                "block",
                "get the block below",
                new SimpleWorker(new int[]{0x9F,0x0B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getRelative(0,-1,0));
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TORCH",
                "TORCH RT",
                "Block",
                "Block",
                "block",
                "place redstone torch",
                new SimpleWorker(new int[]{0x9F,0x0C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Block.class)
                                .setType(Material.REDSTONE_TORCH_ON);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "AIR",
                "AIR",
                "Block",
                "Block",
                "block",
                "place air block",
                new SimpleWorker(new int[]{0x9F,0x0D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Block.class)
                                .setType(Material.AIR);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKID",
                "BLOCKID BID",
                "Block",
                "Integer",
                "block",
                "get block id",
                new SimpleWorker(new int[]{0x9F,0x0E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Block.class)
                                        .getTypeId()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKDATA",
                "BLOCKDATA BDATA",
                "Block",
                "Integer",
                "block",
                "get block data",
                new SimpleWorker(new int[]{0x9F,0x0F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Block.class)
                                        .getData()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKDROPS",
                "BLOCKDROPS BDROPS",
                "Block",
                "Collection(ItemStack)",
                "block",
                "get a list of items which would drop by destroying this block",
                new SimpleWorker(new int[]{0x9F,0x10}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Block.class)
                                        .getDrops()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKDROPSITEM",
                "BLOCKDROPSITEM BDROPSITEM",
                "Block ItemStack(tool)",
                "Collection(ItemStack)",
                "block",
                "get a list of items which would drop by destroying this block with a specific tool",
                new SimpleWorker(new int[]{0x9F,0x11}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack tool = v.pop(ItemStack.class);
                        v.push(
                                v.pop(Block.class)
                                        .getDrops(tool)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CHUNK",
                "CHUNK",
                "Block",
                "Chunk",
                "chunk",
                "get chunk",
                new SimpleWorker(new int[]{0x9F,0x12}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Block.class)
                                        .getChunk()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CHUNKSNAPSHOT",
                "CHUNKSNAPSHOT",
                "Chunk",
                "ChunkSnapshot",
                "chunk",
                "get chunk",
                new SimpleWorker(new int[]{0x9F,0x13}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Chunk.class)
                                        .getChunkSnapshot()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "TILES",
                "TILES",
                "Chunk",
                "List(BlockState)",
                "chunk",
                "get all tile entities in chunk",
                new SimpleWorker(new int[]{0x9F,0x14}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.convert(List.class,
                                        v.pop(Chunk.class)
                                                .getTileEntities()
                                )
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CHUNKLOADED",
                "CHUNKLOADED CHLOADED",
                "Chunk",
                "Boolean",
                "chunk",
                "put TRUE if chunk is loaded",
                new SimpleWorker(new int[]{0x9F,0x15}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Chunk.class)
                                        .isLoaded()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LOADCHUNK",
                "LOADCHUNK LOADCH",
                "Chunk",
                "Chunk",
                "chunk",
                "load chunk",
                new SimpleWorker(new int[]{0x9F,0x16}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Chunk.class)
                                .load();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "UNLOADCHUNK",
                "UNLOADCHUNK UNLOADCH",
                "Chunk",
                "Chunk",
                "chunk",
                "load chunk",
                new SimpleWorker(new int[]{0x9F,0x17}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Chunk.class)
                                .unload();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CHUNKX",
                "CHUNKX CHX",
                "Chunk",
                "Integer",
                "chunk",
                "get x location of chunk",
                new SimpleWorker(new int[]{0x9F,0x18}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Chunk.class)
                                        .getX()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CHUNKZ",
                "CHUNKZ CHZ",
                "Chunk",
                "Integer",
                "chunk",
                "get z location of chunk",
                new SimpleWorker(new int[]{0x9F,0x19}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Chunk.class)
                                        .getZ()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SID",
                "SID",
                "BlockState",
                "Integer",
                "blockstate",
                "get block ID of blockstate",
                new SimpleWorker(new int[]{0x9F,0x1A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        BlockState state = v.pop(BlockState.class);
                        v.push(
                                state.getTypeId()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SDATA",
                "SDATA",
                "BlockState",
                "Byte",
                "blockstate",
                "get block data of blockstate",
                new SimpleWorker(new int[]{0x9F,0x1B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        BlockState state = v.pop(BlockState.class);
                        v.push(
                                state.getData().getData()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETSID",
                "SETSID >SID",
                "BlockState Integer",
                "BlockState",
                "blockstate",
                "set block ID of blockstate",
                new SimpleWorker(new int[]{0x9F,0x1C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        v.peek(BlockState.class).setTypeId(val);

                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETSDATA",
                "SETSDATA >SDATA",
                "BlockState Byte",
                "BlockState",
                "blockstate",
                "set block data of blockstate",
                new SimpleWorker(new int[]{0x9F,0x1D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Byte val = v.pop(Byte.class);
                        v.peek(BlockState.class).setRawData(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BREWTIME",
                "BREWTIME",
                "BrewingStand",
                "Integer",
                "blockstate",
                "get brewing time",
                new SimpleWorker(new int[]{0x9F,0x1E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        BrewingStand state = v.pop(BrewingStand.class);
                        v.push(
                                state.getBrewingTime()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETBREWTIME",
                "SETBREWTIME >BREWTIME",
                "BrewingStand Integer",
                "BrewingStand",
                "blockstate",
                "set brewing time of BrewingStand",
                new SimpleWorker(new int[]{0x9F,0x1F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        BrewingStand state = v.pop(BrewingStand.class);
                        state.setBrewingTime(val);
                        v.push(state);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "CHESTINV",
                "CHESTINV",
                "Chest",
                "Inventory",
                "blockstate",
                "get single chest inventory",
                new SimpleWorker(new int[]{0x9F,0x20}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(Chest.class).getBlockInventory());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLCOMMAND",
                "BLCOMMAND BLCMD",
                "CommandBlock",
                "String(command)",
                "blockstate",
                "get command stored in command block",
                new SimpleWorker(new int[]{0x9F,0x21}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(CommandBlock.class).getCommand());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETBLCOMMAND",
                "SETBLCOMMAND SETBLCMD >BLCMD",
                "CommandBlock String(command)",
                "CommandBlock",
                "blockstate",
                "set command in command block",
                new SimpleWorker(new int[]{0x9F,0x22}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String val = v.pop(String.class);
                        v.peek(CommandBlock.class).setCommand(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLNAME",
                "BLNAME",
                "CommandBlock",
                "String(name)",
                "blockstate",
                "get command block name",
                new SimpleWorker(new int[]{0x9F,0x23}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(CommandBlock.class).getName());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETBLNAME",
                "SETBLNAME >BLNAME",
                "CommandBlock String(name)",
                "CommandBlock",
                "blockstate",
                "set name of command block",
                new SimpleWorker(new int[]{0x9F,0x24}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String val = v.pop(String.class);
                        v.peek(CommandBlock.class).setName(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLCREATURE",
                "BLCREATURE",
                "CreatureSpawner",
                "String(name)",
                "blockstate",
                "get creature type name of mob spawner",
                new SimpleWorker(new int[]{0x9F,0x25}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(CreatureSpawner.class).getCreatureTypeName());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLSPAWN",
                "BLSPAWN",
                "CreatureSpawner",
                "#EntityType",
                "blockstate",
                "get spawned type of mob spawner",
                new SimpleWorker(new int[]{0x9F,0x26}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(CreatureSpawner.class)
                                        .getSpawnedType()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLDELAY",
                "BLDELAY",
                "CreatureSpawner",
                "Integer",
                "blockstate",
                "get delay (in ticks) of mob spawner",
                new SimpleWorker(new int[]{0x9F,0x27}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(CreatureSpawner.class)
                                        .getDelay()
                        );
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "SETBLDELAY",
                "SETBLDELAY >BLDELAY",
                "CreatureSpawner Integer",
                "CreatureSpawner",
                "blockstate",
                "set delay (in ticks) of mob spawner",
                new SimpleWorker(new int[]{0x9F,0x28}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        CreatureSpawner spawner = v.pop(CreatureSpawner.class);
                        spawner.setDelay(val);
                        v.push(spawner);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETBLCREATURE",
                "SETBLCREATURE >BLCREATURE",
                "CreatureSpawner String(creature)",
                "CreatureSpawner",
                "blockstate",
                "set creature type of mob spawner",
                new SimpleWorker(new int[]{0x9F,0x29}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String val = v.pop(String.class);
                        CreatureSpawner spawner = v.pop(CreatureSpawner.class);
                        spawner.setCreatureTypeByName(val);
                        v.push(spawner);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETBLSPAWN",
                "SETBLSPAWN >BLSPAWN",
                "CreatureSpawner #EntityType",
                "CreatureSpawner",
                "blockstate",
                "set entity type of mob spawner",
                new SimpleWorker(new int[]{0x9F,0x2A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        EntityType val = v.pop(EntityType.values());
                        CreatureSpawner spawner = v.pop(CreatureSpawner.class);
                        spawner.setSpawnedType(val);
                        v.push(spawner);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "DISPENSE",
                "DISPENSE DISP",
                "Dispenser",
                "Dispenser",
                "blockstate",
                "set entity type of mob spawner",
                new SimpleWorker(new int[]{0x9F,0x2B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Dispenser.class).dispense();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETBLOCKID",
                "SETBLOCKID SETBID >BID",
                "Block Integer",
                "Block",
                "block",
                "change block id",
                new SimpleWorker(new int[]{0x9F,0x2C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        v.peek(Block.class).setTypeId(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETBLOCKDATA",
                "SETBLOCKDATA SETBDATA >BDATA",
                "Block Byte",
                "Block",
                "block",
                "change block data",
                new SimpleWorker(new int[]{0x9F,0x2D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Byte val = v.pop(Byte.class);
                        v.peek(Block.class).setData(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETBLOCKIDDATA",
                "SETBLOCKIDDATA SETBIDDATA >BIDDATA",
                "Block Integer(id) Byte(data)",
                "Block",
                "block",
                "change block id and data",
                new SimpleWorker(new int[]{0x9F,0x2E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Byte data = v.pop(Byte.class);
                        Integer id = v.pop(Integer.class);
                        v.peek(Block.class).setTypeIdAndData(id,data,false);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLOCKIDDATA",
                "BLOCKIDDATA BIDDATA",
                "Block",
                "Integer(id) Byte(data)",
                "block",
                "get block id and data",
                new SimpleWorker(new int[]{0x9F,0x2F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(b.getTypeId());
                        v.push(b.getData());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "FAKEBLOCKIDDATA",
                "FAKEBLOCKIDDATA FBLIDDATA FB2",
                "Block Integer(id) Integer(data)",
                "Block",
                "block",
                "send fake block to all players by id and data",
                new SimpleWorker(new int[]{0x9F,0x30}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Byte data = v.pop(Byte.class);
                        Integer id = v.pop(Integer.class);
                        Block b = v.peek(Block.class);
                        for(Player p: b.getWorld().getPlayers()) try{
                            double distance = p.getLocation().distance(b.getLocation());
                            if(distance > Bukkit.getViewDistance()*16) break;
                            p.sendBlockChange(b.getLocation(),id,data);
                        }catch (Exception ignored){
                        }
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BLDROP",
                "BLDROP",
                "Dropper",
                "Dropper",
                "blockstate",
                "drop item from dropper",
                new SimpleWorker(new int[]{0x9F,0x31}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Dropper.class).drop();
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "BURNTIME",
                "BURNTIME",
                "Furnace",
                "Short",
                "blockstate",
                "get burn time of furnace",
                new SimpleWorker(new int[]{0x9F,0x32}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Furnace.class).getBurnTime()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "COOKTIME",
                "COOKTIME",
                "Furnace",
                "Short",
                "blockstate",
                "get cook time of furnace",
                new SimpleWorker(new int[]{0x9F,0x33}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Furnace.class).getCookTime()
                        );
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBURNTIME",
                "SETBURNTIME >BURNTIME",
                "Furnace Short",
                "Furnace",
                "blockstate",
                "get burn time of furnace",
                new SimpleWorker(new int[]{0x9F,0x34}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Short time = v.pop(Short.class);
                        Furnace bs = v.pop(Furnace.class);
                        bs.setBurnTime(time);
                        v.push(bs);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCOOKTIME",
                "SETCOOKTIME >COOKTIME",
                "Furnace Short",
                "Furnace",
                "blockstate",
                "get cook time of furnace",
                new SimpleWorker(new int[]{0x9F,0x35}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Short time = v.pop(Short.class);
                        Furnace bs = v.pop(Furnace.class);
                        bs.setBurnTime(time);
                        v.push(bs);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "JEJECT",
                "JEJECT",
                "Jukebox",
                "Jukebox",
                "blockstate",
                "eject item in jukebox",
                new SimpleWorker(new int[]{0x9F,0x36}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.peek(Jukebox.class).eject();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "JPLAY",
                "JPLAY",
                "Jukebox ItemStack(disc)",
                "Jukebox",
                "blockstate",
                "play disc in jukebox",
                new SimpleWorker(new int[]{0x9F,0x37}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack val = v.pop(ItemStack.class);
                        Jukebox bs = v.pop(Jukebox.class);
                        bs.setPlaying(val.getType());
                        v.push(bs);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "JISPLAY",
                "JISPLAY",
                "Jukebox",
                "Boolean",
                "blockstate",
                "Put true, if jukebox is playing disc, false otherwise",
                new SimpleWorker(new int[]{0x9F,0x38}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Jukebox bs = v.pop(Jukebox.class);
                        v.push(bs.isPlaying());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NOTE",
                "NOTE",
                "NoteBlock",
                "Byte",
                "blockstate",
                "get note id of note block",
                new SimpleWorker(new int[]{0x9F,0x39}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        NoteBlock bs = v.pop(NoteBlock.class);
                        v.push(bs.getRawNote());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "BSPLAY",
                "BSPLAY",
                "NoteBlock",
                "NoteBlock",
                "blockstate",
                "play note block",
                new SimpleWorker(new int[]{0x9F,0x3A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        NoteBlock bs = v.pop(NoteBlock.class);
                        bs.play();
                        v.push(bs);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "BSPLAYNOTE",
                "BSPLAYNOTE",
                "NoteBlock Integer(note) #Instrument",
                "NoteBlock",
                "blockstate",
                "play custom note in block",
                new SimpleWorker(new int[]{0x9F,0x3B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Instrument val = v.pop(Instrument.class);
                        Byte lvl = v.pop(Byte.class);
                        NoteBlock bs = v.pop(NoteBlock.class);
                        bs.play(val,new Note(lvl));
                        v.push(bs);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETNOTE",
                "SETNOTE >NOTE",
                "NoteBlock Byte(note)",
                "NoteBlock",
                "blockstate",
                "set note id of note block",
                new SimpleWorker(new int[]{0x9F,0x3C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Byte val = v.pop(Byte.class);
                        NoteBlock bs = v.pop(NoteBlock.class);
                        bs.setRawNote(val);
                        v.push(bs);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LINES",
                "LINES",
                "Sign",
                "List(String)",
                "blockstate sign",
                "get all lines in sign",
                new SimpleWorker(new int[]{0x9F,0x3D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                Arrays.asList(
                                        v.pop(Sign.class).getLines()
                                )
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "LINE",
                "LINE",
                "Sign Integer(N)",
                "String",
                "blockstate sign",
                "get N line in sign",
                new SimpleWorker(new int[]{0x9F,0x3E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer val = v.pop(Integer.class);
                        v.push(
                                Arrays.asList(
                                        v.pop(Sign.class).getLine(val)
                                )
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETLINE",
                "SETLINE >LINE",
                "Sign String(value) Integer(N)",
                "Sign",
                "blockstate sign",
                "write value to N line in sign",
                new SimpleWorker(new int[]{0x9F,0x3F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer n = v.pop(Integer.class);
                        String val = v.pop(String.class);
                        Sign sign = v.pop(Sign.class);
                        sign.setLine(n,val);
                        v.push(sign);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SKULLOWNER",
                "SKULLOWNER",
                "Skull",
                "String",
                "blockstate",
                "get owner of skull block",
                new SimpleWorker(new int[]{0x9F,0x40}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Skull.class).getOwner()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SKULLTYPE",
                "SKULLTYPE",
                "Skull",
                "#SkullType",
                "blockstate",
                "get type of skull\n(SKELETON, WITHER, ZOMBIE ,PLAYER, CREEPER..)",
                new SimpleWorker(new int[]{0x9F,0x41}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Skull.class).getSkullType()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SKULLROT",
                "SKULLROT",
                "Skull",
                "#BlockFace",
                "blockstate",
                "get rotation of skull",
                new SimpleWorker(new int[]{0x9F,0x42}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Skull.class).getRotation()
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETSKULLOWNER",
                "SETSKULLOWNER >SKULLOWNER",
                "Skull String(owner)",
                "Skull",
                "blockstate",
                "set owner of skull",
                new SimpleWorker(new int[]{0x9F,0x43}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String val = v.pop(String.class);
                        Skull bs = v.pop(Skull.class);
                        bs.setOwner(val);
                        v.push(bs);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETSKULLTYPE",
                "SETSKULLTYPE >SKULLTYPE",
                "Skull #SkullType",
                "Skull",
                "blockstate",
                "set type of skull",
                new SimpleWorker(new int[]{0x9F,0x44}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        SkullType val = v.pop(SkullType.values());
                        Skull bs = v.pop(Skull.class);
                        bs.setSkullType(val);
                        v.push(bs);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETSKULLROT",
                "SETSKULLROT >SKULLROT",
                "Skull #BlockFace",
                "Skull",
                "blockstate",
                "set rotation of skull",
                new SimpleWorker(new int[]{0x9F,0x45}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        BlockFace val = v.pop(BlockFace.values());
                        Skull bs = v.pop(Skull.class);
                        bs.setRotation(val);
                        v.push(bs);
                    }
                }
        ));
    }

}
