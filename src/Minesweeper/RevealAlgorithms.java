package Minesweeper;

import java.util.*;

public class RevealAlgorithms {
	public static void DFSRecursive(final int X, final int Y, final Cell[][] CELLS) {
		if (!validCoords(X, Y, CELLS) || CELLS[Y][X].isArmed() || CELLS[Y][X].isRevealed())
			return;
		
		CELLS[Y][X].reveal();

		if (CELLS[Y][X].mineValue > 0)
			return;
		
		for (int i = -1; i < 2; ++i) {
 			for (int j = -1; j < 2; ++j) {
 				if (i != 0 || j != 0)
					DFSRecursive(X + j, Y + i, CELLS);
			}
		}
	}
	
	public static void DFSIterative(final int X, final int Y, final Cell[][] CELLS) {
		Stack<Coordinates> stack = new Stack<>();
		stack.add(new Coordinates(X, Y));
		
		while (!stack.isEmpty()) {
			final Coordinates COORDS = stack.pop();
			final int X_CURR = COORDS.x, Y_CURR = COORDS.y;
			
			CELLS[Y_CURR][X_CURR].reveal();
			
			if (CELLS[Y_CURR][X_CURR].mineValue > 0)
				continue;

			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					if (i == 0 && j == 0) continue;
					
					final int OFFSET_X = X_CURR + j, OFFSET_Y = Y_CURR + i;
					
					if (validCoords(OFFSET_X, OFFSET_Y, CELLS) && //!visited[OFFSET_Y][OFFSET_X] &&
							!CELLS[OFFSET_Y][OFFSET_X].isRevealed() && !CELLS[OFFSET_Y][OFFSET_X].isArmed() ) {
							
						stack.add(new Coordinates(OFFSET_X, OFFSET_Y));
						//visited[OFFSET_Y][OFFSET_X] = true;
					}
				}
			}
		}
	}

	public static void BFSIterative(final int X, final int Y, final Cell[][] CELLS) {
		Queue<Coordinates> q = new LinkedList<>();
		q.add(new Coordinates(X, Y));
		
		while (!q.isEmpty()) {
			
			final Coordinates COORDS = q.remove();
			final int X_CURR = COORDS.x, Y_CURR = COORDS.y;
			
			CELLS[Y_CURR][X_CURR].reveal();
			
			if (CELLS[Y_CURR][X_CURR].mineValue > 0)
				continue;

			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					if (i == 0 && j == 0) continue;
					
					final int OFFSET_X = X_CURR + j, OFFSET_Y = Y_CURR + i;
					if (validCoords(OFFSET_X, OFFSET_Y, CELLS) && //!visited[OFFSET_Y][OFFSET_X] &&
							!CELLS[OFFSET_Y][OFFSET_X].isRevealed() && !CELLS[OFFSET_Y][OFFSET_X].isArmed()) {
						q.add(new Coordinates(OFFSET_X, OFFSET_Y));
						//visited[OFFSET_Y][OFFSET_X] = true;
					}
				}
			}
		}
	}
	
	private static boolean validCoords(final int X, final int Y, final Cell[][] CELLS) {
		return X >= 0 && Y >= 0 && Y < CELLS.length && X < CELLS[Y].length;
	}
}