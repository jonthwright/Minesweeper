package Minesweeper;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class RevealAlgos {
	public static void DFSRecursive(final int X, final int Y, final Spot[][] SPOTS) {
		if (!validCoord(X, Y, SPOTS)) return;
		if (SPOTS[Y][X].isMine()) return;
		if (SPOTS[Y][X].isRevealed()) return;
		
		SPOTS[Y][X].reveal();
		if (!isZeroMineNeighbour(SPOTS[Y][X])) return;
		
		for (int i = -1; i < 2; ++i) {
 			for (int j = -1; j < 2; ++j) {
 				if (i == 0 && j == 0) continue;
				DFSRecursive(X + j, Y + i, SPOTS);
			}
		}
	}
	
	public static void DFSIterative(final int X, final int Y, final Spot[][] SPOTS) {
		Stack<Pair> stack = new Stack<>();
		boolean[][] visited = new boolean[SPOTS.length][SPOTS[0].length];
		
		for (int i = 0; i < visited.length; ++i)
			for (int j = 0; j < visited[0].length; ++j)
				visited[i][j] = false;
		
		stack.add(new Pair(X, Y));
		visited[Y][X] = true;
		
		while (!stack.isEmpty()) {
			final Pair COORD = stack.pop();
			final int X_CURR = COORD.first, Y_CURR = COORD.second;
			
			SPOTS[Y_CURR][X_CURR].reveal();
			
			if (!isZeroMineNeighbour(SPOTS[Y_CURR][X_CURR])) continue;

			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					if (i == 0 && j == 0) continue;
					final int OFFSET_X = X_CURR + j, OFFSET_Y = Y_CURR + i;
					
					if (validCoord(OFFSET_X, OFFSET_Y, SPOTS) && //!visited[OFFSET_Y][OFFSET_X] &&
							!SPOTS[OFFSET_Y][OFFSET_X].isRevealed() && !SPOTS[OFFSET_Y][OFFSET_X].isMine() ) {
							
						stack.add(new Pair(OFFSET_X, OFFSET_Y));
						//visited[OFFSET_Y][OFFSET_X] = true;
					}
				}
			}
		}
	}

	public static void BFSIterative(final int X, final int Y, final Spot[][] SPOTS) {
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(X, Y));
		
		boolean[][] visited = new boolean[SPOTS.length][SPOTS[0].length];
		
		for (int i = 0; i < visited.length; ++i)
			for (int j = 0; j < visited[0].length; ++j)
				visited[i][j] = false;
		
		visited[Y][X] = true;
		
		while (!q.isEmpty()) {
			final Pair COORD = q.remove();
			final int X_CURR = COORD.first, Y_CURR = COORD.second;
			
			SPOTS[Y_CURR][X_CURR].reveal();
			
			if (!isZeroMineNeighbour(SPOTS[Y_CURR][X_CURR])) continue;

			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					if (i == 0 && j == 0) continue;
					final int OFFSET_X = X_CURR + j, OFFSET_Y = Y_CURR + i;
					if (validCoord(OFFSET_X, OFFSET_Y, SPOTS) && //!visited[OFFSET_Y][OFFSET_X] &&
							!SPOTS[OFFSET_Y][OFFSET_X].isRevealed() && !SPOTS[OFFSET_Y][OFFSET_X].isMine()) {
						
						q.add(new Pair(OFFSET_X, OFFSET_Y));
						//visited[OFFSET_Y][OFFSET_X] = true;
					}
				}
			}
		}
	}
	
	private static boolean isZeroMineNeighbour(Spot spot) {
		return spot.getMineValue() == 0;
	}
	
	private static boolean validCoord(final int X, final int Y, final Spot[][] SPOTS) {
		return !(X < 0 || Y < 0 || X >= SPOTS[0].length || Y >= SPOTS.length);
	}
}