package Just_Forge_2D.ForgeEditor.Windows;

import Just_Forge_2D.Forge;
import Just_Forge_2D.ForgeEditor.EditorManager;
import Just_Forge_2D.ForgeEditor.ImGuiManager;
import Just_Forge_2D.RenderingSystems.Framebuffer;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.opengl.GL30C.GL_FRAMEBUFFER;

public class EditorWindow extends Window
{
    private Framebuffer frame;

    public EditorWindow(WindowConfig CONFIG)
    {
        super(CONFIG);
        Logger.FORGE_LOG_TRACE("Assigning Framebuffer for : " + this.toString());
        this.frame = new Framebuffer(this.getWidth(), this.getHeight());
    }

    @Override
    public void loop()
    {
        warnFPSspike();
        manageInput();
        Forge.update(dt);
        render();
        ImGuiManager.update(dt);
        finishInputFrames();
        keepTime();
    }

    @Override protected void render()
    {
        if (!DefaultValues.IS_RELEASE)
        {
            this.frame.bind();
        }
        glViewport(0, 0, this.frame.getWidth(), this.frame.getHeight());

        Vector4f clearColor = this.config.getClearColor();
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        WindowSystemManager.getRenderer(this).render();

        this.frame.unbind();
        glViewport(0, 0, getWidth(), getHeight());
    }


    // - - - GET framebuffer

    public Framebuffer getFramebuffer()
    {
        return this.frame;
    }
}
