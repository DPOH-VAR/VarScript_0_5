/**
 * NBTTools v2.0.0
 * module for VarScript (Bukkit plugin)
 * Author: DPOH-VAR
 * (c) 2013
 * */

package NBTTools_1_7

import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack

public class NBTUtils {
    @SuppressWarnings("all")
    public static String nms = org.bukkit.Bukkit.getServer().getHandle().class.name.split("\\.")[3]
    public static Class classNBTBase = Class.forName "net.minecraft.server." + nms + ".NBTBase"
    public static Class classNBTNumber = Class.forName "net.minecraft.server." + nms + ".NBTNumber"
    public static Class classNBTTagByte = Class.forName "net.minecraft.server." + nms + ".NBTTagByte"
    public static Class classNBTTagShort = Class.forName "net.minecraft.server." + nms + ".NBTTagShort"
    public static Class classNBTTagInt = Class.forName "net.minecraft.server." + nms + ".NBTTagInt"
    public static Class classNBTTagLong = Class.forName "net.minecraft.server." + nms + ".NBTTagLong"
    public static Class classNBTTagFloat = Class.forName "net.minecraft.server." + nms + ".NBTTagFloat"
    public static Class classNBTTagDouble = Class.forName "net.minecraft.server." + nms + ".NBTTagDouble"
    public static Class classNBTTagByteArray = Class.forName "net.minecraft.server." + nms + ".NBTTagByteArray"
    public static Class classNBTTagIntArray = Class.forName "net.minecraft.server." + nms + ".NBTTagIntArray"
    public static Class classNBTTagString = Class.forName "net.minecraft.server." + nms + ".NBTTagString"
    public static Class classNBTTagCompound = Class.forName "net.minecraft.server." + nms + ".NBTTagCompound"
    public static Class classNBTTagList = Class.forName "net.minecraft.server." + nms + ".NBTTagList"
    public
    static Class classNBTCompressedStreamTools = Class.forName "net.minecraft.server." + nms + ".NBTCompressedStreamTools"

    public static def getNBT(def object) {
        if (object == null) return null
        if (classNBTTagCompound.isInstance(object)) return new NBTCompound(object)
        if (classNBTTagList.isInstance(object)) return new NBTList(object)
        return object.@data
    }

    public static def wrapNBT(boolean object) {
        byte val = object ? 1 as byte : 0 as byte
        classNBTTagByte.newInstance val
    }

    public static def wrapNBT(byte object) {
        classNBTTagByte.newInstance object as byte
    }

    public static def wrapNBT(short object) {
        classNBTTagShort.newInstance object as short
    }

    public static def wrapNBT(int object) {
        classNBTTagInt.newInstance object as int
    }

    public static def wrapNBT(long object) {
        classNBTTagLong.newInstance object as long
    }

    public static def wrapNBT(float object) {
        classNBTTagFloat.newInstance object as float
    }

    public static def wrapNBT(double object) {
        classNBTTagDouble.newInstance object as double
    }

    public static def wrapNBT(String object) {
        classNBTTagString.newInstance object as String
    }

    public static def wrapNBT(byte[] object) {
        classNBTTagByteArray.newInstance object as byte[]
    }

    public static def wrapNBT(int[] object) {
        classNBTTagIntArray.newInstance object as int[]
    }

    public static def wrapNBT(Collection<?> list) {
        def nbtList = classNBTTagList.newInstance()
        list.each {
            if (classNBTBase.isInstance(it)) nbtList.add(it.clone())
            else nbtList.add wrapNBT(it)
        }
        return nbtList;
    }

    public static def wrapNBT(Map<?, ?> map) {
        def nbtCompound = classNBTTagCompound.newInstance()
        map.each { it ->
            if (classNBTBase.isInstance(it.value)) {
                nbtCompound.set it.key as String, it.value.clone()
            } else {
                nbtCompound.set it.key as String, wrapNBT(it.value)
            }
        }
        return nbtCompound;
    }

    public static NBTList nbt(Collection t) {
        return new NBTList(wrapNBT(t));
    }

    public static NBTCompound nbt(Map t) {
        return new NBTCompound(wrapNBT(t));
    }

    public static def nbt(def object, Closure handler) {
        def main = this.nbt(object)
        def temp = main.clone()
        def result = handler(temp)
        if (main != temp) this.nbt(object, temp)
        return result
    }

    public static def nbt(def object, Map val) {
        return this.nbt(object, new NBTCompound(this.wrapNBT(val)))
    }

    public static def nbt(def object, Collection val) {
        return this.nbt(object, new NBTList(this.wrapNBT(val)))
    }

