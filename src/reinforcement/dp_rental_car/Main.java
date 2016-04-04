package reinforcement.dp_rental_car;

public class Main {
	public static final int MOVING_CAR_MAX = 5;
	public static final int CAR_NUM_MAX = 20;
	private static final double GAMMA = 0.9;
	private static final double DELTA_VALUE_THRESHOLD = 1.0;
	private static final int COST_PER_MOVE = 2;
	private static final int REWARD_PER_CUSTOMER = 10;
	private static final double REQUEST_LAMBDA_A = 3.0;
	private static final double REQUEST_LAMBDA_B = 4.0;
	private static final double RETURN_LAMBDA_A = 3.0;
	private static final double RETURN_LAMBDA_B = 2.0;

	private State[][] states = new State[CAR_NUM_MAX + 1][CAR_NUM_MAX + 1];

	public static void main(String args[]) {
		Main main = new Main();
		
		// Iterate 4 times for test
		for(int i=0; i<4; ++i) {
			main.process();
		}
		
		System.out.println("---[Action]---");
		main.dumpAction();
		
		System.out.println("---[Value]---");
		main.dumpValue();
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
	 *            Car size at branch A after Jack moved cars at night
	 * @param afterCarB
	 *            Car size at branch A after Jack moved cars at night
	 */
	private double estimateValue(int afterCarA, int afterCarB) {
		// TODO: Check whether this probablity & reward calculaition is correct.
		
		double value = 0.0;

		for (int customerA = 0; customerA <= afterCarA; ++customerA) {
			// customerA -> customer num at A
			double rateCustomerA = poissionRequestA.getRate(customerA, afterCarA);
			// slotA -> remaining slot size at A
			int slotA = CAR_NUM_MAX - (afterCarA - customerA);

			for (int returnA = 0; returnA <= slotA; ++returnA) {
				// retuenA -> Returned car num at A
				double rateReturnA = poissionReturnA.getRate(returnA, slotA);

				for (int customerB = 0; customerB <= afterCarB; ++customerB) {
					// customerB -> Customer num at B
					double rateCustomerB = poissionRequestB.getRate(customerB, afterCarB);

					int slotB = CAR_NUM_MAX - (afterCarB - customerB);
					// slotB -> Remaining slot size at B
					for (int returnB = 0; returnB <= slotB; ++returnB) {
						// returnB -> Returned car num at B
						double rateReturnB = poissionReturnB.getRate(returnB, slotB);

						// Probability to become this pattern
						double rate = rateCustomerA * rateReturnA * rateCustomerB * rateReturnB;

						// Reward given at this pattern
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

		double newValues[][] = new double[CAR_NUM_MAX + 1][CAR_NUM_MAX + 1];
		while(true) {
			for (int i = 0; i < CAR_NUM_MAX + 1; ++i) {
				for (int j = 0; j < CAR_NUM_MAX + 1; ++j) {
					State state = states[i][j];
					// Calculate new value following current policy.
					double newValue = estimateActionValue(state, state.action);
					newValues[i][j] = newValue;
				}
			}

			double maxDeltaValue = -Double.MAX_VALUE;

			for (int i = 0; i < CAR_NUM_MAX + 1; ++i) {
				for (int j = 0; j < CAR_NUM_MAX + 1; ++j) {
					State state = states[i][j];
					double deltaValue = Math.abs(newValues[i][j] - state.v);
					if (deltaValue > maxDeltaValue) {
						maxDeltaValue = deltaValue;
					}
					// Update value
					state.v = newValues[i][j];
				}
			}

			// Loop until value difference becomes below threshold.
			if( maxDeltaValue < DELTA_VALUE_THRESHOLD ) {
				break;
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

				// Choose best action which has best estimated value.
				for (int action = movingCarMin; action <= movingCarMax; ++action) {
					double newValue = estimateActionValue(state, action);
					if (newValue > bestValue) {
						bestValue = newValue;
						bestAction = action;
					}
				}

				// Update action
				state.action = bestAction;
			}
		}
	}

	private void dumpAction() {
		for (int i = 0; i < CAR_NUM_MAX + 1; ++i) {
			for (int j = 0; j < CAR_NUM_MAX + 1; ++j) {
				State state = states[i][j];
				state.dumpAction();
			}
			System.out.println("");
		}
	}
	
	private void dumpValue() {
		for (int i = 0; i < CAR_NUM_MAX + 1; ++i) {
			for (int j = 0; j < CAR_NUM_MAX + 1; ++j) {
				State state = states[i][j];
				state.dumpValue();
			}
			System.out.println("");
		}
	}	
}
