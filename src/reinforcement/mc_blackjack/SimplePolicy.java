package reinforcement.mc_blackjack;

public class SimplePolicy implements Policy {
	@Override
	public boolean doesHit(State state, boolean firstStep) {
		if (state.playerSum < 20) {
			// Hit if player sum is below 20.
			return true;
		} else {
			// Stick if player sum is 20 or 21.
			return false;
		}
	}
}
