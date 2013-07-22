package me.dpohvar.varscript.vs.compiler;

import me.dpohvar.varscript.vs.Worker;
import me.dpohvar.varscript.vs.exception.SourceException;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 25.06.13
 * Time: 18:35
 */
public interface CompileRule {

    public boolean checkCondition(String string);
    public String[] getTags();
    public String getDescription();

    public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException;
    public Worker[] getNewWorkersWithRules();

}
