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
        int left,right;
        left = col-20;
        // todo: optimize left and right;
        if(line.length()<40){
            left=0;
            right=line.length();
        } else{
            if(left<0) left=0;
            right = left+40;
            if(right>line.length()) right=line.length();
        }
        int len = operand.toString().length();
        if(col+len>right) len = right-col;
        StringBuilder builder = new StringBuilder();
        builder.append(line.substring(left,col));
        builder.append(ChatColor.GOLD);
        builder.append(line.substring(left+col,left+col+len));
        builder.append(ChatColor.RESET);
        builder.append(line.substring(left+col+len,right));
        return builder.toString();
    }




}
