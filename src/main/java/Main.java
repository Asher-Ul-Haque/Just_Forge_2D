import Just_Forge_2D.Core.justForgeWindow;
import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;

public class Main extends Application {
    @Override
    protected void configure(Configuration config) {
        config.setTitle("Dear ImGui is Awesome!");
    }

    @Override
    public void process() {
        ImGui.text("Hello, World!");
    }

    public static void main(String[] args) {
        //launch(new Main());
        justForgeWindow window = justForgeWindow.get();
        window.run();
    }
}