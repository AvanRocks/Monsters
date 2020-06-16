import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;

class GamePanel extends JPanel implements Runnable {
  private Thread thread;
  private long prevTime, diffTime;
  //private Monster[] monsters;
  //private Player player;
	private ArrayList<Character> characters;
  private Map map;
  private boolean[] keyIsPressed = new boolean[4];
	private CardLayout cards;
	private Container pane;
	private MutableBoolean gameIsActive;

	GamePanel(CardLayout cards, Container pane) {
		this.cards=cards;
		this.pane=pane;
	}

  public void start() {
		gameIsActive = new MutableBoolean(true);

    thread = new Thread(this);
    thread.start();

		map = new Map(getWidth(), getHeight());

		characters = new ArrayList<>();

		// create player
    try { characters.add( new Player(map.getPlayerPos().getX(), map.getPlayerPos().getY(), ImageIO.read(new File("images"+File.separator+"player.png")), map) ); }
		catch (IOException e) { e.printStackTrace();}

    // create Enemies at their specified starting positions
    for (int y = 0; y < map.getNumRows(); ++y) {
      for (int x = 0; x < map.getNumColumns(); ++x) {
				// create enemy
				if (map.getBlock(x,y) == Map.BlockType.ENEMY_SPAWN) {
          try { characters.add( new Enemy(x, y, ImageIO.read(new File("images"+File.separator+"enemy-orc.png")), map, cards, pane, gameIsActive) ); }
					catch (IOException e) { e.printStackTrace();}
        }
      }
		}


		addKeyListener((Player)characters.get(0));

		setFocusable(true);
  }

  private void paintMap(Graphics g) {
		// Change color, but remember old color to set it back later
    Color oldColor = g.getColor();
    g.setColor(Color.black);

		// draw all the walls
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

    // Draw the characters
		for (Character c : characters) {
			c.updatePos();
			c.drawImage(g);
		}

		map.setPlayerPos((int) Math.round(characters.get(0).getX()), (int) Math.round(characters.get(0).getY()));
  }

  @Override
  public void run() {
		// count how long repaint() will take
    prevTime = System.currentTimeMillis();

    while (gameIsActive.getVal()) {
      repaint();

      diffTime = System.currentTimeMillis() - prevTime;

      // Sleep for 75 milliseconds with accomodation for the time repaint() took
      try { Thread.sleep(75 - diffTime); }
      catch (InterruptedException e) {}

			// count how long repaint() will take
      prevTime = System.currentTimeMillis();
    }
  }
}
