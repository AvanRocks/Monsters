import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

class GamePanel extends JPanel implements KeyListener{
	double blockWidth;
	double blockHeight;
	int playerX, playerY;
	int playerInitX = 100, playerInitY = 100;
	//Monster[] monsters;
	Player player;
	private int[][] map = {
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1 },
		{ 1, 0, 1, 1, 0, 0, 0, 0, 0, 1 },
		{ 1, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
		{ 1, 1, 1, 1, 0, 0, 0, 1, 0, 1 },
		{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 1 },
		{ 1, 0, 0, 0, 3, 0, 0, 0, 0, 1 },
		{ 1, 0, 0, 1, 1, 1, 0, 0, 1, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
	};

	GamePanel() {
		try {
			player = new Player(playerInitX, playerInitY, ImageIO.read(new File("images"+File.pathSeparator+"player1.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		resetPlayerPos();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (isVisible()) {
			if (e.getKeyCode() == KeyEvent.VK_UP) player.move(Character.UP);
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) player.move(Character.DOWN);
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) player.move(Character.LEFT);
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.move(Character.RIGHT);
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

		// draw the map
		blockWidth=(double)getWidth()/map[0].length;
		blockHeight=(double)getHeight()/map.length;
		
		g.setColor(Color.black);

		for (int i=0;i<map.length;++i) {
			for (int j=0;j<map[i].length;++j) {
				if (map[i][j]==1) {
					g.fillRect((int)(j*blockWidth), (int)(i*blockHeight), (int)blockWidth+1, (int)blockHeight+1);
				}
			}
		}
	}
}
