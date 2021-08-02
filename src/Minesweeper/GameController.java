package Minesweeper;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


public class GameController extends JFrame implements MouseInputListener, KeyListener, MouseMotionListener {
	private int DIM = 30;
	private final Minesweeper MINESWEEPER;
	private Difficulty currDiff = Difficulty.HARD;
	
	public GameController() {
		this.MINESWEEPER = new Minesweeper(this);
		this.setTitle("Minesweeper");
		this.add(this.MINESWEEPER);
		//this.setSize((this.tileX + 1) * this.DIM - this.DIM / 2, (this.tileY + 1) * this.DIM + 9);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setIconImage(Toolkit.getDefaultToolkit().
				getImage(GameController.class.getResource("/resources/icons/mine_ex_icon.png")));
		this.setResizable(false);
		this.setVisible(true);
		this.pack();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		int X = e.getX();
		int Y = e.getY();
		
		if (!this.MINESWEEPER.isMineExploded() && Cell.SafeCells > 0) {
			X /= this.DIM;
			Y /= this.DIM;
			
			switch (e.getButton()) {
				case MouseEvent.BUTTON1 -> this.MINESWEEPER.selectSpot(X, Y);
				case MouseEvent.BUTTON2, MouseEvent.BUTTON3 -> this.MINESWEEPER.toggleFlagSpot(X, Y);
			}
		} else if (100 <= X && X < 620) {
			if (240 <= Y && Y < 330) this.currDiff = Difficulty.EASY;
			else if (340 <= Y && Y < 430) this.currDiff = Difficulty.MEDIUM;
			else if (440 <= Y && Y < 530) this.currDiff = Difficulty.HARD;
			else if (615 <= Y && Y < 700) System.exit(0);
			
			this.MINESWEEPER._newGame(this.currDiff);
			
			this.DIM = (int) (this.MINESWEEPER.getPreferredSize().width / Math.sqrt(this.MINESWEEPER.numberOfSpots()));
			
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
	
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {

	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println(e.getX() + "," + e.getY());
		
	}
	
	@Override
	public void keyTyped(KeyEvent key) {

	}
	
	@Override
	public void keyPressed(KeyEvent key) {
		
		switch (key.getKeyCode()) {
			case KeyEvent.VK_NUMPAD0, KeyEvent.VK_0 -> this.MINESWEEPER._newGame(this.currDiff);
			case KeyEvent.VK_NUMPAD1, KeyEvent.VK_1 -> this.MINESWEEPER._newGame(this.currDiff = Difficulty.EASY);
			case KeyEvent.VK_NUMPAD2, KeyEvent.VK_2 -> this.MINESWEEPER._newGame(this.currDiff = Difficulty.MEDIUM);
			case KeyEvent.VK_NUMPAD3, KeyEvent.VK_3 -> this.MINESWEEPER._newGame(this.currDiff = Difficulty.HARD);
		}
		
		this.DIM = (int) (this.MINESWEEPER.getPreferredSize().width / Math.sqrt(this.MINESWEEPER.numberOfSpots()));
	}
	
	@Override
	public void keyReleased(KeyEvent key) {
	
	}
}
