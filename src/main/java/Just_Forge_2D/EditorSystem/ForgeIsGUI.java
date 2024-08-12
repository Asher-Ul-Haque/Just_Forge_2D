package Just_Forge_2D.EditorSystem;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

// - - - Custom ImGUI
public class ForgeIsGUI
{
    // - - - private variables
    private static final float defaultWidth = 110.0f;
    private static final float defaultReset = 0.0f;


    // - - - | Customization | - - -


    // - - - Vec2 control - - -

    public static void drawVec2Control(String LABEL, Vector2f VALUES, float RESELT_VALUE, float COLUMN_WIDTH)
    {
        ImGui.pushID(LABEL);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, COLUMN_WIDTH);
        ImGui.text(LABEL);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);

        if (ImGui.button("X", buttonSize.x, buttonSize.y))
        {
            VALUES.x = RESELT_VALUE;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {VALUES.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.4f, 0.9f, 0.4f, 1.0f);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y))
        {
            VALUES.y = RESELT_VALUE;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {VALUES.y};
        ImGui.dragFloat("##y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        VALUES.x = vecValuesX[0];
        VALUES.y = vecValuesY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static void drawVec2Control(String LABEL, Vector2f VALUES)
    {
        drawVec2Control(LABEL, VALUES, defaultReset, defaultWidth);
    }

    public static void drawVec2Control(String LABEL, Vector2f VALUES, float RESET_VALUE)
    {
        drawVec2Control(LABEL, VALUES, RESET_VALUE, defaultWidth);
    }


    // - - - Vec3 - - -

    public static void drawVec3(String LABEL, Vector3f VALUES, float RESET_VALUE, float COLUMN_WIDTH)
    {
        ImGui.pushID(LABEL);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, COLUMN_WIDTH);
        ImGui.text(LABEL);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 3.0f) / 3.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        if (ImGui.button("x", buttonSize.x, buttonSize.y))
        {
            VALUES.x = RESET_VALUE;
        }
        ImGui.popStyleVar(3);

        ImGui.sameLine();
        float[] vecValuesX = {VALUES.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y))
        {
            VALUES.y = RESET_VALUE;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {VALUES.y};
        ImGui.dragFloat("##Y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.columns(1);
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.1f, 0.25f, 0.8f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.2f, 0.35f, 0.9f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.1f, 0.25f, 0.8f, 1.0f);
        if (ImGui.button("Z", buttonSize.x, buttonSize.y))
        {
            VALUES.x = RESET_VALUE;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesZ = {VALUES.z};
        ImGui.dragFloat("##Z", vecValuesZ, 0.1f);
        ImGui.popItemWidth();
        ImGui.columns(1);

        VALUES.x = vecValuesX[0];
        VALUES.y = vecValuesY[0];
        VALUES.z = vecValuesZ[0];

        ImGui.popStyleVar();
        ImGui.popID();
    }

    public static void drawVec3(String LABEL, Vector3f VALUES, float RESET_VALUE)
    {
        drawVec3(LABEL, VALUES, RESET_VALUE, defaultWidth);
    }

    public static void drawVec3(String LABEL, Vector3f VALUES)
    {
        drawVec3(LABEL, VALUES, defaultReset, defaultWidth);
    }

    // - - - Drag Float
    public static float drawFloatControl(String LABEL, float VALUE)
    {
        ImGui.pushID(LABEL);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultWidth);
        ImGui.text(LABEL);
        ImGui.nextColumn();

        float[] valArr = {VALUE};
        ImGui.dragFloat("##dragFloat", valArr, 0.1f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    // - - - int
    public static int drawIntControl(String LABEL, int VALUE)
    {
        ImGui.pushID(LABEL);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultWidth);
        ImGui.text(LABEL);
        ImGui.nextColumn();

        int[] valArr = {VALUE};
        ImGui.dragInt("##dragFloat", valArr, 1);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }


    // - - - COLOR PICKER

    public static boolean colorPicker4(String LABEL, Vector4f COLOR)
    {
        boolean result = false;
        ImGui.pushID(LABEL);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultWidth);
        ImGui.text(LABEL);
        ImGui.nextColumn();

        float[] valArray = {COLOR.x, COLOR.y, COLOR.z, COLOR.w};
        if (ImGui.colorEdit4("##Color Picker", valArray))
        {
            COLOR.set(valArray[0], valArray[1], valArray[2], valArray[3]);
            result = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return result;
    }

    public static String inputText(String LABEL, String TEXT)
    {
        ImGui.pushID(LABEL);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultWidth);
        ImGui.text(LABEL);
        ImGui.nextColumn();

        ImString outString = new ImString(TEXT, 256);
        if (ImGui.inputText("##" + LABEL, outString))
        {
            ImGui.columns(1);
            ImGui.popID();

            return outString.get();
        }

        ImGui.columns(1);
        ImGui.popID();

        return TEXT;
    }
}