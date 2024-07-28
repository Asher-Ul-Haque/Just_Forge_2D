package Just_Forge_2D.Utils.JsonHandlers;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.ECS.Components.TransformComponent;
import com.google.gson.*;

import java.lang.reflect.Type;

public class justForgeGameObjectJsonHandler implements JsonDeserializer<GameObject>
{
    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject object = jsonElement.getAsJsonObject();
        String name = object.get("name").getAsString();
        JsonArray components = object.getAsJsonArray("components");
        TransformComponent transform = jsonDeserializationContext.deserialize(object.get("transform"), TransformComponent.class);
        int layer = jsonDeserializationContext.deserialize(object.get("layer"), int.class);

        GameObject newGameObject = new GameObject(name, transform, layer);
        for (JsonElement e : components)
        {
            Component component = jsonDeserializationContext.deserialize(e, Component.class);
            newGameObject.addComponent(component);
        }

        return newGameObject;
    }
}
