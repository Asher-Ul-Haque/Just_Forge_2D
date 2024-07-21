package Just_Forge_2D.Core.Scene;
import Just_Forge_2D.Core.Input.*;
import Just_Forge_2D.Core.justForgeWindow;

import java.awt.event.KeyEvent;

public class justForgeLevelEditorScene extends justForgeScene
{
    private boolean changingScene = false;
    private int timeToChangeScene = 2;


    public justForgeLevelEditorScene()
    {
        System.out.println("Inside Level editor system");
    }

    @Override
    public void update(double DELTA_TIME)
    {
        if (!changingScene && justForgeKeyboard.isKeyPressed(KeyEvent.VK_SPACE))
        {
            changingScene = true;
        }
        if (changingScene && timeToChangeScene <= 0)
        {
            justForgeWindow.changeScene(1);
            timeToChangeScene -= DELTA_TIME;
            justForgeWindow.get().r -= DELTA_TIME * 5.0f;
            justForgeWindow.get().g -= DELTA_TIME * 5.0f;
            justForgeWindow.get().b -= DELTA_TIME * 5.0f;
        }
    }
}
