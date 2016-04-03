package reinforcement.dp_grid;

public class Cell {
	public float v = 0.0f;

	private boolean end;

	public void setEnd() {
		end = true;
	}

	public boolean isEnd() {
		return end;
	}

	public void dump() {
		System.out.print(v);
		System.out.print(" ");
	}
}
