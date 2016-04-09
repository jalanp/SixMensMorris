package t1_group5_SixMensMorris;

public class SixGame {
	private SixGameView gameView; //this game's view (display)
	private Point[][] points; //2d array that holds all the points on the board
	private Point currentPoint; //currently selected point
	private Player[] players; //0 is current player, 1 is P1, 2 is P2
	public enum Phase {PLACEMENT, MOVEMENT, REMOVAL}; //possible phases of the game 
	private Phase gamePhase; //current phase of the game
	private boolean mills; //if milling is occurring or not
	private FileIO fileIO; //file input/output class for saving and loading
	
	//constructor if a game is being loaded
	public SixGame(PointLabel[][] pointLabels, Phase phase, String boardType, int currentPlayer, Player playerOne, Player playerTwo){
		this.fileIO = new FileIO(this);
		this.setPhase(phase);
		this.setPoints(new Point[2][8]);
		this.initializePlayers((currentPlayer == playerOne.getValue() ? playerOne : playerTwo), playerOne, playerTwo);
		this.gameView = new SixGameView(this, pointLabels); // connect this model to the view
	}
	
	//constructor used to start a new game or to create the setup interface
	public SixGame(String boardType){
		this.fileIO = new FileIO(this);
		this.setPoints(new Point[2][8]); //create the 16 points for the board
		this.initializePlayers(6, (boardType.equals("computer") ? true : false)); //initialize players with 6 pieces
		this.setPhase(Phase.PLACEMENT); //initial phase is placement
		this.gameView = new SixGameView(this, boardType); //connect this model to the view
	}
	
	//constructor used to start a game from a preset board
	public SixGame(PointLabel[][] pointLabels){
		this.fileIO = new FileIO(this);
		this.initializePlayers(6, false); //initialize players with 6 pieces (false because preset means no computer)
		this.setPoints(new Point[2][8]); //create the 16 points for a board
		this.setPhase(Phase.PLACEMENT); //initial phase is placement
		this.gameView = new SixGameView(this, pointLabels); //connect this model to the view where points are already setup
	}
	
	//mutator method that sets the current phase of the game
	public void setPhase(Phase phase){
		this.gamePhase = phase;
	}
	
	//accessor method for the game's current phase
	public Phase getPhase(){
		return this.gamePhase;
	}
	
	//accessor method for the if milling is occurring
	public boolean getMills(){
		return this.mills;
	}
	
	//method that checks whether or not the game has been lost by a player and ends the game if so
	public void checkEnd(){
		int currentPlayerStuckPieces = 0;
		//if the current player has < 3 pieces, they lose the game (if none are unplaced)
		if (this.getCurrentPlayer().getPlaced() < 3 && this.getCurrentPlayer().getUnplaced() == 0)
			this.getView().endGame(this.getCurrentPlayer().getOtherPlayer()); //end game, pass in winning player as argument
		//if the other player has lost the game instead (by the same logic as above)
		else if(this.getPlayer(this.getCurrentPlayer().getOtherPlayer()).getPlaced() < 3 && this.getPlayer(this.getCurrentPlayer().getOtherPlayer()).getUnplaced() == 0)
			this.getView().endGame(this.getCurrentPlayer().getValue()); //end game, pass in winning player as argument
		
		//for every point on the board (empty or not)
		for (int i = 0; i < this.points.length; i++)
			for (int j = 0; j < this.points[0].length; j++){
				//if the current point's value is the same as the current player's value
				if (this.points[i][j].getValue() == this.getCurrentPlayer().getValue())
					//if the point is stuck, increment the current player's number of stuck pieces
					if (isStuck(this.points[i][j])){
						currentPlayerStuckPieces++;
					}
			}
		
		//if the curren't player's stuck pieces are equal to the number of total pieces they own (placed and unplaced), they cannot make a move
		if (currentPlayerStuckPieces == (this.getCurrentPlayer().getPlaced() + this.getCurrentPlayer().getUnplaced()))
			this.getView().endGame(this.getCurrentPlayer().getOtherPlayer()); //end game, pass in other player as winner
	}
	
