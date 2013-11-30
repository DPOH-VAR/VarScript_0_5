/**
 * metaclasses v1.0.1
 * module for VarScript (Bukkit plugin)
 * Author: DPOH-VAR
 * (c) 2013
 */


import me.dpohvar.varscript.se.trigger.SERunnable
import me.dpohvar.varscript.utils.region.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.*
import org.bukkit.event.EventPriority
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

import java.lang.reflect.Method

String cb = org.bukkit.Bukkit.getServer().class.name.split("\\.")[3];
String nms = org.bukkit.Bukkit.getServer().getHandle().class.name.split("\\.")[3];
Class classCraftItemStack = Class.forName "org.bukkit.craftbukkit." + cb + ".inventory.CraftItemStack";
Class classNMSItemStack = Class.forName "net.minecraft.server." + cb + ".ItemStack";
Random random = new Random()

Script.metaClass.eval = { String code ->
    VarscriptRuntime.runScript(delegate.caller, code, 'groovy', null)
}
Script.metaClass.eval = { String code, String lang ->
    VarscriptRuntime.runScript(delegate.caller, code, lang, null)
}
Script.metaClass.script = { String filename ->
    VarscriptRuntime.runFile(delegate.caller, filename, 'groovy', null)
}
Script.metaClass.script = { String filename, String lang ->
    VarscriptRuntime.runFile(delegate.caller, filename, lang, null)
}
Script.metaClass.vector = { double x, double y, double z ->
    return new Vector(x, y, z)
}
Script.metaClass.up = Script.metaClass.vector = { double y ->
    new Vector(0, y, 0)
}
Script.metaClass.down = { double y ->
    new Vector(0, -y, 0)
}
Script.metaClass.vrnd = {->
    double ang1 = Math.PI * 2 * random.nextDouble()
    double ang2 = Math.PI * random.nextDouble()
    new Vector(Math.sin(ang1), Math.cos(ang1), Math.cos(ang2)).normalize()
}
Script.metaClass.player = { String name ->
    Bukkit.getPlayer(name)
}
Script.metaClass.loc = { double x, double y, double z, String world ->
    return new Location(Bukkit.getWorld(world), x, y, z)
}
Script.metaClass.print = { def some ->
    delegate.program.caller.send(some);
}
Script.metaClass.getPlayers = {->
    return Bukkit.getOnlinePlayers() as List;
}
Script.metaClass.getWorlds = {->
    return Server.worlds as List
}
Script.metaClass.getEntities = {->
    List<Entity> result = new ArrayList<Entity>()
    for (w in Server.worlds) result += w.entities;
    return result;
}
Script.metaClass.getLiving = {->
    List<LivingEntity> result = new ArrayList<LivingEntity>()
    for (w in Server.worlds)
        for (e in w.entities)
            if (e instanceof LivingEntity)
                result << e
    return result;
}
Script.metaClass.getMobs = {->
    List<LivingEntity> result = new ArrayList<LivingEntity>()
    for (w in Server.worlds)
        for (e in w.entities)
            if (e instanceof LivingEntity)
                if (!(e instanceof Player))
                    result << e
    return result;
}
Script.metaClass.getItems = {->
    List<org.bukkit.entity.Item> result = new ArrayList<org.bukkit.entity.Item>()
    for (w in Server.worlds)
        for (e in w.entities)
            if (e instanceof Item)
                result << e
    return result;
}
Script.metaClass.getTarb = {->
    delegate.me.getTargetBlock(null, 16);
}

Script.metaClass.item = { int id ->
    classCraftItemStack.newInstance(classNMSItemStack.newInstance(id, 1, 0))
}
Script.metaClass.item = { int id, int type ->
    classCraftItemStack.newInstance(classNMSItemStack.newInstance(id, 1, type))
}
Script.metaClass.item = { int id, int type, int amount ->
    classCraftItemStack.newInstance(classNMSItemStack.newInstance(id, amount, type))
}
/**
 * regions
 * */
