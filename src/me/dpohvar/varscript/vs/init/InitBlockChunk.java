package me.dpohvar.varscript.vs.init;

import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
                "Block",
                "ItemStack",
                "block item",
                "get itemstack of block",
                new SimpleWorker(new int[]{0x90}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Block b = v.pop(Block.class);
                        v.push(new ItemStack(b.getType(),1,b.getData()));
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
                "Block Biome",
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
                "UPDATESTATE",
                "BlockState",
                "BlockState",
                "block",
                "update block state",
                new SimpleWorker(new int[]{0x96}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        BlockState b = v.peek(BlockState.class);
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
                "BLOCKFACE BLF",
                "Block(A) Block(B)",
                "BlockFace",
                "block",
                "get blockface B to A",
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
                "get x location of chunk",
                new SimpleWorker(new int[]{0x9F,0x19}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(Chunk.class)
                                        .getZ()
                        );
                    }
                }
        ));

        // собрать все блокстейты
    }
}
