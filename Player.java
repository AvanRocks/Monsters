import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;

class Player extends Character implements KeyListener {
	private boolean[] keyIsPressed = new boolean[4];
	double blockWidth, blockHeight;
	JPanel panel;

	Player(int x, int y, BufferedImage spriteSheet, JPanel panel, Map map) {
		super(x,y,spriteSheet,map);
		this.panel=panel;
	}

	@Override
  public void keyPressed(KeyEvent e) {
    if (panel.isVisible()) {
      if (e.getKeyCode() == KeyEvent.VK_UP) keyIsPressed[Direction.UP]=true;
      else if (e.getKeyCode() == KeyEvent.VK_DOWN) keyIsPressed[Direction.DOWN]=true;
      else if (e.getKeyCode() == KeyEvent.VK_LEFT) keyIsPressed[Direction.LEFT]=true;
      else if (e.getKeyCode() == KeyEvent.VK_RIGHT) keyIsPressed[Direction.RIGHT]=true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (panel.isVisible()) {
      if (e.getKeyCode() == KeyEvent.VK_UP) keyIsPressed[Direction.UP]=false;
      else if (e.getKeyCode() == KeyEvent.VK_DOWN) keyIsPressed[Direction.DOWN]=false;
      else if (e.getKeyCode() == KeyEvent.VK_LEFT) keyIsPressed[Direction.LEFT]=false;
      else if (e.getKeyCode() == KeyEvent.VK_RIGHT) keyIsPressed[Direction.RIGHT]=false;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {}

	public void move() {
    if (keyIsPressed[Direction.UP]) move(Direction.UP);
    if (keyIsPressed[Direction.DOWN]) move(Direction.DOWN);
    if (keyIsPressed[Direction.LEFT]) move(Direction.LEFT);
    if (keyIsPressed[Direction.RIGHT]) move(Direction.RIGHT);

    if (!keyIsPressed[Direction.UP] &&
      !keyIsPressed[Direction.DOWN] &&
      !keyIsPressed[Direction.LEFT] &&
      !keyIsPressed[Direction.RIGHT])
      stop();
	}

	private void move(int dir) {
    walk(dir);
    Rectangle playerRect = getRect();

		// resize block size
		blockWidth = (double) panel.getWidth() / panel.numColumns();
    blockHeight = (double) panel.getHeight() / panel.numRows();


    for (int y = 0; y < numRows(); ++y)
      for (int x = 0; x < numColumns(); ++x)
        if (isWall(x, y) && playerRect.intersects(new Rectangle((int)(x*blockWidth), (int)(y*blockHeight), (int)blockWidth+1, (int)blockHeight+1))) {
          move(Direction.getOpposite(dir));
          return;
        }
	}
	
}
