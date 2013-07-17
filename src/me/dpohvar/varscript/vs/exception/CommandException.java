package me.dpohvar.varscript.vs.exception;

import me.dpohvar.varscript.vs.compiler.VSSmartParser;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 30.06.13
 * Time: 11:20
 */
public class CommandException extends SourceException {

    VSSmartParser.ParsedOperand operand;

    public CommandException(VSSmartParser.ParsedOperand operand, String source, Exception exception) {
        super(source, operand.row, operand.col, exception);
        this.operand = operand;
    }

    @Override public String getMessage(){
        String msg = getCause().getMessage();
        return getCause().getClass().getSimpleName()+" at ["+(row+1)+':'+(col+1)+"]\n"
                +getErrorString()
                +(msg==null?"":'\n'+msg);
    }




}
