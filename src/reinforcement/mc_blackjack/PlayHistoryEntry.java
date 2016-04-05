package reinforcement.mc_blackjack;

/**
 * State and Action pair
 */
public class PlayHistoryEntry {
	public State state;
	public boolean action; // true=Hit(Twist), false=Stick

	public PlayHistoryEntry(State state, boolean action) {
		this.state = state;
		this.action = action;
	}
}
