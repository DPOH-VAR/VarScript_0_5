package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.vs.exception.StopFunction;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:48
 */
public class VSFunction extends VSNamedCommandList implements VSRunnable,VSFieldable{

    VSFieldable prototype;
    VSRunnable constructor = null;
    VSFieldable proto;

    VSScope scope;

    public VSFunction(List<VSCommand> commands, String name,VSScope delegateScope){
        super(commands, name);
        this.scope=delegateScope;
        try{
            this.constructor = (VSRunnable) scope.getVar("[[Function]]");
            this.proto = constructor.getPrototype();
        } catch (Exception ignored){
            proto = new VSObject(scope);
        }
        this.prototype = new VSObject(this.scope);
    }

    @Override
    public String toString(){
        return getName()+"{}";
    }

    @Override
    public VSFieldable getPrototype(){
        return prototype;
    }

    @Override
    public void setPrototype(VSFieldable prototype){
        this.prototype = prototype;
    }

    @Override
    public VSScope getDelegatedScope() {
        return scope;
    }

    public void runCommands(VSThreadRunner threadRunner, VSThread thread, VSContext runner) throws Exception {
        if (commands==null) return;
        while(runner.getPointer()<commands.size()){
            try{
                VSCommand command = commands.get(runner.nextPointer());
                command.runWorker(threadRunner,thread,runner);
            } catch(StopFunction ignored){
                return;
            }
        }
    }


    public void save(OutputStream out) throws IOException {
        String name = this.name;
        if (name==null) name="";
        byte[] bytes = name.getBytes(VarScript.UTF8);
        if(bytes.length>254){
            out.write(255);
            byte[] temp = new byte[4];
            ByteBuffer.wrap(temp).putInt(commands.size());
            out.write(bytes);
        } else {
            out.write(bytes.length);
        }
        out.write(bytes);
        if(commands.size()>254){
            byte[] temp = new byte[4];
            ByteBuffer.wrap(temp).putInt(commands.size());
            out.write(bytes);
        } else {
            out.write(commands.size());
        }
        for(VSCommand command:commands){
            command.save(out);
        }
    }
    HashMap<String,Object> fields = new HashMap<String,Object>();

    @Override public Set<String> getAllFields() {
        HashSet<String> names = new HashSet<String>();
        names.addAll(fields.keySet());
        names.add("prototype");
        names.add("constructor");
        if (proto != null) names.addAll(proto.getAllFields());
        return names;
    }

    @Override public Object getField(String key){
        if("prototype".equals(key)) return prototype;
        if("constructor".equals(key)) return constructor;
        if(fields.containsKey(key)) return fields.get(key);
        if (proto == null) return null;
        return proto.getField(key);
    }

    @Override public void setField(String key,Object value){
        fields.put(key,value);
    }

    @Override
    public void removeField(String key){
        fields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        if("prototype".equals(key)) return true;
        if("constructor".equals(key)) return true;
        return fields.containsKey(key);
    }

    @Override
    public VSRunnable getConstructor(){
        return constructor;
    }

    @Override
    public VSFieldable getProto(){
        return proto;
    }

    @Override
    public void setProto(VSFieldable proto){
        this.proto = proto;
    }

}
