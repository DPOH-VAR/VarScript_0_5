package org.dpohvar.varscript.vs.init;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.dpohvar.varscript.converter.ConvertException;
import org.dpohvar.varscript.vs.Context;
import org.dpohvar.varscript.vs.SimpleWorker;
import org.dpohvar.varscript.vs.Thread;
import org.dpohvar.varscript.vs.ThreadRunner;
import org.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import org.dpohvar.varscript.vs.compiler.VSCompiler;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitCustomEntity {
    public static void load() {


        /** AGEABLE */


        VSCompiler.addRule(new SimpleCompileRule(
                "CANBREED",
                "CANBREED",
                "Ageable",
                "Boolean",
                "ageable",
                "Returns true if entity can breed",
                new SimpleWorker(new int[]{0x80}) {
                    @Override
                    public void run(ThreadRunner r, org.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Ageable a = v.pop(Ageable.class);
                        v.push(a.canBreed());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "AGE",
                "AGE",
                "Ageable",
                "Integer",
                "ageable",
                "Get age",
                new SimpleWorker(new int[]{0x81}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Ageable a = v.pop(Ageable.class);
                        v.push(a.getAge());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "AGELOCKED",
                "AGELOCKED AGELOCK",
                "Ageable",
                "Boolean",
                "ageable",
                "Returns true if age locked",
                new SimpleWorker(new int[]{0x82}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Ageable a = v.pop(Ageable.class);
                        v.push(a.getAgeLock());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ADULT",
                "ISADULT ADULT",
                "Ageable",
                "Boolean",
                "ageable",
                "Returns true if entity is adult",
                new SimpleWorker(new int[]{0x83}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Ageable a = v.pop(Ageable.class);
                        v.push(a.isAdult());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "BABY",
                "BABY ISBABY",
                "Ageable",
                "Boolean",
                "ageable",
                "Returns true if entity is baby",
                new SimpleWorker(new int[]{0x84}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Ageable a = v.pop(Ageable.class);
                        v.push(!(a.isAdult()));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETADULT",
                "SETADULT >ADULT",
                "Ageable",
                "Ageable",
                "ageable",
                "Set entity's age to adult",
                new SimpleWorker(new int[]{0x85}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Ageable a = v.peek(Ageable.class);
                        a.setAdult();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBABY",
                "SETBABY >BABY",
                "Ageable",
                "Ageable",
                "ageable",
                "Set entity's age to baby",
                new SimpleWorker(new int[]{0x86}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Ageable a = v.peek(Ageable.class);
                        a.setBaby();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETAGE",
                "SETAGE",
                "Ageable Integer",
                "Ageable",
                "ageable",
                "Set entity's age",
                new SimpleWorker(new int[]{0x87}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Ageable a = v.peek(Ageable.class);
                        a.setAge(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETAGELOCK",
                "SETAGELOCK SETAGELOCKED >AGELOCK",
                "Ageable Boolean",
                "Ageable",
                "ageable",
                "Lock the age of the animal, setting this will prevent the animal from maturing or getting ready for mating",
                new SimpleWorker(new int[]{0x88}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean i = v.pop(Boolean.class);
                        Ageable a = v.peek(Ageable.class);
                        a.setAgeLock(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBREED",
                "SETBREED",
                "Ageable Boolean",
                "Ageable",
                "ageable",
                "Set breedability of the animal, if the animal is a baby and set to breed it will instantly grow up",
                new SimpleWorker(new int[]{0x89}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean i = v.pop(Boolean.class);
                        Ageable a = v.peek(Ageable.class);
                        a.setBreed(i);
                    }
                }
        ));


        /** BOAT **/


        VSCompiler.addRule(new SimpleCompileRule(
                "BOATSPEED",
                "BOATSPEED",
                "Boat",
                "Double",
                "boat",
                "Gets the maximum speed of a boat",
                new SimpleWorker(new int[]{0x8A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boat b = v.pop(Boat.class);
                        v.push(b.getMaxSpeed());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "OCCUPIEDDECELERATION",
                "OCCUPIEDDECELERATION",
                "Boat",
                "Double",
                "boat",
                "Gets the deceleration rate of occupied boats",
                new SimpleWorker(new int[]{0x8B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boat b = v.pop(Boat.class);
                        v.push(b.getOccupiedDeceleration());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "UNOCCUPIEDDECELERATION",
                "UNOCCUPIEDDECELERATION",
                "Boat",
                "Double",
                "boat",
                "Gets the deceleration rate of unoccupied boats",
                new SimpleWorker(new int[]{0x8C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boat b = v.pop(Boat.class);
                        v.push(b.getUnoccupiedDeceleration());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WORKONLAND",
                "WORKONLAND",
                "Boat",
                "Boolean",
                "boat",
                "Returns true if boat work on land",
                new SimpleWorker(new int[]{0x8D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boat b = v.pop(Boat.class);
                        v.push(b.getUnoccupiedDeceleration());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBOATSPEED",
                "SETBOATSPEED >BSPEED",
                "Boat Double",
                "Boat",
                "boat",
                "Set the maximum speed of a boat",
                new SimpleWorker(new int[]{0x8E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double i = v.pop(Double.class);
                        Boat b = v.peek(Boat.class);
                        b.setMaxSpeed(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETOCCUPIEDDECELERATION",
                "SETOCCUPIEDDECELERATION",
                "Boat Double",
                "Boat",
                "boat",
                "Sets the deceleration rate of occupied boats",
                new SimpleWorker(new int[]{0x8F, 0x00}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double i = v.pop(Double.class);
                        Boat b = v.peek(Boat.class);
                        b.setOccupiedDeceleration(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETUNOCCUPIEDDECELERATION",
                "SETUNOCCUPIEDDECELERATION",
                "Boat Double",
                "Boat",
                "boat",
                "Sets the deceleration rate of unoccupied boats",
                new SimpleWorker(new int[]{0x8F, 0x01}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double i = v.pop(Double.class);
                        Boat b = v.peek(Boat.class);
                        b.setUnoccupiedDeceleration(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETWORKONLAND",
                "SETWORKONLAND >WORKONLAND",
                "Boat Boolean",
                "Boat",
                "boat",
                "Set whether boats can work on land",
                new SimpleWorker(new int[]{0x8F, 0x02}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean i = v.pop(Boolean.class);
                        Boat b = v.peek(Boat.class);
                        b.setWorkOnLand(i);
                    }
                }
        ));


        /** Creature **/


        VSCompiler.addRule(new SimpleCompileRule(
                "TARGET",
                "TARGET",
                "Creature",
                "LivingEnity",
                "creature",
                "Gets the current target of this Creature",
                new SimpleWorker(new int[]{0x8F, 0x03}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Creature c = v.pop(Creature.class);
                        v.push(c.getTarget());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETTARGET",
                "SETTARGET >TARGET",
                "Creature LivingEntity",
                "Creature",
                "creature",
                "Instructs this Creature to set the specified LivingEntity as its target",
                new SimpleWorker(new int[]{0x8F, 0x04}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        LivingEntity l = v.pop(LivingEntity.class);
                        Creature c = v.peek(Creature.class);
                        c.setTarget(l);
                    }
                }
        ));


        /** Creeper **/


        VSCompiler.addRule(new SimpleCompileRule(
                "POWERED",
                "POWERED",
                "Creeper",
                "Boolean",
                "creeper",
                "Returns true if creeper is powered",
                new SimpleWorker(new int[]{0x8F, 0x05}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Creeper c = v.pop(Creeper.class);
                        v.push(c.isPowered());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPOWERED",
                "SETPOWERED",
                "Creeper Boolean",
                "Creeper",
                "creeper",
                "Set powered status of creeper",
                new SimpleWorker(new int[]{0x8F, 0x06}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Creeper c = v.peek(Creeper.class);
                        c.setPowered(b);
                    }
                }
        ));


        /** Damageable **/


        VSCompiler.addRule(new SimpleCompileRule(
                "DAMAGE",
                "DAMAGE DMG",
                "Damageable Double",
                "Damageable",
                "damage",
                "Damage entity",
                new SimpleWorker(new int[]{0x8F, 0x07}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double count = v.pop(Double.class);
                        Damageable e = v.peek(Damageable.class);
                        e.damage(count);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "DAMAGEBY",
                "DAMAGEBY DMGFROM DMGBY",
                "Damageable(A) Double(damage) Entity(B)",
                "Damageable",
                "damage",
                "Damage Entity A from Entity B",
                new SimpleWorker(new int[]{0x8F, 0x08}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e2 = v.pop(Entity.class);
                        Double count = v.pop(Double.class);
                        Damageable e = v.peek(Damageable.class);
                        e.damage(count, e2);
                    }
                }
        ));


        /** Enderman **/


        VSCompiler.addRule(new SimpleCompileRule(
                "CARRIEDMATERIAL",
                "CARRIEDMATERIAL CARRIED",
                "Enderman",
                "ItemStack",
                "enderman",
                "Get carried material of enderman",
                new SimpleWorker(new int[]{0x8F, 0x09}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Enderman e = v.pop(Enderman.class);
                        v.push(e.getCarriedMaterial().toItemStack());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCARRIEDMATERIAL",
                "SETCARRIEDMATERIAL SETCARRIED",
                "Enderman ItemStack",
                "Enderman",
                "enderman",
                "Set carried material for enderman",
                new SimpleWorker(new int[]{0x8F, 0x0A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack i = v.pop(ItemStack.class);
                        Enderman e = v.peek(Enderman.class);
                        e.setCarriedMaterial(i.getData());
                    }
                }
        ));


        /** ExperienceOrb **/


        VSCompiler.addRule(new SimpleCompileRule(
                "ORBEXP",
                "ORBEXP",
                "ExperienceOrb",
                "Integer",
                "experienceorb",
                "Gets how much experience is contained within this orb",
                new SimpleWorker(new int[]{0x8F, 0x0B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ExperienceOrb e = v.pop(ExperienceOrb.class);
                        v.push(e.getExperience());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETORBEXP",
                "SETORBEXP >ORBEXP",
                "ExperienceOrb Integer",
                "ExperienceOrb",
                "experienceorb",
                "Sets how much experience is contained within this orb",
                new SimpleWorker(new int[]{0x8F, 0x0C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        ExperienceOrb e = v.peek(ExperienceOrb.class);
                        e.setExperience(i);
                    }
                }
        ));


        /** Explosive **/


        VSCompiler.addRule(new SimpleCompileRule(
                "YIELD",
                "YIELD",
                "Explosive",
                "Float",
                "explosive",
                "Return the radius or yield of this explosive's explosion",
                new SimpleWorker(new int[]{0x8F, 0x0D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Explosive e = v.pop(Explosive.class);
                        v.push(e.getYield());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "INCENDIARY",
                "INCENDIARY ISINCENDIARY",
                "Explosive",
                "Boolean",
                "explosive",
                "Return whether or not this explosive creates a fire when exploding",
                new SimpleWorker(new int[]{0x8F, 0x0E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Explosive e = v.pop(Explosive.class);
                        v.push(e.isIncendiary());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETINCENDIARY",
                "SETINCENDIARY SETISINCENDIARY",
                "Explosive Boolean",
                "Explosive",
                "explosive",
                "Set whether or not this explosive's explosion causes fire",
                new SimpleWorker(new int[]{0x8F, 0x0F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Explosive e = v.peek(Explosive.class);
                        e.setIsIncendiary(b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETYIELD",
                "SETYIELD >YIELD",
                "Explosive Integer",
                "Explosive",
                "explosive",
                "Set the radius affected by this explosive's explosion",
                new SimpleWorker(new int[]{0x8F, 0x10}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer b = v.pop(Integer.class);
                        Explosive e = v.peek(Explosive.class);
                        e.setYield(b);
                    }
                }
        ));


        /** Falling bloooock! **/


        VSCompiler.addRule(new SimpleCompileRule(
                "FALLINGDATA",
                "FALLINGDATA FDATA",
                "FallingBlock",
                "Byte",
                "fallingblock",
                "Get the data of the falling block",
                new SimpleWorker(new int[]{0x8F, 0x11}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        FallingBlock s = v.pop(FallingBlock.class);
                        v.push(s.getBlockData());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FALLINGID",
                "FALLINGID FID",
                "FallingBlock",
                "Integer",
                "fallingblock",
                "Get the ID of the falling block",
                new SimpleWorker(new int[]{0x8F, 0x12}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        FallingBlock s = v.pop(FallingBlock.class);
                        v.push(s.getBlockId());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FALLINGDROP",
                "FALLINGDROP FDROP",
                "FallingBlock",
                "Boolean",
                "fallingblock",
                "Get if the falling block will break into an item if it cannot be placed",
                new SimpleWorker(new int[]{0x8F, 0x13}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        FallingBlock s = v.pop(FallingBlock.class);
                        v.push(s.getDropItem());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FALLINGMATERIAL",
                "FALLINGMATERIAL FMATERIAL",
                "FallingBlock",
                "ItemStack",
                "fallingblock",
                "Get the Material of the falling block",
                new SimpleWorker(new int[]{0x8F, 0x14}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        FallingBlock s = v.pop(FallingBlock.class);
                        ItemStack i = new ItemStack(s.getMaterial(), 1, s.getBlockData());
                        v.push(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFALLINGDROP",
                "SETFALLINGDROP >FDROP",
                "FallingBlock Boolean",
                "FallingBlock",
                "fallingblock",
                "Set if the falling block will break into an item if it cannot be placed",
                new SimpleWorker(new int[]{0x8F, 0x15}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean val = v.pop(Boolean.class);
                        v.peek(FallingBlock.class).setDropItem(val);
                    }
                }
        ));


        /** Fireball :3 **/


        VSCompiler.addRule(new SimpleCompileRule(
                "FIREBALLDIRECTION",
                "FIREBALLDIRECTION FDIRECTION FDIR",
                "Fireball",
                "Vector",
                "fireball",
                "Retrieve the direction this fireball is heading toward",
                new SimpleWorker(new int[]{0x8F, 0x16}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Fireball e = v.pop(Fireball.class);
                        v.push(e.getDirection());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFIREBALLDIRECTION",
                "SETFIREBALLDIRECTION SETFDIRECTION >FDIR",
                "Fireball Vector",
                "Fireball",
                "fireball",
                "Fireballs fly straight and do not take setVelocity(...) well.",
                new SimpleWorker(new int[]{0x8F, 0x17}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector vec = v.pop(Vector.class);
                        Fireball e = v.peek(Fireball.class);
                        e.setDirection(vec);
                    }
                }
        ));


        /** Fish **/


        VSCompiler.addRule(new SimpleCompileRule(
                "BITECHANCE",
                "BITECHANCE",
                "Fish",
                "Double",
                "fish",
                "Gets the chance of a fish biting",
                new SimpleWorker(new int[]{0x8F, 0x18}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Fish e = v.pop(Fish.class);
                        v.push(e.getBiteChance());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBITECHANCE",
                "SETBITECHANCE",
                "Fish Double",
                "Fish",
                "fish",
                "Sets the chance of a fish biting",
                new SimpleWorker(new int[]{0x8F, 0x19}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double k = v.pop(Double.class);
                        Fish e = v.peek(Fish.class);
                        e.setBiteChance(k);

                    }
                }
        ));


        /** Hanging **/


        VSCompiler.addRule(new SimpleCompileRule(
                "SETFACINGDIRECTION",
                "SETFACINGDIRECTION >FACEDIR",
                "Hanging #BlockFace Boolean",
                "Hanging",
                "hanging",
                "Sets the direction of the hanging entity, potentially overriding rules of placement",
                new SimpleWorker(new int[]{0x8F, 0x1A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        BlockFace b2 = v.pop(BlockFace.values());
                        Hanging h = v.peek(Hanging.class);
                        h.setFacingDirection(b2, b);

                    }
                }
        ));


        /** Horse **/


        VSCompiler.addRule(new SimpleCompileRule(
                "HORSECOLOR",
                "HORSECOLOR",
                "Horse",
                "#HorseColor",
                "horse",
                "Gets the horse's color",
                new SimpleWorker(new int[]{0x8F, 0x1B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Horse h = v.pop(Horse.class);
                        v.push(h.getColor());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HORSETAME",
                "HORSETAME HORSEDOMESTICATION",
                "Horse",
                "Integer",
                "horse",
                "Gets the domestication level of this horse",
                new SimpleWorker(new int[]{0x8F, 0x1C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Horse h = v.pop(Horse.class);
                        v.push(h.getDomestication());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HORSEINVENTORY",
                "HORSEINVENTORY HORSEINV HINV",
                "Horse",
                "HorseInventory",
                "horse",
                "Get horse inventory",
                new SimpleWorker(new int[]{0x8F, 0x1D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Horse h = v.pop(Horse.class);
                        v.push(h.getInventory());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HORSEJUMPSTRENGTH",
                "HORSEJUMPSTRENGTH HORSEJUMP HJUMP",
                "Horse",
                "Double",
                "horse",
                "Gets the jump strength of this horse",
                new SimpleWorker(new int[]{0x8F, 0x1E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Horse h = v.pop(Horse.class);
                        v.push(h.getJumpStrength());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "MAXHORSETAME",
                "MAXHORSETAME MAXHORSEDOMESTICATION",
                "Horse",
                "Integer",
                "horse",
                "Gets the maximum domestication level of this horse",
                new SimpleWorker(new int[]{0x8F, 0x1F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Horse h = v.pop(Horse.class);
                        v.push(h.getMaxDomestication());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HORSESTYLE",
                "HORSESTYLE HSTYLE",
                "Horse",
                "String",
                "horse",
                "Get horse style",
                new SimpleWorker(new int[]{0x8F, 0x20}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Horse h = v.pop(Horse.class);
                        v.push(h.getStyle());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HORSEVARIANT",
                "HORSEVARIANT HVARIANT",
                "Horse",
                "String",
                "horse",
                "Gets the horse's variant",
                new SimpleWorker(new int[]{0x8F, 0x21}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Horse h = v.pop(Horse.class);
                        v.push(h.getVariant());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HORSECHEST",
                "HORSECHEST CARRYINGCHEST",
                "Horse",
                "Boolean",
                "horse",
                "Gets whether the horse has a chest equipped",
                new SimpleWorker(new int[]{0x8F, 0x22}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Horse h = v.pop(Horse.class);
                        v.push(h.isCarryingChest());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETHORSECHEST",
                "SETHORSECHEST SETCARRYINGCHEST",
                "Horse Boolean",
                "Horse",
                "horse",
                "Set whether the horse has a chest equipped",
                new SimpleWorker(new int[]{0x8F, 0x23}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Horse h = v.peek(Horse.class);
                        h.setCarryingChest(b);


                    }
                }
        ));
        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "SETHORSECOLOR",
                    "SETHORSECOLOR >HCOLOR",
                    "Horse #HorseColor",
                    "Horse",
                    "horse",
                    "Sets the horse's color",
                    new SimpleWorker(new int[]{0x8F, 0x24}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            Horse.Color b = v.pop(Horse.Color.values());
                            Horse h = v.peek(Horse.class);
                            h.setColor(b);


                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "SETHORETAME",
                    "SETHORETAME >HTAME SETDOMESTICATION",
                    "Horse Integer",
                    "Horse",
                    "horse",
                    "Sets the domestication level of this horse",
                    new SimpleWorker(new int[]{0x8F, 0x25}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            Integer b = v.pop(Integer.class);
                            Horse h = v.peek(Horse.class);
                            h.setDomestication(b);
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "SETHORSEJUMPSTRENGTH",
                    "SETHORSEJUMPSTRENGTH >HJUMP SETHORSEJUMP >HORSEJUMP",
                    "Horse Double",
                    "Horse",
                    "horse",
                    "Sets the domestication level of this horse",
                    new SimpleWorker(new int[]{0x8F, 0x26}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            Double b = v.pop(Double.class);
                            Horse h = v.peek(Horse.class);
                            h.setJumpStrength(b);
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "SETHORSEMAXTAME",
                    "SETHORSEMAXTAME >HMAXTAME >HORSEMAXTAME SETMAXDOMESTICATION",
                    "Horse Double",
                    "Horse",
                    "horse",
                    "Sets the domestication level of this horse",
                    new SimpleWorker(new int[]{0x8F, 0x27}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            Integer b = v.pop(Integer.class);
                            Horse h = v.peek(Horse.class);
                            h.setMaxDomestication(b);
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "SETHORSESTYLE",
                    "SETHORSESTYLE >HORSESTYLE >HSTYLE",
                    "Horse #HorseStyle",
                    "Horse",
                    "horse",
                    "Set horse style",
                    new SimpleWorker(new int[]{0x8F, 0x28}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            Horse.Style b = v.pop(Horse.Style.values());
                            Horse h = v.peek(Horse.class);
                            h.setStyle(b);
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }

        try {
            VSCompiler.addRule(new SimpleCompileRule(
                    "SETHORSEVARIANT",
                    "SETHORSEVARIANT >HORSEVARIANT >HVARIANT",
                    "Horse #HorseVariant",
                    "Horse",
                    "horse",
                    "Set horse variant",
                    new SimpleWorker(new int[]{0x8F, 0x29}) {
                        @Override
                        public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                            Horse.Variant b = v.pop(Horse.Variant.values());
                            Horse h = v.peek(Horse.class);
                            h.setVariant(b);
                        }
                    }
            ));
        } catch (NoClassDefFoundError ignored) {
        }


        /** IronGolem **/


        VSCompiler.addRule(new SimpleCompileRule(
                "ISPLAYERCREATED",
                "ISPLAYERCREATED",
                "IronGolem",
                "Boolean",
                "irongolem",
                "Returns true if golem created by player",
                new SimpleWorker(new int[]{0x8F, 0x3A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        IronGolem i = v.pop(IronGolem.class);
                        v.push(i.isPlayerCreated());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPLAYERCREATED",
                "SETPLAYERCREATED SETISPLAYERCREATED",
                "IronGolem Boolean",
                "IronGolem",
                "irongolem",
                "Sets whether this iron golem was built by a player or not",
                new SimpleWorker(new int[]{0x8F, 0x3B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        IronGolem i = v.peek(IronGolem.class);
                        i.setPlayerCreated(b);

                    }
                }
        ));


        /** ItemStack **/


        VSCompiler.addRule(new SimpleCompileRule(
                "ITEMSTACK",
                "ITEMSTACK",
                "Item",
                "ItemStack",
                "item",
                "Gets the item stack associated with this item drop",
                new SimpleWorker(new int[]{0x8F, 0x3C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Item i = v.pop(Item.class);
                        v.push(i.getItemStack());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PICKUPDELAY",
                "PICKUPDELAY",
                "Item",
                "Integer",
                "item",
                "Gets the delay before this Item is available to be picked up by players",
                new SimpleWorker(new int[]{0x8F, 0x3D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Item i = v.pop(Item.class);
                        v.push(i.getPickupDelay());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETITEMSTACK",
                "SETITEMSTACK >ITEMSTACK",
                "Item ItemStack",
                "Item",
                "item",
                "Sets the item stack associated with this item drop",
                new SimpleWorker(new int[]{0x8F, 0x3E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack i2 = v.pop(ItemStack.class);
                        Item i = v.peek(Item.class);
                        i.setItemStack(i2);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPICKUPDELAY",
                "SETPICKUPDELAY >PICKUPDELAY",
                "Item Integer",
                "Item",
                "item",
                "Sets the delay before this Item is available to be picked up by players",
                new SimpleWorker(new int[]{0x8F, 0x3F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i2 = v.pop(Integer.class);
                        Item i = v.peek(Item.class);
                        i.setPickupDelay(i2);

                    }
                }
        ));


        /** ItemFrame **/


        VSCompiler.addRule(new SimpleCompileRule(
                "FRAMEITEM",
                "FRAMEITEM",
                "ItemFrame",
                "ItemStack",
                "itemframe",
                "Get the item in this frame",
                new SimpleWorker(new int[]{0x8F, 0x40}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemFrame i = v.pop(ItemFrame.class);
                        v.push(i.getItem());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FRAMEROTATION",
                "FRAMEROTATION",
                "ItemFrame",
                "#Rotation",
                "itemframe",
                "Get the rotation of the frame's item",
                new SimpleWorker(new int[]{0x8F, 0x41}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemFrame i = v.pop(ItemFrame.class);
                        v.push(i.getRotation());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFRAMEROTATION",
                "SETFRAMEROTATION",
                "ItemFrame #Rotation",
                "ItemFrame",
                "itemframe",
                "Set the rotation of the frame's item",
                new SimpleWorker(new int[]{0x8F, 0x42}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Rotation k = v.pop(Rotation.values());
                        ItemFrame i = v.peek(ItemFrame.class);
                        i.setRotation(k);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFRAMEITEM",
                "SETFRAMEITEM",
                "ItemFrame ItemStack",
                "ItemFrame",
                "itemframe",
                "Set the item in this frame",
                new SimpleWorker(new int[]{0x8F, 0x43}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack i2 = v.pop(ItemStack.class);
                        ItemFrame i = v.peek(ItemFrame.class);
                        i.setItem(i2);

                    }
                }
        ));


        /** Human **/


        VSCompiler.addRule(new SimpleCompileRule(
                "CLOSEINVENTORY",
                "CLOSEINVENTORY",
                "HumanEntity",
                "HumanEntity",
                "player",
                "Close inventory",
                new SimpleWorker(new int[]{0x8F, 0x44}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        HumanEntity e = v.peek(HumanEntity.class);
                        e.closeInventory();

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ENDERCHEST",
                "ENDERCHEST",
                "HumanEntity",
                "Inventory",
                "player",
                "Get ender chest's inventory",
                new SimpleWorker(new int[]{0x8F, 0x45}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        HumanEntity e = v.pop(HumanEntity.class);
                        v.push(e.getEnderChest());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "GAMEMODE",
                "GAMEMODE GM",
                "HumanEntity",
                "#GameMode",
                "player",
                "Get player's gamemode",
                new SimpleWorker(new int[]{0x8F, 0x46}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        HumanEntity e = v.pop(HumanEntity.class);
                        v.push(e.getGameMode());

                    }
                }
        ));
        // code 0x47
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERCURSOR",
                "PLAYERCURSOR CURSOR",
                "HumanEntity",
                "ItemStack",
                "player",
                "Get item on cursor",
                new SimpleWorker(new int[]{0x8F, 0x48}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        HumanEntity e = v.pop(HumanEntity.class);
                        v.push(e.getItemOnCursor());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "OPENEDINVENTORY",
                "OPENEDINVENTORY OPDINV",
                "HumanEntity",
                "Inventory",
                "player",
                "Get opened inventory",
                new SimpleWorker(new int[]{0x8F, 0x49}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        HumanEntity e = v.pop(HumanEntity.class);
                        v.push(e.getOpenInventory());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SLEEPTICKS",
                "SLEEPTICKS",
                "HumanEntity",
                "Integer",
                "player",
                "Get player's sleep ticks",
                new SimpleWorker(new int[]{0x8F, 0x4A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        HumanEntity e = v.pop(HumanEntity.class);
                        v.push(e.getSleepTicks());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISBLOCKING",
                "ISBLOCKING",
                "HumanEntity",
                "Boolean",
                "player",
                "Check if the player is currently blocking (ie with a sword)",
                new SimpleWorker(new int[]{0x8F, 0x4B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        HumanEntity e = v.pop(HumanEntity.class);
                        v.push(e.isBlocking());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISSLEEPING",
                "ISSLEEPING ISSLEEP",
                "HumanEntity",
                "Boolean",
                "player",
                "Returns true if player sleep",
                new SimpleWorker(new int[]{0x8F, 0x4C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        HumanEntity e = v.pop(HumanEntity.class);
                        v.push(e.isSleeping());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "OPENENCHANTING",
                "OPENENCHANTING OPENENCH",
                "HumanEntity Location",
                "HumanEntity",
                "player",
                "Open enchanting table for player",
                new SimpleWorker(new int[]{0x8F, 0x4D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        HumanEntity e = v.peek(HumanEntity.class);
                        e.openEnchanting(l, true);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "OPENINVENTORY",
                "OPENINVENTORY OPENINV",
                "HumanEntity Inventory",
                "HumanEntity",
                "player",
                "Open inventory for player",
                new SimpleWorker(new int[]{0x8F, 0x4E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Inventory i = v.pop(Inventory.class);
                        HumanEntity e = v.peek(HumanEntity.class);
                        e.openInventory(i);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "OPENWORKBENCH",
                "OPENWORKBENCH OPENWORK",
                "HumanEntity Location",
                "HumanEntity",
                "player",
                "Open inventory for player",
                new SimpleWorker(new int[]{0x8F, 0x4F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location i = v.pop(Location.class);
                        HumanEntity e = v.peek(HumanEntity.class);
                        e.openWorkbench(i, true);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETGAMEMODE",
                "SETGAMEMODE >GM SETGM",
                "HumanEntity #GameMode",
                "HumanEntity",
                "player",
                "Open inventory for player",
                new SimpleWorker(new int[]{0x8F, 0x50}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        GameMode i = v.pop(GameMode.values());
                        HumanEntity e = v.peek(HumanEntity.class);
                        e.setGameMode(i);

                    }
                }
        ));
        // code 0x51
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCURSOR",
                "SETCURSOR",
                "HumanEntity ItemStack",
                "HumanEntity",
                "player",
                "Sets the item to the given ItemStack, this will replace whatever the user was moving",
                new SimpleWorker(new int[]{0x8F, 0x52}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack i = v.pop(ItemStack.class);
                        HumanEntity e = v.peek(HumanEntity.class);
                        e.setItemOnCursor(i);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETWINDOWPROPERTY",
                "SETWINDOWPROPERTY",
                "HumanEntity Integer InventoryView.Property",
                "HumanEntity",
                "player",
                "If the player currently has an inventory window open, this method will set a property of that window, such as the state of a progress bar",
                new SimpleWorker(new int[]{0x8F, 0x53}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        InventoryView.Property p = v.pop(InventoryView.Property.values());
                        Integer i = v.pop(Integer.class);
                        HumanEntity e = v.peek(HumanEntity.class);
                        e.setWindowProperty(p, i);

                    }
                }
        ));


        /**   Ocelot   **/


        VSCompiler.addRule(new SimpleCompileRule(
                "CATTYPE",
                "CATTYPE",
                "Ocelot",
                "#OcelotType",
                "ocelot",
                "Get ocelot type",
                new SimpleWorker(new int[]{0x8F, 0x54}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Ocelot o = v.pop(Ocelot.class);
                        v.push(o.getCatType());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CATSITTING",
                "CATSITTING CATSIT",
                "Ocelot",
                "Boolean",
                "ocelot",
                "Returns true if ocelot sitting",
                new SimpleWorker(new int[]{0x8F, 0x55}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Ocelot o = v.pop(Ocelot.class);
                        v.push(o.isSitting());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCATTYPE",
                "SETCATTYPE SETCATTYPE >CATTYPE",
                "Ocelot #OcelotType",
                "Ocelot",
                "ocelot",
                "Returs true if ocelot sitting",
                new SimpleWorker(new int[]{0x8F, 0x56}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Ocelot.Type ot = v.pop(Ocelot.Type.values());
                        Ocelot o = v.peek(Ocelot.class);
                        o.setCatType(ot);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCATSITTING",
                "SETCATSITTING SETCATSIT >CATSIT",
                "Ocelot Boolean",
                "Ocelot",
                "ocelot",
                "Returs true if ocelot sitting",
                new SimpleWorker(new int[]{0x8F, 0x57}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean ot = v.pop(Boolean.class);
                        Ocelot o = v.peek(Ocelot.class);
                        o.setSitting(ot);

                    }
                }
        ));


        /** Art **/


        VSCompiler.addRule(new SimpleCompileRule(
                "ART",
                "ART",
                "Painting",
                "#Art",
                "painting",
                "Get painting art",
                new SimpleWorker(new int[]{0x8F, 0x58}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Painting p = v.pop(Painting.class);
                        v.push(p.getArt());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETART",
                "SETART >ART",
                "Painting #Art",
                "Painting",
                "painting",
                "Set painting art",
                new SimpleWorker(new int[]{0x8F, 0x59}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Art a = v.pop(Art.values());
                        Painting p = v.peek(Painting.class);
                        p.setArt(a);

                    }
                }
        ));


        /** Pig **/


        VSCompiler.addRule(new SimpleCompileRule(
                "HASSADDLE",
                "HASSADDLE",
                "Pig",
                "Boolean",
                "pig",
                "Returs true if pig has saddle",
                new SimpleWorker(new int[]{0x8F, 0x5A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Pig p = v.pop(Pig.class);
                        v.push(p.hasSaddle());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSADDLE",
                "SETSADDLE",
                "Pig Boolean",
                "Pig",
                "pig",
                "Sets if the pig has a saddle or not",
                new SimpleWorker(new int[]{0x8F, 0x5B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Pig p = v.peek(Pig.class);
                        p.setSaddle(b);

                    }
                }
        ));


        /** PigZombie **/


        VSCompiler.addRule(new SimpleCompileRule(
                "PIGZOMBIEANGER",
                "PIGZOMBIEANGER PZANGER",
                "PigZombie",
                "Integer",
                "pigzombie",
                "Get pigzombie anger lvl",
                new SimpleWorker(new int[]{0x8F, 0x5C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        PigZombie p = v.pop(PigZombie.class);
                        v.push(p.getAnger());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PIGZOMBIEANGRY",
                "PIGZOMBIEANGRY PZANGRY",
                "PigZombie",
                "Boolean",
                "pigzombie",
                "Returns true if pigzombie is anger",
                new SimpleWorker(new int[]{0x8F, 0x5D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        PigZombie p = v.pop(PigZombie.class);
                        v.push(p.isAngry());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPIGZOMBIEANGER",
                "SETPIGZOMBIEANGER SETPZANGER",
                "PigZombie Integer",
                "PigZombie",
                "pigzombie",
                "Set pigzombie anger lvl",
                new SimpleWorker(new int[]{0x8F, 0x5E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        PigZombie p = v.peek(PigZombie.class);
                        p.setAnger(i);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPIGZOMBIEANGRY",
                "SETPIGZOMBIEANGRY SETPZANGRY",
                "PigZombie Boolean",
                "PigZombie",
                "pigzombie",
                "Shorthand; sets to either 0 or the default level",
                new SimpleWorker(new int[]{0x8F, 0x5F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean i = v.pop(Boolean.class);
                        PigZombie p = v.peek(PigZombie.class);
                        p.setAngry(i);

                    }
                }
        ));


        /** Projectile **/


        VSCompiler.addRule(new SimpleCompileRule(
                "BOUNCE",
                "BOUNCE",
                "Projectile",
                "Boolean",
                "projectile",
                "Determine if this projectile should bounce or not when it hits",
                new SimpleWorker(new int[]{0x8F, 0x60}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Projectile p = v.pop(Projectile.class);
                        v.push(p.doesBounce());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SHOOTER",
                "SHOOTER",
                "Projectile",
                "LivingEntity",
                "projectile",
                "Get owner of projectile",
                new SimpleWorker(new int[]{0x8F, 0x61}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Projectile p = v.pop(Projectile.class);
                        v.push(p.getShooter());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETBOUNCE",
                "SETBOUNCE",
                "Projectile Boolean",
                "Projectile",
                "projectile",
                "Set whether or not this projectile should bounce or not when it hits something",
                new SimpleWorker(new int[]{0x8F, 0x62}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Projectile p = v.peek(Projectile.class);
                        p.setBounce(b);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSHOOTER",
                "SETSHOOTER",
                "Projectile LivingEntity",
                "Projectile",
                "projectile",
                "Set the shooter of this projectile",
                new SimpleWorker(new int[]{0x8F, 0x63}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        LivingEntity b = v.pop(LivingEntity.class);
                        Projectile p = v.peek(Projectile.class);
                        p.setShooter(b);

                    }
                }
        ));


        /** Sheep **/


        VSCompiler.addRule(new SimpleCompileRule(
                "SHEARED",
                "SHEARED",
                "Sheep",
                "Boolean",
                "sheep",
                "Returns true if sheep is sheared",
                new SimpleWorker(new int[]{0x8F, 0x64}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Sheep s = v.pop(Sheep.class);
                        v.push(s.isSheared());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSHEARED",
                "SETSHEARED",
                "Sheep Boolean",
                "Sheep",
                "sheep",
                "Set sheared status of sheep",
                new SimpleWorker(new int[]{0x8F, 0x65}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Sheep s = v.peek(Sheep.class);
                        s.setSheared(b);

                    }
                }
        ));


        /** Skeleton **/


        VSCompiler.addRule(new SimpleCompileRule(
                "SKELETONTYPE",
                "SKELETONTYPE",
                "Skeleton",
                "#SkeletonType",
                "skeleton",
                "Get skeleton type",
                new SimpleWorker(new int[]{0x8F, 0x66}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Skeleton s = v.pop(Skeleton.class);
                        v.push(s.getSkeletonType());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSKELETONTYPE",
                "SETSKELETONTYPE >SKELETONTYPE",
                "Skeleton #SkeletonType",
                "Skeleton",
                "skeleton",
                "Set skeleton type",
                new SimpleWorker(new int[]{0x8F, 0x67}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Skeleton.SkeletonType st = v.pop(Skeleton.SkeletonType.values());
                        Skeleton s = v.peek(Skeleton.class);
                        s.setSkeletonType(st);

                    }
                }
        ));


        /** Slime **/


        VSCompiler.addRule(new SimpleCompileRule(
                "SLIMESIZE",
                "SLIMESIZE SSIZE",
                "Slime",
                "Integer",
                "slime",
                "Get slime size",
                new SimpleWorker(new int[]{0x8F, 0x68}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Slime s = v.pop(Slime.class);
                        v.push(s.getSize());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETSLIMESIZE",
                "SETSLIMESIZE SETSSIZE >SSIZE",
                "Slime Integer",
                "Slime",
                "slime",
                "Set slime size",
                new SimpleWorker(new int[]{0x8F, 0x69}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Slime s = v.peek(Slime.class);
                        s.setSize(i);

                    }
                }
        ));


        /** PrimedTNT **/


        VSCompiler.addRule(new SimpleCompileRule(
                "FUSE",
                "FUSE",
                "TNTPrimed",
                "Integer",
                "tnt",
                "Get fuse ticks of tnt",
                new SimpleWorker(new int[]{0x8F, 0x6A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        TNTPrimed t = v.pop(TNTPrimed.class);
                        v.push(t.getFuseTicks());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PRIMER",
                "PRIMER",
                "TNTPrimed",
                "Entity",
                "tnt",
                "Gets the source of this primed TNT",
                new SimpleWorker(new int[]{0x8F, 0x6B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        TNTPrimed t = v.pop(TNTPrimed.class);
                        v.push(t.getFuseTicks());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFUSE",
                "SETFUSE",
                "TNTPrimed Integer",
                "Integer",
                "tnt",
                "Set fuse ticks of tnt",
                new SimpleWorker(new int[]{0x8F, 0x6C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        TNTPrimed t = v.peek(TNTPrimed.class);
                        t.setFuseTicks(i);

                    }
                }
        ));


        /** Villager **/


        VSCompiler.addRule(new SimpleCompileRule(
                "PROFESSION",
                "PROFESSION",
                "Villager",
                "#Profession",
                "villager",
                "Get villager profession",
                new SimpleWorker(new int[]{0x8F, 0x6D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Villager i = v.pop(Villager.class);
                        v.push(i.getProfession());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPROFESSION",
                "SETPROFESSION",
                "Villager #Profession",
                "Villager",
                "villager",
                "Set villager profession",
                new SimpleWorker(new int[]{0x8F, 0x6E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Villager.Profession p = v.pop(Villager.Profession.values());
                        Villager i = v.peek(Villager.class);
                        i.setProfession(p);

                    }
                }
        ));


        /** Wolf **/


        VSCompiler.addRule(new SimpleCompileRule(
                "WOLFCOLOR",
                "WOLFCOLOR",
                "Wolf",
                "#DyeColor",
                "wolf",
                "Get the collar color of wolf",
                new SimpleWorker(new int[]{0x8F, 0x6F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Wolf x = v.pop(Wolf.class);
                        v.push(x.getCollarColor());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WOLFANGRY",
                "WOLFANGRY",
                "Wolf",
                "Boolean",
                "wolf",
                "Returns true if wolf is angry",
                new SimpleWorker(new int[]{0x8F, 0x70}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Wolf x = v.pop(Wolf.class);
                        v.push(x.isAngry());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "WOLFSITTING",
                "WOLFSITTING WOLFSIT",
                "Wolf",
                "Boolean",
                "wolf",
                "Returns true if wolf sit",
                new SimpleWorker(new int[]{0x8F, 0x71}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Wolf x = v.pop(Wolf.class);
                        v.push(x.isSitting());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETWOLFANGRY",
                "SETWOLFANGRY",
                "Wolf Boolean",
                "Wolf",
                "wolf",
                "Sets the anger of this wolf An angry wolf can not be fed or tamed, and will actively look for targets to attack",
                new SimpleWorker(new int[]{0x8F, 0x72}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Wolf x = v.peek(Wolf.class);
                        x.setAngry(b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETWOLFCOLOR",
                "SETWOLFCOLOR",
                "Wolf #DyeColor",
                "Wolf",
                "wolf",
                "Set the collar color of this wolf",
                new SimpleWorker(new int[]{0x8F, 0x73}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        DyeColor b = v.pop(DyeColor.values());
                        Wolf x = v.peek(Wolf.class);
                        x.setCollarColor(b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETWOLFSITTING",
                "SETWOLFSITTING >WOLFSIT SETWOLFSIT",
                "Wolf Boolean",
                "Wolf",
                "wolf",
                "Sets if this wolf is sitting Will remove any path that the wolf was following beforehand",
                new SimpleWorker(new int[]{0x8F, 0x74}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Wolf x = v.peek(Wolf.class);
                        x.setSitting(b);
                    }
                }
        ));


        /** Zombie **/


        VSCompiler.addRule(new SimpleCompileRule(
                "ZOMBIEBABY",
                "ZOMBIEBABY",
                "Zombie",
                "Boolean",
                "zombie",
                "Gets whether the zombie is a baby",
                new SimpleWorker(new int[]{0x8F, 0x75}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Zombie z = v.pop(Zombie.class);
                        v.push(z.isBaby());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ZOMBIEVILLAGER",
                "ZOMBIEVILLAGER",
                "Zombie",
                "Boolean",
                "zombie",
                "Gets whether the zombie is a villager",
                new SimpleWorker(new int[]{0x8F, 0x76}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Zombie z = v.pop(Zombie.class);
                        v.push(z.isVillager());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETZOMBIEBABY",
                "SETZOMBIEBABY >ZBABY",
                "Zombie Boolean",
                "Zombie",
                "zombie",
                "Sets whether the zombie is a baby",
                new SimpleWorker(new int[]{0x8F, 0x77}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean status = v.pop(Boolean.class);
                        Zombie z = v.peek(Zombie.class);
                        z.setBaby(status);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETZOMBIEVILLAGER",
                "SETZOMBIEVILLAGER >ZVILLAGER",
                "Zombie Boolean",
                "Zombie",
                "zombie",
                "Sets whether the zombie is a villager",
                new SimpleWorker(new int[]{0x8F, 0x78}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean status = v.pop(Boolean.class);
                        Zombie z = v.peek(Zombie.class);
                        z.setVillager(status);
                    }
                }
        ));


    }


}
