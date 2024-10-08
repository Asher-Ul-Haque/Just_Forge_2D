package Just_Forge_2D.AudioSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector3f;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SEC_OFFSET;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound extends Component
{
    private int bufferID;
    private int sourceID;
    private final String filepath;

    private boolean isPlaying = false;

    public Sound(String FILEPATH, boolean LOOPS)
    {
        this.filepath = FILEPATH;

        // - - - allocate space to store the return information from stb
        stackPush();
        IntBuffer channelsBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(FILEPATH, channelsBuffer, sampleRateBuffer);
        if (rawAudioBuffer == null)
        {
            Logger.FORGE_LOG_ERROR("Could not load sound: " + FILEPATH);
            stackPop();
            stackPop();
            return;
        }

        // - - - retrieve the extra information that was store d on the buffers
        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        // - - - free
        stackPop();
        stackPop();

        // - - - find the correct format
        int format = -1;
        if (channels == 1)
        {
            format = AL_FORMAT_MONO16;
        }
        else if (channels == 2)
        {
            format = AL_FORMAT_STEREO16;
        }

        bufferID = alGenBuffers();
        alBufferData(bufferID, format, rawAudioBuffer, sampleRate);

        // - - - generate the source
        sourceID = alGenSources();
        alSourcei(sourceID, AL_BUFFER, bufferID);
        alSourcei(sourceID, AL_LOOPING, LOOPS ? 1 : 0);
        alSourcei(sourceID, AL_POSITION, 0);
        alSourcef(sourceID, AL_GAIN, 1.0f);
        alSourcef(sourceID, AL_PITCH, 1.0f);

        // - - - free raw buffers
        free(rawAudioBuffer);
    }

    public void delete()
    {
        alDeleteSources(sourceID);
        alDeleteBuffers(bufferID);
    }

    public void play()
    {
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
            alSourceStop(sourceID);
            isPlaying = false;
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
        alSourcef(sourceID, AL_GAIN, VOLUME);
    }

    public void setPitch(float PITCH)
    {
        alSourcef(sourceID, AL_PITCH, PITCH);
    }

    public void setLooping(boolean LOOPS)
    {
        alSourcei(sourceID, AL_LOOPING, LOOPS ? 1 : 0);
    }

    public String getFilePath()
    {
        return this.filepath;
    }

    public boolean isPlaying()
    {
        int state = alGetSourcei(sourceID, AL_SOURCE_STATE);
        if (state == AL_STOPPED)
        {
            isPlaying = false;
        }
        return isPlaying;
    }
}