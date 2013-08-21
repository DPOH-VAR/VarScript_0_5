package org.dpohvar.varscript.vs.exception;

import org.bukkit.ChatColor;
import org.dpohvar.varscript.vs.*;
import org.dpohvar.varscript.vs.Runnable;
import org.dpohvar.varscript.vs.compiler.VSSmartParser;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 12.08.13
 * Time: 14:31
 */
public class ThreadTrace extends Exception {
    Stack<Context> contexts;

    public ThreadTrace(Stack<Context> contexts, Throwable e) {
        super(e);
        this.contexts = contexts;
    }

    public
    @Override
    String getMessage() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getCause().getMessage());
        int t = 10;
        while (!contexts.isEmpty()) {
            Context c = contexts.pop();
            if (t-- <= 0) {
                if (!contexts.isEmpty()) {
                    buffer.append("\n>> ").append(contexts.size()).append(" more");
                }
                break;
            }
            Runnable runnable = c.getConstructor();
            buffer.append("\n>> ");
            buffer.append(ChatColor.YELLOW).append(runnable).append(ChatColor.RESET);

            if (runnable instanceof Function) {
                Function function = (Function) runnable;
                Command command = function.getCommand(c.getPointer() - 1);
                if (command instanceof CommandDebug) {
                    VSSmartParser.ParsedOperand operand = ((CommandDebug) command).operand;
                    buffer.append(" at [");
                    buffer.append(operand.row + 1).append(':').append(operand.col + 1).append("] ");
                    buffer.append(ChatColor.GOLD).append(operand).append(ChatColor.RESET);
                }
            }
            buffer.append(" for ");
            buffer.append(c.getApply());
        }
        return buffer.toString();
    }
}
