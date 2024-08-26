package Just_Forge_2D.AudioSsstem;

import Just_Forge_2D.Utils.Logger;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;

public class AudioSystemManager
{
    private static boolean isOpenALinitialized = false;
    private static long audioContext;
    private static long audioDevicePtr;

    public static void initialize()
    {
        if (isOpenALinitialized)
        {
            Logger.FORGE_LOG_WARNING("Audio System is already online");
            return;
        }

        // - - - Initialize audio

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        assert defaultDeviceName != null;
        audioDevicePtr = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevicePtr, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevicePtr);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10)
        {
            Logger.FORGE_LOG_ERROR("Audio Not supported");
            assert false;
        }
        isOpenALinitialized = true;
    }


    public static void terminate()
    {
        if (!isOpenALinitialized)
        {
            Logger.FORGE_LOG_ERROR("Audio System is already offline");
            return;
        }
        // - - - Destroy Audio Context
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevicePtr);

        isOpenALinitialized = false;
    }
}
