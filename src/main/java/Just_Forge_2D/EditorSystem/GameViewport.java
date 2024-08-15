package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.CoreSystems.EventSystem.EventManager;
import Just_Forge_2D.CoreSystems.EventSystem.Events.Event;
import Just_Forge_2D.CoreSystems.EventSystem.Events.EventTypes;
import Just_Forge_2D.CoreSystems.InputSystem.Mouse;
import Just_Forge_2D.CoreSystems.ForgeDynamo;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class GameViewport
{
    private float leftX, rightX, topY, bottomY;
    private boolean isPlaying = false;

    public void gui()
    {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar);

        ImGui.beginMenuBar();
        if (ImGui.menuItem("Play", "", isPlaying, !isPlaying))
        {
            isPlaying = true;
            EventManager.notify(null, new Event(EventTypes.ForgeStart));
        }
        if (ImGui.menuItem("Stop", "", !isPlaying, isPlaying))
        {
            isPlaying = false;
            EventManager.notify(null, new Event(EventTypes.ForgeStop));
        }
        ImGui.endMenuBar();

        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);
        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();
        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        Mouse.setGameViewport(new Vector2f(topLeft.x, topLeft.y), new Vector2f(windowSize.x, windowSize.y));

        int textureId = ForgeDynamo.getFramebuffer().getTextureID();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        ImGui.end();
    }

    private ImVec2 getLargestSizeForViewport()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / ForgeDynamo.getAspectRatio();
        if (aspectHeight > windowSize.y)
        {
            // - - - switch to pillar mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * ForgeDynamo.getAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 ASPECT_SIZE)
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.x -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (ASPECT_SIZE.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (ASPECT_SIZE.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    public boolean getWantCaptureMouse()
    {
        return Mouse.getX() >= leftX && Mouse.getX() <= rightX && Mouse.getY() >= bottomY && Mouse.getY() <= topY;
    }
}
