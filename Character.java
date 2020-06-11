import java.awt.image.BufferedImage;
import java.awt.*;

public abstract class Character {
	// x, y, and speed are relative to blockSize
	private double x, y;
	final private double speed = 0.08;
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

	public double getY() {
		return y;
	}

	public double getX() {
		return x;
	}

  // check if they hit a wall, if so, move them back
	protected void checkCollision(int dir) {
	  Rectangle characterRect = getRect();

    for (int y = 0; y < map.getNumRows(); ++y)
      for (int x = 0; x < map.getNumColumns(); ++x)
        if (map.getBlock(x,y) == Map.BlockType.WALL && characterRect.intersects(new Rectangle((int)(x*map.getBlockWidth()), (int)(y*map.getBlockHeight()), (int)map.getBlockWidth()+1, (int)map.getBlockHeight()+1))) {
          move(Direction.getOpposite(dir));
          return;
        }

	}

	public BufferedImage getImage() {
		if (!isMoving || prevStep==8) prevStep=-1;
		return walk[prevDir][++prevStep];
	}

	private Rectangle getRect() {
		double width = map.getBlockWidth();
		double height = map.getBlockHeight();
		return new Rectangle((int)(x*width+width/3.67), (int)(y*height+height/4.8), (int)(width/2.13), (int)(height/1.3));
	}

	protected Map getMap() { return map; }

	protected int getPrevDir() {return prevDir;}

	protected double getSpeed() {return speed;}

