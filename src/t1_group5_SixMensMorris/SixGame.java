package t1_group5_SixMensMorris;

public class SixGame {
	private SixGameView gameView; //this game's view (display)
	private Point[][] points; //2d array that holds all the points on the board
	private Point currentPoint; //currently selected point
	private Player[] players; //0 is current player, 1 is P1, 2 is P2
	private enum Phase {PLACEMENT, MOVEMENT, REMOVAL};
	private Phase gamePhase;
	private boolean mills;
	
	//constructor used to start a new game or to create the setup interface
	public SixGame(boolean setupBoard){
		this.setPoints(new Point[2][8]); //create the 16 points for the board
		this.initializePlayers(6); //initialize players with 6 pieces
		this.setPhase(Phase.PLACEMENT); //initial phase is placement
		this.gameView = new SixGameView(this, setupBoard); //connect this model to the view
	}
	
	//constructor used to start a game from a preset board
	public SixGame(PointLabel[][] pointLabels){
		this.initializePlayers(6); //initialize players with 6 pieces
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
	
	public void endTurn(){
		this.setCurrentPlayer(this.getPlayer(this.getCurrentPlayer().getOtherPlayer())); //set the other player as the current player
		if (this.getCurrentPlayer().getUnplaced() != 0){ //if the current player has pieces to place
			this.setPhase(Phase.PLACEMENT); //placement phase
			this.getView().setAllPointsEnabled(); //initially enables all points
			//disable the points where pieces have been placed so far (so no new piece can be placed there)
			this.getView().setPointsDisabled(this.getCurrentPlayer().getValue());
			this.getView().setPointsDisabled(this.getCurrentPlayer().getOtherPlayer());
		}
		else{
			this.setPhase(Phase.MOVEMENT); //movement phase
			this.getView().setAllPointsEnabled();
			this.getView().setPointsDisabled(this.getCurrentPlayer().getOtherPlayer()); //disables other player's points
		}
	}
	
	//method that runs the game (receives point clicked instructions from gameView)
	public void positionClicked(Point point){
		switch (this.getPhase()){ //do something based on the current phase of the game
		case PLACEMENT:
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
		case MOVEMENT:
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
		case REMOVAL:
			if (this.removePiece(point)){ //if the point that was selected was successfully removed
				this.mills = false; //set milling to false
				this.endTurn(); //end current player's turn
			}
			else //point wasn't removed, then deselect the point that was selected
				this.setCurrentPoint(null);
			break;
		}
		this.getView().repaint();
		//if a player has < 3 pieces, they lose the game (if none are unplaced)
		if (this.getCurrentPlayer().getPlaced() < 3 && this.getCurrentPlayer().getUnplaced() == 0){
			this.getView().endGame(this.getCurrentPlayer().getOtherPlayer()); //end game, pass in winning player as argument
		}
	}
	
	//checks if there are 3 pieces in a row given a certain point, and returns the result (true if 3 in a row, false otherwise)
	public boolean millPieces(Point point){
		int square = point.getSquare();
		int value = point.getValue();
		switch(point.getLocation()){ //switch case statement that puts the mill possibilities for each location
		case 0:
			return ((this.getPoint(square, 1).getValue() == value && this.getPoint(square, 2).getValue() == value) ||
					this.getPoint(square, 3).getValue() == value && this.getPoint(square, 5).getValue() == value);
		case 1:
			return (this.getPoint(square, 0).getValue() == value && this.getPoint(square, 2).getValue() == value);
		case 2:
			return ((this.getPoint(square, 0).getValue() == value && this.getPoint(square, 1).getValue() == value) ||
					this.getPoint(square, 4).getValue() == value && this.getPoint(square, 7).getValue() == value);
		case 3:
			return (this.getPoint(square, 0).getValue() == value && this.getPoint(square, 5).getValue() == value);
		case 4:
			return (this.getPoint(square, 2).getValue() == value && this.getPoint(square, 7).getValue() == value);
		case 5:
			return ((this.getPoint(square, 6).getValue() == value && this.getPoint(square, 7).getValue() == value) ||
					this.getPoint(square, 0).getValue() == value && this.getPoint(square, 3).getValue() == value);
		case 6:
			return (this.getPoint(square, 5).getValue() == value && this.getPoint(square, 7).getValue() == value);
		case 7:
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
		return players[value];
	}
	
	//initially sets up the points on the board (called in constructor)
	private void setPoints(Point[][] points){
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
		return players[player].getPlaced();
	}
	
	//gets players unplaced pieces
	public int getUnplacedPieces(int player){
		return players[player].getUnplaced();
	}
	
	//initializes the players for the game with the appropriate number of pieces (called in constructor)
	private void initializePlayers(int pieces){
		players = new Player[3]; //0 for current player, 1 for P1, 2 for P2
		players[1] = new Player(1, pieces); //initialize player 1
		players[2] = new Player(2, pieces); //initialize player 2
		this.setCurrentPlayer(players[(int)(Math.random()*2 + 1)]); //determines first player randomly
	}
}
