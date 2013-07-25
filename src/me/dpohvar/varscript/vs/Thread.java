package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.trigger.Trigger;
import me.dpohvar.varscript.converter.ConvertException;
import me.dpohvar.varscript.converter.Converter;
import me.dpohvar.varscript.vs.exception.InterruptFunction;
import me.dpohvar.varscript.vs.exception.StopThread;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.06.13
 * Time: 19:36
 */
public class Thread implements Fieldable {
    private HashSet<Trigger<?>> triggers = new HashSet<Trigger<?>>();
    private Stack<java.lang.Object> stack = new Stack<java.lang.Object>();
    private Stack<Context> runners = new Stack<Context>();
    boolean finished = false;
    private final Program program;
    private final Converter converter;
    private boolean sleeping = false;
    Fieldable proto;
    Runnable constructor;

    public Stack<Context> getContextStack(){
        return runners;
    }

    public Thread(Program program) {
        this.program = program;
        program.threads.add(this);
        converter = program.getRuntime().converter;
        try{
            this.constructor = (Runnable) program.getScope().getVar("[[Thread]]");
            this.proto = constructor.getPrototype();
        } catch (Exception ignored){
            proto = new FieldableObject(program.getScope());
        }
    }

    public void setSleep(){
        sleeping = true;
    }

    public boolean isSleeping(){
        return sleeping;
    }

    public Stack<java.lang.Object> getStack(){
        return stack;
    }
    public void addTrigger(Trigger<?> trigger){
        if(isFinished()){
            trigger.unregister();
        }else{
            triggers.add(trigger);
        }
    }

    public void removeTrigger(Trigger<?> trigger){
        triggers.remove(trigger);
    }

    public Program getProgram(){
        return program;
    }

    public boolean isFinished(){
        return finished;
    }

    void clearTriggers(){
        for (Trigger t:triggers) t.unregister();
        triggers.clear();

    }

    public void setFinished(){
        finished = true;
        clearTriggers();
        program.threads.remove(this);
        program.checkFinished();
    }

    public Context pushFunction(Runnable function, java.lang.Object apply){
        return runners.push(new Context(function,apply));
    }

    public Context pushFunction(Runnable function){
        return runners.push(new Context(function));
    }

    void runFunctions(ThreadRunner threadRunner) throws Exception {
        sleeping = false;
        if(finished) {
            throw new RuntimeException("thread interrupted");
        }
        while(!runners.empty()){
            try{
                Context r = runners.peek();
                r.runCommands(threadRunner,this);
            } catch(InterruptFunction ignored){
                continue;
            } catch (StopThread ignored){
                return;
            }
            runners.pop();
        }
        setFinished();

    }

    public java.lang.Object pop(){
        return stack.pop();
    }

    public Runnable pop(Scope scope) throws ConvertException{
        java.lang.Object t = stack.pop();
        try{
            return convert(Runnable.class,t);
        } catch (ConvertException e){
            return convert(NamedCommandList.class,t).build(scope);
        }
    }

    public <V extends Enum> V pop(V[] enums){
        return convert(enums,stack.pop());
    }

    public <V extends Enum> V convert(V[] enums, java.lang.Object a){
        return Converter.convert(enums,a);
    }

    public java.lang.Object peek(){
        return stack.peek();
    }

    public <T> T pop(Class<? extends T>[] classes) {
        for(Class<? extends T> clazz: classes){
            try{
                return converter.convert(clazz, stack.pop(), this, program.getScope());
            } catch (ConvertException ignored) {}
        }
        return null;
    }

    public <T> T pop(Class<T> clazz) throws ConvertException {
        return converter.convert(clazz, stack.pop(),  this, program.getScope());
    }

    public <T> T convert(Class<T> clazz, java.lang.Object o) throws ConvertException {
        return converter.convert(clazz, o,  this, program.getScope());
    }

    public <T> T peek(Class<T> clazz) throws ConvertException{
        return converter.convert(clazz, stack.peek(),  this, program.getScope());
    }

    public Thread push(java.lang.Object val){
        stack.push(val);
        return this;
    }


    private HashMap<String, java.lang.Object> fields = new HashMap<String, java.lang.Object>();

    @Override
    public Set<String> getAllFields() {
        HashSet<String> names = new HashSet<String>();
        names.addAll(fields.keySet());
        names.add("constructor");
        if (proto != null) names.addAll(proto.getAllFields());
        return names;
    }

    @Override
    public java.lang.Object getField(String key) {
        if("constructor".equals(key)) return constructor;
        return fields.get(key);
    }

    @Override
    public void setField(String key, java.lang.Object value) {
        fields.put(key,value);
    }

    @Override
    public void removeField(String key) {
        fields.remove(key);
    }

    @Override
    public boolean hasField(String key) {
        if("constructor".equals(key)) return true;
        return fields.containsKey(key);
    }

    @Override
    public Runnable getConstructor() {
        return constructor;
    }

    @Override
    public Fieldable getProto() {
        return proto;
    }

    @Override
    public void setProto(Fieldable proto) {
        this.proto = proto;
    }
}