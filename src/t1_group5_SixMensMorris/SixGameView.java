package t1_group5_SixMensMorris;

import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.*;

public class SixGameView extends JPanel implements MouseListener, ActionListener {
	private SixGame game; //this view's model
	private Rectangle outerSquare, innerSquare; //squares to be drawn on the board
	public PointLabel[][] pointLabels; //labels to show the points
	private JButton blueButton, redButton, doneButton; //buttons for the setup board
	private JButton saveButton; //button for saving game
	private JLabel playerOneDisks, playerTwoDisks; //labels for each player's disks
	private JTextArea setupRules, gameProgress, turnPlayer; //text area for setup rules, game progress, current player's turn
	private int currentColor; //current color disk being set on board (setup board)
	private boolean setupBoard; //if the current board is the setup board or not
	private JFrame oldFrame; //frame that contains this view
	private ArrayList<String> errorList; //errors that may occur during board setup
	public String boardType; //the type of board (twoPlayers, computer, setup) 
	
	//constructor for the view (for a new board or the board to setup a game)
	public SixGameView(SixGame game, String boardType){
		this.game = game; //sets the model
		this.boardType = boardType;
		
		this.addMouseListener(this); //adds a mouse listener
		
		//sets the look of this panel and creates the squares to be drawn on the board
		setLayout(null);
		this.setBackground(new Color(254, 242, 197));
		this.setBounds(0,0,600,600);
		this.outerSquare = new Rectangle(75, 75, 420, 420);
		this.innerSquare = new Rectangle(this.outerSquare);
		this.innerSquare.grow(-120, -120);
		
		this.currentColor = 0; //initial color is 0
		this.setupBoard = (boardType.equals("setup") ? true : false); //sets value for setupBoard 
		
		if (this.setupBoard == true){ //if a board is being setup
			//creates the appropriate buttons
			this.blueButton = new JButton();
			this.redButton = new JButton();
			this.doneButton = new JButton();
			
			//sets up the button for placing blue color disks
			this.blueButton.setBounds(570, 75, 100, 50);
			this.blueButton.setText("BLUE");
			this.blueButton.addActionListener(this);
			this.add(this.blueButton);
			
			//sets up the button for placing red color disks
			this.redButton.setBounds(570, 150, 100, 50);
			this.redButton.setText("RED");
			this.redButton.addActionListener(this);
			this.add(this.redButton);
			
			//sets up the button to finish board setup and start the preset game
			this.doneButton.setBounds(570, 350, 100, 50);
			this.doneButton.setText("DONE");
			this.doneButton.addActionListener(this);
			this.add(this.doneButton);
			
			//sets up the label for the setup board's rules
			this.setupRules = new JTextArea("Each color can have a minimum of 3 disks or a maximum "
					+ "of 6 disks on the board for your preset board to be valid. Having 3 disks in a row"
					+ " (for a mill) is disregarded in this setup.");
			this.setupRules.setBounds(20, 15, 490, 40);
			this.setupRules.setLineWrap(true);
			this.setupRules.setEditable(false);
			this.setupRules.setOpaque(false);
			this.setupRules.setFocusable(false);
			this.add(this.setupRules);
			
			//creates the point labels and calls the point label setup method
			this.pointLabels = new PointLabel[2][8];
			this.setPointLabels();
		}
		else{ //if a board isn't being setup (therefore a new game button was clicked earlier)
			//sets up the label for player one's disks
			this.playerOneDisks = new JLabel("Player One's Unplaced Disks:");
			this.playerOneDisks.setBounds(10, 25, 170, 20);
			this.add(this.playerOneDisks);
			
			//sets up the label for player two's disks
			this.playerTwoDisks = new JLabel((boardType.equals("computer") ? "Computer" : "Player Two") + "'s Unplaced Disks:");
			this.playerTwoDisks.setBounds(10, 535, 170, 20);
			this.add(this.playerTwoDisks);
			
			//sets up the button for saving the game
			this.saveButton = new JButton("SAVE");
			this.saveButton.setBounds(520, 100, 100, 50);
			this.saveButton.addActionListener(this);
			this.add(this.saveButton);
			
			//creates the point labels and calls the point label setup method
			this.pointLabels = new PointLabel[2][8];
			this.setPointLabels();
			
			//current player's turn textArea
			this.turnPlayer = new JTextArea((this.game.getCurrentPlayer().getValue() == 1 ? "Blue" : "Red") + "'s turn.");
			this.turnPlayer.setBounds(440, 15, 120, 20);
			this.turnPlayer.setLineWrap(true); //so line can be seen properly
			//text can't be clicked/focused on
			this.turnPlayer.setEditable(false);
			this.turnPlayer.setOpaque(false);
			this.turnPlayer.setFocusable(false);
			this.add(this.turnPlayer); //adds component to this panel
			
			//game progress textArea
			this.gameProgress = new JTextArea("Game In Progress.");
			this.gameProgress.setBounds(440, 35, 120, 20);
			this.gameProgress.setLineWrap(true); //so line can be seen properly
			//text can't be clicked/focused on
			this.gameProgress.setEditable(false);
			this.gameProgress.setOpaque(false);
			this.gameProgress.setFocusable(false);
			this.add(this.gameProgress); //adds component to this panel
		}
		
	}
	