Script.metaClass.box = { Location a, Location b ->
    new CubeRegion(a, b)
}
Script.metaClass.boxArea = { Location a, Location b ->
    new CubeArea(a, b)
}
/**
 * Register events easier!
 * trigger = delay(ticks) { ... }* trigger = timer(ticks) { ... }* trigger = timeout(seconds) { ... }* trigger = interval(seconds) { ... }* */
Script.metaClass.onTicks =
    Script.metaClass.delay = { long time, Closure closure ->
        return delegate.program.onTicks(closure, time);
    }
Script.metaClass.everyTicks =
    Script.metaClass.timer = { long time, Closure closure ->
        return delegate.program.everyTicks(closure, time);
    }
Script.metaClass.onTimeout =
    Script.metaClass.timeout = { long time, Closure closure ->
        return delegate.program.onTimeout(closure, time);
    }
Script.metaClass.everyTimeout =
    Script.metaClass.interval = { long time, Closure closure ->
        return delegate.program.onTimeout(closure, time);
    }

/**
 * trigger = register { Event event -> ... }* trigger = register (priority) { Event event -> ... }* trigger = register (String eventType) { ... }* trigger = register (String eventType,priority) { ... }* trigger = listen { Event event -> ... }* trigger = listen (priority) { Event event -> ... }* trigger = listen (String eventType) { ... }* trigger = listen (String eventType,priority) { ... }*
 * */
Script.metaClass.register = { final Closure closure ->
    Class[] classes = closure.getParameterTypes();
    assert classes.length == 1
    Class clazz = classes[0]
    assert org.bukkit.event.Event.isAssignableFrom(clazz);
    SERunnable runnable = new SERunnable() {
        public void run(Object event) {
            closure(event);
        }
    }
    return delegate.program.onEvent(runnable, clazz, EventPriority.NORMAL, false);
}
Script.metaClass.register = { Object priority, final Closure closure ->
    Class[] classes = closure.getParameterTypes();
    assert classes.length == 1
    Class clazz = classes[0]
    assert org.bukkit.event.Event.isAssignableFrom(clazz);
    SERunnable runnable = new SERunnable() {
        public void run(Object event) {
            closure(event);
        }
    }
    return delegate.program.onEvent(runnable, clazz, priority, false);
}
Script.metaClass.listen = { final Closure closure ->
    Class[] classes = closure.getParameterTypes();
    assert classes.length == 1
    Class clazz = classes[0]
    assert org.bukkit.event.Event.isAssignableFrom(clazz);
    SERunnable runnable = new SERunnable() {
        public void run(Object event) {
            closure(event);
        }
    }
    return delegate.program.onEvent(runnable, clazz, EventPriority.NORMAL, true);
}
Script.metaClass.listen = { Object priority, final Closure closure ->
    Class[] classes = closure.getParameterTypes();
    assert classes.length == 1
    Class clazz = classes[0]
    assert org.bukkit.event.Event.isAssignableFrom(clazz);
    SERunnable runnable = new SERunnable() {
        public void run(Object event) {
            closure(event);
        }
    }
    return delegate.program.onEvent(runnable, clazz, priority, true);
}
Script.metaClass.register = { String type, final Closure closure ->
    SERunnable runnable = new SERunnable() {
        public void run(Object event) {
            closure(event);
        }
    }
    return delegate.program.onEvent(runnable, type, EventPriority.NORMAL, false);
}
Script.metaClass.listen = { String type, final Closure closure ->
    SERunnable runnable = new SERunnable() {
        public void run(Object event) {
            closure(event);
        }
    }
    return delegate.program.onEvent(runnable, type, EventPriority.NORMAL, true);
}
Script.metaClass.register = { String type, Object priority, final Closure closure ->
    SERunnable runnable = new SERunnable() {
        public void run(Object event) {
            closure(event);
        }
    }
    return delegate.program.onEvent(runnable, type, priority, false);
}
Script.metaClass.listen = { String type, Object priority, final Closure closure ->
    SERunnable runnable = new SERunnable() {
        public void run(Object event) {
            closure(event);
        }
    }
    return delegate.program.onEvent(runnable, type, priority, true);
}
/**
 * witn Random
 * */
