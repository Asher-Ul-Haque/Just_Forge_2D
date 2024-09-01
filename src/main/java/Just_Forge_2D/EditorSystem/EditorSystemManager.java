package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.AudioSystem.AudioSystemManager;
import Just_Forge_2D.EditorSystem.Themes.CleanTheme;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Windows.ObjectSelector;
import Just_Forge_2D.EntityComponentSystem.Components.ComponentList;
import Just_Forge_2D.RenderingSystem.Framebuffer;
import Just_Forge_2D.RenderingSystem.Shader;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.SceneSystem.EmptySceneInitializer;
import Just_Forge_2D.SceneSystem.SceneInitializer;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
import SampleMario.GameCode.EditorSceneInitializer;

import java.io.IOException;


public class EditorSystemManager
{
    private static Framebuffer framebuffer;
    private static Theme currentTheme;
    protected static Shader defaultShader;
    protected static Shader selectorShader;
    protected static ImGUIManager editorLayer;
    protected static boolean isRuntimePlaying = false;
    public static WindowConfig editorWindowConfig;

    public static Class<? extends SceneInitializer> getCurrentSceneInitializer()
    {
        return currentSceneInitializer;
    }

    public static void setCurrentSceneInitializer(Class<? extends SceneInitializer> INITIALIZER)
    {
        if (INITIALIZER == null)
        {
            Logger.FORGE_LOG_ERROR("Cant assign null as initializer");
            return;
        }
        EditorSystemManager.currentSceneInitializer = INITIALIZER;
        try
        {
            MainWindow.changeScene(currentSceneInitializer.getDeclaredConstructor().newInstance());
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_FATAL("Couldnt change scene");
            MainWindow.changeScene(new EmptySceneInitializer());
        }
    }

    public static Class<? extends SceneInitializer> currentSceneInitializer = EmptySceneInitializer.class;
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
        framebuffer = new Framebuffer(1980, 720);
    }

    public static void setSelector()
    {
        ObjectSelector.init(1980, 720);
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
        ImGUIManager.initImGui(MainWindow.get().getGlfwWindowPtr());
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
        MainWindow.get().run();
        end();
    }

    public static void start()
    {
        AudioSystemManager.initialize();
        WindowSystemManager.initialize();
        editorWindowConfig = new WindowConfig();
        editorWindowConfig.setHeight(800);
        if (currentTheme == null) currentTheme = new CleanTheme();
        MainWindow.get();
        AssetPool.addSpriteSheet("Gizmos", new SpriteSheet(AssetPool.getTexture("Assets/Textures/gizmos.png"), 24, 48, 3, 0));
        EditorSystemManager.setSelector();
        setEditorLayer();
        ComponentList.initialize();
    }

    public static void end()
    {
        AudioSystemManager.terminate();
        ImGUIManager.destroyImGui();
    }
}