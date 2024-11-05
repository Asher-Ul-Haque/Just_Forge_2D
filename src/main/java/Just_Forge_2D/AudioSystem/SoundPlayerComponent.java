package Just_Forge_2D.AudioSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EditorSystem.Windows.AssetPoolDisplay;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class SoundPlayerComponent extends Component
{
    private Sound sound;
    private Vector3f position = new Vector3f();

    public SoundPlayerComponent() {}

    public SoundPlayerComponent(Sound SOUND)
    {
        this.position = new Vector3f(this.gameObject.transform.position, 0);
    }

    public void play()
    {
        if (warn()) return;
        this.sound.play();
    }

    public void stop()
    {
        if (warn()) return;
        this.sound.stop();
    }

    public void resume()
    {
        if (warn()) return;
        this.sound.resume();
    }

    public void pause()
    {
        if (warn()) return;
        this.sound.pause();
    }

    public void playFrom(float SECONDS)
    {
        if (warn()) return;
        this.sound.playFrom(SECONDS);
    }

    public void setPosition(Vector3f POSITION)
    {
        if (warn()) return;
        this.position = POSITION;
        this.sound.setPosition(POSITION);
    }

    public void setPosition(Vector2f POSITION)
    {
        if (warn()) return;
        this.position = new Vector3f(POSITION, 0.0f);
        this.setPosition(this.position);
    }

    public void setVolume(float VOLUME)
    {
        if (warn()) return;
        this.sound.setVolume(VOLUME);
    }

    public void setPitch(float PITCH)
    {
        if (warn()) return;
        this.sound.setPitch(PITCH);
    }

    public void setLooping(boolean LOOPS)
    {
        if (warn()) return;
        this.sound.setLooping(LOOPS);
    }

    private boolean warn()
    {
        if (this.sound != null) return false;
        sound = AssetPool.getSound("Default");
        Logger.FORGE_LOG_ERROR(this.gameObject + "'s Sound PLayer has no Sound");
        Logger.FORGE_LOG_WARNING("Setting Default Sound");
        return true;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public int getVolume()
    {
        if (warn()) return -1;
        return this.sound.getVolume();
    }

    public float getPitch()
    {
        if (warn()) return -1;
        return this.sound.getPitch();
    }

    public boolean isLooping()
    {
        if (warn()) return false;
        return this.sound.loops();
    }

    public void setSound(Sound SOUND)
    {
        if (SOUND != null)
        {
            this.sound = SOUND;
            return;
        }
        Logger.FORGE_LOG_ERROR("Cannot set null as sound");
    }

    @Override
    public void editorGUI()
    {
        deleteButton();
        if (Widgets.button(Icons.Music + "  Set Sound", true))
        {
            AssetPoolDisplay.enableSoundSelection(this::setSound);
        };
        if (this.sound == null) return;

        Widgets.drawVec3Control(Icons.MapPin + "  Position", getPosition());
        setPosition(getPosition());
        setPitch(Widgets.drawFloatControl(Icons.Microphone + "  Pitch", getPitch()));
        setVolume(Widgets.drawIntControl((getVolume() > 0 ? Icons.VolumeUp : Icons.VolumeMute) + "  Volume", getVolume()));
        setLooping(Widgets.drawBoolControl(Icons.RedoAlt + "  Looping", isLooping()));

        if (Widgets.button(!sound.isPlaying() ? Icons.Play : Icons.Stop))
        {
            if (sound.isPlaying()) sound.stop();
            else sound.play();
        }

        ImGui.sameLine();

        if (Widgets.button(!sound.isPlaying() ? Icons.PlayCircle : Icons.Pause))
        {
            if (sound.isPlaying()) sound.pause();
            else sound.resume();
        }
    }
}