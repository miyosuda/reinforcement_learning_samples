package reinforcement.dp_rental_car;

public class Poisson {
	private double rates[];

	private long factorial(long n) {
		if (n <= 1L) {
			return 1L;
		} else {
			return n * factorial(n - 1);
		}
	}

	public Poisson(double lambda, int max) {
		rates = new double[max];

		for (int i = 0; i < rates.length; ++i) {
			double rate = Math.pow(lambda, i) / (double) factorial(i) * Math.exp(-lambda);
			rates[i] = rate;
		}
	}

	public double getRate(int carSize, int availableSize) {
		if (carSize >= availableSize) {
			double sum = 0.0f;
			for (int i = 0; i < carSize; ++i) {
				sum += rates[i];
			}
			return 1.0 - sum;
		} else {
			return rates[carSize];
		}
	}
}
