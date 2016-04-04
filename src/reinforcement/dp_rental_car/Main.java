package reinforcement.dp_rental_car;

public class Main {
	private static final double REQUEST_LAMBDA_A = 3.0;
	private static final double REQUEST_LAMBDA_B = 4.0;

	private static final double RETURN_LAMBDA_A = 3.0;
	private static final double RETURN_LAMBDA_B = 2.0;

	public static final int CAR_NUM_MAX = 20;

	private static final double GAMMA = 0.9;

	private static final int COST_PER_MOVE = 2;
	private static final int REWARD_PER_CUSTOMER = 10;

	private State[][] states = new State[CAR_NUM_MAX + 1][CAR_NUM_MAX + 1];

	public static void main(String args[]) {
		Main main = new Main();
		main.process();
		main.dump();
	}

	private Poisson poissionRequestA = new Poisson(REQUEST_LAMBDA_A, CAR_NUM_MAX + 1);
	private Poisson poissionReturnA = new Poisson(RETURN_LAMBDA_A, CAR_NUM_MAX + 1);
	private Poisson poissionRequestB = new Poisson(REQUEST_LAMBDA_B, CAR_NUM_MAX + 1);
	private Poisson poissionReturnB = new Poisson(RETURN_LAMBDA_B, CAR_NUM_MAX + 1);

	private Main() {
		for (int i = 0; i < CAR_NUM_MAX + 1; ++i) {
			for (int j = 0; j < CAR_NUM_MAX + 1; ++j) {
				states[i][j] = new State(i, j);
			}
		}
	}

	/**
	 * @param afterCarA
	 *            夜間にジャックが移動した後の支店Aの車の数
	 * @param afterCarB
	 *            夜間にジャックが移動した後の支店Bの車の数
	 */
	private double estimateValue(int afterCarA, int afterCarB) {
		double value = 0.0;
		
		for (int customerA = 0; customerA <= afterCarA; ++customerA) {
			// Aに来る客の人数
			double rateCustomerA = poissionRequestA.getRequestRate(customerA, afterCarA);
			// Aに返却可能な残りスロットの数
			int slotA = CAR_NUM_MAX - (afterCarA - customerA);

			for (int returnA = 0; returnA <= slotA; ++returnA) {
				// retuenA -> Aに返却しに来た客の数
				double rateReturnA = poissionReturnA.getReturnRate(returnA, slotA);

				for (int customerB = 0; customerB <= afterCarB; ++customerB) {
					// Bに来る客の人数
					double rateCustomerB = poissionRequestB.getRequestRate(customerB, afterCarB);

					// Bに返却可能な残りスロットの数
					int slotB = CAR_NUM_MAX - (afterCarB - customerB);
					for (int returnB = 0; returnB <= slotB; ++returnB) {
						// returnB -> Bに返却しに来た客の数
						double rateReturnB = poissionReturnB.getReturnRate(returnB, slotB);
						
						// このパターンになる確率
						double rate = rateCustomerA * rateReturnA * rateCustomerB * rateReturnB;
						
						// このパターンで受け取る報酬
						double reward = (customerA + customerB) * REWARD_PER_CUSTOMER;
						
						int newCarA = afterCarA - customerA + returnA;
						int newCarB = afterCarB - customerB + returnB;
						
						State newState = states[newCarA][newCarB];
						
						double dvalue = rate * (reward + GAMMA * newState.v);
						value += dvalue;
					}
				}
			}
		}
		return value;
	}
	
	private double estimateActionValue(State state, int action) {
		int movingCost = -Math.abs(action) * COST_PER_MOVE;

		int afterCarA = state.carA - action;
		int afterCarB = state.carB + action;
		
		double newValue = estimateValue(afterCarA, afterCarB) + movingCost;
		return newValue;
	}
	
	private void process() {
		// Update value
		double newValues[][] = new double[CAR_NUM_MAX+1][CAR_NUM_MAX+1];
		
		for (int i = 0; i < CAR_NUM_MAX + 1; ++i) {
			for (int j = 0; j < CAR_NUM_MAX + 1; ++j) {
				State state = states[i][j];
				double newValue = estimateActionValue(state, state.action);
				newValues[i][j] = newValue;
			}
		}
		
		// Update policy
		for (int i = 0; i < CAR_NUM_MAX + 1; ++i) {
			for (int j = 0; j < CAR_NUM_MAX + 1; ++j) {
				State state = states[i][j];
				int movingCarMin = state.getMovingActionMin();
				int movingCarMax = state.getMovingActionMax();
				
				int bestAction = -1;
				double bestValue = -Double.MAX_VALUE;

				for (int action = movingCarMin; action <= movingCarMax; ++action) {
					double newValue = estimateActionValue(state, action);
					if( newValue > bestValue ) {
						bestValue = newValue;
						bestAction = action;
					}
				}
				
				state.action = bestAction;
			}
		}
	}
	
	private void dump() {
		for (int i = 0; i < CAR_NUM_MAX + 1; ++i) {
			for (int j = 0; j < CAR_NUM_MAX + 1; ++j) {
				State state = states[i][j];
				state.dump();
			}
			System.out.println("");
		}
	}
}
