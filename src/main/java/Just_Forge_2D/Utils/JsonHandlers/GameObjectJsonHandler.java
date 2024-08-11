package Just_Forge_2D.Utils.JsonHandlers;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.ECS.Components.TransformComponent;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GameObjectJsonHandler implements JsonDeserializer<GameObject>
{
    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject object = jsonElement.getAsJsonObject();
        String name = object.get("name").getAsString();
        JsonArray components = object.getAsJsonArray("components");

        GameObject newGameObject = new GameObject(name);

        for (JsonElement e : components)
        {
            Component component = jsonDeserializationContext.deserialize(e, Component.class);
            newGameObject.addComponent(component);
        }
        newGameObject.transform = newGameObject.getCompoent(TransformComponent.class);

        return newGameObject;
    }
}