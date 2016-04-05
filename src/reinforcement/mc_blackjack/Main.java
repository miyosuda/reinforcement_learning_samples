package reinforcement.mc_blackjack;

public class Main {
	public static void main(String[] args) {
		// Simple MonteCarlo value display
		MonteCarlo app = new MonteCarlo();
		app.process();
		
		// MonteCarloES method policy improvement
		/*
		MonteCarloES app = new MonteCarloES();
		app.process();
		*/
	}
}
