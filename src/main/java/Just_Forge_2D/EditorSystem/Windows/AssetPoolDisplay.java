package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.AudioSystem.Sound;
import Just_Forge_2D.EditorSystem.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.InputControls.MouseControlComponent;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.util.List;

public class AssetPoolDisplay
{
    private static String name = "";
    private static String path = "";
    private static boolean open = false;
    private static Vector2f size = new Vector2f(GridlinesComponent.gridSize);
    private static int spriteCount;
    private static int spriteSpacing;
    private static boolean loop;


    private static void spriteSheetDisplay()
    {
        if (ImGui.beginTabItem("Sprite Sheets"))
        {
            if (ImGui.button("Add a SpriteSheet")) open = !open;
            ImGui.sameLine();
            if (ImGui.button("Clear")) AssetPool.clearSpriteSheetPool();
            if (open)
            {
                name = Widgets.inputText("Name", name);
                path = Widgets.inputText("Path", path);
                Widgets.drawVec2Control("Sprite Size", size);
                spriteCount = Widgets.drawIntControl("Sprite Count", spriteCount);
                spriteSpacing = Widgets.drawIntControl("Sprite Spacing", spriteSpacing);
                ImGui.columns(2);
                if (ImGui.button("Browse"))
                {
                    path = TinyFileDialogs.tinyfd_openFileDialog("Path", EditorSystemManager.projectDir + "/Assets/Textures/", null, null, false);
                }
                ImGui.nextColumn();
                if (!name.isEmpty() && !path.isEmpty())
                {
                    if (ImGui.button("Add"))
                    {
                        Texture t = new Texture();
                        if (t.init(path))
                        {
                            SpriteSheet sheet = new SpriteSheet(t, (int) size.x, (int) size.y, spriteCount, spriteSpacing);
                            AssetPool.addSpriteSheet(name, sheet, true);
                        }
                        else
                        {
                            Logger.FORGE_LOG_ERROR("Bad Texture: " + name);
                        }
                    }
                }

                ImGui.columns(1);
            }
            List<SpriteSheet> spriteSheets = AssetPool.getAllSpriteSheets();
            List<String> spriteSheetNames = AssetPool.getSpriteSheetNames();
            for (int j = 0; j < spriteSheets.size(); ++j)
            {
                if (ImGui.collapsingHeader(spriteSheetNames.get(j)))
                {
                    ImGui.sameLine();
                    if (ImGui.button("Remove"))
                    {
                        AssetPool.removeSpriteSheet(spriteSheetNames.get(j));
                        continue;
                    }
                    ImVec2 windowPos = new ImVec2();
                    ImGui.getWindowPos(windowPos);
                    ImVec2 windowSize = new ImVec2();
                    ImGui.getWindowSize(windowSize);
                    ImVec2 itemSpacing = new ImVec2();
                    ImGui.getStyle().getItemSpacing(itemSpacing);
                    float windowX2 = windowPos.x + windowSize.x;
                    for (int i = 0; i < spriteSheets.get(j).size(); ++i)
                    {
                        Sprite sprite = spriteSheets.get(j).getSprite(i);
                        float spriteWidth = sprite.getWidth() * 2;
                        float spriteHeight = sprite.getHeight() * 2;
                        int id = sprite.getTextureID();
                        Vector2f[] texCoords = sprite.getTextureCoordinates();

                        ImGui.pushID(i);
                        if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
                        {
                            GameObject object = PrefabManager.generateDefaultSpriteObject(sprite, GridControls.snapToGrid ? GridlinesComponent.gridSize.x : sprite.getWidth() , GridControls.snapToGrid ? GridlinesComponent.gridSize.y : sprite.getHeight());
                            MouseControlComponent.pickupObject(object);
                        }
                        ImGui.popID();

                        ImVec2 lastButtonPos = new ImVec2();
                        ImGui.getItemRectMax(lastButtonPos);
                        float lastButtonX2 = lastButtonPos.x;
                        float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                        if (i + 1 < spriteSheets.get(j).size() && nextButtonX2 < windowX2)
                        {
                            ImGui.sameLine();
                        }

                    }
                }
            }
            ImGui.endTabItem();
        }
    }

