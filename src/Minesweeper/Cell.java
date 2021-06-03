package Minesweeper;

public class Cell {
	int x, y;
	int mineValue;
	private boolean mine;
	private boolean isRevealed;
	private boolean isFlagged;
	public static boolean MinesExploded;
	public static int SafeSpots = 0;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.mine = false;
		this.isRevealed = false;
		this.isFlagged = false;
		this.mineValue = 0;
		SafeSpots++;
	}
	
	public boolean isArmed() {
		return this.mine;
	}
	
	public boolean isRevealed() {
		return this.isRevealed;
	}
	
	public void reveal() {
		if (this.isRevealed) return;
		this.isRevealed = true;
		SafeSpots--;
	}

	public void toggleFlag() {
		if (this.isRevealed) return;
		this.isFlagged = !this.isFlagged;
	}
	
	public boolean isFlagged() {
		return this.isFlagged;
	}
	
	public void arm() {
		this.mine = true;
		SafeSpots--;
	}
	
	public void minesGoBoom() {
		if (MinesExploded) return;
		MinesExploded = true;
	}
	
	@Override
	public String toString(){
		return "Tile(" + x + "," + y + ",revealed=" + this.isRevealed + ",flagged=" + this.isFlagged + ")";
	}
}
