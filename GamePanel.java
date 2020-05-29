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
	private Rectangle playerRect;
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
			if (e.getKeyCode() == KeyEvent.VK_UP) player.walk(Character.UP);
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) player.walk(Character.DOWN);
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) player.walk(Character.LEFT);
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.walk(Character.RIGHT);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (isVisible()) {
			if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) player.stop(Character.UP);
			else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) player.stop(Character.LEFT);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// resize blocks
		blockWidth=(double)getWidth()/map[0].length;
		blockHeight=(double)getHeight()/map.length;
		
		// draw map
		g.setColor(Color.black);

		for (int i=0;i<map.length;++i) {
			for (int j=0;j<map[i].length;++j) {
				if (map[i][j]==1) {
					g.fillRect((int)(j*blockWidth), (int)(i*blockHeight), (int)blockWidth+1, (int)blockHeight+1);
				}
			}
		}

		// If player intersects a wall, stop them
		playerRect = player.getRect();
		for (int i=0;i<map.length;++i) {
			for (int j=0;j<map[i].length;++j) {
				if (map[i][j]==1 && playerRect.intersects(new Rectangle((int)(j*blockWidth), (int)(i*blockHeight), (int)blockWidth, (int)blockHeight))) {
					player.reverseDir();
				}
			}
		}

		// Draw the player
		player.updatePos();
		int[] playerPos = player.getPos();
		g.drawImage(player.getImage(), playerPos[0], playerPos[1], null);

	}

	@Override
	public void run() {
		prevTime = System.currentTimeMillis();

		while (true) {
			repaint();	
			diffTime = System.currentTimeMillis() - prevTime;

			//Sleep for 40 milliseconds with accomodation for the time repaint() took
			try {
				Thread.sleep(40 - diffTime);
			} catch (InterruptedException e) {}

			prevTime = System.currentTimeMillis();
		}

	}

}
