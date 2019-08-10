package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Musikverwaltung;
import playlist.Playlist;
import song.Song;

public class MainFrame extends JFrame implements ActionListener {

	private JList<String> list;
	private JButton pause;
	boolean clicked = false;
    public MainFrame() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        DefaultListModel<String> listModel2 = new DefaultListModel<>();
        for(int i = 0; i<Musikverwaltung.allSongs.size(); i++) {
        	listModel.addElement(Musikverwaltung.allSongs.get(i).getTitle());
        }
        list = new JList<>(listModel);
        add(list);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(200,200);
        this.setTitle("Musikverwaltung");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				for(int l = 0; l<Musikverwaltung.allSongs.size(); l++) {
					if(l != list.getSelectedIndex()) {
						Musikverwaltung.allSongs.get(l).playSound("pause");
					}
				}
				Musikverwaltung.allSongs.get(list.getSelectedIndex()).playSound("play");
			}
		});
        
        //pause
        JButton pause =new JButton("Pause");
        pause.setBounds(200,100,95,30);
        add(pause);
        setSize(600,600);  
        setLayout(null);
        setVisible(true);
        pause.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!clicked) {
					Musikverwaltung.allSongs.get(list.getSelectedIndex()).playSound("pause");
					clicked = true;
				}
				else {
					Musikverwaltung.allSongs.get(list.getSelectedIndex()).playSound("play");
					clicked = false;
				}
			}
		});
        
        //playAll
        JButton playAll =new JButton("play all");
        playAll.setBounds(380,100,95,30);
        add(playAll);
        setSize(600,600);  
        setLayout(null);
        setVisible(true);
        playAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Musikverwaltung.allSongs.get(list.getSelectedIndex()).playSound("stop");
			}
		});
        JTextField search = new JTextField("");
        search.setBounds(200,50,95,30);
        search.setVisible(true);
        add(search);
        JButton go =new JButton("Search");
        go.setBounds(290,50,95,30);
        go.setVisible(true);
        add(go);
        go.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = search.getText();
				if(!text.isEmpty()) {
					ArrayList<Song> results = searchSong(text);
					listModel.removeAllElements();
					for(int i = 0; i<results.size(); i++) {
						listModel.addElement(results.get(i).getTitle());
					}
					list.setModel(listModel);
				}
				else {
					listModel.removeAllElements();
					for(int i = 0; i<Musikverwaltung.allSongs.size(); i++) {
						listModel.addElement(Musikverwaltung.allSongs.get(i).getTitle());
					}
					list.setModel(listModel);
				}
			}
		});
        
        //playlist dropdown
        JComboBox<String> playlists = new JComboBox<String>();
        playlists.setVisible(true);
        playlists.setBounds(350,200,95,30);
        playlists.setEditable(true);
        playlists.addItem("all songs");
        add(playlists);
        for(int k = 0; k<Musikverwaltung.allPlaylists.size(); k++) {
        	playlists.addItem(Musikverwaltung.allPlaylists.get(k).getName());
        }
        
        playlists.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				listModel2.removeAllElements();
				for(int i = 0; i<Musikverwaltung.allPlaylists.size(); i++) {
					if(Musikverwaltung.allPlaylists.get(i).getName() == playlists.getSelectedItem().toString()) {
						BufferedReader br;
						try {
							br = new BufferedReader(new FileReader("./playlists/" + playlists.getSelectedItem().toString() + ".txt"));
							File f = new File("./playlists/" + playlists.getSelectedItem().toString() + ".txt");
							if(f.exists() && !f.isDirectory()) {
								    StringBuilder sb = new StringBuilder();
								    String line = br.readLine();
								    while (line != null) {
								        sb.append(line);
								        sb.append(System.lineSeparator());
								        String[] partsLine = line.split("-");
										String partLine1 = partsLine[0];
										listModel2.addElement(partLine1);
										list.setModel(listModel2);
								        line = br.readLine();
								    }
								    String everything = sb.toString();
									br.close();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				}
				if(playlists.getSelectedItem().toString().equals("all songs")) {
					list.setModel(listModel);
				}
			}
		});
        
        //add playlist button
        JButton addPlaylist =new JButton("Add playlist");
        final JFrame popup = new JFrame();
        JList popList = new JList<>(listModel);
        popup.pack();
        popup.setVisible(false);
        addPlaylist.setBounds(290,300,95,30);
        addPlaylist.setVisible(true);
        add(addPlaylist);
        addPlaylist.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog(popup, popList, "Name:");
				int[] indeces = popList.getSelectedIndices();
				Playlist playlist = new Playlist(name);
				for(int j = 0; j<indeces.length; j++) {
					playlist.addSong(Musikverwaltung.allSongs.get(j));
				}
				Musikverwaltung.allPlaylists.add(playlist);
				playlists.addItem(name);
				File f = new File("./playlists/" + name + ".txt");
				if(!f.exists()) {
					f.getParentFile().mkdirs(); 
					try {
						f.createNewFile();
						BufferedWriter writer = new BufferedWriter(new FileWriter("./playlists/" + name + ".txt"));
						for(int i = 0; i<indeces.length; i++) {
							writer.write(Musikverwaltung.allSongs.get(indeces[i]).getTitle() + "-" + Musikverwaltung.allSongs.get(indeces[i]).getInterpreter() + "-" + Musikverwaltung.allSongs.get(indeces[i]).getGenre() + "\n");
						}
						writer.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
    }

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}
	
	public ArrayList<Song> searchSong(String s) {
		ArrayList<Song> searchResults = new ArrayList<>();
		for(int i = 0; i<Musikverwaltung.allSongs.size(); i++) {
			Song song = Musikverwaltung.allSongs.get(i);
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
}
