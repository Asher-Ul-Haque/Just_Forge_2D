package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.AudioSystem.Sound;
import Just_Forge_2D.EditorSystem.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.InputControls.MouseControlComponent;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.Sprite;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.util.List;
import java.util.function.Consumer;

public class AssetPoolDisplay
{
    // - - - Dialog Box
    private static String name = "";
    private static String path = "";
    private static boolean open = false;
    private static boolean spriteEditorOpen = false;
    private static boolean keepSize;

    // - - - Sprite Sheet
    private static final Vector2f size = new Vector2f(GridlinesComponent.gridSize);
    private static int spriteCount;
    private static int spriteSpacing;

    // - - - LowLevelSound
    private static boolean loop;

    // - - - Icons
    private static final String ICON_ADD = Icons.PlusSquare + " Add";
    private static final String ICON_REMOVE = Icons.TrashAlt + " Remove";
    private static final String ICON_BROWSE = Icons.FolderOpen + " Browse";

    // - - - Mode
    public enum Mode
    {
        SELECTION,
        CREATION
    }

    private static Mode mode = Mode.CREATION;
    private static Consumer<Sprite> onSpriteSelection;



    // - - - | Functions | - - -



    public static void enableSelection(Consumer<Sprite> CALLBACK)
    {
        mode = Mode.SELECTION;
        onSpriteSelection = CALLBACK;
    }

    public static Mode getMode()
    {
        return mode;
    }


    // - - - Centralized handling for browsing path
    private static String handleBrowse(String DIALOG_TITLE, String DEFAULT_PATH, String[] FILE_TYPE_FILTERS)
    {
        try (MemoryStack stack = MemoryStack.stackPush())
        {
            // - - - Allocate the pointer buffer for filter patterns
            PointerBuffer filterBuffer = stack.mallocPointer(FILE_TYPE_FILTERS.length);

            // - - - Populate the filter buffer with the provided file type patterns
            for (String filter : FILE_TYPE_FILTERS)
            {
                filterBuffer.put(MemoryUtil.memUTF8(filter));
            }

            // - - - Flip the buffer for reading
            filterBuffer.flip();

            // - - - Call the tinyfd_openFileDialog with the filter patterns
            String selectedPath = TinyFileDialogs.tinyfd_openFileDialog(DIALOG_TITLE, DEFAULT_PATH, filterBuffer, null, false);
            return selectedPath != null ? selectedPath : "";
        }
    }

    // - - - Helper for asset addition
    private static void addAsset(String assetName, String assetPath, boolean condition, Runnable onAdd, Runnable EXTRA)
    {
        name = Widgets.inputText(Icons.User + "  Name", name);
        path = Widgets.inputText(Icons.FileImage + "  Path", path);
        EXTRA.run();
        ImGui.columns(2);

        if (Widgets.button(ICON_BROWSE))
        {
            switch (assetName)
            {
                case "Sprite Sheet" ->
                        path = handleBrowse("Select a Sprite Sheet", assetPath, new String[]{"*.png", "*.jpg", "*.jpeg"});
                case "Texture" ->
                        path = handleBrowse("Select a Texture", assetPath, new String[]{"*.png", "*.jpg", "*.jpeg"});
                case "Sound" ->
                        path = handleBrowse("Select a Sound", assetPath, new String[]{"*.wav", "*.mp3", "*.ogg"});
            }
        }

        ImGui.nextColumn();

        if (condition)
        {
            if (Widgets.button(Icons.FileImport + " Add"))
            {
                onAdd.run();
            }
        }
        ImGui.columns(1);
    }



    // - - - Sprite Sheet Display - - -


