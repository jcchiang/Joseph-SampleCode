package gui.music;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class BGMPlayer {
   private static File soundFile = null;
   private static AudioInputStream audioIn = null;
   private static Clip clip = null;
      
   public static void playFile(File newBGM){
	   if(clip != null){
			   clip.stop();
	   }
	   try {
	         // Open an audio input stream.
	    	 soundFile = newBGM;
	         audioIn = AudioSystem.getAudioInputStream(soundFile);
	         // Get a sound clip resource.
	         clip = AudioSystem.getClip();
	         // Open audio clip and load samples from the audio input stream.
	         clip.open(audioIn);
	         clip.start();
	         clip.loop(Clip.LOOP_CONTINUOUSLY);
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
   }
   
}