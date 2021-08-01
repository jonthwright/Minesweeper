package Minesweeper;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class Minesweeper extends JPanel implements ActionListener {
	
	private Cell[][] cells;
	private Image FLAG_IMAGE, MINE_IMAGE, EXPLOSION_IMAGE;
	private Image GAME_OVER, WIN;
	private Image[] NUMBER_IMAGES, SELECT_LVLS;
	private final GameController CONTROLLER;
	private boolean mineExploded;
	
	public Minesweeper(GameController gameController) {
		Timer timer = new Timer(0, this);
		timer.start();
		
		int numX = 24;
		int numY = 24;
		
		this.setPreferredSize(new Dimension(721, 721));

		newGame(numX, numY, 99);
		loadImages(20);
		
		this.addMouseListener(this.CONTROLLER = gameController);
		this.addKeyListener(this.CONTROLLER);
		this.addMouseMotionListener(this.CONTROLLER);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		
	}
	
	private void newGame(int xSpots, int ySpots, int numOfMines){
		this.mineExploded = false;
		generateCells(xSpots, ySpots);
		generateMines(numOfMines);
	}
	
	private void generateCells(int xSpots, int ySpots) {
		this.cells = new Cell[ySpots][xSpots];
		Cell.SafeCells = 0;

		for (int i = 0; i < this.cells.length; ++i)
			for (int j = 0; j < this.cells[i].length; ++j)
				this.cells[i][j] = new Cell(j, i);
	}
	
	private void generateMines(int numOfMines) {
		int minesPlaced = 0;
		Random rand = new Random();
		
		while (minesPlaced < numOfMines) {
			final int Y = rand.nextInt(this.cells.length);
			final int X = rand.nextInt(this.cells[Y].length);
			
			if (!this.cells[Y][X].isArmed()) {
				this.cells[Y][X].arm();
				
				for (int i = -1; i < 2; ++i) {
					for (int j = -1; j < 2; ++j) {
						final int X_NEIGH = X + i;
						final int Y_NEIGH = Y + j;
						
						if (validCoords(X_NEIGH, Y_NEIGH))
							this.cells[Y_NEIGH][X_NEIGH].mineValue++;
					}
				}
				
				minesPlaced++;
			}
		}
	}
	
	private void loadImages(int dim) {
		String res = "/resources";

		this.FLAG_IMAGE = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource(String.format("%s/misc/flag_%dx%d.png", res, dim, dim)));
		this.MINE_IMAGE = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource(String.format("%s/misc/mine_%dx%d.png", res, dim, dim)));
		this.EXPLOSION_IMAGE = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource(String.format("%s/misc/explosion_%dx%d.png", res, dim, dim)));
		this.WIN = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource(res + "/lvls/winning.png"));
		this.GAME_OVER = Toolkit.getDefaultToolkit().
				getImage(Minesweeper.class.getResource(res + "/lvls/gameover.png"));
		
		this.NUMBER_IMAGES = new Image[8];
		for (int i = 0; i < 8; ++i)
			this.NUMBER_IMAGES[i] = Toolkit.getDefaultToolkit().
											getImage(Minesweeper.class.getResource(String.format("%s/numbers/number%d_%dx%d.png",
													res, i + 1, dim, dim)));
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
				
				if (cell.isFlagged() && !cell.isRevealed() && !this.mineExploded) {
					g2d.drawImage(this.FLAG_IMAGE, drawX, drawY, null);
				} else if (cell.isArmed() && (this.mineExploded || Cell.SafeCells == 0)) {
					g2d.drawImage(this.mineExploded ? this.EXPLOSION_IMAGE : this.MINE_IMAGE, drawX, drawY, null);
					this.removeMouseListener(this.CONTROLLER);
				} else if (cell.isRevealed()) {
					g2d.setColor(Color.GRAY);
					g2d.fillRect(cell.x * DIM, cell.y * DIM, DIM, DIM);

					if (cell.mineValue > 0)
						g2d.drawImage(this.NUMBER_IMAGES[cell.mineValue - 1], drawX, drawY, null);
				
				}
			}
		}
		
		if (this.mineExploded) {
			final int IMG_WIDTH = 505;
			final int CEN_X = (this.getWidth() - IMG_WIDTH) / 2;
			g2d.drawImage(this.GAME_OVER, CEN_X, 40, null);
		} else if (Cell.SafeCells == 0) {
			final int IMG_WIDTH = 421;
			final int CEN_X = (this.getWidth() - IMG_WIDTH) / 2;
			g2d.drawImage(this.WIN, CEN_X, 40, null);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	public void selectSpot(int x, int y) {
		if (!validCoords(x, y) || this.cells[y][x].isRevealed())
			return;
		
		if (!this.cells[y][x].isArmed()) {
			revealNeighbours(new Coordinates(x, y));
			return;
		}
		
		if (this.cells[y][x].isArmed()) {
			this.cells[y][x].reveal();
			this.mineExploded = true;
			//System.out.println("Mines go boom :(...");
			
			for (Cell[] spotRow : this.cells) {
				for (Cell cell : spotRow) {
					if (cell.isArmed()) cell.reveal();
				}
			}
		}
		
	}
	
	public void toggleFlagSpot(int x, int y) {
		if (!validCoords(x, y) || this.cells[y][x].isRevealed())
			return;

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
	
	private void revealNeighbours(final Coordinates COORD) {
		Queue<Coordinates> q = new LinkedList<>();
		q.add(COORD);

		this.cells[COORD.y][COORD.x].reveal();
		
		while (!q.isEmpty()) {
			final Coordinates COORDS = q.remove();
			final int X = COORDS.x, Y = COORDS.y;
			
			if (this.cells[Y][X].mineValue > 0)
				continue;
			
			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					final int X_ = X + j, Y_ = Y + i;
					Coordinates neighbour = new Coordinates(X_, Y_);
					
					if (validCoords(X_, Y_) && !this.cells[Y_][X_].isRevealed() && this.cells[Y_][X_].isArmed()) {
						q.add(neighbour);
						this.cells[Y_][X_].reveal();
					}
				}
			}
		}
	}
	
	private boolean validCoords(final int X, final int Y) {
		return X >= 0 && Y >= 0 && Y < this.cells.length && X < this.cells[Y].length;
	}
	
}
