public class Direction {
	public final static int UP = 0;
	public final static int LEFT = 1;
	public final static int DOWN = 2;
	public final static int RIGHT = 3;

	public static int getOpposite(int dir) {
		switch (dir) {
			case UP: return DOWN;
			case DOWN: return UP;
			case LEFT: return RIGHT;
			case RIGHT: return LEFT;
		}
		return -1;
	}
}
