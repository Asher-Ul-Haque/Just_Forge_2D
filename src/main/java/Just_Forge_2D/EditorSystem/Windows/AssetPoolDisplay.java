package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.AudioSystem.Sound;
import Just_Forge_2D.EditorSystem.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EditorSystem.InputControls.MouseControlComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

import java.io.File;
import java.util.Collection;

public class AssetPoolDisplay
{
    public static void render()
    {
        ImGui.begin("Asset Pool");
        if (ImGui.beginTabBar("Window Tabs"))
        {
            if (ImGui.beginTabItem("Sounds"))
            {
                Collection<Sound> sounds = AssetPool.getAllSounds();
                for (Sound sound : sounds)
                {
                    File tmp = new File(sound.getFilePath());
                    if (ImGui.button(tmp.getName()))
                    {
                        if (!sound.isPlaying())
                        {
                            sound.play();
                        }
                        else
                        {
                            sound.stop();
                        }
                    }
                }
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Sprite Sheets"))
            {
                Collection<SpriteSheet> spriteSheets = AssetPool.getAllSpriteSheets();
                for (SpriteSheet sheet: spriteSheets)
                {
                    if (ImGui.collapsingHeader(sheet.getTexture().getFilepath()))
                    {
                        ImVec2 windowPos = new ImVec2();
                        ImGui.getWindowPos(windowPos);
                        ImVec2 windowSize = new ImVec2();
                        ImGui.getWindowSize(windowSize);
                        ImVec2 itemSpacing = new ImVec2();
                        ImGui.getStyle().getItemSpacing(itemSpacing);
                        float windowX2 = windowPos.x + windowSize.x;
                        for (int i = 0; i < sheet.size(); ++i)
                        {
                            Sprite sprite = sheet.getSprite(i);
                            float spriteWidth = sprite.getWidth() * 2;
                            float spriteHeight = sprite.getHeight() * 2;
                            int id = sprite.getTextureID();
                            Vector2f[] texCoords = sprite.getTextureCoordinates();

                            ImGui.pushID(i);
                            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
                            {
                                GameObject object = PrefabManager.generateDefaultSpriteObject(sprite, GridlinesComponent.gridSize.x, GridlinesComponent.gridSize.y);
                                MouseControlComponent.pickupObject(object);
                            }
                            ImGui.popID();

                            ImVec2 lastButtonPos = new ImVec2();
                            ImGui.getItemRectMax(lastButtonPos);
                            float lastButtonX2 = lastButtonPos.x;
                            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                            if (i + 1 < sheet.size() && nextButtonX2 < windowX2)
                            {
                                ImGui.sameLine();
                            }
                        }
                    }
                }
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }
        ImGui.end();
    }
}