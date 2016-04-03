package reinforcement.q_grid;

/*
    S = Start
    G = Goal
    W = Wall
    
    + + + + + + + W G
    + + W + + + + W +
    S + W + + + + W +
    + + W + + + + + +
    + + + + + W + + +
    + + + + + + + + +
    
    At evey step, agent will recive -1 reward until it reaches goal.
 */
public class World {
	public static final int WIDTH = 9;
	public static final int HEIGHT = 6;

	private Cell cells[][] = new Cell[WIDTH][HEIGHT];

	public World() {
		for (int i = 0; i < WIDTH; ++i) {
			for (int j = 0; j < HEIGHT; ++j) {
				cells[i][j] = new Cell();
			}
		}

		cells[8][0].setEnd();

		cells[2][1].setWall();
		cells[2][2].setWall();
		cells[2][3].setWall();

		cells[5][4].setWall();

		cells[7][0].setWall();
		cells[7][1].setWall();
		cells[7][2].setWall();
	}

	public Cell getCell(int x, int y) {
		return cells[x][y];
	}

	public float[] getQRange() {
		float minQ = Float.MAX_VALUE;
		float maxQ = -Float.MAX_VALUE;

		for (int j = 0; j < HEIGHT; ++j) {
			for (int i = 0; i < WIDTH; ++i) {
				float q = cells[i][j].getMaxQ();
				if (q > maxQ) {
					maxQ = q;
				} else if (q < minQ) {
					minQ = q;
				}
			}
		}

		return new float[] { minQ, maxQ };
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