    public static NBTCompound nbt(Entity e) {
        def basic = classNBTTagCompound.newInstance()
        e.handle.e(basic)
        return new NBTCompound(basic)
    }

    public static boolean nbt(Entity e, NBTCompound val) {
        e.handle.f(val.@handle)
        return true
    }

    public static NBTCompound nbt(Block b) {
        Object tile = b.world.getTileEntityAt(b.x, b.y, b.z)
        if (!tile) return null
        def basic = classNBTTagCompound.newInstance()
        tile.b basic
        NBTCompound result = new NBTCompound(basic)
        result.remove "x"
        result.remove "y"
        result.remove "z"
        return result
    }

    private static int maxDist = Bukkit.server.viewDistance * 32

    public static boolean nbt(Block b, NBTCompound val) {
        Object tile = b.world.getTileEntityAt(b.x, b.y, b.z)
        if (!tile) return false
        val = val.clone()
        if (!val.x) val.x = b.x
        if (!val.y) val.y = b.y
        if (!val.z) val.z = b.z
        tile.a(val.handle())
        def packet = tile.getUpdatePacket();
        b.world.players.findAll {
            it.location.distance(b.location) < NBTUtils.maxDist
        }.each {
            it.handle.playerConnection.sendPacket packet
        }
        return true
    }

    public static NBTCompound nbt(ItemStack item) {
        def tag = item.handle.tag
        if (tag == null) return null
        else new NBTCompound(tag.clone())
    }

    public static boolean nbt(ItemStack item, NBTCompound val) {
        try {
            item.handle.tag = val.clone().handle()
            return true
        } catch (ignored) {
            return false
        }
    }

    public static NBTCompound nbt(File file) {
        FileInputStream input = null
        try {
            input = new FileInputStream(file)
            def tag = classNBTCompressedStreamTools.a(input)
            if (tag == null) return null
            else return new NBTCompound(tag)
        } catch (e) {
            e.printStackTrace()
            try {
                input.close()
            } catch (ignored) {
            }
            return null
        }
    }

    public static boolean nbt(File file, NBTCompound val) {
        if (file == null || val == null) return false
        if (!file.isFile()) {
            File parent = file.parentFile
            if (parent == null) parent = new File('.')
            parent.mkdirs()
            if (!file.createNewFile()) return false
        }
        FileOutputStream output = null
        try {
            output = new FileOutputStream(file)
            classNBTCompressedStreamTools.a(val.@handle, output)
            return true
        } catch (e) {
            e.printStackTrace()
            try {
                output.close()
            } catch (ignored) {
            }
            return false
        }
    }

    private static File playersFolder = new File(org.bukkit.Bukkit.worlds[0].worldFolder, 'players')

    public static NBTCompound nbt(String name) {
        this.nbt(new File(playersFolder, name + '.dat'))
    }

    public static boolean nbt(String name, NBTCompound val) {
        this.nbt(new File(playersFolder, name + '.dat'), val)
    }

}


public class NBTCompound implements Map<String, Object> {
    def handle
    Map handleMap

    protected NBTCompound(def handle) {
        this.handle = handle
        this.handleMap = handle.map
    }

    public NBTCompound() {
        this.handle = NBTUtils.classNBTTagCompound.newInstance('')
        this.handleMap = this.handle.@map
    }

    public boolean equals(Object other) {
        if (other instanceof NBTCompound) handle.equals(other.@handle)
        else super.equals(other)
    }

    public NBTCompound clone() {
        return new NBTCompound(handle.clone())
    }

    public def handle() {
        return handle
    }

    public int size() {
        return handleMap.size()
    }

    public boolean isEmpty() {
        return handleMap.isEmpty()
    }

    public boolean containsKey(Object key) {
        return handleMap.containsKey(key as String);
    }

    public boolean containsValue(Object value) {
        if (NBTUtils.classNBTBase.isInstance(value)) return handleMap.containsValue(value);
        else return handleMap.containsValue(NBTUtils.wrapNBT(value))
    }

    public Object get(Object key) {
        return NBTUtils.getNBT(handleMap.get(key as String))
    }

    public Object put(String key, Object value) {
        def temp = get(key);
        if (NBTUtils.classNBTBase.isInstance(value)) handle.set(key as String, value.clone())
        else handle.set(key as String, NBTUtils.wrapNBT(value))
        return temp;
    }

    public Object put_NBTCompound(String key, Object value) {
        return put(key, value)
    }

    public Object remove(Object key) {
        def temp = get(key);
        handle.remove(key as String)
        return temp;
    }

