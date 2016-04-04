package reinforcement.dp_rental_car;

public class State {
	public int carA;
	public int carB;
	
	public double v = 0.0;
	public int action = 0;

	public State(int carA, int carB) {
		this.carA = carA;
		this.carB = carB;
	}
	
	public int getMovingActionMax() {
		int availableCarFromA = carA;
		int availableCarToB = Main.CAR_NUM_MAX - carB;
		return Math.min(availableCarFromA, availableCarToB);
	}

	public int getMovingActionMin() {
		int availableCarFromB = carB;
		int availableCarToA = Main.CAR_NUM_MAX - carA;
		return -Math.min(availableCarFromB, availableCarToA);
	}
	
	public void dump() {
		System.out.print("" + action + " ");
	}
}
