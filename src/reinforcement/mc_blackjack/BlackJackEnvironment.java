package reinforcement.mc_blackjack;

import java.util.ArrayList;
import java.util.Random;

public class BlackJackEnvironment {
	private Random random = new Random();

	private ArrayList<Integer> dealerCards = new ArrayList<>();
	private ArrayList<Integer> playerCards = new ArrayList<>();
	private Policy policy;
	private ArrayList<PlayHistoryEntry> playHistory = new ArrayList<>();

	public BlackJackEnvironment(Policy policy) {
		this.policy = policy;
	}

	private void initialize() {
		playerCards.clear();
		dealerCards.clear();
		playHistory.clear();

		// Player draws two cards.
		draw(playerCards);
		draw(playerCards);

		// Dealer draws two cards.
		draw(dealerCards);
		draw(dealerCards);
	}

	private State getCurrentState() {
		int playerSum = getPlayerSum();
		// Only first card of dealer is visible to player.
		int dealerCard = dealerCards.get(0);
		boolean usableAce = hasUseableAce(playerCards);
		return new State(playerSum, dealerCard, usableAce);
	}

	public int runEpisode() {
		initialize();

		boolean firstStep = true;

		while (true) {
			// Even when player has 21 at the first time,
			// policy will decide hit or stick.

			while (getPlayerSum() < 12) {
				// If player's sum is below 12, draw cards without decision.
				draw(playerCards);
				continue;
			}

			if (doesLearnerHit(firstStep)) {
				// When player decide to hit (draw card).

				// Store current state-action pair.
				playHistory.add(new PlayHistoryEntry(getCurrentState(), true));

				// Player draws.
				draw(playerCards);

				int playerSum = getPlayerSum();

				if (playerSum > 21) {
					// Playe went bust.
					break;
				}

			} else {
				// When player decide to stick. (not drawing card)
				// Store current state-action pair.				
				playHistory.add(new PlayHistoryEntry(getCurrentState(), false));
				break;
			}

			firstStep = false;
		}

		// Dealer draws.
		while (true) {
			int dealerSum = getDealerSum();
			if (dealerSum < 17) {
				// Dealer draws card if card sum is below 17.
				draw(dealerCards);
			} else {
				break;
			}
		}

		int playerSum = getPlayerSum();
		int dealerSum = getDealerSum();

		if (playerSum > 21) {
			// Player went bust and lost.
			return -1;
		} else if (dealerSum > 21) {
			// Dealer went bust and lost.
			return 1;
		} else if (playerSum > dealerSum) {
			// Player won.
			return 1;
		} else if (playerSum < dealerSum) {
			// Player lost.
			return -1;
		} else {
			// Draw.
			return 0;
		}
	}

	private void draw(ArrayList<Integer> cards) {
		int card = random.nextInt(13) + 1;
		if (card >= 11) {
			// Face cards are treated as 10
			card = 10;
		}
		cards.add(card);
	}

	private int countCards(ArrayList<Integer> cards) {
		// card num of non-ace
		int nonAceNum = 0;
		// card num of ace
		int aceNum = 0;

		for (int card : cards) {
			if (card == 1) {
				aceNum++;
			} else {
				nonAceNum += card;
			}
		}
		if (aceNum > 0) {
			// If player has more than one ace
			if (nonAceNum + 11 + (aceNum - 1) <= 21) {
				// If one ace is treated as 11 and the sum doesn't be greater than 21.
				nonAceNum += 11 + (aceNum - 1);
			} else {
				// If it becomes greater than 21, ace is treated as 1.
				nonAceNum += aceNum;
			}
		}
		return nonAceNum;
	}

	private int getPlayerSum() {
		return countCards(playerCards);
	}

	private int getDealerSum() {
		return countCards(dealerCards);
	}

	private boolean hasUseableAce(ArrayList<Integer> cards) {
		// card num of non-ace		
		int nonAceNum = 0;
		// card num of ace
		int aceNum = 0;

		for (int card : cards) {
			if (card == 1) {
				aceNum++;
			} else {
				nonAceNum += card;
			}
		}
		if (aceNum > 0 && nonAceNum + 11 + (aceNum - 1) <= 21) {
			// If one ace is treated as 11 and the sum doesn't go greater than 21.			
			// (Then ace can be treated as 11)
			return true;
		} else {
			return false;
		}
	}

	private boolean doesLearnerHit(boolean firstStep) {
		return policy.doesHit(getCurrentState(), firstStep);
	}

	public int getPlayHistoryEntrySize() {
		return playHistory.size();
	}

	public PlayHistoryEntry getPlayHistoryEntry(int index) {
		return playHistory.get(index);
	}
}
