package Just_Forge_2D.Core.Scene;
import Just_Forge_2D.Core.Input.*;
import Just_Forge_2D.Core.justForgeWindow;

import java.awt.event.KeyEvent;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class justForgeLevelEditorScene extends justForgeScene
{
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;


    public justForgeLevelEditorScene()
    {
        System.out.println("Inside Level Editor Scene");
    }

    @Override
    public void update(double DELTA_TIME)
    {
        if (!changingScene && justForgeKeyboard.isKeyPressed(KeyEvent.VK_SPACE))
        {
            changingScene = true;
        }

        if (changingScene)
        {
            if (timeToChangeScene > 0.0f)
            {
                timeToChangeScene -= DELTA_TIME;
                justForgeWindow.get().r -= DELTA_TIME * 2.0f;
                justForgeWindow.get().g -= DELTA_TIME * 2.0f;
                justForgeWindow.get().b -= DELTA_TIME * 2.0f;
            }
            else
            {
                justForgeWindow.changeScene(1);
            }
        }
    }
}
