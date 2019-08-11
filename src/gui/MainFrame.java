package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Musikverwaltung;
import playlist.Playlist;
import song.Song;

public class MainFrame extends JFrame implements ActionListener {

	private JList<String> list;
	boolean clicked = false;
    public MainFrame() {
    	setTitle("Musikverwaltung");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.getHSBColor((float) 0.55, (float) 0.7, (float) 0.8));
        setLayout(null);
        setVisible(true);
        setSize(600, 320);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        DefaultListModel<String> listModel2 = new DefaultListModel<>();
        for(int i = 0; i<Musikverwaltung.allSongs.size(); i++) {
        	listModel.addElement(Musikverwaltung.allSongs.get(i).getTitle());
        }
        list = new JList<>(listModel);
        add(list);
        list.setSize(200,200);
        list.setVisible(true);
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
//        list.setCellRenderer(new DefaultListCellRenderer() {
//
//            @Override
//            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//            	setText(((Component) value).getName());
//            	if (isSelected)
//
//                {
//                   setBackground(list.getSelectionBackground());
//                   setForeground(list.getSelectionForeground());
//                }
//            	else
//                {
//                   setBackground(list.getBackground());
//                   setForeground(list.getForeground());
//                }
//            	return this;
//            }
//        });
        
        //Font
		Font f1 = new Font(Font.SERIF, Font.PLAIN, 15);

        
        //pause
        JButton pause =new JButton("Pause/Play");
        pause.setBounds(0,205,200,30);
        pause.setFont(f1);
        add(pause);

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
        JButton playAll =new JButton("Play All");
        playAll.setBounds(0,240,200,30);
        playAll.setFont(f1);
        add(playAll);
        
        playAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Song> songsToPlay = new ArrayList<>();
				for(int i = 0; i<list.getModel().getSize(); i++) {
					String songname = list.getModel().getElementAt(i).toString();
					for(int j = 0; j<Musikverwaltung.allSongs.size(); j++) {
						if(Musikverwaltung.allSongs.get(i).equals(songname)) {
							songsToPlay.add(Musikverwaltung.allSongs.get(i));
						}
					}
				}
				for(int k = 0; k<songsToPlay.size(); k++) {
					System.out.println(songsToPlay.get(k).getTitle());
					songsToPlay.get(k).playSound("play");
					try {
						Thread.sleep((long) (songsToPlay.get(k).getSongLength()*1000));
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
        
        //searchbar
        JTextField search = new JTextField("");
        add(search);
        search.setFont(f1);
        search.setBounds(200,10,150,30);
        search.setVisible(true);
        
        //searchbutton
        JButton go =new JButton("Search");
        add(go);
        go.setBounds(350,10,95,30);
        go.setVisible(true);
        go.setFont(f1);
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
        JComboBox<String> interpreters = new JComboBox<String>();
        JComboBox<String> genres = new JComboBox<String>();
        add(playlists);
        add(interpreters);
        add(genres);
        
        //all playlists
        playlists.setVisible(true);
        playlists.setBounds(200,50,150,30);
        playlists.setEditable(true);
        playlists.addItem("Playlists");
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
									br.close();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				}
				if(playlists.getSelectedItem().toString().equals("Playlists")) {
					list.setModel(listModel);
				}
				interpreters.setSelectedItem("All Interpreters");
				genres.setSelectedItem("All Genres");
			}
		});
        
        //sort interpreter
        interpreters.setBounds(200,85,150,30);
        interpreters.setEditable(true);
        interpreters.addItem("All Interpreters");
        String interpreter = Musikverwaltung.allSongs.get(0).getInterpreter();
        for(int i = 0; i<Musikverwaltung.allSongs.size(); i++) {
			if(!Musikverwaltung.allSongs.get(i).getInterpreter().equals(interpreter)) {
				interpreters.addItem(interpreter);
				interpreter = Musikverwaltung.allSongs.get(i).getInterpreter();
			}
		}
        interpreters.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				listModel2.removeAllElements();
				for(int i = 0; i<Musikverwaltung.allSongs.size(); i++) {
					if(interpreters.getSelectedItem().equals(Musikverwaltung.allSongs.get(i).getInterpreter())) {
						listModel2.addElement(Musikverwaltung.allSongs.get(i).getTitle());
						list.setModel(listModel2);
					}
				}
				if(interpreters.getSelectedItem().toString().equals("All Interpreters")) {
					list.setModel(listModel);
				}
				playlists.setSelectedItem("Playlists");
				genres.setSelectedItem("All Genres");
			}
		});
        
        //sort genre
        genres.setBounds(200,120,150,30);
        genres.setEditable(true);
        genres.addItem("All Genres");
        String genre = Musikverwaltung.allSongs.get(0).getGenre();
        for(int i = 0; i<Musikverwaltung.allSongs.size(); i++) {
			if(!Musikverwaltung.allSongs.get(i).getGenre().equals(genre)) {
				genres.addItem(genre);
				genre = Musikverwaltung.allSongs.get(i).getGenre();
			}
		}
        genres.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				listModel2.removeAllElements();
				for(int i = 0; i<Musikverwaltung.allSongs.size(); i++) {
					if(genres.getSelectedItem().equals(Musikverwaltung.allSongs.get(i).getGenre())) {
						listModel2.addElement(Musikverwaltung.allSongs.get(i).getTitle());
						list.setModel(listModel2);
					}
				}
				if(genres.getSelectedItem().toString().equals("All Genres")) {
					list.setModel(listModel);
				}
				playlists.setSelectedItem("Playlists");
				interpreters.setSelectedItem("All Interpreters");
			}
		});
        
        //add playlist button
        JButton addPlaylist =new JButton("Add");
        final JFrame popup = new JFrame();
        JList popList = new JList<>(listModel);
        add(addPlaylist);
        popup.pack();
        popup.setVisible(false);
        addPlaylist.setBounds(350,50,95,30);
        addPlaylist.setFont(f1);
        addPlaylist.setVisible(true);
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
