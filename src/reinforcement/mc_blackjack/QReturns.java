package reinforcement.mc_blackjack;


public class QReturns {
	// [playerSum][dealerCard][usableAce][action]
	private int[][][][] rewardSums = new int[State.PLAYER_SUM_SIZE][State.DEALER_CARD_SIZE][2][2];
	private int[][][][] rewardCounts = new int[State.PLAYER_SUM_SIZE][State.DEALER_CARD_SIZE][2][2];
	
	public void addReward(PlayHistoryEntry entry, int reward) {
		int playerSumIndex = getPlayerSumIndex(entry.state.playerSum);
		int dealerCardIndex = getDealerCardIndex(entry.state.dealerCard);
		int usableAceIndex = getBoolIndex(entry.state.usableAce);
		int actionIndex = getBoolIndex(entry.action);

		rewardSums[playerSumIndex][dealerCardIndex][usableAceIndex][actionIndex] += reward;
		rewardCounts[playerSumIndex][dealerCardIndex][usableAceIndex][actionIndex] += 1;		
	}

	private double getAverageReturn(int playerSum, int dealerCard, boolean usableAce, boolean action) {
		int playerSumIndex = getPlayerSumIndex(playerSum);
		int dealerCardIndex = getDealerCardIndex(dealerCard);
		int usableAceIndex = getBoolIndex(usableAce);
		int actionIndex = getBoolIndex(action);

		int rewardSum = rewardSums[playerSumIndex][dealerCardIndex][usableAceIndex][actionIndex];
		int count = rewardCounts[playerSumIndex][dealerCardIndex][usableAceIndex][actionIndex];

		if (count == 0) {
			return 0.0;
		}
		return (double) rewardSum / (double) count;
	}
	
	public boolean getBestAction(State state) {
		double hitReturn = getAverageReturn(state.playerSum, state.dealerCard, state.usableAce, true);
		double stickReturn = getAverageReturn(state.playerSum, state.dealerCard, state.usableAce, false);
		return hitReturn > stickReturn;
	}

	private int getPlayerSumIndex(int playerSum) {
		return playerSum - 12;
	}

	private int getDealerCardIndex(int dealerCard) {
		return dealerCard - 1;
	}
	
	private int getBoolIndex(boolean value) {
		if( value ) {
			return 1;
		} else {
			return 0;
		}
	}

	public String getPlotString(boolean usableAce) {
		StringBuilder sb = new  StringBuilder();
		sb.append("\nPlayer\tDealer\tValue");
		
		for(int i=12; i<=21; ++i) {
			for(int j=1; j<=10; ++j) {
				double hitReturn = getAverageReturn(i, j, usableAce, true);
				double stickReturn = getAverageReturn(i, j, usableAce, false);
				double r = hitReturn;
				if( stickReturn > r ) {
					r = stickReturn;
				}
				sb.append("\n" + i + "\t" + j + "\t" + r);
			}
		}
		return sb.toString();
	}
}
