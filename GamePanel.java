import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

class GamePanel extends JPanel {
  private ArrayList<Character> characters;
  private Map map;
  private final CardLayout cards;
  private final Container pane;
  private ArrayList<Line2D.Double> walls;
  private boolean isFirstPaint = true;
  public static double WALL_WIDTH_SCALE = 0.1;

  GamePanel(CardLayout cards, Container pane) {
    this.cards = cards;
    this.pane = pane;
  }

  public void start() {
    MutableBoolean gameIsActive = new MutableBoolean(true);

    map = new Map(getWidth(), getHeight(), cards, pane, gameIsActive);
    map.setPanelSize(getWidth(), getHeight());
    advanceLevel();

    walls = new ArrayList<>();

    new Thread(new GameThread(characters, gameIsActive, this, map)).start();

    setFocusable(true);
  }

  private void drawWall(int x1, int y1, int x2, int y2, Graphics2D g2d) {
    Line2D.Double wall = map.getLineInBetween(x1, y1, x2, y2);

    if (wall != null) {
      walls.add(wall);
      g2d.draw(wall);
    }
  }

  private void paintMap(Graphics g) {
    // Change color and stroke, but remember them to set it back later
    Color oldColor = g.getColor();
    g.setColor(Color.black);

    Graphics2D g2d = (Graphics2D) g;
    g2d.setStroke(new BasicStroke(
      (int) Math.min(map.getBlockHeight() * WALL_WIDTH_SCALE * 3,
        map.getBlockWidth() * WALL_WIDTH_SCALE * 3))
    );

    // draw borders
    // top
    for (int x = 0; x < map.getNumColumns(); ++x) if (
      (map.getEdge(x, 0) & (1 << Direction.UP)) == 0
    ) drawWall(x, 0, x, -1, g2d);

    // left
    for (int y = 0; y < map.getNumRows(); ++y) if (
      (map.getEdge(0, y) & (1 << Direction.LEFT)) == 0
    ) drawWall(0, y, -1, y, g2d);

    g2d.setStroke(new BasicStroke(
      (int) Math.min(map.getBlockHeight() * WALL_WIDTH_SCALE,
        map.getBlockWidth() * WALL_WIDTH_SCALE))
    );

    // draw walls
    for (int y = 0; y < map.getNumRows(); ++y) for (
      int x = 0;
      x < map.getNumColumns();
      ++x
    ) {
      if ((map.getEdge(x, y) & (1 << Direction.RIGHT)) == 0) {
        drawWall(x, y, x + 1, y, g2d);
      }
      if ((map.getEdge(x, y) & (1 << Direction.DOWN)) == 0) {
        drawWall(x, y, x, y + 1, g2d);
      }
    }

    g.setColor(oldColor);
  }

  private void advanceLevel() {
    map.nextLevel();

    characters = map.getCharacters();
    addKeyListener((Player) characters.get(0));
  }

  private void paintLevel(Graphics g) {
    int rectWidth = getWidth() / 3;
    int rectHeight = getWidth() / 5;

    Color oldColor = g.getColor();
    g.setColor(Color.black);

    g.fillRoundRect(
      getWidth() - rectWidth / 2,
      getHeight() - rectHeight / 2,
      rectWidth,
      rectHeight,
      10,
      10
    );

    g.setColor(oldColor);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // resize blocks
    map.updateBlockSize(getWidth(), getHeight());

    // draw map
    walls.clear();
    paintMap(g);
    map.setWalls(walls);

    // Draw the characters
    for (Character c : characters) {
      c.drawImage(g);
    }

    // check if the player has reached the exit
    if (((Player) characters.get(0)).reachedExit()) {
      advanceLevel();
      paintLevel(g);
    }

    if (isFirstPaint) {
      paintLevel(g);
      isFirstPaint = false;
      //try { Thread.sleep(5000); }
      //catch (Exception ignored) {}
    }
  }
}
