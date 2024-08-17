package Just_Forge_2D.WindowSystem;

// - - - Imports | - - -
import Just_Forge_2D.EditorSystem.ObjectSelector;
import Just_Forge_2D.EditorSystem.justForgeImGui;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.Forge;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.PhysicsSystem.PhysicsManager;
import Just_Forge_2D.RenderingSystems.DebugPencil;
import Just_Forge_2D.RenderingSystems.Framebuffer;
import Just_Forge_2D.RenderingSystems.Renderer;
import Just_Forge_2D.SceneSystem.EditorSceneInitializer;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.TimeKeeper;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


// - - - | Class | - - -


public class EditorWindow extends Window {
    private Framebuffer framebuffer;
    private boolean isRuntimePlaying = false;

    // - - - Systems

    private long audioContext;
    private long audioDevicePtr;


    // - - - | Functions | - - -


    // - - - Private Constructor for Singleton
    public EditorWindow() {
        super(null);
        Forge.setAudioSystem();
        Forge.window = this;
    }

    // - - - Run the game
    public void run() {
        Logger.FORGE_LOG_WARNING("Running Game Engine in automatic mode");
        init();
        Forge.changeScene(new EditorSceneInitializer());
        while (!shouldClose()) {
            loop();
        }
        close();
    }

    // - - - Initialize the window
    public void init() {
        this.framebuffer = new Framebuffer(1980, 720);
        Forge.selector = new ObjectSelector(1980, 720);
        glViewport(0, 0, 1980, 720);
        Logger.FORGE_LOG_INFO("Framebuffer created and assigned for offscreen rendering");

        Forge.editorLayer = new justForgeImGui(this.glfwWindowPtr, Forge.selector);
        Forge.editorLayer.initImGui();
        Logger.FORGE_LOG_INFO("Editor linked with window");


        // - - - compile shaders
        AssetPool.addShader("Default", "Assets/Shaders/default.glsl");
        AssetPool.addShader("Selector", "Assets/Shaders/selector.glsl");
        AssetPool.addShader("Debug", "Assets/Shaders/debug.glsl");
        Forge.defaultShader = AssetPool.getShader("Default");
        Forge.selectorShader = AssetPool.getShader("Selector");

        beginTime = (float) TimeKeeper.getTime();
        Logger.FORGE_LOG_INFO("Time keeping system Online");
    }


    // - - - Loop the game
    @Override
    public void loop() {
        // - - - Poll events
        glfwPollEvents();


        // - - - Renderpasses - - -

        // - - - 1: renderer to object picker
        glDisable(GL_BLEND);
        Forge.selector.enableWriting();

        glViewport(0, 0, 1980, 720);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Forge.getRenderer(this).bindShader(Forge.selectorShader);
        Forge.currentScene.render(dt);
        Forge.selector.disableWriting();

        // - - - 2: render to monitor

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        // - - - Debug Drawing
        DebugPencil.beginFrame();

        // - - - Framebuffer
        this.framebuffer.bind();

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        if (dt >= 0.0d) {
            Forge.getRenderer(this).bindShader(Forge.defaultShader);
            if (isRuntimePlaying) {
                Forge.currentScene.update(dt);
            } else {
                Forge.currentScene.editorUpdate(dt);
            }
            Forge.currentScene.render(dt);
            DebugPencil.draw();
        }

        // - - - Finish drawing to texture so that imgui should be rendered to the window
        this.framebuffer.unbind();

        // - - - Update the editor
        Forge.editorLayer.update((float) dt, Forge.currentScene);


        // - - - finish input frames
        Mouse.endFrame();
        Keyboard.endFrame();

        // - - - Swap buffer for next frame
        glfwSwapBuffers(glfwWindowPtr);

        // - - - finish input
        Mouse.endFrame();

        // - - - Keep time
        endTime = (float) TimeKeeper.getTime();
        dt = endTime - beginTime;
        beginTime = endTime;
    }


    // - - - | Getters Setters and Decoration | - - -

    // - - - scene

    // - - - framebuffer
    public Framebuffer getFramebuffer() {
        return this.framebuffer;
    }


    @Override
    public void onNotify(GameObject OBJECT, Event EVENT) {
        switch (EVENT.type) {
            case ForgeStart:
                Logger.FORGE_LOG_INFO("Starting Game");
                this.isRuntimePlaying = true;
                Forge.currentScene.save();
                Forge.changeScene(new EditorSceneInitializer());
                break;

            case ForgeStop:
                Logger.FORGE_LOG_INFO("Ending Game");
                this.isRuntimePlaying = false;
                Forge.changeScene(new EditorSceneInitializer());
                break;

            case SaveLevel:
                Logger.FORGE_LOG_INFO("Saving Scene: " + Forge.currentScene);
                Forge.currentScene.save();
                break;

            case LoadLevel:
                Forge.changeScene(new EditorSceneInitializer());
                break;
        }
    }
}