    public void putAll(Map<? extends String, ? extends Object> m) {
        m.each {
            if (NBTUtils.classNBTBase.isInstance(it.value)) {
                handle.set(it.key as String, it.value.clone())
            } else {
                handle.set(it.key as String, NBTUtils.wrapNBT(it.value))
            }
        }
    }

    public void clear() {
        handleMap.clear()
    }

    private void clear_NBTCompound() {
        clear()
    }

    public Set keySet() {
        return handleMap.keySet()
    }

    public Collection values() {
        return null
    }

    public NBTEntrySet entrySet() {
        return new NBTEntrySet(handleMap.entrySet());
    }

    public String toString() {
        NBTEntryIterator i = entrySet().iterator();
        if (!i.hasNext()) return '{}';
        StringBuilder sb = new StringBuilder().append('{')
        for (; ;) {
            NBTEntry e = i.next();
            sb.append(e.key).append('=')
            if (e.value instanceof byte[]) {
                sb.append 'int[' + (e.value as byte[]).length + ']'
            } else if (e.value instanceof int[]) {
                sb.append 'byte[' + (e.value as int[]).length + ']'
            } else {
                sb.append e.value
            }
            if (!i.hasNext()) return sb.append('}').toString()
            sb.append(', ')
        }
    }

    private class NBTEntrySet extends AbstractSet<Map.Entry<String, Object>> {
        Set handleEntrySet;

        NBTEntrySet(Set entrySet) {
            this.handleEntrySet = entrySet;
        }

        public NBTEntryIterator iterator() {
            return new NBTEntryIterator(handleEntrySet.iterator())
        }

        public boolean contains(Object value) {
            if (NBTUtils.classNBTBase.isInstance(value)) return handleEntrySet.contains(value)
            else return handleEntrySet.contains(NBTUtils.wrapNBT(value))
        }

        public boolean remove(Object value) {
            if (NBTUtils.classNBTBase.isInstance(value)) return handleEntrySet.remove(value)
            else return handleEntrySet.remove(NBTUtils.wrapNBT(value))
        }

        public int size() {
            return handleMap.size();
        }

        public void clear() {
            clear_NBTCompound();
        }
    }

    private class NBTEntryIterator implements Iterator<Map.Entry<String, Object>> {
        Iterator handleEntryIterator

        NBTEntryIterator(Iterator entryIterator) {
            this.handleEntryIterator = entryIterator
        }

        public boolean hasNext() {
            return handleEntryIterator.hasNext()
        }

        public NBTEntry next() {
            return new NBTEntry(handleEntryIterator.next());
        }

        public void remove() {
            handleEntryIterator.remove()
        }
    }

    private class NBTEntry implements Map.Entry<String, Object> {
        private Map.Entry handleEntry

        public NBTEntry(Map.Entry entry) {
            this.handleEntry = entry
        }

        public String getKey() {
            return handleEntry.getKey()
        }

        public Object getValue() {
            return NBTUtils.getNBT(handleEntry.getValue())
        }

        public Object setValue(Object value) {
            return put_NBTCompound(handleEntry.key, value)
        }

        public String toString() {
            return getKey().toString() + "=" + getValue().toString();
        }
    }
}

public class NBTList implements List<Object> {
    def handle;
    List handleList;

    public NBTList(def handle) {
        this.handle = handle
        this.handleList = handle.list
    }

    public NBTList() {
        this.handle = NBTUtils.classNBTTagCompound.newInstance()
        this.handleList = this.handle.list
    }

    public NBTList clone() {
        return new NBTList(handle.clone());
    }

    public boolean equals(NBTList other) {
        return handle.equals(other.@handle)
    }

    public def handle() {
        return handle
    }

    public int size() {
        return handleList.size()
    }

    public boolean isEmpty() {
        return handleList.isEmpty();
    }

    public boolean contains(Object value) {
        if (NBTUtils.classNBTBase.isInstance(value)) return handleList.contains(value)
        else return handleList.contains(NBTUtils.wrapNBT(value))
    }

    public Iterator iterator() {
        return new NBTListIterator(handleList.listIterator(0))
    }

    public Object[] toArray() {
        handleList.collect { NBTUtils.getNBT(it) } as Object[]
    }

    public boolean add(Object value) {
        if (NBTUtils.classNBTBase.isInstance(value)) return handle.add(value.clone())
        else return handle.add(NBTUtils.wrapNBT(value))
    }

    public boolean remove(Object value) {
        if (NBTUtils.classNBTBase.isInstance(value)) return handleList.remove(value)
        else return handleList.remove(NBTUtils.wrapNBT(value))
    }

