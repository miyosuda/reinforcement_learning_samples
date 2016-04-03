package reinforcement.dp_grid;

/**
 * Sample of Dynamic Programming
 * 
 * @see https://webdocs.cs.ualberta.ca/~sutton/book/ebook/node41.html
 */
class Main {
	public static void main(String args[]) {
		World world = new World();
		world.process();
		world.dump();
	}
}
