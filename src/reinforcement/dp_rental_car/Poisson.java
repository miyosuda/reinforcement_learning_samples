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

	public double getRequestRate(int usingCarSize, int availableCarSize) {
		if (usingCarSize >= availableCarSize) {
			double sum = 0.0f;
			for (int i = 0; i < usingCarSize; ++i) {
				sum += rates[i];
			}
			return 1.0 - sum;
		} else {
			return rates[usingCarSize];
		}
	}
	
	// TODO: 結局上と同じになっているので、統一すること.
	public double getReturnRate(int returningCarSize, int remainingSlotSize) {
		if (returningCarSize >= remainingSlotSize) {
			double sum = 0.0f;
			for (int i = 0; i < returningCarSize; ++i) {
				sum += rates[i];
			}
			return 1.0 - sum;
		} else {
			return rates[returningCarSize];
		}
	}
}
