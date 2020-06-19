import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public abstract class Character {
  // x, y, and speed are relative to blockSize
  private double x, y;
  private final double speed = 0.1;
  private int prevDir = Direction.DOWN;
  private int prevStep = 0;
  private final BufferedImage[][] walk = new BufferedImage[4][9];
  private boolean isMoving = false;
  private final Map map;

  Character(int x, int y, BufferedImage spriteSheet, Map map) {
    this.x = x;
    this.y = y;
    this.map = map;

    // load walking animation of character
    for (int i = 0; i < 4; ++i) {
      for (int j = 0; j < 9; ++j) {
        walk[i][j] = spriteSheet.getSubimage(j * 64, i * 64, 64, 64);
      }
    }
  }

  public abstract void updatePos();

  protected void walk(int dir, boolean diagonal) {
    prevDir = dir;
    move(dir, diagonal);
  }

  protected void stop() {
    isMoving = false;
  }

  protected void move(int dir, boolean diagonal) {
    isMoving = true;

    double moveSpeed = speed;
    if (diagonal) moveSpeed = speed / Math.sqrt(2);

    switch (dir) {
      case Direction.UP:
        y -= moveSpeed;
        break;
      case Direction.DOWN:
        y += moveSpeed;
        break;
      case Direction.LEFT:
        x -= moveSpeed;
        break;
      case Direction.RIGHT:
        x += moveSpeed;
        break;
    }
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  // check if they hit a wall, if so, move them back
  protected void checkCollision(int dir, boolean diagonal) {
    Rectangle myRect = getRect();

    // Check collision with walls
    Line2D.Double[] walls = map.getWalls();
    if (walls == null) {
      move(Direction.getOpposite(dir), diagonal);
      return;
    }

    for (Line2D.Double wall : walls) {
      Rectangle2D wallBounds = wall.getBounds2D();
      double width = Math.max(1, wallBounds.getWidth());
      double height = Math.max(1, wallBounds.getHeight());
      double x = wallBounds.getX();
      double y = wallBounds.getY();

      wallBounds.setRect(x, y, width, height);
      if (myRect.intersects(wallBounds)) {
        move(Direction.getOpposite(dir), diagonal);
        return;
      }
    }
  }

  public void updateImage() {
    if (!isMoving || prevStep == 8) prevStep = -1;
    prevStep++;
  }

  public BufferedImage getImage() {
    return walk[prevDir][prevStep];
  }

  public void drawImage(Graphics g) {
    g.drawImage(
      getImage(),
      (int) (getX() * map.getBlockWidth() + map.getBlockWidth()/6),
      (int) (getY() * map.getBlockHeight() + map.getBlockHeight()/6),
      (int) (map.getBlockWidth()*2/3),
      (int) (map.getBlockHeight()*2/3),
      null
    );
  }

  public Rectangle getRect() {
    double width = map.getBlockWidth();
    double height = map.getBlockHeight();

    double hitboxWidth = width * 0.4;
    double hitboxHeight = height * 0.55;
    double hitboxXOffset = width / 2 - hitboxWidth / 2;
    double hitboxYOffset = height / 2 - hitboxHeight * 0.35;

    return new Rectangle(
      (int) (x * width + hitboxXOffset),
      (int) (y * height + hitboxYOffset),
      (int) (hitboxWidth),
      (int) (hitboxHeight)
    );
  }

  protected Map getMap() {
    return map;
  }

  protected int getPrevDir() {
    return prevDir;
  }

  protected double getSpeed() {
    return speed;
  }
}
