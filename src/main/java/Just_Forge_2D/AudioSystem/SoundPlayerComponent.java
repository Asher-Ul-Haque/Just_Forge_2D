package Just_Forge_2D.AudioSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class SoundPlayerComponent extends Component
{
    private Sound sound;
    private Vector3f position = new Vector3f();
    private float volume = 1.0f;
    private float pitch = 1.0f;
    private boolean looping;

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
        this.volume = VOLUME;
        this.sound.setVolume(VOLUME);
    }

    public void setPitch(float PITCH)
    {
        if (warn()) return;
        this.pitch = PITCH;
        this.sound.setPitch(PITCH);
    }

    public void setLooping(boolean LOOPS)
    {
        if (warn()) return;
        this.looping = LOOPS;
        this.sound.setLooping(LOOPS);
    }

    private boolean warn()
    {
        if (this.sound != null) return false;
        Logger.FORGE_LOG_ERROR(this.gameObject + "'s Sound PLayer has no Sound");
        return true;
    }

    public Vector3f getPosition()
    {
        return position;
    }

    public float getVolume()
    {
        return this.volume;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public boolean isLooping()
    {
        return this.looping;
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
}