	//check if the current point is blocked off by other points (cannot move in any direction), return true if so and false otherwise
	public boolean isStuck(Point point){
		int square = point.getSquare(); //initialize the point's square
		int otherSquare = (point.getSquare() == 0 ? 1 : 0); //initialize the other square corresponding to the point
		
		//returns true if all adjacent points to the current point are not empty
		switch(point.getLocation()){ //switch case statement that puts the stuck possibilities for each location
		case 0: //top left corner point
			return (this.getPoint(square, 1).getValue() != 0 && this.getPoint(square, 3).getValue() != 0);
		case 1: //top middle point
			return (this.getPoint(square, 0).getValue() != 0 && this.getPoint(square, 2).getValue() != 0 &&
					this.getPoint(otherSquare, 1).getValue() != 0);
		case 2: //top right corner point
			return (this.getPoint(square, 1).getValue() != 0 && this.getPoint(square, 4).getValue() != 0);
		case 3: //left middle point
			return (this.getPoint(square, 0).getValue() != 0 && this.getPoint(square, 5).getValue() != 0 &&
					this.getPoint(otherSquare, 3).getValue() != 0);
		case 4: //right middle point
			return (this.getPoint(square, 2).getValue() != 0 && this.getPoint(square, 7).getValue() != 0 &&
					this.getPoint(otherSquare, 4).getValue() != 0);
		case 5: //bottom left corner point
			return (this.getPoint(square, 3).getValue() != 0 && this.getPoint(square, 6).getValue() != 0);
		case 6: //bottom middle point
			return (this.getPoint(square, 5).getValue() != 0 && this.getPoint(square, 7).getValue() != 0 &&
					this.getPoint(otherSquare, 6).getValue() != 0);
		case 7: //bottom right corner point
			return (this.getPoint(square, 4).getValue() != 0 && this.getPoint(square, 6).getValue() != 0);
		default: //point isn't stuck, return false as default
			return false;
		}
	}
	
	//method that ends the turn for the current player and sets up the next player's turn
	public void endTurn(){
		this.setCurrentPlayer(this.getPlayer(this.getCurrentPlayer().getOtherPlayer())); //set the other player as the current player
		if (this.getCurrentPlayer().getUnplaced() != 0){ //if the current player has pieces to place
			this.setPhase(Phase.PLACEMENT); //placement phase
			this.getView().setAllPointsEnabled(); //initially enables all points
			//disable the points where pieces have been placed so far (so no new piece can be placed there)
			this.getView().setPointsDisabled(this.getCurrentPlayer().getValue());
			this.getView().setPointsDisabled(this.getCurrentPlayer().getOtherPlayer());
		}
		else{ //they have placed all pieces, so movement phase
			this.setPhase(Phase.MOVEMENT); //set the phase to movement
			
			//disable the other player's points
			this.getView().setAllPointsEnabled();
			this.getView().setPointsDisabled(this.getCurrentPlayer().getOtherPlayer()); //disables other player's points
		}
		//if a computer player is playing and it's computer's turn 
		if (this.getCurrentPlayer().getAI())
			this.computerMove(); //run the computer turn algorithm
	}
	
