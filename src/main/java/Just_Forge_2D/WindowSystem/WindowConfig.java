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


    // - - - Functions - - -

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Vector4f getClearColor() {
        return clearColor;
    }

    public void setClearColor(Vector4f clearColor) {
        this.clearColor = clearColor;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDecorated() {
        return decorated;
    }

    public void setDecorated(boolean decorated) {
        this.decorated = decorated;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isAlwaysOnTop() {
        return alwaysOnTop;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
    }

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