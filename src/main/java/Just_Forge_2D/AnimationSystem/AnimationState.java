package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EditorSystem.Windows.AssetPoolDisplay;
import Just_Forge_2D.RenderingSystem.Sprite;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import imgui.ImGui;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnimationState
{
    // - - - private variables
    public String title;
    public List<Frame> animationFrames = new ArrayList<>();
    private static Sprite defaultSprite = new Sprite();
    private transient float timeTracker = 0.0f;
    private transient int currentSprite = 0;
    public boolean doesLoop = false;
    public boolean isFinished = false;
    private float previewSpeed = Settings.DEFAULT_FRAME_TIME();
    private transient boolean isPreviewing;
    private transient float elapsedTime = 0;


    // - - - Functions - - -


    // - - - Constructor - - -

    public AnimationState(String TITLE, boolean LOOP, Sprite DEFAULT)
    {
        Logger.FORGE_LOG_DEBUG("Adding new animation state : " + TITLE);
        this.title = TITLE;
        this.doesLoop = LOOP;
        defaultSprite = DEFAULT;
        this.animationFrames = new ArrayList<>();
        this.isFinished = false;
    }


    // - - - add frames - - -

    public void addFrame(Sprite SPRITE, float FRAME_TIME)
    {
        animationFrames.add(new Frame(SPRITE, FRAME_TIME));
    }

    public void addFrame(Sprite SPRITE)
    {
        animationFrames.add(new Frame(SPRITE, Settings.DEFAULT_FRAME_TIME()));
    }

    public void addFrames(List<Sprite> SPRITES, float FRAME_TIME)
    {
        for (Sprite sprite : SPRITES)
        {
            this.animationFrames.add(new Frame(sprite, FRAME_TIME));
        }
    }


    // - - - run and manage - - -

    public void update(float DELTA_TIME)
    {
        if (currentSprite < animationFrames.size())
        {
            timeTracker -= DELTA_TIME;
            if (timeTracker <= 0)
            {
                if (!(currentSprite == animationFrames.size() - 1 && !doesLoop))
                {
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                }
                else
                {
                    this.isFinished = true;
                }
                timeTracker = animationFrames.get(currentSprite).frameTime;
            }
        }
        else
        {
            this.isFinished = true;
        }
    }

    public void refreshTextures()
    {
        Logger.FORGE_LOG_DEBUG("Refreshing Textures");
        for (Frame frame : animationFrames)
        {
            frame.sprite.setTexture(AssetPool.makeTexture(frame.sprite.getTexture().getFilepath()));
        }
    }

    public void reset()
    {
        this.currentSprite = 0;
        this.timeTracker = animationFrames.isEmpty() ? 0.0f : animationFrames.get(0).frameTime;
        this.isFinished = false;
    }

    public void setLoop(boolean REALLY)
    {
        Logger.FORGE_LOG_TRACE("Setting looping status of : " + this);
        if (this.doesLoop == REALLY)
        {
            Logger.FORGE_LOG_ERROR(this + " has looping status already ste to : " + REALLY);
            return;
        }
        this.doesLoop = REALLY;
    }


    // - - - Getters - - -

    public Sprite getCurrentSprite()
    {
        if (currentSprite < animationFrames.size())
        {
            return animationFrames.get(currentSprite).sprite;
        }

        return defaultSprite;
    }

    public boolean isFinished()
    {
        return this.isFinished;
    }

    @Override
    public String toString()
    {
        return this.title;
    }

    public boolean isLooping()
    {
        return this.doesLoop;
    }


    // - - - Editor
    protected void previewControls(float DELTA_TIME)
    {
        if (Widgets.button(isPreviewing ? (Icons.Stop + " Stop") : (Icons.Play + "  Play")))
        {
            isPreviewing = !isPreviewing;
        }

        if (isPreviewing)
        {
            elapsedTime += DELTA_TIME * previewSpeed;
            float totalAnimationDuration = calculateTotalDuration();

            if (totalAnimationDuration > 0)
            {
                if (elapsedTime >= totalAnimationDuration)
                {
                    elapsedTime %= totalAnimationDuration;
                    if (!isLooping()) isPreviewing = false;
                }

                // - - - Find the current frame based on elapsedTime
                Sprite currentFrameSprite = getCurrentFrameSprite(elapsedTime);
                if (currentFrameSprite != null)
                {
                    Vector2f[] texCoords = currentFrameSprite.getTextureCoordinates();
                    Widgets.image(currentFrameSprite.getTextureID(), currentFrameSprite.getWidth() * 2, currentFrameSprite.getHeight() * 2,
                            texCoords, true);}
            }
        }
    }

    protected void newFrameControls()
    {
        // - - - Add a new frame
        if (Widgets.button(Icons.PlusSquare + "  Add Frame" + " ##" + hashCode()))
        {
            Sprite n = new Sprite();
            addFrame(n);
        }
        if (Widgets.drawBoolControl(Icons.Redo + "  Looping", doesLoop) != doesLoop) setLoop(!doesLoop);
        previewSpeed = Math.max(Float.MIN_VALUE, Widgets.drawFloatControl(Icons.HourglassHalf + "  Preview Speed", previewSpeed));
    }

    public void editorGUI()
    {
        // - - - Animation preview controls
        Widgets.text(AssetPoolDisplay.getMode().equals(AssetPoolDisplay.Mode.SPRITE_SELECTION) ? "Click on any Texture or Sprite Sheet in the Asset Pool" : "");


        // - - - Loop through frames and display their editorGUI
        for (int i = 0; i < animationFrames.size(); i++)
        {
            animationFrames.get(i).editorGUI();

            if (Widgets.button(Icons.Trash + "  Remove Frame" + " ##" + hashCode() + i))
            {
                animationFrames.remove(i);
                continue;
            }
            ImGui.sameLine();

            // - - - Reordering options (Up/Down buttons)
            if (i > 0 && Widgets.button(Icons.ArrowUp + "  Move Up" + " ##" + hashCode() + i))
            {
                Collections.swap(animationFrames, i, i - 1);
            }
            ImGui.sameLine();
            if (i < animationFrames.size() - 1 && Widgets.button(Icons.ArrowDown + "  Move Down" + " ##" + hashCode() + i))
            {
                Collections.swap(animationFrames, i, i + 1);
            }
            Widgets.text("");
        }
    }

    private float calculateTotalDuration()
    {
        float totalDuration = 0;
        for (Frame frame : animationFrames)
        {
            totalDuration += frame.frameTime;
        }
        return totalDuration;
    }

    // - - - Helper function to get the current frame sprite based on elapsed time
    private Sprite getCurrentFrameSprite(float elapsedTime)
    {
        float currentTime = 0;
        for (Frame frame : animationFrames)
        {
            currentTime += frame.frameTime;
            if (elapsedTime <= currentTime)
            {
                return frame.sprite;
            }
        }
        return null; // - - - Fallback, should not happen in looping animations
    }

}