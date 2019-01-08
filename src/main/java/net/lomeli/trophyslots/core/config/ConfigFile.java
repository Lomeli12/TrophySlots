package net.lomeli.trophyslots.core.config;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.FabricLoader;
import net.lomeli.trophyslots.core.Logger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

// TODO: Add to library mod? Replace once official Fabric config API is established
public class ConfigFile {
    private static final Logger CONFIG_LOG = new Logger("Config Log");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_DIR = FabricLoader.INSTANCE.getConfigDirectory();

    private String modid;
    private Class<?> configClass;
    private boolean missingOptions;
    private File configFile;
    private Map<String, List<String>> keyMap = Maps.newHashMap();

    public ConfigFile(String modid, Class<?> configClass) {
        this.modid = modid;
        this.configFile = new File(CONFIG_DIR, modid + ".conf");
        this.configClass = configClass;
        populateKeyList();
    }

    public void loadConfig() {
        if (!CONFIG_DIR.exists() || !configFile.exists() || !configFile.isFile())
            writeConfig();

        if (missingOptions)
            writeConfig();
    }

    @SuppressWarnings({"WeakerAccess", "ResultOfMethodCallIgnored"})
    public void writeConfig() {
        if (!CONFIG_DIR.exists())
            CONFIG_DIR.mkdir();

        JsonObject parent = new JsonObject();
        Field[] fields = configClass.getDeclaredFields();
        if (fields == null || fields.length < 1)
            return;
        for (Field field : fields) {
            boolean accessibility = field.isAccessible();
            field.setAccessible(true);

            if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Config.class)) {
                Config details = field.getAnnotation(Config.class);
                String name = Strings.isNullOrEmpty(details.configName()) ? field.getName() : details.configName();

                try {
                    writeConfig(parent, details.category(), name, details.comment(), field.get(null));
                } catch (IllegalAccessException ex) {
                    CONFIG_LOG.exception("Could not write config value for %s", ex, name);
                }
            }
            field.setAccessible(accessibility);
        }
        if (parent.size() > 0) {
            String data = GSON.toJson(parent);
            try {
                FileUtils.writeStringToFile(configFile, data, Charset.forName("UTF-8"));
            } catch (IOException ex) {
                CONFIG_LOG.exception("Failed to write config file %s", ex, configFile.getName());
            }
        }
    }

    private void writeConfig(JsonObject parent, String categoryName, String name, String comment, Object value) {
        JsonObject category;
        boolean isSub = false;
        if (Strings.isNullOrEmpty(categoryName))
            category = parent;
        else {
            isSub = true;
            if (parent.has(categoryName))
                category = parent.getAsJsonObject(categoryName);
            else category = new JsonObject();
        }
        if (value instanceof Number)
            writeConfig(category, name, comment, (Number) value);
        if (value instanceof Boolean)
            writeConfig(category, name, comment, (Boolean) value);
        if (value instanceof String)
            writeConfig(category, name, comment, (String) value);
        if (value instanceof Character)
            writeConfig(category, name, comment, (Character) value);

        if (isSub)
            parent.add(categoryName, category);
    }

    private void writeConfig(JsonObject category, String name, String comment, Number value) {
        if (!Strings.isNullOrEmpty(comment))
            category.addProperty(name + "_comment", comment);
        category.addProperty(name, value);
    }

    private void writeConfig(JsonObject category, String name, String comment, Boolean value) {
        if (!Strings.isNullOrEmpty(comment))
            category.addProperty(name + "_comment", comment);
        category.addProperty(name, value);
    }

    private void writeConfig(JsonObject category, String name, String comment, String value) {
        if (!Strings.isNullOrEmpty(comment))
            category.addProperty(name + "_comment", comment);
        category.addProperty(name, value);
    }

    private void writeConfig(JsonObject category, String name, String comment, Character value) {
        if (!Strings.isNullOrEmpty(comment))
            category.addProperty(name + "_comment", comment);
        category.addProperty(name, value);
    }

    private void populateKeyList() {
        keyMap.clear();

        Field[] fields = configClass.getDeclaredFields();
        if (fields == null || fields.length < 1)
            return;
        for (Field field : fields) {
            boolean accessibility = field.isAccessible();
            field.setAccessible(true);

            if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Config.class)) {
                Config details = field.getAnnotation(Config.class);
                String name = Strings.isNullOrEmpty(details.configName()) ? field.getName() : details.configName();
                addConfigKey(name, details.category());
            }

            field.setAccessible(accessibility);
        }
    }

    private void addConfigKey(String name, String category) {
        List<String> keys;
        if (keyMap.containsKey(category))
            keys = keyMap.get(category);
        else
            keys = Lists.newArrayList();

        if (keys.contains(name)) {
            if (Strings.isNullOrEmpty(category))
                CONFIG_LOG.error("Duplicate config key \"%s\"!", name);
            else
                CONFIG_LOG.error("Duplicate config key \"%1$s\" in category \"%2$s\"!");
        } else {
            keys.add(name);
            keyMap.put(category, keys);
        }
    }
}
