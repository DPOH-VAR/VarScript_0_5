package me.dpohvar.varscript.vs.init;


import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 26.06.13
 * Time: 1:28
 */
public class InitLiving {
    public static void load() {
        VSCompiler.addRule(new SimpleCompileRule(
                "CUSTOMNAME",
                "CUSTOMNAME CNAME",
                "LivingEntity",
                "String",
                "entity living",
                "Put to stack custom name of entity",
                new SimpleWorker(new int[]{0x5F, 0x40}) {
                    @Override
                    public void run(ThreadRunner r, me.dpohvar.varscript.vs.Thread v, Context f, Void d) throws ConvertException {
                        LivingEntity e = v.pop(LivingEntity.class);
                        v.push(e.getCustomName());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LASTDAMAGE",
                "LASTDAMAGE LASTDMG",
                "LivingEntity",
                "Integer",
                "entity living",
                "Get last damage",
                new SimpleWorker(new int[]{0x5F, 0x41}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(LivingEntity.class).getLastDamage());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HEALTH",
                "HEALTH HP",
                "LivingEntity",
                "Integer",
                "entity living",
                "Get entity's hp",
                new SimpleWorker(new int[]{0x5F, 0x42}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Damageable e = v.pop(Damageable.class);
                        v.push(e.getHealth());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "MAXHEALTH",
                "MAXHEALTH MAXHP MXHP",
                "Damageable",
                "Integer",
                "entity living damage",
                "Get entity's max health",
                new SimpleWorker(new int[]{0x5F, 0x43}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Damageable e = v.pop(Damageable.class);
                        v.push(e.getMaxHealth());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCUSTOMNAME",
                "SETCUSTOMNAME >CNAME",
                "Entity String",
                "Entity",
                "entity living",
                "Set custom name for entity",
                new SimpleWorker(new int[]{0x5F, 0x44}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String e2 = v.pop(String.class);
                        Entity e = v.pop(Entity.class);
                        ((LivingEntity) e).setCustomName(e2);
                        v.push(e);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EYELOCATION",
                "EYE EYELOCATION",
                "LivingEntity",
                "Location",
                "entity living",
                "Get eye location",
                new SimpleWorker(new int[]{0x5F, 0x45}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        LivingEntity e = v.pop(LivingEntity.class);
                        v.push(e.getEyeLocation());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CUSTOMNAMEVISIBLE",
                "CUSTOMNAMEVISIBLE CNAMEVIS",
                "Entity",
                "Boolean",
                "entity living",
                "Returns true if custom name visible",
                new SimpleWorker(new int[]{0x5F, 0x46}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity) e).isCustomNameVisible());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETHEALTH",
                "SETHEALTH >HP SETHP",
                "Damageable Double(health)",
                "Damageable",
                "entity living damage",
                "Set health of damageable object",
                new SimpleWorker(new int[]{0x5F, 0x47}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double hp = v.pop(Double.class);
                        Damageable e = v.peek(Damageable.class);
                        e.setHealth(hp);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "POTIONEFFECTS",
                "POTIONEFFECTS PEFS",
                "Entity",
                "ArrayList",
                "living potion",
                "Get potion effects of entity",
                new SimpleWorker(new int[]{0x5F, 0x48}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity) e).getActivePotionEffects());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "MAXAIR",
                "MAXAIR MXAIR",
                "Entity",
                "Integer",
                "entity living",
                "Get max air of entity",
                new SimpleWorker(new int[]{0x5F, 0x49}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity) e).getMaximumAir());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETMAXAIR",
                "SETMAXAIR >MXAIR",
                "Entity Integer",
                "Entity",
                "entity living",
                "Set max air of entity",
                new SimpleWorker(new int[]{0x5F, 0x4A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity) e).setMaximumAir(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETREMAININGAIR",
                "SETREMAININGAIR >RMAIR",
                "Entity Integer",
                "Entity",
                "entity living",
                "Set remaining air of entity",
                new SimpleWorker(new int[]{0x5F, 0x4B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity) e).setRemainingAir(i);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "REMAININGAIR",
                "REMAININGAIR REMAIR",
                "Entity",
                "Integer",
                "entity living",
                "Get remaining air of entity",
                new SimpleWorker(new int[]{0x5F, 0x4C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity) e).getRemainingAir());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CANPICKUPLOOT",
                "CANPICKUPLOOT PKP",
                "Entity",
                "Boolean",
                "entity living",
                "Returns true if entity can pickup items",
                new SimpleWorker(new int[]{0x5F, 0x4D}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity) e).getCanPickupItems());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCANPICKUPLOOT",
                "SETCANPICKUPLOOT >PKP",
                "Entity Boolean",
                "Entity",
                "entity living",
                "Set can pickup loot for entity",
                new SimpleWorker(new int[]{0x5F, 0x4E}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity) e).setCanPickupItems(b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "PERSISTENCEREQUIRED",
                "PERSISTENCEREQUIRED PERREQ",
                "Entity",
                "Boolean",
                "entity living",
                "Returns false if mob must despawn when play far away",
                new SimpleWorker(new int[]{0x5F, 0x4F}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity) e).getRemoveWhenFarAway());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPERSISTENCEREQUIRED",
                "SETPERSISTENCEREQUIRED >PERREQ",
                "LivingEntity Boolean",
                "LivingEntity",
                "living",
                "Set entity persistence required",
                new SimpleWorker(new int[]{0x5F, 0x50}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        v.peek(LivingEntity.class).setRemoveWhenFarAway(b);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETMAXHEALTH",
                "SETMAXHEALTH SETMXHP >MXHP",
                "Entity Double",
                "Entity",
                "entity living damage",
                "Set max health for damageable object",
                new SimpleWorker(new int[]{0x5F, 0x51}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double hp = v.pop(Double.class);
                        Damageable e = v.peek(Damageable.class);
                        e.setMaxHealth(hp);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "RESETMAXHEALTH",
                "RESETMAXHEALTH RMXHP",
                "Entity",
                "Entity",
                "entity living damage",
                "Reset max health of damageable object",
                new SimpleWorker(new int[]{0x5F, 0x52}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Damageable e = v.peek(Damageable.class);
                        e.resetMaxHealth();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "POTIONEFFECTADD",
                "POTIONEFFECTADD PEFADD PEF+",
                "Entity PotionEffect",
                "Entity",
                "living potion",
                "Add potion effect to entity",
                new SimpleWorker(new int[]{0x5F, 0x53}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        PotionEffect pef = v.pop(PotionEffect.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity) e).addPotionEffect(pef);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "POTIONEFFECT",
                "POTIONEFFECT PEF",
                "Integer(duration) Integer(level) #PotionEffectType",
                "PotionEffect",
                "living potion",
                "Create potion effect",
                new SimpleWorker(new int[]{0x5F, 0x54}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        PotionEffectType type = v.pop(PotionEffectType.class);
                        Integer lvl = v.pop(Integer.class);
                        Integer dur = v.pop(Integer.class);
                        v.push(new PotionEffect(type, dur, lvl));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "REMOVEPOTIONEFFECT",
                "REMOVEPOTIONEFFECT RMPEF",
                "Entity PotionEffectType",
                "Entity",
                "living potion",
                "Remove potion effect from entity",
                new SimpleWorker(new int[]{0x5F, 0x55}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        PotionEffectType type = v.pop(PotionEffectType.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity) e).removePotionEffect(type);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CLEAREFFECTS",
                "CLEAREFFECTS CLRPEF",
                "LivingEntity",
                "LivingEntity",
                "living potion",
                "Clear all potion effects",
                new SimpleWorker(new int[]{0x5F, 0x56}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        LivingEntity e = v.peek(LivingEntity.class);
                        Collection<PotionEffect> eff = e.getActivePotionEffects();
                        for (PotionEffect pef : eff) e.removePotionEffect(pef.getType());
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
                new SimpleWorker(new int[]{0x5F, 0x57}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(LivingEntity.class).getEquipment());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "KILL",
                "KILL",
                "LivingEntity",
                "LivingEntity",
                "living",
                "Kill living entity",
                new SimpleWorker(new int[]{0x5F, 0x58}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Damageable t = v.peek(Damageable.class);
                        t.damage(Double.MAX_VALUE);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "KILLBY",
                "KILLBY",
                "Damageable(A) Entity(B)",
                "Damageable(A)",
                "living",
                "Kill living entity A by entity B",
                new SimpleWorker(new int[]{0x5F, 0x59}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.peek(Entity.class);
                        Damageable t = v.peek(Damageable.class);
                        t.damage(Double.MAX_VALUE, e);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETCUSTOMNAMEVISIBLE",
                "SETCUSTOMNAMEVISIBLE SETCNAMEVIS >CNAMEVIS",
                "LivingEntity Boolean",
                "LivingEntity",
                "entity living",
                "Set libing entity's custom name visible or not",
                new SimpleWorker(new int[]{0x5F, 0x5A}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean val = v.pop(Boolean.class);
                        v.peek(LivingEntity.class).setCustomNameVisible(val);
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "TARGETBLOCK",
                "TARGETBLOCK TARB",
                "LivingEntity",
                "Block",
                "living block",
                "Gets the block that the living entity has targeted",
                new SimpleWorker(new int[]{0x5F, 0x5B}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(
                                v.pop(LivingEntity.class).getTargetBlock(null, 64)
                        );
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "PROJECTILE",
                "PROJECTILE PRJ",
                "LivingEntity Object(projectile_name_or_class)",
                "Projectile",
                "living",
                "Launches a Projectile from the living entity",
                new SimpleWorker(new int[]{0x5F, 0x5C}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Object val = v.pop();
                        LivingEntity e = v.pop(LivingEntity.class);
                        Class prj = null;
                        if (val instanceof String) try {
                            EntityType type = v.convert(EntityType.values(), val);
                            prj = type.getEntityClass();
                        } catch (Exception ignored) {
                        }
                        if (prj == null) {
                            prj = v.convert(Class.class, val);
                        }
                        Object projectile = null;
                        try {
                            projectile = e.launchProjectile(prj);
                        } catch (Exception ignored) {
                        }
                        v.push(projectile);
                    }
                }
        ));


    }
}
