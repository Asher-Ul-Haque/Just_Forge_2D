package Just_Forge_2D;

import Just_Forge_2D.EditorSystem.ObjectSelector;
import Just_Forge_2D.EditorSystem.justForgeImGui;
import Just_Forge_2D.RenderingSystems.Renderer;
import Just_Forge_2D.RenderingSystems.Shader;
import Just_Forge_2D.SceneSystem.EditorSceneInitializer;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneInitializer;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.EditorWindow;
import Just_Forge_2D.WindowSystem.Window;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.util.HashMap;

import static org.lwjgl.openal.ALC10.*;

public class Forge
{
    private static final HashMap<Window, Renderer> windowRendererHashMap = new HashMap<>();
    public static Scene currentScene;
    public static EditorWindow window = null;
    public static justForgeImGui editorLayer;
    public static ObjectSelector selector;

    public static Shader defaultShader;
    public static Shader selectorShader;

    private static long audioContext;
    private static long audioDevicePtr;

    public static void changeScene(SceneInitializer INITIALIZER)
    {
        if (currentScene != null)
        {
            Logger.FORGE_LOG_INFO("Clearing Scene Catch from previous run");
            currentScene.destroy();
        }

     //   getEditor().getPropertiesWindow().setActiveGameObject(null);

        currentScene = new Scene(INITIALIZER);
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public static void run()
    {
        Window main = new Window(null);
        AssetPool.addShader("Default", "Assets/Shaders/default.glsl");
        AssetPool.addShader("Selector", "Assets/Shaders/selector.glsl");
        AssetPool.addShader("Debug", "Assets/Shaders/debug.glsl");
        changeScene(new EditorSceneInitializer());
        while (!main.shouldClose())
        {
            main.loop();
        }
        main.close();
    }

    public static void assignRenderer(Window WINDOW, Renderer RENDERER)
    {
        Logger.FORGE_LOG_INFO("Linking " + WINDOW.getTitle() + " to Renderer " + RENDERER);
        windowRendererHashMap.put(WINDOW, RENDERER);
    }

    public static Renderer getRenderer(Window WINDOW)
    {
        Renderer renderer = windowRendererHashMap.get(WINDOW);
        if (renderer == null)
        {
            Logger.FORGE_LOG_WARNING("No renderer has been assigned to : " + WINDOW);
            Renderer newRenderer = new Renderer(WINDOW.getTitle());
            assignRenderer(WINDOW, newRenderer);
            return newRenderer;
        }
        return renderer;
    }

    public static void setAudioSystem()
    {

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        assert defaultDeviceName != null;
        audioDevicePtr = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevicePtr, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevicePtr);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10)
        {
            Logger.FORGE_LOG_ERROR("Audio Not supported");
            assert false;
        }
    }

    public static void finishAudio()
    {
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevicePtr);
    }

    public static void update(float DELTA_TIME){}
}
