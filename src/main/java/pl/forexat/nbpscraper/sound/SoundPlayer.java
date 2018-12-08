package pl.forexat.nbpscraper.sound;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlayer
{
  Clip clip;
  
  public SoundPlayer()
    throws LineUnavailableException, IOException, UnsupportedAudioFileException
  {
    this.clip = AudioSystem.getClip();
    System.out.println("will play sound");
    InputStream soundStream = SoundPlayer.class.getResourceAsStream("/alarm/police.wav");
    if (soundStream == null) {
      System.out.println("Sound stream == null");
    }
    AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundStream);
    this.clip.open(inputStream);
  }
  
  public void alarm()
  {
    this.clip.setFramePosition(0);
    this.clip.flush();
    this.clip.start();
  }
}
