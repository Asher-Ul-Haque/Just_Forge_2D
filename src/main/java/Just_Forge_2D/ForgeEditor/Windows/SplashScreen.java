package Just_Forge_2D.ForgeEditor.Windows;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.ForgeEditor.Configurations.ColorScheme;
import Just_Forge_2D.ForgeEditor.EditorManager;
import Just_Forge_2D.RenderingSystems.Texture;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;

public class SplashScreen extends Window
{
    private static SplashScreen self;
    private static Scene splashScreenScene;

    protected static WindowConfig splashScreenConfig = new WindowConfig(
            500, 500, "Just-Forge-2D",
            true,
            500, 500, 1f,
            true, false, true, false, false, true, DefaultValues.DEFAULT_ICON_PATH);

    private SplashScreen()
    {
        super(splashScreenConfig);
        EditorManager.splashScreen = this;
        self = this;
        self.setClearColor(ColorScheme.primaryColor);
    }

    public static void run()
    {
        new SplashScreen();
        splashScreenScene = SceneSystemManager.addScene(splashScreenScene, new SplashScreenSceneConfig(), "Splash");
        GameObject logo = new GameObject("Splash Screen");
        logo.addComponent(new TransformComponent());

        logo.transform.scale.set(1, 1);
        logo.transform.position.set((self.getWidth() - logo.transform.scale.x) / 2, (self.getHeight() - logo.transform.scale.y) / 2);

        Sprite logoSpr = new Sprite();
        logoSpr.setTexture(new Texture(500, 500, DefaultValues.DEFAULT_TEXTURE_PATH));
        SpriteComponent logoSprite = new SpriteComponent();
        logoSprite.setSprite(logoSpr);

        splashScreenScene.start();

        for (int i = 0; i < 90; ++i)
        {
            self.loop();
        }
        self.close();
    }
}
