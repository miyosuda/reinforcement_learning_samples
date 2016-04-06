package reinforcement.mc_blackjack;

/**
 * Displays state values with simple policy that hits only when careds >= 20.
 * 
 * @see https://webdocs.cs.ualberta.ca/~sutton/book/ebook/node51.html
 */
public class MonteCarlo {
	private static final int EPISODES = 500000;
	
	// Simply policy that hits only when cards >= 20.
	private Policy policy = new SimplePolicy();
	private Returns returns = new Returns();
	private BlackJackEnvironment environment = new BlackJackEnvironment(policy);

	public MonteCarlo() {
	}

	public void execute() {
		for (int i = 0; i < EPISODES; i++) {
			// Run one episode and receive reward (-1,0,1)
			int reward = environment.runEpisode();
			
			// In BlackJack, the same state never recurs within one episode, 
			// so there is no difference between first-visit and every-visit MC methods.
			for (int j = 0; j < environment.getPlayHistoryEntrySize(); j++) {
				State state = environment.getPlayHistoryEntry(j).state;
				// Sum up reward for each state in play history.
				returns.addReward(state, reward);
			}
		}
	}

	public String getPlotString(boolean usableAce) {
		return returns.getPlotString(usableAce);
	}

	public void process() {
		execute();
		System.out.println("Usable Ace");
		System.out.println(getPlotString(true));
		
		System.out.println("");		
		System.out.println("No Usable Ace");
		System.out.println(getPlotString(false));
	}
}
