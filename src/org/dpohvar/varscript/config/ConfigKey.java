package org.dpohvar.varscript.config;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 20.07.13
 * Time: 12:19
 */
public enum ConfigKey {
    DEBUG("debug", ConfigType.BOOLEAN),
    SCHEDULER_ENABLED("scheduler.enabled", ConfigType.BOOLEAN),;
    public final String configPath;
    public final ConfigType configType;

    ConfigKey(String k, ConfigType t) {
        this.configPath = k;
        this.configType = t;
    }
}
