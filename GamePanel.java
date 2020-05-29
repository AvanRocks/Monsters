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
	private boolean[] keyIsPressed = new boolean[4];
	private int[][] map = {
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1 },
		{ 1, 0, 1, 1, 0, 3, 0, 0, 0, 1 },
		{ 1, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
		{ 1, 1, 1, 1, 0, 0, 0, 1, 0, 1 },
		{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
		{ 1, 0, 0, 1, 1, 1, 0, 0, 1, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};

	public void start() {
		thread = new Thread(this);
		thread.start();

		// initialize block size
		blockWidth=(double)getWidth()/numColumns();
		blockHeight=(double)getHeight()/numRows();

		// find player's starting position in map and create player there
		for (int i=0;i<map.length;++i) {
			for (int j=0;j<map[0].length;++j) {
				if (map[i][j]==3) {
					try { player = new Player((int)(j*blockWidth),(int)(i*blockHeight), ImageIO.read(new File("images"+File.separator+"player1.png"))); }
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

		for (int i=0;i<map.length;++i) {
			for (int j=0;j<map[i].length;++j) {
				if (map[i][j]==1) {
					g.fillRect((int)(j*blockWidth), (int)(i*blockHeight), (int)blockWidth+1, (int)blockHeight+1);
				}
			}
		}

		g.setColor(oldColor);
	}

	private boolean isWall(int x, int y) {
		return map[y][x] == 1;
	}

	public int numColumns() {
		return map[0].length;
	}

	public int numRows() {
		return map.length;
	}

	public void movePlayer(int dir) {
		Rectangle playerRect = player.getRect();
		player.walk(dir);

		for (int y = 0; y < numRows(); ++y) {
			for (int x = 0; x < numColumns(); ++x) {
				if (isWall(x, y) && playerRect.intersects(new Rectangle((int)(x*blockWidth), (int)(y*blockHeight), (int)blockWidth, (int)blockHeight))) {
					player.walk(Direction.getOpposite(dir));
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
		movePlayer();
		super.paintComponent(g);

		// resize blocks
		double blockWidth = (double) getWidth() / numColumns();
		double blockHeight = (double) getHeight() / numRows();

		// draw map
		this.paintMap(g);


		// Draw the player
		g.drawImage(player.getImage(), player.getX(), player.getY(), null);

  	}

  @Override
  public void run() {
    prevTime = System.currentTimeMillis();

    while (true) {
      repaint();

      diffTime = System.currentTimeMillis() - prevTime;

      // Sleep for 40 milliseconds with accomodation for the time repaint() and movePlayer() took
      try { Thread.sleep(40 - diffTime); }
      catch (InterruptedException e) {}

      prevTime = System.currentTimeMillis();
    }

  }

}
