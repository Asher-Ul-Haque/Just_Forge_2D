import Just_Forge_2D.EditorSystem.Game;
import Just_Forge_2D.Utils.Logger;

public class Main implements Game
{
    @Override
    public void init()
    {
        Logger.FORGE_LOG_DEBUG("Yay");
    }

    @Override
    public void update(float deltaTime)
    {
        Logger.FORGE_LOG_TRACE("Game updated!");
    }

    @Override
    public void end()
    {
        Logger.FORGE_LOG_DEBUG("Game ended!");
    }
}