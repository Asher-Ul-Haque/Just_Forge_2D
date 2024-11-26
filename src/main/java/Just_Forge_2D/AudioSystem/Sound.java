package Just_Forge_2D.AudioSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.AudioSystem.TinySound.LowLevelSound;
import Just_Forge_2D.AudioSystem.TinySound.TinySound;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector3f;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SEC_OFFSET;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound
{
    private int bufferID;
    private int sourceID;
    private final String filepath;
    public final boolean valid;
    private LowLevelSound wav;
    private boolean loop;
    private int volume = 100;
    private float pitch = 1;

    private transient boolean isPlaying = false;

    public Sound(String FILEPATH, boolean LOOPS)
    {
        this.loop = LOOPS;
        this.filepath = FILEPATH;

        if (FILEPATH.endsWith(".ogg"))
        {
            valid = loadOgg(FILEPATH);
        }
        else if (FILEPATH.endsWith(".wav"))
        {
            valid = loadWav(FILEPATH);
        }
        else
        {
            Logger.FORGE_LOG_ERROR("Unsupported sound format: " + FILEPATH);
            valid = false;
        }
    }

    private boolean loadOgg(String FILEPATH)
    {
        String correctPath;
        if (AssetPool.isAbsolutePath(FILEPATH)) correctPath = FILEPATH;
        else correctPath = new File(System.getProperty("user.dir"), FILEPATH).getAbsolutePath();

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);
            ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(correctPath, channelsBuffer, sampleRateBuffer);
            if (rawAudioBuffer == null)
            {
                Logger.FORGE_LOG_ERROR("Could not load .ogg sound: " + FILEPATH);
                return false;
            }

            int channels = channelsBuffer.get(0);
            int sampleRate = sampleRateBuffer.get(0);
            int format = (channels == 1) ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;

            bufferID = alGenBuffers();
            alBufferData(bufferID, format, rawAudioBuffer, sampleRate);

            createSource();
            free(rawAudioBuffer);
            return true;
        }
    }

    private boolean loadWav(String FILEPATH)
    {
        try
        {
            wav = TinySound.loadSound(new java.io.File(FILEPATH));
            if (wav == null)
            {
                Logger.FORGE_LOG_ERROR("Could not load .wav sound: " + FILEPATH);
                return false;
            }
            // - - - Logic for managing TinySound playback within OpenAL goes here, if applicable
            return true;
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_ERROR("Could not load .wav sound: " + FILEPATH + " Error: " + e.getMessage());
            return false;
        }
    }

    private void createSource()
    {
        sourceID = alGenSources();
        alSourcei(sourceID, AL_BUFFER, bufferID);
        alSourcei(sourceID, AL_LOOPING, loop ? 1 : 0);
        alSourcef(sourceID, AL_GAIN, 1.0f);
        alSourcef(sourceID, AL_PITCH, 1.0f);
    }

    public boolean loops()
    {
        return this.loop;
    }

    public void delete()
    {
        alDeleteSources(sourceID);
        alDeleteBuffers(bufferID);
    }

    public void play()
    {
        if (wav != null && !isPlaying)
        {
            wav.play(volume, pitch);
            isPlaying = true;
            return;
        }
        int state = alGetSourcei(sourceID, AL_SOURCE_STATE);
        if (state == AL_STOPPED)
        {
            isPlaying = false;
            alSourcei(sourceID, AL_POSITION, 0);
        }

        if (!isPlaying)
        {
            alSourcePlay(sourceID);
            isPlaying = true;
        }
    }

    public void stop()
    {
        if (isPlaying)
        {
            isPlaying = false;
            if (wav == null) alSourceStop(sourceID);
            else wav.stop();
        }
    }

    public void pause()
    {
        if (isPlaying)
        {
            alSourcePause(sourceID);
            isPlaying = false;
        }
    }

    public void resume()
    {
        if (!isPlaying)
        {
            alSourcePlay(sourceID);
            isPlaying = true;
        }
    }

    public void playFrom(float SECONDS)
    {
        alSourcef(sourceID, AL_SEC_OFFSET, SECONDS);
        play();
    }

    public void setPosition(Vector3f POSITION)
    {
        alSource3f(sourceID, AL_POSITION, POSITION.x, POSITION.y, POSITION.z);
    }

    public void setVolume(float VOLUME)
    {
        volume = (int) Math.max(0, Math.min(100, VOLUME));
        alSourcef(sourceID, AL_GAIN, Math.max(0.0f, Math.min(1.0f, VOLUME / 100)));
    }

    public int getVolume()
    {
        return volume;
    }

    public float getPitch()
    {
        return pitch;
    }

    public void setPitch(float PITCH)
    {
        pitch = PITCH;
        alSourcef(sourceID, AL_PITCH, PITCH);
    }

    public void setLooping(boolean LOOPS)
    {
        this.loop = LOOPS;
        alSourcei(sourceID, AL_LOOPING, LOOPS ? 1 : 0);
    }

    public String getFilePath()
    {
        return this.filepath;
    }

    public boolean isPlaying()
    {
        if (wav != null) return isPlaying;
        int state = alGetSourcei(sourceID, AL_SOURCE_STATE);
        if (state == AL_STOPPED)
        {
            isPlaying = false;
        }
        return isPlaying;
    }
}