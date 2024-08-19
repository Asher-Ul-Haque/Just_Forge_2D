package Just_Forge_2D.WindowSystem;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;
import org.joml.Random;
import org.joml.Vector4f;

public class WindowConfig
{
    // - - - basic size and title
    protected int width = Configurations.DEFAULT_WINDOW_WIDTH;
    protected int height = Configurations.DEFAULT_WINDOW_HEIGHT;
    protected String title = Configurations.DEFAULT_WINDOW_TITLE;

    // - - - medium config
    protected Vector4f clearColor = Configurations.DEFAULT_CLEAR_COLOR;
    protected String iconPath = Configurations.DEFAULT_ICON_PATH;
    protected int x = 0;
    protected int y = 0;

    // - - - advanced configuration
    protected float aspectRatio = Configurations.DEFAULT_ASPECT_RATIO;
    protected boolean vsync = Configurations.DEFAULT_VSYNC_ENABLE;
    protected boolean transparent = Configurations.DEFAULT_WINDOW_TRANSPARENCY_STATE;
    protected boolean maximized = Configurations.DEFAULT_WINDOW_MAXIMIZED_STATE;
    protected boolean visible = Configurations.DEFAULT_WINDOW_VISIBLE_STATE;
    protected boolean decorated = Configurations.DEFAULT_WINDOW_DECORATION_STATE;
    protected boolean resizable = Configurations.DEFAULT_WINDOW_RESIZABLE_STATE;
    protected boolean alwaysOnTop = Configurations.DEFAULT_WINDOW_FLOAT_STATUS;


    // - - - Constructors - - -

    // - - - one with everything
    public WindowConfig(int WIDTH,
                        int HEIGHT,
                        String TITLE,
                        boolean VSYNC,
                        int X_POS,
                        int Y_POS,
                        float ASPECT_RATIO,
                        boolean TRANSPARENT,
                        boolean MAXIMIZED,
                        boolean VISIBLE,
                        boolean DECORATED,
                        boolean RESIZABLE,
                        boolean TOP,
                        String ICON_PATH)
    {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.title = TITLE;
        this.vsync = VSYNC;
        this.aspectRatio = ASPECT_RATIO;
        this.transparent = TRANSPARENT;
        this.decorated = DECORATED;
        this.resizable = RESIZABLE;
        this.maximized = MAXIMIZED;
        this.visible = VISIBLE;
        this.alwaysOnTop = TOP;
        this.x = X_POS;
        this.y = Y_POS;
        this.iconPath = ICON_PATH;
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