package t1_group5_SixMensMorris;

import java.io.Serializable;

public class Point implements Serializable{
	private int value; //0 for empty, 1 for P1, 2 for P2
	private int square; //point is in outer or inner square
	private int location; //specific location on the square
	private boolean enabled; //whether this point is enabled or not
	
	//constructor for this point on the board
	public Point (int square, int location){
		//initializes variables based on passed in variables
		this.square = square;
		this.location = location;
		this.value = 0; //initial value is 0 for empty point
		this.enabled = true;
	}
	
	//overloaded constructor in case initial value wants to be set
	public Point (int square, int location, int value){
		//initializes variables based on passed in variables
		this.square = square;
		this.location = location;
		this.value = value;
		this.enabled = true;
	}
	
	//accessor for this point's value (0 for empty, 1 for blue or 2 for red)
	public int getValue(){
		return this.value;
	}
	
	//accessor for this point's square (0 for outer or 1 for inner)
	public int getSquare(){
		return this.square;
	}
	
	//accessor for this point's location on the square (0,1,2 for top 3 points)(3,4 for middle 2 points)(5,6,7 for bottom 3 points) 
	public int getLocation(){
		return this.location;
	}
	
	//mutator for this point's value (if player has a piece placed here or not)
	public void setValue(int value){
		this.value = value;
	}
	
	//mutator that sets this point's enabled value
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	//accessor for if this point is enabled
	public boolean getEnabled(){
		return this.enabled;
	}

	//returns true if points are adjacent to each other on the same square, false if not
	public boolean squareAdjacency(Point otherPoint){
		boolean adjacency = false;
		
		if (this.getLocation() == 0) //top left corner
			adjacency = (otherPoint.getLocation() == 1 || otherPoint.getLocation() == 3);
		else if (this.getLocation() == 1 || this.getLocation() == 6) //middle points on top and bottom horizontal lines of square
			adjacency = (this.getLocation() + 1 == otherPoint.getLocation() || this.getLocation() - 1 == otherPoint.getLocation());
		else if (this.getLocation() == 2) //top right corner
			adjacency = (otherPoint.getLocation() == 1 || otherPoint.getLocation() == 4);
		else if (this.getLocation() == 3) //middle left corner
			adjacency = (otherPoint.getLocation() == 0 || otherPoint.getLocation() == 5);
		else if (this.getLocation() == 4) //middle right corner
			adjacency = (otherPoint.getLocation() == 2 || otherPoint.getLocation() == 7);
		else if (this.getLocation() == 5) //bottom left corner
			adjacency = (otherPoint.getLocation() == 3 || otherPoint.getLocation() == 6);
		else //bottom right corner (point 7)
			adjacency = (otherPoint.getLocation() == 4 || otherPoint.getLocation() == 6);
			
		return adjacency;
	}
	
	//returns true if the two values are within the same square, false otherwise
	public boolean sameSquare(Point otherPoint){
		return this.getSquare() == otherPoint.getSquare();
	}
}