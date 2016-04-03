package reinforcement.q_grid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Sample of Q-Learning and SARSA method.
 * 
 * Java version of ugo-nama-kun's orignal sample in
 * https://github.com/ugo-nama-kun/RL_nyu-mon
 */
@SuppressWarnings("serial")
class Main extends JFrame {
	private static final int GRID_WIDTH = 50;
	private static final int EPISODE_SIZE = 300;

	private enum LearningType {
		Q, // Q-Learning
		SARSA // SARSA-Learning
	}

	// private LearningType learningType = LearningType.Q;
	private LearningType learningType = LearningType.SARSA;

	public static void main(String args[]) {
		Main frame = new Main();
		frame.setVisible(true);
	}

	Main() {
		setTitle("Grid World");
		setBounds(100, 100, World.WIDTH * GRID_WIDTH, World.HEIGHT * GRID_WIDTH + 22);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new BorderLayout());
		add(new GridPanel());
	}

	public class GridPanel extends JPanel {
		private World world = new World();

		public GridPanel() {
			process();
		}

		private void process() {
			Agent agent = new Agent(world);

			for (int i = 0; i < EPISODE_SIZE; ++i) {
				if (learningType == LearningType.Q) {
					agent.processQLearningEpisode();
				} else {
					agent.processSarsaLearningEpisode();
				}
			}

			world.dump();
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(GRID_WIDTH * World.WIDTH, GRID_WIDTH * World.HEIGHT);
		}

		private Color calcCellColor(float q, float minQ, float maxQ) {
			float rate = (q - minQ) / (maxQ - minQ);
			Color color = new Color(rate, rate, rate);
			return color;
		}

		private void drawCell(Graphics g, int x, int y, float minQ, float maxQ) {
			Cell cell = world.getCell(x, y);
			Color color;

			if (cell.isEnd()) {
				color = Color.BLUE;
			} else if (cell.isWall()) {
				color = Color.GREEN;
			} else {
				float q = cell.getMaxQ();
				color = calcCellColor(q, minQ, maxQ);
			}
			g.setColor(color);

			int px = GRID_WIDTH * x;
			int py = GRID_WIDTH * y;
			g.fillRect(px, py, GRID_WIDTH, GRID_WIDTH);
		}

		private void drawAction(Graphics g, int x, int y) {
			Cell cell = world.getCell(x, y);
			if (cell.isEnd() || cell.isWall()) {
				return;
			}

			Action action = Decision.chooseGreedy(cell);

			int cx = GRID_WIDTH * x + GRID_WIDTH / 2;
			int cy = GRID_WIDTH * y + GRID_WIDTH / 2;

			g.setColor(Color.RED);
			switch (action) {
			case UP:
				g.drawLine(cx, cy, cx, cy - GRID_WIDTH / 2);
				break;
			case DOWN:
				g.drawLine(cx, cy, cx, cy + GRID_WIDTH / 2);
				break;
			case RIGHT:
				g.drawLine(cx, cy, cx + GRID_WIDTH / 2, cy);
				break;				
			case LEFT:
				g.drawLine(cx, cy, cx - GRID_WIDTH / 2, cy);
				break;
			}
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.setColor(Color.GREEN);
			g.fillRect(0, 0, World.WIDTH * GRID_WIDTH, World.HEIGHT * GRID_WIDTH);

			float[] qRange = world.getQRange();
			float minQ = qRange[0];
			float maxQ = qRange[1];

			for (int i = 0; i < World.WIDTH; ++i) {
				for (int j = 0; j < World.HEIGHT; ++j) {
					drawCell(g, i, j, minQ, maxQ);
					drawAction(g, i, j);
				}
			}
		}
	}
}
