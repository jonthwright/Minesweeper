package Minesweeper;

class Coordinates {
	int x;
	int y;
	
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int hashCode() {
		return this.x * 101839 + this.y * 328897;
	}
	
	@Override
	public String toString() {
		final String CLASS_NAME = this.getClass().getSimpleName();
		return CLASS_NAME + "(" + this.x + "," + this.y + ")";
	}
}