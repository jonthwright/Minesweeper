package Minesweeper;

import java.util.Random;

public class Spot {
	int x, y;
	private final boolean MINE;
	private boolean isRevealed;
	private boolean isFlagged;
	private int mineValue;
	
	public Spot(int x, int y) {
		this.x = x;
		this.y = y;
		this.MINE = new Random().nextBoolean();
		this.isRevealed = false;
		this.isFlagged = false;
		this.mineValue = 0;
	}
	
	public boolean isMine() {
		return this.MINE;
	}
	
	public boolean isRevealed() {
		return this.isRevealed;
	}
	
	public void reveal() {
		System.out.println(this + " reveal");
		this.isRevealed = true;
	}

	public void toggleFlag() {
		this.isFlagged = !this.isFlagged;
	}
	
	public boolean isFlagged() {
		return this.isFlagged;
	}
	
	public void setMineValue(int mineValue) {
		this.mineValue = mineValue;
	}
	
	public int getMineValue() {
		return this.mineValue;
	}

	@Override
	public String toString(){
		return "Tile(" + x + "," + y + ",revealed=" + this.isRevealed + ",flagged=" + this.isFlagged + ")";
	}
	
}
