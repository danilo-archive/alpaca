package Hack;
import java.io.File;
import java.io.IOException;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.FFMPEGLocator;
import it.sauronsoftware.jave.InputFormatException;
import javaFlacEncoder.FLAC_FileEncoder;

import javax.sound.sampled.AudioFileFormat;

public class VideoToAudio {


    public static void convertToFLAC(File video , String pathFlac) throws IllegalArgumentException, InputFormatException, EncoderException
    {
        File target = new File(pathFlac);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("flac");
        audio.setBitRate(new Integer(128000));
        audio.setChannels(new Integer(1));
        audio.setSamplingRate(new Integer(44100));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("flac");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        encoder.encode(video, target, attrs);
    }

    public static void convertToMP3(File video , String pathFlac) throws IllegalArgumentException, InputFormatException, EncoderException
    {
        File target = new File(pathFlac);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("flac");
        audio.setBitRate(new Integer(128000));
        audio.setChannels(new Integer(1));
        audio.setSamplingRate(new Integer(44100));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("flac");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        encoder.encode(video, target, attrs);
    }

}
