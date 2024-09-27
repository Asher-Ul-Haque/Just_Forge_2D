package Just_Forge_2D.Utils.JsonHandlers;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.Utils.Logger;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ComponentJsonHandler implements JsonSerializer<Component>, JsonDeserializer<Component>
{
    @Override
    public JsonElement serialize(Component COMPONENT, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject result = new JsonObject();

        result.add("type", new JsonPrimitive(COMPONENT.getClass().getCanonicalName()));
        result.add("properties", jsonSerializationContext.serialize(COMPONENT, COMPONENT.getClass()));

        return result;
    }

    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject object = jsonElement.getAsJsonObject();
        String objectType = object.get("type").getAsString();
        JsonElement element = object.get("properties");

        try
        {
            return jsonDeserializationContext.deserialize(element, Class.forName(objectType));
        }
        catch (ClassNotFoundException e)
        {
            Logger.FORGE_LOG_FATAL("Unkown element type: " + type, e);
            throw new JsonParseException("Unkown element type: " + type, e);
        }
    }
}
