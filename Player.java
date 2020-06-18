import java.awt.image.BufferedImage;
import java.awt.event.*;

class Player extends Character implements KeyListener {
	private boolean[] keyIsPressed = new boolean[4];

	Player(int x, int y, BufferedImage spriteSheet, Map map) {
		super(x,y,spriteSheet,map);
	}

	@Override
  public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) keyIsPressed[Direction.UP]=true;
      else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) keyIsPressed[Direction.DOWN]=true;
      else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) keyIsPressed[Direction.LEFT]=true;
      else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) keyIsPressed[Direction.RIGHT]=true;
  }

  @Override
  public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) keyIsPressed[Direction.UP]=false;
      else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) keyIsPressed[Direction.DOWN]=false;
      else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) keyIsPressed[Direction.LEFT]=false;
      else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) keyIsPressed[Direction.RIGHT]=false;
  }

  @Override
  public void keyTyped(KeyEvent e) {}

	// this is the method called from GamePanel
	@Override
	public void updatePos() {
    if (keyIsPressed[Direction.UP]) updatePos(Direction.UP);
    if (keyIsPressed[Direction.DOWN]) updatePos(Direction.DOWN);
    if (keyIsPressed[Direction.LEFT]) updatePos(Direction.LEFT);
    if (keyIsPressed[Direction.RIGHT]) updatePos(Direction.RIGHT);

    if (!keyIsPressed[Direction.UP] &&
      !keyIsPressed[Direction.DOWN] &&
      !keyIsPressed[Direction.LEFT] &&
      !keyIsPressed[Direction.RIGHT])
      stop();

	}

	// helper method
	private void updatePos(int dir) {
    walk(dir);
		checkCollision(dir);
	}
}
