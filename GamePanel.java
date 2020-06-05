import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

class GamePanel extends JPanel implements Runnable {
  private Thread thread;
  private long prevTime, diffTime;
  //private Monster[] monsters;
  private Player player;
  private Map map;
  private boolean[] keyIsPressed = new boolean[4];

  public void start() {
    thread = new Thread(this);
    thread.start();

		map = new Map(getWidth(), getHeight());

    // initialize block size
		map.updateBlockSize(getWidth(),getHeight());

    // find player's starting position in map and create player there
    for (int y = 0; y < map.getNumRows(); ++y) {
      for (int x = 0; x < map.getNumColumns(); ++x) {
        if (map.getBlock(x, y) == Map.BlockType.PLAYER_SPAWN) {
          try { player = new Player((int)(x * map.getBlockWidth()),(int)(y * map.getBlockHeight()), ImageIO.read(new File("images"+File.separator+"player1.png")), map); }
         catch (IOException e) { e.printStackTrace();}
        }
      }
    }
		

		addKeyListener(player);

		setFocusable(true);
  }

  private void paintMap(Graphics g) {
    Color oldColor = g.getColor();
    g.setColor(Color.black);

    for (int y = 0; y < map.getNumRows(); ++y) {
      for (int x = 0; x < map.getNumColumns(); ++x) {
        if (map.getBlock(x, y) == Map.BlockType.WALL) {
          g.fillRect((int)(x*map.getBlockWidth()), (int)(y*map.getBlockHeight()), (int)map.getBlockWidth()+1, (int)map.getBlockHeight()+1);
        }
      }
    }

    g.setColor(oldColor);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // resize blocks
		map.updateBlockSize(getWidth(),getHeight());

    // draw map
    this.paintMap(g);

    // Draw the player
    player.updatePos();
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
