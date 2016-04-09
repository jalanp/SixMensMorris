/*
 * Names: Prajvin Jalan, Anthony Chang, Nic Tristani
 * 
 * Design Decisions: 	Program gives the user the option to start a new game player vs player game, a new
 * 						player vs computer game, or a preset game. In player vs player, the first turn player
 * 						is chosen randomly, and the game can be played up to the winner (placement, movement,
 * 						and removal all work). In player vs computer, the game can be played up to the winner
 * 						just as if with player vs player, but this time against an AI who's moves are determined
 * 						algorithmically. In the preset game, a board can be set up manually and the game starts
 * 						from the movement stage (this preset game has not been implemented yet because the
 * 						latest assignment didn't require it).
 *  
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
		frame.setPreferredSize(new Dimension(350, 370));
		frame.pack();
		frame.setVisible(true);
		mainStart.setOldFrame(frame); //sets mainStart's old frame as this one so that it can be closed in that class
	}

}
