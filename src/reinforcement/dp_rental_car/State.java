package reinforcement.dp_rental_car;

public class State {
	public int carA;
	public int carB;
	
	// State value
	public double v = 0.0;
	
	// positive action means from A to B, negative action means from B to A.
	public int action = 0;

	public State(int carA, int carB) {
		this.carA = carA;
		this.carB = carB;
	}
	
	public int getMovingActionMax() {
		int availableCarFromA = carA;
		int availableCarToB = Main.CAR_NUM_MAX - carB;
		int moving = Math.min(availableCarFromA, availableCarToB);
		return Math.min(moving, Main.MOVING_CAR_MAX);
	}

	public int getMovingActionMin() {
		int availableCarFromB = carB;
		int availableCarToA = Main.CAR_NUM_MAX - carA;
		int moving = Math.min(availableCarFromB, availableCarToA);
		return -Math.min(moving, Main.MOVING_CAR_MAX);
	}
	
	public void dumpAction() {
		System.out.print("" + action + " ");
	}
	
	public void dumpValue() {
		System.out.print("" + v + " ");
	}	
}
