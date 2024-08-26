package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.RenderingSystem.Framebuffer;
import Just_Forge_2D.RenderingSystem.Shader;
import Just_Forge_2D.Utils.AssetPool;

public class EditorSystemManager
{
    private static Framebuffer framebuffer;
    private static ObjectSelector selector;


    // - - - shaders
    protected static Shader defaultShader;
    protected static Shader selectorShader;

    protected static justForgeImGui editorLayer;

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

    public static justForgeImGui getEditor()
    {
        return editorLayer;
    }

    public static void setEditorLayer()
    {
        editorLayer = new justForgeImGui(EditorWindow.get().getGlfwWindowPtr(), EditorSystemManager.getSelector());
        editorLayer.initImGui();
    }
}