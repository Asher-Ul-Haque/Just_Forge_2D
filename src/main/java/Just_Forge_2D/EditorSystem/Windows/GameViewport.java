package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class GameViewport
{
    private static float leftX, rightX, topY, bottomY;
    private static boolean isPlaying = false;
    private static boolean windowIsHovered = false;

    public static void render()
    {
        ImGui.begin(Icons.Gamepad + "  Game Viewport", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse |  ImGuiWindowFlags.NoDecoration);

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

        int textureId = GameWindow.getFrameBuffer().getTextureID();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);
        windowIsHovered = ImGui.isItemHovered();

        float buttonWidth = 80f;
        float buttonPadding = 8f;

        float buttonStartX = (windowSize.x - (buttonWidth * 2) + buttonPadding) / 2;

        ImGui.setCursorPos(windowPos.x + buttonStartX, 0);

        if (ImGui.button(isPlaying ? Icons.StopCircle + " Stop" : Icons.PlayCircle + " Start", buttonWidth, 36))
        {
            isPlaying = !isPlaying;
            EventManager.notify(null, new Event(isPlaying ? EventTypes.ForgeStart : EventTypes.ForgeStop));
            windowIsHovered = false;
        }
        if (ImGui.isItemHovered()) windowIsHovered = false;

        buttonStartX += buttonWidth + buttonPadding;

        ImGui.setCursorPos(windowPos.x + buttonStartX, 0);

        if (ImGui.button(SceneSystemManager.isRunning(GameWindow.getCurrentScene()) ? Icons.Pause + " Pause" : Icons.Play + " Play", buttonWidth, 36))
        {
            SceneSystemManager.setPause(GameWindow.getCurrentScene(), !SceneSystemManager.isRunning(GameWindow.getCurrentScene()));
            windowIsHovered = false;
        }
        if (ImGui.isItemHovered()) windowIsHovered = false;
        ImGui.end();
    }


    private static ImVec2 getLargestSizeForViewport()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / ((float) GameWindow.getFrameBuffer().getSize().x / GameWindow.getFrameBuffer().getSize().y);
        if (aspectHeight > windowSize.y)
        {
            // - - - switch to pillar mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * ((float) GameWindow.getFrameBuffer().getSize().x / GameWindow.getFrameBuffer().getSize().y);
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }

    private static ImVec2 getCenteredPositionForViewport(ImVec2 ASPECT_SIZE)
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.x -= ImGui.getScrollY();

        float viewportX = (windowSize.x / 2.0f) - (ASPECT_SIZE.x / 2.0f);
        float viewportY = (windowSize.y / 2.0f) - (ASPECT_SIZE.y / 2.0f);

        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    public static boolean getWantCaptureMouse()
    {
        return windowIsHovered;
    }
}