package me.dpohvar.varscript.vs;

import me.dpohvar.varscript.vs.converter.ConvertException;
import me.dpohvar.varscript.vs.converter.Converter;
import me.dpohvar.varscript.vs.exception.InterruptFunction;
import me.dpohvar.varscript.vs.exception.StopThread;
import me.dpohvar.varscript.scheduler.VSTrigger;

import java.lang.annotation.Target;
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
public class VSThread implements VSFieldable {
    private HashSet<VSTrigger<?>> triggers = new HashSet<VSTrigger<?>>();
    private Stack<Object> stack = new Stack<Object>();
    private Stack<VSContext> runners = new Stack<VSContext>();
    boolean finished = false;
    private final VSProgram program;
    private final Converter converter;
    private boolean sleeping = false;
    VSFieldable proto;
    VSRunnable constructor;

    public Stack<VSContext> getContextStack(){
        return runners;
    }

    public VSThread(VSProgram program) {
        this.program = program;
        program.threads.add(this);
        converter = program.getRuntime().converter;
        try{
            this.constructor = (VSRunnable) program.getScope().getVar("[[Thread]]");
            this.proto = constructor.getPrototype();
        } catch (Exception ignored){
            proto = new VSObject(program.getScope());
        }
    }

    public void setSleep(){
        sleeping = true;
    }

    public boolean isSleeping(){
        return sleeping;
    }

    public Stack<Object> getStack(){
        return stack;
    }
    public void addTrigger(VSTrigger<?> trigger){
        if(isFinished()){
            trigger.unregister();
        }else{
            triggers.add(trigger);
        }
    }

    public void removeTrigger(VSTrigger<?> trigger){
        triggers.remove(trigger);
    }

    public VSProgram getProgram(){
        return program;
    }

    public boolean isFinished(){
        return finished;
    }

    void clearTriggers(){
        for (VSTrigger t:triggers) t.unregister();
        triggers.clear();

    }

    public void setFinished(){
        finished = true;
        clearTriggers();
        program.threads.remove(this);
        System.out.print("ENDOFTHREAD(__" + triggers.size() + "__)");
        program.checkFinished();
    }

    public VSContext pushFunction(VSRunnable function,Object apply){
        return runners.push(new VSContext(function,apply));
    }

    void runFunctions(VSThreadRunner threadRunner) throws Exception {
        sleeping = false;
        if(finished) {
            throw new RuntimeException("thread interrupted");
            //todo: just return ;)
        }
        while(!runners.empty()){
            try{
                VSContext r = runners.peek();
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

    public Object pop(){
        return stack.pop();
    }

    public <V extends Enum> V pop(V[] enums){
        return convert(enums,stack.pop());
    }

    public <V extends Enum> V convert(V[] enums,Object a){
        return Converter.convert(enums,a);
    }

    public Object peek(){
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

    public <T> T convert(Class<T> clazz,Object o) throws ConvertException {
        return converter.convert(clazz, o,  this, program.getScope());
    }

    public <T> T peek(Class<T> clazz) throws ConvertException{
        return converter.convert(clazz, stack.peek(),  this, program.getScope());
    }

    public VSThread push(Object val){
        stack.push(val);
        return this;
    }


    private HashMap<String,Object> fields = new HashMap<String, Object>();

    @Override
    public Set<String> getAllFields() {
        HashSet<String> names = new HashSet<String>();
        names.addAll(fields.keySet());
        names.add("constructor");
        if (proto != null) names.addAll(proto.getAllFields());
        return names;
    }

    @Override
    public Object getField(String key) {
        if("constructor".equals(key)) return constructor;
        return fields.get(key);
    }

    @Override
    public void setField(String key, Object value) {
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
    public VSRunnable getConstructor() {
        return constructor;
    }

    @Override
    public VSFieldable getProto() {
        return proto;
    }

    @Override
    public void setProto(VSFieldable proto) {
        this.proto = proto;
    }
}
