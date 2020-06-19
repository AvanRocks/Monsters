import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import javax.imageio.ImageIO;

class Map {
  // Number of rows and columns in the map's maze
  private int numRows;
  private int numColumns;

  // Block width (sized relative to the window's width and height)
  private double blockWidth, blockHeight;

  // Player's current level
  private int level = 0;

  // Player's current position in the maze based on rows and columns (not
  // pixels)
  private Coordinate playerPos = new Coordinate(0, 0);

  // An array where the first character is always the player and subsequent
  // characters are monsters in the curren tlevel
  private final ArrayList<Character> characters;

  // Union-set related variables

  // link[i] = The head of node i
  private int[] link;

  // size[i] = The # of nodes of the set which node i belongs to
  private int[] size;

  // The walls of the maze
  private ArrayList<Line2D.Double> walls = null;

  // The exit position and orientation
  private int exitX, exitY, exitDir;

  // The block type of the coordinates in the game map/maze
  private int[][] map;

  // The edges of the map, these are actually inverse edges ("anti-edges"):
  // they determine where there are NO walls
  public int[][] edges;

  // The card layout (needed to switch scenes (e.g. if the player loses,
  // switch to the game over screen))
  private final CardLayout cards;

  // Pane needed to switch the card layout (e.g. game over screen)
  private final Container pane;

  // MutableBoolean representing the active state of the game (active, or
  // non-active: game over or on main menu)
  private final MutableBoolean gameIsActive;

  // The panel width and height based on JFrame's getWidth() and getHeight()
  private int panelWidth, panelHeight;

  // The player's exact coordinates from the top-left in fractional rows and
  // columns (e.g. if the player is in between column 2 and 3, playerExactX
  // would be a double between 2 and 3)
  private double playerExactX, playerExactY;

  // An enum representing a grid cell's type (used for map initialization)
  public enum BlockType {
    EMPTY,
    ENEMY_SPAWN,
    PLAYER_SPAWN,
  }

  // Initializing variables and basic map layout (block size)
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