    private static void spriteSheetDisplay()
    {
        if (ImGui.beginTabItem(Icons.PhotoVideo + " Sprite Sheets"))
        {
            handleAddAndClear(AssetPool::clearSpriteSheetPool, "a SpriteSheet");

            if (open)
            {
                addAsset("Sprite Sheet", EditorSystemManager.projectDir + "/Assets/Textures/", !name.isEmpty() && !path.isEmpty(),
                () ->
                {
                    Texture t = AssetPool.makeTexture(path);
                    if (t.init(path))
                    {
                        SpriteSheet sheet = new SpriteSheet(t, (int) size.x, (int) size.y, spriteCount, spriteSpacing);
                        AssetPool.addSpriteSheet(name, sheet, true);
                    }
                    else Logger.FORGE_LOG_ERROR("Bad Texture: " + name);
                },
                () ->
                {
                    Widgets.drawVec2Control(Icons.Expand + "  Sprite Size", size);
                    spriteCount = Widgets.drawIntControl(Icons.ListOl + "  Sprite Count", spriteCount);
                    spriteSpacing = Widgets.drawIntControl(Icons.Underline + " Sprite Spacing", spriteSpacing);
                });
            }
            drawSpriteSheets();
            ImGui.endTabItem();
        }
    }

    private static void drawSpriteSheets()
    {
        Widgets.text("");
        List<String> spriteSheetNames = AssetPool.getSpriteSheetNames();
        if (mode.equals(Mode.CREATION))
        {
            if (!spriteSheetNames.isEmpty()) keepSize = Widgets.drawBoolControl(Icons.Expand + "  Keep Size", keepSize);
        }
        for (String sheetName : spriteSheetNames)
        {
            if (ImGui.collapsingHeader(sheetName))
            {
                drawSpriteSheetOptions(sheetName);
            }
        }
    }

    private static void drawSpriteSheetOptions(String NAME)
    {
        if (mode.equals(Mode.CREATION))
        {
            ImGui.columns(2);
            if (Widgets.button(ICON_REMOVE))
            {
                AssetPool.removeSpriteSheet(NAME);

                return;
            }

            ImGui.nextColumn();
            if (Widgets.button(Icons.PencilRuler + "  Edit")) spriteEditorOpen = !spriteEditorOpen;
            ImGui.columns(1);
            if (spriteEditorOpen) AssetPool.getSpriteSheet(NAME).Editor();
            Widgets.text("");
        }
        List<Sprite> sprites = AssetPool.getSpriteSheet(NAME).getSprites();
        drawSprites(sprites);
        ImGui.newLine();
    }

    private static void drawSprites(List<Sprite> SPRITES)
    {
        ImVec2 windowPos = new ImVec2();
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImGui.getWindowSize(windowSize);

        float windowX2 = windowPos.x + windowSize.x;

        for (int i = 0; i < SPRITES.size(); ++i)
        {
            Sprite sprite = SPRITES.get(i);
            drawSpriteButton(sprite, windowX2, i);
        }
    }

