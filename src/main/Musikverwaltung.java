package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import gui.MainFrame;
import playlist.Playlist;
import song.Song;

public class Musikverwaltung{

	public static ArrayList<Song> allSongs = new ArrayList<>();
	public static ArrayList<Playlist> allPlaylists = new ArrayList<>();
	public static void main(String[] args) throws IOException {
		
		//read songs
		File folder = new File("./soundsource/");
		File[] listOfFiles = folder.listFiles();
		for(int i = 0; i< listOfFiles.length; i++) {
			String string = listOfFiles[i].getName();
			String[] parts = string.split("-");
			String part1 = parts[0];
			String part2 = parts[1];
			String part3 = parts[2];
			String[] parts2 = part3.split("\\.");
			part3 = parts2[0];
			Song song = new Song(part1, part2, part3);
			allSongs.add(song);
		}

		//read playlists
		File folder2 = new File("./playlists/");
		File[] listOfFiles2 = folder2.listFiles();
		for(int i = 0; i< listOfFiles2.length; i++) {
			String string = listOfFiles2[i].getName();
			String[] parts = string.split("\\.");
			String part1 = parts[0];
			Playlist playlist = new Playlist(part1);
			allPlaylists.add(playlist);
			BufferedReader br = new BufferedReader(new FileReader("./playlists/" + part1 + ".txt"));
			File f = new File("./playlists/" + part1 + ".txt");
			if(f.exists() && !f.isDirectory()) {
				try {
				    StringBuilder sb = new StringBuilder();
				    String line = br.readLine();
				    while (line != null) {
				        sb.append(line);
				        sb.append(System.lineSeparator());
				        String[] partsLine = line.split("-");
						String partLine1 = partsLine[0];
				        for(int u = 0; u<allSongs.size(); u++) {
				        	if(allSongs.get(u).getTitle().equals(partLine1)) {
				        		playlist.addSong(allSongs.get(u));
				        	}
				        }
				        line = br.readLine();
				    }
				} finally {
				    br.close();
				}
			}
		}
		
		//gui
		MainFrame frame = new MainFrame();
	}
}
