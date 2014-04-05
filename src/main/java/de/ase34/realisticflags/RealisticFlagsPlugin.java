package de.ase34.realisticflags;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import de.ase34.realisticflags.executors.ListColorMapsCommandExecutor;
import de.ase34.realisticflags.executors.ListFlagsCommandExecutor;
import de.ase34.realisticflags.executors.NewColorMapCommandExecutor;
import de.ase34.realisticflags.executors.NewFlagCommandExecutor;
import de.ase34.realisticflags.executors.ReloadCommandExecutor;
import de.ase34.realisticflags.executors.RespawnFlagsCommandExecutor;
import de.ase34.realisticflags.gson.DyeColorMapPluginProxyCreator;
import de.ase34.realisticflags.gson.DyeColorMapSerializer;
import de.ase34.realisticflags.gson.LocationSerializer;

public class RealisticFlagsPlugin extends JavaPlugin {

    private Map<String, Flag> flags = new HashMap<String, Flag>();
    private Map<String, DyeColorMap> colorMaps = new HashMap<String, DyeColorMap>();

    private GsonBuilder gsonBuilder = new GsonBuilder();

    private WorldEditPlugin worldEdit;

    @Override
    public void onEnable() {
        try {
            gsonBuilder.registerTypeAdapter(Location.class, new LocationSerializer());
            gsonBuilder.registerTypeAdapter(AbstractDyeColorMap.class, new DyeColorMapSerializer());
            gsonBuilder.registerTypeAdapter(DyeColorMapPluginProxy.class,
                    new DyeColorMapPluginProxyCreator(this));
            // gsonBuilder.serializeNulls().setPrettyPrinting();

            getDataFolder().mkdir();

            loadColorMaps();
            loadFlags();

            loadWorldEdit();

            getCommand("realisticflags-newcolormap").setExecutor(
                    new NewColorMapCommandExecutor(this));
            getCommand("realisticflags-newflag").setExecutor(new NewFlagCommandExecutor(this));
            getCommand("realisticflags-respawn").setExecutor(new RespawnFlagsCommandExecutor(this));
            getCommand("realisticflags-reload").setExecutor(new ReloadCommandExecutor(this));
            getCommand("realisticflags-listcolormaps").setExecutor(
                    new ListColorMapsCommandExecutor(this));
            getCommand("realisticflags-listflags").setExecutor(new ListFlagsCommandExecutor(this));

            respawnFlags();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadWorldEdit() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
        if (plugin != null && plugin instanceof WorldEditPlugin) {
            worldEdit = (WorldEditPlugin) plugin;
        }
    }

    public void respawnFlags() {
        for (Flag flag : flags.values()) {
            flag.spawn();
        }
    }

    @Override
    public void onDisable() {
        try {
            saveColorMaps();
            saveFlags();
            // removeAllFlags();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAllFlags() {
        for (Flag flag : flags.values()) {
            flag.remove();
        }
        flags.clear();
    }

    public void loadColorMaps() throws Exception {
        File file = new File(getDataFolder(), "color_maps.json");
        file.createNewFile();

        Gson gson = gsonBuilder.create();

        JsonParser parser = new JsonParser();
        FileReader reader = new FileReader(file);

        JsonElement element = parser.parse(reader);

        reader.close();

        if (element == null || !element.isJsonObject()) {
            return;
        }

        JsonObject object = element.getAsJsonObject();
        colorMaps.clear();

        for (Entry<String, JsonElement> entry : object.entrySet()) {
            JsonObject obj = entry.getValue().getAsJsonObject();

            colorMaps.put(entry.getKey(), gson.fromJson(obj, DyeColorMap.class));
        }
    }

    public void loadFlags() throws Exception {
        File file = new File(getDataFolder(), "flags.json");
        file.createNewFile();

        Gson gson = gsonBuilder.create();

        JsonParser parser = new JsonParser();
        FileReader reader = new FileReader(file);

        JsonElement element = parser.parse(reader);

        reader.close();

        if (element == null || !element.isJsonObject()) {
            return;
        }

        JsonObject array = element.getAsJsonObject();
        flags.clear();

        for (Entry<String, JsonElement> elem : array.entrySet()) {
            JsonObject object = elem.getValue().getAsJsonObject();
            Flag flag = gson.fromJson(object, Flag.class);

            flags.put(elem.getKey(), flag);
        }
    }

    public void saveColorMaps() throws Exception {
        File file = new File(getDataFolder(), "color_maps.json");
        file.createNewFile();

        Gson gson = gsonBuilder.create();
        String result = gson.toJson(colorMaps);

        FileWriter writer = new FileWriter(file);
        writer.write(result);
        writer.close();
    }

    public void saveFlags() throws Exception {
        File file = new File(getDataFolder(), "flags.json");
        file.createNewFile();

        Gson gson = gsonBuilder.create();
        String result = gson.toJson(flags);

        FileWriter writer = new FileWriter(file);
        writer.write(result);
        writer.close();
    }

    public Map<String, Flag> getFlags() {
        return flags;
    }

    public Map<String, DyeColorMap> getColorMaps() {
        return colorMaps;
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public WorldEditPlugin getWorldEdit() {
        return worldEdit;
    }
}
