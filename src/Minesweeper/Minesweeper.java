package Minesweeper;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Minesweeper extends JPanel implements ActionListener {
	
	private Cell[][] cells;
	private Image FLAG_IMAGE, MINE_IMAGE, EXPLOSION_IMAGE;
	private Image[] NUMBER_IMAGES;
	private final GameController CONTROLLER;
	
	public Minesweeper(GameController gameController) {
		Timer timer = new Timer(0, this);
		timer.start();
		
		int numX = 24;
		int numY = 24;
		
		this.setPreferredSize(new Dimension(numX * 30 + 1, numY * 30 + 1));

		newGame(numX, numY, 99);
		loadImages(20);
		
		this.addMouseListener(this.CONTROLLER = gameController);
		this.addKeyListener(this.CONTROLLER);
		this.addMouseMotionListener(this.CONTROLLER);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		
	}
	
	private void newGame(int xSpots, int ySpots, int numOfMines){
		generateGame(xSpots, ySpots);
		generateMines(numOfMines);
		generateSpotValue();
	}
	
	private void generateGame(int xSpots, int ySpots) {
		this.cells = new Cell[ySpots][xSpots];
		Cell.SafeSpots = 0;
		Cell.MinesExploded = false;

		for (int i = 0; i < this.cells.length; ++i) {
			for (int j = 0; j < this.cells[i].length; ++j) {
				this.cells[i][j] = new Cell(j, i);
			}
		}
	}
	
	private void generateMines(int numOfMines) {
		int minesPlaced = 0;
		boolean[][] minesArmed = new boolean[this.cells.length][this.cells[0].length];
		Random rand = new Random();
		
		while (minesPlaced < numOfMines) {
			final int X_RAND = rand.nextInt(minesArmed[0].length);
			final int Y_RAND = rand.nextInt(minesArmed.length);

			if (!cells[Y_RAND][X_RAND].isArmed()) {
				this.cells[Y_RAND][X_RAND].arm();
				minesPlaced++;
			}
		}
	}
	
	private void generateSpotValue() {
		for (int i = 0; i < this.cells.length; ++i) {
			for (int j = 0; j < this.cells[i].length; ++j) {
				if (this.cells[i][j].isArmed()) {
					
					for (int offX = -1; offX < 2; ++offX) {
						for (int offY = -1; offY < 2; ++offY) {
							final int X_ = j + offX, Y_ = i + offY;
							
							if (!(offX == 0 && offY == 0) && validCoords(X_, Y_) && this.cells[i][j].isArmed() &&
									!this.cells[Y_][X_].isArmed())
								this.cells[Y_][X_].mineValue++;
							
						}
					}
				}
			}
		}
	}
	
	private void loadImages(int imageDim) {
		String resourcePath = "/resources";
		this.FLAG_IMAGE = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource(String.format("%s/misc/flag_%dx%d.png", resourcePath, imageDim, imageDim)));
		this.MINE_IMAGE = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource(String.format("%s/misc/mine_%dx%d.png", resourcePath, imageDim, imageDim)));
		this.EXPLOSION_IMAGE = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource(String.format("%s/misc/explosion_%dx%d.png", resourcePath, imageDim, imageDim)));
		
		this.NUMBER_IMAGES = new Image[8];
		for (int i = 0; i < 8;)
			this.NUMBER_IMAGES[i++] = Toolkit.getDefaultToolkit().
											getImage(Minesweeper.class.getResource(String.format("%s/numbers/pix/number%d_%dx%d.png",
													resourcePath, i, imageDim, imageDim)));
	}
	
	private boolean validCoords(final int X, final int Y) {
		return X >= 0 && Y >= 0 && Y < this.cells.length && X < this.cells[Y].length;
	}
	
	public void paintComponent(Graphics gfx) {
		super.paintComponent(gfx);
		Graphics2D g2d = (Graphics2D) gfx;
		
		final int DIM = this.getPreferredSize().width / this.cells.length;
		int drawX, drawY;
		
		for (Cell[] spotRow : this.cells) {
			for (Cell cell : spotRow) {
				drawX = DIM * cell.x + DIM * 3 / 16;
				drawY = DIM * cell.y + DIM * 3 / 16;
				
				g2d.setColor(Color.WHITE);
				g2d.drawRect(cell.x * DIM, cell.y * DIM, DIM, DIM);
				
				if (cell.isFlagged()) {
					g2d.drawImage(this.FLAG_IMAGE, drawX, drawY, null);
				} else if (cell.isArmed() && (Cell.MinesExploded || Cell.SafeSpots == 0)) {
					g2d.drawImage(Cell.MinesExploded ? this.EXPLOSION_IMAGE : this.MINE_IMAGE, drawX, drawY, null);
					this.removeMouseListener(this.CONTROLLER);
				} else if (cell.isRevealed()) {
					g2d.setColor(Color.GRAY);
					g2d.fillRect(cell.x * DIM, cell.y * DIM,
							DIM + (cell.x == this.cells[cell.y].length - 1 ? 1 : 0),
							DIM + (cell.y == this.cells.length - 1 ? 1 : 0));
					
					if (cell.x == this.cells[cell.y].length - 1)
						drawX += 1;
					if (cell.y == this.cells.length - 1)
						drawY += 1;
					if (cell.mineValue > 0)
						g2d.drawImage(this.NUMBER_IMAGES[cell.mineValue - 1], drawX, drawY, null);
 				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public void selectSpot(int x, int y) {
		if (!validCoords(x, y)) return;
		
		if (this.cells[y][x].isRevealed())
			return;
		
		if (!this.cells[y][x].isArmed()) {
			RevealAlgorithms.BFSIterative(x, y, this.cells);
			if (Cell.SafeSpots == 0)
				System.out.println("Game Completed :)!");
			
		}
		
		this.cells[y][x].reveal();
		
		if (this.cells[y][x].isArmed()) {
			this.cells[y][x].minesGoBoom();
			System.out.println("Mines go boom :(...");
			
			for (Cell[] spotRow : this.cells) {
				for (Cell cell : spotRow) {
					if (cell.isArmed()) cell.reveal();
				}
			}
		}
		
	}
	
	public void toggleFlagSpot(int x, int y) {
		if (!validCoords(x, y)) return;
		if (this.cells[y][x].isRevealed()) return;
		this.cells[y][x].toggleFlag();
	}
	
	public int numberOfSpots() {
		return this.cells.length * this.cells[0].length;
	}
	
	public void _newGame(int gameDifficulty) {
		if (this.getMouseListeners().length == 0)
			this.addMouseListener(this.CONTROLLER);

		int xSize, ySize, mineCount;
		if (gameDifficulty == 1) {
			xSize = ySize = mineCount = 10;
		} else if (gameDifficulty == 2) {
			xSize = ySize = 16;
			mineCount = 40;
		} else {
			xSize = ySize = 24;
			mineCount = 99;
		}
		
		//this.setPreferredSize(new Dimension(xSize * 30, ySize * 30));
		newGame(xSize, ySize, mineCount);
		loadImages(this.getPreferredSize().width / xSize * 2 / 3);
	}
	
}
