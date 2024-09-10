package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.InputSystem.Mouse;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class GameViewport
{
    private static float leftX, rightX, topY, bottomY;
    private static boolean isPlaying = false;

    public static void render() {
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.NoTitleBar);

        // Calculate the size and centered position for the viewport
        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        // Set the cursor position for the image below the buttons
        ImGui.setCursorPos(windowPos.x, windowPos.y);  // Move down to fit the buttons' height (adjust as needed)

        // Capture the top-left corner of the viewport to set the game viewport boundaries
        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();
        leftX = topLeft.x;
        bottomY = topLeft.y;
        rightX = topLeft.x + windowSize.x;
        topY = topLeft.y + windowSize.y;

        // Update mouse viewport
        Mouse.setGameViewport(new Vector2f(topLeft.x, topLeft.y), new Vector2f(windowSize.x, windowSize.y));

        // Render the game image (framebuffer)
        int textureId = EditorSystemManager.getFramebuffer().getTextureID();
        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        float buttonWidth = 64f;  // Approximate width for buttons (adjust as needed)
        float spacing = 8f;       // Spacing between buttons (adjust as needed)

        // Center the buttons horizontally by calculating their position based on window width
        float totalButtonWidth = (2 * buttonWidth) + spacing;  // Two buttons with spacing
        float buttonStartX = (windowSize.x - totalButtonWidth) / 2;

        // Set the cursor position for the first button
        ImGui.setCursorPos(windowPos.x + buttonStartX, windowPos.y);  // Top-center aligned buttons

        // Render the "Play" button
        if (ImGui.button("Play", buttonWidth, 30)) {
            isPlaying = true;
            EventManager.notify(null, new Event(EventTypes.ForgeStart));
        }

        // Same line to position the "Stop" button next to the "Play" button
        ImGui.sameLine(0, spacing);

        // Render the "Stop" button
        if (ImGui.button("Stop", buttonWidth, 30))
        {
            isPlaying = false;
            EventManager.notify(null, new Event(EventTypes.ForgeStop));
        }

        ImGui.end();
    }


    private static ImVec2 getLargestSizeForViewport()
    {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / MainWindow.get().getAspectRatio();
        if (aspectHeight > windowSize.y)
        {
            // - - - switch to pillar mode
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * MainWindow.get().getAspectRatio();
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
        return Mouse.getX() >= leftX && Mouse.getX() <= rightX && Mouse.getY() >= bottomY && Mouse.getY() <= topY;
    }
}
