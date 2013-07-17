package me.dpohvar.varscript.vs.exception;

import org.bukkit.ChatColor;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 30.06.13
 * Time: 11:20
 */
public abstract class SourceException extends Exception {
    protected String source;
    protected int row, col;
    public SourceException(String source,int row,int col,Throwable reason) {
        super(reason);
        this.source = source;
        this.row = row;
        this.col = col;
    }

    public String getErrorString(){
        if(source==null) return ChatColor.translateAlternateColorCodes('&',"&c[no source]&r");
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
            right = left+30;
            if(right>line.length()) right=line.length();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(line.substring(left,col));
        builder.append(ChatColor.RED);
        builder.append(line.charAt(col));
        builder.append(ChatColor.RESET);
        builder.append(line.substring(col+1,right));
        return builder.toString();
    }




}
