package me.dpohvar.varscript.utils;

import me.dpohvar.varscript.VarScript;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 14.07.13
 * Time: 12:57
 */
public class ScriptManager {
    private File home;
    HashMap<String,String> langExtensions = new HashMap<String, String>(){{
        put("vs",".vs");
        put("vsbin",".bin");
        put("js",".js");
    }};
    public ScriptManager(File home){
        if(!home.isDirectory()) if(!home.mkdirs()) throw new RuntimeException("can't create folder "+home);
        this.home=home;
        for(String lang: langExtensions.keySet()){
            File langHome = new File(home,lang);
            if(!langHome.isDirectory()) langHome.mkdirs();
        }
    }

    public File getScriptFile(String lang,String name){
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home,lang);
        if (!folder.isDirectory()) return null;
        File file = new File(folder,name+ langExtensions.get(lang));
        if (!file.isFile()) return null;
        return file;
    }

    public File getModuleFile(String lang,String module,String name){
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home,lang);
        if (!folder.isDirectory()) return null;
        File mod = new File(folder,module);
        if (!mod.isDirectory()) return null;
        File file = new File(mod,name+ langExtensions.get(lang));
        if (!file.isFile()) return null;
        return file;
    }

    public Map<String,File> getModuleFiles(String lang,String module){
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home,lang);
        if (!folder.isDirectory()) return null;
        File mod = new File(folder,module);
        if (!mod.isDirectory()) return null;
        HashMap<String,File> files = new HashMap<String,File>();
        File[] innerFiles = mod.listFiles();
        if (innerFiles==null) return files;
        String ext = langExtensions.get(lang);
        for(File f:innerFiles){
            String t = f.getName();
            int p = t.lastIndexOf('.');
            if( p == -1 ) continue;
            String e = t.substring(p);
            if( ! e.equalsIgnoreCase(ext)) continue;
            String name = t.substring(0,p);
            files.put(name,f);
        }
        return files;

    }

    public File getCreateScriptFile(String lang,String name){
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home,lang);
        if (!folder.isDirectory()) return null;
        File file = new File(folder,name+ langExtensions.get(lang));
        if (file.isDirectory()) return null;
        try {
            file.createNewFile();
        } catch (IOException e) {
            return null;
        }
        return file;
    }

    public File getCreateModuleFile(String lang,String module,String name){
        if (!langExtensions.containsKey(lang)) return null;
        File folder = new File(home,lang);
        if (!folder.isDirectory()) return null;
        File mod = new File(folder,module);
        if (!folder.isDirectory() && !folder.mkdirs()) return null;
        File file = new File(mod,name+ langExtensions.get(lang));
        if (file.isDirectory()) return null;
        try {
            file.createNewFile();
        } catch (IOException e) {
            return null;
        }
        return file;
    }

    public FileInputStream openScriptFile(String lang,String name){
        File file = getScriptFile(lang,name);
        if (file==null) return null;
        try {
            return new FileInputStream(getScriptFile(lang,name));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public byte[] readBytesScriptFile(String lang,String name){
        try {
            return IOUtils.toByteArray(openScriptFile(lang, name));
        } catch (Exception e) {
            return null;
        }
    }

    public String readScriptFile(String lang,String name){
        byte[] bytes = readBytesScriptFile(lang, name);
        if(bytes==null) return null;
        return new String(bytes, VarScript.UTF8);
    }

    public boolean saveScriptFile(String lang,String name,String data){
        return saveScriptFile(lang, name, data.getBytes(VarScript.UTF8));
    }

    public boolean saveModuleFile(String lang,String module,String name,String data){
        return saveModuleFile(lang, module, name, data.getBytes(VarScript.UTF8));
    }

    public boolean saveScriptFile(String lang,String name,byte[] data){
        File file = getCreateScriptFile(lang,name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            return false;
        }
        try {
            fos.write(data);
            fos.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean saveModuleFile(String lang,String module,String name,byte[] data){
        File file = getCreateModuleFile(lang,module,name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            return false;
        }
        try {
            fos.write(data);
            fos.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean saveScriptFile(String lang,String name,InputStream data){
        File file = getCreateScriptFile(lang,name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            return false;
        }
        try {
            int t;
            while( (t=data.read())!=-1 ) fos.write(t);
            fos.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public boolean saveModuleFile(String lang,String module,String name,InputStream data){
        File file = getCreateModuleFile(lang,module,name);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            return false;
        }
        try {
            int t;
            while( (t=data.read())!=-1 ) fos.write(t);
            fos.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }

}
