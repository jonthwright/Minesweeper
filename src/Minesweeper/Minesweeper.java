package Minesweeper;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Minesweeper extends JPanel implements ActionListener {
	
	private GAME_STATE gameState;
	private final GameController gameController;
	private Spot[][] spots;
	private Timer timer;
	private int minesNum = 99;
	private int selectedX, selectedY;
	
	public Minesweeper(GameController gameController) {
		this.timer = new Timer(0, this);
		this.timer.start();
		
		this.setPreferredSize(new Dimension(24*30, 24*30));
		this.gameController = gameController;
		this.gameState = GAME_STATE.RUNNING;

		newGame();
		
		for (Spot[] spotR : this.spots) {
			for (Spot spot : spotR) {
				System.out.print((spot.isMine() ? "*" : spot.mineValue) + " ");
			}
			System.out.println();
		}
		
		this.addMouseListener(this.gameController);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
	}
	
	private void newGame(){
		generateGame();
		generateMines();
		generateSpotValue();
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
	
	private void generateMines() {
		int minesPlaced = 0;
		boolean[][] bombsPlaced = new boolean[this.spots.length][this.spots[0].length];
		Random rand = new Random();
		while (minesPlaced < this.minesNum) {
			final int X_RAND = rand.nextInt(bombsPlaced[0].length);
			final int Y_RAND = rand.nextInt(bombsPlaced.length);

			if (!bombsPlaced[Y_RAND][X_RAND]) {
				this.spots[Y_RAND][X_RAND].arm();
				bombsPlaced[Y_RAND][X_RAND] = true;
				minesPlaced++;
			}
		}
	}
	
	private void generateSpotValue() {
		for (int i = 0; i < this.spots.length; ++i) {
			for (int j = 0; j < this.spots[i].length; ++j) {
				if (this.spots[i][j].isMine()) {
					
					for (int offSetX = -1; offSetX < 2; ++offSetX) {
						for (int offSetY = -1; offSetY < 2; ++offSetY) {
							final int X = j + offSetX, Y = i + offSetY;
							
							if (!(offSetX == 0 && offSetY == 0) && validCoord(X, Y)
									&& this.spots[i][j].isMine() && !this.spots[Y][X].isMine()) this.spots[Y][X].mineValue++;
							
						}
					}
					
				}
			}
		}
	}
	
	private boolean validCoord(final int X, final int Y) {
		return !(X < 0 || Y < 0 || X >= this.spots[0].length || Y >= this.spots.length);
	}
	
	public void paintComponent(Graphics gfx) {
		super.paintComponent(gfx);
		Graphics2D g2d = (Graphics2D) gfx;
		
		final int DIM = this.getPreferredSize().width / this.spots.length;
		
		final Image FLAG_IMAGE = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource("/resources/flag_open_tilted_20x20.png"));
		final Image MINE_IMAGE = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource("/resources/mine_gs_20x20.png"));


		for (Spot[] spotRow : this.spots) {
			for (Spot spot : spotRow) {
				if (!spot.isRevealed()) {
					if (spot.isFlagged()) {
						
						g2d.drawImage(FLAG_IMAGE, spot.x * DIM + DIM / 4, spot.y * DIM + DIM / 4, null);
					}
					g2d.setColor(Color.WHITE);
					g2d.drawRect(spot.x * DIM, spot.y * DIM, DIM, DIM);
				} else {
					if (spot.isMine()) {
						if (spot.isMineLostTo()) {
							g2d.setColor(Color.RED);
							g2d.fillRect(spot.x * DIM, spot.y * DIM, DIM, DIM);
						}
						
						
						g2d.drawImage(MINE_IMAGE, spot.x * DIM + DIM / 4, spot.y * DIM + DIM / 4, null);
						//this.removeMouseListener(this.gameController);
					} else {
						g2d.setColor(Color.GRAY);
						g2d.fillRect(spot.x * DIM, spot.y * DIM, DIM, DIM);
					}
				}
				
				if (spot.mineValue > 0 && !spot.isMine() && !spot.isFlagged() && spot.isRevealed()) {
					//g2d.setColor(Color.BLACK);
					//drawCentredString(g2d, String.valueOf(spot.mineValue), spot.x * DIM, spot.y * DIM, DIM, DIM);
					final Image NUMBER_IMAGE = Toolkit.getDefaultToolkit().getImage(
							Minesweeper.class.getResource("/resources/numbers/col/transparent_number" + spot.mineValue + "_20x20.png"));
					g2d.drawImage(NUMBER_IMAGE, spot.x * DIM + DIM / 4, spot.y * DIM + DIM / 4, null);
					
				}
			}
		}
	}
	
	private void drawCentredString(final Graphics2D g2d, final String string, int x, int y, int WIDTH, int HEIGHT) {
		FontMetrics fm = g2d.getFontMetrics(g2d.getFont());
		
		Rectangle2D rect = fm.getStringBounds(string, g2d);
		
		final int TEXT_WIDTH = (int) rect.getWidth(), TEXT_HEIGHT = (int) rect.getHeight();
		int TEXT_POS_X = x + (WIDTH - TEXT_WIDTH) / 2;
		int TEXT_POS_Y = y + (HEIGHT - TEXT_HEIGHT) / 2 + fm.getAscent();
		
		g2d.drawString(string, TEXT_POS_X, TEXT_POS_Y);
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
		//System.out.println("Selected " + this.spots[y][x]);

		if (this.spots[y][x].isRevealed())
			return;
		
		if (!this.spots[y][x].isMine()) RevealAlgos.DFSIterative(x, y, this.spots);
		
		this.spots[y][x].reveal();
		if (this.spots[y][x].isMine()) {
			this.spots[y][x].mineGoBoom();
			for (int i = 0; i < this.spots.length; ++i)
				for (int j = 0; j < this.spots[i].length; ++j)
					if (this.spots[i][j].isMine())
						this.spots[i][j].reveal();
		}
		
	}
	
	public void flagSpot(int x, int y) {
		//System.out.println((this.spots[y][x].isFlagged() ? "Unflagged " : "Flagged ") + this.spots[y][x]);
		this.spots[y][x].toggleFlag();
	}
}
