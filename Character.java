import java.awt.image.BufferedImage;
import java.awt.*;

abstract class Character {
	private int x, y, speed;
	private int prevDir = Direction.DOWN;
	private int prevStep = 0;
	private BufferedImage[][] walk;
	private boolean isMoving = false;

	Character(int x, int y, BufferedImage spriteSheet) {
	  this.x = x;
	  this.y = y;

		walk = new BufferedImage[4][9];
		for (int i=0;i<4;++i) {
			for (int j=0;j<9;++j) {
				walk[i][j] = spriteSheet.getSubimage(j*64, i*64, 64, 64);
			}
		}
	}

	public void walk(int dir) {
		prevDir = dir;
		switch (dir) {
			case Direction.UP:    y -= speed; break;
			case Direction.DOWN:  y += speed; break;
			case Direction.LEFT:  x -= speed; break;
			case Direction.RIGHT: x += speed; break;
		}
	}

	public void stop() {
	  this.isMoving = false;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public BufferedImage getImage() {
	  if (!isMoving) return walk[prevDir][0];
		if (prevStep==8) prevStep=-1;
		return walk[prevDir][++prevStep];
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, 64, 64);
	}
}
