package me.dpohvar.varscript.vs.compiler;

import me.dpohvar.varscript.vs.exception.ParseException;

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

    static enum ParseMode{
        OPER,
        TRYCOMMENT,
        LINECOMMENT,
        NEEDSPACE,
        STRINGQUOTE,
        DOUBLE,
        ESCAPING,
        SPACER,
        NUMBER,
        NUMBER0,
        NUMBERHEX,
        OPENSQ,
        STILLSPACES,
        UNICODE1,
        UNICODE2,
        UNICODE3,
        UNICODE4,
        COLONVALUES,

    }

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

    public static Queue<ParsedOperand> parse(String input) throws ParseException {
        Queue<ParsedOperand> operands = new LinkedList<ParsedOperand>();
        ParseMode parseMode = SPACER;
        ParsedOperand currentOperand = null;
        int row=0;
        int col=0;
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
                        case '<':{
                            currentOperand = new ParsedOperand(row, col);
                            currentOperand.builder.append(b);
                            parseMode = COLONVALUES;
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
                            parseMode = TRYCOMMENT;
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

                case LINECOMMENT:{
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

                case TRYCOMMENT:{
                    switch (b){
                        case '#':{
                            parseMode = LINECOMMENT;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,"unexpected comment");
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
                            parseMode = TRYCOMMENT;
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
                            throw new ParseException(input,row,col,"Unexpected "+b);
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
                        case ':':{
                            currentOperand.builder.append(b);
                            parseMode = COLONVALUES;
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
                            throw new ParseException(input,row,col,"can not parse number");
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
                        case ':':{
                            currentOperand.builder.append(b);
                            parseMode = COLONVALUES;
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
                            throw new ParseException(input,row,col,"can not parse number");
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
                            throw new ParseException(input,row,col,"can not parse number");
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
                        case ':':{
                            currentOperand.builder.append(b);
                            parseMode = COLONVALUES;
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
                            throw new ParseException(input,row,col,"can not parse .number");
                        }
                    }
                    break;
                }


                case STRINGQUOTE:{
                    switch (b){
                        case '\\':{
                            currentOperand.builder.append('\\');
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
                            break;
                        }
                    }
                    break;
                }

                case ESCAPING:{
                    switch (b){
                        case '\\':
                        case '\"':
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case 'r':
                        case 't':
                        case 'f':
                        case '\'':
                        case 'b':
                        case 'n':
                        case '&':{
                            currentOperand.builder.append(b);
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        case ' ':
                        case '\t':
                        case '\r':
                        case '\n':{
                            currentOperand.builder.append(b);
                            parseMode = STILLSPACES;
                            break;
                        }
                        case 'u':{
                            currentOperand.builder.append(b);
                            parseMode = UNICODE1;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,"Can't escape symbol '\\"+b+"'");
                        }
                    }
                    break;
                }

                case STILLSPACES:{
                    switch (b){
                        case ' ':case '\t':{
                            currentOperand.builder.append(b);
                            break;
                        }
                        case '\r':
                        case '\n':{
                            currentOperand.builder.append(b);
                            break;
                        }
                        case '\\':{
                            currentOperand.builder.append(b);
                            parseMode = ESCAPING;
                            break;
                        }
                        case '\"':{
                            currentOperand.builder.append(b);
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
                            currentOperand.builder.append(b);
                            parseMode = UNICODE2;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,"Can't escape unicode hex symbol '"+b+"'");
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
                            currentOperand.builder.append(b);
                            parseMode = UNICODE3;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,"Can't escape unicode hex symbol: '"+b+"'");
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
                            currentOperand.builder.append(b);
                            parseMode = UNICODE4;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,"Can't escape unicode hex symbol: '"+b+"'");
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
                            currentOperand.builder.append(b);
                            parseMode = STRINGQUOTE;
                            break;
                        }
                        default:{
                            throw new ParseException(input,row,col,"Can't escape unicode hex symbol: '"+b+"'");
                        }
                    }
                    break;
                }
                default:{
                    break;
                }

                case COLONVALUES:{
                    switch (b){
                        case ' ':case '\n':case '\t':case '\r':{
                            operands.add(currentOperand);
                            parseMode = SPACER;
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
            }

            if (b != '\n') {
                col++;
            } else {
                col=0;
                row++;
            }
        }

        switch (parseMode){
            case OPER:
            case NEEDSPACE:
            case NUMBER:
            case NUMBER0:
            case DOUBLE:
            case NUMBERHEX:{
                operands.add(currentOperand);
                break;
            }
            case LINECOMMENT:
            case STRINGQUOTE:
            case ESCAPING:
            case OPENSQ:
            case STILLSPACES:
            case UNICODE1:
            case UNICODE2:
            case UNICODE3:
            case UNICODE4:{
                throw new ParseException(input,row,col,"Unexpected end of source");
            }
            case TRYCOMMENT:default:{
                break;
            }
        }
        return operands;
    }

















}
