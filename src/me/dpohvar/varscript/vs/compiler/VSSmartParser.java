package me.dpohvar.varscript.vs.compiler;

import me.dpohvar.varscript.vs.exception.ParseException;
import org.bukkit.ChatColor;

import java.util.LinkedList;
import java.util.Queue;

import static me.dpohvar.varscript.vs.compiler.VSSmartParser.ParseMode.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 24.06.13
 * Time: 23:49
 */
public class VSSmartParser {

    public static final class ParsedOperand{
        public StringBuilder builder = new StringBuilder();
        public final int row;
        public final int col;

        public ParsedOperand(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override public String toString(){return builder.toString();}
    }

    static enum ParseMode{OPER,COMMENT,NEEDSPACE,STRINGQUOTE,DOUBLE,ESCAPING,SPACER,NUMBER,NUMBER0,NUMBERHEX,OPENSQ,STILLSPACES,UNICODE1,UNICODE2,UNICODE3,UNICODE4}

    private String source;

    public static Queue<ParsedOperand> parse(String input) throws ParseException {
        Queue<ParsedOperand> operands = new LinkedList<ParsedOperand>();
        ParseMode parseMode = SPACER;
        ParsedOperand currentOperand = null;
        int row=0;
        int col=0;
        char[] unicodebuffer = new char[4];
        for(char b:input.toCharArray()){
            switch (parseMode){

                case SPACER:{
                    switch (b){
                        case ' ':case '\n':case '\t':case '\r':{
                            break;
                        }
                        case '"':{
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append('"');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '0':{
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            parseMode = NUMBER0;
                            break;
                        }
                        case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':{
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            parseMode = NUMBER;
                            break;
                        }
                        case '[':{
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            parseMode = OPENSQ;
                            break;
                        }
                        case '}':case '{':{
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            break;
                        }
                        case '#':{
                            parseMode = COMMENT;
                            break;
                        }
                        case '!':case ',':{
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        default:{
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            parseMode = OPER;
                            break;
                        }
                    }
                    break;
                }

                case COMMENT:{
                    switch (b){
                        case '\n':{
                            parseMode = SPACER;
                            break;
                        }
                        default:{
                            break;
                        }
                    }
                    break;
                }

                case OPER:{
                    switch (b){
                        case ' ':case '\n':case '\t':case '\r':{
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        case ':':case '.':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            break;
                        }
                        case '\"':{
                            currentOperand.builder.append(b);
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case ',':case '}':case ']':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        case '{':{
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        default:{
                            currentOperand.builder.append(b);
                            break;
                        }
                    }
                    break;
                }

                case OPENSQ:{
                    switch (b){
                        case ' ':case '\n':case '\t':case '\r':{
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        case '"':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append('"');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '0':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            parseMode = NUMBER0;
                            break;
                        }
                        case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            parseMode = NUMBER;
                            break;
                        }
                        case '[':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            parseMode = OPENSQ;
                            break;
                        }
                        case '}':case '{':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            break;
                        }
                        case ']':{
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        case '#':{
                            parseMode = COMMENT;
                            break;
                        }
                        case '!':case ',':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        default:{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            parseMode = OPER;
                            break;
                        }
                    }
                    break;
                }

                case NEEDSPACE:{
                    switch (b){
                        case ' ':case '\n':case '\t':case '\r':{
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        case '}':case '{':case ',':case ']':{
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("Unexpected "+b));
                        }
                    }
                    break;
                }

                case NUMBER:{
                    switch (b){
                        case ' ':case '\n':case '\t':case '\r':{
                            operands.add(currentOperand);
                            parseMode=SPACER;
                            break;
                        }
                        case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':{
                            currentOperand.builder.append(b);
                            break;
                        }
                        case 'L':case 'S':{
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode=NEEDSPACE;
                            break;
                        }
                        case '.':{
                            currentOperand.builder.append(b);
                            parseMode = DOUBLE;
                            break;
                        }
                        case '}':case ',':case ']':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("can not parse number"));
                        }
                    }
                    break;
                }

                case NUMBER0:{
                    switch (b){
                        case ' ':case '\n':case '\t':case '\r':{
                            operands.add(currentOperand);
                            parseMode=SPACER;
                            break;
                        }
                        case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':{
                            currentOperand.builder.append(b);
                            parseMode = NUMBER;
                            break;
                        }
                        case '.':{
                            currentOperand.builder.append(b);
                            parseMode = DOUBLE;
                            break;
                        }
                        case 'L':case 'S':{
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = NEEDSPACE;
                            break;
                        }
                        case 'x':{
                            currentOperand.builder.append(b);
                            parseMode = NUMBERHEX;
                            break;
                        }
                        case '}':case ']':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("can not parse number"));
                        }
                    }
                    break;
                }

                case NUMBERHEX:{
                    switch (b){
                        case ' ':case '\n':case '\t':case '\r':{
                            operands.add(currentOperand);
                            parseMode=SPACER;
                            break;
                        }
                        case '0':case '1':case '2':case '3':
                        case '4':case '5':case '6':case '7':
                        case '8':case '9':case 'A':case 'B':
                        case 'C':case 'D':case 'E':case 'F':
                        case 'a':case 'b':case 'c':case 'd':
                        case 'e':case 'f':{
                            currentOperand.builder.append(b);
                            break;
                        }
                        case 'L':case 'S':{
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = NEEDSPACE;
                            break;
                        }
                        case '}':case ']':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("can not parse number"));
                        }
                    }
                    break;
                }


                case DOUBLE:{
                    switch (b){
                        case ' ':case '\n':case '\t':case '\r':{
                            operands.add(currentOperand);
                            parseMode=SPACER;
                            break;
                        }
                        case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':{
                            currentOperand.builder.append(b);
                            break;
                        }
                        case '}':case ',':case ']':{
                            operands.add(currentOperand);
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            operands.add(currentOperand);
                            parseMode = SPACER;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("can not parse .number"));
                        }
                    }
                    break;
                }


                case STRINGQUOTE:{
                    switch (b){
                        case '\\':{
                            parseMode = ESCAPING;
                            break;
                        }
                        case '\"':{
                            currentOperand.builder.append('\"');
                            parseMode = OPER;
                            break;
                        }
                        case '&':{
                            currentOperand.builder.append(ChatColor.COLOR_CHAR);
                            break;
                        }
                        default:{
                            currentOperand.builder.append(b);
                            break;
                        }
                    }
                    break;
                }

                case ESCAPING:{
                    switch (b){
                        case '\\':{
                            currentOperand.builder.append('\\');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '\"':{
                            currentOperand.builder.append('\"');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '0':{
                            currentOperand.builder.append('\0');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '1':{
                            currentOperand.builder.append('\1');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '2':{
                            currentOperand.builder.append('\2');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '3':{
                            currentOperand.builder.append('\3');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '4':{
                            currentOperand.builder.append('\4');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '5':{
                            currentOperand.builder.append('\5');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '6':{
                            currentOperand.builder.append('\6');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '7':{
                            currentOperand.builder.append('\7');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case 'r':{
                            currentOperand.builder.append('\r');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case 't':{
                            currentOperand.builder.append('\t');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case 'f':{
                            currentOperand.builder.append('\f');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '\'':{
                            currentOperand.builder.append('\'');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case 'b':{
                            currentOperand.builder.append('\b');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case 'n':{
                            currentOperand.builder.append('\n');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case '&':{
                            currentOperand.builder.append('&');
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case ' ':case '\t':{
                            parseMode = STILLSPACES;
                            break;
                        }
                        case '\r':{
                            currentOperand.builder.append('\r');
                            parseMode = STILLSPACES;
                            break;
                        }
                        case '\n':{
                            currentOperand.builder.append('\n');
                            parseMode = STILLSPACES;
                            break;
                        }
                        case 'u':{
                            parseMode = UNICODE1;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("Can't escape symbol '\\"+b+"'"));
                        }
                    }
                    break;
                }

                case STILLSPACES:{
                    switch (b){
                        case ' ':case '\t':{
                            break;
                        }
                        case '\r':{
                            currentOperand.builder.append('\r');
                            break;
                        }
                        case '\n':{
                            currentOperand.builder.append('\n');
                            break;
                        }
                        case '\\':{
                            parseMode = ESCAPING;
                            break;
                        }
                        case '\"':{
                            currentOperand.builder.append('\"');
                            parseMode = OPER;
                            break;
                        }
                        default:{
                            currentOperand.builder.append(b);
                            parseMode = STRINGQUOTE;
                            break;
                        }
                    }
                    break;
                }

                case UNICODE1:{
                    switch (b){
                        case '0': case '1': case '2': case '3':
                        case '4': case '5': case '6': case '7':
                        case '8': case '9': case 'A': case 'B':
                        case 'C': case 'D': case 'E': case 'F':
                        case 'a': case 'b': case 'c': case 'd':
                        case 'e': case 'f':{
                            unicodebuffer[0]=b;
                            parseMode = UNICODE2;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("Can't escape unicode hex symbol '"+b+"'"));
                        }
                    }
                    break;
                }
                case UNICODE2:{
                    switch (b){
                        case '0': case '1': case '2': case '3':
                        case '4': case '5': case '6': case '7':
                        case '8': case '9': case 'A': case 'B':
                        case 'C': case 'D': case 'E': case 'F':
                        case 'a': case 'b': case 'c': case 'd':
                        case 'e': case 'f':{
                            unicodebuffer[1]=b;
                            parseMode = UNICODE3;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("Can't escape unicode hex symbol: '"+b+"'"));
                        }
                    }
                    break;
                }
                case UNICODE3:{
                    switch (b){
                        case '0': case '1': case '2': case '3':
                        case '4': case '5': case '6': case '7':
                        case '8': case '9': case 'A': case 'B':
                        case 'C': case 'D': case 'E': case 'F':
                        case 'a': case 'b': case 'c': case 'd':
                        case 'e': case 'f':{
                            unicodebuffer[2]=b;
                            parseMode = UNICODE4;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("Can't escape unicode hex symbol: '"+b+"'"));
                        }
                    }
                    break;
                }
                case UNICODE4:{
                    switch (b){
                        case '0': case '1': case '2': case '3':
                        case '4': case '5': case '6': case '7':
                        case '8': case '9': case 'A': case 'B':
                        case 'C': case 'D': case 'E': case 'F':
                        case 'a': case 'b': case 'c': case 'd':
                        case 'e': case 'f':{
                            unicodebuffer[3]=b;
                            char character = (char) Integer.parseInt(new String(unicodebuffer),16);
                            currentOperand.builder.append(character);
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,new RuntimeException("Can't escape unicode hex symbol: '"+b+"'"));
                        }
                    }
                    break;
                }
                default:{
                    break;
                }
            }

            if (b != '\n') {
                col++;
            } else {
                col=0;
                row++;
            }
        }

        switch (parseMode){
            case OPER:case NEEDSPACE:case NUMBER:case NUMBER0:case DOUBLE:case NUMBERHEX:{
                operands.add(currentOperand);
                break;
            }
            case STRINGQUOTE:case ESCAPING:case OPENSQ:case STILLSPACES:case UNICODE1:case UNICODE2:case UNICODE3:case UNICODE4:{
                throw new ParseException(input,row,col,new RuntimeException("Unexpected end of source"));
            }
            case COMMENT:default:{
                break;
            }
        }
        return operands;
    }

















}
