package Minesweeper;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Minesweeper extends JPanel implements ActionListener {
	
	private final GAME_STATE gameState;
	private final GameController gameController;
	private Spot[][] spots;
	private Timer timer;

	public Minesweeper(GameController gameController) {
		this.timer = new Timer(0, this);
		this.timer.start();
		
		this.gameController = gameController;
		this.gameState = GAME_STATE.RUNNING;
		generateGame();
		this.addMouseListener(this.gameController);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
	}
	
	private void generateGame() {
		int xSpotsNum = this.gameController.getTileX();
		int ySpotsNum = this.gameController.getTileY();
		this.spots = new Spot[ySpotsNum][xSpotsNum];
		
		for (int i = 0; i < this.spots.length; ++i) {
			for (int j = 0; j < this.spots[i].length; ++j) {
				this.spots[i][j] = new Spot(j, i);
			}
		}
	}
	
	public void paintComponent(Graphics gfx) {
		super.paintComponent(gfx);
		Graphics2D g2d = (Graphics2D) gfx;
		
		if (this.gameState == GAME_STATE.RUNNING) {
			final int DIM = this.gameController.DIM;
			for (Spot[] spotRow : this.spots) {
				for (Spot spot : spotRow) {
					if (!spot.isRevealed()) {
						if (!spot.isFlagged()) {
							g2d.setColor(Color.BLACK);
						} else {
							g2d.setColor(Color.BLUE);
						}
					} else {
						if (spot.isMine()) {
							g2d.setColor(Color.RED);
							this.removeMouseListener(this.gameController);
						} else {
							g2d.setColor(Color.GREEN);
						}
					}
					
					g2d.fillRect(spot.x * DIM, spot.y * DIM, DIM, DIM);
				}
			}
		}
	}
	
	public void update() {
		if (this.gameState == GAME_STATE.RUNNING) {
		
		}
		
		if (this.gameState == GAME_STATE.GAME_OVER) {
		
		}

		if (this.gameState == GAME_STATE.GAME_COMPLETED) {
		
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
		update();
	}
	
	public void selectSpot(int x, int y) {
		System.out.println("Selected " + this.spots[y][x]);

		if (this.spots[y][x].isRevealed())
			return;

		this.spots[y][x].reveal();
	}
	
	public void flagSpot(int x, int y) {
		System.out.println((this.spots[y][x].isFlagged() ? "Unflagged " : "Flagged ") + this.spots[y][x]);
		this.spots[y][x].toggleFlag();
	}
}