	//constructor for the view (for a preset game board and load game)
	public SixGameView(SixGame game, PointLabel[][] pointLabels){
		this.game = game; //sets the model
		
		//sets the look of this panel and creates the squares to be drawn on the board
		this.addMouseListener(this);
		setLayout(null);
		this.setBackground(new Color(254, 242, 197));
		this.setBounds(0,0,600,600);
		this.outerSquare = new Rectangle(75, 75, 420, 420);
		this.innerSquare = new Rectangle(this.outerSquare);
		this.innerSquare.grow(-120, -120);
		
		this.currentColor = 0; //initial color is 0
		this.setupBoard = false; //sets the value of setupBoard to false (board isn't being setup)
		
		//sets up the label for player one's disks
		this.playerOneDisks = new JLabel("Player One's Unplaced Disks:");
		this.playerOneDisks.setBounds(10, 25, 170, 20);
		this.add(this.playerOneDisks);
		
		//sets up the label for player two's disks
		this.playerTwoDisks = new JLabel("Player Two's Unplaced Disks:");
		this.playerTwoDisks.setBounds(10, 535, 170, 20);
		this.add(this.playerTwoDisks);
		
		//sets up the button for saving the game
		this.saveButton = new JButton("SAVE");
		this.saveButton.setBounds(520, 100, 100, 50);
		this.saveButton.addActionListener(this);
		this.add(this.saveButton);
		
		//creates the point labels and calls the point label setup method with the passed in 2d array (preset points)
		this.pointLabels = new PointLabel[2][8];
		this.setPointLabels(pointLabels);
		
		//current player's turn textArea
		this.turnPlayer = new JTextArea((this.game.getCurrentPlayer().getValue() == 1 ? "Blue" : "Red") + "'s turn.");
		this.turnPlayer.setBounds(440, 15, 120, 20);
		this.turnPlayer.setLineWrap(true);
		this.turnPlayer.setEditable(false);
		this.turnPlayer.setOpaque(false);
		this.turnPlayer.setFocusable(false);
		this.add(this.turnPlayer);
		
		//game progress textArea
		this.gameProgress = new JTextArea("Game in Progress.");
		this.gameProgress.setBounds(440, 35, 120, 20);
		this.gameProgress.setLineWrap(true);
		this.gameProgress.setEditable(false);
		this.gameProgress.setOpaque(false);
		this.gameProgress.setFocusable(false);
		this.add(this.gameProgress);
	}
	
	//method that sets up a point's color (empty, P1's point, or P2's point)
	public void fillPoint(int square, int location, int player){
		this.getPointLabel(square, location).getPoint().setValue(player); //point is filled by player (sets color accordingly)
	}
	
