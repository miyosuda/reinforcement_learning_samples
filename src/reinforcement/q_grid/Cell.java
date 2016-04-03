package reinforcement.q_grid;

public class Cell {
	// Q values for UP, DOWN, LEFT, RIGHT action
	private float qs[] = new float[4];
	
	private boolean end;
	private boolean wall;

	public Cell() {
	}

	public boolean isEnd() {
		return end;
	}

	public boolean isWall() {
		return wall;
	}

	public void setEnd() {
		end = true;
	}

	public void setWall() {
		wall = true;
	}
	
	public float[] getQs() {
		return qs;
	}

	public float getQ(Action action) {
		int actionIndex = action.ordinal();
		return qs[actionIndex];
	}

	public void addQ(float deltaQ, Action action) {
		int actionIndex = action.ordinal();
		qs[actionIndex] += deltaQ;
	}

	public float getMaxQ() {
		float maxQ = -Float.MAX_VALUE;

		for (float q : qs) {
			if (q > maxQ) {
				maxQ = q;
			}
		}
		return maxQ;
	}

	public void dump() {
		float maxQ = getMaxQ();
		System.out.print(maxQ);
		System.out.print(" ");
	}
}
