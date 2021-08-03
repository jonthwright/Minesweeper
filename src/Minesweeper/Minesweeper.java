package Minesweeper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class Minesweeper extends JPanel implements ActionListener {
	
	private Cell[][] cells;
	private BufferedImage FLAG_IMG, MINE_IMG; // EXPLODE_IMG;
	private BufferedImage GAME_OVER_IMG, EXIT_IMG, YOU_WIN_IMG;
	private BufferedImage[] NUMBER_IMAGES, SELECT_LVLS;
	private final GameController CONTROLLER;
	private boolean mineExploded;
	final String RESRC_DIR = "src/resources";
	
	public Minesweeper(GameController gameController) {
		Timer timer = new Timer(0, this);
		timer.start();

		this.setPreferredSize(new Dimension(721, 721));
		
		int gridCount = 24;
		this.SELECT_LVLS = new BufferedImage[3];
		this.NUMBER_IMAGES = new BufferedImage[8];

		try {
			this.YOU_WIN_IMG = ImageIO.read(new File(this.RESRC_DIR + "/lvls/winning.png"));
			this.GAME_OVER_IMG = ImageIO.read(new File(this.RESRC_DIR + "/lvls/gameover.png"));
			this.EXIT_IMG = ImageIO.read(new File(this.RESRC_DIR + "/lvls/exit.png"));
			
			for (int i = 0; i < this.SELECT_LVLS.length; ++i)
				this.SELECT_LVLS[i] = ImageIO.read(new File(String.format("%s/lvls/unselected_lvl%d.png", this.RESRC_DIR, i + 1)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		newGame(gridCount, 99);
		loadImages(20);
		
		this.addMouseListener(this.CONTROLLER = gameController);
		this.addKeyListener(this.CONTROLLER);
		this.addMouseMotionListener(this.CONTROLLER);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		
	}
	
	private void newGame(int gridCount, int numOfMines){
		this.cells = new Cell[gridCount][gridCount];
		this.mineExploded = false;
		Cell.SafeCells = 0;
		
		for (int y = 0; y < gridCount; ++y)
			for (int x = 0; x < gridCount; ++x)
				this.cells[y][x] = new Cell(x, y);

		generateMines(numOfMines);
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
						final int X_ = X + i, Y_ = Y + j;
						
						if (validCoords(X_, Y_))
							this.cells[Y_][X_].mineValue++;
					}
				}
				
				minesPlaced++;
			}
		}
	}
	
	private void loadImages(int dim) {
		try {
			this.FLAG_IMG = ImageIO.read(new File(String.format("%s/misc/flag_%dx%d.png", this.RESRC_DIR, dim, dim)));
			this.MINE_IMG = ImageIO.read(new File(String.format("%s/misc/mine_%dx%d.png", this.RESRC_DIR, dim, dim)));
			//this.EXPLODE_IMG = ImageIO.read(new File(String.format("%s/misc/explosion_%dx%d.png", this.RESRC_DIR, dim, dim)));
			
			for (int i = 0; i < 8; ++i)
				this.NUMBER_IMAGES[i] = ImageIO.read(new File(String.format("%s/numbers/number%d_%dx%d.png",
						this.RESRC_DIR, i + 1, dim, dim)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics gfx) {
		super.paintComponent(gfx);
		Graphics2D g2d = (Graphics2D) gfx;
		
		final int DIM = this.getPreferredSize().width / this.cells.length;
		int drawX, drawY;
		
		for (Cell[] cellRow : this.cells) {
			for (Cell cell : cellRow) {
				drawX = DIM * cell.x + DIM * 3 / 16;
				drawY = DIM * cell.y + DIM * 3 / 16;
				
				g2d.setColor(Color.WHITE);
				g2d.drawRect(cell.x * DIM, cell.y * DIM, DIM, DIM);
				
				if ((cell.isFlagged() && !cell.isRevealed()) || (Cell.SafeCells == 0 && cell.isArmed())) {
					g2d.drawImage(this.FLAG_IMG, drawX, drawY, null);
				} else if (cell.isArmed() && this.mineExploded) {
					g2d.drawImage(this.MINE_IMG, drawX, drawY, null);
				} else if (cell.isRevealed()) {
					g2d.setColor(Color.GRAY);
					g2d.fillRect(cell.x * DIM, cell.y * DIM, DIM - 1, DIM - 1);

					if (cell.mineValue > 0)
						g2d.drawImage(this.NUMBER_IMAGES[cell.mineValue - 1], drawX, drawY, null);
				
				}
			}
		}
		
		if (this.mineExploded || Cell.SafeCells == 0)
			drawGameScreen(g2d);
	}
	
	private void drawGameScreen(Graphics2D g2d) {
		int imgWidth, cenX;
		final int WIN_WIDTH = this.getWidth();

		imgWidth = this.SELECT_LVLS[0].getWidth();
		cenX = (WIN_WIDTH - imgWidth) / 2;
		for (int i = 0; i < this.SELECT_LVLS.length; ++i)
			g2d.drawImage(this.SELECT_LVLS[i], cenX, 250 + 100 * i, null);
		
		imgWidth = 240;
		g2d.drawImage(this.EXIT_IMG, (WIN_WIDTH - imgWidth) / 2, 625,null);

		if (this.mineExploded) {
			imgWidth = this.GAME_OVER_IMG.getWidth();
			g2d.drawImage(this.GAME_OVER_IMG, (WIN_WIDTH - imgWidth) / 2, 40, null);
		} else if (Cell.SafeCells == 0) {
			imgWidth = this.YOU_WIN_IMG.getWidth();
			g2d.drawImage(this.YOU_WIN_IMG, (WIN_WIDTH - imgWidth) / 2, 40, null);
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
		
		this.mineExploded = this.cells[y][x].isArmed();
	}
	
	public void toggleFlagSpot(int x, int y) {
		if (!validCoords(x, y) || this.cells[y][x].isRevealed())
			return;
		
		this.cells[y][x].toggleFlag();
	}
	
	public int numberOfCells() {
		return this.cells.length * this.cells[0].length;
	}
	
	public boolean isMineExploded() {
		return this.mineExploded;
	}
	
	public void _newGame(Difficulty gameDifficulty) {
		if (this.getMouseListeners().length == 0)
			this.addMouseListener(this.CONTROLLER);

		int gridCount, mineCount, imgDim;
		if (gameDifficulty == Difficulty.EASY) {
			gridCount = 10;
			mineCount = 10;
		} else if (gameDifficulty ==  Difficulty.MEDIUM) {
			gridCount = 16;
			mineCount = 40;
		} else {
			gridCount = 24;
			mineCount = 99;
		}
		
		imgDim = this.getPreferredSize().width / gridCount * 2 / 3;

		newGame(gridCount, mineCount);
		loadImages(imgDim);
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
					
					if (validCoords(X_, Y_) && !this.cells[Y_][X_].isRevealed() && !this.cells[Y_][X_].isArmed()) {
						q.add(new Coordinates(X_, Y_));
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
