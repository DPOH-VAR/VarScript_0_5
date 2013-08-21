package org.dpohvar.varscript.vs.init;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.dpohvar.varscript.converter.ConvertException;
import org.dpohvar.varscript.vs.Context;
import org.dpohvar.varscript.vs.SimpleWorker;
import org.dpohvar.varscript.vs.Thread;
import org.dpohvar.varscript.vs.ThreadRunner;
import org.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import org.dpohvar.varscript.vs.compiler.VSCompiler;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitEntity {
    public static void load() {
        VSCompiler.addRule(new SimpleCompileRule(
                "SETVELOCITY",
                "SETVELOCITY SETVEL >VEL",
                "Entity Vector",
                "Entity",
                "entity",
                "Set velocity for entity",
                new SimpleWorker(new int[]{0x50}) {
                    @Override
                    public void run(ThreadRunner r, org.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        Vector s = v.pop(Vector.class);
                        Entity e = v.peek(Entity.class);
                        e.setVelocity(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "TELEPORT",
                "TELEPORT TP",
                "Entity(A) Location(B)",
                "Entity(A)",
                "entity",
                "Teleport entity A to location B, save pitch and yaw",
                new SimpleWorker(new int[]{0x51}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location s = v.pop(Location.class).clone();
                        Entity e = v.peek(Entity.class);
                        Location el = e.getLocation();
                        s.setPitch(el.getPitch());
                        s.setYaw(el.getYaw());
                        e.teleport(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPASSENGER",
                "SETPASSENGER SETPAS >PAS",
                "Entity(A) Entity(B)",
                "Entity(A)",
                "entity",
                "Pas entity B to A",
                new SimpleWorker(new int[]{0x52}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity s = v.pop(Entity.class);
                        Entity e = v.peek(Entity.class);
                        e.setPassenger(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFIRE",
                "SETFIRE >FIRE",
                "Entity Integer(Ticks)",
                "Entity",
                "entity",
                "Burn entity",
                new SimpleWorker(new int[]{0x53}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer s = v.pop(Integer.class);
                        Entity e = v.peek(Entity.class);
                        e.setFireTicks(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISDEAD",
                "ISDEAD _DEAD",
                "Entity",
                "Boolean",
                "entity",
                "Returns true if entity is dead",
                new SimpleWorker(new int[]{0x54}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e.isDead());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISALIVE",
                "ISALIVE _ALIVE",
                "Entity",
                "Boolean",
                "entity",
                "Returns true if entity is alive",
                new SimpleWorker(new int[]{0x55}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(!e.isDead());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETFALLDISTANCE",
                "SETFALLDISTANCE SETFALL >FALL",
                "Entity Float",
                "Entity",
                "entity",
                "Set fall distance for entity",
                new SimpleWorker(new int[]{0x56}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Float e2 = v.pop(Float.class);
                        Entity e = v.peek(Entity.class);
                        e.setFallDistance(e2);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FIRE",
                "FIRE",
                "Entity",
                "Integer",
                "entity",
                "Get fire ticks of entity",
                new SimpleWorker(new int[]{0x57}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e.getFireTicks());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EJECT",
                "EJECT EJC",
                "Entity",
                "Entity",
                "entity",
                "Eject entity",
                new SimpleWorker(new int[]{0x58}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.peek(Entity.class);
                        e.eject();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PASSENGER",
                "PASSENGER PAS",
                "Entity(A)",
                "Entity(B)",
                "entity",
                "Put to stack passenger of entity A",
                new SimpleWorker(new int[]{0x59}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e.getPassenger());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ENTITYID",
                "ENTITYID ENTID",
                "Entity",
                "Integer",
                "entity",
                "Put to stack passenger of entity A",
                new SimpleWorker(new int[]{0x5A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e.getEntityId());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "FALLDISTANCE",
                "FALLDISTANCE FALLDIS FALL",
                "Entity",
                "Float",
                "entity",
                "Get fall distance of entity",
                new SimpleWorker(new int[]{0x5B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e.getFallDistance());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SPAWNENTITY",
                "SPAWNENTITY SPAWNMOB SPE SPM",
                "Location #EntityType",
                "Entity",
                "entity",
                "Spawn mob",
                new SimpleWorker(new int[]{0x5C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        EntityType s = v.pop(EntityType.values());
                        Location l = v.pop(Location.class);
                        World w = l.getWorld();
                        v.push(w.spawnEntity(l, s));
                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "VELOCITY",
                "VELOCITY VEL",
                "Entity",
                "Vector",
                "entity",
                "Get velocity",
                new SimpleWorker(new int[]{0x5D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e.getVelocity());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "THROW",
                "THROW TH ADDVELOCITY",
                "Entity Vector",
                "Entity",
                "entity",
                "Throw entity",
                new SimpleWorker(new int[]{0x5E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector e2 = v.pop(Vector.class);
                        Entity e = v.peek(Entity.class);
                        e.setVelocity(e.getVelocity().add(e2));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VEHICLE",
                "VEHICLE",
                "Entity",
                "Entity",
                "entity",
                "Get vehicle",
                new SimpleWorker(new int[]{0x5F, 0x00}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e.getVehicle());

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ENTITIES",
                "ENTITIES E",
                "",
                "ArrayList",
                "entity",
                "Get all entities",
                new SimpleWorker(new int[]{0x5F, 0x01}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ArrayList<Entity> ar = new ArrayList<Entity>();
                        for (World w : Bukkit.getWorlds()) {
                            for (Entity e : w.getEntities()) {
                                ar.add(e);
                            }
                        }
                        v.push(ar);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PLAYERS",
                "PLAYERS P",
                "",
                "ArrayList",
                "entity player",
                "Get all players",
                new SimpleWorker(new int[]{0x5F, 0x02}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(Arrays.asList(Bukkit.getOnlinePlayers()));

                    }
                }
        ));


        VSCompiler.addRule(new SimpleCompileRule(
                "MOBS",
                "MOBS M LIVINGS",
                "",
                "ArrayList",
                "entity living",
                "Get all living entities",
                new SimpleWorker(new int[]{0x5F, 0x03}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ArrayList<Entity> ar = new ArrayList<Entity>();
                        for (World w : Bukkit.getWorlds()) {
                            for (Entity e : w.getEntities()) {
                                if (e instanceof LivingEntity && !(e instanceof Player)) ar.add(e);
                            }
                        }
                        v.push(ar);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ITEMS",
                "ITEMS T",
                "",
                "ArrayList",
                "entity",
                "Get all items",
                new SimpleWorker(new int[]{0x5F, 0x04}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ArrayList<Entity> ar = new ArrayList<Entity>();
                        for (World w : Bukkit.getWorlds()) {
                            for (Entity e : w.getEntities()) {
                                if (e instanceof Item) ar.add(e);
                            }
                        }
                        v.push(ar);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "VEHICLES",
                "VEHICLES VEHS",
                "",
                "ArrayList",
                "entity",
                "Get all vehicles",
                new SimpleWorker(new int[]{0x5F, 0x05}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ArrayList<Entity> ar = new ArrayList<Entity>();
                        for (World w : Bukkit.getWorlds()) {
                            for (Entity e : w.getEntities()) {
                                if (e instanceof Vehicle) ar.add(e);
                            }
                        }
                        v.push(ar);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISPLAYER",
                "ISPLAYER PLAYER",
                "Entity",
                "Boolean",
                "entity player",
                "Put to stack true if entity instanceof player",
                new SimpleWorker(new int[]{0x5F, 0x06}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e instanceof Player);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISLIVING",
                "ISLIVING _LIVING",
                "Entity",
                "Boolean",
                "entity living",
                "Put to stack true if entity instanceof living",
                new SimpleWorker(new int[]{0x5F, 0x07}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e instanceof LivingEntity);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISITEM",
                "ISITEM ITEM",
                "Entity",
                "Boolean",
                "entity",
                "Put to stack true if entity instanceof item",
                new SimpleWorker(new int[]{0x5F, 0x08}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e instanceof Item);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ISVEHICLE",
                "ISVEHICLE VEHICLE",
                "Entity",
                "Boolean",
                "entity",
                "Put to stack true if entity instanceof vehicle",
                new SimpleWorker(new int[]{0x5F, 0x09}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e instanceof Vehicle);

                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "REMOVE",
                "REMOVE RM",
                "Entity",
                "",
                "entity",
                "Remove entity",
                new SimpleWorker(new int[]{0x5F, 0x0A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        if (!(e instanceof Player)) e.remove();

                    }
                }
        ));

        // FREEID (0x5F,0x0B)

        VSCompiler.addRule(new SimpleCompileRule(
                "SPAWNCLASS",
                "SPAWNCLASS SPC",
                "Location Class",
                "Entity",
                "entity",
                "Spawn entity by class",
                new SimpleWorker(new int[]{0x5F, 0x0C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Class c = v.pop(Class.class);
                        Location l = v.pop(Location.class);
                        World w = l.getWorld();
                        if (Entity.class.isAssignableFrom(c)) {
                            v.push(w.<Entity>spawn(l, c));
                        } else {
                            v.push(null);
                        }
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "THROWTO",
                "THROWTO THTO",
                "Entity location",
                "Entity",
                "entity",
                "Throw entity to location",
                new SimpleWorker(new int[]{0x5F, 0x0D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location l = v.pop(Location.class);
                        Entity e = v.peek(Entity.class);
                        Vector w = e.getLocation().toVector().add(l.toVector().multiply(-1)).multiply(0.3);
                        e.setVelocity(w);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ENTITYTYPE",
                "ENTITYTYPE ENTTYPE",
                "Entity",
                "String",
                "entity",
                "Get type of entity",
                new SimpleWorker(new int[]{0x5F, 0x0E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(e.getType());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "ENTITYEFFECT",
                "ENTITYEFFECT EEF",
                "Entity #EntityEffect",
                "Entity",
                "entity",
                "Play effect for entity\nExample: ME \"HURT\" ENTITYEFFECT",
                new SimpleWorker(new int[]{0x5F, 0x0F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        EntityEffect ef = v.pop(EntityEffect.values());
                        v.peek(Entity.class).playEffect(ef);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SPAWNFALLINGBLOCK",
                "SPAWNFALLINGBLOCK SFB",
                "Location ItemStack",
                "FallingBlock",
                "entity",
                "Spawn new falling block",
                new SimpleWorker(new int[]{0x5F, 0x10}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        ItemStack item = v.pop(ItemStack.class);
                        Location l = v.pop(Location.class);
                        v.push(
                                l.getWorld().spawnFallingBlock(l, item.getTypeId(), item.getData().getData())
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "NEAR",
                "NEAR",
                "Entity(A) Double(length)",
                "List(Entity)",
                "entity",
                "Get all entities nearby to A",
                new SimpleWorker(new int[]{0x5F, 0x11}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double val = v.pop(Double.class);
                        v.push(
                                v.pop(Entity.class).getNearbyEntities(val, val, val)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TELEPORTTO",
                "TELEPORTTO TPTO",
                "Entity(A) Location(B)",
                "Entity",
                "entity",
                "Teleport entity A to location B",
                new SimpleWorker(new int[]{0x5F, 0x12}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Location s = v.pop(Location.class);
                        Entity e = v.peek(Entity.class);
                        e.teleport(s);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SHIFT",
                "SHIFT",
                "Entity Vector",
                "Entity",
                "entity",
                "Teleport entity to shifted location",
                new SimpleWorker(new int[]{0x5F, 0x13}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Vector s = v.pop(Vector.class);
                        Entity e = v.peek(Entity.class);
                        e.teleport(e.getLocation().add(s));
                    }
                }
        ));


    }


}
