package Just_Forge_2D.WindowSystem;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector4f;

public class WindowConfig
{
    // - - - basic size and title
    protected int width = Configurations.DEFAULT_WINDOW_WIDTH;
    protected int height = Configurations.DEFAULT_WINDOW_HEIGHT;
    protected String title = Configurations.DEFAULT_WINDOW_TITLE;

    // - - - medium config
    protected Vector4f clearColor = Configurations.DEFAULT_CLEAR_COLOR;

    // - - - advanced configuration
    protected boolean vsync = Configurations.DEFAULT_VSYNC_ENABLE;
    protected boolean transparent = Configurations.DEFAULT_WINDOW_TRANSPARENCY_STATE;
    protected boolean maximized = Configurations.DEFAULT_WINDOW_MAXIMIZED_STATE;
    protected boolean visible = Configurations.DEFAULT_WINDOW_VISIBLE_STATE;
    protected boolean decorated = Configurations.DEFAULT_WINDOW_DECORATION_STATE;
    protected boolean resizable = Configurations.DEFAULT_WINDOW_RESIZABLE_STATE;


    // - - - Constructors - - -

    // - - - one with everything
    public WindowConfig(int WIDTH, int HEIGHT, String TITLE, boolean VSYNC, boolean TRANSPARENT, boolean MAXIMIZED, boolean VISIBLE, boolean DECORATED, boolean RESIZABLE)
    {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.title = TITLE;
        this.vsync = VSYNC;
        this.transparent = TRANSPARENT;
        this.decorated = DECORATED;
        this.resizable = RESIZABLE;
        this.maximized = MAXIMIZED;
        this.visible = VISIBLE;
        logCreation();
    }

    // - - - one with essentials
    public WindowConfig(int WIDTH, int HEIGHT, String TITLE)
    {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.title = TITLE;
        logCreation();
    }

    // - - - one with all defaults
    public WindowConfig(){logCreation();}


    // - - - private helper function
    private void logCreation()
    {
        Logger.FORGE_LOG_INFO("Creating an Initializer for a Window: " + title + " of size: " + width + " : " + height);
    }
}