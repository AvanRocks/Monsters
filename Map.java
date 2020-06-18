import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import javax.imageio.ImageIO;

class Map {
  private int numRows;
  private int numColumns;
  private double blockWidth, blockHeight;
  private int level = 0;
  private Coordinate playerPos = new Coordinate(0, 0);
  private final ArrayList<Character> characters;
  private int[] link;
  private int[] size;
  private ArrayList<Line2D.Double> walls = null;
  private Line2D.Double exit;
  private int[][] map;
  // these are actually inverse edges
  // they determine where there are NO walls
  public int[][] edges;
  private final CardLayout cards;
  private final Container pane;
  private final MutableBoolean gameIsActive;

  public enum BlockType {
    EMPTY,
    ENEMY_SPAWN,
    PLAYER_SPAWN,
  }

  // For testing
  public Map(
    double panelWidth,
    double panelHeight,
    CardLayout cards,
    Container pane,
    MutableBoolean gameIsActive
  ) {
    this.cards = cards;
    this.pane = pane;
    this.gameIsActive = gameIsActive;

    updateBlockSize(panelWidth, panelHeight);

    characters = new ArrayList<Character>();
  }

  public void advanceLevel() {
    generateMap();
    level++;
  }

  public BlockType getBlock(int x, int y) {
    switch (map[y][x]) {
      case 0: return BlockType.EMPTY;
      case 1: return BlockType.ENEMY_SPAWN;
      case 2: return BlockType.PLAYER_SPAWN;
      default: return BlockType.EMPTY;
    }
  }

  public Line2D.Double getLineInBetween(int x1, int y1, int x2, int y2) {
    System.out.println(blockHeight + " " + blockWidth);

    if (y1 == y2) {
      if (x2 > x1) {
        System.out.println(new Line2D.Double(
          x2 * blockWidth,
          y1 * blockHeight,
          x2 * blockWidth,
          (y1 + 1) * blockHeight
        ).getBounds2D());
        return new Line2D.Double(
          x2 * blockWidth,
          y1 * blockHeight,
          x2 * blockWidth,
          (y1 + 1) * blockHeight
        );
      } else if (x1 > x2) {
        System.out.println(new Line2D.Double(
          x1 * blockWidth,
          y1 * blockHeight,
          x1 * blockWidth,
          (y1 + 1) * blockHeight
        ).getBounds2D());
        return new Line2D.Double(
          x1 * blockWidth,
          y1 * blockHeight,
          x1 * blockWidth,
          (y1 + 1) * blockHeight
        );
      }
    } else if (x1 == x2) {
      if (y2 > y1) {
        System.out.println(new Line2D.Double(
          x2 * blockWidth,
          y2 * blockHeight,
          (x2 + 1) * blockWidth,
          y2 * blockHeight
        ).getBounds2D());
        return new Line2D.Double(
          x2 * blockWidth,
          y2 * blockHeight,
          (x2 + 1) * blockWidth,
          y2 * blockHeight
        );
      } else if (y1 > y2) {
        System.out.println(new Line2D.Double(
          x2 * blockWidth,
          y1 * blockHeight,
          (x2 + 1) * blockWidth,
          y1 * blockHeight
        ).getBounds2D());
        return new Line2D.Double(
          x2 * blockWidth,
          y1 * blockHeight,
          (x2 + 1) * blockWidth,
          y1 * blockHeight
        );
      }
    }

    return null;
  }

