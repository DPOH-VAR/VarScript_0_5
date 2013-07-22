package me.dpohvar.varscript.vs.init;


import me.dpohvar.varscript.vs.Context;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Thread;
import me.dpohvar.varscript.vs.ThreadRunner;
import me.dpohvar.varscript.vs.compiler.SimpleCompileRule;
import me.dpohvar.varscript.vs.compiler.VSCompiler;
import me.dpohvar.varscript.converter.ConvertException;
import org.bukkit.entity.Entity;
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
    public static void load(){
        VSCompiler.addRule(new SimpleCompileRule(
                "CUSTOMNAME",
                "CUSTOMNAME CSTNAME",
                "Entity",
                "String",
                "entity living",
                "Put to stack custom name of entity",
                new SimpleWorker(new int[]{0x5F, 0x01}) {
                    @Override
                    public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity) e).getCustomName());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "LASTDAMAGE",
                "LASTDAMAGE LASTDMG",
                "Entity",
                "Integer",
                "entity living",
                "Get last damage",
                new SimpleWorker(new int[]{0x5F,0x02}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        v.push(v.pop(LivingEntity.class).getLastDamage());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "HEALTH",
                "HEALTH HP",
                "Entity",
                "Integer",
                "entity living",
                "Get entity's hp",
                new SimpleWorker(new int[]{0x5F,0x03}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity) e).getHealth());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "MAXHEALTH",
                "MAXHEALTH MAXHP",
                "Entity",
                "Integer",
                "entity living",
                "Get entity's max health",
                new SimpleWorker(new int[]{0x5F,0x04}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity) e).getMaxHealth());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETCUSTOMNAME",
                "SETCUSTOMNAME >CSTNAME",
                "Entity String",
                "Entity",
                "entity living",
                "Set custom name for entity",
                new SimpleWorker(new int[]{0x5F,0x05}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        String e2 = v.pop(String.class);
                        Entity e = v.pop(Entity.class);
                        ((LivingEntity)e).setCustomName(e2);
                        v.push(e);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "EYELOCATION",
                "EYE EYELOCATION",
                "Entity",
                "Location",
                "entity living",
                "Get eye location",
                new SimpleWorker(new int[]{0x5F,0x06}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        ((LivingEntity)e).getEyeLocation();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CUSTOMNAMEVISIBLE",
                "CUSTOMNAMEVISIBLE CSTNAMEVIS",
                "Entity",
                "Boolean",
                "entity living",
                "Returns true if custom name visible",
                new SimpleWorker(new int[]{0x5F,0x07}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity)e).isCustomNameVisible());
                    }
                }
        ));

        VSCompiler.addRule(new SimpleCompileRule(
                "SETHEALTH",
                "SETHEALTH >HP SETHP",
                "Entity Double",
                "Entity",
                "entity living",
                "Set health",
                new SimpleWorker(new int[]{0x5F,0x17}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double hp = v.pop(Double.class);
                        Entity e = v.pop(Entity.class);
                        ((LivingEntity)e).setHealth(hp);
                        v.push(e);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "POTIONEFFECTS",
                "POTIONEFFECTS PEFS",
                "Entity",
                "ArrayList",
                "entity living",
                "Get potion effects of entity",
                new SimpleWorker(new int[]{0x5F,0x18}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
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
                new SimpleWorker(new int[]{0x5F,0x19}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity)e).getMaximumAir());
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
                new SimpleWorker(new int[]{0x5F,0x1A}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity)e).setMaximumAir(i);
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
                new SimpleWorker(new int[]{0x5F,0x1B}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Integer i = v.pop(Integer.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity)e).setRemainingAir(i);
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
                new SimpleWorker(new int[]{0x5F,0x1C}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity)e).getRemainingAir());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CANPICKUPLOOT",
                "CANPICKUPLOOT PKP",
                "Entity",
                "Boolean",
                "entity living",
                "Returns true if entity can pickup loot",
                new SimpleWorker(new int[]{0x5F,0x1D}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity)e).getCanPickupItems());
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
                new SimpleWorker(new int[]{0x5F,0x1E}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity)e).setCanPickupItems(b);
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
                new SimpleWorker(new int[]{0x5F,0x1F}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.pop(Entity.class);
                        v.push(((LivingEntity)e).getRemoveWhenFarAway());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETPERSISTENCEREQUIRED",
                "SETPERSISTENCEREQUIRED >PERREQ",
                "Entity Boolean",
                "Entity",
                "entity living",
                "Set entity persistence required",
                new SimpleWorker(new int[]{0x5F,0x20}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Boolean b = v.pop(Boolean.class);
                        Entity e = v.peek(Entity.class);
                        v.push(((LivingEntity)e).getRemoveWhenFarAway());
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "SETMAXHEALTH",
                "SETMAXHEALTH SETMAXHP >MXHP",
                "Entity Double",
                "Entity",
                "entity living",
                "Set max hp for entity",
                new SimpleWorker(new int[]{0x5F,0x21}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Double hp = v.pop(Double.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity)e).setMaxHealth(hp);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "RESETMAXHEALTH",
                "RESETMAXHEALTH RMXHP",
                "Entity",
                "Entity",
                "entity living",
                "Reset max health of entity",
                new SimpleWorker(new int[]{0x5F,0x22}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity)e).resetMaxHealth();
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "ADDPOTIONEFFECT",
                "ADDPOTIONEFFECT PEF+",
                "Entity PotionEffect",
                "Entity",
                "entity living",
                "Add potion effect to entity",
                new SimpleWorker(new int[]{0x5F,0x23}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        PotionEffect pef = v.pop(PotionEffect.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity)e).addPotionEffect(pef);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "POTIONEFFECT",
                "POTIONEFFECT PEF",
                "Integer(duration) Integer(level) PotionEffectType",
                "PotionEffect",
                "entity living",
                "Create potion effect",
                new SimpleWorker(new int[]{0x5F,0x24}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        PotionEffectType type = v.pop(PotionEffectType.class);
                        Integer lvl = v.pop(Integer.class);
                        Integer dur = v.pop(Integer.class);
                        v.push(new PotionEffect(type,dur,lvl));
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "REMOVEPOTIONEFFECT",
                "REMOVEPOTIONEFFECT RMPEF",
                "Entity PotionEffectType",
                "Entity",
                "entity living",
                "Remove potion effect from entity",
                new SimpleWorker(new int[]{0x5F,0x25}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        PotionEffectType type = v.pop(PotionEffectType.class);
                        Entity e = v.peek(Entity.class);
                        ((LivingEntity)e).removePotionEffect(type);
                    }
                }
        ));
        VSCompiler.addRule(new SimpleCompileRule(
                "CLEAREFFECTS",
                "CLEAREFFECTS CLRPEF",
                "Entity",
                "Entity",
                "entity living",
                "Clear all potion effects",
                new SimpleWorker(new int[]{0x5F,0x26}){
                    @Override public void run(ThreadRunner r, Thread v, Context f, Void d) throws ConvertException {
                        Entity e = v.peek(Entity.class);
                        Collection<PotionEffect> eff = ((LivingEntity) e).getActivePotionEffects();
                        for(PotionEffect pef:eff) ((LivingEntity) e).removePotionEffect(pef.getType());
                    }
                }
        ));





    }
}
