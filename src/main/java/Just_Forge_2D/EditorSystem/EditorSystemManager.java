package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.EditorSystem.Themes.IntelliTheme;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Windows.ObjectSelector;
import Just_Forge_2D.RenderingSystem.Framebuffer;
import Just_Forge_2D.RenderingSystem.Shader;
import Just_Forge_2D.Utils.AssetPool;

public class EditorSystemManager
{
    private static Framebuffer framebuffer;
    private static ObjectSelector selector;
    private static Theme currentTheme;
    private static EditorWindow window;


    // - - - shaders
    protected static Shader defaultShader;
    protected static Shader selectorShader;

    protected static ImGUIManager editorLayer;

    protected static boolean isRuntimePlaying = false;


    public static Framebuffer getFramebuffer()
    {
        return framebuffer;
    }

    public static void setFramebuffer()
    {
        framebuffer = new Framebuffer(1980, 720);
    }

    public static ObjectSelector getSelector()
    {
        return selector;
    }

    public static void setSelector()
    {
        selector = new ObjectSelector(1980, 720);
    }

    public static void compileShaders()
    {
        AssetPool.addShader("Default", "Assets/Shaders/default.glsl");
        AssetPool.addShader("Selector", "Assets/Shaders/selector.glsl");
        AssetPool.addShader("Debug", "Assets/Shaders/debug.glsl");
        EditorSystemManager.defaultShader = AssetPool.getShader("Default");
        EditorSystemManager.selectorShader = AssetPool.getShader("Selector");
    }

    public static ImGUIManager getEditor()
    {
        return editorLayer;
    }

    public static void setEditorLayer()
    {
        ImGUIManager.initImGui(EditorWindow.get().getGlfwWindowPtr(), EditorSystemManager.getSelector());
    }

    public static Theme getCurrentTheme()
    {
        return currentTheme;
    }

    public void setTheme(Theme THEME)
    {
        currentTheme = THEME;
    }

    public static void run()
    {
        if (currentTheme == null) currentTheme = new IntelliTheme();
        window = EditorWindow.get();
        EditorSystemManager.setSelector();
        setEditorLayer();
        window.run();
        ImGUIManager.destroyImGui();
    }
}