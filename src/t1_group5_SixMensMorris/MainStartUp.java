package t1_group5_SixMensMorris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MainStartUp extends JPanel implements ActionListener{
	private JButton newGameButton, presetGameButton; //buttons for this window
	private JFrame oldFrame; //frame that contains the window
	
	//constructor for this window
	public MainStartUp(){
		//sets the look of the window
		setLayout(null);
		this.setBackground(new Color(254, 242, 197));
		this.setBounds(0,0,600,600);
		
		//sets up the two buttons for this window and adds action listeners for when they're clicked
		this.newGameButton = new JButton("New Game");
		this.newGameButton.setBounds(75, 30, 200, 50);
		this.newGameButton.addActionListener(this);
		this.presetGameButton = new JButton("Preset Game");
		this.presetGameButton.setBounds(75, 120, 200, 50);
		this.presetGameButton.addActionListener(this);
		
		//adds the two button components to this window
		this.add(this.newGameButton);
		this.add(this.presetGameButton);
	}

	@Override //when a button is clicked
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.newGameButton)){ //new game was clicked (show the board for a new game)
			this.oldFrame.setVisible(false); //to hide this window
			//sets up the frame for the game window
			JFrame frame = new JFrame("Six Men's Morris");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//creates the model and sets the view on the frame
			SixGame game = new SixGame(false); //parameter is (boolean setupBoard)
			game.getView().setOldFrame(frame);
	        frame.setContentPane(game.getView());
			frame.setPreferredSize(new Dimension(650, 630));
			
	        //displays the window
	        frame.pack();
	        frame.setVisible(true);
		}
		else{ //preset game was clicked (show the board to setup a game)
			this.oldFrame.setVisible(false); //to hide this window
			//sets up the frame for the game window
			JFrame frame = new JFrame("Six Men's Morris");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//creates the model and sets the view on the frame
			SixGame game = new SixGame(true); //parameter is (boolean setupBoard)
			game.getView().setOldFrame(frame);
	        frame.setContentPane(game.getView());
			frame.setPreferredSize(new Dimension(750, 630));
			
	        //displays the window
	        frame.pack();
	        frame.setVisible(true);
		}
	}
	
	//mutator for the frame that holds this window
	public void setOldFrame (JFrame oldFrame){
		this.oldFrame = oldFrame; //sets this window's frame
	}
}
