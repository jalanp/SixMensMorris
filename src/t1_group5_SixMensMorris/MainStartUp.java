package t1_group5_SixMensMorris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MainStartUp extends JPanel implements ActionListener{
	private JButton newGameButton, presetGameButton, newAIGameButton; //buttons for this window
	private JButton loadGameButton; //button for loading a game from a file
	private JFrame oldFrame; //frame that contains the window
	
	//constructor for this window
	public MainStartUp(){
		//sets the look of the window
		setLayout(null);
		this.setBackground(new Color(254, 242, 197));
		this.setBounds(0,0,600,600);
		
		//sets up the three buttons for this window and adds action listeners for when they're clicked
		//new two player game
		this.newGameButton = new JButton("New Game (2 Player)");
		this.newGameButton.setBounds(75, 30, 200, 50);
		this.newGameButton.addActionListener(this);
		
		//new player vs AI game
		this.newAIGameButton = new JButton("New Game (vs. AI)");
		this.newAIGameButton.setBounds(75, 100, 200, 50);
		this.newAIGameButton.addActionListener(this);
		
		//preset game
		this.presetGameButton = new JButton("Preset Game");
		this.presetGameButton.setBounds(75, 170, 200, 50);
		this.presetGameButton.addActionListener(this);
		
		//load game
		this.loadGameButton = new JButton("Load Game");
		this.loadGameButton.setBounds(75, 240, 200, 50);
		this.loadGameButton.addActionListener(this);
		
		//adds the two button components to this window
		this.add(this.newGameButton);
		this.add(this.newAIGameButton);
		this.add(this.presetGameButton);
		this.add(this.loadGameButton);
	}

	@Override //when a button is clicked
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.newGameButton)){ //new 2 player game was clicked (show the board for a new game)
			this.oldFrame.setVisible(false); //to hide this window
			//sets up the frame for the game window
			JFrame frame = new JFrame("Six Men's Morris");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//creates the model and sets the view on the frame
			SixGame game = new SixGame("twoPlayers"); //parameter is (String boardType)
			game.getView().setOldFrame(frame);
	        frame.setContentPane(game.getView());
			frame.setPreferredSize(new Dimension(650, 630));
			
	        //displays the window
	        frame.pack();
	        frame.setVisible(true);
		}
		else if (e.getSource().equals(this.newAIGameButton)){ //new AI game was clicked
			this.oldFrame.setVisible(false); //to hide this window
			//sets up the frame for the game window
			JFrame frame = new JFrame("Six Men's Morris");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//creates the model and sets the view on the frame
			SixGame game = new SixGame("computer"); //parameter is (String boardType)
			game.getView().setOldFrame(frame);
	        frame.setContentPane(game.getView());
			frame.setPreferredSize(new Dimension(650, 630));
			
	        //displays the window
	        frame.pack();
	        frame.setVisible(true);
		}
		else if (e.getSource().equals(this.presetGameButton)){ //preset game was clicked (show the board to setup a game)
			this.oldFrame.setVisible(false); //to hide this window
			//sets up the frame for the game window
			JFrame frame = new JFrame("Six Men's Morris");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//creates the model and sets the view on the frame
			SixGame game = new SixGame("setup"); //parameter is (String boardType)
			game.getView().setOldFrame(frame);
	        frame.setContentPane(game.getView());
			frame.setPreferredSize(new Dimension(750, 630));
			
	        //displays the window
	        frame.pack();
	        frame.setVisible(true);
		}
		else if (e.getSource().equals(this.loadGameButton)) { //load game was clicked
			this.oldFrame.setVisible(false); //to hide this window
			
			//sets up the frame for the game window
			JFrame frame = new JFrame("Six Men's Morris");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//creates the model and sets the view on the frame
			FileIO loadGame = new FileIO(frame);
			loadGame.load();
		}
	}
	
	//mutator for the frame that holds this window
	public void setOldFrame (JFrame oldFrame){
		this.oldFrame = oldFrame; //sets this window's frame
	}
}
