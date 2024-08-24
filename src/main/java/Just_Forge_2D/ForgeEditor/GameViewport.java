package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.Forge;
import Just_Forge_2D.RenderingSystems.Framebuffer;
import Just_Forge_2D.WindowSystem.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class GameViewport
{
    private final Window owner;

    public GameViewport(Window OWNER)
    {
        this.owner = OWNER;
    }

    public void render()
    {
        ImGui.begin("Framebuffer Viewer", ImGuiWindowFlags.AlwaysAutoResize);

        // Get the size of the available content region
        float contentWidth = ImGui.getContentRegionAvailX();
        float contentHeight = ImGui.getContentRegionAvailY();

        // Calculate aspect ratio of the framebuffer
        float aspectRatio = (float) owner.getFramebuffer().getWidth() / owner.getFramebuffer().getHeight();

        // Adjust size based on aspect ratio
        if (contentWidth / contentHeight > aspectRatio)
        {
            contentWidth = contentHeight * aspectRatio;
        }
        else
        {
            contentHeight = contentWidth / aspectRatio;
        }

        // Bind the framebuffer texture
        int textureID = owner.getFramebuffer().getTextureID();

        // Display the image using ImGui::Image
        ImGui.image(textureID, contentWidth, contentHeight, 0, 1, 1, 0);

        ImGui.end();
    }

    private ImVec2 getLargestSize()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);


        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / (16f / 9f);
        if (aspectHeight > windowSize.y)
        {
            // - - - switch to pillar mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * (16f / 9f);
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPos(ImVec2 ASPECT_SIZE)
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.x -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (ASPECT_SIZE.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (ASPECT_SIZE.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }
}