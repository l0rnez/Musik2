package playlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import song.Song;

public class Playlist {

	String name;
	String genre;
	ArrayList<Song> content = new ArrayList<>();
	String interpreter;

	public ArrayList<Song> getContent() {
		return content;
	}

	public void setContent(ArrayList<Song> content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Playlist(String name) {
		this.name = name;
	}

	public void addSong(Song... s) {
		for(int i = 0; i<s.length; i++) {
			content.add(s[i]);
		}
	}

	public ArrayList<Song> searchSong(String s) {
		ArrayList<Song> searchResults = new ArrayList<>();
		for(int i = 0; i<content.size(); i++) {
			Song song = content.get(i);
			int l = 0;
			if(s.length() > song.getTitle().length()) {
				l = song.getTitle().length();
			}
			else {
				l = s.length(); 
			}
			for(int j = 0; j<l; j++) {
				char c = song.getTitle().charAt(j);
				char b = song.getInterpreter().charAt(j);
				char d = s.charAt(j);
				if(c == d && !searchResults.contains(song)) {
					searchResults.add(song);
				}
				if(b == d && !searchResults.contains(song)) {
					searchResults.add(song);
				}
				if(b != d && c != d) {
					searchResults.remove(song);
					break;
				}
			}
		}
		return searchResults;
	}
	
	public void sort(String s) {
		if(s == "a") {
//			Collections.sort(content);
		}
	}
	
	public void TsortAbc()
	{
		Collections.sort(content,new Comparator<Song>() 
		{
			@Override
			public int compare(Song s1, Song s2) 
			{
				int comparison=0;
				comparison=s1.getTitle().compareTo(s2.getTitle());
				return comparison;
			}
			
		});
	}

	public void IsortAbc()
	{
		Collections.sort(content,new Comparator<Song>() 
		{
			@Override
			public int compare(Song s1, Song s2) 
			{
				int comparison=0;
				comparison=s1.getInterpreter().compareTo(s2.getInterpreter());
				return comparison;
			}
			
		});
	}
	
	public void GsortAbc()
	{
		Collections.sort(content,new Comparator<Song>() 
		{
			@Override
			public int compare(Song s1, Song s2) 
			{
				int comparison=0;
				comparison=s1.getGenre().compareTo(s2.getGenre());
				return comparison;
			}	
		});
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(name + "\n");
		for(int i = 0; i<content.size(); i++) {
			sb.append(content.get(i).getInterpreter() + " - " + content.get(i).getTitle() + "\n");
		}
		return "Playlist: " + sb;
	}
	
	
	
	public ArrayList<Song> createPlaylistInterpreter(String searchinterpreter) {
		ArrayList<Song> searchResult = new ArrayList<Song>();
		
		//Content wird durchsucht nach Interpreten, Songs von diesem Interpreten landen in searchResult
		for(int i = 0; i < content.size(); i++) {
			Song song = content.get(i);
			String interpreter = content.get(i).getInterpreter();
			
			if(searchinterpreter == interpreter) {
				searchResult.add(song);
			}	
		}
		
		
		
		return null;  //noch ändern
	}
	
	public ArrayList<Song> createPlaylistGenre(String searchgenre) {
		ArrayList<Song> searchResult = new ArrayList<Song>();
		
		//Content wird durchsucht nach Interpreten, Songs von diesem Interpreten landen in searchResult
		for(int i = 0; i < content.size(); i++) {
			Song song = content.get(i);
			String Genre = content.get(i).getGenre();
			
			if(searchgenre == Genre) {
				searchResult.add(song);
			}	
		}
		
		
		
		return null;  //noch ändern
	}
	
	//packt alle Interpreten in eine Liste
		public ArrayList<String> getAllInterpreter() {
				
			ArrayList<String> liste = new ArrayList<String>();
				
			for(int i = 0; i< content.size(); i++) {
				liste.add(i, content.get(i).getInterpreter());
			}
			return liste;
		}
}
