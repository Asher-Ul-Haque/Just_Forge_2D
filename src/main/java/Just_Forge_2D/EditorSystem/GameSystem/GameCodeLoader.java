package Just_Forge_2D.EditorSystem.GameSystem;

import static Just_Forge_2D.EditorSystem.GameSystem.GameManager.game;
import static Just_Forge_2D.EditorSystem.GameSystem.GameManager.success;

public class GameCodeLoader
{
    public static void init() {
        if (success) game.init();
    }

    public static void loop(float DELTA_TIME) {
        if (success) game.update(DELTA_TIME);
    }

    public static void terminate() {
        if (success) game.end();
    }
}