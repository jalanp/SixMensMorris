/*
 * Names: Prajvin Jalan, Anthony Chang, Nic Tristani
 * 
 * Some Assignment 1 Design Decisions : Program gives the option to run a new game or a preset game. The new game option
 * 										loads up an empty board with blue and red disks on either side. Nothing has been coded
 * 										past this stage since the assignment was a little ambiguous, and since moves are 
 * 										supposed be done in assignment 2 we decided to postpone the coding of any player 
 * 										movement in a game. The preset game option allows the user to set up the board with
 * 										the appropriate amount of blue and red disks, and if there are no errors it proceeds
 * 										to create the board in this state (again with movement gameplay disabled).
 */

package t1_group5_SixMensMorris;

import java.awt.Dimension;
import javax.swing.*;

public class SixMensMorris {
	public static void main(String[] args) {
		//sets up a frame to hold the initial window
		JFrame frame = new JFrame("Six Men's Morris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MainStartUp mainStart = new MainStartUp(); //creates a main start up window
		//sets up the details of the frame for this window
		frame.setContentPane(mainStart);
		frame.setPreferredSize(new Dimension(350, 260));
		frame.pack();
		frame.setVisible(true);
		mainStart.setOldFrame(frame); //sets mainStart's old frame as this one so that it can be closed in that class
	}

}
