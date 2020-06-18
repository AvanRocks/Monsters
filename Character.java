import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.*;

public abstract class Character {
	// x, y, and speed are relative to blockSize
	private double x, y;
	final private double speed = 0.1;
	private int prevDir = Direction.DOWN;
	private int prevStep = 0;
	private BufferedImage[][] walk = new BufferedImage[4][9];
	private boolean isMoving = false;
	private Map map;

	Character(int x, int y, BufferedImage spriteSheet, Map map) {
		this.x = x;
		this.y = y;
		this.map=map;

		// load walking animation of character
		for (int i=0;i<4;++i) {
			for (int j=0;j<9;++j) {
				walk[i][j] = spriteSheet.getSubimage(j*64, i*64, 64, 64);
			}
		}
	}

	abstract public void updatePos();

	protected void walk(int dir) {
		prevDir = dir;
		move(dir);
	}

	protected void stop() {
		isMoving = false;
	}

	protected void move(int dir) {
		isMoving=true;
		switch (dir) {
			case Direction.UP:    y -= speed; break;
			case Direction.DOWN:  y += speed; break;
			case Direction.LEFT:  x -= speed; break;
			case Direction.RIGHT: x += speed; break;
		}
	}

	public void setX(int x) { this.x=x; }
	public void setY(int y) { this.y=y; }

	public double getX() { return x; }
	public double getY() { return y; }

  // check if they hit a wall, if so, move them back
	protected void checkCollision(int dir) {
	  Rectangle myRect = getRect();

		// Check collision with walls
		Line2D.Double[] walls = map.getWalls();
		if (walls == null) {
				move(Direction.getOpposite(dir));
				return;
		}

		for (Line2D.Double wall : walls) {
			Rectangle2D wallBounds = wall.getBounds2D();
			double width = Math.max(5, wallBounds.getWidth());
			double height = Math.max(5, wallBounds.getHeight());
			double x = wallBounds.getX();
			double y = wallBounds.getY();

			wallBounds.setRect(x, y, width, height);
			if (myRect.intersects(wallBounds)) {
				move(Direction.getOpposite(dir));
				return;
			}
		}

		// Check collision with other characters
		for (Character c : map.getCharacters())
			if (this != c && myRect.intersects(c.getRect()))
          move(Direction.getOpposite(dir));

	}

	public BufferedImage getImage() {
		if (!isMoving || prevStep==8) prevStep=-1;
		return walk[prevDir][++prevStep];
	}

	public void drawImage(Graphics g) {
    g.drawImage(getImage(), (int)(getX() * map.getBlockWidth()), (int)(getY() * map.getBlockHeight()), (int)(map.getBlockWidth()), (int)(map.getBlockHeight()), null);
	}
	

	public Rectangle getRect() {
		double width = map.getBlockWidth();
		double height = map.getBlockHeight();

		double hitboxWidth = width * 0.5;
		double hitboxHeight = height * 0.75;
		double hitboxXOffset = width / 2 - hitboxWidth / 2;
		double hitboxYOffset = height / 2 - hitboxHeight * 0.4;

		// return new Rectangle((int)(x*width+width/3.67), (int)(y*height+height/4.8), (int)(width/2.13), (int)(height/1.3));
		return new Rectangle(
			(int)(x * width + hitboxXOffset),
			(int)(y * height + hitboxYOffset),
			(int)(hitboxWidth),
			(int)(hitboxHeight)
		);
	}

	protected Map getMap() { return map; }

	protected int getPrevDir() {return prevDir;}

	protected double getSpeed() {return speed;}
}
