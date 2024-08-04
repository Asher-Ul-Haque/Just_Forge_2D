package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Editor.ForgeGUI;
import Just_Forge_2D.Utils.justForgeLogger;
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
    public void start(){}

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
                    field.set(this, ForgeGUI.drawIntControl(name, val));
                }
                else if (type == float.class)
                {
                    float val = (float) value;
                    field.set(this, ForgeGUI.drawFloatControl(name, val));
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
                    ForgeGUI.drawVec2Control(name, val);
                }
                else if (type == Vector3f.class)
                {
                    Vector3f val = (Vector3f) value;
                    float[] imVec3 = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name +": ", imVec3))
                    {
                        val.set(imVec3[0], imVec3[1], imVec3[2]);
                    }
                }
                else if (type == Vector4f.class)
                {
                    Vector4f val = (Vector4f) value;
                    float[] imVec4 = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat4(name +": ", imVec4))
                    {
                        val.set(imVec4[0], imVec4[1], imVec4[2], imVec4[3]);
                    }
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
}