	//AI algorithms for computer's turns (placement, movement, and removal)
	public void computerMove(){
		int square, location; //declare square and location variables for point creation
		Point point, newPoint; //declare point (for placement and movement) and newPoint (for movement) variables
		
		switch (this.getPhase()){ //computer's move based on current phase of the game
		case PLACEMENT: //computer is placing a disk
			do{ //do loop
				square = (int)(Math.random()*2); //randomize the square (0 or 1)
				location = (int)(Math.random()*8); //randomize the location (from 0 to 7)
				point = this.getPoint(square, location); //get the corresponding point
			}while(point.getValue() != 0); //keep trying until an empty point is found
			
			this.placePiece(point); //place a disk for the computer at this point
			
			//if the computer has 3 disks in a row
			if(this.millPieces(point)){
				this.mills = true;
				this.setPhase(Phase.REMOVAL); //set to removal phase
				this.computerMove(); //conduct computer's turn once more (for removal)
				break; //break out of this case
			}
			this.endTurn(); //end turn (happens only if not milling)
			break;
		case MOVEMENT: //computer needs to move a disk
			do{ //first do loop
				do{ //second do loop
					square = (int)(Math.random()*2); //randomize the square (0 or 1)
					location = (int)(Math.random()*8); //randomize the location (from 0 to 7)
					point = this.getPoint(square, location); //get the corresponding point
					
					square = (int)(Math.random()*2); //randomize the square again
					location = (int)(Math.random()*8); //randomize the location again
					newPoint = this.getPoint(square, location); //get the new corresponding point
				}while(!(point.getValue() == 2 && newPoint.getValue() == 0)); //only break out of loop if point is owned by computer and newPoint is empty
			}while(!this.moveMade(point, newPoint)); //only break out of loop if a move could be made between the two found points
			
			//if the computer has 3 disks in a row
			if(this.millPieces(newPoint)){
				this.mills = true;
				this.setPhase(Phase.REMOVAL); //set to removal phase 
				this.computerMove(); //conduct computer's turn once more (for removal)
				break; //break out of this case
			}
			this.endTurn(); //end turn (happens only if not milling)
			break; //break out of case
		case REMOVAL: //computer needs to remove a disk (belonging to the player)
			do{
				square = (int)(Math.random()*2); //randomize the square
				location = (int)(Math.random()*8); //randomize the location
				point = this.getPoint(square, location); //get the corresponding point
			}while(point.getValue() != 1); //keep trying until one of player's disk is found
			
			//if the piece was removed successfully (it will be since the above loop ensures a correct point was picked)
			if (this.removePiece(point)){
				this.mills = false;
				this.endTurn(); //end the turn
			}
			break; //break out of case
		}
		this.getView().repaint(); //repaint the view
	}
	
	//method that runs the game (receives point clicked instructions from gameView)
	public void positionClicked(Point point){
		switch (this.getPhase()){ //do something based on the current phase of the game
		case PLACEMENT: //phase to place a disk
			if (point.getEnabled()){ //if the point is allowed to be clicked on, place the piece and end current player's turn
				this.placePiece(point);
				if(this.millPieces(point)){ //if a mill should occur, proceed to removal phase
					this.mills = true;
					this.setPhase(Phase.REMOVAL);
					this.getView().setAllPointsEnabled();
					this.getView().setPointsDisabled(this.getCurrentPlayer().getValue());
					break;
				}
				this.endTurn(); //end turn
			}
			break;
		case MOVEMENT: //phase to move a disk
			//no point currently selected, and they're trying to select a point no one owns, then don't do anything
			if (this.currentPoint == null && point.getValue() == 0){
				break;
			}
			//no point currently selected, then select the point (if possible) and disable any necessary points
			if (this.currentPoint == null && point.getValue() == this.getCurrentPlayer().getValue()){
				this.setCurrentPoint(point);
				this.getView().setPointsDisabled(this.getCurrentPlayer().getValue());
				this.getView().setPointsDisabled(this.getCurrentPlayer().getOtherPlayer());
				break;
			}
			if (point.getValue() == this.getCurrentPlayer().getValue()){ //player selected a point they already own
				//reset points (selected and disabled) so player can select a point again
				this.setCurrentPoint(null); 
				this.getView().setAllPointsEnabled();
				this.getView().setPointsDisabled(this.getCurrentPlayer().getOtherPlayer());
				break;
			}
			//if the point could be clicked (after an appropriate point was first selected)
			if (point.getEnabled()){
				if (moveMade(this.currentPoint, point)){ //point was moved successfully
					if (this.millPieces(point)){ //check for mills and remove a piece accordingly
						this.mills = true;
						this.setPhase(Phase.REMOVAL);
						this.getView().setAllPointsEnabled();
						this.getView().setPointsDisabled(this.getCurrentPlayer().getValue());
					}
					else //end turn if there was no mill during movement
						this.endTurn();
				}
				else{ //point was not moved
					this.getView().deselectPoints();
					this.getView().selectPoint(this.currentPoint.getSquare(), this.currentPoint.getLocation());
				}
			}
			break;
		case REMOVAL: //phase to remove a disk
			if (this.removePiece(point)){ //if the point that was selected was successfully removed
				this.mills = false; //set milling to false
				this.endTurn(); //end current player's turn
			}
			else //point wasn't removed, then deselect the point that was selected
				this.setCurrentPoint(null);
			break;
		}
		this.getView().repaint(); //repaint the view
		this.checkEnd(); //check if the game has been won by a player
	}
	
