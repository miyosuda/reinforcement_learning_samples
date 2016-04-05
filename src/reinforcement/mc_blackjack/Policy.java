package reinforcement.mc_blackjack;

public interface Policy {
	boolean doesHit(State state, boolean firstStep);
}
