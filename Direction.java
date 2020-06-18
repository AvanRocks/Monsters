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
}
