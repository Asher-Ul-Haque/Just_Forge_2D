package Just_Forge_2D.Utils.JsonHandlers;

import Just_Forge_2D.Core.ECS.Components.justForgeComponent;
import Just_Forge_2D.Core.ECS.justForgeGameObject;
import Just_Forge_2D.Utils.justForgeTransform;
import com.google.gson.*;

import java.lang.reflect.Type;

public class justForgeGameObjectJsonHandler implements JsonDeserializer<justForgeGameObject>
{
    @Override
    public justForgeGameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject object = jsonElement.getAsJsonObject();
        String name = object.get("name").getAsString();
        JsonArray components = object.getAsJsonArray("components");
        justForgeTransform transform = jsonDeserializationContext.deserialize(object.get("transform"), justForgeTransform.class);
        int layer = jsonDeserializationContext.deserialize(object.get("layer"), int.class);

        justForgeGameObject newGameObject = new justForgeGameObject(name, transform, layer);
        for (JsonElement e : components)
        {
            justForgeComponent component = jsonDeserializationContext.deserialize(e, justForgeComponent.class);
            newGameObject.addComponent(component);
        }

        return newGameObject;
    }
}
