package reinforcement.mc_blackjack;

/**
 * Black Jack with Monte Carlo method.
 * 
 * @see https://webdocs.cs.ualberta.ca/~sutton/book/ebook/node51.html
 * @see https://webdocs.cs.ualberta.ca/~sutton/book/ebook/node53.html
 */
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