	//creates the point labels and adds them to the view
	private void setPointLabels(){
		//for every possible point label position on the board
		for (int i = 0; i < this.pointLabels.length; i++){
			for (int j = 0; j < this.pointLabels[0].length; j++){
				this.pointLabels[i][j] = new PointLabel(this.game.getPoint(i, j)); //create a point label with its corresponding point object
				this.add(this.pointLabels[i][j]); //add the point label to the game board view
			}
		}
		this.setBoardPointLabels(); //call the method that sets each point's bounds
	}
	
	//overloaded method that creates the preset point labels and adds them to the view
	private void setPointLabels(PointLabel[][] pointLabels){
		//for every possible point label position on the board
		for (int i = 0; i < this.pointLabels.length; i++){
			for (int j = 0; j < this.pointLabels[0].length; j++){
				this.pointLabels[i][j] = new PointLabel(this.game.getPoint(i, j)); //create a point label with its corresponding point object
				this.pointLabels[i][j].getPoint().setValue(pointLabels[i][j].getPoint().getValue()); //set the value of the label's point to its appropriate match from the passed in pointLabels array
				if (pointLabels[i][j].getPoint().getValue() != 0){ //not empty point
					this.game.getPlayer(pointLabels[i][j].getPoint().getValue()).placePiece(); //place a piece on the board for the corresponding player
				}
				this.add(this.pointLabels[i][j]); //add the point label to the game board view
			}
		}
		//checks points for disabling appropriately
		for (int i = 0; i < this.pointLabels.length; i++){
			for (int j = 0; j < this.pointLabels[0].length; j++){
				//if both players have pieces to place, disable the point (if it is filled)
				if (this.game.getPlayer(1).getUnplaced() != 0 && this.game.getPlayer(2).getUnplaced() != 0 &&
						this.pointLabels[i][j].getPoint().getValue() != 0)
					this.pointLabels[i][j].getPoint().setEnabled(false);
			}
		}
		
		this.setBoardPointLabels(); //call the method that sets each point's bounds
	}
	
	private void setBoardPointLabels(){
		//sets all the PointLabels for the board's outer square
		this.getPointLabel(0, 0).setBounds((int)this.outerSquare.getMinX()-15, (int)this.outerSquare.getMinY()-15, 30, 30);
		this.getPointLabel(0, 1).setBounds((int)this.outerSquare.getMinX()-15, (int)this.outerSquare.getCenterY()-15, 30, 30);
		this.getPointLabel(0, 2).setBounds((int)this.outerSquare.getMinX()-15, (int)this.outerSquare.getMaxY()-15, 30, 30);
		this.getPointLabel(0, 3).setBounds((int)this.outerSquare.getCenterX()-15, (int)this.outerSquare.getMinY()-15, 30, 30);
		this.getPointLabel(0, 4).setBounds((int)this.outerSquare.getCenterX()-15, (int)this.outerSquare.getMaxY()-15, 30, 30);
		this.getPointLabel(0, 5).setBounds((int)this.outerSquare.getMaxX()-15, (int)this.outerSquare.getMinY()-15, 30, 30);
		this.getPointLabel(0, 6).setBounds((int)this.outerSquare.getMaxX()-15, (int)this.outerSquare.getCenterY()-15, 30, 30);
		this.getPointLabel(0, 7).setBounds((int)this.outerSquare.getMaxX()-15, (int)this.outerSquare.getMaxY()-15, 30, 30);
		
		//sets all the PointLabels for the board's inner square
		this.getPointLabel(1, 0).setBounds((int)this.innerSquare.getMinX()-15, (int)this.innerSquare.getMinY()-15, 30, 30);
		this.getPointLabel(1, 1).setBounds((int)this.innerSquare.getMinX()-15, (int)this.innerSquare.getCenterY()-15, 30, 30);
		this.getPointLabel(1, 2).setBounds((int)this.innerSquare.getMinX()-15, (int)this.innerSquare.getMaxY()-15, 30, 30);
		this.getPointLabel(1, 3).setBounds((int)this.innerSquare.getCenterX()-15, (int)this.innerSquare.getMinY()-15, 30, 30);
		this.getPointLabel(1, 4).setBounds((int)this.innerSquare.getCenterX()-15, (int)this.innerSquare.getMaxY()-15, 30, 30);
		this.getPointLabel(1, 5).setBounds((int)this.innerSquare.getMaxX()-15, (int)this.innerSquare.getMinY()-15, 30, 30);
		this.getPointLabel(1, 6).setBounds((int)this.innerSquare.getMaxX()-15, (int)this.innerSquare.getCenterY()-15, 30, 30);
		this.getPointLabel(1, 7).setBounds((int)this.innerSquare.getMaxX()-15, (int)this.innerSquare.getMaxY()-15, 30, 30);
	}
	
