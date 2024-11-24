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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private static boolean keepAspectRatio = true;
    private static boolean copyToProject = true;
    private static boolean popup = false;
    private static String popupMessage = "";
    private static Runnable clearMethod;
    private static String toDelete = "";

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
        SPRITE_SELECTION,
        SOUND_SELECTION,
        CREATION
    }

    private static Mode mode = Mode.CREATION;
    private static Consumer<Sprite> onSpriteSelection;
    private static Consumer<Sound> onSoundSelection;



    // - - - | Functions | - - -



    public static void enableSpriteSelection(Consumer<Sprite> CALLBACK)
    {
        mode = Mode.SPRITE_SELECTION;
        onSpriteSelection = CALLBACK;
    }

    public static void enableSoundSelection(Consumer<Sound> CALLBACK)
    {
        mode = Mode.SOUND_SELECTION;
        onSoundSelection = CALLBACK;
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


    private static void addAsset(String assetName, String assetPath, boolean condition, Runnable onAdd, Runnable EXTRA)
    {
        name = Widgets.inputText(Icons.User + "  Name", name);
        path = Widgets.inputText(Icons.FileImage + "  Path", path);
        copyToProject = Widgets.drawBoolControl(Icons.Copy + "  Copy To Project", copyToProject);
        EXTRA.run();
        ImGui.columns(2);

        if (Widgets.button(ICON_BROWSE))
        {
            switch (assetName)
            {
                case "Sprite Sheet" -> path = handleBrowse("Select a Sprite Sheet", assetPath, new String[]{"*.png", "*.jpg", "*.jpeg"});
                case "Texture" -> path = handleBrowse("Select a Texture", assetPath, new String[]{"*.png", "*.jpg", "*.jpeg"});
                case "Sound" -> path = handleBrowse("Select a Sound", assetPath, new String[]{"*.wav", "*.ogg"});
            }
        }

        ImGui.nextColumn();

        if (condition)
        {
            if (Widgets.button(Icons.FileImport + " Add"))
            {
                if (copyToProject)
                {
                    try
                    {
                        String destinationFolder = switch (assetName)
                        {
                            case "Sprite Sheet", "Texture" -> EditorSystemManager.projectDir + "/Assets/Textures/";
                            case "Sound" -> EditorSystemManager.projectDir + "/Assets/Sounds/";
                            default -> "";
                        };

                        Path sourcePath = Paths.get(path);
                        Path destinationPath = Paths.get(destinationFolder + sourcePath.getFileName());

                        // - - - Ensure the destination folder exists
                        Files.createDirectories(destinationPath.getParent());

                        // - - - Copy the file to the destination folder
                        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);

                        // - - - Update the path to the new location within the project directory
                        path = destinationPath.toString();
                    }
                    catch (IOException e)
                    {
                        Logger.FORGE_LOG_ERROR("Failed to copy asset: " + e.getMessage());
                    }
                }

                onAdd.run();
                open = false;
            }
        }
        ImGui.columns(1);
    }




    // - - - Sprite Sheet Display - - -


    private static void spriteSheetDisplay()
    {
        if (ImGui.beginTabItem(Icons.PhotoVideo + " Sprite Sheets"))
        {
            handleAddAndClear(AssetPool::clearSpriteSheetPool, "a SpriteSheet", AssetPool.getAllSpriteSheets().isEmpty());

            if (open)
            {
                addAsset("Sprite Sheet", EditorSystemManager.projectDir + "/Assets/Textures/", !name.isEmpty() && !path.isEmpty(),
                () ->
                {
                    Texture t = new Texture();
                    if (t.init(path))
                    {
                        SpriteSheet sheet = new SpriteSheet(t, (int) size.x, (int) size.y, spriteCount, spriteSpacing);
                        AssetPool.addSpriteSheet(name, sheet, true);
                    }
                    else
                    {
                        popup = true;
                        open = false;
                        popupMessage = "Bad Texture: " + name;
                        Logger.FORGE_LOG_ERROR(popupMessage);
                    }
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
            if (!spriteSheetNames.isEmpty())
            {
                keepSize = Widgets.drawBoolControl(Icons.Expand + "  Keep Size", keepSize);
                if (!keepSize) keepAspectRatio = Widgets.drawBoolControl(Icons.ExpandArrowsAlt + "  Keep Aspect Ratio", keepAspectRatio);
            }
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
                clearMethod = () -> {AssetPool.removeSpriteSheet(NAME);};
                toDelete = NAME;
                popup = true;
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
            spriteButton(sprite);
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
                handleAddAndClear(AssetPool::clearTexturePool, "a Texture", AssetPool.getAllTextures().isEmpty());

                if (open)
                {
                    addAsset("Texture", EditorSystemManager.projectDir + "/Assets/Textures/", !name.isEmpty() && !path.isEmpty(), () -> AssetPool.addTexture(name, path, !copyToProject), () -> {
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
                    if (!keepSize) keepAspectRatio = Widgets.drawBoolControl(Icons.ExpandArrowsAlt + "  Keep Aspect Ratio", keepAspectRatio);

                    if (Widgets.button(ICON_REMOVE))
                    {
                        clearMethod = () -> {AssetPool.removeTexture(textureName);};
                        toDelete = textureName;
                        popup = true;
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

        if (Widgets.imageButton(id, TEXTURE.getWidth() * 2, TEXTURE.getHeight() * 2, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
        {
            spriteButton(sprite);
        }
    }

    private static void spriteButton(Sprite sprite)
    {
        switch (mode)
        {
            case CREATION:
                GameObject object;
                if (!keepSize)
                {
                    if (keepAspectRatio)
                    {
                        float aspectRatio = sprite.getWidth() / sprite.getHeight();
                        float height = GridlinesComponent.gridSize.y;
                        float width = height * aspectRatio;
                        object = PrefabManager.generateObject(sprite, width, height);
                    }
                    else object = PrefabManager.generateObject(sprite, GridlinesComponent.gridSize.x, GridlinesComponent.gridSize.y);
                }
                else
                {
                    object = PrefabManager.generateObject(sprite, sprite.getWidth() / Settings.DEFAULT_SIZE_DOWN_FACTOR(), sprite.getHeight() / Settings.DEFAULT_SIZE_DOWN_FACTOR());
                }
                MouseControlComponent.pickupObject(object);
                break;

            case SPRITE_SELECTION:
                onSpriteSelection.accept(sprite.copy());
                mode = Mode.CREATION;
                break;
        }
    }

    // - - - Sounds
    private static void soundDisplay()
    {
        if (ImGui.beginTabItem(Icons.Music + " Sounds"))
        {
            if (mode.equals(Mode.CREATION))
            {
                handleAddAndClear(AssetPool::clearSoundPool, "a Sound", AssetPool.getAllSounds().isEmpty());

                if (open)
                {
                    addAsset("Sound", EditorSystemManager.projectDir + "/Assets/Sounds/", !name.isEmpty() && !path.isEmpty(), () -> AssetPool.addSound(name, path, loop, true), () -> loop = Widgets.drawBoolControl(Icons.SyncAlt + " Looping", loop));
                }
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
                clearMethod = () -> {AssetPool.removeSound(soundName);};
                toDelete = soundName;
                popup = true;
                continue;
            }

            ImGui.sameLine();

            if (Widgets.button((sound.isPlaying() ? Icons.Stop : Icons.Play) + " " + soundName))
            {
                switch (mode)
                {
                    case SOUND_SELECTION:
                        onSoundSelection.accept(sound);
                        mode = Mode.CREATION;
                        break;

                    case CREATION:
                        if (!sound.isPlaying()) sound.play();
                        else sound.stop();
                        break;
                }
            }
        }
    }

    // - - - Helper to handle adding and clearing assets
    private static void handleAddAndClear(Runnable CLEAR, String addLabel, boolean condition)
    {
        if (mode.equals(Mode.SPRITE_SELECTION)) return;
        if (Widgets.button(ICON_ADD + " " + addLabel)) open = !open;
        if (condition) return;
        ImGui.sameLine();
        if (Widgets.button(Icons.TrashAlt + " Clear"))
        {
            popup = true;
            clearMethod = CLEAR;
            if (addLabel.equals("a SpriteSheet"))
            {
                toDelete = "All Sprite Sheets";
            }
            else if (addLabel.equals("a Sound"))
            {
                toDelete = "All Sounds";
            }
            else if (addLabel.equals("a Texture"))
            {
                toDelete = "All Textures";
            }
        }
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
                    textureDisplay();
                    spriteSheetDisplay();
                    soundDisplay();
                    break;

                case SOUND_SELECTION:
                    soundDisplay();
                    ImGui.newLine();
                    if (Widgets.button("Stop Selection", true))
                    {
                        onSoundSelection = null;
                        mode = Mode.CREATION;
                    }
                    break;

                case SPRITE_SELECTION:
                    textureDisplay();
                    spriteSheetDisplay();
                    ImGui.newLine();
                    if (Widgets.button("Stop Selection", true))
                    {
                        onSpriteSelection = null;
                        mode = Mode.CREATION;
                    }
                    break;
            }
            ImGui.endTabBar();
        }
        if (popup)
        {
            switch(Widgets.popUp(Icons.ExclamationTriangle, "Delete Asset", "Are you sure you want to delete: \n" + toDelete))
            {
                case OK:
                    clearMethod.run();
                    popup = !popup;
                    clearMethod = null;
                    toDelete = "";
                    break;

                case CANCEL:
                    popup = !popup;
                    break;
            }
        }
        ImGui.end();
    }
}
