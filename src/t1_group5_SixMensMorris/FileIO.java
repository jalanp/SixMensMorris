package t1_group5_SixMensMorris;

import java.awt.Dimension;
import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import t1_group5_SixMensMorris.SixGame.*;

public class FileIO {
	private JPanel fileContents; //used to load the file chooser box
	private JFileChooser fileChooser; //used to allow player to save/load a certain file
	private FileNameExtensionFilter filter; //filter for files to be saved/loaded
	private File myFile; //file to be saved in or loaded from
	
	private SixGame gameModel; //game model attached to this class
	
	private JFrame frame; //when loading a file
	
	//default constructor
	public FileIO(JFrame frame){
		super();
		this.fileContents = new JPanel();
		this.fileChooser = new JFileChooser();
		this.filter = new FileNameExtensionFilter("SAV files","sav"); //"sav" file extension filter
		this.frame = frame;
	}
	
	//constructor for this class that creates all the appropriate objects and links the model
	public FileIO (SixGame gameModel){
		super();
		this.gameModel = gameModel; //links game model to this class
		this.fileContents = new JPanel();
		this.fileChooser = new JFileChooser();
		this.filter = new FileNameExtensionFilter("SAV files","sav"); //"sav" file extension filter
	}
	
	//method that opens the appropriate dialog box and returns the selected file based on the event occurring (save or load)
	public File getFile(String type){
		fileChooser.setFileFilter(this.filter); //sets the filter for the FileChooser object
        int fileSelected;
        
        if (type.equals("save")) { //saving a file opens corresponding dialog box
             fileSelected = this.fileChooser.showSaveDialog(this.fileContents);
        }
        else { //loading a file opens corresponding dialog box
             fileSelected = this.fileChooser.showOpenDialog(this.fileContents);
        }
        
        //if the approve option is chosen in the dialog box
        if(fileSelected == JFileChooser.APPROVE_OPTION) {
             this.myFile = (this.fileChooser.getSelectedFile());
        }
        
        return this.myFile; //return the appropriate file
	}
	
	//method that exports the data from the program into the ".sav" file
	public void save(){
		try {
			//save file is created, and the appropriate extension is appended
			FileOutputStream saveFile = new FileOutputStream(this.getFile("save")+".sav");
			
			//object output stream to put objects into save file
			ObjectOutputStream saveObj = new ObjectOutputStream(saveFile);
			
			System.out.println(this.gameModel.getView().boardType);
			System.out.println(this.gameModel.getPlayer(1).getPlaced());
			
			//all necessary objects are saved
			saveObj.writeObject(this.gameModel.getView().boardType); //the type of board
			saveObj.writeObject(this.gameModel.getView().getPointLabels()); //point labels (which have point objects)
			saveObj.writeObject(this.gameModel.getPhase()); //game phase
			saveObj.writeObject(this.gameModel.getCurrentPlayer().getValue());
			saveObj.writeObject(this.gameModel.getPlayer(1));
			saveObj.writeObject(this.gameModel.getPlayer(2));
			saveObj.close(); //close the saving file
		} catch (Exception e) { //in case of any error
			e.printStackTrace();
		}
	}
	
	//method that imports the data from the ".sav" file into the program and returns the created game model
	public void load(){
		SixGame game;
		PointLabel[][] pointLabels;
		Phase phase;
		int currentPlayer;
		Player playerOne, playerTwo;
		String boardType;
		
		try {
			//load file into an input stream
			FileInputStream loadFile = new FileInputStream(this.getFile("load"));
			
			//object input stream to retrieve objects from the selected file
			ObjectInputStream loadObj = new ObjectInputStream(loadFile);
			
			boardType = (String)loadObj.readObject();
			pointLabels = (PointLabel[][])loadObj.readObject();
			phase = (Phase)loadObj.readObject();
			currentPlayer = (int)loadObj.readObject();
			playerOne = (Player)loadObj.readObject();
			playerTwo = (Player)loadObj.readObject();
			loadObj.close(); //close the loading file

			//creates a new game with the objects read from the file
			game = new SixGame(pointLabels, phase, boardType, currentPlayer, playerOne, playerTwo);
			
			//sets the view for the the frame and connects them
			game.getView().setOldFrame(this.frame);
			this.frame.setContentPane(game.getView());
			this.frame.setPreferredSize(new Dimension(650, 630));
			
			//displays the window
			this.frame.pack();
			this.frame.setVisible(true);
			
			game.getView().repaint();
			game.getView().updateUI();
			
		} catch (Exception e) { //in case of any error
			e.printStackTrace();
		}
	}
}
