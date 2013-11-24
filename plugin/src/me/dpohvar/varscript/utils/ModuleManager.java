package me.dpohvar.varscript.utils;

import me.dpohvar.varscript.Runtime;
import me.dpohvar.varscript.VarScript;

import javax.script.ScriptEngine;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 25.10.13
 * Time: 3:14
 */

/**
 *
 */
public class ModuleManager {

    private HashMap<ScriptEngine, Map<String, Module>> modules = new HashMap<ScriptEngine, Map<String, Module>>();
    private ScriptManager scriptManager;
    private me.dpohvar.varscript.Runtime runtime;

    public Module getModule(ScriptEngine engine, String name) {
        Map<String, Module> map = modules.get(engine);
        assert map != null;
        return map.get(name);
    }

    public ModuleManager(ScriptManager scriptManager, me.dpohvar.varscript.Runtime runtime) {
        this.scriptManager = scriptManager;
        this.runtime = runtime;
    }

    public void reload() {
        for (ScriptEngine engine : Runtime.getEngines()) {
            reload(engine);
        }
        reload(null);
    }

    public void unload() {
        for (Map<String, Module> map : modules.values()) {
            for (Module module : map.values()) {
                module.stop();
            }
        }
        modules = new HashMap<ScriptEngine, Map<String, Module>>();
    }

    public void reload(ScriptEngine engine) {
        Map<String, Object> bindings = runtime.getModuleBindings(engine);
        Map<String, Module> map = modules.get(engine);
        if (map != null) {
            Iterator<Module> itrModule = map.values().iterator();
            while (itrModule.hasNext()) {
                itrModule.next().stop();
                itrModule.remove();
            }
        }
        bindings.clear();
        modules.remove(engine);
        map = new HashMap<String, Module>();
        modules.put(engine, map);
        if (engine == null) /* varscript */ {
            Map<String, File> files = scriptManager.getModuleFiles("vs");
            if (files != null) for (Map.Entry<String, File> e : files.entrySet()) {
                String name = e.getKey();
                if (map.containsKey(name)) continue;
                String src = VarScriptIOUtils.readFile(e.getValue());
                if (src == null) continue;
                VSModule module = new VSModule(name);
                map.put(name, module);
                try {
                    Object result;
                    result = module.load(runtime, engine, src);
                    bindings.put(name, result);
                } catch (Exception ignored) {
                    //map.remove(name);
                }
            }
            return;
        }
        Map<String, File> files = scriptManager.getScriptModules(engine.getFactory());
        if (files != null) for (Map.Entry<String, File> e : files.entrySet()) {
            String name = e.getKey();
            if (map.containsKey(name)) continue;
            String src = VarScriptIOUtils.readFile(e.getValue());
            if (src == null) continue;
            Module module = new Module(name);
            map.put(name, module);
            try {
                Object result;
                result = module.load(runtime, engine, src);
                bindings.put(name, result);
            } catch (Exception ignored) {
                //map.remove(name);
            }
        }

    }

    public Module reload(ScriptEngine engine, String name) {
        Map<String, Module> map = modules.get(engine);
        Map<String, Object> bindings = runtime.getModuleBindings(engine);
        if (map != null) {
            Module m = map.get(name);
            if (m != null) m.stop();
            map.remove(name);
            bindings.remove(name);
        }
        String src = scriptManager.readScriptModule(engine.getFactory(), name);
        if (src == null) return null;
        if (map == null) {
            map = new HashMap<String, Module>();
            modules.put(engine, new HashMap<String, Module>());
        }
        Module module = new Module(name);
        map.put(name, module);
        Object result = null;
        Exception exception = null;
        try {
            result = module.load(runtime, engine, src);
        } catch (Exception e) {
            exception = e;
        }
        if (result == null) {
            VarScript.instance.getLogger().info("[" + engine.getFactory().getLanguageName() + "] <!> module " + name + " fail!");
            if (exception != null) if (VarScript.instance.isDebug()) exception.printStackTrace();
            //map.remove(name);
            return null;
        }
        bindings.put(name, result);
        return module;
    }


}
