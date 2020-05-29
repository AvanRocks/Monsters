import java.awt.image.BufferedImage;
import java.awt.*;

abstract class Character {
	private final int POS_INCREMENT = 5;

	private int xPos, yPos;
	private int xVel, yVel;
	private int prevDir = Direction.DOWN;
	private int prevStep = 0;
	private BufferedImage[][] walk;

	Character(int x, int y, BufferedImage spriteSheet) {
		xPos=x;
		yPos=y;

		walk = new BufferedImage[4][9];
		for (int i=0;i<4;++i) {
			for (int j=0;j<9;++j) {
				walk[i][j] = spriteSheet.getSubimage(j*64, i*64, 64, 64);
			}
		}
	}

	public void walk(int dir) {
		switch (dir) {
			case Direction.UP:   yVel=-POS_INCREMENT; prevDir = Direction.UP; break;
			case Direction.DOWN: yVel=POS_INCREMENT;  prevDir= Direction.DOWN; break;
			case Direction.LEFT: xVel=-POS_INCREMENT; prevDir=Direction.LEFT; break;
			case Direction.RIGHT:xVel=POS_INCREMENT;  prevDir=Direction.RIGHT; break;
		}
	}

	public void stop(int dir) {
		switch (dir) {
			case Direction.UP:
			case Direction.DOWN: yVel=0; break;
			case Direction.LEFT:
			case Direction.RIGHT:xVel=0; break;
		}
	}

	public int getY() {
		return yPos;
	}

	public int getX() {
		return xPos;
	}

	public void reverseDir() {
		xVel=-xVel;
		yVel=-yVel;
	}

	public BufferedImage getImage() {
		if (xVel==0 && yVel==0) return walk[prevDir][0];
		if (prevStep==8) prevStep=-1;
		return walk[prevDir][++prevStep];
	}

	public Rectangle getRect() {
		return new Rectangle(xPos, yPos, 64, 64);
	}

	public void updatePos() {
		xPos+=xVel;
		yPos+=yVel;
	}

	public int[] getPos() {
		return new int[] {xPos, yPos};
	}

}
