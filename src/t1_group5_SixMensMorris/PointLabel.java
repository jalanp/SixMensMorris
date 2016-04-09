package t1_group5_SixMensMorris;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.JLabel;

public class PointLabel extends JLabel implements Serializable{
	private Point point; //variable for the point that this label is for
	private boolean selected; //if the point is selected on the board
	
	//constructor for this label
	public PointLabel(Point point){
		this.selected = false;
		this.point = point; //initializes the point associated with this label
	}
	
	//accessor for this label's point
	public Point getPoint(){
		return this.point;
	}
	
	//mutator to change the label's selection
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	//accessor for this label's selection
	public boolean getSelected(){
		return this.selected;
	}
	
	//paint method to draw the appropriate point
	public void paintComponent (Graphics g){
		super.paintComponent(g);
		switch(this.point.getValue()){ //based on the point's value
			case 0: //point not filled
				break;
			case 1: //point filled by player 1 (blue)
				g.setColor(this.selected ? new Color(100,100,255) : Color.BLUE);
				g.fillOval(0, 0, 30, 30);
				break;
			case 2: //point filled by player 2 (red)
				g.setColor(this.selected ? new Color(255,100,100) : Color.RED);
				g.fillOval(0, 0, 30, 30);
				break;
		}
	}
}