package Just_Forge_2D.Utils.JsonHandlers;

import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.NonSpritePrefab;
import Just_Forge_2D.PrefabSystem.Prefab;
import Just_Forge_2D.PrefabSystem.SpritePrefab;
import Just_Forge_2D.RenderingSystem.Sprite;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PrefabJsonHandler implements JsonSerializer<Prefab>, JsonDeserializer<Prefab>
{
    @Override
    public Prefab deserialize(JsonElement ELEMENT, Type TYPE, JsonDeserializationContext CONTEXT) throws JsonParseException
    {
        GameObject gameObject = CONTEXT.deserialize(ELEMENT, GameObject.class);
        SpriteComponent spr = gameObject.getComponent(SpriteComponent.class);
        if (spr != null)
        {
            Sprite sprite = spr.getSpriteCopy();
            return new SpritePrefab(gameObject.name, sprite, gameObject.transform.scale.x, gameObject.transform.scale.y);
        }
        else
        {
            return new NonSpritePrefab(gameObject.name, gameObject.transform.scale.x, gameObject.transform.scale.y);
        }
    }

    @Override
    public JsonElement serialize(Prefab PREFAB, Type TYPE, JsonSerializationContext CONTEXT)
    {
        GameObject gameObject = PREFAB.create();
        return CONTEXT.serialize(gameObject, GameObject.class);
    }
}