  private void createCharacters() {
    characters.clear();

    // create player
    for (int y = 0; y < getNumRows(); ++y) {
      for (int x = 0; x < getNumColumns(); ++x) if (
        getBlock(x, y) == BlockType.PLAYER_SPAWN
      ) {
        playerPos = new Coordinate(x, y);
        try {
          characters.add(
            new Player(
              playerPos.getX(),
              playerPos.getY(),
              ImageIO.read(new File("images" + File.separator + "player.png")),
              this
            )
          );
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    // create Enemies at their specified starting positions
    for (int y = 0; y < numRows; ++y) {
      for (int x = 0; x < numColumns; ++x) {
        // create enemy
        if (getBlock(x, y) == Map.BlockType.ENEMY_SPAWN) {
          try {
            characters.add(
              new Enemy(
                x,
                y,
                ImageIO.read(
                  new File("images" + File.separator + "enemy-orc.png")
                ),
                this,
                cards,
                pane,
                gameIsActive
              )
            );
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  private int find(int x) {
    while (x != link[x]) x = link[x];
    return x;
  }

  private boolean same(int a, int b) {
    return find(a) == find(b);
  }

  private void unite(int a, int b) {
    a = find(a);
    b = find(b);
    if (size[a] > size[b]) {
      int tmp = a;
      a = b;
      b = tmp;
    }
    link[a] = b;
    size[b] += size[a];
  }

  private void generateMap() {
    numColumns = (int) (Math.random() * 7) + 7;
    numRows = (int) (Math.random() * 7) + 7;

    edges = new int[numRows][numColumns];
    map = new int[numRows][numColumns];

    // Eller's algorithm for maze generation (using Union Find structure)

    int[][] set = new int[2][numColumns];
    link = new int[numColumns * (numRows + 1) + 1];
    size = new int[numColumns * (numRows + 1) + 1];

    // initialize link and size
    for (int i = 1; i < numColumns * numRows + 1; ++i) link[i] = i;
    for (int i = 1; i < numColumns * numRows + 1; ++i) size[i] = 1;

    // initialize first row
    for (int x = 0; x < numColumns; ++x) set[0][x] = x + 1;

    // set number : ArrayList of indices
    HashMap<Integer, ArrayList<Integer>> sets = new HashMap<>();

    for (int y = 0; y < numRows - 1; ++y) {
      // randomly merge adjacent cells which are in different sets
      for (int x = 0; x < numColumns; ++x) {
        if (
          x + 1 < numColumns &&
          !same(set[0][x], set[0][x + 1]) &&
          ((int) (Math.random() * 2) == 0)
        ) {
          unite(set[0][x], set[0][x + 1]);
          edges[y][x] |= (1 << Direction.RIGHT);
          edges[y][x + 1] |= (1 << Direction.LEFT);
        }
      }

      // find all sets
      for (int x = 0; x < numColumns; ++x) {
        int tmp = find(set[0][x]);
        sets.putIfAbsent(tmp, new ArrayList<>());
        sets.get(tmp).add(x);
      }

      // randomly add vertical connections (at least one per set)
      for (Entry<Integer, ArrayList<Integer>> entry : sets.entrySet()) {
        int numOfConnections = (int) (Math.random() * entry.getValue().size()) +
        1;
        for (int i = 0; i < numOfConnections; ++i) {
          int randIdx = (int) (Math.random() * entry.getValue().size());
          set[1][entry.getValue().get(randIdx)] =
            set[0][entry.getValue().get(randIdx)];
          edges[y][entry.getValue().get(randIdx)] |= (1 << Direction.DOWN);
          edges[y + 1][entry.getValue().get(randIdx)] |= (1 << Direction.UP);
          entry.getValue().remove(randIdx);
        }
      }

      // put untouched 2nd row cells in their own set
      for (int x = 0; x < numColumns; ++x) {
        if (set[1][x] == 0) set[1][x] = (y + 1) * getNumColumns() + x + 1;
      }

      // move 2nd row up, and make a new 2nd row
      set[0] = set[1];
      set[1] = new int[numColumns];

      sets.clear();
    }

    // for the last row, merge all distinct adjacent sets
    for (int x = 0; x < numColumns - 1; ++x) {
      if (!same(set[0][x], set[0][x + 1])) {
        unite(set[0][x], set[0][x + 1]);
        edges[numRows - 1][x] |= (1 << Direction.RIGHT);
        edges[numRows - 1][x + 1] |= (1 << Direction.LEFT);
      }
    }

    // make the exit
    int side = (int) (Math.random() * 4);
    int x = -1, y = -1;
    int dir = -1;
    switch (side) {
      // top
      case 0: {
        x = (int) (Math.random() * numColumns);
        y = 0;
        dir = Direction.UP;
        exit = getLineInBetween(x, y, x, y - 1);
        break;
      }
      // right
      case 1: {
        x = numColumns - 1;
        y = (int) (Math.random() * numRows);
        dir = Direction.RIGHT;
        exit = getLineInBetween(x, y, x + 1, y);
        break;
      }
      // down
      case 2: {
        x = (int) (Math.random() * numColumns);
        y = numRows - 1;
        dir = Direction.DOWN;
        exit = getLineInBetween(x, y, x, y + 1);
        break;
      }
      // left
      case 3: {
        x = 0;
        y = (int) (Math.random() * numRows);
        dir = Direction.LEFT;
        exit = getLineInBetween(x, y, x - 1, y);
        break;
      }
    }
    edges[y][x] |= (1 << dir);

    // place the player spawn
    int playerX = (int) (Math.random() * numColumns);
    int playerY = (int) (Math.random() * numRows);
    map[playerY][playerX] = 2;

    // place the enemies' spawn(s)
    int numEnemies = (int) (Math.random() * 3) + 1;
    for (int i = 0; i < numEnemies; ++i) {
      int enemyX, enemyY;
      do {
        enemyX = (int) (Math.random() * numColumns);
        enemyY = (int) (Math.random() * numRows);
      } while (Math.abs(enemyX - playerX) + Math.abs(enemyY - playerY) < 5);
      map[enemyY][enemyX] = 1;
    }

    createCharacters();
  }

  public ArrayList<Character> getCharacters() {
    return characters;
  }

  public int getEdge(int x, int y) {
    return edges[y][x];
  }

  public Line2D.Double[] getWalls() {
    return (walls == null) ? null : walls.toArray(new Line2D.Double[0]);
  }

  public void setWalls(ArrayList<Line2D.Double> walls) {
    this.walls = walls;
  }

  public Line2D.Double getExit() {
    return exit;
  }

  public void setPlayerPos(int x, int y) {
    playerPos.setX(x);
    playerPos.setY(y);
  }

  public Coordinate getPlayerPos() {
    return playerPos;
  }

  public int getLevel() {
    return level;
  }

  public double getBlockWidth() {
    return blockWidth;
  }

  public double getBlockHeight() {
    return blockHeight;
  }

  public void updateBlockSize(double panelWidth, double panelHeight) {
    blockWidth = panelWidth / numColumns;
    blockHeight = panelHeight / numRows;
  }

  public int getNumRows() {
    return numRows;
  }

  public int getNumColumns() {
    return numColumns;
  }
}
