package me.dpohvar.varscript.vs.compiler;

import me.dpohvar.varscript.VarScript;
import me.dpohvar.varscript.vs.VSCommand;
import me.dpohvar.varscript.vs.VSCommandDebug;
import me.dpohvar.varscript.vs.VSNamedCommandList;
import me.dpohvar.varscript.vs.VSWorker;
import me.dpohvar.varscript.vs.exception.*;
import me.dpohvar.varscript.vs.init.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 24.06.13
 * Time: 22:47
 */

public class VSCompiler {

    static Collection<CompileRule> compileRules = new LinkedList<CompileRule>();
    static Object[] readRules = new Object[255];
    static HashMap<String,Set<CompileRule>> tags = new HashMap<String, Set<CompileRule>>();

    static{
        InitStack.load();
        InitMathematic.load();
        InitDynamic.load();
        InitLogic.load();
        InitString.load();
        InitMultiThread.load();
    }

    public static Set<CompileRule> getRules(String tag){
        Set<CompileRule> set = tags.get(tag);
        if (set==null) set = new HashSet<CompileRule>();
        return new HashSet<CompileRule>(set);
    }

    public static Set<CompileRule> getRules(){
        return new HashSet<CompileRule>(compileRules);
    }

    public static Set<String> geTags(){
        return tags.keySet();
    }

    public static void addRule(CompileRule rule){
        compileRules.add(rule);
        VSWorker[] workers = rule.getNewWorkersWithRules();
        if(workers!=null) for(VSWorker worker:workers){
            addWorkerToRules(worker);
        }
        for(String s:rule.getTags()){
            if(!tags.containsKey(s)) tags.put(s,new HashSet<CompileRule>());
            tags.get(s).add(rule);
        }
    }

    public static VSNamedCommandList read(InputStream input) throws IOException {
        int size = input.read();
        if(size==-1) throw new RuntimeException("can't read commands: EOS");
        if(size==255){ //read name length
            byte[] temp = new byte[4];
            if(input.read(temp)<4) throw new RuntimeException("can't read commands: EOS");
            size = ByteBuffer.wrap(temp).getInt();
        }
        byte[] bytes = new byte[size]; // read name bytes
        if(input.read(bytes)<size) throw new RuntimeException("can't read commands: EOS");
        String name = new String(bytes, VarScript.UTF8);
        ReadSession readSession = new ReadSession(name);
        size = input.read();
        if(size==-1) throw new RuntimeException("can't read commands: EOS");
        else if(size==254){
            byte[] temp = new byte[4];
            if(input.read(temp)<4) throw new RuntimeException("can't read commands: EOS");
            size = ByteBuffer.wrap(temp).getInt();
        } else if (size==255){
            return read(new GZIPInputStream(input));
        }
        while(readSession.commands.size()<size){
            readCommandByBytes(readRules, input, readSession);
        }
        return readSession.commandList;
    }

    private static void addWorkerToRules(VSWorker worker){
        byte[] wBytes = worker.getBytes();
        byte id = wBytes[wBytes.length-1];
        byte[] sBytes = new byte[wBytes.length-1];
        System.arraycopy(wBytes, 0, sBytes, 0, sBytes.length);
        Object[] current = readRules;
        for(byte b:sBytes){
            if(current[b&0xFF]==null) current[b&0xFF] = new Object[255];
            if(current[b&0xFF] instanceof  Object[]) current = (Object[]) current[b&0xFF];
            else throw new RuntimeException("can not add worker to rules: rule already exists");
        }
        if(current[id&0xFF]!=null) throw new RuntimeException("can not add worker to rules: worker already exists");
        current[id&0xFF]=worker;

    }

    private static void readCommandByBytes(Object[] readRules,InputStream input,ReadSession session) throws IOException {
        int pos = input.read();
        Object got = readRules[pos];
        if(got instanceof VSWorker){
            VSWorker w = (VSWorker) got;
            Object data = w.readObject(input,session);
             session.addCommand(w, data);
        } else if (got instanceof  Object[]){
            readCommandByBytes((Object[]) got,input,session);
        } else throw new RuntimeException("bytecode error: "+pos+"="+got);
    }

    public static VSNamedCommandList compile(String name,CompileSession compileSession, boolean first) throws SourceException {
        FunctionSession functionSession = compileSession.newFunctionSession(name);
        try{
            whileForOperands: while(functionSession.hasOperand()){
                VSSmartParser.ParsedOperand operand = functionSession.pollOperand();
                String string = operand.toString();
                for(CompileRule rule: compileRules){
                    if(rule.checkCondition(string)){
                        rule.apply(operand,functionSession, compileSession);
                        continue whileForOperands;
                    }
                }
                ////////////////////////////ADDITIONAL////////////////////////
                if(compileSession.hasVarName(string)){
                    functionSession.addCommand(InitDynamic.wGetVariable,string,operand);
                    functionSession.addCommand(InitDynamic.wRun,null,operand);
                    continue;
                }
                ////////////////////////////ADDITIONAL////////////////////////
                throw new CommandException(operand, compileSession.getSource(),new RuntimeException("operand is not recognized: "+string));
            }
        } catch (CloseFunction e){
            if (first) throw e;
        }
        if (!functionSession.noJumps()){
            VSSmartParser.ParsedOperand operand = functionSession.peekJumpOperand();
            throw new CommandException(operand, compileSession.getSource(),new RuntimeException("unterminated "+operand));
        }
        return functionSession.getCommandList();
    }
    public static VSNamedCommandList compile(String source) throws SourceException {
        return compile(source,"");
    }

    public static VSNamedCommandList compile(String source,String name) throws SourceException {
        if (name==null) name="";
        CompileSession compileSession = new CompileSession(source);
        return compile(name, compileSession, true);
    }

