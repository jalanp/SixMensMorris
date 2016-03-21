package t1_group5_SixMensMorris;

public class Player {
	private int value; //P1 or P2
	private int unplaced; //pieces left to place
	private int placed; //pieces already placed on board
	
	//constructor for player (used when initializing players for a game)
	public Player (int value, int pieces){
		//initializes variables based on passed in variables
		this.value = value;
		this.unplaced = pieces;
		this.placed = 0;
	}
	
	//accessor for this player
	public int getValue(){
		return this.value;
	}
	
	//accessor for player that isn't this one
	public int getOtherPlayer(){
		return (value == 1 ? 2 : 1); //if player 1, return 2 (other player is 2), else return 1
	}
	
	//accessor for this player's unplaced pieces	
	public int getUnplaced(){
		return this.unplaced;
	}
	
	//accessor for this player's placed pieces
	public int getPlaced(){
		return this.placed;
	}
	
	//for placing a piece on the board during preset board setup
	public void placePiece(){
		this.unplaced--; //1 less piece that is unplaced
		this.placed++; //1 more piece that is placed
	}
	
	//for removing a piece during preset board setup
	public void removeSetupPiece(){
		this.unplaced++; //1 more piece that is unplaced
		this.placed--; //1 less piece that is placed
	}
	
	//for removing a piece during removal phase of game
	public void removeGamePiece(){
		this.placed--; //1 less piece on the board
	}
}
