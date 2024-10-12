package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.AudioSystem.AudioSystemManager;
import Just_Forge_2D.EditorSystem.Themes.CleanTheme;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Windows.ObjectSelector;
import Just_Forge_2D.EntityComponentSystem.Components.ComponentList;
import Just_Forge_2D.RenderingSystem.Framebuffer;
import Just_Forge_2D.RenderingSystem.Shader;
import Just_Forge_2D.SceneSystem.EmptySceneScript;
import Just_Forge_2D.SceneSystem.SceneScript;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;


public class EditorSystemManager
{
    private static Framebuffer framebuffer;
    private static Theme currentTheme;
    public static Shader defaultShader;
    public static Shader selectorShader;
    protected static ImGUIManager editorLayer;
    public static boolean isRuntimePlaying = false;
    public static WindowConfig editorWindowConfig;

    public static Class<? extends SceneScript> getCurrentSceneInitializer()
    {
        return currentSceneInitializer;
    }

    public static void setCurrentSceneInitializer(Class<? extends SceneScript> INITIALIZER)
    {
        if (INITIALIZER == null)
        {
            Logger.FORGE_LOG_ERROR("Cant assign null as initializer");
            return;
        }
        EditorSystemManager.currentSceneInitializer = INITIALIZER;
        try
        {
            GameWindow.changeScene(currentSceneInitializer.getDeclaredConstructor().newInstance());
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_FATAL("Couldn't change scene");
            GameWindow.changeScene(new EmptySceneScript());
        }
    }

    public static Class<? extends SceneScript> currentSceneInitializer;
    public static String projectDir = System.getProperty("user.dir");
    public static boolean isRelease = false;

    public static state getCurrentState()
    {
        return currentState;
    }

    public static void setCurrentState(state currentState) {
        EditorSystemManager.currentState = currentState;
    }

    protected static state currentState = state.isSplashScreen;

    public static enum state
    {
        isEditor,
        isSplashScreen,
        isSelector
    }


    public static Framebuffer getFramebuffer()
    {
        return framebuffer;
    }

    public static void setFramebuffer()
    {
        framebuffer = new Framebuffer(WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y);
    }

    public static void setSelector()
    {
        ObjectSelector.init(WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y);
    }

    public static void compileShaders()
    {
        AssetPool.addShader("Default", "Assets/Shaders/default.glsl");
        EditorSystemManager.defaultShader = AssetPool.getShader("Default");

        if (!EditorSystemManager.isRelease)
        {
            AssetPool.addShader("Selector", "Assets/Shaders/selector.glsl");
            AssetPool.addShader("Debug", "Assets/Shaders/debug.glsl");
            EditorSystemManager.selectorShader = AssetPool.getShader("Selector");
        }
    }

    public static ImGUIManager getEditor()
    {
        return editorLayer;
    }

    public static void setEditorLayer()
    {
        ImGUIManager.initImGui(GameWindow.get().getGlfwWindowPtr());
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
        start();
        GameWindow.get().run();
        end();
    }

    public static void start()
    {
        AudioSystemManager.initialize();
        WindowSystemManager.initialize();
        editorWindowConfig = new WindowConfig();
        editorWindowConfig.setHeight(800);
        if (currentTheme == null) currentTheme = new CleanTheme(DefaultValues.DARK_MODE_ENABLED);
        GameWindow.get();
        EditorSystemManager.setSelector();
        setEditorLayer();
        ComponentList.initialize();
    }

    public static void end()
    {
        AudioSystemManager.terminate();
        ImGUIManager.destroyImGui();
        Logger.finish();
    }
}