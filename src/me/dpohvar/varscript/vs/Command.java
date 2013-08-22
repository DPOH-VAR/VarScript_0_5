package me.dpohvar.varscript.vs;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 22:21
 */
public class Command<T> {
    final Worker<T> worker;
    final T data;

    public Command(Worker<T> worker, T data) {
        this.worker = worker;
        this.data = data;
    }

    public void runWorker(ThreadRunner threadRunner, Thread thread, Context runner) throws Exception {
        worker.run(threadRunner, thread, runner, data);
    }

    public void save(OutputStream out) throws IOException {
        worker.save(out, data);
    }
}
