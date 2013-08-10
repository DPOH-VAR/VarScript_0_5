package me.dpohvar.varscript.vs.exception;

import me.dpohvar.varscript.vs.compiler.VSSmartParser;
import org.bukkit.ChatColor;

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

    public CommandException(VSSmartParser.ParsedOperand operand, String source, String exception) {
        super(source, operand.row, operand.col, exception);
        this.operand = operand;
    }

    @Override public String getMessage(){
        String msg,reason;
        if(getCause()==null) {
            msg = super.getMessage();
            reason = "";
        } else {
            msg = getCause().getMessage();
            reason = getCause().getClass().getSimpleName();

        }
        return reason+" at ["+(row+1)+':'+(col+1)+"]\n"+getErrorString()+(msg==null?"":'\n'+msg);
    }

    @Override public String getErrorString(){
        if(source==null) return ChatColor.translateAlternateColorCodes('&', "&c[no source]&r");
        String[] lines = source.split("\n");
        if(lines.length==row) return ChatColor.translateAlternateColorCodes('&',"&e[end of source]&r");
        else if(lines.length<row) return ChatColor.translateAlternateColorCodes('&',"&c[unknown source]&r");
        String line = lines[row];
        if(col==line.length()) return ChatColor.translateAlternateColorCodes('&',"&e[end of line]&r ")+line;
        if(col>line.length()) return ChatColor.translateAlternateColorCodes('&',"&c[unknown position]&r ")+line;
        String op = operand.toString();
        if(op.contains("\n")) op=op.substring(0,op.indexOf('\n'));
        int len = op.length();
        StringBuilder builder = new StringBuilder();
        builder.append(line.substring(0,col));
        builder.append(ChatColor.GOLD);
        builder.append(op);
        builder.append(ChatColor.RESET);
        builder.append(line.substring(col+len));
        return builder.toString();
    }




}