    private static void drawSpriteButton(Sprite sprite, float windowX2, int index)
    {
        float spriteWidth = sprite.getWidth() * 2;
        float spriteHeight = sprite.getHeight() * 2;
        int id = sprite.getTexture().getID();
        Vector2f[] texCoords = sprite.getTextureCoordinates();

        ImGui.pushID(index);
        if (Widgets.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
        {
            switch (mode)
            {
                case CREATION:
                    GameObject object = PrefabManager.generateDefaultSpriteObject(sprite, keepSize ? GridlinesComponent.gridSize.x : sprite.getWidth() / Settings.DEFAULT_SIZE_DOWN_FACTOR,
                        keepSize ? GridlinesComponent.gridSize.y : sprite.getHeight() / Settings.DEFAULT_SIZE_DOWN_FACTOR);
                    MouseControlComponent.pickupObject(object);
                    break;

                case SELECTION:
                    onSpriteSelection.accept(sprite.copy());
                    mode = Mode.CREATION;
                    break;
            }
        }
        ImGui.popID();

        ImVec2 lastButtonPos = new ImVec2();
        ImGui.getItemRectMax(lastButtonPos);
        float lastButtonX2 = lastButtonPos.x;
        float nextButtonX2 = lastButtonX2 + spriteWidth;
        if (nextButtonX2 < windowX2) ImGui.sameLine();
    }


    // - - - Texture - - -

    private static void textureDisplay()
    {
        if (ImGui.beginTabItem(Icons.FileImage + " Textures"))
        {
            if (mode.equals(Mode.CREATION))
            {
                handleAddAndClear(AssetPool::clearTexturePool, "a Texture");

                if (open)
                {
                    addAsset("Texture", EditorSystemManager.projectDir + "/Assets/Textures/", !name.isEmpty() && !path.isEmpty(), () -> AssetPool.addTexture(name, path, true), () -> {
                    });
                }
            }
            drawTextures();
            ImGui.endTabItem();
        }
    }

    private static void drawTextures()
    {
        Widgets.text("");
        List<String> textureNames = AssetPool.getTextureNames();
        for (String textureName : textureNames)
        {
            if (ImGui.collapsingHeader(textureName))
            {
                if (mode.equals(Mode.CREATION))
                {
                    keepSize = Widgets.drawBoolControl(Icons.Expand + "  Keep Size", keepSize);

                    if (Widgets.button(ICON_REMOVE))
                    {
                        AssetPool.removeTexture(textureName);
                        continue;
                    }
                }

                drawTexture(AssetPool.getTexture(textureName));
            }
        }
    }

    private static void drawTexture(Texture TEXTURE)
    {
        if (TEXTURE == null) return;

        Sprite sprite = new Sprite();
        sprite.setTexture(TEXTURE);

        Widgets.button(TEXTURE.getFilepath());

        int id = TEXTURE.getID();
        Vector2f[] texCoords = sprite.getTextureCoordinates();

        if (Widgets.imageButton(id, sprite.getWidth(), sprite.getHeight(), texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
        {
            switch (mode)
            {
                case CREATION:
                    GameObject object = PrefabManager.generateDefaultSpriteObject(sprite, !keepSize ? GridlinesComponent.gridSize.x : sprite.getWidth() / Settings.DEFAULT_SIZE_DOWN_FACTOR,
                            !keepSize ? GridlinesComponent.gridSize.y : sprite.getHeight() / Settings.DEFAULT_SIZE_DOWN_FACTOR);
                    MouseControlComponent.pickupObject(object);
                    break;

                case SELECTION:
                    onSpriteSelection.accept(sprite.copy());
                    mode = Mode.CREATION;
                    break;
            }
        }
    }

    // - - - Sounds
    private static void soundDisplay()
    {
        if (ImGui.beginTabItem(Icons.Music + " Sounds"))
        {
            handleAddAndClear(AssetPool::clearSoundPool, "a Sound");

            if (open)
            {
                addAsset("Sound", EditorSystemManager.projectDir + "/Assets/Sounds/", !name.isEmpty() && !path.isEmpty(), () -> AssetPool.addSound(name, path, loop, true), ()-> loop = Widgets.drawBoolControl(Icons.SyncAlt + " Looping", loop));
            }
            drawSounds();
            ImGui.endTabItem();
        }
    }

    private static void drawSounds()
    {
        Widgets.text("");
        List<String> soundNames = AssetPool.getAllSoundNames();

        for (int i = 0; i < soundNames.size(); ++i)
        {
            Sound sound = AssetPool.getSound(soundNames.get(i));
            String soundName = soundNames.get(i);

            if (Widgets.button(ICON_REMOVE + " ##" + i))
            {
                AssetPool.removeSound(soundName);
                continue;
            }

            ImGui.sameLine();

            if (Widgets.button((sound.isPlaying() ? Icons.Stop : Icons.Play) + " " + soundName))
            {
                if (!sound.isPlaying()) sound.play();
                else sound.stop();
            }
        }
    }

    // - - - Helper to handle adding and clearing assets
    private static void handleAddAndClear(Runnable clearMethod, String addLabel)
    {
        if (Widgets.button(ICON_ADD + " " + addLabel)) open = !open;
        ImGui.sameLine();
        if (Widgets.button(Icons.TrashAlt + " Clear")) clearMethod.run();
        Widgets.text("");
    }

    public static void render()
    {
        ImGui.begin(Icons.Images + "  Asset Pool");
        if (ImGui.beginTabBar("Window Tabs"))
        {
            switch (mode)
            {
                case CREATION:
                    soundDisplay();

                case SELECTION:
                    textureDisplay();
                    spriteSheetDisplay();
                    break;
            }
            ImGui.endTabBar();
        }
        ImGui.end();
    }
}
