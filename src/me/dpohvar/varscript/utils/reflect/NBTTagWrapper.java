package me.dpohvar.varscript.utils.reflect;

import me.dpohvar.powernbt.nbt.NBTBase;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;
import me.dpohvar.varscript.vs.Fieldable;
import me.dpohvar.varscript.vs.Runnable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.07.13
 * Time: 7:12
 */
public class NBTTagWrapper implements Fieldable {

    protected NBTBase tag;

    public NBTTagWrapper(NBTBase nbt){
        this.tag = nbt;
    }

    public NBTBase getTag(){
        return tag;
    }

    @Override public Set<String> getAllFields() {
        HashSet<String> names = new HashSet<String>();
        if(tag instanceof NBTTagList){
            int size=((NBTTagList) tag).size();
            for(int i=0;i<size;i++){
                names.add(String.valueOf(i));
            }
        }
        else if(tag instanceof NBTTagCompound){
            for(NBTBase base:(NBTTagCompound) tag){
                names.add(base.getName());
            }
        }
        return names;
    }

    @Override public Object getField(final String name) {
        if(tag instanceof NBTTagList){
            try{
                int pos = Integer.parseInt(name);
                NBTTagList list = (NBTTagList) tag;
                return list.get(pos);
            }catch (Exception ignored){
            }
        } else if(tag instanceof NBTTagCompound){
            NBTTagCompound compound = (NBTTagCompound) tag;
            if(compound.has(name)) return compound.get(name);
        }
        return null;
    }

    @Override public void setField(String key, Object value) {
        if(tag instanceof NBTTagList){
            int pos = Integer.parseInt(key);
            NBTTagList list = (NBTTagList) tag;
            list.set(pos,value);
        } else if(tag instanceof NBTTagCompound){
            NBTTagCompound compound = (NBTTagCompound) tag;
            compound.set(key, value);
        }
    }

    @Override public void removeField(String key) {
        if(tag instanceof NBTTagList){
            int pos = Integer.parseInt(key);
            NBTTagList list = (NBTTagList) tag;
            list.remove(pos);
        } else if(tag instanceof NBTTagCompound){
            NBTTagCompound compound = (NBTTagCompound) tag;
            compound.remove(key);
        }
    }

    @Override
    public boolean hasField(String key) {
        if(tag instanceof NBTTagList){
            try{
                int pos = Integer.parseInt(key);
                NBTTagList list = (NBTTagList) tag;
                return list.size()>pos;
            } catch (Exception ignored){
            }
        } else if(tag instanceof NBTTagCompound){
            NBTTagCompound compound = (NBTTagCompound) tag;
            return compound.has(key);
        }
        return false;
    }

    @Override public Runnable getConstructor() {
        return null;
    }

    @Override public Fieldable getProto() {
        return null;
    }

    @Override public void setProto(Fieldable proto) {
    }
}
