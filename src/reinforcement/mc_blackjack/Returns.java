package reinforcement.mc_blackjack;

public class Returns {
	// [playerSum][dealerCard][usableAce]
	private int[][][] rewardSums = new int[State.PLAYER_SUM_SIZE][State.DEALER_CARD_SIZE][2];
	private int[][][] rewardCounts = new int[State.PLAYER_SUM_SIZE][State.DEALER_CARD_SIZE][2];

	public void addReward(State state, int reward) {
		int playerSumIndex = getPlayerSumIndex(state.playerSum);
		int dealerCardIndex = getDealerCardIndex(state.dealerCard);
		int usableAceIndex = getBoolIndex(state.usableAce);

		rewardSums[playerSumIndex][dealerCardIndex][usableAceIndex] += reward;
		rewardCounts[playerSumIndex][dealerCardIndex][usableAceIndex] += 1;
	}
	
	private double getAverageReturn(int playerSum, int dealerCard, boolean usableAce) {
		int playerSumIndex = getPlayerSumIndex(playerSum);
		int dealerCardIndex = getDealerCardIndex(dealerCard);
		int usableAceIndex = getBoolIndex(usableAce);

		int rewardSum = rewardSums[playerSumIndex][dealerCardIndex][usableAceIndex];
		int count = rewardCounts[playerSumIndex][dealerCardIndex][usableAceIndex];

		if (count == 0) {
			return 0.0;
		}
		return (double) rewardSum / (double) count;
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
				sb.append("\n" + i + "\t" + j + "\t" + getAverageReturn(i, j, usableAce));
			}
		}
		return sb.toString();
	}
}