	//accessor for the point label at a given square and location
	private PointLabel getPointLabel(int square, int location){
		return this.pointLabels[square][location];
	}
	
	//method that sets every point as enabled on the board (can be clicked by user to conduct an action)
	public void setAllPointsEnabled(){
		for (int i = 0; i < this.pointLabels.length; i++)
			for (int j = 0; j < this.pointLabels[0].length; j++)
					this.pointLabels[i][j].getPoint().setEnabled(true);
	}
	
	//method that sets the appropriate pointLabels disabled on the board
	public void setPointsDisabled(int player){
		for (int i = 0; i < this.pointLabels.length; i++)
			for (int j = 0; j < this.pointLabels[0].length; j++)
				if (this.pointLabels[i][j].getPoint().getValue() == player)
					this.pointLabels[i][j].getPoint().setEnabled(false);
	}
	
	//deselect any points on the board that are selected
	public void deselectPoints(){
		for (int i = 0; i < this.pointLabels.length; i++)
			for (int j = 0; j < this.pointLabels[0].length; j++)
					this.pointLabels[i][j].setSelected(false);
	}
	
	//select a point based on its square and location on the board
	public void selectPoint(int square, int location){
		this.pointLabels[square][location].setSelected(true);
	}
	
	//accessor for PointLabel 2D array
	public PointLabel[][] getPointLabels(){
		return this.pointLabels;
	}
	
	//updates the view for ending the game
	public void endGame(int result){
		String message = "";
		
		if (result == 0)
			message = "Game was a draw!";
		else
			message = (result == 1 ? "Blue wins!" : "Red wins!"); //1 means P1(blue) was the winner, else 2 which means P2(red) was the winner
		
		//show the user a dialog box with the game result
		JOptionPane.showMessageDialog(this.oldFrame, message, "Game Result.", JOptionPane.PLAIN_MESSAGE);
		
		this.oldFrame.dispose();
		
		//sets up a frame to hold the main window for starting a game (new or preset)
		JFrame frame = new JFrame("Six Men's Morris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MainStartUp mainStart = new MainStartUp(); //creates a main start up window
		//sets up the details of the frame for this window
		frame.setContentPane(mainStart);
		frame.setPreferredSize(new Dimension(350, 370));
		frame.pack();
		frame.setVisible(true);
		mainStart.setOldFrame(frame); //sets mainStart's old frame as this one so that it can be closed in that class
	}
	
	//paint method to draw the appropriate graphics on the view
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		//sets a line color and stroke width
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(4));
		//draws the outer and inner squares onto the board
		g2.draw(outerSquare);
		g2.draw(innerSquare);
		
		//draws all the lines necessary between the squares onto the board
		Line2D line = new Line2D.Double(innerSquare.getCenterX(), innerSquare.getMaxY(), outerSquare.getCenterX(), outerSquare.getMaxY());
		g2.draw(line);
		line.setLine(innerSquare.getCenterX(), innerSquare.getMinY(), outerSquare.getCenterX(), outerSquare.getMinY());
		g2.draw(line);
		line.setLine(innerSquare.getMinX(), innerSquare.getCenterY(), outerSquare.getMinX(), outerSquare.getCenterY());
		g2.draw(line);
		line.setLine(innerSquare.getMaxX(), innerSquare.getCenterY(), outerSquare.getMaxX(), outerSquare.getCenterY());
		g2.draw(line);
		
