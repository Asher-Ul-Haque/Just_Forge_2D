package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.EditorSystem.ForgeIsGUI;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import imgui.type.ImInt;
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

    // - - - Functions - - -

    // - - - use
    public void update(float DELTA_TIME) {}
    public void start(){}
    public void destroy(){}


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


    // - - - Physics
    public void beginCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL) {}

    public void endCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL) {}

    public void beforeCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL) {}

    public void afterCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL) {}
}
