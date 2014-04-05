package de.ase34.realisticflags.gson;

import java.lang.reflect.Type;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocationSerializer implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("x", src.getX());
        object.addProperty("y", src.getY());
        object.addProperty("z", src.getZ());
        object.addProperty("world", src.getWorld().getUID().toString());

        return object;
    }

    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        World world = Bukkit.getWorld(UUID.fromString(json.getAsJsonObject()
                .getAsJsonPrimitive("world").getAsString()));

        double x = json.getAsJsonObject().getAsJsonPrimitive("x").getAsDouble();
        double y = json.getAsJsonObject().getAsJsonPrimitive("y").getAsDouble();
        double z = json.getAsJsonObject().getAsJsonPrimitive("z").getAsDouble();
        return new Location(world, x, y, z);
    }
}
