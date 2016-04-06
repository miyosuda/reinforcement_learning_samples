package reinforcement.mc_blackjack;

import java.util.Random;

public class EsPolicy implements Policy {
	// [playerSum][dealerCard][usableAce]
	// If true, choose hit and otherwise choose stick.
	private boolean[][][] policy = new boolean[State.PLAYER_SUM_SIZE][State.DEALER_CARD_SIZE][2];

	private Random random = new Random();

	public EsPolicy() {
		// As an initial policy, player hits if the player sum is below 20.
		for (int playerSum = 12; playerSum <= 21; playerSum++) {
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				boolean hit = playerSum < 20;

				int playerSumIndex = getPlayerSumIndex(playerSum);
				int dealerCardIndex = getDealerCardIndex(dealerCard);

				// Has usable ace.
				policy[playerSumIndex][dealerCardIndex][1] = hit;
				// Doesn't have usable ace.
				policy[playerSumIndex][dealerCardIndex][0] = hit;
			}
		}
	}

	@Override
	public boolean doesHit(State state, boolean firstStep) {
		if (firstStep) {
			return random.nextBoolean();
		} else {
			int playerSumIndex = getPlayerSumIndex(state.playerSum);
			int dealerCardIndex = getDealerCardIndex(state.dealerCard);
			int usableAceIndex = getBoolIndex(state.usableAce);

			return policy[playerSumIndex][dealerCardIndex][usableAceIndex];
		}
	}

	public void addStateActionPolicy(State state, boolean action) {
		int playerSumIndex = getPlayerSumIndex(state.playerSum);
		int dealerCardIndex = getDealerCardIndex(state.dealerCard);
		int usableAceIndex = getBoolIndex(state.usableAce);

		policy[playerSumIndex][dealerCardIndex][usableAceIndex] = action;
	}

	private int getPlayerSumIndex(int playerSum) {
		return playerSum - 12;
	}

	private int getDealerCardIndex(int dealerCard) {
		return dealerCard - 1;
	}

	private int getBoolIndex(boolean value) {
		if (value) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public String getPlotString(boolean usableAce) {	
		StringBuilder sb = new StringBuilder();

		for (int playerSum = 12; playerSum <= 21; playerSum++) {		
			for (int dealerCard = 1; dealerCard <= 10; dealerCard++) {
				int playerSumIndex = getPlayerSumIndex(playerSum);
				int dealerCardIndex = getDealerCardIndex(dealerCard);
				int usableAceIndex = getBoolIndex(usableAce);
				boolean p = policy[playerSumIndex][dealerCardIndex][usableAceIndex];
				sb.append("\n" + playerSum + "\t" + dealerCard + "\t" + p);
			}
		}

		return sb.toString();
	}
}
