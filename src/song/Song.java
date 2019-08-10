package song;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Song {

	String title;
	String interpreter;
	String genre;
	File file;
	AudioInputStream stream;
	Clip clip;

	public Song(String title, String interpreter, String genre) {
		this.title = title;
		this.interpreter = interpreter;
		this.genre = genre;
		file = new File("./soundsource/" + title + "-" + interpreter + "-" + genre + ".wav");
		try {
			stream = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {
			clip.open(stream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}
	
	public void playSound(String s) {
		try {
//		      clip.start();
		      if(s == "stop") {
		    	  clip.close();
		      }
		      if(s == "pause") {
		    	  clip.stop();
		      }
		      if(s == "play") {
		    	  clip.start();
		      }
		    } catch (Exception ex) {
		      System.out.println(ex.getMessage());
		    }
	}
}