    public static class ReadSession {

        private final VSNamedCommandList commandList;
        private final ArrayList<VSCommand> commands = new ArrayList<VSCommand>();
        private final ArrayList<VSCommand> commandsAfter = new ArrayList<VSCommand>();
        public ReadSession(String funName){
            commandList = new VSNamedCommandList(commands,funName);
        }
        public <T> void addCommand(VSCommand<T> command){
            commands.add(command);
            commands.addAll(commandsAfter);
            commandsAfter.clear();
        }
        public <T> void addCommand(VSWorker<T> worker, T data){
            addCommand(new VSCommand<T>(worker,data));
        }
        public <T> void addCommandAfter(VSCommand<T> command){
            commandsAfter.add(command);
        }
        public <T> void addCommandAfter(VSWorker<T> worker, T data){
            commandsAfter.add(new VSCommand<T>(worker,data));
        }
    }

    public static class FunctionSession {
        private final ArrayList<VSCommand> commands = new ArrayList<VSCommand>();
        private final VSNamedCommandList commandList;
        private final JumpStack jumpStack = new JumpStack();
        private final JumpStack metaJumpStack = new JumpStack();
        private final Queue<VSSmartParser.ParsedOperand> operands;
        private final String source;
        private final HashMap<String,Integer> labels = new HashMap<String, Integer>();
        public final HashMap<Integer,String> labelJumps = new HashMap<Integer, String>();

        public void addLabel(String label,int pos){
            labels.put(label,pos);
        }
        public void addLabel(String label){
            labels.put(label,commands.size());
        }
        public Integer getLabelPos(String label){
            return labels.get(label);
        }

        public FunctionSession(String name,Queue<VSSmartParser.ParsedOperand> operands,String source) throws ParseException {
            this.commandList = new VSNamedCommandList(commands,name);
            this.operands = operands;
            this.source = source;
        }

        public void addCommand(VSCommand command){
            commands.add(command);
        }

        public void setCommand(int position,VSCommand command){
            commands.set(position,command);
        }

        public <T> void addCommand(VSWorker<T> worker,T object,VSSmartParser.ParsedOperand operand){
            commands.add(new VSCommandDebug<T>(worker,object,source,operand));
        }

        public <T> void setCommand(int position,VSWorker<T> worker,T object,VSSmartParser.ParsedOperand operand){
            commands.set(position,new VSCommandDebug<T>(worker,object,source,operand));
        }

        public int getCurrentPos(){
            return commands.size();
        }

        public VSNamedCommandList getCommandList(){
            return commandList;
        }

        public VSSmartParser.ParsedOperand pollOperand(){
            return operands.poll();
        }

        public boolean hasOperand(){
            return !operands.isEmpty();
        }

        public void addJump(VSSmartParser.ParsedOperand operand,String type,int codePosition){
            jumpStack.push(operand, type, codePosition);
        }
        public void addJump(VSSmartParser.ParsedOperand operand,String type){
            jumpStack.push(operand, type, commands.size());
        }
        public void addMetaJump(VSSmartParser.ParsedOperand operand,String type,int codePosition){
            metaJumpStack.push(operand, type, codePosition);
        }
        public void addMetaJump(VSSmartParser.ParsedOperand operand,String type){
            metaJumpStack.push(operand, type, commands.size());
        }
        public boolean noJumps(){
            return jumpStack.isEmpty();
        }
        public boolean noMetaJumps(){
            return metaJumpStack.isEmpty();
        }
        public void popJump(){
            jumpStack.pop();
        }
        public void popMetaJump(){
            metaJumpStack.pop();
        }
        public int peekJumpPosition(){
            return jumpStack.peekPosition();
        }
        public int peekMetaJumpPosition(){
            return metaJumpStack.peekPosition();
        }
        public VSSmartParser.ParsedOperand peekJumpOperand(){
            return jumpStack.peekOperand();
        }
        public VSSmartParser.ParsedOperand peekMetaJumpOperand(){
            return metaJumpStack.peekOperand();
        }
        public String peekJumpType(){
            return jumpStack.peekType();
        }
        public String peekMetaJumpType(){
            return metaJumpStack.peekType();
        }
    }

    public static class CompileSession {
        private final HashSet<String> varNames = new HashSet<String>();
        private final String source;
        private final Queue<VSSmartParser.ParsedOperand> operands;

        public CompileSession(String source) throws ParseException {
            this.source = source;
            this.operands = VSSmartParser.parse(source);
        }

        public FunctionSession newFunctionSession(String name) throws ParseException {
            return new FunctionSession(name,operands,source);
        }

        public boolean hasVarName(String varName){
            return varNames.contains(varName);
        }

        public void addVarName(String varName){
            varNames.add(varName);
        }

        public String getSource(){
            return source;
        }

    }

    public static class JumpStack{
        private Stack<VSSmartParser.ParsedOperand> operands = new Stack<VSSmartParser.ParsedOperand>();
        private Stack<String> types = new Stack<String>();
        private Stack<Integer> positions = new Stack<Integer>();

        public JumpStack(){
        }

        public boolean isEmpty(){
            return positions.isEmpty();
        }

        public void push(VSSmartParser.ParsedOperand operand,String type,int codePosition){
            operands.push(operand);
            types.push(type);
            positions.push(codePosition);
        }

        public int peekPosition(){
            return positions.peek();
        }

        public String peekType(){
            if(types.isEmpty()) return null;
            return types.peek();
        }

        public VSSmartParser.ParsedOperand peekOperand(){
            return operands.peek();
        }

        public void pop(){
            positions.pop();
            types.pop();
            operands.pop();
        }

    }

}