	//checks if there are 3 pieces in a row given a certain point, and returns the result (true if 3 in a row, false otherwise)
	public boolean millPieces(Point point){
		int square = point.getSquare();
		int value = point.getValue();
		switch(point.getLocation()){ //switch case statement that puts the mill possibilities for each location
		case 0: //top left corner point - if top row or left side is 3 in a row
			return ((this.getPoint(square, 1).getValue() == value && this.getPoint(square, 2).getValue() == value) ||
					this.getPoint(square, 3).getValue() == value && this.getPoint(square, 5).getValue() == value);
		case 1: //top middle point - if top row is 3 in a row
			return (this.getPoint(square, 0).getValue() == value && this.getPoint(square, 2).getValue() == value);
		case 2: //top right corner point - if top row or right side is 3 in a row
			return ((this.getPoint(square, 0).getValue() == value && this.getPoint(square, 1).getValue() == value) ||
					this.getPoint(square, 4).getValue() == value && this.getPoint(square, 7).getValue() == value);
		case 3: //left middle point - if left side is 3 in a row
			return (this.getPoint(square, 0).getValue() == value && this.getPoint(square, 5).getValue() == value);
		case 4: //right middle point - if right side is 3 in a row
			return (this.getPoint(square, 2).getValue() == value && this.getPoint(square, 7).getValue() == value);
		case 5: //bottom left corner point - if left side or bottom row is 3 in a row
			return ((this.getPoint(square, 6).getValue() == value && this.getPoint(square, 7).getValue() == value) ||
					this.getPoint(square, 0).getValue() == value && this.getPoint(square, 3).getValue() == value);
		case 6: //bottom middle point - if bottom row is 3 in a row
			return (this.getPoint(square, 5).getValue() == value && this.getPoint(square, 7).getValue() == value);
		case 7: //bottom right corner point - if bottom row or right side is 3 in a row
			return ((this.getPoint(square, 2).getValue() == value && this.getPoint(square, 4).getValue() == value) ||
					this.getPoint(square, 5).getValue() == value && this.getPoint(square, 6).getValue() == value);
		default: //a mill (3 in a row) didn't occur 
			return false;
		}
	}
	
	//makes a move (if possible) and returns the condition of the move (true if it was made, false if not)
	public boolean moveMade(Point selected, Point newLocation){
		//if point is moving to an appropriate new location
		if ((selected.sameSquare(newLocation) && selected.squareAdjacency(newLocation)) ||
				(!selected.sameSquare(newLocation) && selected.getLocation() == newLocation.getLocation()) &&
				(selected.getLocation() != 0 && selected.getLocation() != 2 && selected.getLocation() != 5 &&
				selected.getLocation() != 7)){
			//put disk onto new point
			newLocation.setValue(selected.getValue());
			this.getView().fillPoint(newLocation.getSquare(), newLocation.getLocation(), selected.getValue());
			//remove disk from old point  
			selected.setValue(0);
			this.getView().fillPoint(selected.getSquare(), selected.getLocation(), selected.getValue());
			this.setCurrentPoint(null); //deselect current point
			return true;
		}
		return false;
	}
	
