package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.RenderingSystems.Framebuffer;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class GameViewport
{
    public static void render()
    {
        Framebuffer buffer = EditorManager.editorScreen.getFramebuffer();
        ImGui.begin("Framebuffer Viewer", ImGuiWindowFlags.AlwaysAutoResize);

        // Get the largest possible size for the viewport maintaining aspect ratio
        ImVec2 aspectSize = getLargestSize(buffer.getWidth(), buffer.getHeight());

        // Calculate centered position
        ImVec2 centeredPos = getCenteredPos(aspectSize);

        // Set cursor position to the centered position
        ImGui.setCursorPos(centeredPos.x, centeredPos.y);

        // Bind the framebuffer texture
        int textureID = buffer.getTextureID();
        ImGui.image(textureID, aspectSize.x, aspectSize.y, 0, 1, 1, 0);

        ImGui.end();
    }

    private static ImVec2 getLargestSize(int bufferWidth, int bufferHeight)
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float aspectRatio = (float) bufferWidth / bufferHeight;

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / aspectRatio;

        if (aspectHeight > windowSize.y)
        {
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * aspectRatio;
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 getCenteredPos(ImVec2 aspectSize)
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);

        float viewportX = (windowSize.x / 2.0f) - (aspectSize.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (aspectSize.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }
}
