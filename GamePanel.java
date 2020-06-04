import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

class GamePanel extends JPanel implements KeyListener, Runnable {
  private double blockWidth, blockHeight;
  private Thread thread;
  private long prevTime, diffTime;
  //private Monster[] monsters;
  private Player player;
  private Map map;
  private boolean[] keyIsPressed = new boolean[4];

  public void start() {
    this.map = new Map();
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

  @Override
  public void keyPressed(KeyEvent e) {
    if (isVisible()) {
      if (e.getKeyCode() == KeyEvent.VK_UP) keyIsPressed[Direction.UP]=true;
      else if (e.getKeyCode() == KeyEvent.VK_DOWN) keyIsPressed[Direction.DOWN]=true;
      else if (e.getKeyCode() == KeyEvent.VK_LEFT) keyIsPressed[Direction.LEFT]=true;
      else if (e.getKeyCode() == KeyEvent.VK_RIGHT) keyIsPressed[Direction.RIGHT]=true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (isVisible()) {
      if (e.getKeyCode() == KeyEvent.VK_UP) keyIsPressed[Direction.UP]=false;
      else if (e.getKeyCode() == KeyEvent.VK_DOWN) keyIsPressed[Direction.DOWN]=false;
      else if (e.getKeyCode() == KeyEvent.VK_LEFT) keyIsPressed[Direction.LEFT]=false;
      else if (e.getKeyCode() == KeyEvent.VK_RIGHT) keyIsPressed[Direction.RIGHT]=false;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  private void paintMap(Graphics g) {
    Color oldColor = g.getColor();
    g.setColor(Color.black);

    for (int y = 0; y < map.getNumRows(); ++y) {
      for (int x = 0; x < map.getNumColumns(); ++x) {
        if (map.getBlock(x, y) == Map.BlockType.WALL) {
          g.fillRect((int)(y*blockWidth), (int)(x*blockHeight), (int)blockWidth+1, (int)blockHeight+1);
        }
      }
    }

    g.setColor(oldColor);
  }

  private void movePlayer(int dir) {
    player.walk(dir);
    Rectangle playerRect = player.getRect();

    for (int y = 0; y < map.getNumRows(); ++y) {
      for (int x = 0; x < map.getNumColumns(); ++x) {
        if (map.getBlock(x, y) == Map.BlockType.WALL && playerRect.intersects(new Rectangle((int)(x*blockWidth), (int)(y*blockHeight), (int)blockWidth+1, (int)blockHeight+1))) {
          player.move(Direction.getOpposite(dir));
          return;
        }
      }
    }
  }

  public void movePlayer() {
    if (keyIsPressed[Direction.UP]) movePlayer(Direction.UP);
    if (keyIsPressed[Direction.DOWN]) movePlayer(Direction.DOWN);
    if (keyIsPressed[Direction.LEFT]) movePlayer(Direction.LEFT);
    if (keyIsPressed[Direction.RIGHT]) movePlayer(Direction.RIGHT);

    if (!keyIsPressed[Direction.UP] &&
      !keyIsPressed[Direction.DOWN] &&
      !keyIsPressed[Direction.LEFT] &&
      !keyIsPressed[Direction.RIGHT]) {
      player.stop();
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // resize blocks
    double blockWidth = (double) getWidth() / map.getNumColumns();
    double blockHeight = (double) getHeight() / map.getNumRows();

    // draw map
    this.paintMap(g);

           // Draw the player
    movePlayer();
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
