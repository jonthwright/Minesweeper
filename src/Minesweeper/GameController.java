package Minesweeper;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GameController extends JFrame implements MouseInputListener {
	private final Minesweeper MINESWEEPER;
	private int tileX;
	private int tileY;
	public final static int DIM = 30;
	
	public GameController() {
		this.tileX = 24;
		this.tileY = 24;
	
		this.MINESWEEPER = new Minesweeper(this);
		this.setTitle("Minesweeper");
		this.add(this.MINESWEEPER);
		//this.setSize((this.tileX + 1) * this.DIM - this.DIM / 2, (this.tileY + 1) * this.DIM + 9);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setIconImage(Toolkit.getDefaultToolkit().
				getImage(GameController.class.getResource("/resources/minesweeper.png")));
		
		this.setVisible(true);
		this.pack();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		final int X_MOUSE = e.getX() / this.DIM;
		final int Y_MOUSE = e.getY() / this.DIM;

		if (e.getButton() == MouseEvent.BUTTON1) {
			this.MINESWEEPER.selectSpot(X_MOUSE, Y_MOUSE);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			this.MINESWEEPER.flagSpot(X_MOUSE, Y_MOUSE);
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
	
	}
	
	public int getTileX() {
		return this.tileX;
	}
	
	public int getTileY() {
		return this.tileY;
	}
}
