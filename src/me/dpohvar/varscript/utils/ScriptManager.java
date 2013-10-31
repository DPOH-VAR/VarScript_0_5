package me.dpohvar.varscript.utils;

import me.dpohvar.varscript.VarScript;

import javax.annotation.Nullable;
import javax.script.ScriptEngineFactory;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 14.07.13
 * Time: 12:57
 */
public class ScriptManager {
    private File home;
    public static final String autorunFolder = "autorun";
    public static final String moduleFolder = "modules";

    HashMap<String, String> langExtensions = new HashMap<String, String>() {{
        put("vs", ".vs");
        put("vsbin", ".bin");
    }};

    public ScriptManager(File home) {
        if (!home.isDirectory()) if (!home.mkdirs()) throw new RuntimeException("can't create folder " + home);
        this.home = home;
        for (String lang : langExtensions.keySet()) {
            File langHome = new File(home, lang);
            if (!langHome.isDirectory()) langHome.mkdirs();
        }
    }

    public File getScriptFile(String lang, String name) {
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home, lang);
        if (!folder.isDirectory()) return null;
        File file = new File(folder, name + langExtensions.get(lang));
        if (!file.isFile()) return null;
        return file;
    }

    public File getModuleFile(String lang, String name) {
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home, lang);
        if (!folder.isDirectory()) return null;
        File mod = new File(folder, moduleFolder);
        if (!mod.isDirectory()) return null;
        File file = new File(mod, name + langExtensions.get(lang));
        if (!file.isFile()) return null;
        return file;
    }

    public File getAutorunFile(String lang, String name) {
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home, lang);
        if (!folder.isDirectory()) return null;
        File mod = new File(folder, autorunFolder);
        if (!mod.isDirectory()) return null;
        File file = new File(mod, name + langExtensions.get(lang));
        if (!file.isFile()) return null;
        return file;
    }

    public Map<String, File> getModuleFiles(String lang) {
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home, lang);
        if (!folder.isDirectory()) return null;
        File mod = new File(folder, moduleFolder);
        if (!mod.isDirectory()) return null;
        HashMap<String, File> files = new HashMap<String, File>();
        File[] innerFiles = mod.listFiles();
        if (innerFiles == null) return files;
        String ext = langExtensions.get(lang);
        for (File f : innerFiles) {
            String t = f.getName();
            int p = t.lastIndexOf('.');
            if (p == -1) continue;
            String e = t.substring(p);
            if (!e.equalsIgnoreCase(ext)) continue;
            String name = t.substring(0, p);
            files.put(name, f);
        }
        return files;

    }

    public File getCreateScriptFile(String lang, String name) {
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home, lang);
        if (!folder.isDirectory()) return null;
        File file = new File(folder, name + langExtensions.get(lang));
        if (file.isDirectory()) return null;
        try {
            file.createNewFile();
        } catch (IOException e) {
            return null;
        }
        return file;
    }

    public File getCreateModuleFile(String lang, String module, String name) {
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home, lang);
        if (!folder.isDirectory()) return null;
        File mod = new File(folder, module);
        if (!folder.isDirectory() && !folder.mkdirs()) return null;
        File file = new File(mod, name + langExtensions.get(lang));
        if (file.isDirectory()) return null;
        try {
            file.createNewFile();
        } catch (IOException e) {
            return null;
        }
        return file;
    }

    public FileInputStream openScriptFile(String lang, String name) {
        File file = getScriptFile(lang, name);
        if (file == null) return null;
        try {
            return new FileInputStream(getScriptFile(lang, name));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public byte[] readBytesScriptFile(String lang, String name) {
        try {
            return VarScriptIOUtils.getBytes(getScriptFile(lang, name));
        } catch (Exception e) {
            return null;
        }
    }

    public String readScriptFile(String lang, String name) {
        return VarScriptIOUtils.readFile(getScriptFile(lang, name));
    }

    public boolean saveScriptFile(String lang, String name, String data) {
        return saveScriptFile(lang, name, data.getBytes(VarScript.UTF8));
    }

    public boolean saveModuleFile(String lang, String module, String name, String data) {
        return saveModuleFile(lang, module, name, data.getBytes(VarScript.UTF8));
    }

    public boolean saveScriptFile(String lang, String name, byte[] data) {
        File file = getCreateScriptFile(lang, name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            return false;
        }
        try {
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean saveModuleFile(String lang, String module, String name, byte[] data) {
        File file = getCreateModuleFile(lang, module, name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            return false;
        }
        try {
            fos.write(data);
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean saveScriptFile(String lang, String name, InputStream data) {
        File file = getCreateScriptFile(lang, name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            return false;
        }
        try {
            int t;
            while ((t = data.read()) != -1) fos.write(t);
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean saveModuleFile(String lang, String module, String name, InputStream data) {
        File file = getCreateModuleFile(lang, module, name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            return false;
        }
        try {
            int t;
            while ((t = data.read()) != -1) fos.write(t);
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Nullable
    public String readScriptFile(ScriptEngineFactory factory, String fileName) {
        File scriptDir = new File(home, factory.getLanguageName());
        if (!scriptDir.isDirectory()) return null;
        File file = null;
        for (String ex : factory.getExtensions()) {
            file = new File(scriptDir, fileName + "." + ex);
            if (file.isFile()) break;
        }
        if (file == null) file = new File(scriptDir, fileName);
        if (!file.isFile()) return null;
        return VarScriptIOUtils.readFile(file);
    }

    public Map<String, File> getScriptFiles(ScriptEngineFactory factory) {
        HashMap<String, File> map = new HashMap<String, File>();
        File scriptDir = new File(home, factory.getLanguageName());
        File[] files = scriptDir.listFiles();
        if (files == null) return map;
        List<String> extensions = factory.getExtensions();
        if (extensions.isEmpty()) {
            for (File f : files) {
                map.put(f.getName(), f);
            }
            return map;
        }
        for (File f : files) {
            String name = f.getName();
            int dotpos = name.lastIndexOf('.');
            if (dotpos == -1) continue;
            String ex = name.substring(dotpos + 1, name.length());
            name = name.substring(0, dotpos);
            for (String e : factory.getExtensions()) {
                if (e.equals(ex)) {
                    map.put(name, f);
                    break;
                }
            }
        }
        return map;
    }


    @Nullable
    public String readScriptAutorun(ScriptEngineFactory factory, String fileName) {
        File scriptDir = new File(home, factory.getLanguageName());
        if (!scriptDir.isDirectory()) return null;
        File targetDir = new File(scriptDir, autorunFolder);
        File file = null;
        for (String ex : factory.getExtensions()) {
            file = new File(targetDir, fileName + "." + ex);
            if (file.isFile()) break;
        }
        if (file == null) file = new File(targetDir, fileName);
        if (!file.isFile()) return null;
        return VarScriptIOUtils.readFile(file);
    }

    public Map<String, File> getScriptAutoruns(ScriptEngineFactory factory) {
        HashMap<String, File> map = new HashMap<String, File>();
        File tDir = new File(home, factory.getLanguageName());
        File scriptDir = new File(tDir, autorunFolder);
        File[] files = scriptDir.listFiles();
        if (files == null) return map;
        List<String> extensions = factory.getExtensions();
        if (extensions.isEmpty()) {
            for (File f : files) {
                map.put(f.getName(), f);
            }
            return map;
        }
        for (File f : files) {
            String name = f.getName();
            int dotpos = name.lastIndexOf('.');
            if (dotpos == -1) continue;
            String ex = name.substring(dotpos + 1, name.length());
            name = name.substring(0, dotpos);
            for (String e : factory.getExtensions()) {
                if (e.equals(ex)) {
                    map.put(name, f);
                    break;
                }
            }
        }
        return map;
    }

    @Nullable
    public String readScriptModule(ScriptEngineFactory factory, String fileName) {
        File scriptDir = new File(home, factory.getLanguageName());
        if (!scriptDir.isDirectory()) return null;
        File targetDir = new File(scriptDir, moduleFolder);
        File file = null;
        for (String ex : factory.getExtensions()) {
            file = new File(targetDir, fileName + "." + ex);
            if (file.isFile()) break;
        }
        if (file == null) file = new File(targetDir, fileName);
        if (!file.isFile()) return null;
        return VarScriptIOUtils.readFile(file);
    }

    public Map<String, File> getScriptModules(ScriptEngineFactory factory) {
        HashMap<String, File> map = new HashMap<String, File>();
        File tDir = new File(home, factory.getLanguageName());
        File scriptDir = new File(tDir, moduleFolder);
        File[] files = scriptDir.listFiles();
        if (files == null) return map;
        List<String> extensions = factory.getExtensions();
        if (extensions.isEmpty()) {
            for (File f : files) {
                map.put(f.getName(), f);
            }
            return map;
        }
        for (File f : files) {
            String name = f.getName();
            int dotpos = name.lastIndexOf('.');
            if (dotpos == -1) continue;
            String ex = name.substring(dotpos + 1, name.length());
            name = name.substring(0, dotpos);
            for (String e : factory.getExtensions()) {
                if (e.equals(ex)) {
                    map.put(name, f);
                    break;
                }
            }
        }
        return map;
    }

    public void createEnginesFolder(ScriptEngineFactory factory) {
        File scriptsFolder = new File(home, factory.getLanguageName());
        if (!scriptsFolder.isDirectory()) scriptsFolder.mkdirs();
    }

}
