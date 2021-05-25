package Minesweeper;

class Pair {
	int first;
	int second;
	
	public Pair(int first, int second) {
		this.first = first;
		this.second = second;
	}
	
	@Override
	public String toString() {
		return "Pair(" + this.first + "," + this.second + ")";
	}
}