    public boolean containsAll(Collection<?> c) {
        for (def value in c) {
            if (NBTUtils.classNBTBase.isInstance(value)) {
                if (!handleList.contains(value)) return false;
            } else {
                if (!handleList.contains(NBTUtils.wrapNBT(value))) return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection c) {
        for (def t in c) {
            if (NBTUtils.classNBTBase.isInstance(t)) return handle.add(t.clone())
            else return handleList.contains(NBTUtils.wrapNBT(t))
        }
    }

    public boolean addAll(int index, Collection c) {
        c.each {
            if (NBTUtils.classNBTBase.isInstance(it)) handle.add(index++, it.clone())
            else handle.add(index++, NBTUtils.wrapNBT(it))
        }
        return true
    }

    public boolean removeAll(Collection<?> c) {
        c.each {
            if (NBTUtils.classNBTBase.isInstance(it)) handleList.remove(it)
            else handleList.remove NBTUtils.wrapNBT(it)
        }
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        ((List) handleList.clone()).each {
            if (!c.contains(it) && !c.contains(NBTUtils.wrapNBT(it))) handleList.remove(it)
        }
        return true;
    }

    public void clear() {
        handleList.clear();
    }

    public Object get(int index) {
        return NBTUtils.getNBT(handleList.get(index))
    }

    public Object set(int index, Object element) {
        def result
        if (NBTUtils.classNBTBase.isInstance(element)) result = handleList.set(index, element.clone())
        else result = handleList.set(index, NBTUtils.wrapNBT(element))
        if (result == null) return result
        return result
    }

    public void add(int index, Object element) {
        if (NBTUtils.classNBTBase.isInstance(element)) handleList.add(index, element.clone())
        else handleList.add(NBTUtils.wrapNBT(element))
    }

    public Object remove(int index) {
        def result = handleList.remove(index);
        return NBTUtils.getNBT(result)
    }

    public int indexOf(Object element) {
        if (NBTUtils.classNBTBase.isInstance(element)) return handleList.indexOf(element)
        else return handleList.indexOf(NBTUtils.wrapNBT(element))
    }

    public int lastIndexOf(Object element) {
        if (NBTUtils.classNBTBase.isInstance(element)) return handleList.indexOf(element)
        else return handleList.indexOf(NBTUtils.wrapNBT(element))
    }

    public ListIterator listIterator() {
        return new NBTListIterator(handleList.listIterator(0))
    }

    public ListIterator listIterator(int index) {
        return new NBTListIterator(handleList.listIterator(index))
    }

    public List subList(int fromIndex, int toIndex) {
        new ArrayList(this).subList(fromIndex, toIndex)
    }

    public Object[] toArray(Object[] a) {
        NBTListIterator itr = iterator();
        for (int i = 0; i < a.length; i++) {
            if (!itr.hasNext()) break;
            a[i] = itr.next();
        }
        return a;
    }

    public String toString() {
        NBTListIterator i = listIterator();
        if (!i.hasNext()) return "[]";
        StringBuilder sb = new StringBuilder().append('[');
        for (; ;) {
            def e = i.next();
            if (e instanceof int[]) sb.append("int[${(e as int[]).length}]");
            else if (e instanceof byte[]) sb.append("byte[${(e as byte[]).length}]");
            else sb.append(e);
            if (!i.hasNext()) return sb.append(']').toString();
            sb.append(", ");
        }
    }

    private class NBTListIterator implements ListIterator {
        ListIterator handleIterator

        public NBTListIterator(iterator) {
            this.handleIterator = iterator;
        }

        public boolean hasNext() {
            return handleIterator.hasNext()
        }

        public Object next() {
            return NBTUtils.getNBT(handleIterator.next())
        }

        public boolean hasPrevious() {
            return handleIterator.hasPrevious()
        }

        public Object previous() {
            return NBTUtils.getNBT(handleIterator.previous())
        }

        public int nextIndex() {
            handleIterator.nextIndex()
        }

        public int previousIndex() {
            handleIterator.previousIndex()
        }

        public void remove() {
            handleIterator.remove()
        }

        public void set(Object value) {
            if (NBTUtils.classNBTBase.isInstance(value)) handleIterator.set(value.clone())
            else handleIterator.set(NBTUtils.wrapNBT(value))
        }

        public void add(Object value) {
            if (NBTUtils.classNBTBase.isInstance(value)) handleIterator.add(value.clone())
            else handleIterator.add(NBTUtils.wrapNBT(value))
        }
    }
}

Script.metaClass.nbt = NBTUtils.&nbt;

return [
        name: 'NBTTools',
        version: [2, 1, 1],
        nbt: NBTUtils.&nbt,
        NBTCompound: NBTCompound,
        NBTList: NBTList,
        NBTUtils: NBTUtils,
]