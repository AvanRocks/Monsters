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
	boolean[] keyIsPressed;
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
		blockWidth=(double)getWidth()/map[0].length;
		blockHeight=(double)getHeight()/map.length;

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
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) keyIsPressed[Direction.DOWN)=false;
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

	public void movePlayer() {
		if (keyIsPressed[Direction.UP]) player.move(Direction.UP);
		if (keyIsPressed[Direction.DOWN]) player.move(Direction.DOWN);
		if (keyIsPressed[Direction.LEFT]) player.move(Direction.LEFT);
		if (keyIsPressed[Direction.RIGHT]) player.move(Direction.RIGHT);

		if (!keyIsPressed[Direction.UP] && !keyIsPressed[Direction.DOWN] && !keyIsPressed[Direction.LEFT] && !keyIsPressed[Direction.RIGHT])
			player.stop();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// resize blocks
		double blockWidth = (double) getWidth() / numColumns();
		double blockHeight = (double) getHeight() / numRows();

		// draw map
		this.paintMap(g);

		// If player intersects a wall, stop them
		Rectangle playerRect = player.getRect();
	
		/*
		for (int y = 0; y < numRows(); ++y) {
			for (int x = 0; x < numColumns(); ++x) {
				Point topLeftPoint = new Point((int) (x * blockWidth), (int) (y * blockHeight));
				Dimension blockSize = new Dimension((int) blockWidth, (int) blockHeight);
				Rectangle wallBlockRect = new Rectangle(topLeftPoint, blockSize);
				if (isWall(x, y) && playerRect.intersects(wallBlockRect)) {
					// TODO only stop in the direction they intersect in
					player.stop(0);
					player.stop(1);
				}
			}
		}
		*/

		// Draw the player
		g.drawImage(player.getImage(), player.getX(), player.getY(), null);

	}

	@Override
	public void run() {
		prevTime = System.currentTimeMillis();

		while (true) {
			movePlayer();
			repaint();

			diffTime = System.currentTimeMillis() - prevTime;

			// Sleep for 40 milliseconds with accomodation for the time repaint() and movePlayer() took
			try { Thread.sleep(40 - diffTime); }
			catch (InterruptedException e) {}

			prevTime = System.currentTimeMillis();
		}

	}

}
