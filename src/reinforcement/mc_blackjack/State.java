package reinforcement.mc_blackjack;

public class State {
	public static final int PLAYER_SUM_SIZE = 21 - 12 + 1; // 12~21
	public static final int DEALER_CARD_SIZE = 10 - 1 + 1; // 1~11
	
	public int playerSum; // Player card sum. (12~21)
	public int dealerCard; // Dealer's first card. (1~10)
	public boolean usableAce; // Whether player has usable ace

	public State(int player, int dealer, boolean ace) {
		playerSum = player;
		dealerCard = dealer;
		usableAce = ace;
	}
}
