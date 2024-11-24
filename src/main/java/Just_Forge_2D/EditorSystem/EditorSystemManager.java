package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.AssetPool.AssetPoolSerializer;
import Just_Forge_2D.AudioSystem.AudioSystemManager;
import Just_Forge_2D.EditorSystem.Windows.ObjectSelector;
import Just_Forge_2D.EntityComponentSystem.Components.ComponentList;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.GameSystem.ProjectManager;
import Just_Forge_2D.RenderingSystem.Shader;
import Just_Forge_2D.SceneSystem.MainSceneScript;
import Just_Forge_2D.SceneSystem.SceneScript;
import Just_Forge_2D.Themes.CleanTheme;
import Just_Forge_2D.Themes.Theme;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import Just_Forge_2D.WindowSystem.GameWindow;
import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;


public class EditorSystemManager
{
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
            GameWindow.changeScene(new MainSceneScript());
            return;
        }
        try
        {
            GameWindow.changeScene(GameWindow.getCurrentScene().getScript().getClass().getDeclaredConstructor().newInstance());
        }
        catch (Exception e)
        {
            EditorSystemManager.currentSceneInitializer = INITIALIZER;
            Logger.FORGE_LOG_FATAL("Couldn't change scene");
            GameWindow.changeScene(new MainSceneScript());
        }
    }

    public static Class<? extends SceneScript> currentSceneInitializer;
    public static String projectDir = System.getProperty("user.dir");
    public static final boolean isRelease = false;

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

    public static void setSelector()
    {
        ObjectSelector.init(GameWindow.getFrameBuffer().getSize().x, GameWindow.getFrameBuffer().getSize().y);
    }

    public static void compileShaders()
    {
        AssetPool.addShader("Default", "Assets/Shaders/default.glsl", true);
        AssetPool.addShader("Debug", "Assets/Shaders/debug.glsl", true);
        EditorSystemManager.defaultShader = AssetPool.getShader("Default");

        if (!EditorSystemManager.isRelease)
        {
            EditorSystemManager.selectorShader = new Shader("Assets/Shaders/selector.glsl");
            selectorShader.compile();
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

    public static void setTheme(Theme THEME)
    {
        currentTheme = THEME;
        currentTheme.applyTheme();
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
        if (currentTheme == null) currentTheme = new CleanTheme(Settings.DARK_MODE_ENABLED());
        GameWindow.get();
        EditorSystemManager.setSelector();
        setEditorLayer();
        ComponentList.initialize();
        compileShaders();
    }

    public static void end()
    {
        EventManager.notify(null, new Event(EventTypes.ForgeStop));
        if (!isRelease)
        {
            AssetPoolSerializer.saveAssetPool(projectDir + "/.forge/Pool.justForgeFile");
            AssetPool.clearSpriteSheetPool();
            AssetPool.clearShaderPool();
            AssetPool.clearSoundPool();
            AssetPool.clearTexturePool();
            ProjectManager.saveLastProjectPath();
        }
        AudioSystemManager.terminate();
        ImGUIManager.destroyImGui();
        Settings.save();
        Logger.flushToFile();
    }
}
