package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

// - - - Abstract class for components
public abstract class Component
{
    // - - - Private variables
    public transient GameObject gameObject = null; // a reference to the object it belongs to
    private static int ID_COUNTER = 0;
    private int uniqueID = -1;


    // - - - | Functions | - - -


    // - - - use - - -

    public void update(float DELTA_TIME){}
    public void start(){}
    public void destroy(){}


    // - - - Editor Part - - -

    public void editorGUI()
    {
        try
        {
            if (ImGui.button("Destroy##" + this.getClass().hashCode()))
            {
                this.gameObject.removeComponent(this.getClass());
            }
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields)
            {
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                boolean isProtected = Modifier.isProtected(field.getModifiers());
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient) continue;
                if (isPrivate || isProtected)
                {
                    field.setAccessible(true);
                }

                Class<?> type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                // why cant we do switch on class
                if (type == int.class)
                {
                    int val = (int) value;
                    field.set(this, Widgets.drawIntControl(name, val));
                }
                else if (type == float.class)
                {
                    float val = (float) value;
                    field.set(this, Widgets.drawFloatControl(name, val));
                }
                else if (type == boolean.class)
                {
                    boolean val = (boolean)value;
                    Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
                    if (ImGui.checkbox(name + ": ", val))
                    {
                        val = !val;
                        field.set(this, val);
                    }
                    Theme.resetDefaultTextColor();
                }
                else if (type == Vector2f.class)
                {
                    Vector2f val = (Vector2f) value;
                    Widgets.drawVec2Control(name, val);
                }
                else if (type == Vector3f.class)
                {
                    Vector3f val = (Vector3f) value;
                    Widgets.drawVec3Control(name, val);
                }
                else if (type == Vector4f.class)
                {
                    Vector4f val = (Vector4f) value;
                    Widgets.colorPicker4(name, val);
                }
                else if (type.isEnum())
                {
//                    String[] enumValues = getEnumValues((Class<Enum>) type);
//                    String enumType = ((Enum) value).name();
//                    ImInt index = new ImInt(indexOf(enumType, enumValues));
//                    Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
//                    ImGui.text(field.getName());
//                    ImGui.sameLine();
//                    Theme.resetDefaultTextColor();
//                    if (ImGui.combo(field.getName(), index, enumValues, enumValues.length))
//                    {
//                        field.set(this, type.getEnumConstants()[index.get()]);
//                    }
                    Enum t = Widgets.drawEnumControls(type, field.getName());
                    if (t != null)
                    {
                        field.set(this, t);
                    }
                }
                else if (type == String.class)
                {
                    field.set(this, Widgets.inputText(field.getName() + ": ", (String) value));
                }

                if (isPrivate)
                {
                    field.setAccessible(false);
                }

            }
        }
        catch (IllegalAccessException e)
        {
            Logger.FORGE_LOG_FATAL("Error loading GUI code: illegal access exception for component " + this);
            Logger.FORGE_LOG_ERROR(e.getMessage());
        }
    }

    public void editorUpdate(float DELTA_TIME)  { debugDraw(); }

    public void debugDraw()  {}

    // - - - Unique initialization - - -

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


    // - - - Physics - - -

    public void beginCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL) {}
    public void endCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL) {}
    public void beforeCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL) {}
    public void afterCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL) {}
}