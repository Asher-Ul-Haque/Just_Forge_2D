package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.Themes.Theme;
import Just_Forge_2D.Utils.Settings;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiTableFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import imgui.type.ImString;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static Just_Forge_2D.Themes.Theme.resetDefaultTextColor;
import static Just_Forge_2D.Themes.Theme.setDefaultTextColor;


public class Widgets
{

    private static final float DEFAULT_RESET = 0.0f;


    // - - - Vec2 Control - - -
    public static void drawVec2Control(String label, Vector2f values, float resetValue, float JUMP)
    {
        ImGui.pushID(label);

        if (ImGui.beginTable("##Vec2Table", 2, ImGuiTableFlags.SizingStretchSame))
        {
            ImGui.tableNextColumn();
            ImGui.alignTextToFramePadding();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
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
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
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
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
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
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
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
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
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
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
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
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
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
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
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

        if (ImGui.beginTable("##tableEnumControl", 2, ImGuiTableFlags.NoPadOuterX))
        {
            ImGui.tableNextColumn();
            setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
            ImGui.text(label);
            resetDefaultTextColor();

            ImGui.tableNextColumn();
            ImInt index = new ImInt(indexOf(currentEnumName, enumValues));

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

    public static boolean button(String LABEL, boolean CENTER)
    {
        LABEL = " " + LABEL + " ";
        ImVec2 framePadding = EditorSystemManager.getCurrentTheme().framePadding;
        String mainLabel = LABEL.contains("##") ? LABEL.substring(0, LABEL.indexOf("##")) : LABEL;
        float textWidth = ImGui.calcTextSize(mainLabel).x + ImGui.getStyle().getFramePaddingX() * 2;
        if (CENTER)
        {
            ImGui.setCursorPosX((ImGui.getContentRegionAvailX() - textWidth) * 0.5f);
        }
        if (LABEL.length() < 50) return ImGui.button(LABEL, 0 ,ImGui.getFontSize() + (framePadding.y * 2));
        return ImGui.button(LABEL);
    }

    public static boolean button(String LABEL)
    {
        return button(LABEL, false);
    }

    public static boolean imageButton(int TEXTURE_ID, float SIZE_X, float SIZE_Y, float UV0_X, float UV0_Y, float UV1_X, float UV1_Y, boolean CENTER)
    {
        if (SIZE_X < Settings.MAX_IMAGE_DISPLAY_HEIGHT() && SIZE_Y < Settings.MAX_IMAGE_DISPLAY_WIDTH())
        {
            if (CENTER) ImGui.setCursorPosX(Math.abs(ImGui.getContentRegionAvailX() - SIZE_X) / 2f);
            return ImGui.imageButton(TEXTURE_ID, SIZE_X, SIZE_Y, UV0_X, UV0_Y, UV1_X, UV1_Y);
        }

        float scaleFactor = Math.min(Settings.MAX_IMAGE_DISPLAY_WIDTH() / SIZE_X, Settings.MAX_IMAGE_DISPLAY_HEIGHT() / (SIZE_Y));

        float displayWidth = SIZE_X * scaleFactor;
        float displayHeight = SIZE_Y * scaleFactor;

        if (CENTER) ImGui.setCursorPosX(Math.abs(ImGui.getContentRegionAvailX() - displayWidth) / 2f);
        return ImGui.imageButton(TEXTURE_ID, displayWidth, displayHeight, UV0_X, UV0_Y, UV1_X, UV1_Y);
    }

    public static boolean imageButton(int TEXTURE_ID, float SIZE_X, float SIZE_Y, float UV0_X, float UV0_Y, float UV1_X, float UV1_Y)
    {
        return imageButton(TEXTURE_ID, SIZE_X, SIZE_Y, UV0_X, UV0_Y, UV1_X, UV1_Y, false);
    }

    public static boolean imageButton(int TEXTURE_ID, float SIZE_X, float SIZE_Y, Vector2f[] TEXTURE_COORDS, boolean CENTER)
    {
        return Widgets.imageButton(TEXTURE_ID, SIZE_X, SIZE_Y, TEXTURE_COORDS[2].x, TEXTURE_COORDS[0].y, TEXTURE_COORDS[0].x, TEXTURE_COORDS[2].y, CENTER);
    }

    public static boolean imageButton(int TEXTURE_ID, float SIZE_X, float SIZE_Y, Vector2f[] TEXTURE_COORDS)
    {
        return Widgets.imageButton(TEXTURE_ID, SIZE_X, SIZE_Y, TEXTURE_COORDS[2].x, TEXTURE_COORDS[0].y, TEXTURE_COORDS[0].x, TEXTURE_COORDS[2].y, false);
    }

    public static void image(int TEXTURE_ID, float SIZE_X, float SIZE_Y, float UV0_X, float UV0_Y, float UV1_X, float UV1_Y, boolean CENTER)
    {
        if (SIZE_X < Settings.MAX_IMAGE_DISPLAY_HEIGHT() && SIZE_Y < Settings.MAX_IMAGE_DISPLAY_WIDTH())
        {
            if (CENTER) ImGui.setCursorPosX(Math.abs(ImGui.getContentRegionAvailX() - SIZE_X) / 2f);
            ImGui.image(TEXTURE_ID, SIZE_X, SIZE_Y, UV0_X, UV0_Y, UV1_X, UV1_Y);
            return;
        }

        float scaleFactor = Math.min(Settings.MAX_IMAGE_DISPLAY_WIDTH() / SIZE_X, Settings.MAX_IMAGE_DISPLAY_HEIGHT() / (SIZE_Y));

        float displayWidth = SIZE_X * scaleFactor;
        float displayHeight = SIZE_Y * scaleFactor;

        if (CENTER) ImGui.setCursorPosX(Math.abs(ImGui.getContentRegionAvailX() - displayWidth) / 2f);
        ImGui.image(TEXTURE_ID, displayWidth, displayHeight, UV0_X, UV0_Y, UV1_X, UV1_Y);
    }

    public static void image(int TEXTURE_ID, float SIZE_X, float SIZE_Y, float UV0_X, float UV0_Y, float UV1_X, float UV1_Y)
    {
        image(TEXTURE_ID, SIZE_X, SIZE_Y, UV0_X, UV0_Y, UV1_X, UV1_Y, false);
    }

    public static void image(int TEXTURE_ID, float SIZE_X, float SIZE_Y, Vector2f[] TEXTURE_COORDS)
    {
        Widgets.image(TEXTURE_ID, SIZE_X, SIZE_Y, TEXTURE_COORDS[2].x, TEXTURE_COORDS[0].y, TEXTURE_COORDS[0].x, TEXTURE_COORDS[2].y, false);
    }

    public static void image(int TEXTURE_ID, float SIZE_X, float SIZE_Y, Vector2f[] TEXTURE_COORDS, boolean CENTER)
    {
        Widgets.image(TEXTURE_ID, SIZE_X, SIZE_Y, TEXTURE_COORDS[2].x, TEXTURE_COORDS[0].y, TEXTURE_COORDS[0].x, TEXTURE_COORDS[2].y, CENTER);
    }


    public static void text(String TEXT)
    {
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().textColor);
        ImGui.text(TEXT);
        Theme.resetDefaultTextColor();
    }

    public static void text(String TEXT, Vector4f COLOR)
    {
        ImGui.textColored(COLOR.x * 256, COLOR.y * 256, COLOR.z * 256, COLOR.w * 256, TEXT);
    }


    public static enum PopupReturn
    {
        OK,
        CANCEL,
        NO_INPUT
    }
    // - - - Popup
    public static PopupReturn popUp(String ICON, String TITLE, String TEXT, Vector2f POSITION, Vector2f SIZE, Runnable EXTRA)
    {
        PopupReturn result = PopupReturn.NO_INPUT;

        if (!ImGui.isPopupOpen("##" + TITLE))
        {
            ImGui.openPopup("##" + TITLE);
        }

        if (POSITION != null) ImGui.setNextWindowPos(POSITION.x, POSITION.y);
        else
        {
            float centerX = ImGui.getMainViewport().getSizeX() / 2;
            ImGui.setNextWindowPos(centerX, 50, ImGuiCond.Appearing, 0.5f, 0);
        }

        if (SIZE != null) ImGui.setNextWindowSize(SIZE.x, SIZE.y);

        if (ImGui.beginPopupModal("##" + TITLE, ImGuiWindowFlags.NoTitleBar))
        {
            // - - - Display text
            float windowWidth = ImGui.getWindowWidth();
            if (ICON != null)
            {
                Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().textAltColor);
                ImGui.text(ICON);
                ImGui.sameLine();
                float titleWidth = ImGui.calcTextSize(TITLE).x;
                float iconWidth = ImGui.calcTextSize(ICON).x + ImGui.getStyle().getItemSpacing().x;
                float padding = (windowWidth - (titleWidth + iconWidth)) / 2;
                if (padding > 0) ImGui.setCursorPosX(ImGui.getCursorPosX() + padding);
                ImGui.pushFont(ImGUIManager.interExtraBold);
                ImGui.text(TITLE);
                ImGui.popFont();
                Widgets.text("");
                ImGui.text(TEXT);
                Theme.resetDefaultTextColor();
            }

            // - - - Run extra custom input
            if (EXTRA != null) EXTRA.run();

            // - - - Calculate button widths
            float okButtonWidth = ImGui.calcTextSize(Icons.Check + " OK").x + ImGui.getStyle().getItemSpacing().x * 2; // Add padding
            float cancelButtonWidth = ImGui.calcTextSize(Icons.Ban + " Cancel").x + ImGui.getStyle().getItemSpacing().x * 2; // Add padding
            float totalButtonWidth = okButtonWidth + cancelButtonWidth; // Total width of both buttons
            float buttonSpacing = windowWidth / 5; // Space between the two buttons

            // Calculate the starting X position for the buttons to be centered
            float buttonsStartX = (windowWidth - totalButtonWidth - buttonSpacing) / 2;

            // - - - Set cursor for OK button and display it
            ImGui.newLine();
            ImGui.setCursorPosX(buttonsStartX);
            if (Widgets.button(Icons.Check + " OK"))
            {
                ImGui.closeCurrentPopup();
                result = PopupReturn.OK;
            }

            ImGui.sameLine();

            // - - - Set cursor for Cancel button and display it
            ImGui.setCursorPosX(buttonsStartX + okButtonWidth + buttonSpacing); // - - - Add the width of the OK button + spacing
            if (Widgets.button(Icons.Ban + " Cancel"))
            {
                ImGui.closeCurrentPopup();
                result = PopupReturn.CANCEL;
            }

            ImGui.endPopup();
        }
        return result;
    }


    public static PopupReturn popup(String ICON, String TITLE, String TEXT, Vector2f SIZE, Runnable EXTRA)
    {
        return popUp(ICON, TITLE, TEXT, null, SIZE, EXTRA);
    }

    public static PopupReturn popUp(String ICON, String TITLE, String TEXT, Runnable EXTRA)
    {
        return popUp(ICON, TITLE, TEXT,null, null, EXTRA);
    }

    public static PopupReturn popUp(Runnable EXTRA)
    {
        return popup(null, null, null, null, EXTRA);
    }

    public static PopupReturn popUp(String ICON, String TITLE, String TEXT)
    {
        return popUp(ICON, TITLE, TEXT, null, null, null);
    }

    public static PopupReturn popUp(String ICON, String TITLE, String TEXT, Vector2f POSITION, Vector2f SIZE)
    {
        return popUp(ICON, TITLE, TEXT, POSITION, SIZE, null);
    }

    public static PopupReturn popUp(String ICON, String TITLE, String TEXT,  Vector2f SIZE)
    {
        return popUp(ICON, TITLE, TEXT, null, SIZE, null);
    }
}