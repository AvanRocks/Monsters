import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

class Enemy extends Character {
  private final BufferedImage[][] attack = new BufferedImage[4][6];
  private int attackStep = 0;
  private int attackDir;
  private int steps = 0;
  private int blocksTravelled = 0;
  private ArrayList<ArrayList<Boolean>> visited;
  private ArrayList<ArrayList<Integer>> direction;
  private ArrayList<Integer> path;
  private boolean isAttacking;
  private final CardLayout cards;
  private final Container pane;
  private final MutableBoolean gameIsActive;

  Enemy(
    int x,
    int y,
    BufferedImage spriteSheet,
    Map map,
    CardLayout cards,
    Container pane,
    MutableBoolean gameIsActive
  ) {
    super(x, y, spriteSheet, map);
    this.cards = cards;
    this.pane = pane;
    this.gameIsActive = gameIsActive;

    // load attacking animation of enemy
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 6; ++j) {
        attack[i][j] =
          spriteSheet
            .getSubimage(576 + j * 192, i * 192, 192, 192)
            .getSubimage(64, 64, 64, 64);
      }
    }

    resetArrays(map);
    calcPath();
  }

  private void resetArrays(Map map) {
    // reset visited
    visited = new ArrayList<>();
    for (int i = 0; i < map.getNumRows(); ++i) {
      visited.add(new ArrayList<>(map.getNumColumns()));
      for (int j = 0; j < map.getNumColumns(); ++j) visited.get(i).add(false);
    }

    // reset direction
    direction = new ArrayList<>();
    for (int i = 0; i < map.getNumRows(); ++i) {
      direction.add(new ArrayList<>(map.getNumColumns()));
      for (int j = 0; j < map.getNumColumns(); ++j) direction.get(i).add(0);
    }
  }

  private boolean visit(int x, int y, int dir, Map map) {
    if (x < 0 || x >= map.getNumColumns() || y < 0 || y >= map.getNumRows()) {
      return false;
    }
    if (visited.get(y).get(x)) {
      return false;
    }

    // Checking if there exists a wall between the opposite direction
    if ((map.getEdge(x, y) & (1 << Direction.getOpposite(dir))) == 0) {
      return false;
    }

    visited.get(y).set(x, true);
    direction.get(y).set(x, dir);
    return true;
  }

  // find shortest path to player using breadth-first search
  private void calcPath() {
    Map map = getMap();
    resetArrays(map);

    Queue<Coordinate> queue = new LinkedList<>();
    int startX = (int) Math.round(getX() );
    int startY = (int) Math.round(getY() );

    queue.add(new Coordinate(startX, startY));
    visited.get(startY).set(startX, true);

    Coordinate s;
    while ((s = queue.poll()) != null) {
      if (s.equals(map.getPlayerPos())) break;
      if (visit(s.getX() + 1, s.getY(), Direction.RIGHT, map)) {
        queue.add(new Coordinate(s.getX() + 1, s.getY()));
      }
      if (visit(s.getX() - 1, s.getY(), Direction.LEFT, map)) {
        queue.add(new Coordinate(s.getX() - 1, s.getY()));
      }
      if (visit(s.getX(), s.getY() + 1, Direction.DOWN, map)) {
        queue.add(new Coordinate(s.getX(), s.getY() + 1));
      }
      if (visit(s.getX(), s.getY() - 1, Direction.UP, map)) {
        queue.add(new Coordinate(s.getX(), s.getY() - 1));
      }
    }
    path = new ArrayList<>();
    int x = map.getPlayerPos().getX();
    int y = map.getPlayerPos().getY();

    try {
      while (!(x == startX && y == startY)) {
        path.add(direction.get(y).get(x));
        switch (direction.get(y).get(x)) {
          case Direction.UP:
            ++y;
            break;
          case Direction.DOWN:
            --y;
            break;
          case Direction.LEFT:
            ++x;
            break;
          case Direction.RIGHT:
            --x;
            break;
        }
      }
    } catch (Exception ignored) {}
    Collections.reverse(path);
  }

  @Override
  public void updatePos() {
    Map map = getMap();
    double speed = getSpeed();

    if (path.size() == 0) {
      calcPath();
    }

    if (steps == (int) (1 / speed)) {
      blocksTravelled++;

      steps = 0;

      if (
        blocksTravelled == 1 ||
        blocksTravelled >= path.size() ||
        path.size() == 0
      ) {
        calcPath();
        blocksTravelled = 0;
      }
    }

    Coordinate pos = new Coordinate(
      (int) Math.round(getX() ),
      (int) Math.round(getY() )
    );

    int playerX = map.getPlayerPos().getX();
    int playerY = map.getPlayerPos().getY();

    if (pos.equals(map.getPlayerPos())) {
      isAttacking = true;
      attackDir = getPrevDir();
    }
    // if the player is adjacent AND there is no wall in between
    else if (
      pos.isAdjacentTo(map.getPlayerPos()) &&
      (
        map.getEdge(playerX, playerY) &
        (1 << Direction.getOpposite(pos.compareTo(map.getPlayerPos())))
      ) == 1
    ) {
      isAttacking = true;
      attackDir = pos.compareTo(map.getPlayerPos());
    } else {
      steps++;
      isAttacking = false;

      if (path.size() != 0) {
        walk(path.get(blocksTravelled));
        checkCollision(path.get(blocksTravelled));
      }
    }
  }

  @Override
  public BufferedImage getImage() {
    if (!isAttacking) {
      attackStep = 0;
      return super.getImage();
    } else {
      if (attackStep == 5) {
        gameIsActive.setVal(false);
        cards.show(pane, "game-over");
        attackStep = 0;
      }
      return attack[attackDir][attackStep++];
    }
  }

  @Override
  public void drawImage(Graphics g) {

    if (!isAttacking) {
      super.drawImage(g);
    } else {
      Map map = getMap();
      g.drawImage(
        getImage(),
        (int) (getX() * map.getBlockWidth()),
        (int) (getY() * map.getBlockHeight()),
        (int) (map.getBlockWidth()),
        (int) (map.getBlockHeight()),
        null
      );
    }

    g.setColor(Color.orange);
    int curX =
      (int) (getX() * getMap().getBlockWidth() + getMap().getBlockWidth() / 2);
    int curY =
      (int) (getY() * getMap().getBlockHeight() + getMap()
        .getBlockHeight() / 2);
    for (Integer d : path) {
      g.drawLine(curX, curY, curX + 10, curY + 10);

      switch (d) {
        case Direction.UP:
          curY -= getMap().getBlockHeight();
          break;
        case Direction.DOWN:
          curY += getMap().getBlockHeight();
          break;
        case Direction.LEFT:
          curX -= getMap().getBlockWidth();
          break;
        case Direction.RIGHT:
          curX += getMap().getBlockWidth();
          break;
      }
    }
  }
}