Script.metaClass.random = Script.metaClass.rnd = {->
    return random.nextDouble()
}
Script.metaClass.random = Script.metaClass.rnd = { int val ->
    return random.nextInt(val)
}
Script.metaClass.random = Script.metaClass.rnd = { double val ->
    return random.nextDouble() * val
}
Script.metaClass.random = Script.metaClass.rnd = { int start, int end ->
    if (start == end) return start
    if (start > end) (start, end) = [end, start]
    return random.nextInt(end - start) + start
}
Script.metaClass.random = Script.metaClass.rnd = { double start, double end ->
    return random.nextDouble() * (end - start) + start
}
/**
 * witn arrays
 * */
Collection.metaClass.getRandom = Collection.metaClass.getRnd = {->
    int size = delegate.size()
    if (!size) return null
    int pos = random.nextInt size
    def iter = delegate.iterator()
    pos.times { iter.next() }
    return iter.next()
}
List.metaClass.getRandom = List.metaClass.getRnd = {->
    int size = delegate.size()
    if (!size) return null
    return delegate[random.nextInt(size)]
}
Collection.metaClass.popRandom = Collection.metaClass.popRnd = {->
    int size = delegate.size()
    if (!size) return null
    int pos = random.nextInt size
    def iter = delegate.iterator()
    pos.times { iter.next() }
    def current = iter.next()
    delegate.remove current
    return current
}
List.metaClass.popRandom = List.metaClass.popRnd = {->
    int size = delegate.size()
    if (!size) return null
    return delegate.remove(random.nextInt(size))
}
Collection.metaClass.without = { e ->
    delegate.remove(e);
    return delegate
}

/**
 * Enum metaclasses
 *
 *
 * */
Enum.metaClass.getName = {->
    delegate.name()
}
Enum.metaClass.plus = { String s ->
    (delegate as String).concat(s)
}

/**
 * -"playerName"
 * +"formattedString"
 * "world".w
 */
String.metaClass.getP = String.metaClass.negative = {->
    return Bukkit.getPlayer(delegate);
}
String.metaClass.getW = String.metaClass.bitwiseNegate = {->
    return Bukkit.getWorld(delegate);
}
String.metaClass.getF = String.metaClass.positive = {->
    ChatColor.translateAlternateColorCodes('&' as char, delegate)
}
/**
 * vector + vector
 * vector - vector
 * vector / double
 * */
Vector.metaClass.plus = { Vector v ->
    return delegate.clone().add(v);
}
Vector.metaClass.minus = { Vector v ->
    return delegate.clone().subtract(v);
}
Vector.metaClass.div = { double d ->
    return delegate.clone().multiply(1 / d);
}
Method vectorMulI = Vector.getMethod("multiply", int);
Method vectorMulF = Vector.getMethod("multiply", float);
Method vectorMulD = Vector.getMethod("multiply", double);
Vector.metaClass.multiply = { int d ->
    return vectorMulI.invoke(delegate.clone(), d as int);
}
Vector.metaClass.multiply = { float d ->
    return vectorMulF.invoke(delegate.clone(), d as float);
}
Vector.metaClass.multiply = { double d ->
    return vectorMulD.invoke(delegate.clone(), d as double);
}
Vector.metaClass.negative = {->
    return delegate.clone().multiply(-1);
}
Vector.metaClass.bitwiseNegate = {->
    return delegate.clone().normalize();
}
Vector.metaClass.mod = { double d ->
    return delegate.clone().normalize().multiply(d);
}
/**
 * vector.len
 * vector.len = newlength
 * */
Vector.metaClass.getLen = {->
    return delegate.length();
}
Vector.metaClass.setLen = { double len ->
    return vectorMulD.invoke(delegate.normalize(), len);
}

/**
 * location + vector = location;
 * location - vector = location;
 * location - location = vector;
 * */