    characters = new ArrayList<>();
  }

  // Updates needed variables before moving player to next level
  public void nextLevel() {
    level++;
    generateMap();
  }

  // Returns the type of block at coordinate (x, y) of the map
  public BlockType getBlock(int x, int y) {
    switch (map[y][x]) {
      case 0:
        return BlockType.EMPTY;
      case 1:
        return BlockType.ENEMY_SPAWN;
      case 2:
        return BlockType.PLAYER_SPAWN;
      default:
        return BlockType.EMPTY;
    }
  }

  // Get the wall between two coordinates
  public Line2D.Double getLineInBetween(int x1, int y1, int x2, int y2) {
    if (y1 == y2) {
      if (x2 > x1) {
        return new Line2D.Double(
          x2 * blockWidth,
          y1 * blockHeight,
          x2 * blockWidth,
          (y1 + 1) * blockHeight
        );
      } else if (x1 > x2) {
        return new Line2D.Double(
          x1 * blockWidth,
          y1 * blockHeight,
          x1 * blockWidth,
          (y1 + 1) * blockHeight
        );
      }
    } else if (x1 == x2) {
      if (y2 > y1) {
        return new Line2D.Double(
          x2 * blockWidth,
          y2 * blockHeight,
          (x2 + 1) * blockWidth,
          y2 * blockHeight
        );
      } else if (y1 > y2) {
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

  // Creates and displays the characters in the game
  private void createCharacters() {
    characters.clear();

    // Creating the player
    for (int y = 0; y < getNumRows(); ++y) {
      for (int x = 0; x < getNumColumns(); ++x) if (
        getBlock(x, y) == BlockType.PLAYER_SPAWN
      ) {
        playerPos = new Coordinate(x, y);
        try {
          // Load the character from its image file and place it at its
          // starting position
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

    // Creating the enemies and placing them at their starting positions
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
                gameIsActive,
                Math.max(1, 5 - level)
              )
            );
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  // Union-find data structure methods

  // Gets the head of a union-set
  private int find(int x) {
    while (x != link[x]) x = link[x];
    return x;
  }

  // Returns of two nodes belong to the same union-set
  private boolean same(int a, int b) {
    return find(a) == find(b);
  }

  // Connecting two union-sets
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

  // Generating the map using Eller's algorithm (Using Union-Find)
  private void generateMap() {
    int temp = 6 + (level - 1) * 3;
    numColumns = temp;
    numRows = temp;

    updateBlockSize(panelWidth, panelHeight);

    edges = new int[numRows][numColumns];
    map = new int[numRows][numColumns];

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

    // merge random cells to create some loops
    int numOfMerges = (int) (Math.random() * Math.min(numColumns, numRows)) +
    Math.min(numColumns, numRows) *
    3;
    for (int i = 0; i < numOfMerges; ++i) {
      int x = -1, y = -1, dir = -1;
      do {
        x = (int) (Math.random() * numColumns);
        y = (int) (Math.random() * numRows);
        dir = (int) (Math.random() * 4);
      } while (
        (y == 0 && dir == Direction.UP) ||
        (y == numRows - 1 && dir == Direction.DOWN) ||
        (x == 0 && dir == Direction.LEFT) ||
        (x == numColumns - 1 && dir == Direction.RIGHT) ||
        (edges[y][x] & (1 << dir)) == 1
      );
      // Adding an "anti-edge" in both the current cell and the other
      // adjacent cell to the "anti-edge"/wall
      edges[y][x] |= (1 << dir);

      switch (dir) {
        case Direction.UP:
          edges[y - 1][x] |= (1 << Direction.DOWN);
          break;
        case Direction.LEFT:
          edges[y][x - 1] |= (1 << Direction.RIGHT);
          break;
        case Direction.RIGHT:
          edges[y][x + 1] |= (1 << Direction.LEFT);
          break;
        case Direction.DOWN:
          edges[y + 1][x] |= (1 << Direction.UP);
          break;
      }
    }

    // Creating the exit in the maze
    int side = (int) (Math.random() * 4);
    switch (side) {
      // top
      case 0:
        {
          exitX = (int) (Math.random() * numColumns);
          exitY = 0;
          exitDir = Direction.UP;
          break;
        }
      // right
      case 1:
        {
          exitX = numColumns - 1;
          exitY = (int) (Math.random() * numRows);
          exitDir = Direction.RIGHT;
          break;
        }
      // down
      case 2:
        {
          exitX = (int) (Math.random() * numColumns);
          exitY = numRows - 1;
          exitDir = Direction.DOWN;
          break;
        }
      // left
      case 3:
        {
          exitX = 0;
          exitY = (int) (Math.random() * numRows);
          exitDir = Direction.LEFT;
          break;
        }
    }
    edges[exitY][exitX] |= (1 << exitDir);

    // place the player spawn
    int playerX = (int) (Math.random() * numColumns);
    int playerY = (int) (Math.random() * numRows);
    map[playerY][playerX] = 2;

    // place the enemies' spawn(s)
    int numEnemies = (int) (Math.random() * (level - 1)) + level;
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

  // Returns the exit line
  public Line2D.Double getExit() {
    switch (exitDir) {
      case Direction.UP:
        return getLineInBetween(exitX, exitY, exitX, exitY - 1);
      case Direction.LEFT:
        return getLineInBetween(exitX, exitY, exitX - 1, exitY);
      case Direction.RIGHT:
        return getLineInBetween(exitX, exitY, exitX + 1, exitY);
      case Direction.DOWN:
        return getLineInBetween(exitX, exitY, exitX, exitY + 1);
    }
    return null;
  }

  public void setPlayerPos(int x, int y) {
    playerPos.setX(x);
    playerPos.setY(y);
  }

  public Coordinate getPlayerPos() {
    return playerPos;
  }

  public void setExactPlayerPos(double x, double y) {
    playerExactX = x;
    playerExactY = y;
  }

  public double getPlayerExactX() {
    return playerExactX;
  }

  public double getPlayerExactY() {
    return playerExactY;
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

  // Resizes the block based on the window's width/height
  public void updateBlockSize(double panelWidth, double panelHeight) {
    this.panelWidth = (int) panelWidth;
    this.panelHeight = (int) panelHeight;
    blockWidth = panelWidth / numColumns;
    blockHeight = panelHeight / numRows;
  }

  public void setPanelSize(int panelWidth, int panelHeight) {
    this.panelWidth = panelWidth;
    this.panelHeight = panelHeight;
  }

  public int getNumRows() {
    return numRows;
  }

  public int getNumColumns() {
    return numColumns;
  }
}