	//removes a player's piece from the board (if possible) and returns the condition of the removal (true if removed, false if not)
	public boolean removePiece(Point selected){
		if (selected.getValue() == 0 || selected.getValue() == this.getCurrentPlayer().getValue())
			return false;
		selected.setValue(0); //set point to 0 (no player has a piece at this point)
		this.getView().fillPoint(selected.getSquare(), selected.getLocation(), 0); //set point label to unfilled
		this.getPlayer(this.getCurrentPlayer().getOtherPlayer()).removeGamePiece(); //remove a piece from this player
		return true; //piece was removed
	}
	
	public void placePiece(Point point){
		//set the point's value to the current player's value
		this.getView().fillPoint(point.getSquare(), point.getLocation(), this.getCurrentPlayer().getValue());
		this.getCurrentPlayer().placePiece(); //place a piece for the current player
	}
	
	//accessor for this game's view
	public SixGameView getView(){
		return this.gameView;
	}
	
	//accessor for current point selected
	public Point getCurrentPoint(){
		return this.currentPoint;
	}
	
	//mutator method that sets the current point selected
	public void setCurrentPoint(Point point){
		this.currentPoint = point;
		if (point == null) //want to clear selection
			this.getView().deselectPoints();
		else //select the appropriate point
			this.getView().selectPoint(point.getSquare(), point.getLocation());
	}
	
	//accessor for the current player on the board (stored in the 0th position of the players array)
	public Player getCurrentPlayer(){
		return this.players[0];
	}
	
	//mutator for setting the current player
	private void setCurrentPlayer(Player player){
		this.players[0] = player;
	}
	
	//accessor for the getting a player (1 for P1, 2 for P2)
	public Player getPlayer(int value){
		return this.players[value];
	}
	
	//initially sets up the points on the board (called in constructor)
	public void setPoints(Point[][] points){
		this.points = points;
		for (int i = 0; i < points.length; i++)
			for (int j = 0; j < points[i].length; j++)
				this.points[i][j] = new Point(i, j); //creates a point at the current position
	}
	
	//accessor for a point on the board (based on its square and location on the square)
	public Point getPoint(int square, int location){
		return this.points[square][location];
	}
	
	//gets players placed pieces
	public int getPlacedPieces(int player){
		return this.players[player].getPlaced();
	}
	
	//gets players unplaced pieces
	public int getUnplacedPieces(int player){
		return this.players[player].getUnplaced();
	}
	
	//initializes the players for the game with the appropriate number of pieces (called in constructor)
	private void initializePlayers(int pieces, boolean computer){
		this.players = new Player[3]; //0 for current player, 1 for P1, 2 for P2
		this.players[1] = new Player(1, pieces, false); //initialize player 1
		this.players[2] = new Player(2, pieces, computer); //initialize player 2 (computer is always P2, if player vs computer game)
		
		if (!computer) //if player vs player game
			this.setCurrentPlayer(this.players[(int)(Math.random()*2 + 1)]); //determines first player randomly
		else //if player vs computer game, then player 1 is first
			this.setCurrentPlayer(this.players[1]);
	}
	
	//initializes players using passed in values (for loading game)
	public void initializePlayers(Player currentPlayer, Player playerOne, Player playerTwo){
		this.players = new Player[3]; //0 for current player, 1 for P1, 2 for P2
		this.players[1] = new Player(playerOne.getValue(), playerOne.getPlaced(), playerOne.getUnplaced(), playerOne.getAI());
		this.players[2] = new Player(playerTwo.getValue(), playerTwo.getPlaced(), playerTwo.getUnplaced(), playerTwo.getAI());
		this.setCurrentPlayer(currentPlayer);
	}
	
	//accessor method for fileIO class
	public FileIO getFileIO(){
		return this.fileIO;
	}
}