Location.metaClass.plus = { Vector v ->
    return delegate.clone().add(v);
}
Location.metaClass.minus = { Vector v ->
    return delegate.clone().subtract(v);
}
Location.metaClass.minus = { Location v ->
    if (delegate.world != v.world) return null;
    double x = delegate.x - v.x;
    double y = delegate.y - v.y;
    double z = delegate.z - v.z;
    return new Vector(x, y, z);
}
Location.metaClass.multiply = { int t ->
    assert false, "can't multiply location";
}
Location.metaClass.multiply = { float t ->
    assert false, "can't multiply location";
}
Location.metaClass.multiply = { double t ->
    assert false, "can't multiply location";
}
Method locationGetBlock = Location.getMethod("getBlock");
Location.metaClass.getBl =
    Location.metaClass.getBlock = {->
        locationGetBlock.invoke(delegate)
    }

/**
 * block + vector
 * block - vector
 * */
Block.metaClass.plus = { Vector v ->
    return delegate.location.add(v).block;
}
Block.metaClass.minus = { Vector v ->
    return delegate.location.subtract(v).block;
}
Block.metaClass.getBl =
    Block.metaClass.getBlock = {->
        delegate
    }
/**
 * Entity.throw(double x,double y,double z)
 * add velocity to entity
 * */
Entity.metaClass.throw = Entity.metaClass.th = { double x, double y, double z ->
    Vector v = delegate.velocity
    v.x += x
    v.y += y
    v.z += z
    delegate.velocity = v
    return delegate
}
/**
 * Entity.throw(double y)
 * add velocity to entity by Y axis
 * */
Entity.metaClass.throw = Entity.metaClass.th = { double y ->
    Vector v = delegate.velocity
    v.y += y
    delegate.velocity = v
    return delegate
}
/**
 * Entity.throw(Vector v)
 * add velocity to entity
 * */
Entity.metaClass.throw = Entity.metaClass.th = { Vector v ->
    delegate.velocity = delegate.velocity.add(v)
    return delegate
}
/**
 * Entity.tp(Location dst)
 * teleport entity to location
 * */
Entity.metaClass.tp = Entity.metaClass.rightShift = { Location dst ->
    Location loc = delegate.getLocation();
    loc.x = dst.x
    loc.y = dst.y
    loc.z = dst.z
    loc.world = dst.world
    delegate.teleport(loc)
    return delegate;
}
/**
 * Entity.tp(Entity e)
 * teleport entity to entity
 * */
Entity.metaClass.tp = Entity.metaClass.rightShift = { Entity e ->
    Location loc = delegate.getLocation();
    Location dst = e.getLocation();
    loc.x = dst.x
    loc.y = dst.y
    loc.z = dst.z
    loc.world = dst.world
    delegate.teleport(loc)
    return delegate;
}

/**
 * Entity.tp(Block e)
 * teleport entity to entity
 * */
Entity.metaClass.tp = Entity.metaClass.rightShift = { Block e ->
    Location loc = delegate.getLocation();
    Location dst = e.getLocation();
    loc.x = dst.x
    loc.y = dst.y
    loc.z = dst.z
    loc.world = dst.world
    delegate.teleport(loc)
    return delegate;
}

/**
 * Entity.tpto(Location dst)
 * teleport entity to location
 * */
Entity.metaClass.tpto = { Location dst ->
    delegate.teleport(dst)
    return delegate;
}

/**
 * Entity.tpto(Entity e)
 * teleport entity to entity
 * */
Entity.metaClass.tpto = { Entity e ->
    delegate.teleport(e)
    return delegate;
}

/**
 * Entity.tpto(Block b)
 * teleport entity to entity
 * */
Entity.metaClass.tpto = { Block b ->
    delegate.teleport(b.location)
    return delegate;
}
/**
 * Entity.shift(double x,double y,double z)
 * shift entity to x,y,z
 * */
Entity.metaClass.shift = { double x, double y, double z ->
    Location l = delegate.getLocation();
    l.x += x;
    l.y += y;
    l.z += z;
    delegate.teleport(l);
    return delegate;
}

/**
 * Entity.shift(Vector v)
 * shift entity to vector
 * */
Entity.metaClass.shift = { Vector v ->
    delegate.teleport(delegate.location.add(v));
    return delegate;
}
/**
 * Entity.shift(double y)
 * shift entity up
 * */
