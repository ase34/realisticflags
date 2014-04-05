package de.ase34.realisticflags.gson;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.ase34.realisticflags.AbstractDyeColorMap;

public class DyeColorMapSerializer implements JsonDeserializer<AbstractDyeColorMap>,
        JsonSerializer<AbstractDyeColorMap> {

    @Override
    public JsonElement serialize(AbstractDyeColorMap src, Type typeOfSrc,
            JsonSerializationContext context) {
        JsonObject object = context.serialize(src, src.getClass()).getAsJsonObject();

        object.addProperty("type", src.getClass().getName());
        return object;
    }

    @Override
    public AbstractDyeColorMap deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String type = object.get("type").getAsString();
        object.remove("type");

        try {
            return context.deserialize(object, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }

}
