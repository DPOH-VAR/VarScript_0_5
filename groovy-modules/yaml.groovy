package yamleditor

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

public class YamlEditor {

    public static Yaml yaml;

    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);
    }

    public static def load(InputStream is) {
        return yaml.load(is)
    }

    public static def load(File file) {
        InputStream is = null;
        def result = null;
        try {
            is = new FileInputStream(file);
            result = load(is);
        } finally {
            try {
                is.close();
            } catch (ignored) {
            }
        }
        return result;
    }

    public static def load(String data) {
        return yaml.load(data)
    }


    public static boolean dump(Object data, OutputStream os) {
        String dump = YamlEditor.dump(data)
        try {
            os.write(dump.getBytes("UTF8"))
        } catch (ignored) {
            return false;
        }
        return true;
    }

    public static boolean dump(Object data, File file) {
        boolean result = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file)
            result = YamlEditor.dump(data, fos)
        } finally {
            try {
                fos.close()
            } catch (ignored) {
            }
        }
        return result;
    }

    public static String dump(Object data) {
        return yaml.dump(data);
    }
}

Script.metaClass.loadYaml = YamlEditor.&load;
Script.metaClass.dumpYaml = YamlEditor.&dump;

return [
        name: 'yaml',
        author: 'DPOHVAR & MYXOMOPX',
        version: [0, 1, 1, 0],
        Yaml: YamlEditor,
        load: YamlEditor.&load,
        dump: YamlEditor.&dump,
]