Entity.metaClass.shift = { double y ->
    Location l = delegate.getLocation();
    l.y += y;
    delegate.teleport(l);
    return delegate;
}
/**
 * Entity.loc = loc
 * */
Entity.metaClass.setLoc = { Location l ->
    delegate.teleport(l);
    return delegate;
}
/**
 * Entity.block
 * */
Entity.metaClass.getBl =
    Entity.metaClass.getBlock = {->
        delegate.location.getBlock();
    }
/**
 * Entity.vel
 * Entity.vel = vector
 * */
Entity.metaClass.getVel = {->
    delegate.getVelocity();
}
Entity.metaClass.setVel = { Vector v ->
    delegate.setVelocity(v);
}
Entity.metaClass.vel = { double x, double y, double z ->
    delegate.setVelocity(new Vector(x, y, z));
}
Entity.metaClass.setVel = { double y ->
    delegate.setVelocity(new Vector(0, y, 0));
}

/**
 * Damageable.kill()
 * */
Damageable.metaClass.kill = {->
    delegate.damage(delegate.getMaxHealth());
}
/**
 * Damageable.dmg(double damage)
 * */
Damageable.metaClass.dmg = { double damage ->
    delegate.damage(damage);
}
/**
 * Damageable.hp
 * */
Damageable.metaClass.getHp = {->
    delegate.getHealth();
}
Damageable.metaClass.setHp = { double health ->
    def max = delegate.getMaxHealth();
    if (max < health) health = max;
    delegate.setHealth(health);
}
/**
 * Damageable.maxhp
 * */
Damageable.metaClass.getMaxhp = {->
    delegate.getMaxHealth();
}
Damageable.metaClass.setMaxhp = { double health ->
    delegate.setMaxHealth(health);
}

/**
 * HumanEntity.gm
 * */
HumanEntity.metaClass.getGm = {->
    delegate.gameMode
}
HumanEntity.metaClass.setGm = { GameMode gm ->
    delegate.gameMode = gm;
}
HumanEntity.metaClass.setGm = { String gm ->
    delegate.gameMode = parseEnum(GameMode, gm);
}
HumanEntity.metaClass.setGm = { int gm ->
    delegate.gameMode = GameMode.getByValue(gm);
}
/**
 * HumanEntity.hand
 * */
HumanEntity.metaClass.getHand = {->
    delegate.itemInHand;
}
HumanEntity.metaClass.setHand = { ItemStack item ->
    delegate.itemInHand;
}

/**
 * InventoryHolder.give(int id[, int type[, int amount]])
 * InventoryHolder.give(ItemStack item)
 * give item to holder
 * */
InventoryHolder.metaClass.give = { int id ->
    delegate.getInventory().addItem(new ItemStack(id))
    return delegate;
}
InventoryHolder.metaClass.give = { int id, int type ->
    delegate.getInventory().addItem(new ItemStack(id, 1, type as byte))
    return delegate;
}
InventoryHolder.metaClass.give = { int id, int type, int amount ->
    delegate.getInventory().addItem(new ItemStack(id, amount, type as byte))
    return delegate;
}
InventoryHolder.metaClass.give = { ItemStack item ->
    delegate.getInventory().addItem(item)
    return delegate;
}

/**
 * Inventory.give(int id[, int type[, int amount]])
 * Inventory.give(ItemStack item)
 * give item to holder
 * */
Inventory.metaClass.give = { int id ->
    delegate.addItem(new ItemStack(id))
    return delegate;
}
Inventory.metaClass.give = { int id, int type ->
    delegate.addItem(new ItemStack(id, 0, type as byte))
    return delegate;
}
Inventory.metaClass.give = { int id, int type, int amount ->
    delegate.addItem(new ItemStack(id, amount, type as byte))
    return delegate;
}
Inventory.metaClass.give = { ItemStack item ->
    delegate.addItem(item)
    return delegate;
}

/**
 * with regions
 * */
