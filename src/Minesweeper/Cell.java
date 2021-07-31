package Minesweeper;

public class Cell {
	int x, y;
	int mineValue;
	private boolean mine;
	private boolean isRevealed;
	private boolean isFlagged;
	public static int SafeCells = 0;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.mine = false;
		this.isRevealed = false;
		this.isFlagged = false;
		this.mineValue = 0;
		SafeCells++;
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
		SafeCells--;
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
		SafeCells--;
	}
	
	@Override
	public String toString(){
		return "Tile(" + x + "," + y + ",revealed=" + this.isRevealed + ",flagged=" + this.isFlagged + ")";
	}
}
