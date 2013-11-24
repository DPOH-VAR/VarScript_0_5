package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.vs.exception.InterruptRunner;
import me.dpohvar.varscript.vs.exception.InterruptThread;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:50
 */
public class ThreadRunner {

    private Stack<Thread> threads = new Stack<Thread>();

    public ThreadRunner() {
    }

    public ThreadRunner(Thread thread) {
        threads.push(thread);
    }

    public void pushThread(Thread thread) {
        threads.push(thread);
    }

    public void runThreads() {
        while (!threads.empty()) {
            Thread t = threads.peek();
            try {
                t.runFunctions(this);
            } catch (InterruptThread ignored) {
                continue;
            } catch (InterruptRunner ignored) {
                return;
            } catch (Exception e) {
                t.getProgram().getCaller().handleException(e);
            }
            threads.pop();
        }
    }
}