Region.metaClass.getEntities = {->
    entities.findAll { delegate.hasLocation(it.location) }
}
Region.metaClass.getSolid = {->
    delegate.getSolidBlocks()
}
Region.metaClass.getAir = {->
    delegate.getBlocks(0)
}
Region.metaClass.getId = { int id ->
    delegate.getBlocks(id)
}
Region.metaClass.getEdge = {->
    delegate.getOutsideBlocks()
}
Region.metaClass.has = { Location l ->
    delegate.hasLocation(l)
}
Region.metaClass.has = { Entity e ->
    delegate.hasLocation(e.location)
}
Region.metaClass.has = { Block b ->
    delegate.hasLocation(b.location)
}

/////////////////////////////////////////////////////////////////////////////////
AsLocation(Location, { it })
AsLocation(Entity, { it.location })
AsLocation(Block, { it.location })

def AsLocation(Class clazz, Closure get) {

    clazz.metaClass.getLoc = {->
        get(delegate)
    }

    clazz.metaClass.ex =
        clazz.metaClass.explode = { float power ->
            Location l = get(delegate)
            l.world.createExplosion(l, power)
            delegate
        }

    clazz.metaClass.bolt = { /*true*/ ->
        Location l = get(delegate)
        l.world.strikeLightning(l)
        delegate
    }
    clazz.metaClass.bolt = { boolean damage ->
        Location l = get(delegate)
        if (damage) l.world.strikeLightning(l)
        else l.world.strikeLightningEffect(l)
        delegate
    }
    clazz.metaClass.tphere =
        clazz.metaClass.leftShift = { Entity e ->
            Location l = get(delegate)
            e.tpto(l);
            delegate
        }

    clazz.metaClass.getSpawn = {->
        get(delegate).world.getSpawnLocation()
    }

    clazz.metaClass.move = { double x, double y, double z ->
        Location l = get(delegate)
        l.clone().add(new Vector(x, y, z))
    }
    clazz.metaClass.move = { double y ->
        Location l = get(delegate)
        l.clone().add(new Vector(0, y, 0))
    }
    clazz.metaClass.move = { Vector v ->
        Location l = get(delegate)
        l.clone().add(v)
    }

    clazz.metaClass.spawn = { org.bukkit.entity.EntityType type ->
        Location l = get(delegate)
        l.world.spawnEntity(l, type)
    }
    clazz.metaClass.spawn = { Entity type ->
        Location l = get(delegate)
        l.world.spawn(l, type.class)
    }
    clazz.metaClass.spawn = { ItemStack item ->
        Location l = get(delegate)
        l.world.dropItem(l, item)
    }
    clazz.metaClass.spawn = { String type ->
        Location l = get(delegate)
        l.world.spawnEntity(l, parseEnum(org.bukkit.entity.EntityType, type))
    }

    clazz.metaClass.getTop = {->
        Location l = get(delegate)
        l.world.getHighestBlockAt(l)
    }

    clazz.metaClass.sphere = { double radius ->
        Location l = get(delegate)
        new SphereRegion(l, radius)
    }
    clazz.metaClass.sphereArea = { double radius ->
        Location l = get(delegate)
        new SphereArea(l, radius)
    }
    clazz.metaClass.box = { double radius ->
        Location l = get(delegate)
        new CubeRegion(l, radius, radius, radius)
    }
    clazz.metaClass.box = { double x, double y, double z ->
        Location l = get(delegate)
        new CubeRegion(l, x, y, z)
    }
    clazz.metaClass.boxArea = { double radius ->
        Location l = get(delegate)
        new CubeArea(l, radius, radius)
    }
    clazz.metaClass.boxArea = { double x, double z ->
        Location l = get(delegate)
        new CubeArea(l, x, z)
    }

}

def parseEnum(Class enumClass, String s) {
    try {
        return Enum.&valueOf(s)
    } catch (Throwable e) {}

    def temp = null
    s = s.toLowerCase()
    for (def example in enumClass.values()) {
        String e = example.toString().toLowerCase()
        if (e.equals(s)) return example
        if (temp == null && e.startsWith(s)) temp = example
    }
    return temp
}

// scala.settings().usejavacp().tryToSetFromPropertyValue("true")

return [name: "metaclasses", version: [1, 0, 1]]