		//fills all the initial black ovals for the board's outer square
		g.fillOval((int)outerSquare.getMinX()-8, (int)outerSquare.getMinY()-8, 16, 16);
		g.fillOval((int)outerSquare.getMinX()-8, (int)outerSquare.getCenterY()-8, 16, 16);
		g.fillOval((int)outerSquare.getMinX()-8, (int)outerSquare.getMaxY()-8, 16, 16);
		g.fillOval((int)outerSquare.getCenterX()-8, (int)outerSquare.getMinY()-8, 16, 16);
		g.fillOval((int)outerSquare.getCenterX()-8, (int)outerSquare.getMaxY()-8, 16, 16);
		g.fillOval((int)outerSquare.getMaxX()-8, (int)outerSquare.getMinY()-8, 16, 16);
		g.fillOval((int)outerSquare.getMaxX()-8, (int)outerSquare.getCenterY()-8, 16, 16);
		g.fillOval((int)outerSquare.getMaxX()-8, (int)outerSquare.getMaxY()-8, 16, 16);
		
		//fills all the initial black ovals for the board's inner square
		g.fillOval((int)innerSquare.getMinX()-8, (int)innerSquare.getMinY()-8, 16, 16);
		g.fillOval((int)innerSquare.getMinX()-8, (int)innerSquare.getCenterY()-8, 16, 16);
		g.fillOval((int)innerSquare.getMinX()-8, (int)innerSquare.getMaxY()-8, 16, 16);
		g.fillOval((int)innerSquare.getCenterX()-8, (int)innerSquare.getMinY()-8, 16, 16);
		g.fillOval((int)innerSquare.getCenterX()-8, (int)innerSquare.getMaxY()-8, 16, 16);
		g.fillOval((int)innerSquare.getMaxX()-8, (int)innerSquare.getMinY()-8, 16, 16);
		g.fillOval((int)innerSquare.getMaxX()-8, (int)innerSquare.getCenterY()-8, 16, 16);
		g.fillOval((int)innerSquare.getMaxX()-8, (int)innerSquare.getMaxY()-8, 16, 16);
		
