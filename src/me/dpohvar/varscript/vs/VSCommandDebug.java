package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.vs.compiler.VSSmartParser;
import me.dpohvar.varscript.vs.exception.CommandException;
import me.dpohvar.varscript.vs.exception.RuntimeControl;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 22:21
 */
public class VSCommandDebug<T> extends VSCommand<T>{
    final String source;
    final VSSmartParser.ParsedOperand operand;
    public VSCommandDebug(VSWorker<T> worker, T data, String source, VSSmartParser.ParsedOperand operand){
        super(worker, data);
        this.source = source;
        this.operand = operand;
    }
    public void runWorker(VSThreadRunner threadRunner, VSThread thread, VSContext runner) throws Exception{
        try{
            worker.run(threadRunner,thread,runner,data);
        } catch (RuntimeControl control){
            throw control;
        } catch (Exception ex){
            throw new CommandException(operand,source,ex);
        }
    }
}
