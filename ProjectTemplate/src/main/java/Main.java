import Just_Forge_2D.Game.GameApplication;

public class MainGame implements Game
{
    @Override
    public void init()
    {
        // Initialize game resources
        System.out.println("Game initialized!");
    }

    @Override
    public void update(float deltaTime)
    {
        // Update game state
        System.out.println("Game updated!");
    }

    @Override
    public void end()
    {
        // Cleanup game resources
        System.out.println("Game ended!");
    }
}
