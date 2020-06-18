public class Direction {
  public static final int UP = 0;
  public static final int LEFT = 1;
  public static final int DOWN = 2;
  public static final int RIGHT = 3;

  public static int getOpposite(int dir) {
    return switch (dir) {
      case UP -> DOWN;
      case DOWN -> UP;
      case LEFT -> RIGHT;
      case RIGHT -> LEFT;
      default -> -1;
    };
  }
}
