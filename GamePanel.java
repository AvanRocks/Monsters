import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.*;

class GamePanel extends JPanel {
  private ArrayList<Character> characters;
  private Map map;
  private CardLayout cards;
	private Container pane;
  private MutableBoolean gameIsActive;
  private ArrayList<Line2D.Double> walls;

	GamePanel(CardLayout cards, Container pane) {
		this.cards=cards;
		this.pane=pane;
	}

  public void start() {
		gameIsActive = new MutableBoolean(true);

    map = new Map(getWidth(), getHeight(), cards, pane, gameIsActive);
    walls = new ArrayList<>();
    characters = map.getCharacters();

    new Thread(new GameThread(characters, gameIsActive, this, map)).start();

		addKeyListener((Player)characters.get(0));

		setFocusable(true);
  }

  private void drawWall(int x1, int y1, int x2, int y2, Graphics2D g2d) {
    Line2D.Double wall = null;
    if (y1==y2)
      wall = new Line2D.Double(x2*map.getBlockWidth(), y1*map.getBlockHeight(), x2*map.getBlockWidth(), (y1+1)*map.getBlockHeight());
    else if (x1==x2)
      wall = new Line2D.Double(x2*map.getBlockWidth(), y2*map.getBlockHeight(), (x2+1)*map.getBlockWidth(), y2*map.getBlockHeight());

    if (wall!=null) {
      walls.add(wall);
      g2d.draw(wall);
    } else {
      System.out.println("wall is null");
    }
  }

  private void paintMap(Graphics g) {
		// Change color, but remember old color to set it back later
    Color oldColor = g.getColor();
    g.setColor(Color.black);

    Graphics2D g2d = (Graphics2D) g;
    g2d.setStroke(new BasicStroke(20));


    // draw borders
    // top
    Line2D.Double wall = new Line2D.Double(0, 0, getWidth(), 0);
    g2d.draw(wall);
    walls.add(wall);

    // right
    wall = new Line2D.Double(getWidth(), 0, getWidth(), getHeight());
    g2d.draw(wall);
    walls.add(wall);

    // left
    wall = new Line2D.Double(0, 0, 0, getHeight());
    g2d.draw(wall);
    walls.add(wall);

    // bottom
    wall = new Line2D.Double(0, getHeight(), getWidth(), getHeight());
    g2d.draw(wall);
    walls.add(wall);

    g2d.setStroke(new BasicStroke(10));
    // draw walls
    for (int y=0;y<map.getNumRows(); ++y)
      for (int x=0;x<map.getNumColumns(); ++x) {
        if ((map.getEdge(x,y) & (1<<Direction.RIGHT)) == 0)
          drawWall(x,y,x+1,y,g2d);
        if ((map.getEdge(x,y) & (1<<Direction.DOWN)) == 0)
          drawWall(x,y,x,y+1,g2d);
      }


    g2d.setColor(Color.red);

    for (Line2D.Double w : walls) {
      Rectangle2D wallBounds = w.getBounds2D();
      int width = (int) Math.max(5, wallBounds.getWidth());
      int height = (int) Math.max(5, wallBounds.getHeight());
      int x = (int) wallBounds.getX();
      int y = (int) wallBounds.getY();

      g2d.drawRect(x, y, width, height);
    }

    g2d.draw(characters.get(0).getRect());

    g.setColor(oldColor);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // resize blocks
		map.updateBlockSize(getWidth(),getHeight());

    // draw map
    walls.clear();
    paintMap(g);
    map.setWalls(walls);

    // Draw the characters
		for (Character c : characters) {
			c.drawImage(g);
		}
  }
}