    private static void textureDisplay()
    {
        if (ImGui.beginTabItem("Textures"))
        {
            if (ImGui.button("Add a Texture")) open = !open;
            ImGui.sameLine();
            if (ImGui.button("Clear")) AssetPool.clearTexturePool();
            if (open)
            {
                name = Widgets.inputText("Name", name);
                path = Widgets.inputText("Path", path);
                ImGui.columns(2);
                if (ImGui.button("Browse"))
                {
                    path = TinyFileDialogs.tinyfd_openFileDialog("Path", EditorSystemManager.projectDir + "/Assets/Textures/", null, null, false);
                }
                ImGui.nextColumn();
                if (!name.isEmpty() && !path.isEmpty())
                {
                    if (ImGui.button("Add"))
                    {
                        AssetPool.addTexture(name, path, true);
                    }
                }
                ImGui.columns(1);
            }
            List<Texture> textures = AssetPool.getAllTextures();
            List<String> names = AssetPool.getTextureNames();
            for (int j = 0; j < textures.size(); ++j)
            {
                if (ImGui.collapsingHeader(names.get(j)))
                {
                    if (ImGui.button("Remove"))
                    {
                        AssetPool.removeTexture(names.get(j));
                        continue;
                    }
                    Sprite sprite = new Sprite();
                    sprite.setTexture(textures.get(j));
                    int id = sprite.getTextureID();
                    ImGui.pushID(id);
                    Vector2f[] texCoords = sprite.getTextureCoordinates();
                    if (ImGui.imageButton(id, sprite.getWidth(), sprite.getHeight(), texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
                    {
                        GameObject object = PrefabManager.generateDefaultSpriteObject(sprite, GridControls.snapToGrid ? GridlinesComponent.gridSize.x : sprite.getWidth() , GridControls.snapToGrid ? GridlinesComponent.gridSize.y : sprite.getHeight());
                        MouseControlComponent.pickupObject(object);
                    }
                    ImGui.popID();
                }
            }
            ImGui.endTabItem();
        }
    }

    private static void soundDisplay()
    {
        if (ImGui.beginTabItem("Sounds"))
        {
            if (ImGui.button("Add a Sound")) open = !open;
            ImGui.sameLine();
            if (ImGui.button("Clear")) AssetPool.clearSoundPool();
            if (open)
            {
                name = Widgets.inputText("Name", name);
                path = Widgets.inputText("Path", path);
                loop = Widgets.drawBoolControl("Loop", loop);
                ImGui.columns(2);
                if (ImGui.button("Browse"))
                {
                    path = TinyFileDialogs.tinyfd_openFileDialog("Path", EditorSystemManager.projectDir + "/Assets/Sounds/", null, null, false);
                }
                ImGui.nextColumn();
                if (!name.isEmpty() && !path.isEmpty())
                {
                    if (ImGui.button("Add"))
                    {
                        AssetPool.addSound(name, path, loop, true);
                    }
                }
                ImGui.columns(1);
            }
            List<Sound> sounds = AssetPool.getAllSounds();
            List<String> names = AssetPool.getAllSoundNames();

            for (int i = 0; i < sounds.size(); ++i)
            {
                if (ImGui.button("Remove"))
                {
                    AssetPool.removeSound(names.get(i));
                    continue;
                }
                if (ImGui.button(names.get(i)))
                {
                    if (!sounds.get(i).isPlaying())
                    {
                        sounds.get(i).play();
                    }
                    else
                    {
                        sounds.get(i).stop();
                    }
                }
            }
            ImGui.endTabItem();
        }
    }

    public static void render()
    {
        ImGui.begin("Asset Pool");
        if (ImGui.beginTabBar("Window Tabs"))
        {
            textureDisplay();
            spriteSheetDisplay();
            soundDisplay();
            ImGui.endTabBar();
        }
        ImGui.end();
    }
}