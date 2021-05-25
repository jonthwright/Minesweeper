package Minesweeper;

import java.util.Random;

public class Spot {
	int x, y;
	private boolean mine;
	private boolean isRevealed;
	private boolean isFlagged;
	private boolean mineLostTo;
	private static boolean hasMineExploded = false;
	int mineValue;
	
	public Spot(int x, int y) {
		this.x = x;
		this.y = y;
		this.mine = false;
		this.isRevealed = false;
		this.mineLostTo = false;
		this.isFlagged = false;
		this.mineValue = 0;
	}
	
	public boolean isMine() {
		return this.mine;
	}
	
	public boolean isRevealed() {
		return this.isRevealed;
	}
	
	public void reveal() {
		//System.out.println(this + " reveal");
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
	
	public void arm() {
		this.mine = true;
	}
	
	public boolean isMineLostTo() {
		return this.mine && this.mineLostTo;
	}

	public void mineGoBoom() {
		if (hasMineExploded)
			return;

		hasMineExploded = true;
		this.mineLostTo = true;
	}
	
	@Override
	public String toString(){
		return "Tile(" + x + "," + y + ",revealed=" + this.isRevealed + ",flagged=" + this.isFlagged + ")";
	}
}
