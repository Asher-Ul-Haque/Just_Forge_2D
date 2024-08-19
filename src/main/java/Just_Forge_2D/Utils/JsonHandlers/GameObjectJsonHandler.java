package Just_Forge_2D.Utils.JsonHandlers;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.AssetPool;
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
        newGameObject.transform = newGameObject.getComponent(TransformComponent.class);

        return newGameObject;
    }

    public static GameObject copy(GameObject OBJ)
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentJsonHandler())
                .registerTypeAdapter(GameObject.class, new GameObjectJsonHandler())
                .enableComplexMapKeySerialization()
                .create();
        String objAsJson = gson.toJson(OBJ);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);
        obj.generateUniqueID();

        for (Component c : obj.getComponents())
        {
            c.generateID();
        }

        SpriteComponent sprite = obj.getComponent(SpriteComponent.class);
        if (sprite != null && sprite.getTexture() != null)
        {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilepath()));
        }

        return obj;
    }
}
