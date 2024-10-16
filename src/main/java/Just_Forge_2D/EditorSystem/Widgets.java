package Just_Forge_2D.EditorSystem;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static Just_Forge_2D.EditorSystem.Themes.Theme.resetDefaultTextColor;
import static Just_Forge_2D.EditorSystem.Themes.Theme.setDefaultTextColor;

public class Widgets {

    private static final float DEFAULT_RESET = 0.0f;


    // - - - Vec2 Control - - -
    public static void drawVec2Control(String label, Vector2f values, float resetValue, float JUMP)
    {
        ImGui.pushID(label);

        if (ImGui.beginTable("##Vec2Table", 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            ImGui.alignTextToFramePadding();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.text(label);
            resetDefaultTextColor();

            ImGui.tableNextColumn();
            float framePaddingX = EditorSystemManager.getCurrentTheme().framePadding.x;
            float framePaddingY = EditorSystemManager.getCurrentTheme().framePadding.y;
            float totalWidth = ImGui.calcItemWidth() - (framePaddingX * 2.0f); // Get total available width


            float buttonWidth = ImGui.getFontSize() + framePaddingY * 2.0f + 8.0f; // Button size includes padding
            float usableWidth = totalWidth - (buttonWidth * 2.0f) - (framePaddingX * 4.0f); // Subtract padding
            float controlWidth = usableWidth / 2.0f;

            // - - - X Control
            ImGui.pushItemWidth(controlWidth);
            setButtonStyle(0.8f, 0.1f, 0.15f);
            if (ImGui.button("X", buttonWidth, ImGui.getFontSize() + framePaddingY * 2.0f))
            {
                values.x = resetValue;
            }
            ImGui.popStyleColor(3);
            ImGui.sameLine();
            float[] vecValuesX = {values.x};
            ImGui.dragFloat("##X", vecValuesX, JUMP);
            ImGui.popItemWidth();

            ImGui.sameLine();

            // - - - Y Control
            ImGui.pushItemWidth(controlWidth);
            setButtonStyle(0.2f, 0.7f, 0.2f);
            if (ImGui.button("Y", buttonWidth, ImGui.getFontSize() + framePaddingY * 2.0f))
            {
                values.y = resetValue;
            }
            ImGui.popStyleColor(3);
            ImGui.sameLine();
            float[] vecValuesY = {values.y};
            ImGui.dragFloat("##Y", vecValuesY, JUMP);
            ImGui.popItemWidth();

            values.set(vecValuesX[0], vecValuesY[0]);

            ImGui.endTable();
        }

        ImGui.popID();
    }



    public static void drawVec2Control(String label, Vector2i values, float resetValue, float JUMP)
    {
        Vector2f valuesCpy = new Vector2f(values);
        drawVec2Control(label, valuesCpy, resetValue, JUMP);
        values.set((int) valuesCpy.x, (int) valuesCpy.y);
    }

    public static void drawVec2Control(String label, Vector2f values)
    {
        drawVec2Control(label, values, DEFAULT_RESET, 0.1f);
    }

    public static void drawVec2Control(String label, Vector2i values)
    {
        drawVec2Control(label, values, DEFAULT_RESET, 1f);
    }

    // - - - Vec3 Control - - -
    public static void drawVec3Control(String label, Vector3f values, float resetValue, float JUMP) {
        ImGui.pushID(label);

        if (ImGui.beginTable("##Vec3Table", 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            ImGui.alignTextToFramePadding();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.text(label);
            resetDefaultTextColor();

            ImGui.tableNextColumn(); // Second column for the input controls
            float framePaddingX = EditorSystemManager.getCurrentTheme().framePadding.x;
            float framePaddingY = EditorSystemManager.getCurrentTheme().framePadding.y;
            float totalWidth = ImGui.calcItemWidth() - (framePaddingX * 2.0f); // Get total available width


            float buttonWidth = ImGui.getFontSize() + framePaddingY * 2.0f + 8.0f; // Button size includes padding
            float usableWidth = totalWidth - (buttonWidth * 2.0f) - (framePaddingX * 4.0f); // Subtract padding
            float controlWidth = usableWidth / 2.0f;

            // X Control
            ImGui.pushItemWidth(controlWidth);
            setButtonStyle(0.8f, 0.1f, 0.15f);
            if (ImGui.button("X", buttonWidth, ImGui.getFontSize() + framePaddingY * 2.0f))
            {
                values.x = resetValue;
            }
            ImGui.popStyleColor(3);
            ImGui.sameLine();
            float[] vecValuesX = {values.x};
            ImGui.dragFloat("##X", vecValuesX, JUMP);

            ImGui.sameLine();

            // Y Control
            ImGui.pushItemWidth(controlWidth);
            setButtonStyle(0.2f, 0.7f, 0.2f);
            if (ImGui.button("Y", buttonWidth, ImGui.getFontSize() + framePaddingY * 2.0f))
            {
                values.y = resetValue;
            }
            ImGui.popStyleColor(3);
            ImGui.sameLine();
            float[] vecValuesY = {values.y};
            ImGui.dragFloat("##Y", vecValuesY,  JUMP);
            ImGui.popItemWidth();

            ImGui.sameLine();

            // Z Control
            ImGui.pushItemWidth(controlWidth);
            setButtonStyle(0.1f, 0.25f, 0.8f);
            if (ImGui.button("Z", buttonWidth, ImGui.getFontSize() + framePaddingY * 2.0f))
            {
                values.z = resetValue;
            }
            ImGui.popStyleColor(3);
            ImGui.sameLine();
            float[] vecValuesZ = {values.z};
            ImGui.dragFloat("##Z", vecValuesZ, JUMP);
            ImGui.popItemWidth();

            values.set(vecValuesX[0], vecValuesY[0], vecValuesZ[0]);

            ImGui.endTable(); // End table
        }

        ImGui.popID();
    }

    public static void drawVec3Control(String label, Vector3f values)
    {
        drawVec3Control(label, values, DEFAULT_RESET, 0.1f);
    }


    // - - - Float Control - - -

    public static float drawFloatControl(String label, float value)
    {
        float[] valArr = new float[1];
        valArr[0] = value;
        if (ImGui.beginTable("##tableFloatControl" + label, 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.text(label);
            resetDefaultTextColor();
            ImGui.tableNextColumn();
            ImGui.dragFloat("##DragFloat", valArr, 0.1f);
            ImGui.endTable();
        }
        return valArr[0];
    }

    // - - - Int Control - - -
    public static int drawIntControl(String label, int value)
    {
        int[] valArr = new int[1];
        valArr[0] = value;
        if (ImGui.beginTable("##tableIntControl", 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.text(label);
            resetDefaultTextColor();
            ImGui.tableNextColumn();
            ImGui.dragInt("##DragInt", valArr, 0.1f);
            ImGui.endTable();
        }
        return valArr[0];
    }


    // - - - Color Picker - - -
    public static boolean colorPicker4(String label, Vector4f color)
    {
        ImGui.pushID(label);

        float[] colorArr = {color.x, color.y, color.z, color.w};
        boolean changed = false;

        if (ImGui.beginTable("##tableColorPicker4", 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.text(label);
            resetDefaultTextColor();

            ImGui.tableNextColumn();
            changed = ImGui.colorEdit4("##ColorPicker", colorArr);
            if (changed)
            {
                color.set(colorArr[0], colorArr[1], colorArr[2], colorArr[3]);
            }

            ImGui.endTable();
        }

        ImGui.popID();
        return changed;
    }


    public static boolean colorPicker3(String label, Vector3f color)
    {
        ImGui.pushID(label);

        float[] colorArr = {color.x, color.y, color.z};
        boolean changed = false;

        if (ImGui.beginTable("##tableColorPicker3", 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.text(label);
            resetDefaultTextColor();

            ImGui.tableNextColumn();
            changed = ImGui.colorEdit3("##ColorPicker", colorArr);  // Use colorEdit3 for 3-component color
            if (changed)
            {
                color.set(colorArr[0], colorArr[1], colorArr[2]);
            }

            ImGui.endTable();
        }

        ImGui.popID();
        return changed;
    }


    public static boolean drawBoolControl(String label, boolean value)
    {
        ImGui.pushID(label);

        boolean changed = value;

        if (ImGui.beginTable("##tableBoolControl", 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
            ImGui.text(label);
            resetDefaultTextColor();

            ImGui.tableNextColumn();
            if (ImGui.checkbox("##" + label, value))
            {
                changed = !value;
            }

            ImGui.endTable();
        }

        ImGui.popID();
        return changed;
    }


        // - - - Text Input - - -
    public static String inputText(String label, String text)
    {
        ImGui.pushID(label);

        ImString imString = new ImString(text, 256);
        if (ImGui.beginTable("##tableTextInput", 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.text(label);
            resetDefaultTextColor();

            ImGui.tableNextColumn();
            if (ImGui.inputText("##" + label, imString))
            {
                text = imString.get();
            }

            ImGui.endTable();
        }

        // Ensure popID is called regardless of table success
        ImGui.popID();

        return text;
    }

    // - - - Enum Input - - -
    public static <T extends Enum<T>> T drawEnumControls(Class<T> enumClass, String label, T currentEnum)
    {
        String[] enumValues      = getEnumValues(enumClass);
        String   currentEnumName = currentEnum != null ? currentEnum.name() : null;

        ImGui.pushID(label);

        // Using ImGui.table for proper alignment instead of ImGui.columns
        if (ImGui.beginTable("##tableEnumControl", 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.text(label);
            resetDefaultTextColor();

            ImGui.tableNextColumn();
            ImInt index = new ImInt(indexOf(currentEnumName, enumValues));

            //ImGui.setCursorPos(ImGui.getCursorPosX() + EditorSystemManager.getCurrentTheme().framePadding.x, ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
            if (ImGui.combo("##" + label, index, enumValues, enumValues.length))
            {
                ImGui.endTable();
                ImGui.popID();
                return enumClass.getEnumConstants()[index.get()];
            }

            ImGui.endTable();
        }

        ImGui.popID();
        return currentEnum;
    }


    private static <T extends Enum<T>> String[] getEnumValues(Class<T> enumClass)
    {
        String[] enumValues = new String[enumClass.getEnumConstants().length];
        int i = 0;
        for (T enumConstant : enumClass.getEnumConstants())
        {
            enumValues[i] = enumConstant.name();
            i++;
        }
        return enumValues;
    }

    private static int indexOf(String str, String[] arr)
    {
        for (int i = 0; i < arr.length; i++)
        {
            if (str.equals(arr[i]))
            {
                return i;
            }
        }
        return -1; // Return -1 if not found
    }

    // - - - Utility method to set button styles - - -
    private static void setButtonStyle(float r, float g, float b)
    {
        ImGui.pushStyleColor(ImGuiCol.Button, r, g, b, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, r - 0.0f, g - 0.05f, b - 0.05f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, r + 0.05f, g + 0.05f, b + 0.05f, 1.0f);
    }
}