package Just_Forge_2D.EditorSystem;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Widgets {

    private static final float DEFAULT_WIDTH = 128.0f;
    private static final float DEFAULT_RESET = 0.0f;

    // - - - Utility method for setting default text color - - -
    private static void setDefaultTextColor() {
        ImGui.pushStyleColor(ImGuiCol.Text, 0.0f, 0.0f, 0.0f, 1.0f);  // Always set text color to black
    }

    private static void resetDefaultTextColor() {
        ImGui.popStyleColor();
    }

    // - - - Vec2 Control - - -
    public static void drawVec2Control(String label, Vector2f values, float resetValue, float columnWidth)
    {
        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        setDefaultTextColor();
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 8.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        // X Control
        ImGui.pushItemWidth(widthEach);
        setButtonStyle(0.8f, 0.1f, 0.15f);
        if (ImGui.button("X", buttonSize.x, buttonSize.y)) {
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

        values.set(vecValuesX[0], vecValuesY[0]);

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec2Control(String label, Vector2f values) {
        drawVec2Control(label, values, DEFAULT_RESET, DEFAULT_WIDTH);
    }

    public static void drawVec2Control(String label, Vector2f values, float resetValue) {
        drawVec2Control(label, values, resetValue, DEFAULT_WIDTH);
    }

    // - - - Vec3 Control - - -
    public static void drawVec3Control(String label, Vector3f values, float resetValue, float columnWidth) {
        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        setDefaultTextColor();
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 3.0f) / 3.0f;

        // X Control
        ImGui.pushItemWidth(widthEach);
        setButtonStyle(0.8f, 0.1f, 0.15f);
        if (ImGui.button("X", buttonSize.x, buttonSize.y)) {
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

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec3Control(String label, Vector3f values) {
        drawVec3Control(label, values, DEFAULT_RESET, DEFAULT_WIDTH);
    }

    public static void drawVec3Control(String label, Vector3f values, float resetValue) {
        drawVec3Control(label, values, resetValue, DEFAULT_WIDTH);
    }

    // - - - Float Control - - -
    public static float drawFloatControl(String label, float value) {
        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_WIDTH);
        setDefaultTextColor();
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        float[] valArr = {value};
        ImGui.dragFloat("##DragFloat", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    // - - - Int Control - - -
    public static int drawIntControl(String label, int value) {
        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_WIDTH);
        setDefaultTextColor();
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        int[] valArr = {value};
        ImGui.dragInt("##DragInt", valArr, 1);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    // - - - Color Picker - - -
    public static boolean colorPicker4(String label, Vector4f color) {
        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_WIDTH);
        setDefaultTextColor();
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        float[] colorArr = {color.x, color.y, color.z, color.w};
        boolean changed = ImGui.colorEdit4("##ColorPicker", colorArr);
        if (changed) {
            color.set(colorArr[0], colorArr[1], colorArr[2], colorArr[3]);
        }

        ImGui.columns(1);
        ImGui.popID();

        return changed;
    }

    // - - - Text Input - - -
    public static String inputText(String label, String text) {
        ImGui.pushID(label);
        ImGui.columns(2);
        ImGui.setColumnWidth(0, DEFAULT_WIDTH);
        setDefaultTextColor();
        ImGui.text(label);
        resetDefaultTextColor();
        ImGui.nextColumn();

        ImString imString = new ImString(text, 256);
        if (ImGui.inputText("##" + label, imString)) {
            text = imString.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return text;
    }

    // - - - Utility method to set button styles - - -
    private static void setButtonStyle(float r, float g, float b) {
        ImGui.pushStyleColor(ImGuiCol.Button, r, g, b, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, r - 0.0f, g - 0.05f, b - 0.05f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, r + 0.05f, g + 0.05f, b + 0.05f, 1.0f);
    }
}