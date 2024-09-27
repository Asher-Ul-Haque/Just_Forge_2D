package Just_Forge_2D.WindowSystem;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector4f;

public class WindowConfig
{
    // - - - basic size and title
    protected int width = DefaultValues.DEFAULT_WINDOW_WIDTH;
    protected int height = DefaultValues.DEFAULT_WINDOW_HEIGHT;
    protected String title = DefaultValues.DEFAULT_WINDOW_TITLE;

    // - - - medium config
    protected Vector4f clearColor = DefaultValues.DEFAULT_CLEAR_COLOR;
    protected String iconPath = DefaultValues.DEFAULT_ICON_PATH;
    protected int x = 0;
    protected int y = 0;

    // - - - advanced configuration
    protected float aspectRatio = DefaultValues.DEFAULT_ASPECT_RATIO;
    protected boolean vsync = DefaultValues.DEFAULT_VSYNC_ENABLE;
    protected boolean transparent = DefaultValues.DEFAULT_WINDOW_TRANSPARENCY_STATE;
    protected boolean maximized = DefaultValues.DEFAULT_WINDOW_MAXIMIZED_STATE;
    protected boolean visible = DefaultValues.DEFAULT_WINDOW_VISIBLE_STATE;
    protected boolean decorated = DefaultValues.DEFAULT_WINDOW_DECORATION_STATE;
    protected boolean resizable = DefaultValues.DEFAULT_WINDOW_RESIZABLE_STATE;
    protected boolean alwaysOnTop = DefaultValues.DEFAULT_WINDOW_FLOAT_STATUS;


    // - - - | Functions | - - -


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

    
    // - - - Width and Height Getter Setters - - -

    public int getWidth() {
        return width;
    }

    public void setWidth(int WIDTH) {
        this.width = WIDTH;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int HEIGHT) {
        this.height = HEIGHT;
    }


    // - - - Title getter setters - - -

    public String getTitle() {
        return title;
    }

    public void setTitle(String TITLE) {
        this.title = TITLE;
    }


    // - - - clear color getter setters - - -

    public Vector4f getClearColor() {
        return clearColor;
    }

    public void setClearColor(Vector4f COLOR) {
        this.clearColor = COLOR;
    }


    // - - - icon getter setters - - -

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String ICON_FILE_PATH) {
        this.iconPath = ICON_FILE_PATH;
    }


    // - - - Position getter setters - - -

    public int getX() {
        return x;
    }

    public void setX(int X_POS) {
        this.x = X_POS;
    }

    public int getY() {
        return y;
    }

    public void setY(int Y_POS) {
        this.y = Y_POS;
    }


    // - - - aspect ratio getter setter - - -

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }


    // - - - Vsync getter and setter - - -

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
    }


    // - - - Transparency getter and setter - - -

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }


    // - - - maximization getter and setter - - -

    public boolean isMaximized() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }


    // - - - visibility getter and setter - - -

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }


    // - - - decoration getter and setter - - -

    public boolean isDecorated() {
        return decorated;
    }

    public void setDecorated(boolean decorated) {
        this.decorated = decorated;
    }


    // - - - resize getter and setter - - -

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }


    // - - - always on top getter and setter - - -

    public boolean isAlwaysOnTop() {
        return alwaysOnTop;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
    }

    // - - - private helper function
    private void logCreation()
    {
        Logger.FORGE_LOG_INFO("Creating an Initializer for a Window: " + title + " of size: " + width + " : " + height);
    }
}