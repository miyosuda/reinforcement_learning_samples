package reinforcement.dp_grid;

public class World {
	public static final int WIDTH = 4;
	public static final int HEIGHT = 4;

	private Cell cells[][] = new Cell[WIDTH][HEIGHT];

	public World() {
		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				cells[i][j] = new Cell();
			}
		}

		cells[0][0].setEnd();
		cells[3][3].setEnd();

		process();
	}

	private void process() {
		for (int i = 0; i < 1000; ++i) {
			step();
		}
	}

	private Cell getCell(int x, int y) {
		x = Math.min(Math.max(0, x), WIDTH - 1);
		y = Math.min(Math.max(0, y), HEIGHT - 1);
		return cells[x][y];
	}

	private void step() {
		float newVs[][] = new float[WIDTH][HEIGHT];
		
		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				Cell cell = cells[i][j];
				if (cell.isEnd()) {
					continue;
				}

				Cell cellUp = getCell(i, j - 1);
				Cell cellDown = getCell(i, j + 1);
				Cell cellRight = getCell(i + 1, j);
				Cell cellLeft = getCell(i - 1, j);
				
				float newV = 
						0.25f * (-1.0f + cellUp.v) +
						0.25f * (-1.0f + cellDown.v) +
						0.25f * (-1.0f + cellRight.v) +
						0.25f * (-1.0f + cellLeft.v);
				newVs[i][j] = newV;
			}
		}
		
		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				Cell cell = cells[i][j];
				cell.v = newVs[i][j];
			}
		}
	}

	public void dump() {
		for (int j = 0; j < HEIGHT; ++j) {
			for (int i = 0; i < WIDTH; ++i) {
				cells[i][j].dump();
			}
			System.out.println("");
		}
	}
}
