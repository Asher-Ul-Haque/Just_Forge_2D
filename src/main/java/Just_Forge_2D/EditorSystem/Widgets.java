package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.EditorSystem.Themes.Theme;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static Just_Forge_2D.EditorSystem.Themes.Theme.resetDefaultTextColor;
import static Just_Forge_2D.EditorSystem.Themes.Theme.setDefaultTextColor;

public class Widgets {

    private static final float DEFAULT_RESET = 0.0f;


    // - - - Vec2 Control - - -
    public static void drawVec2Control(String label, Vector2f values, float resetValue)
    {
        ImGui.pushID(label);
        ImGui.columns(2);
        setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        float lineHeight = ImGui.getFontSize() + EditorSystemManager.getCurrentTheme().framePadding.y * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 8.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        // X Control
        ImGui.pushItemWidth(widthEach);
        setButtonStyle(0.8f, 0.1f, 0.15f);
        ImGui.setCursorPos(ImGui.getCursorPosX() + EditorSystemManager.getCurrentTheme().framePadding.x ,ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
        if (ImGui.button("X", buttonSize.x, buttonSize.y))
        {
            values.x = resetValue;
        }

        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##X", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        // Y Control
        ImGui.pushItemWidth(widthEach);
        setButtonStyle(0.2f, 0.7f, 0.2f);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y))
        {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##Y", vecValuesY, 0.1f);
        ImGui.popItemWidth();

        values.set(vecValuesX[0], vecValuesY[0]);

        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec2Control(String label, Vector2f values) {
        drawVec2Control(label, values, DEFAULT_RESET);
    }

    // - - - Vec3 Control - - -
    public static void drawVec3Control(String label, Vector3f values, float resetValue)
    {
        ImGui.pushID(label);
        ImGui.columns(2);

        setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 3.0f) / 3.0f;

        // X Control
        ImGui.pushItemWidth(widthEach);
        ImGui.setCursorPos(ImGui.getCursorPosX() + EditorSystemManager.getCurrentTheme().framePadding.x ,ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
        setButtonStyle(0.8f, 0.1f, 0.15f);
        if (ImGui.button("X", buttonSize.x, buttonSize.y))
        {
            values.x = resetValue;
        }
        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] vecValuesX = {values.x};
        ImGui.dragFloat("##X", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        // Y Control
        ImGui.pushItemWidth(widthEach);
        setButtonStyle(0.2f, 0.7f, 0.2f);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y)) {
            values.y = resetValue;
        }
        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] vecValuesY = {values.y};
        ImGui.dragFloat("##Y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        // Z Control
        ImGui.pushItemWidth(widthEach);
        setButtonStyle(0.1f, 0.25f, 0.8f);
        if (ImGui.button("Z", buttonSize.x, buttonSize.y)) {
            values.z = resetValue;
        }
        ImGui.popStyleColor(3);
        ImGui.sameLine();
        float[] vecValuesZ = {values.z};
        ImGui.dragFloat("##Z", vecValuesZ, 0.1f);
        ImGui.popItemWidth();

        values.set(vecValuesX[0], vecValuesY[0], vecValuesZ[0]);

        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec3Control(String label, Vector3f values)
    {
        drawVec3Control(label, values, DEFAULT_RESET);
    }


    // - - - Float Control - - -

    public static float drawFloatControl(String label, float value)
    {
        ImGui.pushID(label);
        ImGui.columns(2);
        setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        float[] valArr = {value};
        ImGui.setCursorPos(ImGui.getCursorPosX() + EditorSystemManager.getCurrentTheme().framePadding.x ,ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
        ImGui.dragFloat("##DragFloat", valArr, 0.05f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    // - - - Int Control - - -
    public static int drawIntControl(String label, int value)
    {
        ImGui.pushID(label);
        ImGui.columns(2);
        setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        int[] valArr = {value};
        ImGui.setCursorPos(ImGui.getCursorPosX() + EditorSystemManager.getCurrentTheme().framePadding.x ,ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
        ImGui.dragInt("##DragInt", valArr, 1);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    // - - - Color Picker - - -
    public static boolean colorPicker4(String label, Vector4f color)
    {
        ImGui.pushID(label);
        ImGui.columns(2);
        setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        float[] colorArr = {color.x, color.y, color.z, color.w};
        boolean changed = ImGui.colorEdit4("##ColorPicker", colorArr);
        if (changed)
        {
            color.set(colorArr[0], colorArr[1], colorArr[2], colorArr[3]);
        }

        ImGui.columns(1);
        ImGui.popID();

        return changed;
    }

    public static boolean colorPicker3(String label, Vector3f COLOR)
    {
        ImGui.pushID(label);
        ImGui.columns(2);
        setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        float[] colorArr = {COLOR.x, COLOR.y, COLOR.z};
        boolean changed = ImGui.colorEdit4("##ColorPicker", colorArr);
        if (changed)
        {
            COLOR.set(colorArr[0], colorArr[1], colorArr[2]);
        }

        ImGui.columns(1);
        ImGui.popID();

        return changed;
    }

    public static boolean drawBoolControl(String LABEL, boolean VALUE)
    {
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        if (ImGui.checkbox( LABEL, VALUE))
        {
            VALUE = !VALUE;
        }
        Theme.resetDefaultTextColor();
        return VALUE;
    }

        // - - - Text Input - - -
    public static String inputText(String label, String text)
    {
        ImGui.pushID(label);
        ImGui.columns(2);
        setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        ImString imString = new ImString(text, 256);
        if (ImGui.inputText("##" + label, imString))
        {
            text = imString.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return text;
    }

    // - - - Enum Input - - -
    public static <T extends Enum<T>> T drawEnumControls(Class<T> ENUM_CLASS, String LABEL, T CURRENT_ENUM)
    {
        String[] enumValues = getEnumValues(ENUM_CLASS);
        String currentEnumName = CURRENT_ENUM != null ? CURRENT_ENUM.name() : null;

        ImGui.pushID(LABEL);
        ImGui.columns(2);
        setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
        ImGui.text(LABEL);
        resetDefaultTextColor();

        ImGui.nextColumn();
        ImInt index = new ImInt(indexOf(currentEnumName, enumValues));

        if (ImGui.combo(LABEL, index, enumValues, enumValues.length))
        {
            ImGui.columns(1);
            ImGui.popID();
            return ENUM_CLASS.getEnumConstants()[index.get()];
        }

        ImGui.columns(1);
        ImGui.popID();
        return CURRENT_ENUM;
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