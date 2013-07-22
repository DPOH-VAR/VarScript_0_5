package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.vs.compiler.VSCompiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 30.06.13
 * Time: 3:02
 */
public abstract class SimpleWorker extends Worker<Void> {

    public byte[] bytes;
    public SimpleWorker(int[] bytes){
        this.bytes=new byte[bytes.length];
        int i=0; for(int b:bytes) this.bytes[i++]=(byte)b;
    }

    @Override
    public byte[] getBytes(){
        return bytes;
    }

    @Override
    public void save(OutputStream out, Void data) throws IOException {
        out.write(bytes);
    }

    @Override
    public Void readObject(InputStream input,VSCompiler.ReadSession session){
        return null;
    }
}
