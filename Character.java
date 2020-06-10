import java.awt.image.BufferedImage;
import java.awt.*;

public abstract class Character {
	private int x, y, speed;
	private int prevDir = Direction.DOWN;
	private int prevStep = 0;
	private BufferedImage[][] walk = new BufferedImage[4][9];
	private boolean isMoving = true;
	protected Map map;

	Character(int x, int y, BufferedImage spriteSheet, Map map) {
		this.x = x;
		this.y = y;
		this.map=map;

		for (int i=0;i<4;++i) {
			for (int j=0;j<9;++j) {
				walk[i][j] = spriteSheet.getSubimage(j*64, i*64, 64, 64);
			}
		}

		// set speed relative to frame size
		speed = (int)Math.min(map.getBlockWidth(),map.getBlockHeight())/12;
	}

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

	public void updateSpeed() {
		speed = (int)Math.min(map.getBlockWidth(),map.getBlockHeight())/12;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public BufferedImage getImage() {
		if (!isMoving || prevStep==8) prevStep=-1;
		return walk[prevDir][++prevStep];
	}

	public Rectangle getRect() {
		return new Rectangle(x+18, y+16, 25, 43);
	}
}
