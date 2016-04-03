package reinforcement.q_grid;

public class Agent {
	private static final float ALPHA = 0.1f;
	private static final float GAMMA = 0.95f;

	private enum PolicyType {
		EPSILON_GREEDY, // Epsilon Greedy Policy
		SOFT_MAX, // Soft Max Policy
	}

	private World world;

	private int posX;
	private int posY;

	private int episodeReward = 0;

	private PolicyType policyType = PolicyType.SOFT_MAX;

	public Agent(World world) {
		this.world = world;
		reset();
	}

	private void reset() {
		posX = 0;
		posY = 2;
		episodeReward = 0;
	}

	private Action chooseAction(Cell cell) {
		if (policyType == PolicyType.EPSILON_GREEDY) {
			return Decision.chooseEpsilonGreedy(cell);
		} else {
			return Decision.chooseSoftMax(cell);
		}
	}

	private Cell applyAction(Action action) {
		int nextX = posX;
		int nextY = posY;

		switch (action) {
		case UP:
			nextY -= 1;
			break;
		case DOWN:
			nextY += 1;
			break;
		case LEFT:
			nextX -= 1;
			break;
		case RIGHT:
			nextX += 1;
			break;
		}

		nextX = Math.min(Math.max(0, nextX), World.WIDTH - 1);
		nextY = Math.min(Math.max(0, nextY), World.HEIGHT - 1);

		Cell nextCell = world.getCell(nextX, nextY);

		if (nextCell.isWall()) {
			nextX = posX;
			nextY = posY;
			nextCell = world.getCell(nextX, nextY);
		}

		posX = nextX;
		posY = nextY;

		return nextCell;
	}

	public void processQLearningEpisode() {
		while (true) {
			Cell cell = world.getCell(posX, posY);
			Action action = chooseAction(cell);
			Cell nextCell = applyAction(action);
			boolean ended = nextCell.isEnd();

			int reward = -1;
			if (ended) {
				reward = 0;
			}
			updateStepQ((float) reward, cell, action, nextCell);

			if (ended) {
				break;
			}
		}

		System.out.println("episode reward=" + episodeReward);

		reset();
	}

	private void updateStepQ(float reward, Cell cell, Action action, Cell nextCell) {
		float deltaQ;

		if (!nextCell.isEnd()) {
			deltaQ = reward + Agent.GAMMA * nextCell.getMaxQ() - cell.getQ(action);
		} else {
			deltaQ = reward - cell.getQ(action);
		}

		cell.addQ(ALPHA * deltaQ, action);
	}

	public void processSarsaLearningEpisode() {
		Cell cell = world.getCell(posX, posY);
		Action action = chooseAction(cell);

		while (true) {
			Cell nextCell = applyAction(action);
			boolean ended = nextCell.isEnd();

			int reward = -1;
			if (ended) {
				reward = 0;
			}

			Action nextAction = chooseAction(nextCell);
			updateStepSarsa((float) reward, cell, action, nextCell, nextAction);
			if (ended) {
				break;
			}

			cell = nextCell;
			action = nextAction;
		}

		System.out.println("episode reward=" + episodeReward);

		reset();
	}

	private void updateStepSarsa(float reward, Cell cell, Action action, Cell nextCell, Action nextAction) {
		float deltaQ;

		if (!nextCell.isEnd()) {
			deltaQ = reward + Agent.GAMMA * nextCell.getQ(nextAction) - cell.getQ(action);
		} else {
			deltaQ = reward - cell.getQ(action);
		}

		cell.addQ(ALPHA * deltaQ, action);
	}
}
