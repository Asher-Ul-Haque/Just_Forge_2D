package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import org.joml.Vector2i;
import org.joml.Vector4f;

public class WindowManagerComponent extends Component
{
    protected Vector4f cacheBgColor = new Vector4f(GameWindow.get().getClearColor());
    protected Vector2i cacheSize = new Vector2i(GameWindow.get().getWidth(), GameWindow.get().getHeight());
    protected boolean cacheDecorated = GameWindow.get().isDecorated();
    protected boolean cacheVsync = GameWindow.get().isVsync();
    protected float cacheOpacity = GameWindow.get().getOpacity();
    protected String cacheTitle = GameWindow.get().getTitle();
    protected Vector2i cachePosition = new Vector2i(GameWindow.get().getXPosition(), GameWindow.get().getYPosition());

    // Wrapper for setting the window size
    public void setWindowSize(Vector2i size)
    {
        this.cacheSize.set(size);
        GameWindow.get().setSize(size.x, size.y);
    }

    // Wrapper for setting the background color
    public void setBackgroundColor(Vector4f color)
    {
        this.cacheBgColor.set(color);
        GameWindow.get().setClearColor(color);
    }


    // Wrapper for window decoration
    public void setDecorated(boolean decorated)
    {
        this.cacheDecorated = decorated;
        GameWindow.get().setDecorated(decorated);
    }


    // Wrapper for VSync
    public void setVsync(boolean vsync) {
        this.cacheVsync = vsync;
        GameWindow.get().setVsync(vsync);
    }

    // Wrapper for opacity
    public void setOpacity(float opacity) {
        this.cacheOpacity = opacity;
        GameWindow.get().setOpacity(opacity);
    }

    // Wrapper for setting the window title
    public void setTitle(String title) {
        this.cacheTitle = title;
        GameWindow.get().setTitle(title);
    }

    // Wrapper for setting position
    public void setPosition(Vector2i position) {
        this.cachePosition.set(position);
        GameWindow.get().setPosition(position.x, position.y);
    }

    // Synchronize all cached settings with the GameWindow
    public void sync()
    {
        GameWindow gameWindow = GameWindow.get();
        gameWindow.setSize(cacheSize.x, cacheSize.y);
        gameWindow.setClearColor(cacheBgColor);
        gameWindow.setDecorated(cacheDecorated);
        gameWindow.setTitle(cacheTitle);
        gameWindow.setPosition(cachePosition.x, cachePosition.y);
        gameWindow.setVsync(cacheVsync);
        gameWindow.setOpacity(cacheOpacity);
    }

    @Override
    public void start() {
        sync();
    }

    @Override
    public void editorGUI()
    {
        super.deleteButton();

        // - - - title control
        if (Widgets.button(Icons.Check + " " + " ##" + "window title")) setTitle(cacheTitle);
        ImGui.sameLine();
        cacheTitle = Widgets.inputText(Icons.Pen + "  Title", cacheTitle);

        // - - - Size control
        if (Widgets.button(Icons.Check + " " + " ##" + "window size")) setWindowSize(cacheSize);
        ImGui.sameLine();
        Widgets.drawVec2Control(Icons.Expand + " Window Size", cacheSize, 100, 100);

        // - - - Position control:
        if (Widgets.button(Icons.Check + " " + " ##" + "window pos")) setPosition(cachePosition);
        ImGui.sameLine();
        Widgets.drawVec2Control(Icons.MapPin + "  Window Position", cachePosition, 100, 100);

        // - - - Background color control
        if (Widgets.button(Icons.Check + " " + " ##" + "window color")) setBackgroundColor(cacheBgColor);
        ImGui.sameLine();
        Widgets.colorPicker4(Icons.EyeDropper + "  Background Color", cacheBgColor);

        // - - - opacity control
        if (Widgets.button(Icons.Check + " " + " ##" + "window opacity")) setOpacity(cacheOpacity);
        ImGui.sameLine();
        cacheOpacity = Math.min(Math.max(0.1f, Widgets.drawFloatControl(Icons.Eye + "  Opacity", cacheOpacity)), 1f);

        // - - - Decorated (border) toggle
        if (Widgets.button(Icons.Check + " " + " ##" + "window decorated")) setDecorated(cacheDecorated);
        ImGui.sameLine();
        cacheDecorated = Widgets.drawBoolControl(Icons.WindowMinimize + "  Decorated", cacheDecorated);
    }
}
