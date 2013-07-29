package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.VarScript;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 30.06.13
 * Time: 1:52
 */
public class CommandList {

    protected final String name;
    protected final List<Command> commands;

    public CommandList(List<Command> commands, String name){
        this.commands = commands;
        this.name = name;
    }

    public Function build(Scope delegateScope){
        return new Function(commands,name,delegateScope);
    }

    public Command getCommand(int pos) {
        return commands.get(pos);
    }

    public int getSize() {
        return commands.size();
    }

    public String getName() {
        return name;
    }

    public void save(OutputStream out) throws IOException {
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
        for(Command command:commands){
            command.save(out);
        }
    }
}
