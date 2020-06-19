import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

class Player extends Character implements KeyListener {
  private final boolean[] keyIsPressed = new boolean[4];

  Player(int x, int y, BufferedImage spriteSheet, Map map) {
    super(x, y, spriteSheet, map);
  }

  // Methods related to player movement
  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
      keyIsPressed[Direction.UP] = true;
    } else if (
      e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S
    ) {
      keyIsPressed[Direction.DOWN] = true;
    } else if (
      e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A
    ) {
      keyIsPressed[Direction.LEFT] = true;
    } else if (
      e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D
    ) {
      keyIsPressed[Direction.RIGHT] = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
      keyIsPressed[Direction.UP] = false;
    } else if (
      e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S
    ) {
      keyIsPressed[Direction.DOWN] = false;
    } else if (
      e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A
    ) {
      keyIsPressed[Direction.LEFT] = false;
    } else if (
      e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D
    ) {
      keyIsPressed[Direction.RIGHT] = false;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  // Checks state of keys and uses them to decide whether to move the player
  // or not
  @Override
  public void updatePos() {
    if (keyIsPressed[Direction.UP] && keyIsPressed[Direction.RIGHT]) {
      updatePos(Direction.UP, true);
      updatePos(Direction.RIGHT, true);
    } else if (keyIsPressed[Direction.DOWN] && keyIsPressed[Direction.RIGHT]) {
      updatePos(Direction.DOWN, true);
      updatePos(Direction.RIGHT, true);
    } else if (keyIsPressed[Direction.UP] && keyIsPressed[Direction.LEFT]) {
      updatePos(Direction.UP, true);
      updatePos(Direction.LEFT, true);
    } else if (keyIsPressed[Direction.DOWN] && keyIsPressed[Direction.LEFT]) {
      updatePos(Direction.DOWN, true);
      updatePos(Direction.LEFT, true);
    } else if (keyIsPressed[Direction.UP]) {
      updatePos(Direction.UP, false);
    } else if (keyIsPressed[Direction.DOWN]) {
      updatePos(Direction.DOWN, false);
    } else if (keyIsPressed[Direction.LEFT]) {
      updatePos(Direction.LEFT, false);
    } else if (keyIsPressed[Direction.RIGHT]) {
      updatePos(Direction.RIGHT, false);
    }

    if (
      !keyIsPressed[Direction.UP] &&
      !keyIsPressed[Direction.DOWN] &&
      !keyIsPressed[Direction.LEFT] &&
      !keyIsPressed[Direction.RIGHT]
    ) {
      stop();
    }
  }

  // Helper method for updating the player's position
  private void updatePos(int dir, boolean diagonal) {
    walk(dir, diagonal);
    checkCollision(dir);
  }

  // Verifies if the player intersected with the exit wall
  public boolean reachedExit() {
    Rectangle2D exitWallBounds = getMap().getExit().getBounds2D();
    exitWallBounds.setRect(
      exitWallBounds.getX(),
      exitWallBounds.getY(),
      Math.max(1, exitWallBounds.getWidth()),
      Math.max(1, exitWallBounds.getHeight())
    );
    return getRect().intersects(exitWallBounds);
  }
}
