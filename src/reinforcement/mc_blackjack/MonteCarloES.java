package reinforcement.mc_blackjack;

/**
 * Policy improvement with Monte Carlo ES method.
 * 
 * @see https://webdocs.cs.ualberta.ca/~sutton/book/ebook/node53.html
 */
public class MonteCarloES {
	private static final int EPISODES = 500000;	

	private EsPolicy policy = new EsPolicy();
	// State-Action returns
	private QReturns returns = new QReturns();
	private BlackJackEnvironment environment = new BlackJackEnvironment(policy);

	public MonteCarloES() {
	}

	public void execute() {
		for (int i = 0; i < EPISODES; i++) {
			int reward = environment.runEpisode();

			for (int j = 0; j < environment.getPlayHistoryEntrySize(); j++) {
				PlayHistoryEntry playHistoryEntry = environment.getPlayHistoryEntry(j);
				returns.addReward(playHistoryEntry, reward);
			}
			
			// Improve policy
			for (int j = 0; j < environment.getPlayHistoryEntrySize(); j++) {
				PlayHistoryEntry playHistoryEntry = environment.getPlayHistoryEntry(j);
				policy.addStateActionPolicy(playHistoryEntry.state, returns.getBestAction(playHistoryEntry.state));
			}
		}
	}

	public String getPlotString(boolean usableAce) {
		return policy.getPlotString(usableAce);
		//return returns.plot(usableAce);
	}

	public void process() {
		execute();
		System.out.println("Useable Ace");
		System.out.println(getPlotString(true));

		System.out.println("");
		System.out.println("No Useable Ace");
		System.out.println(getPlotString(false));
	}
}
