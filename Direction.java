public class Direction {
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;

	public static int getOpposite(int dir) {
		switch (dir) {
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			default:
				return -1;
		}
	}

	public static boolean isHorizontal(int dir) {
		switch (dir) {
			case UP:
			case DOWN:
				return false;
			case LEFT:
			case RIGHT:
				return true;
		}
		return true;
	}

	public static boolean isVertical(int dir) {
		return !isHorizontal(dir);
	}
}
