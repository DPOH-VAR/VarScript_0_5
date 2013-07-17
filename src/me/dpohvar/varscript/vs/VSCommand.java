package me.dpohvar.varscript.vs;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 22:21
 */
public class VSCommand<T> {
    final VSWorker<T> worker;
    final T data;
    public VSCommand(VSWorker<T> worker, T data){
        this.worker = worker;
        this.data = data;
    }
    public void runWorker(VSThreadRunner threadRunner, VSThread thread, VSContext runner) throws Exception{
        worker.run(threadRunner,thread,runner,data);
    }

    public void save(OutputStream out) throws IOException {
        worker.save(out,data);
    }
}
