import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

class GamePanel extends JPanel implements Runnable {
  private double blockWidth, blockHeight;
  private Thread thread;
  private long prevTime, diffTime;
  //private Monster[] monsters;
  private Player player;
  private Map map = new Map();
  private boolean[] keyIsPressed = new boolean[4];

	public GamePanel() {
		setFocusable(true);
		addKeyListener(player);
	}

  public void start() {
    thread = new Thread(this);
    thread.start();

    // initialize block size
    blockWidth=(double)getWidth() / map.getNumColumns();
    blockHeight=(double)getHeight() / map.getNumRows();

    // find player's starting position in map and create player there
    for (int y = 0; y < map.getNumRows(); ++y) {
      for (int x = 0; x < map.getNumColumns(); ++x) {
        if (map.getBlock(x, y) == Map.BlockType.PLAYER_SPAWN) {
          try { player = new Player((int)(x * blockWidth),(int)(y * blockHeight), ImageIO.read(new File("images"+File.separator+"player1.png"))); }
         catch (IOException e) { e.printStackTrace();}
        }
      }
    }

  }

  private void paintMap(Graphics g) {
    Color oldColor = g.getColor();
    g.setColor(Color.black);

    for (int y = 0; y < map.getNumRows(); ++y) {
      for (int x = 0; x < map.getNumColumns(); ++x) {
        if (map.getBlock(x, y) == Map.BlockType.OPEN) {
          g.fillRect((int)(y*blockWidth), (int)(x*blockHeight), (int)blockWidth+1, (int)blockHeight+1);
        }
      }
    }

    g.setColor(oldColor);
  }

  public boolean isWall(int x, int y) {
    return map[y][x] == 1;
  }

  public int numColumns() {
    return map[0].length;
  }

  public int numRows() {
    return map.length;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // resize blocks
    blockWidth = (double) getWidth() / map.getNumColumns();
    blockHeight = (double) getHeight() / map.getNumRows();

    // draw map
    this.paintMap(g);

    // Draw the player
    player.move();
    g.drawImage(player.getImage(), player.getX(), player.getY(), null);

  }

  @Override
  public void run() {
    prevTime = System.currentTimeMillis();

    while (true) {
      repaint();

      diffTime = System.currentTimeMillis() - prevTime;

      // Sleep for 40 milliseconds with accomodation for the time repaint() took
      try { Thread.sleep(40 - diffTime); }
      catch (InterruptedException e) {}

      prevTime = System.currentTimeMillis();
    }

  }

}