		if (this.setupBoard == false){ //if the board is not being setup
			//draw player one's unplaced pieces (blue) on the top side of the board
			g.setColor(Color.BLUE);
			for (int i=0; i!=game.getUnplacedPieces(1); i++)
				g.fillOval(185 + i*35, 20, 30, 30);
			//draw player two's unplaced pieces (red) on the top side of the board
			g.setColor(Color.RED);
			for (int i=0; i!=game.getUnplacedPieces(2); i++)
				g.fillOval(185 + i*35, 530, 30, 30);
			
			//set current stage of game based on certain values in model
			this.turnPlayer.setText((this.game.getCurrentPlayer().getValue() == 1 ? "Blue" : "Red") + 
					(this.game.getMills() ? " Mills." : "'s Turn."));
		}
	}
	
	@Override //when the mouse is clicked
	public void mouseClicked (MouseEvent e){
		if (this.setupBoard == true){ //if a preset board is being setup
			for (int i = 0; i < this.pointLabels.length; i++){
				for (int j = 0; j < this.pointLabels[0].length; j++){
					if (this.pointLabels[i][j].getBounds().contains(e.getPoint())){
						//removes a piece on the board for the other player if their piece is replaced on setup board (point values match with player values)
						if (this.pointLabels[i][j].getPoint().getValue() == this.game.getPlayer(this.currentColor).getOtherPlayer()){
							this.game.getPlayer(this.game.getPlayer(this.currentColor).getOtherPlayer()).removeSetupPiece();
							this.pointLabels[i][j].getPoint().setValue(0); //resets the value for the point that was clicked
						}
						
						//places a piece on the board for the corresponding player if they have not placed a piece there already (i.e spot is empty)
						if (this.pointLabels[i][j].getPoint().getValue() == 0){
							this.pointLabels[i][j].getPoint().setValue(this.currentColor); //sets the value for the point that was clicked
							this.game.getPlayer(this.currentColor).placePiece();
						}
						this.pointLabels[i][j].repaint(); //repaints the point label
					}
				}
			}
		}
		else{ //new game running - this will control the game between the players
			
			//for every point on the board, if the mouse click occurred within the bounds of a point, then call the positionClicked method
			for (int i = 0; i < this.pointLabels.length; i++)
				for (int j = 0; j < this.pointLabels[0].length; j++)
					if (this.pointLabels[i][j].getBounds().contains(e.getPoint()))
						this.game.positionClicked(this.pointLabels[i][j].getPoint());
		}
		this.repaint(); //repaints this view
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// CURRENTLY UNUSED
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// CURRENTLY UNUSED
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// CURRENTLY UNUSED
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// CURRENTLY UNUSED
		
	}

	@Override //button was clicked
	public void actionPerformed(ActionEvent e) {
		boolean errorsExist = false;
		
		if (e.getSource().equals(this.blueButton)){ //if blue button was clicked during board setup stage
			this.currentColor = 1; //current color is 1 for blue (P1)
		}
		else if (e.getSource().equals(this.redButton)){ //if red button was clicked during board setup stage
			this.currentColor = 2; //current color is 2 for red (P2)
		}
		else if (e.getSource().equals(this.doneButton)){ //if done button was clicked during board setup stage
			this.errorList = new ArrayList<String>(); //initializes list of errors
			
			//points are replaced when colors are placed on them, so the error of having a disk underneath another is not possible
			
			//if red has an invalid number of pieces (less than 3 or more than 6) then add that to the list of errors
			if (this.game.getPlayer(1).getPlaced() < 3 || this.game.getPlayer(1).getPlaced() > 6){
				errorsExist = true;
				this.errorList.add("Invalid number of blue pieces.");
			}
			//if blue has an invalid number of pieces (less than 3 or more than 6) then add that to the list of errors
			if (this.game.getPlayer(2).getPlaced() < 3 || this.game.getPlayer(2).getPlaced() > 6){
				errorsExist = true;
				this.errorList.add("Invalid number of red pieces.");
			}
			this.findErrors(errorsExist); //calls the method that checks if there are any errors after the done button is clicked
		}
		else if (e.getSource().equals(this.saveButton)) { //if save button was clicked during gameplay
			this.game.getFileIO().save();
		}
		this.updateUI(); //updates GUI
		this.repaint(); //repaints the view
	}
	
	//method that resets the frame or creates a new one with a preset board based on the existence of errors
	public void findErrors(boolean errorsExist){
		if (errorsExist == false){ //if an error does not exist
			//hide the current frame and create a new one
			this.oldFrame.setVisible(false);
			JFrame newBoardFrame = new JFrame();
			newBoardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//creates the model and sets the view on the frame
			SixGame game = new SixGame(this.pointLabels); //passes in preset points for the preset board
			game.getView().setOldFrame(newBoardFrame);
			newBoardFrame.setContentPane(game.getView());
			newBoardFrame.setPreferredSize(new Dimension(650, 630));
			
	        //Display the window.
			newBoardFrame.pack();
	        newBoardFrame.setVisible(true);
		}
		else{ //error(s) exist
			String errors = "";
			for (String error : this.errorList){ //put all errors in the ArrayList together in a string
				errors += error + "\n";
			}
			//show the user a dialog box with the errors
			JOptionPane.showMessageDialog(this.oldFrame, errors, "Board Creation Error.", JOptionPane.ERROR_MESSAGE);
			
			//resets frame for creating a preset game
			this.oldFrame.dispose(); //deletes the current frame and creates a new empty board for setup
			JFrame frame = new JFrame("Six Men's Morris");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//creates the model and sets the view on the frame
			SixGame game = new SixGame("setup"); //parameter is (boolean setupBoard)
			game.getView().setOldFrame(frame);
	        frame.setContentPane(game.getView());
			frame.setPreferredSize(new Dimension(750, 630));
			
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
		}
	}
	
	//mutator for the frame that holds this window
	public void setOldFrame(JFrame oldFrame){
		this.oldFrame = oldFrame; //sets this window's frame
	}
}
