import Just_Forge_2D.GameSystem.Game;
import Just_Forge_2D.Utils.Logger;

public class Main implements Game
{
    // - - - This is a sample game. 
    /* 
        All interactions with Just Forge 2D can happen either via 
        * The Engine Editor
        * This Main Class That Implements Game (note that changing the class name or not implementing Game results in the Engine not being able to link your game)
        * Your self-made components
        * Scene Scripts        
     */
    
    
    // - - - This runs once when the game starts. You could use it to change the window color or the size etc
    @Override
    public void init()
    {
        Logger.FORGE_LOG_DEBUG("Game started!");
    }

    // - - - This runs once every frame of the game. You could use it with conjunction to other libraries that you are using in your game
    @Override
    public void update(float DELTA_TIME)
    {
        //Logger.FORGE_LOG_TRACE("Game updated!");
    }

    // - - - This runs once the game ends.
    @Override
    public void end()
    {
        Logger.FORGE_LOG_DEBUG("Game ended!");
    }
}
