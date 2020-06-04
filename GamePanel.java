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
          try { player = new Player((int)(x * blockWidth),(int)(y * blockHeight), ImageIO.read(new File("images"+File.separator+"player1.png")), this, map); }
         catch (IOException e) { e.printStackTrace();}
        }
      }
    }

		// add player as Key Listener
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(player);

  }

  private void paintMap(Graphics g) {
    Color oldColor = g.getColor();
    g.setColor(Color.black);

    for (int y = 0; y < map.getNumRows(); ++y) {
      for (int x = 0; x < map.getNumColumns(); ++x) {
        if (map.getBlock(x, y) == Map.BlockType.WALL) {
          g.fillRect((int)(x*blockWidth), (int)(y*blockHeight), (int)blockWidth+1, (int)blockHeight+1);
        }
      }
    }

    g.setColor(oldColor);
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
