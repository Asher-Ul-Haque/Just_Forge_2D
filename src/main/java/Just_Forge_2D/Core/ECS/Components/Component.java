package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Editor.ForgeIsGUI;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

// - - - Abstrasct class for components
public abstract class Component
{
    // - - - Private variables
    public transient GameObject gameObject = null; // a reference to the object it belongs to
    private static int ID_COUNTER = 0;
    private int uniqueID = -1;

    // - - - Functions - - -

    // - - - use
    public void update(float DELTA_TIME){}
    public void editorUpdate(float DELTA_TIME){}
    public void start(){}
    public void destroy(){}

    // - - - make the IMGui PART
    public void editorGUI()
    {
        try
        {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields)
            {
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient) continue;
                if (isPrivate)
                {
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                // why cant we do switch on class
                if (type == int.class)
                {
                    int val = (int) value;
                    field.set(this, ForgeIsGUI.drawIntControl(name, val));
                }
                else if (type == float.class)
                {
                    float val = (float) value;
                    field.set(this, ForgeIsGUI.drawFloatControl(name, val));
                }
                else if (type == boolean.class)
                {
                    boolean val = (boolean)value;
                    boolean[] imBool = {val};
                    if (ImGui.checkbox(name + ": ", val))
                    {
                        val = !val;
                        field.set(this, val);
                    }
                }
                else if (type == Vector2f.class)
                {
                    Vector2f val = (Vector2f) value;
                    ForgeIsGUI.drawVec2Control(name, val);
                }
                else if (type == Vector3f.class)
                {
                    Vector3f val = (Vector3f) value;
                    ForgeIsGUI.drawVec3(name, val);
                }
                else if (type == Vector4f.class)
                {
                    Vector4f val = (Vector4f) value;
                    ForgeIsGUI.colorPicker4(name, val);
                }


                if (isPrivate)
                {
                    field.setAccessible(false);
                }
            }
        }
        catch (IllegalAccessException e)
        {
            //justForgeLogger.FORGE_LOG_FATAL("Error loading GUI code: illegal access exception for component " + this.toString());
            //justForgeLogger.FORGE_LOG_ERROR(e.getMessage());
        }
    }

    public void generateID()
    {
        if (this.uniqueID == -1)
        {
            this.uniqueID = ID_COUNTER++;
        }
    }

    public int getUniqueID()
    {
        return this.uniqueID;
    }

    public static void init(int MAX_ID)
    {
        ID_COUNTER = MAX_ID;
    }

    public void editorUpdate(SpriteComponent SPRITE)
    {}
}
