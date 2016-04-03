package reinforcement.q_grid;

import java.util.Random;

public class Decision {
	private static final float EPSILON = 0.1f;			
	private static final float TAU = 1.0f;
	
	private static Random random = new Random();
	
	public static Action chooseGreedy(Cell cell) {
		Action[] actions = Action.values();
		
		float[] qs = cell.getQs();
		float maxQ = -Float.MAX_VALUE;
		int maxQIndex = -1;
		
		for (int i = 0; i < actions.length; ++i) {
			float q = qs[i];
			if (q >= maxQ) {
				maxQIndex = i;
				maxQ = q;
			}
		}
		return actions[maxQIndex];
	}
	
	public static Action chooseEpsilonGreedy(Cell cell) {
		float r = random.nextFloat();

		if (r < EPSILON) {
			Action[] actions = Action.values();			
			return actions[random.nextInt(actions.length)];
		} else {
			return chooseGreedy(cell);
		}
	}
	
	public static Action chooseSoftMax(Cell cell) {
		Action[] actions = Action.values();
		
		float[] qs = cell.getQs();
		float[] ps = new float[qs.length];
		
		float maxP = -Float.MAX_VALUE;
		
		for (int i = 0; i < qs.length; ++i) {
			float q = qs[i];
			float p = q / TAU;
			if( p > maxP) {
				maxP = p;
			}
		}
		
		float sum = 0.0f;

		for (int i = 0; i < qs.length; ++i) {
			float q = qs[i];
			float p = q / TAU;
			float pp = (float)Math.exp(p - maxP);
			sum += pp;
			ps[i] = pp;
		}
		
		for (int i = 0; i < ps.length; ++i) {
			ps[i] /= sum;
		}		
		
		float r = random.nextFloat();
		
		float tmpSum = 0.0f;
		for(int i=0; i<actions.length; ++i)  {
			tmpSum += ps[i];
			if( tmpSum >= r ) {
				return actions[i];
			}
		}
		return actions[0];
	}
}
